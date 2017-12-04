package moreextend.type_parameterization

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
// 纯函数式队列----返回新对象
// 主构造器 使用 private 表示私有的 eg: class Test private(private val a:Int){} ----只能被类以及伴生对象访问
// 里氏替换原则----需要使用U的地方都可以使用T进行替换,那么T即是U的子类型
// T* ----重复参数标记
// T ---- 类型参数
// 泛型类型默认是非协变的 可加上 +T ----向scala说明进行协变解析/参数化注解
// -T ----逆变----类型反向转换
// 对于类自身来说 自己即是自己的超类型和子类型
// [U>:T]----T是U的下界
// [T<:Ordered[T]] 类型参数T具有上界,T必须是Ordered的子类型
object Queues1 {

  def main(args: Array[String]) {
    val q = new SlowAppendQueue(Nil) append 1 append 2
    println(q)
  }

  class SlowAppendQueue[T](elems: List[T]) {
    // Not efficient
    def head = elems.head

    def tail = new SlowAppendQueue(elems.tail)

    def append(x: T) = new SlowAppendQueue(elems ::: List(x))
  }
}
