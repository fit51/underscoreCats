import cats.part1.intro.Json

sealed trait Shape
case class Circle(radius: Double) extends Shape

//Covariance   F[+A]
val circles: List[Circle] = List(Circle(1.2))
val shapes: List[Shape] = circles

//Contravariance  F[-A]
trait JsonWriter[-A] {
  def write(value: A): Json
}

val shape: Shape = ???
val circle: Circle = ???

val shapeWriter: JsonWriter[Shape] = ???
val circleWriter: JsonWriter[Circle] = ???
// Circle < Shape
//JsonWriter[Shape] < JsonWriter[Circle]

def format[A](value: A, writer: JsonWriter[A]): Json =
  writer.write(value)

format(circle, shapeWriter)
format(circle, circleWriter)

format(shape, shapeWriter)

//Invariance F[A]

// Searching for implicits -
// look for one matching Type or Subtype
sealed trait A
final case object B extends A
final case object C extends A
//                                Inv       Cov     ContraV
//Supertype instance used?        No        No      Yes
//More specific type preferred?   No        Yes     No