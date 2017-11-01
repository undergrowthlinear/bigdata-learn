package com.undergrowth.bean;

import java.io.Serializable;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description Person
 * @date 2017-11-01-23:41
 */
public class Person implements Serializable {

  private String name;
  private int age;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }
}

