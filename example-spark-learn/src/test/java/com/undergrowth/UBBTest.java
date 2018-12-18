package com.undergrowth;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * 基于用户的分桶测试
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-11-14-16:33
 */
public class UBBTest {

    @Test
    public void ubbTest() throws IOException {
        /*String[] divisionParamArray = {"1_55b76c098e477edc071868d1", "1_55b76c0a8e477edc071868d2", "2_5813207e64d2935fdf98a127",
            "2_58132d5764d2935fdf98a129", "8_5a86efbbe4b0670f404720f5"};*/

        File dir = new File("E:\\iyourcar\\project\\server-services-bdbox\\sql\\user_account");
        for (String fileName :
            dir.list()) {
            List<String> uidLine = Files
                .readLines(new File(dir, fileName), Charsets.UTF_8);
            long start = System.currentTimeMillis();
            long result = 0;
            int lessE50 = 0;
            int thanE50 = 0;
            for (String divisionParam :
                uidLine) {
                if (StringUtils.isEmpty(divisionParam)) {
                    continue;
                }
                long numForHash = jsHash(divisionParam);
                numForHash = ((numForHash + 10000000000L) * (199 + 10000000000L) * 0x9e370001L) >> 24;
                result = (1 + Math.abs(((int) numForHash) % 100));
                if (result <= 50) {
                    lessE50 += 1;
                } else {
                    thanE50 += 1;
                }
                //  System.out.println(divisionParam + "\t" + result);
            }
            long timeConsumer = System.currentTimeMillis() - start;
            System.out.println(
                fileName + "\t 总数:" + (lessE50 + thanE50) + "\t 桶1:" + lessE50 + "\t 桶2:" + thanE50 + "\t 总时间消耗:" + timeConsumer + "ms \t 单次时间消耗:" + (timeConsumer) / (lessE50
                    + thanE50) + "ms");
        }
    }

    @Test
    public void ubb2Test() throws IOException {
        /**
         */
        File dir = new File("E:\\iyourcar\\project\\server-services-bdbox\\sql\\user_account");
        for (String fileName :
            dir.list()) {
            List<String> uidLine = Files
                .readLines(new File(dir, fileName), Charsets.UTF_8);
            long result = 0;
            int lessE50 = 0;
            int thanE50 = 0;
            for (String divisionParam :
                uidLine) {
                if (StringUtils.isEmpty(divisionParam)) {
                    continue;
                }
                long numForHash = DigestUtils.md5(divisionParam).hashCode();
                result = (1 + Math.abs(((int) numForHash) % 100));
                if (result <= 50) {
                    lessE50 += 1;
                } else {
                    thanE50 += 1;
                }
                //  System.out.println(divisionParam + "\t" + result);
            }
            System.out.println(fileName + "\t" + (lessE50 + thanE50) + "\t" + lessE50 + "\t" + thanE50);
        }
    }

    private int jsHash(String divisionParam) {
        int hash = 1315423911;
        for (int i = 0; i < divisionParam.length(); i++) {
            hash ^= ((hash << 5) + divisionParam.charAt(i) + (hash >> 2));
        }
        return (hash & 0x7FFFFFFF);
    }

}