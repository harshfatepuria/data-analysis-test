byteFreq=[]
for i in range(0,256):
    byteFreq.append(0)
input_file=open('anagram_out.txt','rb')
data=input_file.read()
for j in range(0,len(data)):
    x=ord(data[j:(j+1)])
    print x
    byteFreq[x]=byteFreq[x]+1

for i in range(0,256):
    print i," ",byteFreq[i]