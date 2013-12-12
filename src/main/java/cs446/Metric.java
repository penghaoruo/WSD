package cs446;

public enum Metric
{
	LeacockChodorow(1),
	Lesk(2),
	WuPalmer(3),
	Resnick(4),
	Lin(5),
	Jiangconrath(6);
	
	private int value;
	private Metric(int val){
		this.value=val;
	}
	public int getVal(){
		return value;
	}
};