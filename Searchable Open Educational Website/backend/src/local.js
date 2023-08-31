require('dotenv').config();
const AWS = require("aws-sdk");
const {processQueryFlex} = require("./search");
// environment variables
const {TABLE_NAME, ENDPOINT_OVERRIDE, REGION, AWS_KEY_ID, AWS_KEY_SECRET} = process.env;
const options = {region: "us-west-2"};
AWS.config.update({
  region: "us-west-2",
  accessKeyId: AWS_KEY_ID,
  secretAccessKey: AWS_KEY_SECRET,
});

const isRunningLocally = () => 'true'
// const tableName = process.env.TABLE_NAME;
const tableName = "MainTable";
options.endpoint = 'http://localhost:8000';
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

// adapted from https://stackoverflow.com/a/62900949/8170714
const updateParams = async (item, idAttributeName) => {
  const params = {
    Key: {},
    ExpressionAttributeValues: {},
    ExpressionAttributeNames: {},
    UpdateExpression: "",
    ReturnValues: "UPDATED_NEW"
  };

  params["Key"][idAttributeName] = item[idAttributeName];

  let prefix = "set ";
  let attributes = Object.keys(item);
  for (let i = 0; i < attributes.length; i++) {
    let attribute = attributes[i];
    if (attribute !== idAttributeName) {
      params["UpdateExpression"] += prefix + "#" + attribute + " = :" + attribute;
      params["ExpressionAttributeValues"][":" + attribute] = item[attribute];
      params["ExpressionAttributeNames"]["#" + attribute] = attribute;
      prefix = ", ";
    }
  }
}

const search = async (event) => {
  try {
    // console.log('Search received:', event);
    // const body = JSON.parse(event.body);
    // const body = {'level': ['basico', 'intermedio'], 'technology_used': 'video'};
    const body = {'q': 'myra'};
    // const body = {'technology_used': 'video'};
    console.log('Options', options, 'Table name', tableName);

    const params = {
      TableName: tableName
    };
    console.log('Search request json', body);

    const data = await processQueryFlex(body, docClient, tableName);
    return jsonResponse(200, data);
  } catch (e) {
    console.error(e);
  }
}

// search().then(r => console.log("Result", r));
search().then(r => console.log("Done"));
