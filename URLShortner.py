import uuid
import json
import os
from glob import iglob
from pprint import pprint

mapping={}
mapping['URL']=[]
#Getting JSON file of initial Tika parsing containing list of file paths categorized by MIME types
file="C:/Users/rahul/Documents/GitHub/Scientific-Content-Enrichment-in-the-Text-Retrieval-Conference-TREC-Polar-Dynamic-Domain-Dataset/fulldump-path-all-json/"

outFile='output-from-url-shortner-all-types'+'.json'
output_file=open(outFile,'w')

for filepath in iglob(os.path.join(file, '*.json')):
    with open(filepath) as data_file:    
        data = json.load(data_file)
        for i in data['files']:
            #Getting a unique md5 hash for the file path relative to the current directory
            d={}
            d['filePath']=i
            
            s="polar.usc.edu/"+str(uuid.uuid4())[:8]
            d['shortURL']=s
            mapping['URL'].append(d)
            
            print "\'"+ i+ "\'" + " : " +"\'"+ s+ "\'"
            #print dispString 
            #output_file.write(dispString)
            
        data_file.close()

#Dumping JSON object with mapped shortened URLs and file path
keys=json.dumps(mapping, sort_keys=True)
output_file.write(keys)
output_file.close()
