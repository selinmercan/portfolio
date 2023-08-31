// default imports, from https://github.com/aws-samples/lambda-refarch-webapp/
// const AWSXRay = require("aws-xray-sdk-core");
// const AWS = AWSXRay.captureAWS(require("aws-sdk"));
// const { metricScope, Unit } = require("aws-embedded-metrics");
// const DDB = new AWS.DynamoDB({ apiVersion: "2012-10-08" });
let processQueryDynamo, processQueryFlex;

const AWS = require("aws-sdk");

// environment variables
const {TABLE_NAME, ENDPOINT_OVERRIDE, REGION} = process.env;
const options = {region: REGION};
AWS.config.update({region: REGION});

const isRunningLocally = () => process.env.AWS_SAM_LOCAL === 'true'
const tableName = TABLE_NAME;
// const tableName = "MainTable";

if (ENDPOINT_OVERRIDE !== "") {
  options.endpoint = ENDPOINT_OVERRIDE;
}
if (isRunningLocally()) {
  options.endpoint = 'http://host.docker.internal:8000';
}

const docClient = new AWS.DynamoDB.DocumentClient(options);
// response helper
const jsonResponse = (statusCode, body, additionalHeaders) => ({
  statusCode,
  body: JSON.stringify(body),
  headers: {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    ...additionalHeaders,
  },
});

exports.search = async (event, context) => {
  if (!processQueryFlex){
    console.log("Importing search stuff");
    ({processQueryDynamo, processQueryFlex} = require("./search"));
  }
  try {
    console.log('Search received:', event);
    const body = JSON.parse(event.body);
    console.log('Options', options, 'Table name', tableName);

    const params = {
      TableName: tableName
    };
    console.log('Search request json', body);

    // const data = await processQueryDynamo(body, params, docClient);
    const data = await processQueryFlex(body, docClient, tableName);
    return jsonResponse(200, data);
  } catch (e) {
    console.error(e);
    return jsonResponse(500, {error: `Couldn't process request: ${e}`})
  }
}
