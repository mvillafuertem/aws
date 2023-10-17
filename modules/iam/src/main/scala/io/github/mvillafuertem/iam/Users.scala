package io.github.mvillafuertem.iam

import io.github.mvillafuertem.aws.iam_user.IamUser
import software.constructs.Construct

final case class Users(scope: Construct, id: String) extends Construct(scope, id) {
  self: Construct =>

  val user1: IamUser = IamUser.Builder
    .create(self, "user_1")
    .name("user-1")
    .build()

  val user2: IamUser = IamUser.Builder
    .create(self, "user_2")
    .name("user-2")
    .build()

  val user3: IamUser = IamUser.Builder
    .create(self, "user_3")
    .name("user-3")
    .build()

}
