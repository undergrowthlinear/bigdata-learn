package basic

import java.io.{FileNotFoundException, FileReader, IOException}

/**
  * @description
  * @author zhangwu
  * @date 2017-11-04-9:40
  * @version 1.0.0
  */
object TryCatchFinallyDemo {
  def main(args: Array[String]) {
    try {
      val f = new FileReader("input.txt")
    } catch {
      case ex: FileNotFoundException => {
        println("Missing file exception")
      }
      case ex: IOException => {
        println("IO Exception")
      }
    } finally {
      println("Exiting finally...")
    }
  }
}
