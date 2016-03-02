Files Preparation
1) To detect file types and generate json files
java typedetect.runner.TypeDetectRunner
Arguments:
  -data		base data folder
  -output	folder to store output json files
  -mimetype	path to custom mimetype.xml (optional)
		default: use tika default tika-mimetype.xml

Files generated as output:
summary.json		summary of mime diversity
allRecords.json		indicates detected type of each file
byType/*.json		list of files path for each detected type


2) To separate files for each type to test and train data (75%-25% ratio except for application/octet-stream)
java typedetect.runner.SeparateTestTrainDataRunner
Arguments:
  -byType	folder that contains list of files path for each detected type (output from 1)
  -output	folder to store output json files

Files generated as output:
*.json	list of files path, separated to test and train data, for each type


Tika-Content based
1) Prepare datasets
java nnmodel.runner.CreateNNDatasetRunner
Arguments:
  -data		base data folder
  -byType	folder that contains list of files path for each detected type
  -output	folder to store output json files

Files generated as output:
  train.json, val.json, test.json	json file that contains path to files for each type
  train.data, val.data, test.data	tab separated files, each record contain byte frequency distribution and label

2) Detect type using neural network model
java nnmodel.runner.NNBasedTypeDetectRunner
Arguments:
  -model	path to model file
  -data 	base data folder
  -test		path to test dataset json file
  -output	path to store output file

Note:
This is a Maven project. If the codes does not run properly via command line, 
please open this project in supported IDE with proper dependency management.