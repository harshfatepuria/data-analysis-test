README 599 #1


1. We use D3 on the file distribution available in GitHub to create a Pie chart (completeDataPieChart.html) indicating the existing MIME diversity of the TREC- DD-Polar dataset
2. Please specify the path to the entire dataset in the file named ‘pathToDataSet.txt’
	Example, /Volumes/HARSH/CS599/ORIGINAL/




3. We used Apache Tika on the entire data set to create the following:

	a.  A summary of the count of different types of files present:  initialSummary.json 
	b.  A dictionary file for each file type storing the file_type, array of paths to the training data (75%), and array of paths to the testing data (25%)
	     This file is very important because we refer to it for the paths to each file in a particular type.



4. We choose 15 types of files to work upon (specified in ‘MIME Types to be Analysed.txt’), and store the .json dictionary file (described in 2.b) in a directory named chosenFileTypes
5. Perform BFA analysis on 75% of the data set from the chosen 15 types to obtain Byte Frequency Signatures for each file type. For this, execute countByteFreqFile.py. The 15 output .json files are stored in a directory named json_outputs. These output files store an array of size 256 indicating the normalized frequency of the count of each type of byte present in the training files.
6. The resulting D3 Line Chart for the BFA signatures can be seen in D3_BFA_Signature.html 
7. We write a program to perform BFC analysis on an input file by comparing it with its BFA signature. For this, execute bfc_correlation.py  with 2 parameters <type of file> <path to file>

	Example, python bfc_correlation.py 'application/x-sh' 'org/aoncadis/www/C21774DD044605C31ADAA82A7A81BB7E03175A3875752F6AC3E256118D55D548'

	This program generates two output .json files named bfc_corelation_difference_output.json and bfc_corelation_values_output.json in the bfc_corelation directory.
	The D3 visualization to see areas of high and low correlation can be seen in D3_BFC_Correlation_Difference_LineChart.html.
	We also created a D3 Multi-Line Chart visualization (D3_BFC_Comparison_Multiline.html) indicating both the BFA Signature and the Normalized Frequency distribution for the input file.

8. Cross correlation.

9. We perform FHT analysis on the training data to get a FHT signature sparse matrix of size 16 X 256 for each of the 15 types of files chosen. For this, execute fht_16x256.py. The resulting outputs are 15 .json files storing the FHT signatures in the fht_output directory. 
	We use D3 Heat map visualization to depict the FHT sparse matrix in D3_FHT_Signature_heatMap.html for all the 15 types.

10. EXTRA CREDIT README




