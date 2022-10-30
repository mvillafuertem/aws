package io.github.mvillafuertem.iam

import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers

import sys.process._

final class IamSpec extends AnyFlatSpecLike with Matchers {

  it should "apply" in {

    val planActual    = "yarn --cwd modules/iam/ planIam".!
    val applyActual   = "yarn --cwd modules/iam/ applyIam -auto-approve".!
    val destroyActual = "yarn --cwd modules/iam/ destroyIam -auto-approve".!

    planActual shouldBe 0
    applyActual shouldBe 0
    destroyActual shouldBe 0

  }

}
