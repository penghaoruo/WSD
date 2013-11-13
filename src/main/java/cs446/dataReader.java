package cs446;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


import org.apache.thrift.TException;

import edu.illinois.cs.cogcomp.core.utilities.commands.CommandDescription;
import edu.illinois.cs.cogcomp.core.utilities.commands.InteractiveShell;
import edu.illinois.cs.cogcomp.edison.data.curator.CuratorClient;
import edu.illinois.cs.cogcomp.edison.sentences.Constituent;
import edu.illinois.cs.cogcomp.edison.sentences.SpanLabelView;
import edu.illinois.cs.cogcomp.edison.sentences.TextAnnotation;
import edu.illinois.cs.cogcomp.edison.sentences.ViewNames;
import edu.illinois.cs.cogcomp.thrift.base.AnnotationFailedException;
import edu.illinois.cs.cogcomp.thrift.base.ServiceUnavailableException;

public class dataReader {
	
	private static final int curatorPort = 9010;
	private static final String curatorHost = "trollope.cs.illinois.edu";
	private static final CuratorClient client = new CuratorClient(curatorHost, curatorPort);
	private static final boolean forceUpdate = true;
	private static final String corpus = "Senseval";
	private static int textId = 0;
	
	public static String getinfo(String info, String target) {
		int p=info.indexOf(target);
		int p1=info.indexOf("\"",p);
		int p2=info.indexOf("\"",p1+1);
		String res=info.substring(p1+1,p2);
		return res;
	}
	
	public static Doc[] readPlainText() {
		String[] files={"wsj_0105.mrg","wsj_0186.mrg","wsj_0239.mrg","Computer_programming.txt","Masaccio_Knights_of_the_Art_by_Amy_Steedman.txt"};
		String path="data/SemEval-2007/test/";
		int n=files.length;
		
		Doc[] docs=new Doc[n];
		for (int i=0;i<n;i++) {
			docs[i]=new Doc();
			docs[i].setContent(IOManager.readContent(path+files[i]));
			docs[i].setID("d00"+(i+1));
		}
		return docs;
	}
	
	public static void readTestXML(Doc[] docs) {
		String file="eng-coarse-all-words.xml";
		String path="data/SemEval-2007/test/";
		ArrayList<String> lines=IOManager.readLines(path+file);
		
		int textID=0;
		int sentenceID=0;
		int tagID=0;
		for (int i=0;i<lines.size();i++) {
			String line=lines.get(i);
			if (line.startsWith("<text ")) {
				textID++;
				continue;
			}
			if (line.startsWith("<sentence ")) {
				sentenceID++;
				docs[textID-1].addSentence();
				continue;
			}
			if (line.startsWith("<instance ")) {
				tagID++;
				int p1=line.indexOf('>');
				int p2=line.indexOf('<', p1);
				String word=line.substring(p1+1,p2);
				String info=line.substring(0,p1+1);
				
				String strID=getinfo(info,"id");
				String pos=getinfo(info,"lemma");
				String lemma=getinfo(info,"pos");
				
				AmbWord aw=new AmbWord();
				aw.setSentenceID(sentenceID);
				aw.setStrID(strID);
				aw.setTagID(tagID);
				aw.setTextID(textID);
				aw.setWord(word);
				aw.setPos(pos);
				aw.setLemma(lemma);
				docs[textID-1].addAmbWord(aw);
				docs[textID-1].setSentences(word,sentenceID-1);
				continue;
			}
			if (line.startsWith("</text>")) {
				sentenceID=0;
				continue;
			}
			if (line.startsWith("</sentence>")) {
				tagID=0;
				continue;
			}
			if (!line.startsWith("<")) {
				docs[textID-1].setSentences(line,sentenceID-1);
				continue;
			}
		}
	}

	public static void main(String[] args) throws Exception
	{
//		if (args.length!=1)
//		{
//			System.err.println("Need an argument.");
//			System.exit(-1);
//		}
		InteractiveShell<dataReader> tester = new InteractiveShell<dataReader>(dataReader.class);
		if(args.length==0)
		{
			tester.showDocumentation();
		}
		else
		{
			tester.runCommand(args);
		}
//		Instances train = new Instances(new FileReader(new File(args[0])));
		
		Doc[] docs=readPlainText();
		readTestXML(docs);
		
	}
	
	@CommandDescription(description = "printPOS")
	public static void printPOS() throws ServiceUnavailableException, AnnotationFailedException, TException
	{
		textId++;
		String myTextId;
		String text = "The quick brown fox jumped over the lazy dog.";
		System.out.println("Text is "+text);
		myTextId = String.format("%05d", textId);
		
		TextAnnotation ta = client.getTextAnnotation(corpus, myTextId, text, forceUpdate);
		System.out.println("Tokens are "+ta.getTokenizedText());
		client.addPOSView(ta, forceUpdate);
		System.out.println("POS tags: "+ta.getView(ViewNames.POS));
	}


}
