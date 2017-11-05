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

import scala.actors._

object SillyActor extends Actor {
  def act() { 
    for (i <- 1 to 5) {
      println("I'm acting!")
      Thread.sleep(1000)
    }
  }
}

import scala.actors._

object SeriousActor extends Actor {
  def act() { 
    for (i <- 1 to 5) {
      println("To be or not to be.")
      Thread.sleep(1000)
    }
  }
}

import Actor._

object EchoActor {
  val echoActor = actor {
    while (true) {
      receive {
        case msg =>
          println("received message: "+ msg)
      }
    }
  }
}


object NameResolver extends Actor {
  import java.net.{InetAddress, UnknownHostException}

  def act() { 
    react {
      case (name: String, actor: Actor) =>
        actor ! getIp(name)
        act()
      case "EXIT" =>
        println("Name resolver exiting.")
        // quit
      case msg =>
        println("Unhandled message: "+ msg)
        act()
    }
  }

  def getIp(name: String): Option[InetAddress] = {
    try {
      Some(InetAddress.getByName(name))
    } catch {
      case _:UnknownHostException => None
    }
  }
}

object NameResolverLoop extends Actor {
  import java.net.{InetAddress, UnknownHostException}
  import NameResolver.getIp

  def act() { 
    loop {
      react {
        case (name: String, actor: Actor) =>
          actor ! getIp(name)
        case msg =>
          println("Unhandled message: " + msg)
      }
    }
  }
}

object SillyActor2 {
  val sillyActor2 = actor {
    def emoteLater() {
      val mainActor = self
      actor {
        Thread.sleep(1000)
        mainActor ! "Emote"
      }
    }
  
    var emoted = 0
    emoteLater()
  
    loop {
      react {
        case "Emote" =>
          println("I'm acting!")
          emoted += 1
          if (emoted < 5)
            emoteLater()
        case msg =>
          println("Received: "+ msg)
      }
    }
  }
}

object NameResolver2 {
  import NameResolver.getIp

  import scala.actors.Actor._
  import java.net.{InetAddress, UnknownHostException}
  
  case class LookupIP(name: String, respondTo: Actor)
  case class LookupResult(
    name: String, 
    address: Option[InetAddress]
  )
  
  object NameResolver2 extends Actor {
  
    def act() { 
      loop {
        react {
          case LookupIP(name, actor) =>
            actor ! LookupResult(name, getIp(name))
        }
      }
    }
  
    def getIp(name: String): Option[InetAddress] = {
      // As before (in Listing 30.3)
      try {
        Some(InetAddress.getByName(name))
      } catch {
        case _:UnknownHostException => None
      }
    }
  }
}

object Actors {
  def main(args: Array[String]) {
    SillyActor.start()
    SeriousActor.start()
  }
}
