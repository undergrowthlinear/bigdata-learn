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


object Ex6 {
  class Time {
  
    private[this] var h = 12
    private[this] var m = 12
  
    def hour: Int = h
    def hour_= (x: Int) {
      require(0 <= x && x < 24)
      h = x
    }
  
    def minute = m
    def minute_= (x: Int) {
      require(0 <= x && x < 60)
      m = x
    }
  } 

  def main(args: Array[String]) {
    val time = new Time
    println("time.hour [" + (time.hour) + "]")
    println("time.minute [" + (time.minute) + "]")

    time.hour = 13
    time.minute = 14
    println("time.hour [" + (time.hour) + "]")
    println("time.minute [" + (time.minute) + "]")

    try {
      time.minute = 75
    } catch {
      case ex: IllegalArgumentException => println("caught exception")
    }
  }
}
