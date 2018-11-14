package com.undergrowth;

import com.undergrowth.algorithm.itemcf.ContentRelaArticle;
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
        System.out.println(ContentRelaArticle.dot(one,two));
        System.out.println(ContentRelaArticle.dot(two,one));
        System.out.println(ContentRelaArticle.norm(one));
        System.out.println(ContentRelaArticle.norm(two));
    }

    @Test
    public void timeTest(){
        System.out.println(ContentRelaArticle.UnixTimeSmiler(System.currentTimeMillis()/1000,System.currentTimeMillis()/1000));
    }

}