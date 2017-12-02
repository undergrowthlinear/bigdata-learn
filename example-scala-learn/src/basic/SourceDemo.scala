package basic

import java.io.{File, PrintWriter}

import scala.io.Source

/**
  * @description
  * @author zhangwu
  * @date 2017-11-04-13:47
  * @version 1.0.0
  */
object SourceDemo {

  def write_some() = {
    val writer = new PrintWriter(new File("test.txt" ))

    writer.write("菜鸟教程")
    writer.close()
  }

  def main(args: Array[String]) {
    write_some()
    println("文件内容为:" )
    Source.fromFile("test.txt" ).foreach{
      print
    }
  }

}
