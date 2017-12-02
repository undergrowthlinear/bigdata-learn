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


object Ex5 {
  /**
   * A class that calculates a checksum of bytes. This class
   * is not thread-safe.
   */
  class ChecksumAccumulator {
  
    private var sum = 0
  
    /**
     * Adds the passed <code>Byte</code> to the checksum
     * calculation.
     *
     * $@$param b the <code>Byte</code> to add
     */
    def add(b: Byte) { sum += b }
  
    /**
     * Gets a checksum for all the <code>Byte</code>s passed
     * to <code>add</code>. The sum of the integer
     * returned by this method, when added to the
     * sum of all the passed bytes will yield zero.
     */
    def checksum(): Int = ~(sum & 0xFF) + 1
  }

  // In file ChecksumAccumulator.scala
  import scala.collection.mutable.Map
  
  object ChecksumAccumulator {
  
    private val cache = Map[String, Int]()
  
    def calculate(s: String): Int = 
      if (cache.contains(s))
        cache(s)
      else {
        val acc = new ChecksumAccumulator
        for (c <- s)
          acc.add(c.toByte)
        val cs = acc.checksum()
        cache += (s -> cs)
        cs
      }
  }

  def main(args: Array[String]) {
    val sum = ChecksumAccumulator.calculate("hello")
    val sum2 = ChecksumAccumulator.calculate("hello")
    
    println("sum [" + sum + "]")
    println("sum2 [" + sum2 + "]")
  }
}

