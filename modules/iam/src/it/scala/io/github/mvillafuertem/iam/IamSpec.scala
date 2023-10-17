package io.github.mvillafuertem.iam

import io.github.mvillafuertem.configuration.LocalStackConfigurationIT

import scala.sys.process._

final class IamSpec extends LocalStackConfigurationIT {

  it should "apply" in {

    val planActual    = "yarn --cwd modules/iam/ planIam".!
    val applyActual   = "yarn --cwd modules/iam/ applyIam -auto-approve".!
    val destroyActual = "yarn --cwd modules/iam/ destroyIam -auto-approve".!

    planActual shouldBe 0
    applyActual shouldBe 0
    destroyActual shouldBe 0

  }

  override protected def beforeAll(): Unit = dockerInfrastructure.start()

  override protected def afterAll(): Unit = dockerInfrastructure.stop()

}
