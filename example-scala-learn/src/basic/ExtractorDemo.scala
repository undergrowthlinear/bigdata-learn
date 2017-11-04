package basic

/**
  * @description
  * @author zhangwu
  * @date 2017-11-04-9:44
  * @version 1.0.0
  */
object ExtractorDemo {

  def main(args: Array[String]) {

    println ("Apply 方法 : " + apply("Zara", "gmail.com"));
    println ("Unapply 方法 : " + unapply("Zara@gmail.com"));
    println ("Unapply 方法 : " + unapply("Zara Ali"));

  }
  // 注入方法 (可选)
  def apply(user: String, domain: String) = {
    user +"@"+ domain
  }

  // 提取方法（必选）
  def unapply(str: String): Option[(String, String)] = {
    val parts = str split "@"
    if (parts.length == 2){
      Some(parts(0), parts(1))
    }else{
      None
    }
  }
}
