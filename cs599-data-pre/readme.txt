Files Preparation
1) To detect file types and generate json files
java typedetect.runner.TypeDetectRunner
Arguments:
  -data		base data folder
  -output	folder to store output json files
  -mimetype	path to custom mimetype.xml (optional)
		default: use tika default tika-mimetype.xml

Files generated as output
summary.json		summary of mime diversity
allRecords.json		indicates detected type of each file
byType/*.json		list of files path for each detected type


2) To separate files for each type to test and train data (75%-25% ratio except for application/octet-stream)
java typedetect.runner.SeparateTestTrainDataRunner
Arguments:
  -byType	folder that contains list of files path for each detected type (output from 1)
  -output	folder to store output json files

Files generated as output
*.json	list of files path, separated to test and train data, for each type

Content-based detection - Neural Network model

Note:
This is a Maven project. If the codes does not run properly via command line, 
please open this project in supported IDE and proper dependency management.