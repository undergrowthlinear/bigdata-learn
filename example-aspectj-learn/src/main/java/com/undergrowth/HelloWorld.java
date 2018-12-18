package com.undergrowth;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-12-18-16:55
 */
public class HelloWorld {

    public void say() {
        System.out.println("App say");
    }

    public void sayPar(Integer a,String b) {
        System.out.println("App say \t"+a+"\t"+b);
    }

    public void sayException() {
        throw new RuntimeException("sayException");
    }

    public static void main(String[] args) {
        HelloWorld app = new HelloWorld();
        app.say();
        app.sayPar(1,"2");
        try{
            // 对异常进行拦截 需catch才能拦截
            app.sayException();
        }catch (Exception ex){

        }
    }

}