# Generates a file with sparse matrix for FHT Analysis
# Header= 16 Bytes x 256 Unit Length Matrix for ONE MIME TYPE

import json
from itertools import imap
import math

input_file=open('application%2Frtf.json','r')
typeJson = json.loads(input_file.read())
input_file.close()

# print typeJson['train'][0]
byteFreqSignature=[]
for i in range(0,256):
       byteFreqSignature.append(0)

sparseMatrix=[]
colm=[]
for i in range(0,256):
       colm.append(0)

for i in range(0,16):
    sparseMatrix.append(colm)
    
output_file=open('application%2Ffht_16x256.txt','a') 
countFile=0
for k in range(0,len(typeJson['test'])):
    filePath=""+typeJson['test'][k]

    input_file=open(filePath,'rb')
    data=input_file.read()
    if(len(data)<16): continue # In case a file has no header bytes! Ignoring here.
    for j in range(0,16):
        x=ord(data[j:(j+1)])
        sparseMatrix[j][x]=sparseMatrix[j][x]+1
        countFile=countFile+1
if countFile!=0: 
    for i in range(16):
        for j in range(256):
            sparseMatrix[i][j]=sparseMatrix[i][j]/countFile
        
output_file.write(sparseMatrix)
output_file.close()
