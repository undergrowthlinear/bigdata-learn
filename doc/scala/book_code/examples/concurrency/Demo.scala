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

object Demo {
  def main(args: Array[String]) {
    val circuit = new Circuit with Adders
    import circuit._

    val ain = new Wire("ain", true)
    val bin = new Wire("bin", false)
    val cin = new Wire("cin", true)
    val sout = new Wire("sout")
    val cout = new Wire("cout")

    probe(ain)
    probe(bin)
    probe(cin)
    probe(sout)
    probe(cout)

    fullAdder(ain, bin, cin, sout, cout)

    circuit.start()
  }
}

