package moreextend.abstract_members.abstract_members

/*
 * Copyright (C) 2007-2008 Artima, Inc. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Example code from:
 *
 * Programming in Scala (First Edition, Version 6)
 * by Martin Odersky, Lex Spoon, Bill Venners
 *
 * http://booksites.artima.com/programming_in_scala
 */
// 抽象成员----不完全定义的类或者特质的成员
// 把所有未知的东西都变成抽象成员
object AbsConc {

  def main(args: Array[String]) {
    val abs: Abstract = new Concrete
    display(abs)
  }

  def display(abs: Abstract): Unit = {
    println(abs.initial)
    //println(abs.transform("x"))
    println(abs.current)
  }

  trait Abstract {
    // 抽象类型
    type T
    // 类参数与抽象字段的字段初始化顺序不一致----类参数被传递类构造器前计算,抽象字段是在超类完成了初始化之后才开始计算
    // 解决方案----预初始化字段(new {val n:12} with RationalTrait)/懒加载val(lazy val v:String="x")
    // 抽象常量
    val initial: T
    // 抽象变量
    var current: T

    // 抽象方法
    def transform(x: T): T
  }

  class Concrete extends Abstract {
    type T = String
    val initial = "hi"
    var current = initial

    def transform(x: String) = x + x
  }

}
