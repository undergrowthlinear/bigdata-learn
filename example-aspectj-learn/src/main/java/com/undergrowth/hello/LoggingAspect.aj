package com.undergrowth.hello;


import org.aspectj.lang.Signature;

/**
 * ${Description}
 * @author zhangwu
 * @date 2018-12-18-19:41
 * @version 1.0.0
 */

public aspect LoggingAspect {

    before(): within(com.undergrowth..*) && execution(public * *(..)) {
        Signature sig = thisJoinPoint.getSignature();
        System.err.println("entering: " + thisJoinPoint);
        System.err.println("toShortString: " + thisJoinPoint.toShortString());
        System.err.println("toLongString: " + thisJoinPoint.toLongString());
        System.err.println("getKind: " + thisJoinPoint.getKind());
        System.err.println("  w/args: " + thisJoinPoint.getArgs());
        System.err.println("  getSignature: " + thisJoinPoint.getSignature());
        System.err.println("      getSourceLocation: " + thisJoinPoint.getSourceLocation());
        System.err.println("      getStaticPart: " + thisJoinPoint.getStaticPart());
    }
}

