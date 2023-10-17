package io.github.mvillafuertem.iam

import io.github.mvillafuertem.aws.iam_group_membership.IamGroupMembership
import io.github.mvillafuertem.aws.iam_group_policy_attachment.IamGroupPolicyAttachment
import software.constructs.Construct

import scala.jdk.CollectionConverters._

final case class Policies(scope: Construct, id: String)(users: Users, groups: Groups) extends Construct(scope, id) {
  self: Construct =>

  import groups._
  import users._

  //  private val _: IamGroupPolicy = IamGroupPolicy.Builder
  //    .create(self, "ec2_admin_group_policy")
  //    .group(ec2AdminGroup.getName)
  //    .policy("""
  //              |
  //              |""".stripMargin)
  //    .build()

  private val _: IamGroupPolicyAttachment = IamGroupPolicyAttachment.Builder
    .create(self, "ec2_support_group_policy")
    .policyArn("arn:aws:iam::aws:policy/AmazonEC2ReadOnlyAccess")
    .group(ec2SupportGroup.getName)
    .build()

  private val _: IamGroupPolicyAttachment = IamGroupPolicyAttachment.Builder
    .create(self, "s3_support_group_policy")
    .policyArn("arn:aws:iam::aws:policy/AmazonS3ReadOnlyAccess")
    .group(s3SupportGroup.getName)
    .build()

  private val _: IamGroupMembership = IamGroupMembership.Builder
    .create(self, "s3_support_group_membership")
    .name("S3-Support-Membership")
    .users(Seq(user1.getName).asJava)
    .group(s3SupportGroup.getName)
    .build()

  private val _: IamGroupMembership = IamGroupMembership.Builder
    .create(self, "ec2_support_group_membership")
    .name("EC2-Support-Membership")
    .users(Seq(user2.getName).asJava)
    .group(ec2SupportGroup.getName)
    .build()

  private val _: IamGroupMembership = IamGroupMembership.Builder
    .create(self, "ec2_admin_group_membership")
    .name("EC2-Admin-Membership")
    .users(Seq(user3.getName).asJava)
    .group(ec2AdminGroup.getName)
    .build()

}
