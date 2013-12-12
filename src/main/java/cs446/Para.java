package cs446;

public class Para {
	public String path;
	public String similarityMetric;
	public String centralityMetric;
	public String windowSize;
	
	public void init(String[] args) {
		path=args[0];
		similarityMetric=args[1];
		centralityMetric=args[2];
		windowSize=args[3];
	}
}