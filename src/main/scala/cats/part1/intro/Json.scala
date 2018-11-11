package cats.part1.intro

sealed trait Json
final case class JsObject(get: Map[String, Json]) extends Json
final case class JsString(get: String) extends Json
final case class JsNumber(get: Double) extends Json
case object JsNull extends Json

trait JsonWriter[A] {
  def write(value: A): Json
}

object JsonWriter {
  //We could put implicits here, it would be implicit scope
  implicit val personWriter: JsonWriter[Person] =
    new JsonWriter[Person] {
      override def write(value: Person): Json = JsObject(Map(
        "name" -> JsString(value.name),
        "email" -> JsString(value.email)
      ))
    }
  //This would not be used
  implicit val stringWriter2: JsonWriter[String] =
    new JsonWriter[String] {
      override def write(value: String): Json = JsString(value + "2")
    }
}

final case class Person(name: String, email: String)

object JsonWriterInstances {
  implicit val stringWriter: JsonWriter[String] =
    new JsonWriter[String] {
      override def write(value: String): Json = JsString(value)
    }
  //Implicit recursive resolution
  implicit def optionWriter[A](implicit writer: JsonWriter[A]): JsonWriter[Option[A]] =
  new JsonWriter[Option[A]] {
    override def write(value: Option[A]): Json = value match {
      case None     => JsNull
      case Some(v)  => writer.write(v)
    }
  }
}

object scalaDef {
  def implicitly[A](implicit value: A): A = value
}

object Json {
  def toJson[A](value: A)(implicit w: JsonWriter[A]): Json =
    w.write(value)
}

object JsonSyntax {
  implicit class JsonWriterOps[A](value: A) {
    def toJson(implicit w: JsonWriter[A]) = w.write(value)
  }
}

object JRunner extends App {
  import JsonWriterInstances._
  import JsonSyntax._

  println(Json.toJson("Dave")(implicitly[JsonWriter[String]]))
  println(Json.toJson(Person("Dave", "dave@example.com")))
  println(Person("Dave", "dave@example.com").toJson)
  println(Option(Person("Dave", "dave@example.com")).toJson)

}