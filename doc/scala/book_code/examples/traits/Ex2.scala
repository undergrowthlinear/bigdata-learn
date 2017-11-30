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

object Ex2 {

  def main(args: Array[String]) {
    (new Frog).philosophize()

    // Multiple inheritance thought experiment
    val q = new BasicIntQueue with Incrementing with Doubling
    q.put(42) // which put would be called?
    println("q [" + q + "]")
  }

  trait CharSequence {
    def charAt(index: Int): Char

    def length: Int

    def subSequence(start: Int, end: Int): CharSequence

    def toString(): String
  }

  trait Ordered[T] {
    def compare(that: T): Int

    def <(that: T): Boolean = (this compare that) < 0

    def >(that: T): Boolean = (this compare that) > 0

    def <=(that: T): Boolean = (this compare that) <= 0

    def >=(that: T): Boolean = (this compare that) >= 0
  }

  trait Incrementing extends IntQueue {
    abstract override def put(x: Int) {
      super.put(x + 1)
    }
  }

  trait Doubling extends IntQueue {
    abstract override def put(x: Int) {
      super.put(2 * x)
    }
  }

  import scala.collection.mutable.ArrayBuffer

  class Animal

  class Frog extends Animal with Philosophical {
    override def philosophize() {
      println("It ain't easy being " + toString + "!")
    }

    override def toString = "green"
  }

  class Point(x: Int, y: Int)

  class BasicIntQueue extends IntQueue {
    private val buf = new ArrayBuffer[Int]

    def get() = buf.remove(0)

    def put(x: Int) {
      buf += x
    }
  }
}
