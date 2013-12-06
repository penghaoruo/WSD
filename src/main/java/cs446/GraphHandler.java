package cs446;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs446.WNWrapper;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.POS;


public class GraphHandler {
	public enum Metric
	{
		LeacockChodorow(1),
		Lesk(2),
		WuPalmer(3),
		Resnick(4);
		
		private int value;
		private Metric(int val){
			this.value=val;
		}
		public int getVal(){
			return value;
		}
	};
	private List<AmbWord> words;
	final static int WIN_MAX=5;	// should take this as command line arg later
	private GraphCentralityScorer gcScorer;
	private WNWrapper wn;
	private Metric metric;
	public GraphHandler(List<AmbWord> words, int seq_length) {
		this.words=words;
		wn=new WNWrapper("data/WordNet-3.0/dict");
	}
	public Graph<Integer> CreateGraph(List<AmbWord> words)
	{
		
		List<Vertex<Integer>> vertices=new ArrayList<Vertex<Integer>>();
		List<Edge> edges=new ArrayList<Edge>();
		
		Map<AmbWord,List<Vertex<Integer>>> map=new HashMap<AmbWord,List<Vertex<Integer>>>();
		// hashmap storing mapping from ambword to each of its candidate nodes in the graph
		
		// init book-keeping
		for(AmbWord w: words)
		{
//			String pos = w.getPos();
			map.put(w,new ArrayList<Vertex<Integer>>());
			for(int i=0;i<wn.getClusterRange(w.getWord());i++)
			{
				Vertex<Integer> v = new Vertex<Integer>(i);
				vertices.add(v);
				map.get(w).add(v);	// add v to vertices under w
			}
		}
		double edgeCost=0.0;
		for(AmbWord w1: words)
		{
			for(AmbWord w2: words)
			{
				if(w1==w2)
					continue;
				if(w1.getID() - w2.getID() > WIN_MAX)
					break;
				for(Vertex<Integer> s1: map.get(w1))
				{
					for(Vertex<Integer> s2: map.get(w2))
					{
						
						edgeCost=wn.dependency(w1,w2,s1.getVal(),s2.getVal(),metric.WuPalmer.getVal());
						if(edgeCost>0)
						{
							edges.add(new Edge(s1,s2,edgeCost));
						}
					}
				}
			}
		}
		
	return new Graph<Integer>(edges, vertices);
	}
	public void ScoreVertices(Graph<ISynset> g)
	{
		gcScorer=GraphCentralityScorer.getInst();
		for(Vertex<ISynset> v: g.getVertices())
		{
			v.setScore(gcScorer.score(v,g));
		}
		
	}
	public static void main(String[] args) {
		Doc[] docs = dataReader.readPlainText();
		System.out.println(docs.length+" "+docs[0].getAmbWords());
		for(AmbWord a:docs[0].getAmbWords())
			System.out.println(a.getLemma());
	}
}
