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

object Files {
  val filesHere = (new java.io.File(".")).listFiles
  

  def printFiles() {
    for (file <- filesHere)
      println(file)
  }

  def printFilesIter() {
    // Not common in Scala...
    for (i <- 0 to filesHere.length - 1)
      println(filesHere(i))
  }

  def printScalaFiles() {
    val filesHere = (new java.io.File(".")).listFiles
    
    for (file <- filesHere if file.getName.endsWith(".scala"))
      println(file)
  }

  def printScalaFiles2() {
    for (file <- filesHere)
      if (file.getName.endsWith(".scala"))
        println(file)
  }

  def printScalaFiles3() {
    for (
      file <- filesHere
      if file.isFile;
      if file.getName.endsWith(".scala")
    ) println(file)
  }

  def fileLines(file: java.io.File) = 
    scala.io.Source.fromFile(file).getLines.toList
  

  def grepParens(pattern: String) {
    def grep(pattern: String) =
      for (
        file <- filesHere
        if file.getName.endsWith(".scala");
        line <- fileLines(file)
        if line.trim.matches(pattern) 
      ) println(file +": "+ line.trim)
    
    grep(pattern)
  }

  def grepGcd() {
    def grep(pattern: String) = grepParens(pattern)
    grep(".*gcd.*")
  }

  def grepGcd2() {
    def grep(pattern: String) =
      for {
        file <- filesHere
        if file.getName.endsWith(".scala")
        line <- fileLines(file)
        trimmed = line.trim
        if trimmed.matches(pattern)  
      } println(file +": "+ trimmed)
    
    grep(".*gcd.*")
  }

  def scalaFiles =
    for {
      file <- filesHere
      if file.getName.endsWith(".scala")
    } yield file

  val forLineLengths =
    for {
      file <- filesHere
      if file.getName.endsWith(".scala")
      line <- fileLines(file)
      trimmed = line.trim
      if trimmed.matches(".*for.*")  
    } yield trimmed.length
}

Files.printFiles()
Files.printFilesIter()
Files.printScalaFiles()
Files.printScalaFiles2()
Files.printScalaFiles3()
Files.grepParens(".*asdf.*")
Files.grepGcd()
Files.grepGcd2()
println("Files.scalaFiles.toList [" + Files.scalaFiles.toList + "]")
println("Files.forLineLengths.toList [" + Files.forLineLengths.toList + "]")
