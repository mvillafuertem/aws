package io.github.mvillafuertem.iam

import io.github.mvillafuertem.aws.iam_group.IamGroup
import software.constructs.Construct

final case class Groups(scope: Construct, id: String) extends Construct(scope, id) {
  self: Construct =>

  val ec2AdminGroup: IamGroup = IamGroup.Builder
    .create(self, "ec2_admin_group")
    .name("EC2-Admin")
    .build()

  val ec2SupportGroup: IamGroup = IamGroup.Builder
    .create(self, "ec2_support_group")
    .name("EC2-Support")
    .build()

  val s3SupportGroup: IamGroup = IamGroup.Builder
    .create(self, "s3_support_group")
    .name("S3-Support")
    .build()

}
