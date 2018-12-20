package com.undergrowth.preinit_init;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-12-20-11:29
 */
public class Service extends SuperService {
    private String name;
    private int age;

    static {
        System.out.println("Static 静态代码块...");
    }

    public Service(String name) {
        super(name);
        this.name = name;
    }

    public Service(int age) {
        super("Gavin");
        this.age = age;
    }

    public Service(String name, int age) {
        super(name);
        this.name = name;
        this.age = age;
    }

    public void test(int a, float b) {
        System.out.println("a + b = " + (a + b));
    }

    @Override
    public String toString() {
        return "Service{" +
            "name='" + name + '\'' +
            ", age=" + age +
            '}';
    }
}