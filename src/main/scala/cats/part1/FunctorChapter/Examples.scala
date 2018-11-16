package cats.part1.FunctorChapter

import cats._
import cats.implicits._

object FunctorExample extends App {
  //  import cats._
  //  import cats.instances._
    import cats.instances.function._
    import cats.syntax.functor._

  //Function1 is Functor of it's result Type
  val func1: FunctionInt1[Double] =
    (x: Int) => x.toDouble

  val func2: Double => Double =
    (y: Double) => y * 2

  //Composition
  println((func1 fmap func2) (1))

  type FunctionInt1[A] = Function1[Int, A]

  println(implicitly(Functor[FunctionInt1]))
  println(Functor[FunctionInt1].fmap(func1)(func2)(1))
  (func1 andThen func2) (1)
  func2(func1(1))

}
