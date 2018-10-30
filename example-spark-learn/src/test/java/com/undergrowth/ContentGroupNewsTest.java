package com.undergrowth;

import com.undergrowth.algorithm.itemcf.ContentGroupNews;
import org.junit.Test;

/**
 * hello
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-10-30-17:06
 */
public class ContentGroupNewsTest {

    @Test
    public void dotMul(){
        double[] one={1,2,3};
        double[] two={1,2};
        System.out.println(ContentGroupNews.dot(one,two));
        System.out.println(ContentGroupNews.dot(two,one));
        System.out.println(ContentGroupNews.norm(one));
        System.out.println(ContentGroupNews.norm(two));
    }

}