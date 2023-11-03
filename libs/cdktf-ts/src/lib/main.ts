import {Construct} from "constructs";
import {App, TerraformStack} from "cdktf";
import {AwsProvider} from "../../io.github.mvillafuertem/providers/aws/provider";

class MyStack extends TerraformStack {
    constructor(scope: Construct, id: string) {
        super(scope, id);

        // define resources here
        new AwsProvider(this, "AWS", {
            region: "eu-west-2",
        });



    }
}
// yarn --cwd libs/cdktf-ts/ install
// yarn --cwd libs/cdktf-ts/ synth
const app: App = new App({outdir: "io.github.mvillafuertem"});
new MyStack(app, "cdktf-ts");
app.synth();
