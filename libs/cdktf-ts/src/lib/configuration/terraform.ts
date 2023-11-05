import {Construct} from "constructs";
import {S3Backend, TerraformStack, TerraformVariable} from "cdktf";
import {AwsProvider} from "../../../io.github.mvillafuertem/providers/aws/provider";


interface TerraformConfiguration {
    accountId: string,
    region: string,
    key: string,
    bucket: string,
    dynamodbTable: string,
    commonTags: { [key: string]: string },
}

export class Terraform extends TerraformStack {
    constructor(scope: Construct, id: string, configuration: TerraformConfiguration) {
        super(scope, id);

        const {
            accountId,
            region,
            key,
            bucket,
            dynamodbTable
        }: TerraformConfiguration = configuration

        const profile: TerraformVariable = new TerraformVariable(this, "profile", {
            type: "string"
        })

        // export $(printf "AWS_ACCESS_KEY_ID=%s AWS_SECRET_ACCESS_KEY=%s AWS_SESSION_TOKEN=%s" \
        // $(aws sts assume-role \
        // --role-arn arn:aws:iam::123456789012:role/MyAssumedRole \
        // --role-session-name MySessionName \
        // --query "Credentials.[AccessKeyId,SecretAccessKey,SessionToken]" \
        // --output text))
        new AwsProvider(this, "AWS", {
            allowedAccountIds: [accountId],
            region: region,
            profile: profile.stringValue
        });

        new S3Backend(this, {
            key: key,
            bucket: bucket,
            region: region,
            encrypt: true,
            profile: profile.stringValue,
            dynamodbTable: dynamodbTable
        })

    }
}
