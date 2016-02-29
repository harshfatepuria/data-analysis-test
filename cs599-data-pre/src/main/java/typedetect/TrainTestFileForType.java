package typedetect;

import java.util.ArrayList;
import java.util.List;

public class TrainTestFileForType {
	private String type;
	private List<String> train;
	private List<String> test;
	
	public TrainTestFileForType() {
		train = new ArrayList<>();
		test = new ArrayList<>();
	}
	
	public TrainTestFileForType(FileContentTypeSummary summary) {
		this();
		setType(summary.getType());
		separateFiles(summary);
	}
	
	private void separateFiles(FileContentTypeSummary summary) {
		Integer trainCount;
		Integer testCount;
		
		if (summary.getType().equals("application/octet-stream")) {
			trainCount = 50000;
			testCount = Math.min(50000, summary.getCount() - 50000);
		} else {
			Double t = Math.ceil(0.75 * summary.getCount());
			trainCount = t.intValue();
			testCount = summary.getCount() - trainCount;
		}
		
		List<String> files = summary.getFiles();
		for(Integer i = 0; i < trainCount; i++) {
			train.add(files.get(i));
		}
		
		for(Integer i = trainCount; i < trainCount + testCount; i++) {
			test.add(files.get(i));
		}
	}
	
	public Integer getTrainCount() {
		return train.size();
	}
	public void setTrainCount(Integer trainCount) {

	}
	public Integer getTestCount() {
		return test.size();
	}
	public void setTestCount(Integer testCount) {

	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getTrain() {
		return train;
	}
	public void setTrain(List<String> train) {
		this.train = train;
	}
	public List<String> getTest() {
		return test;
	}
	public void setTest(List<String> test) {
		this.test = test;
	}
	
	
	
}
