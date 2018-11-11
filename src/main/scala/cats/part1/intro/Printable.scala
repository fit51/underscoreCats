package cats.part1.intro

trait Printable[A] {
  def format(a: A): String
}

object Printable {
  def format[A](a: A)(implicit p: Printable[A]): String = p.format(a)
  def print[A](a: A)(implicit p: Printable[A]): Unit = println(p.format(a))
}

object PrintableInstances {
  implicit val strPrintable: Printable[String] = new Printable[String] {
    override def format(a: String): String = a
  }
  implicit val intPrintable: Printable[Int] = new Printable[Int] {
    override def format(a: Int): String = a.toString
  }
  implicit val catPrintable: Printable[Cat] = new Printable[Cat] {
    override def format(a: Cat): String = s"${a.name} is a ${a.age} year-old ${a.color} cat"
  }
}
object PrintableSyntax {
  implicit class PrintableOps[A](a: A) {
    def format(implicit p: Printable[A]) = p.format(a)
    def print(implicit p: Printable[A]) = println(p.format(a))
  }
}