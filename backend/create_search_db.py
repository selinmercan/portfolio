import boto3

dynamodb = boto3.resource('dynamodb', endpoint_url="http://localhost:8000")

import json
with open('json/search_db.json', 'r') as fp:
    params = json.load(fp)

print(params)
try:
    table = dynamodb.Table('SearchTable')
    table.delete()
    table.wait_until_not_exists()
    print("Old table deleted")
except:
    print("Table non-existent")
print("Creating new table")
table = dynamodb.create_table(**params)
table.wait_until_exists()
print("New table created")

# items = df.to_dict(orient='records')
# # table = dynamodb.Table('SearchTable')
# with table.batch_writer() as batch:
#     for i, d in enumerate(items):
#         d['id'] = str(i)
#         batch.put_item(Item=d)
