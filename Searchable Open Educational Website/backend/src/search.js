const {Document} = require("flexsearch");
const searchHeaders = ['title', 'vocabulary', 'grammar', 'level', 'author', 'technology_used', 'skills'];
module.exports.searchHeaders = searchHeaders;
const param_settings = ['limit', 'offset'];
const searchTableName = "SearchTable";
const searchDbId = "_id";

let docIndex;
let allTableData;
let allDataFilled = false;
let tableData;

const indexOptions = {
  preset: "default",
  charset: "latin:advanced",
  language: "es",
  tokenize: "full",
  index: searchHeaders,
  // store: [...searchHeaders, "location"]
}

// const LocalStorage = require('node-localstorage').LocalStorage,
//   localStorage = new LocalStorage('./scratch');

module.exports.processQueryDynamo = async function (query, params, docClient) {
  // const data = (await docClient.scan(params).promise()).Items;
  // let keyConditions = Object.entries(body).map(([key, val], _) => [key, val])
  //   .reduce((map, obj) => (map[obj[0]] = {
  //     ComparisonOperator: "EQ",
  //     AttributeValuesList: [{S: obj[1]}]
  //   }, map), {});
  // Object.assign(params, {
  //   KeyConditions :
  //     keyConditions
  // })
  let q;
  let qterm;
  if ('q' in query) {
    q = query.q;
    delete query.q;
    qterm = searchHeaders.map((header, _) => `contains(#${header}, :q)`).join(" or ");
  }
  let queryTerm = Object.entries(query).map(([key, val], idx) => `contains(#k${idx}, :v${idx})`).join(" and ");
  if (typeof qterm !== 'undefined') {
    if (queryTerm !== "") {
      qterm = ` and (${qterm})`
    }
    queryTerm += qterm;
  }
  Object.assign(params, {
    FilterExpression: queryTerm,
    ExpressionAttributeNames:
      Object.entries(query).map(([key, val], _) => `_search_${key.toLowerCase()}`) // use the search columns
        .reduce((map, key, idx) => (map[`#k${idx}`] = key, map), {}),
    ExpressionAttributeValues:
      Object.entries(query).map(([key, val], _) => val.toLowerCase())
        .reduce((map, val, idx) => (map[`:v${idx}`] = val, map), {}),
  });
  if (typeof q !== 'undefined') {
    Object.assign(params.ExpressionAttributeNames, searchHeaders.reduce((map, header, _) => (map[`#${header}`] = header, map), {}));
    params.ExpressionAttributeValues[':q'] = q;
  }
  console.log("Query params:", params);
  const data = (await docClient.scan(params).promise()).Items;
  // console.log("Data from db", data);
  // get rid of search columns
  data.forEach(item => Object.keys(item).forEach(key => {
    if (key.startsWith("_search_")) delete item[key];
  }))
  console.log('Requested data:', data, `Count: ${data.length}`);
  return data;
}

const isObjEmpty = obj => Object.keys(obj).length === 0;

async function getAllTableData(docClient, tableName) {
  if (!allTableData) {
    allTableData = (await docClient.scan({TableName: tableName}).promise()).Items;
  }
  return allTableData;
}

const indexDocs = async (docClient, tableName) => {
  let start = new Date().getTime();
  const docIndex = new Document(indexOptions);
  const data = await getAllTableData(docClient, tableName);
  data.forEach(item => docIndex.add(item));
  console.log("Indexing took", new Date().getTime() - start, 'ms');

  // todo skipped exporting until bug is fixed
  // // save index to db
  // start = new Date().getTime();
  // const exportItem = {[searchDbId]: "flexsearch"};
  // await docIndex.export((key, data) => {
  //   console.log("Index key:", key);
  //   // localStorage.setItem(key, data);
  //   exportItem[key] = data;
  // });
  // await docCl
  // ient.put({TableName: searchTableName, Item: exportItem}).promise();
  // console.log("Saving index took", new Date().getTime() - start, 'ms');

  return docIndex;
}

const loadIndex = async (docClient) => {
  return null; // todo skip loading until bug is fixed
  let start = new Date().getTime();
  const docIndex = new Document(indexOptions);
  try{
    const indexResult = (await docClient.get({TableName: searchTableName, Key: {[searchDbId]: "flexsearch"}}).promise()).Item;
    if (!indexResult) {
      console.log("Index not found in db");
      return null;
    }
    for (const [key, val] of Object.entries(indexResult)) {
      if (key === searchDbId) continue;
      docIndex.import(key, val);
    }
  }catch (e) {
    console.error("Error loading index from db", e);
    return null;
  }
  console.log("Loading index from DB took", new Date().getTime() - start, 'ms');
  return docIndex;
  // for(let i = 0, key; i < keys.length; i++){
  //   key = keys[i];
  //   docIndex.import(key, localStorage.getItem(key));
  // }
}

const getDocIndex = async (docClient, tableName) => {
  console.log("Getting doc index");
  if (typeof docIndex === 'undefined') {
    docIndex = await loadIndex(docClient);
    if (!docIndex) {
      docIndex = await indexDocs(docClient, tableName);
    }
  }
  return docIndex;
}

const getLocalDoc = (id) => {
  if (!allDataFilled && allTableData) {
    tableData = {};
    for (const item of allTableData){
      tableData[item.id] = item;
    }
    allDataFilled = true;
  }
  if (!tableData) {
    tableData = {};
  }
  return tableData[id]; // return undefined if not in local cache
}

async function getDocs(ids, docClient, tableName) {
  const getParams = {
    RequestItems: {
      [tableName]: {
        Keys: ids.map(id => ({id: id}))
      }
    }
  }
  return (await docClient.batchGet(getParams).promise()).Responses[tableName];
}

async function enrichResults(res, docClient, tableName) {

  const enrichedList = Array(res.length);

  const fromDbList = [];
  const fromDbIdxList = [];

  for (let i = 0, id; i < res.length; i++) {

    id = res[i];

    const localDoc = getLocalDoc(id);
    if (localDoc && i !== 1) {
      enrichedList[i] = localDoc;
    } else {
      fromDbList.push(id);
      fromDbIdxList.push(i);
    }
  }

  if (fromDbList.length > 0) {
    const list = await getDocs(fromDbList, docClient, tableName);
    if (!tableData){
      tableData = {};
    }
    for (let i = 0; i < list.length; i++){
      const doc = list[i];
      tableData[doc.id] = doc; // add to local cache
      enrichedList[fromDbIdxList[i]] = doc;
    }
  }

  return enrichedList;
}

module.exports.processQueryFlex = async function (query, docClient, tableName) {
  tableName = "MainTable"
  if (!query || isObjEmpty(query)){
    return [];
  }
  let q;
  let params = {};
  if ('q' in query) {
    q = query.q;
    delete query.q;
  }

  function processParams(param) {
    if (param in query) {
      params[param] = query[param];
      delete query[param];
    }
  }

  let skipTransform = false;
  let result;
  param_settings.forEach(s => processParams(s));

  if (Object.keys(query).length === 1){ // one field
    const [k, v] = Object.entries(query)[0];
    if (v === ''){ // get all possible values for the field/column
      const data = await getAllTableData(docClient, tableName);
      return [... new Set(data.map(obj => obj[k]))]
    }
  }

  let limit = params.limit;
  let offset = params.offset;

  if (isObjEmpty(query)) { // simple full text search
    // params.enrich = true;
    if (q === '') { // display all results
      result = await getAllTableData(docClient, tableName);
      skipTransform = true;
    } else {
      if (typeof docIndex === 'undefined') docIndex = await getDocIndex(docClient, tableName);
      console.log("doc index size", Object.keys(docIndex.register).length);
      limit = offset = null; // no need to filter again
      result = await docIndex.search(q, params);
      result = result.map(res => res.result).flat(1); // flatten to 1d array
    }
  }
  else { // per field queries
    if (typeof docIndex === 'undefined') docIndex = await getDocIndex(docClient, tableName);
    console.log("doc index size", Object.keys(docIndex.register).length);
    // delete global settings
    delete params.limit;
    delete params.offset;
    let qResult;
    if (q && q.length > 0) {
      // searchList.push.apply(searchList, searchHeaders.map(val => ({...params, field: val, query: q})));
      qResult = await docIndex.search(q, params);
      if (qResult.length === 0){
        return []; // global query didn't match anything
      }
    }
    const searchList = Object.entries(query).flatMap(([key, val]) =>
      typeof val === 'string' ? {...params, field: key, query: val} : val.map(term => ({...params, field: key, query: term}))
    );
    console.log("Search list", searchList);
    try {
      result = await docIndex.search(searchList);
    } catch (e) {
      if (e instanceof TypeError){
        console.log("Invalid request params, returning empty");
        return [];
      }
    }
    console.log("Search results", result);
    // merge multi term results into one list
    const newResult = {};
    for (const res of result){
      if (res.field in newResult)
        newResult[res.field] = Array.from(new Set(newResult[res.field].concat(res.result))); // concat and de-duplicate
      else
        newResult[res.field] = res.result;
    }
    result = newResult;

    if (qResult) { // global query result
      qResult = Array.from(new Set(qResult.flatMap(res => res.result)));
      result.q = qResult;
    }
    if (isObjEmpty(result)) return [];
    if (true) {
      // intersect all results to get AND behavior
      Object.keys(result).forEach(key => delete query[key]);
      if (!isObjEmpty(query)){ // have fields not matched at all
        return [];
      }
      // intersect all fields from https://stackoverflow.com/a/51874332/8170714
      result = Object.values(result).reduce((prevVal, currVal) => prevVal.filter(v => currVal.includes(v)));

      // result = [{result: result.map(res => {
      //     if (res.field in query) delete query[res.field];
      //     return res.result;
      //   }).reduce((a, b) => a.filter(c => b.includes(c)))}]; // from https://stackoverflow.com/a/51874332/8170714
      // if (!isObjEmpty(query)){ // have fields not matched at all
      //   result = [];
      // }
    }
    if (result.length === 0) return [];
  }
  if (!skipTransform) {
    // result = result.map(res => {
    //   if (res.result && res.result.length > 0 && !isNaN(res.result[0])){ // not enriched, bug https://github.com/nextapps-de/flexsearch/issues/264
    //     return res.result.map(id => {
    //       const doc = docIndex.get(id);
    //       doc.id = id;
    //       return doc;
    //     });
    //   } else return res.result.map(doc => {
    //     doc.doc.id = doc.id;
    //     return doc.doc;
    //   });
    // }).flat(1); // flatten to 1d array
    result = await enrichResults(Array.from(new Set(result)), docClient, tableName);
  }

  // need final manual offset and limit
  if (offset) {
    result = result.slice(0, offset);
  }
  if (limit) {
    result = result.slice(0, limit);
  }

  console.log("Search params", params, "Result", result, result.length);
  return result;
}
