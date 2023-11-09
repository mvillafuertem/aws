import {Construct} from "constructs";
import {S3Bucket} from "../../../io.github.mvillafuertem/providers/aws/s3-bucket";
import {
    S3BucketServerSideEncryptionConfigurationA
} from "../../../io.github.mvillafuertem/providers/aws/s3-bucket-server-side-encryption-configuration";
import {S3BucketVersioningA} from "../../../io.github.mvillafuertem/providers/aws/s3-bucket-versioning";
import {DynamodbTable} from "../../../io.github.mvillafuertem/providers/aws/dynamodb-table";
import {TerraformOutput} from "cdktf";

interface BackendConfiguration {
    bucket: string,
    dynamodbTable: string,
    commonTags: { [key: string]: string },
}

export class BackendStack extends Construct {
    constructor(scope: Construct, id: string, backendConfiguration: BackendConfiguration) {
        super(scope, id);

        const {bucket, dynamodbTable} = backendConfiguration;

        const s3Bucket: S3Bucket = new S3Bucket(this, "s3", {
            bucket: bucket,
            forceDestroy: true,
            tags: {
                owner: "cdktf",
            }
        });

        new S3BucketServerSideEncryptionConfigurationA(this, "s3_server_side_encryption", {
            bucket: s3Bucket.bucket,
            rule: [{
                applyServerSideEncryptionByDefault: {
                    sseAlgorithm: "AES256"
                }
            }]
        })

        new S3BucketVersioningA(this, "s3_versioning", {
            bucket: s3Bucket.bucket,
            versioningConfiguration: {
                status: "Enabled"
            }
        })

        new DynamodbTable(this, "dynamodb", {
            name: dynamodbTable,
            billingMode: "PAY_PER_REQUEST",
            hashKey: "LockID",
            attribute: [{name: "LockID", type: "S"}],
            tags: {}
        })

        new TerraformOutput(this, "tfstate_output", {
            value: s3Bucket.arn
        })

    }

}