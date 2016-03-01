# Generates a file with test file paths and bfc coefficient.
# FHT Coefficient= -1(Highly dissimilar to +1(very similar) Header Bytes


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
        
        f=open('fht_output/fht_signature_'+typeOfFile[0:qq]+"_"+typeOfFile[qq+1:]+'.json','r')
        FHT = json.loads(f.read())
        f.close()
        
        FHT_MAX_VALUES= [FHT["0"],FHT["1"],FHT["2"],FHT["3"],FHT["4"],FHT["5"],FHT["6"],FHT["7"],FHT["8"],FHT["9"],FHT["10"],FHT["11"],FHT["12"],FHT["13"],FHT["14"],FHT["15"]]
   
        byteFreq=[]
        for i in range(0,16):
            byteFreq.append(0)
   
        outFile='FHT_4B/fht_analysis_'+typeOfFile[0:qq]+"_"+typeOfFile[qq+1:]+'.json'
        output_file=open(outFile,'a')
        
        
        for k in range(0,len(typeJson['test'])):
            
            filePath=sourcePath+typeJson['test'][k]
            
            print typeJson['type']," ",k, " ", (typeJson['trainCount']-1),
            
            for i in range(0,16):
                byteFreq[i]=0
            try:
                input_file=open(filePath,'rb')
                data=input_file.read()
            except:
                continue
            print len(data)
            if len(data)>0:
                for j in range(0,16):
                    x=ord(data[j:(j+1)])
                    byteFreq.append(x)
                    
                x=bfcPearsonCoeff(byteFreq[0:4],FHT_MAX_VALUES[0:4])
                y=bfcPearsonCoeff(byteFreq[0:8],FHT_MAX_VALUES[0:8])
                z=bfcPearsonCoeff(byteFreq,FHT_MAX_VALUES)
                print x
                stroutput=""
                stroutput=stroutput+filepath+" "+x+" "+y+" "+z+"\n"
                output_file.write(stroutput)

        output_file.close()
