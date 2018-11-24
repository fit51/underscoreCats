package cats.part1.Monad

import cats.Monad

import scala.language.higherKinds

//1
trait MyMonad[F[_]] {
  def pure[A](a: A): F[A]
  def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]
  def map[A, B](value: F[A])(func: A => B): F[B] =
    flatMap(value)(a => pure(func(a)))
}
//2

object Tasks extends App {
  import cats.Id
  // Import funcs for Id
  def pure[A](a: A): Id[A] = a
  def map[A, B](initial: Id[A])(func: A => B): Id[B] =
    func(initial)
  def flatMap[A, B](initial: Id[A])(func: A => Id[B]): Id[B] =
    func(initial)

  println(map(1)(_ + 2))

}
