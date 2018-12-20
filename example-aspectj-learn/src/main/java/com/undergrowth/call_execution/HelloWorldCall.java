package com.undergrowth.call_execution;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-12-20-10:52
 */
public class HelloWorldCall {

    public static void main(int i){
        System.out.println("in the main method  i = " + i);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        main(5);
    }

}