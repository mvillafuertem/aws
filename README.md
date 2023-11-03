<p align="center"><img width="600" src="https://www.logo.wine/a/logo/Amazon_Web_Services/Amazon_Web_Services-Logo.wine.svg"/></p>
<h1 align="center">AWS</h1>

****

AWS is a project with many proof of concept modules...

****

## Initial Configuration

### S3Backend

> **Note**
>
> if you change scala code, you need to rerun main
> ```shell
> 
> sbt vpc/runMain io.github.mvillafuertem.Main2
> 
> ```


1. Comment S3Backend resource which is inside Terraform.scala

```scala
//  private val _: S3Backend = S3Backend.Builder
//    .create(self)
//    .bucket(configuration.bucket)
//    .key("backend/terraform.tfstate")
//    .region("eu-west-2")
//    .dynamodbTable(configuration.dynamodbTable)
//    .encrypt(true)
//    .profile("gbgdev")
//    .build()
```

2. Create backend stack

```shell

cd modules/vpc/src/main/resources/stacks/mvillafuertem-backend 

terraform apply

...
Apply complete! Resources: 2 added, 0 changed, 0 destroyed.
....

```

3. Uncomment S3Backend resource which is inside Terraform.scala

```shell

terraform apply

│ Error: Backend initialization required: please run "terraform init"
│ 
│ Reason: Backend type changed from "local" to "s3"
...

```
4. Run terraform init again

```shell

terraform init -migrate-state

Initializing the backend...
Terraform detected that the backend type changed from "local" to "s3".

Do you want to copy existing state to the new backend?
...

Enter a value: yes

```

5. Check everything is ok

```shell

terraform plan

No changes. Your infrastructure matches the configuration.

```

## Using AWS profile

```shell

terraform init -var="profile=myprofile"

terraform init -backend-config="profile=myprofile"

```

## AWS Technical Essentials

### Lab 1: Introduction to AWS Identity and Access Management

![Image](docs/aws_technical_essentials_lab_1_introduction_to_aws_identity_and_access_management.png?raw=true)

```shell



```

### Lab 2: Creating a VPC and Launching a Web Application in an Amazon EC2 Instance

![Image](docs/aws_technical_essentials_lab_2_creating_a_vpc_and_launching_a_web_application_in_an_amazon_ec2_instance.png?raw=true)

![Image](docs/aws_technical_essentials_lab_2_creating_a_vpc_and_launching_a_web_application_in_an_amazon_ec2_instance_2.png?raw=true)

### Lab 3 - Configure a Web Application to use an Amazon S3 Bucket and DynamoDB Table

![Image](docs/aws_technical_essentials_lab_3_configure_a_web_application_to_use_an_amazon_s3_bucket_and_dynamodb_table.png?raw=true)

### Lab 4: Configure High Availability for your Application

![Image](docs/aws_technical_essentials_lab_4_configure_high_availability_for_your_application.png?raw=true)
