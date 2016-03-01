First, run the java class to create datasets.
java nnmodel.runner.CreateNNDatasetRunner

It will create 3 dataset; training, validation and test.
Each dataset contains a json file that refer the path and its type.
And also a .data file which is byte frequency distribution of each file and its label.

Run modified R script (main2.R) to build a neural network model.

Run another java class to use this model and try to detect file type.
java nnmodel.runner.NNBasedTypeDetectRunner
