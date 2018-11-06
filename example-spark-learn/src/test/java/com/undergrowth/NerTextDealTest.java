package com.undergrowth;

import com.google.common.collect.Lists;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-11-02-9:57
 */
public class NerTextDealTest {

    @Test
    public void stream2Test() throws URISyntaxException, IOException {
        // 428 3207
        Path path = Paths.get(StreamTest.class.getClassLoader().getResource("origindata.txt").toURI());
        Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8);
        Path pathSeries = Paths.get(StreamTest.class.getClassLoader().getResource("series.txt").toURI());
        Stream<String> linesSeries = Files.lines(pathSeries, StandardCharsets.UTF_8);
        lines.collect(Collectors.toList()).forEach(line -> {
            String li = line;
            for (String series :
                linesSeries.collect(Collectors.toList())) {
                li = li.replaceAll(series, "{{car_name:" + series + "}}");
            }
            System.out.println(line);
        });
    }

    @Test
    public void stream3Test() throws URISyntaxException, IOException {
        // 428 3207
        Path path = Paths.get(StreamTest.class.getClassLoader().getResource("origindata.txt").toURI());
        Path pathSeries = Paths.get(StreamTest.class.getClassLoader().getResource("series.txt").toURI());
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        List<String> linesSeries = Files.readAllLines(pathSeries, StandardCharsets.UTF_8);
        List<String> tarnsLine = Lists.newArrayList();
        int i = 0;
        for (String line :
            lines) {
            for (String series :
                linesSeries) {
                if (series.trim().equals("元")) {
                    line = line.replaceAll("[^[:\\d]]" + series, "{{car_name:" + series + "}}");
                } else {
                    line = line.replaceAll("[^:]" + series, "{{car_name:" + series + "}}");
                }
            }
            i++;
            //if(i>5) break;
            System.out.println(line);
            tarnsLine.add(line);
        }
        OutputStream outputStream = new FileOutputStream("trans_origindata.txt");
        IOUtils.writeLines(tarnsLine, null, outputStream);
    }

}