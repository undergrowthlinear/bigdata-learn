package basic

;

/**
  * ${Description}
  *
  * @author zhangwu
  * @date 2018-05-11-14:45
  * @version 1.0.0
  */

object UpperLowerBound {

  def main(args:Array[String]): Unit ={
   // biophony(Seq(new Bird, new Bird))
    //type arguments problem biophony(Seq(new Earth, new Bird))
   // biophony(Seq(new Earth, new Earth)).map(_.sound())
    biophony(Seq(new Bird, new Bird)).map(_.sound())
  }

  //def biophony[T <: Animal](things: Seq[T]) = things map (_.sound)

  def biophony[T >: Animal](things: Seq[T]) = things
}

class Earth {
  def sound(){
    println("hello !")
  }
}

class Animal extends Earth{
  override def sound() ={
    println("animal sound")
  }
}

class Bird extends Animal{
  override def sound()={
    print("bird sounds")
  }
}


