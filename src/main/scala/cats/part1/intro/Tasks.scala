package cats.part1.intro

final case class Cat(name: String, age: Int, color: String)

object PrintableCatRun extends App {

  val c = Cat("Siri", 10, "brown")
  //Printable
  import PrintableInstances._
  import PrintableSyntax._

  c.print

  //Same using show
  import cats.Show
  import cats.syntax.show._
  import cats.instances.int._
  import cats.instances.string._

  implicit val catShow = Show.show[Cat](c =>
    s"${c.name.show} is a ${c.age.show} year-old ${c.color.show} cat"
  )

  println(c.show)
}

object EqCatRun extends App {
  import cats.Eq
  import cats.instances.all._
  import cats.syntax.eq._

  val cat1 =  Cat("Garfield", 38, "orange and black")
  val cat2 =  Cat("Tom", 33, "orange and black")

  val optCat1 = Option(cat1)
  val optCat2 = Option(cat2)

  implicit val catEq = new Eq[Cat] {
    override def eqv(x: Cat, y: Cat): Boolean =
      x.color === y.color &&
      x.age === y.age     &&
      x.name === x.name
  }

  println(cat1 === cat2)
  println(cat1 =!= cat2)

  println(optCat1 === optCat2)
  println(optCat1 =!= optCat2)

}