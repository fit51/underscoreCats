package cats.part1.FunctorChapter

import scala.language.higherKinds

trait FunctorImpl[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}
