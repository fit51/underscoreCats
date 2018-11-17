name := "ScalaCats"

version := "0.1"

scalaVersion := "2.12.7"

libraryDependencies ++= List(
  "org.typelevel" %% "cats-core" % "1.4.0"
)
//Add for Function1 syntax in Functor
scalacOptions += "-Ypartial-unification"
//scalacOptions += "-language:higherKinds"