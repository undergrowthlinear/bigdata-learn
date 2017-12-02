package scalainprogramming.moreextend

/**
  * @description
  * @author zhangwu
  * @date 2017-11-15-21:11
  * @version 1.0.0
  */
object ExtendCompositeDemo {

  // java4个命名空间 字段、方法、包、类型
  // scala2个命名空间 字段/方法/包/单例对象  类/特质

  def display(element: Element) = {
    println(element.height+"\t"+element.width)
    element.contents.foreach((line:String)=>print(line+"\t"))
    println
    element.printWhat
  }

  def main(args: Array[String]) {
    var element:Element=new ArrayElement(Array("hello","world"))
    display(element)
    element=new LineElement("hello"+"world")
    display(element)
    // 多态
    element=new Uniform('q',10,20)
    display(element)
    var element2=new Uniform('w',10,20)
    display(element.above(element2))
    display(element.beside(element2))
    // 工厂方法创建
    display(Element.elem("scala world"))
    display(Element.elem(Array("scala","world")))
    display(Element.elem('s',10,20))
   /* val c =new Tiger(true,19)
    println(c.dangerous)*/
  }

}
// 抽象类
abstract class Element{
  // 无参数方法 与 空括号方法
  // 无参数方法 以可读方式访问对象属性/即方法没有副作用
  def contents:Array[String]
  def height:Int=contents.length
  def width:Int=if (height==0) 0 else contents(0).length
  def printWhat={
    println(getClass.getCanonicalName)
  }
  // ++ 两数组相加
  def above(other:Element):Element={
    new ArrayElement(this.contents ++ other.contents)
  }
  // zip 数组的二元操作
  def beside(other:Element):Element={
    val z:Array[String]=for(
      (line1,line2)<-this.contents zip other.contents
    ) yield line1+line2
    new ArrayElement(z)
  }

  // 补宽
  def widen(w:Int):Element={
    if(w<=width) this else {
      val left=Element.elem(' ',(w-width)/2,height)
      val right=Element.elem(' ',w-width-left.width,height)
      left beside this beside right
    }
  }
  // 补高
  def highten(h:Int):Element={
    if(h<=height) this else {
      val top=Element.elem(' ',width,(h-height)/2)
      val down=Element.elem(' ',width,(h-height-top.height))
      top.above(this).above(down)
    }
  }

  override def toString: String = this.contents.mkString("\n")
}

// 工厂方法
object Element{
  def elem(arr:Array[String]):Element={
    new ArrayElement(arr)
  }
  def elem(c:Char,height:Int,width:Int):Element={
    new Uniform(c,height,width)
  }
  def elem(s:String):Element={
    new LineElement(s)
  }
}

// ArrayElement为子类 Element为超类
// final 可防止类继承 或者 方法被重写
class ArrayElement(arr:Array[String]) extends Element{
  // 无参数方法 与 空括号方法
  override def contents: Array[String] = arr
}

// 调用超类构造器
// 子类重写父类abstract方法 可不使用override,若是具体方法,则必须使用override
class LineElement(s:String) extends ArrayElement(Array(s)){
  override def height: Int = 1
  override def width: Int = s.length
}
// 字段与无参方法可等同看待
class Uniform(ch:Char,override val height:Int,override val width:Int) extends Element{
  // 无参数方法 与 空括号方法
  val line=ch.toString*width
  override def contents: Array[String] = Array.fill(height)(line)
}

class Cat{
  val dangerous=false
}
// 参数化字段
class Tiger(override val dangerous:Boolean,private val age:Int) extends Cat{

}
