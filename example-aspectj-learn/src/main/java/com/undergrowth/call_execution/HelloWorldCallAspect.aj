package com.undergrowth.call_execution;


/**
 * ${Description}
 * @author zhangwu
 * @date 2018-12-20-10:52
 * @version 1.0.0
 */

public aspect HelloWorldCallAspect {

    /**
     * Entering : HelloWorldCall.java:18
     in the main method  i = 5
     */
    // pointcut HelloWorldPointCut() : call(* main(int));
    /**
     * Entering : HelloWorldCall.java:10
     */
    pointcut HelloWorldPointCut() : execution(* main(int));

    before() : HelloWorldPointCut(){
        System.out.println("Entering : " + thisJoinPoint.getSourceLocation());
    }


}
