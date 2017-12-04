package moreextend

/**
  * @description
  * @author zhangwu
  * @date 2017-11-18-10:54
  * @version 1.0.0
  */
object SuiteDemo {

  def main(args: Array[String]) {
    try {
      assert_demo()
    } catch {
      case ex: AssertionError => {
        println(ex)
      }
    }
    try {
      assert1_demo()
    } catch {
      case ex: AssertionError => {
        println(ex)
      }
    }
  }

  def assert_demo(): Unit = {
    assert(false, "测试错误")
  }

  def assert1_demo(): Unit = {
    assert(3 == 2)
  }

}

// import org.scalatest.FunSuite
/*class ElementSuite extends FunSuite {

}*/
