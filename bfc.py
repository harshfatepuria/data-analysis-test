# Generates a file with test file paths and bfc coefficient.
# BFC Coefficient= -1(Highly dissimilar to +1(very similar)

import json
from itertools import imap
import math

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


input_file=open('application%2Frtf.json','r')
typeJson = json.loads(input_file.read())
input_file.close()

# print typeJson['train'][0]
byteFreqSignature=[]
for i in range(0,256):
       byteFreqSignature.append(0)

byteFreq=[]
for i in range(0,256):
       byteFreq.append(0)

output_file=open('application%2Fbfc.txt','a') # change to name of file to whatever you want
output_file2=open('application%2Fbfc_corelation.json','a')

for k in range(0,len(typeJson['test'])):
    #filePath='anagram_out.txt'
    filePath=""+typeJson['test'][k]
    for i in range(0,256):
        byteFreq[i]=0

    input_file=open(filePath,'rb')
    data=input_file.read()
    for j in range(0,len(data)):
        x=ord(data[j:(j+1)])
        byteFreq[x]=byteFreq[x]+1

    maxFreq=max(byteFreq)
    
    for i in range(0,256):
        byteFreq[i]=byteFreq[i]/maxFreq
        #byteFreqSignature[i]=byteFreqSignature[i]+byteFreq[i]
    #Get BFA of the file type in variable BFA
    print bfcPearsonCoeff(byteFreq,BFA)
    stroutput=""
    stroutput=stroutput+filepath+" "+ bfcPearsonCoeff(byteFreq,BFA)+"\n"
    bfc_corelation= [abs(i - j) for i, j in zip(byteFreq, BFA)]
    keys=json.dumps(bfc_corelation, sort_keys=True)
    output_file.write(stroutput)
    output_file2.write(keys)

output_file.close()
output_file2.close()
