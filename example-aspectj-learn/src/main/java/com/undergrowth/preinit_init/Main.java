package com.undergrowth.preinit_init;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-12-20-11:29
 */
public class Main {

    /**
     * 调用Service类构造函数之前...
     Signature: com.undergrowth.preinit_init.Service(String)
     Source Line: Main.java:11

     staticInitializationPointcut在这里...
     Signature: com.undergrowth.preinit_init.SuperService.<clinit>
     Source Line: SuperService.java:0
     Static 静态代码块...

     staticInitializationPointcut在这里...
     Signature: com.undergrowth.preinit_init.Service.<clinit>
     Source Line: Service.java:13

     preInitializationPointcut在这里...
     Signature: com.undergrowth.preinit_init.Service(String)
     Source Line: Service.java:16

     preInitializationPointcut在这里...
     Signature: com.undergrowth.preinit_init.SuperService(String)
     Source Line: SuperService.java:12

     initializationPointcut在这里...
     Signature: com.undergrowth.preinit_init.SuperService(String)
     Source Line: SuperService.java:12

     执行class com.undergrowth.preinit_init.Service类的构造函数之前...
     Signature: com.undergrowth.preinit_init.SuperService(String)
     Source Line: SuperService.java:12

     initializationPointcut在这里...
     Signature: com.undergrowth.preinit_init.Service(String)
     Source Line: Service.java:16

     执行class com.undergrowth.preinit_init.Service类的构造函数之前...
     Signature: com.undergrowth.preinit_init.Service(String)
     Source Line: Service.java:16
     a + b = 3.5
     * @param args
     */
    public static void main(String[] args) {
        Service service = new Service("Gavin");
        service.test(1, 2.5F);
    }

}