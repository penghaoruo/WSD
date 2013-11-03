package cs446;
import java.io.File;
import java.io.FileReader;


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
