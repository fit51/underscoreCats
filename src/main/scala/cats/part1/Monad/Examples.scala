package cats.part1.Monad

import cats.MonadError
import cats.syntax.monadError

import scala.concurrent.{Await, Future}
import scala.util.Try

object SillyExamples extends App {

  def parseInt(str: String): Option[Int] =
    Try(str.toInt).toOption

  def divide(a: Int, b: Int): Option[Int] =
    if (b == 0) None else Some(a / b)

  def stringDivideBy(aStr: String, bStr: String): Option[Int] =
    for {
      aNum <- parseInt(aStr)
      bNum <- parseInt(bStr)
      ans <- divide(aNum, bNum)
    } yield ans

  println(stringDivideBy("23", "0"))
  println(stringDivideBy("2asdf", "2"))
  println(stringDivideBy("24", "2"))

}

object CatsExamples extends App {
  import cats.Monad
  import cats.instances.option._
  import cats.instances.list._

  val list1 = Monad[List].pure(1)
  val res = Monad[List].flatMap(list1)(a => List(a, a*2))


  //Monad for Future is tricky
  // pure and flatMap can not accept implicit ExecutionContext
  // so EC is is summoned in Monad instance for Fututre
  import cats.instances.future._
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._
  val fm = Monad[Future]
  val future = fm.flatMap(fm.pure(1))(x => fm.pure(x + 2))
  println(Await.result(future, 1.second))

  //Monad Syntax
  import cats.syntax.flatMap._     // for Monad
  import cats.syntax.functor._     // for Monad
  import cats.syntax.applicative._ // for pure
  import scala.language.higherKinds

  def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
    for {
      x <- a
      y <- b
    } yield x*x + y*y

  println(sumSquare(Option(3), Option(4)))
  println(sumSquare(List(1, 2, 3), List(4, 5)))


  // IndentityMonad
  import cats.Id
  println(sumSquare(3: Id[Int], 4: Id[Int]))

  // transforming Either
  import cats.syntax.either._
  println(
    "Error".asLeft[Int].orElse(2.asRight[String])
  )
  println(
    "error".asLeft[Int].recover {
      case str: String => -1
    }
  )
  println(
    6.asRight[String].bimap(_.reverse, _ + 1)
  )
  //ErrorHandling
  for {
    a <- 1.asRight[String]
    b <- 0.asRight[String]
    c <- if (b == 0) "DIV0".asLeft[Int]
    else (a/b).asRight[String]
  } yield c * 100
  //Akka Try
  type Result[A] = Either[Throwable, A]

  //MonadError
  import cats.MonadError
  import cats.instances.either._
  type ErrorOr[A] = Either[String, A]

  import cats.syntax.applicative._ // for pure
  import cats.syntax.applicativeError._ // for raiseError etc
  import cats.syntax.monadError._ // for ensure
  val success = 42.pure[ErrorOr]
  "Badness".raiseError[ErrorOr, Int]
  success.ensure("Number to low")(_ > 10)
}
