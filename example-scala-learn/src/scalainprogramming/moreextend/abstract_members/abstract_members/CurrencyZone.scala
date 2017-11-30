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

package scalainprogramming.moreextend.abstract_members.abstract_members

abstract class CurrencyZone {
  type Currency <: AbstractCurrency
  val CurrencyUnit: Currency

  // 工厂方法
  def make(x: Long): Currency

  abstract class AbstractCurrency {
    val amount: Long

    def designation: String

    def +(that: Currency): Currency =
      make(this.amount + that.amount)

    def *(x: Double): Currency =
      make((this.amount * x).toLong)

    def -(that: Currency): Currency =
      make(this.amount - that.amount)

    def /(that: Double) =
      make((this.amount / that).toLong)

    def /(that: Currency) =
      this.amount.toDouble / that.amount

    def from(other: CurrencyZone#AbstractCurrency): Currency =
      make(Math.round(
        other.amount.toDouble * Converter.exchangeRate
        (other.designation)(this.designation)))

    override def toString =
      ((amount.toDouble / CurrencyUnit.amount.toDouble)
        formatted ("%." + decimals(CurrencyUnit.amount) + "f")
        + " " + designation)

    private def decimals(n: Long): Int =
      if (n == 1) 0 else 1 + decimals(n / 10)
  }
}

object US extends CurrencyZone {

  type Currency = Dollar
  val Cent = make(1)
  val Dollar = make(100)
  val CurrencyUnit = Dollar

  def make(cents: Long) = new Dollar {
    val amount = cents
  }

  abstract class Dollar extends AbstractCurrency {
    def designation = "USD"
  }
}

object Europe extends CurrencyZone {

  type Currency = Euro
  val Cent = make(1)
  val Euro = make(100)
  val CurrencyUnit = Euro

  def make(cents: Long) = new Euro {
    val amount = cents
  }

  abstract class Euro extends AbstractCurrency {
    def designation = "EUR"
  }
}

object Japan extends CurrencyZone {

  type Currency = Yen
  val Yen = make(1)
  val CurrencyUnit = Yen

  def make(yen: Long) = new Yen {
    val amount = yen
  }

  abstract class Yen extends AbstractCurrency {
    def designation = "JPY"
  }
}

object Converter {
  var exchangeRate = Map(
    "USD" -> Map("USD" -> 1.0, "EUR" -> 0.7596,
      "JPY" -> 1.211, "CHF" -> 1.223),
    "EUR" -> Map("USD" -> 1.316, "EUR" -> 1.0,
      "JPY" -> 1.594, "CHF" -> 1.623),
    "JPY" -> Map("USD" -> 0.8257, "EUR" -> 0.6272,
      "JPY" -> 1.0, "CHF" -> 1.018),
    "CHF" -> Map("USD" -> 0.8108, "EUR" -> 0.6160,
      "JPY" -> 0.982, "CHF" -> 1.0)
  )
}
