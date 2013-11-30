package cs446;

import java.util.ArrayList;
import java.util.Vector;

public class Doc {
	private String content; // plain text
	private Vector<ArrayList<String>> sentences;
	private String id;
	private ArrayList<AmbWord> ambWords;
	
	Doc() {
		content=new String("");
		id=new String("");
		sentences=new Vector<ArrayList<String>>();
		ambWords=new ArrayList<AmbWord>();
	}
	
	String getContent() {
		return content;
	}
	
	void setContent(String str) {
		content=str;
	}
	
	String getID() {
		return id;
	}
	
	void setID(String str) {
		id=str;
	}
	
	int getSentenceNum() {
		return sentences.size();
	}
	
	ArrayList<String> getSentence(int index) {
		if (index>=sentences.size())
			System.out.println("Get Sentence Error: Out of Bound!");
		return sentences.elementAt(index);
	}
	
	void setSentences(String str,int index) {
		sentences.elementAt(index).add(str);
	}
	
	public void addSentence() {
		sentences.add(new ArrayList<String>());
	}
	
	void addAmbWord(AmbWord x) {
		ambWords.add(x);
	}
	
	ArrayList<AmbWord> getAmbWords() {
		return ambWords;
	}
	
	ArrayList<AmbWord> getAmbWords(int sentenceID) {
		ArrayList<AmbWord> cur=new ArrayList<AmbWord>();
		for (int i=0;i<ambWords.size();i++)
			if (ambWords.get(i).getSentenceID()==sentenceID)
				cur.add(ambWords.get(i));
		return cur;
	}
}