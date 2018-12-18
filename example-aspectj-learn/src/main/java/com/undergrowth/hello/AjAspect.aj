
package com.undergrowth.hello;

import com.undergrowth.HelloWorld;

/**
 * ${Description}
 *
 * ajc编译器编译时织入
 *  会将此转换为注解方式
 * @author zhangwu
 * @date 2018-12-18-16:53
 * @version 1.0.0
 */
public aspect  AjAspect {

    // 切入点
    // pointcut <pointcut name>(par):
    //                          execution(<optional modifier> <return type> <class>.<method>(<param types>))
    //                          * mypack..*.*(..) mypack包以及任意子包中的所有方法
    // args handler target get set new cflow this
    // && ! ||
    // before / after / around / after returning/ after throwing
    // declare precedence 通知优先级
    //  aspect  AjAspect issingleton() / perthis/ pertarget / percflow
    // 继承切入点 继承方面
    pointcut say():
        execution(* com.undergrowth.HelloWorld.say(..));
    pointcut sayPar(Integer a, String b):
        execution(* com.undergrowth.HelloWorld.sayPar(Integer,String)) && args(a,b);
    pointcut sayParHelloWorld(HelloWorld helloObject):
        execution(* com.undergrowth.HelloWorld.sayPar(..)) && target(helloObject);
    pointcut sayException(Exception ex):
        handler(Exception) && args(ex);
    // 前置通知
    before(): say() {
        System.out.println("===================================================");
        System.out.println("AjAspect before say");
    }
    // 前置通知 获取参数
    before(Integer a, String b): sayPar(a,b) {
        System.out.println("===================================================");
        System.out.println("AjAspect before sayPar \t"+a+"\t"+b);
    }
    // 前置通知 获取对象
    before(HelloWorld helloObject): sayParHelloWorld(helloObject) {
        System.out.println("===================================================");
        System.out.println("AjAspect before sayParHelloWorld \t"+helloObject);
    }
    // 前置通知 获取异常
    before(Exception ex): sayException(ex) {
        System.out.println("===================================================");
        System.out.println("AjAspect before sayException \t"+ex.getMessage());
    }
    // 后置通知
    after(): say() {
        System.out.println("AjAspect after say");
    }
}
