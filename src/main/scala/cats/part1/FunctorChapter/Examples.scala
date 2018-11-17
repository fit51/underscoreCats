package cats.part1.FunctorChapter

import cats._
import cats.implicits._

import scala.concurrent.{ExecutionContext, Future}

/*
 * scalacOptions += "-Ypartial-unification"
 */
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

  val func =
    ((x: Int) => x.toDouble).
      map(x => x + 1).
      map(x => x * 2).
      map(x => x + "!")

  println(func(123))

  //----------
  val funcc = (x: Int) => x + 1
  //Option[Int] => Option[Int]
  val lifted = Functor[Option].lift(funcc)

  println(lifted(Option(1)))
  //------
  def doMath[F[_]](start: F[Int])
                  (implicit functor: Functor[F]): F[Int] =
    start.map(_ + 1)

  import cats.instances.option._
  import cats.instances.list._

  println(doMath(Option(20)))

  println(doMath(List(1, 2, 3)))

}

object CustomFunctorInstances {
  implicit def futureFunctor
  (implicit ec: ExecutionContext): Functor[Future] =
    new Functor[Future] {
      def map[A, B](value: Future[A])(func: A => B): Future[B] =
        value.map(func)
    }
}

object HigherKinds extends App {
  import cats.instances.list._
  import scala.language.higherKinds // or scalacOptions += "-language:higherKinds"

  def method[F[_]](implicit f: Functor[F]) = {
    Functor.apply[F]
  }

  println(method[List])
}

object ContravariantInvariant extends App {
  /* trait Contravariant[F[_]] {
        def contramap[A, B](fa: F[A])(f: B => A): F[B]
      }
    trait Invariant[F[_]] {
      def imap[A, B](fa: F[A])(f: A => B)(g: B => A): F[B]
    }
   */

  import cats.Contravariant
  import cats.Show
  import cats.instances.string._

  val showString = Show[String]

  val showSymbol = Contravariant[Show].
    contramap(showString)((sym: Symbol) => s"'${sym.name}")

  showSymbol.show('dave)
  import cats.syntax.contravariant._
  showString.contramap[Symbol](_.name).show('dave)

  //--------Invariant for monois
  import cats.Monoid
  import cats.instances.string._ //for Monoid
  import cats.syntax.invariant._ // for imap
  import cats.syntax.semigroup._ // for |+|

  implicitly[Invariant[Monoid]]

  implicit val symbolMonoid: Monoid[Symbol] =
    Monoid[String].imap(Symbol.apply)(_.name)

  Monoid[Symbol].empty
  println('a |+| 'few |+| 'words)
}

object TypeConstructor extends App {
  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(func: A => B): F[B]
  }
  trait Function1[-A, +B] {
    def apply(arg: A): B
  }
  // Left to Right partial Unification
  // works by fixing type parameter from left to right
  //Int => ?
  type F[A] = Int => A
  val functor = Functor[F]

  import cats.instances.either._
  import cats.syntax.functor._
  val either: Either[String, Int] = Right(123)
  println(either.fmap(_ + 1))

  // there are examples, where left to right does not work
  // Functor for Function1 - then-style, left-to-right
  // Contravarint for Function1 - cmpose-style, right-to-left
  val func1: Int => Double =
  (x: Int) => x.toDouble
  //Right to Left elemination By hand
  type <=[B, A] = A => B
  val func2: Double <= Double =
    (y: Double) => y * 2

  val func3b: Int => Double =
    func2.compose(func1)
  import cats.syntax.contravariant._
  val func3c: Int => Double =
    func2.contramap(func1)

  println(func3c(1))

}