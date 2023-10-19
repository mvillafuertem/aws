package io.github.mvillafuertem

import com.hashicorp.cdktf.AppConfig

import scala.jdk.CollectionConverters._

object Main extends App {

  private implicit val app: com.hashicorp.cdktf.App = new com.hashicorp.cdktf.App(
    AppConfig
      .builder()
      .stackTraces(false)
      .outdir("modules/vpc/src/main/resources/")
      .context(
        Map(
          "excludeStackIdFromLogicalIds" -> true,
          "allowSepCharsInLogicalIds"    -> true
        ).asJava
      )
      .build()
  )

  private implicit val id: String = getClass.getPackage.getName
    .split("\\.")
    .last

  private val commonTags = Map(
    "CreatedBy" -> "scala-cdktf"
  )

  // dev
  backend.BackendStack(commonTags)(app, id)
  dev.DevStack(commonTags)(app, id)

  app.synth()

}
