package com.undergrowth.algorithm.itemcf;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.undergrowth.dataalgorithm.PathAppendUtil;
import com.undergrowth.dataalgorithm.Tuple3;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.feature.IDF;
import org.apache.spark.mllib.feature.IDFModel;
import org.apache.spark.mllib.linalg.SparseVector;
import org.apache.spark.mllib.linalg.Vector;
import scala.Tuple2;

/**
 * 基于内容的分组
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-10-30-9:44
 */
public class ContentRelaArticle {

    public static void main(String[] args) {
        // E:\iyourcar\project\server-services-bdbox\sql\\article_id_title_content_all.txt dataout/
        if (args.length != 2) {
            System.err.println("Usage: ContentRelaArticle <input-path> <out-path>");
            System.exit(1);
        }
        String inputFilePath = args[0];
        double sim = 0.3;
        String outputPath = PathAppendUtil.pathAppend(args[1], ContentRelaArticle.class.getSimpleName());
        SparkConf sparkConf = new SparkConf().setMaster("local[4]");
        sparkConf.setAppName("ContentRelaArticle");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        JavaRDD<String> input = jsc.textFile(inputFilePath);
        // 1.过滤字符
        JavaPairRDD<String, Tuple2<String, String>> inputFilterPair = input
            .map((Function<String, List<String>>) v1 -> Lists.newArrayList(v1.split("\001"))).filter(
                (Function<List<String>, Boolean>) v1 -> v1.size() == 3 && StringUtils.isNotBlank(v1.get(0)) && !v1.get(0).equals("\\N") && v1.get(1).length() > 1 && v1.get(2).length() > 1).mapToPair(
                (PairFunction<List<String>, String, Tuple2<String, String>>) strings -> new Tuple2<>(strings.get(0),
                    new Tuple2<>(strings.get(1), strings.get(2))));
       // System.out.println("inputFilter count:" + inputFilterPair.count());
        // 2.分词 (id,(id,title,[word1,word2]))
        JavaPairRDD<Integer, Tuple3<String, String, List<String>>> splitPair = inputFilterPair.mapToPair(
            (PairFunction<Tuple2<String, Tuple2<String, String>>, Integer, Tuple3<String, String, List<String>>>) v1 -> new Tuple2<>(
                Integer.valueOf(v1._1), new Tuple3<String, String, List<String>>(v1._1, v1._2._1,
                Lists.newArrayList(ToAnalysis.parse(v1._2._2).toStringWithOutNature(" ").split(" ")))));
      /*  splitPair.foreach(
            (VoidFunction<Tuple2<Integer, Tuple3<String, String, List<String>>>>) t -> {
                System.out.println("splitPair---->" + t._1 + "\t" + t._2._1 + "\t" + t._2._2);
            });*/
        splitPair.cache();
        Broadcast<Map<Integer, Tuple2<String, String>>> bIdTitleMap = jsc.broadcast(
            splitPair.mapValues((Function<Tuple3<String, String, List<String>>, Tuple2<String, String>>) v1 -> new Tuple2<>(v1._1, v1._2))
                .collectAsMap());
        // 3.计算tf-idf
        HashingTF hashingTF = new HashingTF((int) Math.pow(2, 18));
        JavaPairRDD<Integer, Vector> tfPair = splitPair.mapValues(
            (Function<Tuple3<String, String, List<String>>, Vector>) v1 -> hashingTF.transform(v1._3));
        tfPair.cache();
        IDFModel idf = new IDF().fit(tfPair.values());
        JavaPairRDD<Integer, Vector> tfIdfPair = tfPair.mapValues((Function<Vector, Vector>) v1 -> idf.transform(v1));
        // 计算出文章中每个词的tf-idf
       // tfIdfPair.foreach((VoidFunction<Tuple2<Integer, Vector>>) t -> System.out.println("tfIdfPair---->" + t._1 + "\t" + t._2 + "\t"));
        // 4.计算相似度(使用余弦相似度进行计算 找出前多少特征 然后找出特征相关文章 再计算相关文章相似度)
        //获取前多少特征值 (id,[w1-tfidf,w2-tfidf])
        JavaPairRDD<Integer, List<Tuple2<Integer, Double>>> idFeaturePairTop = tfIdfPair
            .mapValues((Function<Vector, List<Tuple2<Integer, Double>>>) v1 -> {
                int[] index = v1.toSparse().indices();
                double[] value = v1.toSparse().values();
                int topNum = 40;
                List<Tuple2<Integer, Double>> indexValueList = Lists.newArrayList();
                for (int i = 0; i < index.length; i++) {
                    indexValueList.add(new Tuple2<>(index[i], value[i]));
                }
                indexValueList.sort((o1, o2) -> {
                    if (o2._2 > o1._2) {
                        return 1;
                    }
                    if (o2._2 < o1._2) {
                        return -1;
                    }
                    return 0;
                });
                return indexValueList.size() > topNum ? indexValueList.subList(0, topNum) : indexValueList.subList(0, indexValueList.size());
            });
        idFeaturePairTop.cache();
        /*idFeaturePairTop
            .foreach(
                (VoidFunction<Tuple2<Integer, List<Tuple2<Integer, Double>>>>) t -> System.out.println("idFeaturePairTop---->" + t._1 + "\t" + t._2));*/
        // 倒索引 (featureId,[arId1,arId2])
        JavaPairRDD<Integer, Iterable<Integer>> indexReversePair = idFeaturePairTop.flatMapToPair(
            (PairFlatMapFunction<Tuple2<Integer, List<Tuple2<Integer, Double>>>, Integer, Integer>) t -> {
                List<Tuple2<Integer, Integer>> result = Lists.newArrayList();
                t._2.forEach(t2 -> result.add(new Tuple2<>(t2._1, t._1)));
                return result.iterator();
            }).groupByKey();
        indexReversePair.foreach(
            (VoidFunction<Tuple2<Integer, Iterable<Integer>>>) t -> System.out.println("indexReversePair---->" + t._1 + "\t" + t._2));
        // 广播倒排索引
        Broadcast<Map<Integer, Iterable<Integer>>> bIndexReverseMap = jsc.broadcast(indexReversePair.collectAsMap());
        Broadcast<Map<Integer, Vector>> bTfIdfPairMap = jsc.broadcast(tfIdfPair.collectAsMap());
        JavaRDD<Tuple3<Integer, Integer, Double>> articleSim = idFeaturePairTop.flatMap(
            (FlatMapFunction<Tuple2<Integer, List<Tuple2<Integer, Double>>>, Tuple3<Integer, Integer, Double>>) t -> {
                List<Tuple3<Integer, Integer, Double>> result = Lists.newArrayList();
                // 相关文章id列表
                Set<Integer> relArticleIdSet = Sets.newHashSet();
                // 相关文章id 的tf-idf
                List<Tuple2<Integer, Vector>> relTfIdfList = Lists.newArrayList();
                t._2.forEach(t2 -> bIndexReverseMap.value().get(t2._1).forEach(aId -> relArticleIdSet.add(aId)));
                // 移除自身
                if (relArticleIdSet.contains(t._1)) {
                    relArticleIdSet.remove(t._1);
                }
                // 相关文章的tf-idf
                relArticleIdSet.forEach(aId -> relTfIdfList.add(new Tuple2<>(aId, bTfIdfPairMap.value().get(aId))));
                SparseVector currentVector = (SparseVector) bTfIdfPairMap.value().get(t._1);
                relTfIdfList.forEach(relT -> {
                   /* Double cosSim = dot(currentVector.indices(),currentVector.values(), relT._2.toSparse().indices(),relT._2.toSparse().values()) / (norm(currentVector.indices(),currentVector.values()) * norm(
                        relT._2.toSparse().indices(),relT._2.toSparse().values()));*/
                    Double cosSim =
                        dot(currentVector.indices(), currentVector.values(), relT._2.toSparse().indices(), relT._2.toSparse().values()) / (
                            norm(currentVector.values()) * norm(
                                relT._2.toSparse().values()));
                    result.add(new Tuple3<>(t._1, relT._1, cosSim));
                });
                return result.iterator();
            }).filter((Function<Tuple3<Integer, Integer, Double>, Boolean>) v1 -> v1._3 > sim);
        articleSim.foreach((VoidFunction<Tuple3<Integer, Integer, Double>>) t -> System.out.println(
            "articleSim---->" + t._1 + "," + bIdTitleMap.value().get(t._1)._2 + "\t" + t._2 + "," + bIdTitleMap.value().get(t._2)._2
                + "\t" + t._3));
        articleSim.saveAsTextFile(outputPath);
        // 5.分组

    }

    /**
     * 参看 https://blog.csdn.net/u012160689/article/details/15341303
     */
    public static double dot(int[] oneIndex, double[] oneValue, int[] secondIndex, double[] secondValue) {
        double sum = 0.0;
        int num = oneIndex.length;
        /*if (oneIndex.length > secondIndex.length) { // 使用较少的矩阵进行计算
            num = secondIndex.length;
        }*/
        for (int i = 0; i < num; i++) {
            int index = oneIndex[i];
            double secValue = findSec(index, secondIndex, secondValue);
            sum += oneValue[i] * secValue;
        }
        return sum;
    }

    private static double findSec(int index, int[] secondIndex, double[] secondValue) {
        double secValue = 0;
        for (int i = 0; i < secondIndex.length; i++) {
            if (index == secondIndex[i]) {
                return secondValue[i];
            }
        }
        return secValue;
    }

    public static double norm(int[] oneIndex, double[] oneValue) {
        return Math.sqrt(dot(oneIndex, oneValue, oneIndex, oneValue));
    }

    public static double dot(double[] oneValue, double[] secondValue) {
        double sum = 0.0;
        int num = oneValue.length;
        if (oneValue.length > secondValue.length) { // 使用较少的矩阵进行计算
            num = secondValue.length;
        }
        for (int i = 0; i < num; i++) {
            sum += oneValue[i] * secondValue[i];
        }
        return sum;
    }

    public static double norm(double[] oneValue) {
        return Math.sqrt(dot(oneValue, oneValue));
    }

    /**
     * 标签相同自然文章比较相关，所以标签是重要的一个因素，所以标签在相似度计算中权重要大一些，比如一篇文章 标签完全相同，我计为10，分类完全相同我计为1，时间完全相同我计为1，计算两组标签的相似度算法，在上一篇文章中有介绍
     *
     * @param time1
     * @param time2
     * @return
     */
    public static final double MonthSecond = 60 * 60 * 24 * 30;

    public static double UnixTimeSmiler(Long time1, Long time2) {
        double d = (double) Math.abs(time1 - time2) / MonthSecond;
        return d > 1 ? 0 : 1 - d;
    }

}