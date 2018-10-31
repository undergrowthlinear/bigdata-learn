package com.undergrowth;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.junit.Test;

/**
 * ->(lambda)/::(方法引用)
 *
 * 参考 https://blog.csdn.net/bitcarmanlee/article/details/78492164
 *
 * Stream 不是集合元素，它不是数据结构并不保存数据，它是有关算法和计算的，它更像一个高级版本的 Iterator,Stream 就如同一个迭代器（Iterator），单向，不可往复，数据只能遍历一次，遍历过一次后即用尽了，就好比流水从面前流过，一去不复返
 *
 * 并行化(Fork-Join原理来实现)、惰性(Intermediate/Terminal)、短路操作(anyMatch、allMatch、findFirst、findAny、limit)
 *
 * 流相关操作(BaseStream/Stream)
 *
 * filter 过滤true数据,接收{@link Predicate Predicate}
 *
 * map 转换T类型到R,接收{@link Function Function}
 *
 * mapToInt 转换T类型到int,接收{@link ToIntFunction ToIntFunction} mapToLong 转换T类型到long,接收{@link ToLongFunction ToLongFunction} mapToDouble
 * 转换T类型到double,接收{@link ToDoubleFunction ToDoubleFunction}
 *
 * flatMap 先用Function转换T类型到Stream<? extends R>,然后ReferencePipeline负责扁平化,接收{@link Function Function}      flatMapToInt/flatMapToLong/flatMapToDouble
 * 类似于flatMap
 *
 * distinct 返回流中的不同元素,根据{@link Object#equals(Object)})进行判断是否相同
 *
 * sorted 根据元素的自然顺序或者Comparator排序流中元素
 *
 * peek 查看流中元素,返回流,用于调试,接收{@link Consumer Consumer}
 *
 * limit/skip 立即操作,限定/跳过指定数目,返回处理后流数据
 *
 * reduce 执行累加操作,接收{@link BinaryOperator BinaryOperator}
 *
 * collect 将流数据转换为想要的类型,接收{@link Supplier Supplier} ,{@link BiConsumer BiConsumer}或者通过{@link Collector Collector} 进行处理
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-10-31-10:42
 */
public class StreamTest {

    @Test
    public void streamTest() {
        List<Widget> widgets = Lists.newArrayList(new Widget(1, COLOR.RED), new Widget(1, COLOR.RED), new Widget(1, COLOR.BLUE));
        int sum = widgets.stream()
            .filter(w -> w.getColor() == COLOR.RED).map(w -> new Tuple3<COLOR, Integer, Integer>(w.getColor(), w.getWeight(), w.getWeight() * 100))
            .mapToInt(t -> t._3)
            .sum();
        System.out.println(sum);
    }

    @Test
    public void stream2Test() throws URISyntaxException, IOException {
        Path path = Paths.get(StreamTest.class.getClassLoader().getResource("README.md").toURI());
        Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8);
        Stream<String> words = lines.flatMap(line -> Stream.of(line.split(" +")));
        Stream<String> wordsSorted = words.sorted();
        wordsSorted = wordsSorted.peek(System.out::println).limit(10);
        System.out.println("distinct().count:" + wordsSorted.distinct().count());
    }

    @Test
    public void stream3Test() throws URISyntaxException, IOException {
        Path path = Paths.get(StreamTest.class.getClassLoader().getResource("README.md").toURI());
        Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8);
        Stream<String> words = lines.flatMap(line -> Stream.of(line.split(" +")));
        words.forEach(System.out::println);
    }

    @Test
    public void stream4Test() {
        Stream<Integer> integers = Stream.of(10, 20, 30);
        Integer sum = integers.reduce(0, (a, b) -> a + b);
        System.out.println(sum);
    }

    @Test
    public void stream5Test() {
        Stream<String> stringStream = Stream.of("hello", "world");
        List<String> asList = stringStream.collect(ArrayList::new, ArrayList::add,
            ArrayList::addAll);
        asList.forEach(System.out::println);
        //
        stringStream = Stream.of("hello", "world");
        stringStream.collect(Collectors.toList()).forEach(System.out::println);
    }

    @Test
    public void stream6Test() {
        // 1000 93ms 0ms 0ms
        // 10000000 149ms 114ms 8ms
        int end = 50000000;
        LongStream intStream = LongStream.range(1, end);
        long start = System.currentTimeMillis();
        long sum = intStream.parallel().reduce(0, (a, b) -> {
            //System.out.println(Thread.currentThread().getName());
            return a + b;
        });
        System.out.println(sum + "\t intStream.parallel()---->" + (System.currentTimeMillis() - start) + "ms");
        intStream = LongStream.range(1, end);
        start = System.currentTimeMillis();
        sum = intStream.reduce(0, (a, b) -> a + b);
        System.out.println(sum + "\t intStream---->" + (System.currentTimeMillis() - start) + "ms");
        // for 循环
        start = System.currentTimeMillis();
        for (int i = 1; i < end; i++) {
            sum += i;
        }
        System.out.println(sum + "\t for---->" + (System.currentTimeMillis() - start) + "ms");
    }


    class Widget {
        private int weight;
        private COLOR color;

        public Widget(int weight, COLOR color) {
            this.weight = weight;
            this.color = color;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public COLOR getColor() {
            return color;
        }

        public void setColor(COLOR color) {
            this.color = color;
        }
    }

    enum COLOR {
        RED, BLUE;
    }

    class Tuple3<T1, T2, T3> {
        public final T1 _1;
        public final T2 _2;
        public final T3 _3;

        public Tuple3(T1 first,
            T2 second,
            T3 third) {
            this._1 = first;
            this._2 = second;
            this._3 = third;
        }
    }

}