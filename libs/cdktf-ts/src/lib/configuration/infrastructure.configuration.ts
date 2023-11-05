import {Terraform} from "./terraform";
import {BackendStack} from "../infrastructure/backend";
import {Construct} from "constructs";


export function backendStackFactory(app: Construct): BackendStack {

    const terraform: Terraform = new Terraform(app, "cdktf-backend", {
        accountId: "000000000000",
        region: "eu-west-2",
        key: "backend/terraform.tfstate",
        bucket: "ts-cdktf-dev-terraform-state",
        dynamodbTable: "ts_cdktf_dev_terraform_state_locks",
        commonTags: {}
    })

    return new BackendStack(terraform, "cdktf-backend-stack", {
        bucket: "ts-cdktf-dev-terraform-state",
        dynamodbTable: "ts_cdktf_dev_terraform_state_locks",
        commonTags: {}
    })

}