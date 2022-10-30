package io.github.mvillafuertem.iam

import com.hashicorp.cdktf.{ AppOptions, TerraformStack }
import imports.aws.provider.{ AwsProvider, AwsProviderEndpoints }
import software.constructs.Construct

import scala.jdk.CollectionConverters._

final class Iam(scope: Construct, id: String) extends TerraformStack(scope, id) {
  self: Construct =>

  private val accountId = "000000000000"
  private val region    = "eu-west-3"
  private val accessKey = "accessKeyId"
  private val secretKey = "secretAccessKey"

  private val _: AwsProvider = AwsProvider.Builder
    .create(self, s"${id}_aws_provider")
    .allowedAccountIds(List(accountId).asJava)
    .region(region)
    .accessKey(accessKey)
    .secretKey(secretKey)
    .endpoints(
      Seq(
        AwsProviderEndpoints
          .builder()
          .iam("http://localhost:4566")
          .build()
      ).asJava
    )
    .skipCredentialsValidation(true)
    .skipMetadataApiCheck("true")
    // .skipRequestingAccountId(true)
    .build()

  private val users: Users   = Users(self, s"${id}_users")
  private val groups: Groups = Groups(self, s"${id}_groups")
  private val _: Policies    = Policies(self, s"${id}_policies")(users, groups)

}

// sbt -mem 6000 "iam/runMain io.github.mvillafuertem.iam.Iam"
// yarn --cwd modules/iam/ planIam
// yarn --cwd modules/iam/ applyIam
// yarn --cwd modules/iam/ destroyIam
object Iam extends App {

  private val app: com.hashicorp.cdktf.App = new com.hashicorp.cdktf.App(
    AppOptions
      .builder()
      .stackTraces(false)
      .outdir("modules/iam/src/main/resources/")
      .context(
        Map(
          "excludeStackIdFromLogicalIds" -> true,
          "allowSepCharsInLogicalIds"    -> true
        ).asJava
      )
      .build()
  )

  private val packageName: String = getClass.getPackage.getName
    .split("\\.")
    .drop(2)
    .mkString("_")

  new Iam(app, packageName)
  app.synth()

}
