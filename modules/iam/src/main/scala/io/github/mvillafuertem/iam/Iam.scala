package io.github.mvillafuertem.iam

import software.constructs.Construct
import com.hashicorp.cdktf.{ AppOptions, TerraformStack }
import imports.aws.provider.AwsProvider

import scala.jdk.CollectionConverters._

final class Iam(scope: Construct, id: String) extends TerraformStack(scope, id) {
  self: Construct =>

  private val accountId             = "582268654997"
  private val region                = "eu-west-3"
  private val sharedCredentialsFile = "~/.aws/credentials"
  private val profile               = "myprofile"

  private val _: AwsProvider = AwsProvider.Builder
    .create(self, "cdktf_aws_provider")
    .allowedAccountIds(List(accountId).asJava)
    .region(region)
    .sharedCredentialsFile(sharedCredentialsFile)
    .profile(profile)
    .build()

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

  new Iam(app, "iam")
  app.synth()

}
