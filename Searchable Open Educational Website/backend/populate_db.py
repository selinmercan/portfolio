import mimetypes
import os
import urllib.parse
import re

import pandas as pd

pd.options.mode.chained_assignment = None  # default='warn'

df = pd.read_csv('activities.csv', keep_default_na=False)
headers = ['Location', 'TITLE-Título', 'VOCABULARY- Vocabulario', 'GRAMMAR-Gramática', 'LEVEL-Nivel',
           'AUTHOR-Autor(a)', 'TECHNOLOGY USED-Tecnología', 'SKILLS-Destrezas']

tech_col_idx = df.columns.get_loc('TECHNOLOGY USED-Tecnología')
df['AUTHOR-Autor(a)'] = df.iloc[:, [tech_col_idx - 3, tech_col_idx - 1]].apply(lambda row: ", ".join(filter(None, row)), axis=1)
df['TECHNOLOGY USED-Tecnología'] = df.iloc[:, range(tech_col_idx, tech_col_idx + 3)] \
    .apply(lambda row: ", ".join(filter(None, row)), axis=1)  # join technology together
df['TECHNOLOGY USED-Tecnología'] = df['TECHNOLOGY USED-Tecnología'].str.replace('Pear Deack', 'PearDeck', flags=re.I)
df['TECHNOLOGY USED-Tecnología'] = df['TECHNOLOGY USED-Tecnología'].str.replace('Pear Deck', 'PearDeck', flags=re.I)

df = df[headers]  # ignore none-links and n/a's
rename_d = {n: n[:len(n) if '-' not in n else n.index('-')].replace(' ', '_').lower() for n in df.columns}
df = df.rename(columns=rename_d)

# special rows
special_rows = df[df['location'].str.lower().str.startswith('link to file')]
special_links = [x[len('link to file: '):] for x in special_rows['location']]
special_rows = df.iloc[special_rows.index - 1]  # get starting rows
special_rows.title = special_rows.location
special_rows.location = special_links
for new_i, init_i in enumerate(special_rows.index):
    i = 0
    while init_i + i < len(df) and sum(df.iloc[init_i + i].str.len()) > 0:
        i += 1
    rows = df.iloc[init_i:init_i + i]
    for col in rows.columns:
        if col in ['location', 'title']:
            continue
        special_rows.iloc[new_i][col] = ", ".join(filter(None, rows[col]))

df = df[df['location'].str.startswith('http', na=False)]
df = pd.concat([df, special_rows])

# # duplicate for case-insensitive search
# for col in df.columns:
#     if col == 'location':
#         continue
#     search = "_search_" + col
#     df[search] = df[col].str.lower()

df.reset_index(drop=True, inplace=True)
df.to_csv('out.csv', index=False)

import boto3
from pathlib import Path

# switch google drive locations to s3 locations
import gdown
s3 = boto3.resource('s3')
bucket = s3.Bucket('oerinspanish.org')

fpath = Path('gdrive_files')
fpath.mkdir(exist_ok=True)
prev_path = Path.cwd()
os.chdir(fpath)
for i, link in enumerate(df.location):
    fname = gdown.download(link, quiet=True, fuzzy=True)
    mtype = mimetypes.guess_type(fname)[0]
    if mtype is None:
        mtype = 'binary/octet-stream'
        # if acl is enabled:
#     resp = bucket.upload_file(fname, f"files/{fname}", ExtraArgs={'ContentType': mtype, 'ACL': "public-read"})
    resp = bucket.upload_file(fname, f"files/{fname}", ExtraArgs={'ContentType': mtype})
#     s3_link = f"https://{bucket.name}.s3.amazonaws.com/{urllib.parse.quote(fname)}"
    s3_link = f"/files/{urllib.parse.quote(fname)}"
    print(s3_link)
    df.location.iloc[i] = s3_link

os.chdir(prev_path)
df.to_csv('out2.csv', index=False)

# raise Exception("Stop")

dynamodb = boto3.resource('dynamodb', region_name='us-west-2')


def recreate_table():
    import json
    with open('json/db.json', 'r') as fp:
        params = json.load(fp)

    # create GSIs
    # cols = df.columns
    # params.update({
    #     "LocalSecondaryIndexes": [
    #         {
    #             "IndexName": "cool_index",
    #             "KeySchema": [
    #                 {
    #                     "AttributeName": col,
    #                     "KeyType": "HASH"
    #                 } for col in cols
    #             ],
    #             "Projection": {
    #                 "ProjectionType": "ALL"
    #             },
    #             # "ProvisionedThroughput": {
    #             #     "ReadCapacityUnits": 1,
    #             #     "WriteCapacityUnits": 1
    #             # }
    #         }
    #     ]
    # })
    # if not params['AttributeDefinitions']:
    #     params['AttributeDefinitions'] = []
    # params['AttributeDefinitions'].extend([
    #     {"AttributeName": col, "AttributeType": "S"} for col in cols
    # ])

    print(params)
    try:
        table = dynamodb.Table('MainTable')
        table.delete()
        table.wait_until_not_exists()
        print("Old table deleted")
    except:
        print("Table non-existent")
    print("Creating new table")
    table = dynamodb.create_table(**params)
    table.wait_until_exists()
    print("New table created")
    return table


table = recreate_table()
items = df.to_dict(orient='records')
# table = dynamodb.Table('MainTable')
with table.batch_writer() as batch:
    for i, d in enumerate(items):
        d['id'] = str(i)
        batch.put_item(Item=d)
