package io.github.mvillafuertem.backend

import com.hashicorp.cdktf.Testing
import io.github.mvillafuertem.backend.Backend.BackendConfiguration
import io.github.mvillafuertem.backend.Terraform.TerraformConfiguration
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class BackendSpec extends AnyWordSpecLike with Matchers {

  "Backend" should {

    "synth" in {
      // g i v e n
      val terraformConfiguration =
        TerraformConfiguration(
          bucket = "scala-cdktf-dev-terraform-state",
          dynamodbTable = "scala_cdktf_dev_terraform_state_locks"
        )

      val backendConfiguration = BackendConfiguration(
        bucket = "scala-cdktf-dev-terraform-state",
        dynamodbTable = "scala_cdktf_dev_terraform_state_locks",
        commonTags = Map()
      )

      val terraform = Terraform(terraformConfiguration)(Testing.app, "stack")
      Backend(backendConfiguration)(terraform, "resource")

      // w h e n
      val actual = Testing.toBeValidTerraform(Testing.fullSynth(terraform))

      // t h e n
      actual shouldBe true
    }

  }

}
