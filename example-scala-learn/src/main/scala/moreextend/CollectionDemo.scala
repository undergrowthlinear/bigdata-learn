package moreextend

import scala.collection.immutable.{TreeMap, TreeSet}
import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
  * @description
  * @author zhangwu
  * @date 2017-11-24-23:28
  * @version 1.0.0
  */
object CollectionDemo {

  def mapMaker: mutable.Map[String, Int] = {
    new mutable.HashMap[String, Int]() with mutable.SynchronizedMap[String, Int]
  }

  // 提供可变与不可变版本----默认为不可变版本
  // 不可变与可变版本之前的切换 使用=更易于切换
  // iterable----iterator----与java类似
  // seq(array/list/queue/stack) set(hashset) map(hashmap)
  def main(args: Array[String]) {
    set_demo()
    list_demo()
    //
    println(hasUpper("what"))
    println(hasUpper("What"))
    //
    map_demo()
    //
    countNum("hello world scala world").foreach(println)
    // 相互转换
    trans_demo()
    // 元组使用
    tuple_demo()
  }

  def set_demo(): Unit = {
    var s = Set(1, 2, 3, 2)
    s += 10
    s -= 10
    s.foreach(println)
    s.iterator.foreach(println)
  }

  def list_demo(): Unit = {
    // 可变
    val lbufffer = new ListBuffer[Int]
    lbufffer += 4
    lbufffer += 3
    (1 +: lbufffer).foreach(println)
    // 可变数组
    val arrayBuffer = new ArrayBuffer[Int]()
    arrayBuffer += 1
    // 队列
    val queue = new mutable.Queue[Int]()
    queue.enqueue(20)
    println(queue.dequeue())
    println(queue.isEmpty)
    // 栈
    val stack = new mutable.Stack[Int]()
    stack.push(20)
    println(stack.top)
    println(stack.pop())
    // richString
  }

  def hasUpper(s: String) = s.exists(_.isUpper)

  // 类似于数组使用
  def map_demo(): Unit = {
    val map = mutable.Map("q" -> 1, "w" -> 10)
    map.foreach(println)
    // 有序的set map
    val treeSet = TreeSet(10, 20, 3, 9, 1, 200)
    treeSet.foreach(println)
    val treeMap = TreeMap(1 -> 100, 2 -> 300, 0 -> 19)
    treeMap.foreach(println)
    // 同步

  }

  // 统计单词的使用次数
  def countNum(text: String): mutable.Map[String, Int] = {
    val count = mutable.Map.empty[String, Int]
    for (s <- text.split("[ .,!]")) {
      val words = s.toLowerCase
      val num = if (count.contains(words)) count(words) else 0
      count += (words -> (num + 1))
    }
    count
  }

  def trans_demo(): Unit = {
    val map = Map()
    val list = List(1, 200)
    map ++ list
    map.foreach(println)
    map.toList.foreach(println)
  }

  def tuple_demo(): Unit = {
    val (num, word) = maxLength("what you name i undergrowth".split("[ ,.!]"))
    println(num + "\t" + word)
  }

  def maxLength(text: Array[String]) = {
    var word = text(0)
    var num = 0
    for (i <- 1 until text.length) {
      if (text(i).length > word.length) {
        word = text(i)
        num = i
      }
    }
    (num, word)
  }
}
