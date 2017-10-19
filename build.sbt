import sbt._
import Keys._


name := "DomainScanner"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.5.2"

assemblyMergeStrategy in assembly := {
  case PathList("com", "google", xs @ _*)         => MergeStrategy.first
  case PathList("org", "apache", "commons", xs @ _*)         => MergeStrategy.last
  case PathList("com", "esotericsoftware", "minlog", xs @ _*)         => MergeStrategy.last
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case "application.conf"                            => MergeStrategy.concat
  case "unwanted.txt"                                => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}


