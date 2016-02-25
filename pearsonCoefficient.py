# Generates a file with test file paths and bfc coefficient.
# BFC Coefficient= -1(Highly dissimilar to +1(very similar)


import json
from glob import iglob
import os
from compiler.pycodegen import EXCEPT
from itertools import imap
import math

fileR='chosenFileTypes'
  
sourcePath="/Volumes/HARSH/CS599/ORIGINAL/"

def bfcPearsonCoeff(x, y):
    n = len(x)
    sumX = float(sum(x))
    sumY = float(sum(y))
    sumX2 = sum(map(lambda x: pow(x, 2), x))
    sumY2 = sum(map(lambda x: pow(x, 2), y))
    psum = sum(imap(lambda x, y: x * y, x, y))
    a = psum - (sumX * sumY/n)
    b = pow((sumX2 - pow(sumX, 2) / n) * (sumY2 - pow(sumY, 2) / n), 0.5)
    if b == 0: return 0
    return a / b
  
  
for filepath in iglob(os.path.join(fileR, '*.json')): 
    with open(filepath) as f:
        typeJson = json.loads(f.read())
  
        typeOfFile=str(typeJson['type'])
        qq=typeOfFile.find("/")
        
        f=open('json_outputs/bfa_signature_'+typeOfFile[0:qq]+"_"+typeOfFile[qq+1:]+'.json','r')
        BFA = json.loads(f.read())
        f.close()
   
        byteFreq=[]
        for i in range(0,256):
            byteFreq.append(0)
   
        outFile='pearson_Coefficient/correlation_coef_'+typeOfFile[0:qq]+"_"+typeOfFile[qq+1:]+'.txt'
        output_file=open(outFile,'a')
        
        
        for k in range(0,len(typeJson['test'])):
            
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
   
                maxFreq=max(byteFreq)
                for i in range(0,256):
                    byteFreq[i]=byteFreq[i]/float(maxFreq)
                
                print bfcPearsonCoeff(byteFreq,BFA)
                stroutput=""
                stroutput=stroutput+filepath+" "+ bfcPearsonCoeff(byteFreq,BFA)+"\n"
                output_file.write(stroutput)

        output_file.close()

