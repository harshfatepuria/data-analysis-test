package byteanalysis;

import java.util.Arrays;
import java.util.function.DoubleBinaryOperator;


public class BFA {
	private float[] frequencies;
	
	public BFA() {
		initializeFrequencies();
	}
	
	public BFA(byte[] data) {
		this();
		calculateByteFrequencies(data);
	}
	
	protected BFA(float[] frequencies) {
		this.frequencies = frequencies.clone();
	}
	

	protected void initializeFrequencies() {
		frequencies = new float[256];
		Arrays.fill(frequencies, 0.0f);
	}
	
	private void calculateByteFrequencies(byte[] data) {
		for(Byte b : data) {
			Integer unsignedB = b &0xff; /* convert to unsigned byte */
			frequencies[unsignedB]+= 1;
		}
	}
	
	public BFA normalize() {
		float maxFreq = getMaxFrequency();
		float[] normFrequencies = new float[frequencies.length];
		for(int i = 0; i < frequencies.length; i++) {
			normFrequencies[i] = frequencies[i] / maxFreq;
		}
		
		BFA normalizedBFFD = new BFA(normFrequencies);
		return normalizedBFFD;
	}
	
	private float getMaxFrequency() {
		float max = -1;
		
		for(float f : frequencies) {
			max = Math.max(max, f);
		}
		
		return max;
	}
	
	private static final DoubleBinaryOperator absoluteDifference = (a, b) -> Math.abs(a - b);
	
	public BFA computeCorrelation(BFA other) {
		return computeCorrelation(other, absoluteDifference);
	}
	
	public BFA computeCorrelation(BFA other, DoubleBinaryOperator op) {
		float[] resultFreq = new float[frequencies.length];
		for(int i = 0; i < resultFreq.length; i++) {
			resultFreq[i] = (float)op.applyAsDouble(this.frequencies[i], other.frequencies[i]);
		}
		
		BFA correlatedBFD = new BFA(resultFreq);
		return correlatedBFD;
	}
	
	public float[] getFrequencies() {
		return frequencies;
	}

	public void setFrequencies(float[] frequencies) {
		this.frequencies = frequencies;
	}
	
}
