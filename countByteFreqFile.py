import json

input_file=open('application%2Frtf.json','r')
typeJson = json.loads(input_file.read())
input_file.close()

print typeJson['train'][0]
byteFreqSignature=[]
for i in range(0,256):
   	byteFreqSignature.append(0)

byteFreq=[]
for i in range(0,256):
   	byteFreq.append(0)

for k in range(0,len(typeJson['train'])):
#     filePath='anagram_out.txt'
    filePath=""+typeJson['train'][k]
    for i in range(0,256):
        byteFreq[i]=0

	input_file=open(filePath,'rb')
	data=input_file.read()
    for j in range(0,len(data)):
        x=ord(data[j:(j+1)])
    	byteFreq[x]=byteFreq[x]+1

    for i in range(0,256):
    	byteFreqSignature[i]=((byteFreqSignature[i]*k)+byteFreq[i])/(k+1)



for i in range(0,256):
    print i," ",byteFreqSignature[i]




keys=json.dumps(byteFreqSignature, sort_keys=True)

output_file=open('freq.json','w')
output_file.write(keys)
output_file.close()