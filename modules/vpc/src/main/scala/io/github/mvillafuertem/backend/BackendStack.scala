package io.github.mvillafuertem.backend

import io.github.mvillafuertem.backend.Backend.BackendConfiguration
import io.github.mvillafuertem.backend.Terraform.TerraformConfiguration
import software.constructs.Construct

object BackendStack {

  def apply(commonTags: Map[String, String])(scope: Construct, id: String): Backend = {
    val terraformConfiguration =
      TerraformConfiguration(
        bucket = "scala-cdktf-dev-terraform-state",
        dynamodbTable = "scala_cdktf_dev_terraform_state_locks"
      )

    val backendConfiguration = BackendConfiguration(
      bucket = "scala-cdktf-dev-terraform-state",
      dynamodbTable = "scala_cdktf_dev_terraform_state_locks",
      commonTags = commonTags
    )

    val terraform = Terraform(terraformConfiguration)(scope, s"${id}-backend")
    Backend(backendConfiguration)(terraform, s"${id}_backend_stack")
  }

}
