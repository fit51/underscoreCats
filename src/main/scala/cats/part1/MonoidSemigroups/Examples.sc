import cats.Monoid
import cats.instances.string._

Monoid[String].combine("hi ", "there")

import cats.instances.int._
import cats.instances.option._

Monoid[Option[Int]].combine(Some(1), Some(2))
Monoid[Option[Int]].empty

import cats.syntax.semigroup._

"Hi " |+| "there"
1 |+| 2 |+| Monoid[Int].empty