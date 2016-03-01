 # Generates a file with sparse matrix for FHT Analysis
# Header= 16 Bytes x 256 Unit Length Matrix for ONE MIME TYPE

import json
from itertools import imap
import math
from glob import iglob
import os


fileR='chosenFileTypes'

pathToDataStream=open("pathToDataSet.txt",'r')
sourcePath=pathToDataStream.read().replace('\n', '')

#sourcePath="/Volumes/HARSH/CS599/ORIGINAL/" 


for filepath in iglob(os.path.join(fileR, '*.json')): 
    with open(filepath) as f:
        typeJson = json.loads(f.read())
        print typeJson['type']



        sparseMatrix=[]
        for i in range(0,16):
            sparseMatrix.append([])
            for j in range(0,256):
                sparseMatrix[i].append(0)
    
        
        
        countFile=0

        for k in range(0,len(typeJson['train'])):
            filePath=sourcePath+typeJson['train'][k]
#             print typeJson['type']," ",k, " ", (typeJson['trainCount']-1),
            
            try:
                input_file=open(filePath,'rb')
                data=input_file.read()
            except:
                print filePath
                continue
            if(len(data)<16): 
                continue # In case a file has no header bytes! Ignoring here.
            
            for j in range(0,16):
                x=ord(data[j:(j+1)])
                sparseMatrix[j][x]=sparseMatrix[j][x]+1
            
            countFile=countFile+1
    
        if countFile!=0: 
            for i in range(16):
                for j in range(256):
                    sparseMatrix[i][j]=float(sparseMatrix[i][j])/float(countFile)
        
        sparseMatrixDict={}
        for i in range(16):
            sparseMatrixDict[i]=sparseMatrix[i]
        keys=json.dumps(sparseMatrixDict, sort_keys=True)
        
        typeOfFile=str(typeJson['type'])
        qq=typeOfFile.find("/")
        outFile='fht_output/fht_signature_'+typeOfFile[0:qq]+"_"+typeOfFile[qq+1:]+'.json'
        output_file=open(outFile,'w') 
        
        output_file.write(keys)
        output_file.close()






#  # Generates a file with sparse matrix for FHT Analysis
# # Header= 16 Bytes x 256 Unit Length Matrix for ONE MIME TYPE
# 
# import json
# from itertools import imap
# import math
# 
# input_file=open('application%2Frtf.json','r')
# typeJson = json.loads(input_file.read())
# input_file.close()
# 
# # print typeJson['train'][0]
# sparseMatrix=[]
# for i in range(0,16):
#     sparseMatrix.append([])
#     for j in range(0,256):
#        sparseMatrix[i].append(0)
#     
# output_file=open('application%2Ffht_16x256.json','w') 
# countFile=0
# 
# for k in range(0,len(typeJson['test'])):
#     filePath=""+typeJson['test'][k]
#     try:
#         input_file=open(filePath,'rb')
#         data=input_file.read()
#     except:
#         continue
#     if(len(data)<16): 
#         continue # In case a file has no header bytes! Ignoring here.
#     
#     for j in range(0,16):
#         x=ord(data[j:(j+1)])
#         sparseMatrix[j][x]=sparseMatrix[j][x]+1
#     
#     countFile=countFile+1
#     
# if countFile!=0: 
#     for i in range(16):
#         for j in range(256):
#             sparseMatrix[i][j]=float(sparseMatrix[i][j])/float(countFile)
# 
# sparseMatrixDict={}
# for i in range(16):
#     sparseMatrixDict[i]=sparseMatrix[i]
# keys=json.dumps(sparseMatrixDict, sort_keys=True)
# output_file.write(keys)
# #output_file.write(sparseMatrixDict)
# output_file.close()
