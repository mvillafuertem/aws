package io.github.mvillafuertem.dev

import io.github.mvillafuertem.aws
import io.github.mvillafuertem.aws.default_network_acl.DefaultNetworkAcl
import io.github.mvillafuertem.aws.default_route_table.DefaultRouteTable
import io.github.mvillafuertem.aws.eip.Eip
import io.github.mvillafuertem.aws.iam_role.IamRole
import io.github.mvillafuertem.aws.internet_gateway.InternetGateway
import io.github.mvillafuertem.aws.nat_gateway.NatGateway
import io.github.mvillafuertem.aws.route_table.{ RouteTable, RouteTableRoute }
import io.github.mvillafuertem.aws.route_table_association.RouteTableAssociation
import io.github.mvillafuertem.aws.subnet.Subnet
import io.github.mvillafuertem.utils.Utils.TagName
import io.github.mvillafuertem.dev.Vpc.VpcConfiguration
import software.constructs.Construct

import java.util
import scala.jdk.CollectionConverters._

final case class Vpc(vpcConfiguration: VpcConfiguration)(implicit scope: Construct, id: String) extends Construct(scope, id) {
  self: Construct =>

  private val tags = vpcConfiguration.commonTags

  private val vpc: aws.vpc.Vpc = aws.vpc.Vpc.Builder
    .create(self, s"${id}_vpc")
    .cidrBlock("192.168.0.0/22")
    .tags((TagName("vpc-cdktf") ++ tags).asJava)
    .build()

  private val defaultRouteTable: DefaultRouteTable = DefaultRouteTable.Builder
    .create(self, s"${id}_default_route_table")
    .defaultRouteTableId(vpc.getDefaultRouteTableId)
    .tags((TagName("default_route_table-cdktf") ++ tags).asJava)
    .build()

  private val defaultNetworkAcl: DefaultNetworkAcl = DefaultNetworkAcl.Builder
    .create(self, s"${id}_default_network_acl")
    .defaultNetworkAclId(vpc.getDefaultNetworkAclId)
    .tags((TagName("default_network_acl-cdktf") ++ tags).asJava)
    .build()

  private val subnetPublic: Subnet = Subnet.Builder
    .create(self, s"${id}_subnet_public")
    .vpcId(vpc.getId)
    .availabilityZone("eu-west-2a")
    .cidrBlock("192.168.0.0/24")
    .tags((TagName("subnet_public-eu-west-2a-cdktf") ++ tags).asJava)
    .build()

  private val subnetPrivate: Subnet = Subnet.Builder
    .create(self, s"${id}_subnet_private")
    .vpcId(vpc.getId)
    .availabilityZone("eu-west-2a")
    .cidrBlock("192.168.2.0/23")
    .tags((TagName("subnet_private-eu-west-2a-cdktf") ++ tags).asJava)
    .build()

  private val internetGateway: InternetGateway = InternetGateway.Builder
    .create(self, s"${id}_internet_gateway")
    .vpcId(vpc.getId)
    .tags((TagName("internet_gateway-cdktf") ++ tags).asJava)
    .build()

  private val routeTableRoutesPublic: util.List[RouteTableRoute] = Seq(
    RouteTableRoute
      .builder()
      .gatewayId(internetGateway.getId)
      .cidrBlock("0.0.0.0/0")
      .build()
  ).asJava

  private val routeTablePublic: RouteTable = RouteTable.Builder
    .create(self, s"${id}_route_table_public")
    .vpcId(vpc.getId)
    .tags((TagName("route_table_public-cdktf") ++ tags).asJava)
    .route(routeTableRoutesPublic)
    .build()

  private val elasticIp: Eip = Eip.Builder
    .create(self, s"${id}_elastic_ip")
    .tags((TagName("elastic_ip-cdktf") ++ tags).asJava)
    .build()

  private val natGateway: NatGateway = NatGateway.Builder
    .create(self, s"${id}_nat_gateway")
    .subnetId(subnetPublic.getId)
    .connectivityType("public")
    .allocationId(elasticIp.getAllocationId)
    .tags((TagName("nat_gateway-cdktf") ++ tags).asJava)
    .build()

  private val routeTableRoutesPrivate: util.List[RouteTableRoute] = Seq(
    RouteTableRoute
      .builder()
      .gatewayId(natGateway.getId)
      .cidrBlock("0.0.0.0/0")
      .build()
  ).asJava

  private val routeTablePrivate: RouteTable = RouteTable.Builder
    .create(self, s"${id}_route_table_private")
    .vpcId(vpc.getId)
    .route(routeTableRoutesPrivate)
    .tags((TagName("route_table_private-cdktf") ++ tags).asJava)
    .build()

  private val routeTableAssociationPublic: RouteTableAssociation = RouteTableAssociation.Builder
    .create(self, s"${id}_route_table_association_public")
    .routeTableId(routeTablePublic.getId)
    .subnetId(subnetPublic.getId)
    .build()

  private val routeTableAssociationPrivate: RouteTableAssociation = RouteTableAssociation.Builder
    .create(self, s"${id}_route_table_association_private")
    .routeTableId(routeTablePrivate.getId)
    .subnetId(subnetPrivate.getId)
    .build()

}

object Vpc {

  case class VpcConfiguration(
    iamRole: IamRole,
    commonTags: Map[String, String]
  )

}
