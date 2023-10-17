package io.github.mvillafuertem.configuration

import com.dimafeng.testcontainers.{ DockerComposeContainer, ExposedService }
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AsyncFlatSpecLike
import org.scalatest.matchers.should.Matchers
import org.testcontainers.containers.wait.strategy.{ LogMessageWaitStrategy, Wait }

import java.io.File

trait LocalStackConfigurationIT extends AsyncFlatSpecLike with Matchers with BeforeAndAfterAll {

  private val AWS_LOCALSTACK_HOST: String = "http://localhost"
  private val AWS_LOCALSTACK_PORT: Int    = 4566

  val AWS_LOCALSTACK_ENDPOINT: String = s"$AWS_LOCALSTACK_HOST:$AWS_LOCALSTACK_PORT"

  val dockerInfrastructure: DockerComposeContainer =
    DockerComposeContainer(
      new File(s"modules/iam/src/it/resources/docker-compose.it.yml"),
      exposedServices = Seq(ExposedService("localstack", AWS_LOCALSTACK_PORT, 1, Wait.forLogMessage(".*Ready.*", 1))),
      identifier = "docker_infrastructure"
    )

}
