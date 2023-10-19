package io.github.mvillafuertem.backend

import com.hashicorp.cdktf.{ S3Backend, TerraformStack }
import io.github.mvillafuertem.aws.provider.AwsProvider
import io.github.mvillafuertem.backend.Terraform.TerraformConfiguration
import software.constructs.Construct

import scala.jdk.CollectionConverters._

final case class Terraform(configuration: TerraformConfiguration)(implicit scope: Construct, id: String) extends TerraformStack(scope, id) {
  self: Construct =>

  private val accountId = "000000000000"
  private val region    = "eu-west-2"

  // export $(printf "AWS_ACCESS_KEY_ID=%s AWS_SECRET_ACCESS_KEY=%s AWS_SESSION_TOKEN=%s" \
  // $(aws sts assume-role \
  // --role-arn arn:aws:iam::123456789012:role/MyAssumedRole \
  // --role-session-name MySessionName \
  // --query "Credentials.[AccessKeyId,SecretAccessKey,SessionToken]" \
  // --output text))
  private val _: AwsProvider = AwsProvider.Builder
    .create(self, s"${id}_aws_provider")
    .allowedAccountIds(List(accountId).asJava)
    .region(region)
    .profile("gbgdev")
    .build()

  private val _: S3Backend = S3Backend.Builder
    .create(self)
    .bucket(configuration.bucket)
    .key("backend/terraform.tfstate")
    .region("eu-west-2")
    .dynamodbTable(configuration.dynamodbTable)
    .encrypt(true)
    .profile("gbgdev")
    .build()

}

object Terraform {
  final case class TerraformConfiguration(
    bucket: String,
    dynamodbTable: String
  )
}
