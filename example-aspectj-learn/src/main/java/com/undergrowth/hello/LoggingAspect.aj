package com.undergrowth.hello;


/**
 * ${Description}
 * @author zhangwu
 * @date 2018-12-18-19:41
 * @version 1.0.0
 */

public aspect LoggingAspect {

    before(): within(com.undergrowth..*) && execution(public * *(..)) {
        System.err.println("entering: " + thisJoinPoint);
        System.err.println("  w/args: " + thisJoinPoint.getArgs());
        System.err.println("      at: " + thisJoinPoint.getSourceLocation());
    }
}

