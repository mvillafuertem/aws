package io.github.mvillafuertem.dev

import com.hashicorp.cdktf.TerraformOutput
import io.github.mvillafuertem.aws.data_aws_iam_policy_document.{DataAwsIamPolicyDocument, DataAwsIamPolicyDocumentStatement, DataAwsIamPolicyDocumentStatementPrincipals}
import io.github.mvillafuertem.aws.iam_access_key.IamAccessKey
import io.github.mvillafuertem.aws.iam_group.IamGroup
import io.github.mvillafuertem.aws.iam_group_membership.IamGroupMembership
import io.github.mvillafuertem.aws.iam_group_policy_attachment.IamGroupPolicyAttachment
import io.github.mvillafuertem.aws.iam_role.IamRole
import io.github.mvillafuertem.aws.iam_role_policy_attachment.IamRolePolicyAttachment
import io.github.mvillafuertem.aws.iam_user.IamUser
import io.github.mvillafuertem.dev.Iam.IamConfiguration
import software.constructs.Construct

import scala.jdk.CollectionConverters._

final case class Iam(configuration: IamConfiguration)(implicit scope: Construct, id: String) extends Construct(scope, id) {
  self: Construct =>
  import configuration._

  val cdktfAdmin: IamUser = IamUser.Builder
    .create(self, "admin_user")
    .name("cdktf-admin")
    .tags(commonTags.asJava)
    .build()

  private val iamAccessKey: IamAccessKey = IamAccessKey.Builder
    .create(self, "access_key")
    .user(cdktfAdmin.getName)
    .build()

  val cdktfAdminGroup: IamGroup = IamGroup.Builder
    .create(self, "admin_group")
    .name("cdktf-admin-group")
    .build()

  private val _: IamGroupPolicyAttachment = IamGroupPolicyAttachment.Builder
    .create(self, "admin_group_policy")
    .policyArn("arn:aws:iam::aws:policy/AdministratorAccess")
    .group(cdktfAdminGroup.getName)
    .build()

  private val _: IamGroupMembership = IamGroupMembership.Builder
    .create(self, "admin_group_membership")
    .name("cdktf-admin-group-membership")
    .users(Seq(cdktfAdmin.getName).asJava)
    .group(cdktfAdminGroup.getName)
    .build()

  // {
  //    "Version": "2012-10-17",
  //    "Statement": [
  //        {
  //            "Effect": "Allow",
  //            "Action": "*",
  //            "Resource": "*"
  //        }
  //    ]
  // }
  // private val permissions: DataAwsIamPolicyDocument = DataAwsIamPolicyDocument.Builder
  //   .create(self, "admin_role_policy_permissions")
  //   .statement(
  //     Seq(
  //       DataAwsIamPolicyDocumentStatement
  //         .builder()
  //         .effect("Allow")
  //         .resources(Seq("*").asJava)
  //         .actions(Seq("*").asJava)
  //         .build()
  //     ).asJava
  //   )
  //   .build()

  private val trustedEntities: DataAwsIamPolicyDocument = DataAwsIamPolicyDocument.Builder
    .create(self, "admin_role_policy_trusted_entities")
    .statement(
      Seq(
        DataAwsIamPolicyDocumentStatement
          .builder()
          .effect("Allow")
          .principals(
            Seq(
              DataAwsIamPolicyDocumentStatementPrincipals
                .builder()
                .`type`("AWS")
                .identifiers(Seq(cdktfAdmin.getArn).asJava)
                .build()
            ).asJava
          )
          .actions(Seq("sts:AssumeRole").asJava)
          .build()
      ).asJava
    )
    .build()

  val iamRole: IamRole = IamRole.Builder
    .create(self, "admin_role")
    .name("cdktf-admin-role")
    .tags(commonTags.asJava)
    .assumeRolePolicy(trustedEntities.getJson)
    .build()

  private val iamRolePolicyAttachment: IamRolePolicyAttachment = IamRolePolicyAttachment.Builder
    .create(self, "admin_role_policy_attachment")
    .role(iamRole.getId)
    .policyArn("arn:aws:iam::aws:policy/AdministratorAccess")
    .build()

  // terraform state pull | jq '.resources[] | select(.type == "aws_iam_access_key") | .instances[0].attributes'
  private val _: TerraformOutput = TerraformOutput.Builder
    .create(self, "access_key_id_output")
    .value(s"${iamAccessKey.getId}")
    .sensitive(true)
    .build()

  private val _: TerraformOutput = TerraformOutput.Builder
    .create(self, "access_key_secret_output")
    .value(s"${iamAccessKey.getSecret}")
    .sensitive(true)
    .build()

  private val _: TerraformOutput = TerraformOutput.Builder
    .create(self, "role_output")
    .value(s"${iamRole.getArn}")
    .build()

}

object Iam {
  final case class IamConfiguration(
    commonTags: Map[String, String]
  )
}
