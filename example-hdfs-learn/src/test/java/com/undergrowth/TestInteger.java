package com.undergrowth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.junit.Test;

/**
 * 测试Integer
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-06-05-14:10
 */
public class TestInteger {

    @Test
    public void testInteger() {
        int num7 = 7;
        testNum(num7);
        int num10000 = 10000;
        testNum(num10000);
    }

    @Test
    public void testDate() throws ParseException {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(format.parse("2018-07-30 00:00:00").getTime());
        System.out.println(format.parse("2018-07-30 23:59:59").getTime());
    }

    private void testNum(int num7) {
        Integer num = num7;
        Integer num2 = num7;
        System.out.println(num == num7);
        System.out.println(num == num2);
        System.out.println(num.equals(num2));
    }

}