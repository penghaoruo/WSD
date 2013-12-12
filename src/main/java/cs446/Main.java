package cs446;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Main implements Runnable{
	static GraphHandler gh;
	private static Doc doc;
	private Metric m;
	private int ws;
	private String scorer;
	private static ArrayList<double[]> results;
	private static String spec;
	
	public Main(Doc doc, Metric m, int ws, String scorer, String spec){
		this.doc=doc;
		this.m=m;
		this.ws=ws;
		this.scorer=scorer;
		this.spec=spec;
		results=new ArrayList<double[]>();	
	}
	
	public static ArrayList<AmbWord> assignSenses() {
		ArrayList<AmbWord> list=gh.words;
		Map<AmbWord, List<Vertex<Integer>>> vMap = gh.getVertexMap();
		Vertex<Integer> maxVertex;
		double val,maxval;
		for(AmbWord word: list)
		{
			//System.out.println("AmbWord is "+word.getWord());
			List<Vertex<Integer>> vs = vMap.get(word);
			maxVertex=null;
			maxval=Double.NEGATIVE_INFINITY;
			for(Vertex<Integer> v:vs)
			{	
				val=v.getScore();
				if(val>maxval)
				{
					maxval=val;
					maxVertex=v;
				}
			}
		//System.out.println("Cluster index is "+maxVertex.getVal());
			if (maxVertex==null) {
				System.out.println("Error:"+word.getWord());
				word.setAssignedSense(0);
			}
			else word.setAssignedSense(maxVertex.getVal());
			//list.add(word);
		}
		return list;
	}
	
	public static void main(String[] args) throws Exception {
		
		
		//baseTest(list);
		
		Configuration config=new PropertiesConfiguration("wsd.properities");
		Doc[] docs = dataReader.readPlainText(config.getString("train_path"));
		dataReader.readTestXML(docs,config.getString("test_path"));
		Metric m=getSimilarityMetric(config);
		String scorer=config.getString("cnetrality_metric");
		int ws=config.getInt("window_size");
		String spec=config.getString("similarity_metric")+"+"+scorer+"+"+ws;
		
		for(int i=0;i<docs.length;i++) {
			Thread thread= new Thread(new Main(docs[i],m,ws,scorer,spec));
			thread.start();
		}
		
		BufferedWriter bw=IOManager.openWriter(spec+"-res.txt");
		for (int i=0;i<5;i++) {
			double tmp=0;
			for (int j=0;j<results.size();j++)
				tmp+=results.get(j)[i];
			tmp=tmp/results.size();
			String line="";
			if (i==0) line="Overall precision:"+tmp;
			if (i==1) line="Noun precision:"+tmp;
			if (i==2) line="Adjective precision:"+tmp;
			if (i==3) line="Verb precision:"+tmp;
			if (i==4) line="Adverb precision:"+tmp;
			line=line+"\n";
			IOManager.writeString(line, bw);
		}
		IOManager.closeWriter(bw);
	}

	private static Metric getSimilarityMetric(Configuration config) {
		String str=config.getString("similarity_metric");
		Metric m=Metric.Lesk;
		if (str.equals("LeacockChodorow")) m=Metric.LeacockChodorow;
		if (str.equals("Lesk")) m=Metric.Lesk;
		if (str.equals("WuPalmer")) m=Metric.WuPalmer;
		if (str.equals("Resnick")) m=Metric.Resnick;
		if (str.equals("Lin")) m=Metric.Lin;
		if (str.equals("Jiangconrath")) m=Metric.Jiangconrath;
		return m;
	}

	public static void baseTest(ArrayList<AmbWord> list) {
		
		
	}

	public static double[] evaluation(ArrayList<AmbWord> list) {
		double[] res=new double[10];
		ArrayList<String> lines=IOManager.readLines("data/SemEval-2007/key/dataset21.test.key");
		//double correct=0;
		for (int i=0;i<lines.size();i++) {
			String line=lines.get(i);
			String strs[]=line.split(" ");
			String strID=strs[1];
			ArrayList<String> tags=new ArrayList<String>();
			for (int j=2;j<strs.length;j++)
				if (!strs[j].equals("!!"))
					tags.add(strs[j]);
				else
					break;
		    for (int j=0;j<list.size();j++)
		    	if (list.get(j).getStrID().equals(strID)) {
		    		list.get(j).setGoldSense(gh.wn.getClusterIDs(tags,list.get(j).getLemma(),list.get(j).getPos()));
		    		break;
		    	}
		}
		for (int i=0;i<list.size();i++) {
	    	AmbWord aw=list.get(i);
	    	int tags[]=aw.getGoldSense();
	    	res[10]+=1;
	    	if (aw.getPos().equals("n")) res[6]+=1;
    		if (aw.getPos().equals("a")) res[7]+=1;
    		if (aw.getPos().equals("v")) res[8]+=1;
    		if (aw.getPos().equals("r")) res[9]+=1;	
	    	for (int j=0;j<tags.length;j++)
		    	if (aw.getAssignedSense()==tags[j]) {
		    		res[0]+=1;
		    		if (aw.getPos().equals("n")) res[1]+=1;
		    		if (aw.getPos().equals("a")) res[2]+=1;
		    		if (aw.getPos().equals("v")) res[3]+=1;
		    		if (aw.getPos().equals("r")) res[4]+=1;
		    		break;
		    	}
	    }
		for (int i=0;i<5;i++)
			res[i]=res[i]/res[i+5];
		return res;
	}

	public static void output(ArrayList<AmbWord> list) {
		BufferedWriter bw=IOManager.openWriter(spec+"+"+doc.getID()+"-output.txt");
		for (int i=0;i<list.size();i++) {
			AmbWord aw=list.get(i);
			String str="d00"+aw.getTextID()+" "+aw.getStrID()+" ";
			str=str+gh.wn.getSenseString(aw.getLemma(),aw.getAssignedSense(),aw.getPos());
			str=str+"!! lemma="+aw.getLemma()+"#"+aw.getPos()+"\n";
			IOManager.writeString(str, bw);
		}
		IOManager.closeWriter(bw);
	}

	public void run() {
		gh = new GraphHandler(doc.getAmbWords(),m,ws);
		Graph<Integer> g = gh.CreateGraph();
		gh.ScoreVertices(g,scorer);
		ArrayList<AmbWord> list=assignSenses();
		output(list);
		System.out.println("Using metric "+m.name());
		results.add(evaluation(list));
	}
}
