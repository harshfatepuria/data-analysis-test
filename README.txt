README 
CSCI 599 - Assignment 1 - Team 22


1. We use D3 on the file distribution available in GitHub to create a Pie chart (completeDataPieChart.html) indicating the existing MIME diversity of the TREC- DD-Polar dataset

2. Please specify the path to the entire dataset in the file named ‘pathToDataSet.txt’
	Example, 

/Volumes/HARSH/CS599/ORIGINAL/



3. We use Apache Tika on the entire data set to create the following:
	a.  A summary of the count of different types of files present:  initialSummary.json 
	b.  A dictionary file for each file type storing the file_type, array of paths to the training data (75%), and array of paths to the testing data (25%)
	     This file is very important because we refer it for the paths to each file in a particular MIME type.
		 
	Files Preparation
	a) Build 'cs599-data-pre' as a java project
	b) To detect file types and generate .json files
		java typedetect.runner.TypeDetectRunner
	Arguments:
		-data		base data folder
		-output		folder to store output json files
		-mimetype	path to custom mimetype.xml (optional)
					default: use tika default tika-mimetype.xml
	Files generated as output:
		summary.json		summary of mime diversity
		allRecords.json		indicates detected type of each file
		byType/*.json		list of files path for each detected type
	
	c) To separate files for each type to test and train data (75%-25% ratio except for application/octet-stream)
		java typedetect.runner.SeparateTestTrainDataRunner
	Arguments:
		-byType	folder that contains list of files path for each detected type (output from 1)
		-output	folder to store output json files
	Files generated as output:
		*.json		list of files path, separated to test and train data, for each type



4. Choose 15 types of files to work upon (specified in ‘MIME Types to be Analysed.txt’), and store the JSON dictionary file (described in 3.c) in a directory named -- chosenFileTypes

5. Perform BFA analysis on 75% of the data set from the chosen 15 types to obtain Byte Frequency Signatures for each file type. For this, execute countByteFreqFile.py. The 15 output .json files are stored in a directory named json_outputs. These output files store an array of size 256 indicating the normalized frequency of the count of each type of byte present in the training files.
6. The resulting D3 Line Chart for the BFA signatures can be seen in D3_BFA_Signature.html 
7. We write a program to perform BFC analysis on an input file by comparing it with its BFA signature. For this, execute bfc_correlation.py  with 2 parameters <type of file> <path to file>

	Example, python bfc_correlation.py 'application/x-sh' 'org/aoncadis/www/C21774DD044605C31ADAA82A7A81BB7E03175A3875752F6AC3E256118D55D548'

	This program generates two output .json files named bfc_corelation_difference_output.json and bfc_corelation_values_output.json in the bfc_corelation directory.
	The D3 visualization to see areas of high and low correlation can be seen in D3_BFC_Correlation_Difference_LineChart.html.
	We also created a D3 Multi-Line Chart visualization (D3_BFC_Comparison_Multiline.html) indicating both the BFA Signature and the Normalized Frequency distribution for the input file.


8. Perform Byte Frequency Cross Correlation on the test data over all the 15 MIME types by executing -- bfc_cross_corelation.py
	The resulting JSON files are stored in --  bfc_cross_corelation -- directory, which in turn are used to generate the D3 visualizations. The visualization is seen in -- D3_Cross_Correlation_Signature_HeatMap.html




9. Perform FHT analysis on the training data to get a FHT signature sparse matrix of size 16 X 256 for each of the 15 types of files chosen. For this, execute -- fht_16x256.py -- The resulting outputs are 15 JSON files storing the FHT signatures in the -- fht_output -- directory. 
	Use D3 Heat map visualization to depict the FHT sparse matrix in -- D3_FHT_Signature_heatMap.html -- for all the 15 types.

10. EXTRA CREDIT README

	10.1 Clustering using Tika-Similarity
		After placing modified source code in Tika-Similarity project. We can run these script.
		1) Clustering - run k-means.py to do clustering using different distance measures
			python k-means.py
		Arguments:
			--inputDir	path to directory containing files to cluster
			--measure	specified distance measure
						0 - Euclidean, 1 - Cosine, 2 - Edit (default: 0)
		Output:
			cluster.json	clustering result

		2) Circle Packing - using clustering result (using cluster.json file generated from k-means.py)
			python circle-packing-json.py
		Output:
			circle.json	circle packing result
	
	10.2 Content Based MIME Detection
		1) Prepare datasets by running a java class (Arguments still hard coded)
			java nnmodel.runner.CreateNNDatasetRunner
		Arguments:
			-data		base data folder
			-byType		folder that contains list of files path for each detected type
			-output		folder to store output json files
		Output:
			train.json, val.json, test.json		json file that contains path to files for each type
			train.data, val.data, test.data		tab separated files, each record contain byte frequency distribution and label

		2) After placing modified R scripts and data files in "filetypeDetection" project, we can run R script to train neural network model
			source('main2.r', echo = T)  in R Console

		3) Use the model to do type detection by running a java class (Arguments still hard coded)
			java nnmodel.runner.NNBasedTypeDetectRunner
		Arguments:
			-model		path to model file
			-data 		base data folder
			-test		path to test dataset json file
			-output		path to store output file

