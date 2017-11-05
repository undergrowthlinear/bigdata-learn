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

object Queues5 {
  trait Queue[T] {
    def head: T
    def tail: Queue[T]
    def append(x: T): Queue[T]
  }
  
  object Queue {
  
    def apply[T](xs: T*): Queue[T] = 
      new QueueImpl[T](xs.toList, Nil)
  
    private class QueueImpl[T](
      private val leading: List[T],
      private val trailing: List[T]
    ) extends Queue[T] {
  
      def mirror = 
        if (leading.isEmpty)
          new QueueImpl(trailing.reverse, Nil)
        else 
          this
  
      def head: T = mirror.leading.head
  
      def tail: QueueImpl[T] = {
        val q = mirror
        new QueueImpl(q.leading.tail, q.trailing)
      }
  
      def append(x: T) = 
        new QueueImpl(leading, x :: trailing)
    override def toString() =
        (leading ::: trailing.reverse) mkString ("Queue(", ", ", ")")
    }
  }

  def main(args: Array[String]) {
    val q = Queue[Int]() append 1 append 2
    println(q)
  }
}
