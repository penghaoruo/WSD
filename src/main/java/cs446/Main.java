package cs446;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Main implements Runnable{
	static GraphHandler gh;
	private Doc doc;
	private Metric m;
	public Main(Doc doc, Metric m){
		this.doc=doc;
		this.m=m;
	}
	public static ArrayList<AmbWord> assignSenses() {
		ArrayList<AmbWord> list=new ArrayList<AmbWord>();
		Map<AmbWord, List<Vertex<Integer>>> vMap = gh.getVertexMap();
		Vertex<Integer> maxVertex;
		double val,maxval;
		for(AmbWord word:vMap.keySet())
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
			list.add(word);
		}
		return list;
	}
	
	public static void main(String[] args) {
		Para.init(args);
		
		Doc[] docs = dataReader.readPlainText();
		dataReader.readTestXML(docs);
//		System.out.println(docs[4].getAmbWords().size());
		Metric m=Metric.Lesk;
		for(int i=0;i<docs.length;i++)
		{
			Thread thread= new Thread(new Main(docs[i],m));
			thread.start();
		}
//		gh = new GraphHandler(docs[0].getAmbWords(),m);
////		gh = new GraphHandler(docs[0].getAmbWords().subList(0, 100));
//		Graph<Integer> g = gh.CreateGraph();
//		gh.ScoreVertices(g);
//		ArrayList<AmbWord> list=assignSenses();
//		output(list);
//		System.out.println("Using metric "+m.name());
//		evaluation(list);

		Metric m=Metric.WuPalmer;
		for(int i=0;i<docs.length;i++)
		gh = new GraphHandler(docs[i].getAmbWords(),m);
//		gh = new GraphHandler(docs[0].getAmbWords().subList(0, 100));
		Graph<Integer> g = gh.CreateGraph();
		gh.ScoreVertices(g);
		ArrayList<AmbWord> list=assignSenses();
		output(list);
		System.out.println("Using metric "+m.name());
		evaluation(list);
		
		//baseTest(list);
	}

	public static void baseTest(ArrayList<AmbWord> list) {
		
		
>>>>>>> Stashed changes
	}

	public static void evaluation(ArrayList<AmbWord> list) {
		ArrayList<String> lines=IOManager.readLines("data/SemEval-2007/key/dataset21.test.key");
		double correct=0;
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
	    	for (int j=0;j<tags.length;j++)
		    	if (aw.getAssignedSense()==tags[j]) {
		    		correct+=1;
		    		break;
		    	}
	    }
		System.out.println("Precision:"+correct/list.size());
	}

	public static void output(ArrayList<AmbWord> list) {
		BufferedWriter bw=IOManager.openWriter("output.txt");
		for (int i=0;i<list.size();i++) {
			AmbWord aw=list.get(i);
			String str="d00"+aw.getTextID()+" "+aw.getStrID()+" ";
			//System.out.println(aw.getLemma()+" "+aw.getPos());
			str=str+gh.wn.getSenseString(aw.getLemma(),aw.getAssignedSense(),aw.getPos());
			str=str+"!! lemma="+aw.getLemma()+"#"+aw.getPos()+"\n";
			IOManager.writeString(str, bw);
		}
		IOManager.closeWriter(bw);
	}

	@Override
	public void run() {
		gh = new GraphHandler(doc.getAmbWords(),m);
//		gh = new GraphHandler(docs[0].getAmbWords().subList(0, 100));
		Graph<Integer> g = gh.CreateGraph();
		gh.ScoreVertices(g);
		ArrayList<AmbWord> list=assignSenses();
		output(list);
		System.out.println("Using metric "+m.name());
		evaluation(list);		
	}
}
