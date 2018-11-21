package com.undergrowth;

import com.google.common.collect.Sets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.io.IOUtils;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-11-20-10:44
 */
public class PrepareTHUCTCDataSet {

    public static void main(String args[]) throws IOException {
        File dir = new File("E:\\iyourcar\\project\\server-services-bdbox\\sql\\series");
        File carSeriesDir = new File(dir, "carseriesname_count.txt");
        File searchTopSeriesDir = new File(dir, "search_top_word.csv");
        File outputDir = new File(dir, "output");
        File outputSimpleDir = new File(dir, "output_simple");
        File inputFile = new File(dir, "tmp_dwd_base_article_carseriesname.txt");
        Set<String> carSeriesSet = prepareCarSeriesSet(carSeriesDir);
        Set<String> searchTopCarSeriesSet = prepareSearchTopCarSeriesSet(searchTopSeriesDir);
        boolean isSimple = true;
        int num = 0;
        OutputStream outputStream = null;
        List<String> lineList = Files.readAllLines(inputFile.toPath(), Charset.forName("utf-8"));
        for (String line :
            lineList) {
            String[] lineArray = line.split("\\001");
            if (lineArray.length != 4) {
                continue;
            }
            String label = lineArray[1];
            String firstLabel = label.split(",")[0].split(" ")[0];
            String content = lineArray[3];
            if (!content.contains(firstLabel) || !carSeriesSet.contains(firstLabel)) {
                continue;
            }

            try {
                File dirWrite = null;
                if (!isSimple) {
                    dirWrite = new File(outputDir + "\\" + firstLabel);
                } else {
                    dirWrite = new File(outputSimpleDir + "\\" + firstLabel);
                    // 只输出top的内容
                    if (!searchTopCarSeriesSet.contains(firstLabel)) {
                        continue;
                    }
                }
                if (!dirWrite.exists()) {
                    dirWrite.mkdirs();
                }
                outputStream = new FileOutputStream(new File(dirWrite, UUID.randomUUID().toString() + ".txt"));
                IOUtils.write(content, outputStream, Charset.forName("utf-8"));
                num++;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(outputStream);
            }
        }
        System.out.println("所有总的数目:" + num);
    }

    private static Set<String> prepareSearchTopCarSeriesSet(File searchTopSeriesDir) throws IOException {
        Set<String> content = Sets.newHashSet();
        List<String> lineList = Files.readAllLines(searchTopSeriesDir.toPath(), Charset.forName("utf-8"));
        for (String line :
            lineList) {
            String[] lineArray = line.split(",");
            if (lineArray.length != 4) {
                continue;
            }
            content.add(lineArray[1]);
        }
        return content;
    }

    private static Set<String> prepareCarSeriesSet(File carSeriesDir) throws IOException {
        Set<String> content = Sets.newHashSet();
        List<String> lineList = Files.readAllLines(carSeriesDir.toPath(), Charset.forName("utf-8"));
        for (String line :
            lineList) {
            String[] lineArray = line.split("\\001");
            if (lineArray.length != 2) {
                continue;
            }
            if (Integer.valueOf(lineArray[1]) < 5) {
                continue;
            }
            content.add(lineArray[0]);
        }
        return content;
    }

}