package cats.part1.MonoidSemigroups

object BooleanMonoids {
  val boolAndMonoid = new Monoid[Boolean] {
    override def combine(x: Boolean, y: Boolean): Boolean = x && y
    override def empty: Boolean = true
  }
  val boolOrMonoid = new Monoid[Boolean] {
    override def combine(x: Boolean, y: Boolean): Boolean = x || y
    override def empty: Boolean = false
  }
  //(notX and Y) or (X and notY)
  val boolXORMonoid = new Monoid[Boolean] {
    override def combine(x: Boolean, y: Boolean): Boolean =
      (!x && y) || (x && !y)
    override def empty: Boolean = false
  }
  //Not_Xor
  val boolNotXor = new Monoid[Boolean] {
    override def combine(x: Boolean, y: Boolean): Boolean =
      (!x || y) && (x || !y)
    override def empty: Boolean = true
  }
}

object SetMonoids {
  def setUniteMonad[T] = new Monoid[Set[T]] {
    override def combine(x: Set[T], y: Set[T]): Set[T] =
      x union y
    override def empty: Set[T] = Set.empty
  }
  def setNotIntersect[T] = new Monoid[Set[T]] {
    override def combine(x: Set[T], y: Set[T]): Set[T] =
      (x union y) -- (x.intersect(y))
    override def empty: Set[T] = Set.empty
  }
}

object SuperAdder  extends App {
  import cats.Monoid
  import cats.instances.int._
  import cats.syntax.semigroup._

  def add(items: List[Int]): Int =
    items.fold(Monoid[Int].empty)(Monoid[Int].combine)

  def addAll[T: cats.Monoid](items: List[T]) =
    items.fold(Monoid[T].empty)(_ |+| _)


  import cats.instances.option._
  println(addAll(List(Option(1), None, Option(2))))

  case class Order(totalCost: Double, quantity: Double)
  implicit def orderMonoid(implicit dM: cats.Monoid[Double]) = new cats.Monoid[Order] {
    override def empty: Order = Order(dM.empty, dM.empty)
    override def combine(x: Order, y: Order): Order = {
      Order(
        x.totalCost |+| y.totalCost,
        y.quantity |+| y.quantity)
    }
  }
  import cats.instances.double._
  println(
    addAll(List(Order(1.2, 3.2), Order(1, 4)))
  )
}

object BoolMVerify extends App {
  import MonoidLaws._
  assert(associativeLaw[Boolean](true, false, true)(BooleanMonoids.boolAndMonoid))
  assert(associativeLaw[Boolean](true, false, true)(BooleanMonoids.boolOrMonoid))
  assert(associativeLaw[Boolean](true, false, true)(BooleanMonoids.boolXORMonoid))
  assert(associativeLaw[Boolean](true, false, true)(BooleanMonoids.boolNotXor))

  assert(identityLaw[Boolean](true)(BooleanMonoids.boolAndMonoid))
  assert(identityLaw[Boolean](true)(BooleanMonoids.boolOrMonoid))
  assert(identityLaw[Boolean](true)(BooleanMonoids.boolNotXor))
  assert(identityLaw[Boolean](true)(BooleanMonoids.boolXORMonoid))

  assert(associativeLaw[Set[Int]](Set(1), Set(1, 2), Set(1, 2, 3))(SetMonoids.setUniteMonad))
  assert(associativeLaw[Set[Int]](Set(1), Set(1, 2), Set(1, 2, 3))(SetMonoids.setNotIntersect))

  assert(identityLaw[Set[Int]](Set(1, 2, 3))(SetMonoids.setUniteMonad))
  assert(identityLaw[Set[Int]](Set(1, 2, 3))(SetMonoids.setNotIntersect))

}
