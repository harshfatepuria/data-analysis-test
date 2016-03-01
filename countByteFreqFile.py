import json
from glob import iglob
import os
from compiler.pycodegen import EXCEPT
fileR='chosenFileTypes'
  

pathToDataStream=open("pathToDataSet.txt",'r')
sourcePath=pathToDataStream.read().replace('\n', '')

#sourcePath="/Volumes/HARSH/CS599/ORIGINAL/"  
  
for filepath in iglob(os.path.join(fileR, '*.json')): 
    with open(filepath) as f:
        typeJson = json.loads(f.read())
  
        byteFreqSignature=[]
        for i in range(0,256):
            byteFreqSignature.append(0)
   
        byteFreq=[]
        for i in range(0,256):
            byteFreq.append(0)
   
        for k in range(0,len(typeJson['train'])):
            
            filePath=sourcePath+typeJson['train'][k]
            
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
                    byteFreqSignature[i]=byteFreqSignature[i]+byteFreq[i]
   
        maxFreq=max(byteFreqSignature)
        for i in range(0,256):
            byteFreqSignature[i]=byteFreqSignature[i]/float(maxFreq)
   
        keys=json.dumps(byteFreqSignature, sort_keys=True)
        typeOfFile=str(typeJson['type'])
        qq=typeOfFile.find("/")
        outFile='json_outputs/bfa_signature_'+typeOfFile[0:qq]+"_"+typeOfFile[qq+1:]+'.json'
        output_file=open(outFile,'w')
        output_file.write(keys)
        output_file.close()
