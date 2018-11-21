package com.undergrowth;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.IOUtils;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-11-20-14:35
 */
public class PrepareFasttextDataSet {

    public static void main(String args[]) throws IOException {
        File dir = new File("E:\\iyourcar\\project\\server-services-bdbox\\sql\\series");
        File searchTopSeriesDir = new File(dir, "search_top_word.csv");
        File trainInputFile = new File(dir, "train.txt");
        File testInputFile = new File(dir, "test.txt");
        Set<String> searchTopCarSeriesSet = prepareSearchTopCarSeriesSet(searchTopSeriesDir);
        OutputStream outputStream = null;
        File trainTopDirWrite = new File(dir + "\\" + "train_top.txt");
        File testTopDirWrite = new File(dir + "\\" + "test_top.txt");
        List<String> trainLineList = Files.readAllLines(trainInputFile.toPath(), Charset.forName("utf-8"));
        List<String> testLineList = Files.readAllLines(testInputFile.toPath(), Charset.forName("utf-8"));
        Map<List<String>, File> fileMap = Maps.newConcurrentMap();
        fileMap.put(trainLineList, trainTopDirWrite);
        fileMap.put(testLineList, testTopDirWrite);
        for (Map.Entry<List<String>, File> fileEntry :
            fileMap.entrySet()) {
            outputStream = new FileOutputStream(fileEntry.getValue());
            for (String line :
                fileEntry.getKey()) {
                String[] lineArray = line.split("\t");
                if (lineArray.length != 2) {
                    continue;
                }
                String label = lineArray[1];
                try {
                    // __label__沃兰多
                    if (!searchTopCarSeriesSet.contains(label.replace("__label__",""))) {
                        continue;
                    }
                    IOUtils.write(line+"\n", outputStream, Charset.forName("utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            IOUtils.closeQuietly(outputStream);
        }

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