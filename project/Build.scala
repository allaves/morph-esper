import sbt._
import Keys._
import com.typesafe.sbteclipse.plugin.EclipsePlugin._

object MorphEsperBuild extends Build {
  val scalaOnly = Seq (
    unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(Seq(_)),
    unmanagedSourceDirectories in Test <<= (scalaSource in Test)(Seq(_))
  )
  val projSettings = Seq (
    scalaVersion := "2.10.1",
    crossPaths := false,
    scalacOptions ++= Seq("-deprecation","-feature"),
    parallelExecution in Test := false,
    resolvers ++= Seq(
      DefaultMavenRepository,
      "Local ivy Repository" at "file://"+Path.userHome.absolutePath+"/.ivy2/local")
  )
  val ideSettings = Seq (
    EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource
  )
  val publishSettings = Seq (
    publishTo := Some("Artifactory Realm" at "http://aldebaran.dia.fi.upm.es/artifactory/sstreams-releases-local"),
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    publishMavenStyle := true,
    publishArtifact in (Compile, packageSrc) := false )

  val buildSettings = Defaults.defaultSettings ++ projSettings ++ ideSettings ++ publishSettings 

  lazy val root = Project(id = "morph-esper",
                          base = file("."),settings = buildSettings) aggregate(esperengine,esper)

  lazy val esper = Project(id = "adapter-esper",
                              base = file("adapter-esper"),settings = buildSettings ++ scalaOnly) 

  lazy val esperengine = Project(id = "esper-engine",
                              base = file("esper-engine"),settings = buildSettings ++ scalaOnly) 

}
