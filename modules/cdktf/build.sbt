libraryDependencies ++= Seq(
  // C D K T F
  "com.hashicorp"       % "cdktf"      % "0.19.0",
  "software.constructs" % "constructs" % "10.2.30"
) ++ Seq(
  // C D K T F  T E S T
  "org.scalatest"      %% "scalatest"  % "3.2.15" % Test
)
