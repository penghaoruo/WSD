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
	private Map<AmbWord,List<Vertex<Integer>>> vertexMap;
	private List<AmbWord> words;
	final static int WIN_MAX=100;	// should take this as command line arg later
	private GraphCentralityScorer gcScorer;
	public WNWrapper wn;
	private Metric metric;
	public GraphHandler(List<AmbWord> words) {
		this.words=words;
		wn=new WNWrapper("data/WordNet-3.0/dict");
	}
	public Graph<Integer> CreateGraph()
	{
		
		List<Vertex<Integer>> vertices=new ArrayList<Vertex<Integer>>();
		List<Edge> edges=new ArrayList<Edge>();
		
		vertexMap=new HashMap<AmbWord,List<Vertex<Integer>>>();
		// hashmap storing mapping from ambword to each of its candidate nodes in the graph
		
		// init book-keeping
		for(AmbWord w: words)
		{
//			String pos = w.getPos();
			vertexMap.put(w,new ArrayList<Vertex<Integer>>());
			try {
				for(int i=0;i<wn.getClusterRange(w.getLemma());i++)	// for each coarse grained sense, we have  a node
				{
					Vertex<Integer> v = new Vertex<Integer>(i);
					vertices.add(v);
					vertexMap.get(w).add(v);	// add v to vertices under w
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				for(Vertex<Integer> s1: vertexMap.get(w1))
				{
					for(Vertex<Integer> s2: vertexMap.get(w2))
					{
						
						edgeCost=wn.dependency(w1,w2,s1.getVal(),s2.getVal(),metric.WuPalmer.getVal());
						//System.out.println("Adding edgecost "+edgeCost);
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
	public Map<AmbWord, List<Vertex<Integer>>> getVertexMap()
	{
		return vertexMap;
	}
	public void ScoreVertices(Graph<Integer> g)
	{
		gcScorer=GraphCentralityScorer.getInst();
		gcScorer.getDistances(g);
		for(Vertex<Integer> v: g.getVertices())
		{
			v.setScore(gcScorer.closeness(v, g));
		}
		
	}
	
}
