# Generates BFC Cross Corelation matrix for 15 all MIME Types

import json
from glob import iglob
import os
from compiler.pycodegen import EXCEPT
from itertools import imap
import math

fileR='chosenFileTypes'
  
sourcePath="/Volumes/HARSH/CS599/ORIGINAL/"
  
for filepath in iglob(os.path.join(fileR, '*.json')): 
    with open(filepath) as f:
        typeJson = json.loads(f.read())
  
        typeOfFile=str(typeJson['type'])
        qq=typeOfFile.find("/")
        
        byteFreq=[]
        for i in range(0,256):
            byteFreq.append(0)
            
        crossMatrix=[]
        for i in range(0,256):
            crossMatrix.append([])
            for j in range(0,256):
                crossMatrix[i].append(-1)
   
        outFile='bfc_cross_corelation/bfc_cross_'+typeOfFile[0:qq]+"_"+typeOfFile[qq+1:]+'.json'
        output_file=open(outFile,'w')
        countFile=0
        
        
        for k in range(0,len(typeJson['test'])):
            
            countFile+=1
            filePath=sourcePath+typeJson['test'][k]
            
            print typeJson['type']," ",k, " ", (typeJson['trainCount']-1),
            
            for i in range(0,256):
                byteFreq[i]=0
            try:
                input_file=open(filePath,'rb')
                data=input_file.read()
            except:
                continue
            print len(data)
            if len(data)>0:
                for j in range(0,len(data)):
                    x=ord(data[j:(j+1)])
                    byteFreq[x]=byteFreq[x]+1
   
   
                for a in range(0,256):
                    for b in range(0,256):
                        if(b>a):
                            crossMatrix[a][b]+= abs(byteFreq[a]-byteFreq[b])
        for a in range(0,256):
            for b in range(0,256):
                if(b>a)and countFile!=0:
                    crossMatrix[a][b]/= float(countFile)
        
        keys=json.dumps(crossMatrix, sort_keys=True)
        output_file.write(keys)
        output_file.close()