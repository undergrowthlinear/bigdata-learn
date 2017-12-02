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

object Misc {
  object Obj1 {
    abstract class List[+T] {
        def isEmpty: Boolean
        def head: T
        def tail: List[T]

      def length: Int = 
        if (isEmpty) 0 else 1 + tail.length

      def drop(n: Int): List[T] = 
        if (isEmpty) Nil
        else if (n <= 0) this
        else tail.drop(n - 1)

      def ::[U >: T](x: U): List[U] = new ::(x, this)

        def :::[U >: T](prefix: List[U]): List[U] = 
          if (prefix.isEmpty) this
          else prefix.head :: prefix.tail ::: this

      def map[U](f: T => U): List[U] =
        if (isEmpty) Nil
        else f(head) :: tail.map(f)
    }

    case object Nil extends List[Nothing] {
      override def isEmpty = true
      def head: Nothing =
        throw new NoSuchElementException("head of empty list")
      def tail: List[Nothing] =
        throw new NoSuchElementException("tail of empty list")
    }

    final case class ::[T](hd: T, tl: List[T]) extends List[T] {
      def head = hd
      def tail = tl
      override def isEmpty: Boolean = false
    }
  }

  object Obj2 {
    abstract class List[+T] {
        def isEmpty: Boolean
        def head: T
        def tail: List[T]
    }

    final case class ::[T](head: T, tail: List[T])
        extends List[T] {
    
      override def isEmpty: Boolean = false
    }
  }

  object Obj3 {
    def incAll(xs: List[Int]): List[Int] = xs match {
      case List() => List()
      case x :: xs1 => x + 1 :: incAll(xs1)
    }

    def incAll2(xs: List[Int]): List[Int] = {
      var result = List[Int]()    // a very inefficient approach
      for (x <- xs) result = result ::: List(x + 1)
      result
    }

    import scala.collection.mutable.ListBuffer

    def incAll3(xs: List[Int]): List[Int] = {
      val buf = new ListBuffer[Int]
      for (x <- xs) buf += x + 1
      buf.toList
    }
  }

  object Obj4 {
    abstract class Buffer[T] {
      def toList: List[T]
    }

    final class ListBuffer[T] extends Buffer[T] {
      private var start: List[T] = Nil
      private var last0: ::[T] = _
      private var exported: Boolean = false
      override def toList: List[T] = {
        exported = !start.isEmpty
        start
      }
    }
  }

  def main(args: Array[String]) {
    println("Obj3.incAll(List(2, 3)) [" + Obj3.incAll(List(2, 3)) + "]")
    println("Obj3.incAll2(List(2, 3)) [" + Obj3.incAll2(List(2, 3)) + "]")
    println("Obj3.incAll3(List(2, 3)) [" + Obj3.incAll3(List(2, 3)) + "]")

    val xs = List(0)
    val ys = 1 :: xs
    val zs = 2 :: xs
    println("ys [" + ys + "]")
    println("zs [" + zs + "]")
  }
}

package scala {
  object Obj5 {
    abstract class List[+T] {
        def isEmpty: Boolean
        def head: T
        def tail: List[T]
    }

    final case class ::[U](hd: U, 
        private[scala] var tl: List[U]) extends List[U] {
    
      def head = hd
      def tail = tl
      override def isEmpty: Boolean = false
    }
  }
}

