import java.time.LocalDate
import java.util.Date

//Show
import cats.Show
import cats.instances.int._
import cats.instances.string._

val showInt = Show.apply[Int]
val showString = Show.apply[String]

val intAsString: String =
  showInt.show(123)

import cats.syntax.show._
val showInt2 = 123.show
"abc".show


implicit val dateShow: Show[Date] =
  new Show[Date] {
    override def show(t: Date): String =
      s"${t.getTime}ms since epoch"
  }
implicit val localDateShow: Show[LocalDate] =
  Show.show(d => s"${d.getMonth}")

//Eq
import cats.Eq
import cats.instances.int._
import cats.instances.option._
import cats.syntax.eq._

//Mistake
//List(1, 2, 3).map(Option(_)).filter(_ == 1)

println(123 === 123)
println(123 =!= 124)

//Options
(Some(1): Option[Int]) === (None: Option[Int])
Option(1) === Option.empty

//Special syntax
import cats.syntax.option._

1.some === none[Int]
1.some =!= none[Int]

import cats.instances.long._
implicit val dateEq: Eq[Date] =
  Eq.instance[Date] { (d1, d2) =>
    d1.getTime === d2.getTime
  }


val x = new Date()
val y = new Date()

x === x
x === y