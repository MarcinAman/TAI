name := """TAI"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala).settings(
  watchSources ++= (baseDirectory.value / "public/ui" ** "*").get
)

resolvers += Resolver.sonatypeRepo("snapshots")

scalaVersion := "2.12.8"

scalacOptions += "-Ypartial-unification"


libraryDependencies += guice

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.6.0"

libraryDependencies += "org.sangria-graphql" %% "sangria" % "1.4.2"
libraryDependencies += "org.sangria-graphql" %% "sangria-play-json" % "1.0.5"

libraryDependencies += "com.softwaremill.quicklens" %% "quicklens" % "1.4.12"

libraryDependencies += "org.typelevel" %% "cats-core" % "1.6.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "1.2.0"

libraryDependencies += "com.softwaremill.quicklens" %% "quicklens" % "1.4.12"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.1"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.4.1" % "runtime"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.4.1"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.1"

libraryDependencies += "org.typelevel" %% "frameless-core" % "0.8.0"
libraryDependencies += "org.typelevel" %% "frameless-ml" % "0.8.0"
libraryDependencies += "org.typelevel" %% "frameless-cats" % "0.8.0"
libraryDependencies += "org.typelevel" %% "frameless-dataset" % "0.8.0"

libraryDependencies += ws
libraryDependencies += ehcache
libraryDependencies += "org.mockito" % "mockito-core" % "2.27.0"

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7"
