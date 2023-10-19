package io.github.mvillafuertem.dev

import io.github.mvillafuertem.dev.Iam.IamConfiguration
import io.github.mvillafuertem.dev.Terraform.TerraformConfiguration
import software.constructs.Construct

object DevStack {

  def apply(commonTags: Map[String, String])(scope: Construct, id: String) = {
    val terraformConfiguration =
      TerraformConfiguration(
        bucket = "scala-cdktf-dev-terraform-state",
        dynamodbTable = "scala_cdktf_dev_terraform_state_locks"
      )

    val iamConfiguration = IamConfiguration(
      commonTags = commonTags
    )

    val terraform = Terraform(terraformConfiguration)(scope, s"${id}-dev")
    Iam(iamConfiguration)(terraform, s"${id}_iam")
  }

}
