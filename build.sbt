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
libraryDependencies += "org.typelevel" %% "cats-core" % "1.6.0"
libraryDependencies += "com.softwaremill.quicklens" %% "quicklens" % "1.4.12"
libraryDependencies += ws
libraryDependencies += ehcache
libraryDependencies += "org.mockito" % "mockito-core" % "2.27.0"