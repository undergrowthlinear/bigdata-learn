package com.undergrowth.preinit_init;


/**
 * ${Description}
 * @author zhangwu
 * @date 2018-12-20-11:30
 * @version 1.0.0
 */
public aspect ConstructorAspect {
    pointcut constructorCallPointcut(): call(SuperService+.new(..));

    pointcut constructorExecutionPointcut():execution(SuperService+.new(..));

    pointcut initializationPointcut(): initialization(SuperService+.new(..));

    pointcut preInitializationPointcut(): preinitialization(SuperService+.new(..));

    pointcut staticInitializationPointcut(): staticinitialization(SuperService+);

    before(): constructorCallPointcut(){ // 1次
        System.out.println();
        System.out.println("调用Service类构造函数之前...");
        System.out.println("Signature: " + thisJoinPoint.getSignature());
        System.out.println("Source Line: " + thisJoinPoint.getSourceLocation());
    }

    before(): constructorExecutionPointcut(){ // 2次
        // When the body of code for an actual constructor executes, after its this or super constructor call
        // 在子类构造器与子类对象初始化(父级初始化)之间又嵌入了一层拦截
        System.out.println();
        System.out.println("执行" + thisJoinPoint.getThis().getClass() + "类的构造函数之前...");
        System.out.println("Signature: " + thisJoinPoint.getSignature());
        System.out.println("Source Line: " + thisJoinPoint.getSourceLocation());
    }

    before(): initializationPointcut(){ // 2次 一次为object父类调用
        System.out.println();
        System.out.println("initializationPointcut在这里...");
        System.out.println("Signature: " + thisJoinPoint.getSignature());
        System.out.println("Source Line: " + thisJoinPoint.getSourceLocation());
    }

    before(): preInitializationPointcut(){ // 2次 一次为object父类调用
        System.out.println();
        System.out.println("preInitializationPointcut在这里...");
        System.out.println("Signature: " + thisJoinPoint.getSignature());
        System.out.println("Source Line: " + thisJoinPoint.getSourceLocation());
    }

    after(): staticInitializationPointcut(){ // 2次
        System.out.println();
        System.out.println("staticInitializationPointcut在这里...");
        System.out.println("Signature: " + thisJoinPoint.getSignature());
        System.out.println("Source Line: " + thisJoinPoint.getSourceLocation());
    }
}
