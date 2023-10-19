package io.github.mvillafuertem.backend

import com.hashicorp.cdktf.TerraformOutput
import io.github.mvillafuertem.aws.dynamodb_table.{ DynamodbTable, DynamodbTableAttribute }
import io.github.mvillafuertem.aws.s3_bucket._
import io.github.mvillafuertem.aws.s3_bucket_server_side_encryption_configuration.{
  S3BucketServerSideEncryptionConfigurationA,
  S3BucketServerSideEncryptionConfigurationRuleA,
  S3BucketServerSideEncryptionConfigurationRuleApplyServerSideEncryptionByDefaultA
}
import io.github.mvillafuertem.aws.s3_bucket_versioning.{ S3BucketVersioningA, S3BucketVersioningVersioningConfiguration }
import io.github.mvillafuertem.backend.Backend.BackendConfiguration
import software.constructs.Construct

import scala.jdk.CollectionConverters._

final case class Backend(configuration: BackendConfiguration)(implicit scope: Construct, id: String) extends Construct(scope, id) {
  self: Construct =>

  private val s3Bucket: S3Bucket = S3Bucket.Builder
    .create(self, "s3")
    .bucket(configuration.bucket)
    .forceDestroy(true)
    .tags(configuration.commonTags.asJava)
    .build()

  S3BucketServerSideEncryptionConfigurationA.Builder
    .create(self, "s3_server_side_encryption")
    .bucket(s3Bucket.getBucket)
    .rule(
      Seq(
        S3BucketServerSideEncryptionConfigurationRuleA
          .builder()
          .applyServerSideEncryptionByDefault(
            S3BucketServerSideEncryptionConfigurationRuleApplyServerSideEncryptionByDefaultA
              .builder()
              .sseAlgorithm("AES256")
              .build()
          )
          .build()
      ).asJava
    )
    .build()

  S3BucketVersioningA.Builder
    .create(self, "s3_versioning")
    .bucket(s3Bucket.getBucket)
    .versioningConfiguration(
      S3BucketVersioningVersioningConfiguration
        .builder()
        .status("Enabled")
        .build()
    )
    .build()

  private val dynamodbTable: DynamodbTable = DynamodbTable.Builder
    .create(self, "dynamodb")
    .name(configuration.dynamodbTable)
    .billingMode("PAY_PER_REQUEST")
    .hashKey("LockID")
    .attribute(List(DynamodbTableAttribute.builder().name("LockID").`type`("S").build()).asJava)
    .tags(configuration.commonTags.asJava)
    .build()

  private val _: TerraformOutput = TerraformOutput.Builder
    .create(self, "tfstate_output")
    .value(s"${s3Bucket.getArn}")
    .build()

}

object Backend {

  final case class BackendConfiguration(
    bucket: String,
    dynamodbTable: String,
    commonTags: Map[String, String]
  )

}
