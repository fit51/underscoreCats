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
  val boolEitherMonoid = new Monoid[Boolean] {
    override def combine(x: Boolean, y: Boolean): Boolean = ???
    override def empty: Boolean = ???
  }
}
