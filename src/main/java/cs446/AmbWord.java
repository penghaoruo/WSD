package cs446;

import java.util.ArrayList;
import java.util.List;

public class AmbWord {
	private static int idCount=0;
	private int id;
	private String word;
	private String pos;
	private String lemma;
	private String strID;	
	private int sentenceID;
	private int textID;
	private int tagID;
	private ArrayList<String> possibleSenses; // list of possible senses	
	
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
}