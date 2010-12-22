import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) with ProguardProject with Exec {
  
  //project name
  override val artifactID = "engel"

  //managed dependencies from built-in repositories
  val scalatest = "org.scalatest" % "scalatest" % "1.2"
  // val jsap = "com.martiansoftware" % "jsap" % "2.1"
  
  //files to go in packaged jars
  val extraResources = "README" +++ "LICENSE"
  override val mainResources = super.mainResources +++ extraResources

  //turn down logging level to 'warn'
  log.setLevel(Level.Warn)

  //program entry point
  // override def mainClass: Option[String] = Some("")

  //compiler options
  override def compileOptions = Deprecation :: Unchecked :: Nil //ExplainTypes

  //scaladoc options
  override def documentOptions =
    LinkSource ::
    documentTitle(name + " " + version + " API") ::
    windowTitle(name + " " + version + " API") ::
    Nil

  //proguard
  override def proguardOptions = List(
    "-keepclasseswithmembers public class * { public static void main(java.lang.String[]); }",
    proguardKeepAllScala
  )
  override def proguardInJars = Path.fromFile(scalaLibraryJar) +++ super.proguardInJars
}
