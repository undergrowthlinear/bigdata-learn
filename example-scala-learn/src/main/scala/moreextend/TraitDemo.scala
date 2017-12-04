package moreextend

import scala.collection.mutable.ArrayBuffer

/**
  * @description
  * @author zhangwu
  * @date 2017-11-16-21:15
  * @version 1.0.0
  */
object TraitDemo {

  def main(args: Array[String]) {
    var p: Philos = new Frog
    p.philos()
    println(p)
    val f1 = new Frog
    val f2 = new Frog
    println(f1 < f2)
    //
    val intQueue = new BasicQueue
    intQueue.put(200)
    intQueue.put(300)
    println(intQueue.get())
    println(intQueue.get())
    // 混入多个特质时 越靠右的特质越先起作用
    val t = new BasicQueue with Double with Incrementing with Filting
    t.put(1)
    println(t.get())
    t.put(-1)
    t.put(1)
    println(t.get())
  }

}

// 特质
// 特质就像是具有具体方法的java接口
// 特质不能带有任何类参数
// 类的super都是静态绑定的 特质的super是动态绑定的
// Ordered 特质用于比较
trait Philos {
  def philos(): Unit = {
    println("i consume memory")
  }
}

trait HasLeg

class Animal

// 继承超类 混入特质
// [类型参数]
class Frog extends Animal with Philos with HasLeg with Ordered[Frog] {
  override def toString: String = "green"

  override def philos(): Unit = {
    println("i am green")
  }

  override def compare(that: Frog): Int = {
    println(this.hashCode() + "\t" + that.hashCode())
    this.hashCode() - that.hashCode()
  }
}

abstract class IntQueue {
  def put(x: Int)

  def get(): Int
}

class BasicQueue extends IntQueue {
  private val arrayBuffer = new ArrayBuffer[Int]

  override def put(x: Int): Unit = arrayBuffer += x

  override def get(): Int = arrayBuffer.remove(0)
}

// 强大的特质混入功能
// extends IntQueue 表示只能混入其子类
// 特质的super是动态绑定的 所以才能在abstract的时候 传递
// 混入多个特质时 越靠右的特质越先起作用
// 当new 对象实例时 scala将这个类和其继承类以及它的特质以线性的顺序放在一起
// 在调用super的时候 会调用方法链中的所有链
trait Double extends IntQueue {
  abstract override def put(x: Int) {
    super.put(x * 2)
  }
}

trait Incrementing extends IntQueue {
  abstract override def put(x: Int) {
    super.put(x + 1)
  }
}

trait Filting extends IntQueue {
  abstract override def put(x: Int) {
    if (x > 0) super.put(x)
  }
}