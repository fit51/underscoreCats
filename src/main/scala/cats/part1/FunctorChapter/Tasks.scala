package cats.part1.FunctorChapter

import cats.Functor

sealed trait Tree[+A]

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

final case class Leaf[A](value: A) extends Tree[A]

object TreeInstances {
  implicit val functorTree = new Functor[Tree] {
    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = {
      fa match {
        case Leaf(v) => Leaf(f(v))
        case Branch(left, right) =>
          Branch(map(left)(f), map(right)(f))
      }
    }
  }
}

object TreeRun extends App {
  import TreeInstances._
  import cats.syntax.functor._

  val tree1: Tree[Int] = Branch(
    Branch(
      Leaf(11),
      Leaf(22)
    ),
    Branch(
      Leaf(1),
      Branch(
        Leaf(2),
        Leaf(3)
      )
    )
  )
  println(tree1.fmap(_ + 1))
}

final case class Box[A](value: A)

object ContravariantEx extends App {
  /* Contramap - prepending operation to chain
   *           - for datatypes that
   *           represent transformation(Option - No, Printable - Yes)
   */
  // Trasnformation from A to String
  trait Printable[A] { self =>
    def format(value: A): String
    def contramap[B](func: B => A): Printable[B] =
      new Printable[B] {
        override def format(value: B): String =
          self.format(func(value))
      }
  }

  def format[A](value: A)(implicit p: Printable[A]): String =
    p.format(value)

  implicit val stringPrintable: Printable[String] =
    new Printable[String] {
      override def format(value: String): String = "\"" + value + "\""
    }
  implicit val booleanPrintable: Printable[Boolean] =
    new Printable[Boolean] {
      override def format(value: Boolean): String =
        if (value) "yes" else "no"
    }

  println(format("hello"))
  println(format(true))

  implicit def boxPrintable[A]
  (implicit aP: Printable[A]): Printable[Box[A]] =
    aP.contramap { b: Box[A] => b.value }

  println(format(Box("hello world")))
  println(format(Box(true)))
  //  println(format(Box(123)))

}

object InvariantEx extends App {
  /* imap - combination of map and contramap
   * bidirectional transformation
   */

  trait Codec[A] { self =>
    def encode(value: A): String
    def decode(value: String): A
    def imap[B](dec: A => B, enc: B => A): Codec[B] = new Codec[B] {
      override def encode(value: B): String =
        self.encode(enc(value))
      override def decode(value: String): B =
        dec(self.decode(value))
    }
  }
  def encode[A](value: A)(implicit c: Codec[A]): String =
    c.encode(value)
  def decode[A](value: String)(implicit c: Codec[A]): A =
    c.decode(value)

  implicit val stringCodec: Codec[String] =
    new Codec[String] {
      def encode(value: String): String = value
      def decode(value: String): String = value
    }
  implicit val intCodec: Codec[Int] =
    stringCodec.imap(_.toInt, _.toString)
  implicit val booleanCodec: Codec[Boolean] =
    stringCodec.imap(_.toBoolean, _.toString)

  implicit val doubleCodec: Codec[Double] =
    stringCodec.imap(_.toDouble, _.toString)

  implicit def boxCodec[A](implicit aC: Codec[A]): Codec[Box[A]] =
    aC.imap(a => Box(a), _.value)

  encode(123.4)
  // res0: String = 123.4
  decode[Double]("123.4")
  // res1: Double = 123.4
  println(encode(Box(123.4)))
  // res2: String = 123.4
  println(decode[Box[Double]]("123.4"))

}
