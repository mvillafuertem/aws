import {App} from "cdktf";
import {backendStackFactory} from "./configuration/infrastructure.configuration";

// yarn --cwd libs/cdktf-ts/ install
// yarn --cwd libs/cdktf-ts/ synth
const app: App = new App({outdir: "io.github.mvillafuertem"});
backendStackFactory(app)
app.synth();
