# Given an input file type and file path, this calculates the BFC
# of the file compared to its signature

import json
from itertools import imap
import math
from itertools import imap
import math
import sys

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



input_file_type= sys.argv[1]
input_file=sys.argv[2]

byteFreq=[]
for i in range(0,256):
       byteFreq.append(0)

output_file=open('bfc_corelation_difference_output.json','a') # change to name of file to whatever you want
output_file2=open('bfc_corelation_values_output.json','a')

f=open(input_file,'rb')
data=f.read()
f.close()

for j in range(0,len(data)):
    x=ord(data[j:(j+1)])
    byteFreq[x]=byteFreq[x]+1

maxFreq=max(byteFreq)

for i in range(0,256):
    byteFreq[i]=byteFreq[i]/maxFreq
    
typeOfFile=str(argv[1])
qq=typeOfFile.find("/")

#Get BFA of the file type in variable BFA
fp=open('json_outputs/bfa_signature_'+typeOfFile[0:qq]+"_"+typeOfFile[qq+1:]+'.json','r')
BFA = json.loads(f.read())
fp.close()

print "BFC CORELATION CO-EFFICIENT: ", bfcPearsonCoeff(byteFreq,BFA)

bfc_corelation= [abs(i - j) for i, j in zip(byteFreq, BFA)]
keys=json.dumps(bfc_corelation, sort_keys=True)
output_file.write(keys)

dict={}
for i in range(256):
    dict[i]= [byteFreq[i],BFA[i]]
keys=json.dumps(dict, sort_keys=True)
output_file2.write(keys)

output_file.close()
output_file2.close()
