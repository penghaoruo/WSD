package cs446;

public class AmbWord {
	private static int idCount=0;
	private int id; //unique id (sequential)
	private String word;
	private String pos;
	private String lemma;
	private String strID; //full id
	private int sentenceID;
	private int textID;
	private int tagID;
	private int assignedSense;
	private int[] goldSense;
	
	AmbWord() 
	{
		word="";
		strID="";
		sentenceID=0;
		textID=0;
		tagID=0;
		id=idCount++;
	}
	
	String getWord() 
	{
		return word;
	}
	
	String getPos() 
	{
		return pos;
	}
	
	String getLemma() {
		return lemma;
	}
	
	String getStrID() {
		return strID;
	}
	
	int getSentenceID() {
		return sentenceID;
	}
	
	int getTextID() {
		return textID;
	}
	
	int getTagID() {
		return tagID;
	}
	
	void setWord(String str) {
		word=str.toLowerCase();
	}
	
	void setPos(String str) {
		pos=str;
	}
	
	void setLemma(String str) {
		lemma=str.toLowerCase();
	}
	
	void setStrID(String str) {
		strID=str;
	}
	
	void setSentenceID(int value) {
		sentenceID=value;
	}
	
	void setTextID(int value) {
		textID=value;
	}
	
	void setTagID(int value) {
		tagID=value;
	}
	
	int getID(){
		return id;
	}
	
	void setAssignedSense(int value) {
		assignedSense=value;
	}
	
	int getAssignedSense() {
		return assignedSense;
	}
	
	void setGoldSense(int[] value) {
		goldSense=new int[value.length];
		for (int i=0;i<value.length;i++)
			goldSense[i]=value[i];
	}
	
	int[] getGoldSense() {
		return goldSense;
	}
}