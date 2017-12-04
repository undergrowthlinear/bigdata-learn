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

package moreextend

abstract class BasicCircuitSimulation extends Simulation {
  //  抽象方法
  def InverterDelay: Int

  def AndGateDelay: Int

  def OrGateDelay: Int

  def inverter(input: Wire, output: Wire) = {
    def invertAction() {
      val inputSig = input.getSignal
      // 与柯里化结合 传递执行语句  超级强大
      afterDelay(InverterDelay) {
        output setSignal !inputSig
      }
    }

    input addAction invertAction
  }

  // continued in Listing 18.10...
  // ...continued from Listing 18.9
  def andGate(a1: Wire, a2: Wire, output: Wire) = {
    def andAction() = {
      val a1Sig = a1.getSignal
      val a2Sig = a2.getSignal
      afterDelay(AndGateDelay) {
        output setSignal (a1Sig & a2Sig)
      }
    }

    // 安装信号动作
    a1 addAction andAction
    a2 addAction andAction
  }

  def orGate(o1: Wire, o2: Wire, output: Wire) {
    def orAction() {
      val o1Sig = o1.getSignal
      val o2Sig = o2.getSignal
      afterDelay(OrGateDelay) {
        output setSignal (o1Sig | o2Sig)
      }
    }

    o1 addAction orAction
    o2 addAction orAction
  }

  def probe(name: String, wire: Wire) {
    def probeAction() {
      println(name + " " + currentTime +
        " new-value = " + wire.getSignal)
    }

    wire addAction probeAction
  }

  class Wire {

    private var sigVal = false
    private var actions: List[Action] = List()

    def getSignal = sigVal

    def setSignal(s: Boolean) =
      if (s != sigVal) {
        sigVal = s
        // 对函数的空参数调用简写
        actions foreach (_ ())
      }

    def addAction(a: Action) = {
      actions = a :: actions
      a()
    }
  }
}

abstract class Simulation {

  // 类型成员
  type Action = () => Unit
  // 模拟时间
  private var curtime = 0
  private var agenda: List[WorkItem] = List()

  def currentTime: Int = curtime

  // =>Unit 传名参数 不做计算
  def afterDelay(delay: Int)(block: => Unit) {
    val item = WorkItem(currentTime + delay, () => block)
    agenda = insert(agenda, item)
  }

  def run() {
    afterDelay(0) {
      println("*** simulation started, time = " +
        currentTime + " ***")
    }
    while (!agenda.isEmpty) next()
  }

  // 递归插入元素到合适位置
  private def insert(ag: List[WorkItem],
                     item: WorkItem): List[WorkItem] = {

    if (ag.isEmpty || item.time < ag.head.time) item :: ag
    else ag.head :: insert(ag.tail, item)
  }

  private def next() {
    // 拆分list
    (agenda: @unchecked) match {
      case item :: rest =>
        agenda = rest
        curtime = item.time
        item.action()
    }
  }

  case class WorkItem(time: Int, action: Action)
}


abstract class CircuitSimulation
  extends BasicCircuitSimulation {

  def fullAdder(a: Wire, b: Wire, cin: Wire,
                sum: Wire, cout: Wire) {

    val s, c1, c2 = new Wire
    halfAdder(a, cin, s, c1)
    halfAdder(b, s, sum, c2)
    orGate(c1, c2, cout)
  }

  def halfAdder(a: Wire, b: Wire, s: Wire, c: Wire) {
    val d, e = new Wire
    orGate(a, b, d)
    andGate(a, b, c)
    inverter(c, e)
    andGate(d, e, s)
  }
}

object MySimulation extends CircuitSimulation {
  def InverterDelay = 1

  def AndGateDelay = 3

  def OrGateDelay = 5

  def main(args: Array[String]) {
    val input1, input2, sum, carry = new Wire

    probe("sum", sum)
    probe("carry", carry)
    halfAdder(input1, input2, sum, carry)

    input1 setSignal true
    run()

    input2 setSignal true
    run()
  }
}
