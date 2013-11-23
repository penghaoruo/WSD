package cs446;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs446.WNWrapper;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.POS;

public class GraphHandler {
	private List<AmbWord> words;
	final static int WIN_MAX=5;	// should take this as command line arg later
	private GraphCentralityScorer gcScorer;
	private WNWrapper wn;
	public GraphHandler(List<AmbWord> words, int seq_length) {
		this.words=words;
		wn=new WNWrapper("data/WordNet-3.0/dict");
	}
	public Graph<ISynset> CreateGraph(List<AmbWord> words)
	{
		
		
		List<Vertex<ISynset>> vertices=new ArrayList<Vertex<ISynset>>();
		List<Edge> edges=new ArrayList<Edge>();
		
		Map<AmbWord,List<Vertex<ISynset>>> map=new HashMap<AmbWord,List<Vertex<ISynset>>>();
		// hashmap storing mapping from ambword to each of its candidate nodes in the graph
		
		// init book-keeping
		for(AmbWord w: words)
		{
			String pos = w.getPos();
			map.put(w,new ArrayList<Vertex<ISynset>>());
			for(int i=0;i<wn.getClusterRange(w.getWord());i++)
			{
				List<ISynset>senses=wn.getAllSynsetsFromCluster(w.getWord(),pos,i);
				for(ISynset sense: senses)
				{
					Vertex<ISynset> v = new Vertex<ISynset>(sense);
					vertices.add(v);
					map.get(w).add(v);	// add v to vertices under w
				}
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
				for(Vertex s1: map.get(w1))
				{
					for(Vertex s2: map.get(w2))
					{
//						edgeCost=dependency(w1,w2,s1.getVal(),s2.getVal());
						if(edgeCost>0)
						{
							edges.add(new Edge(s1,s2,edgeCost));
						}
					}
				}
			}
		}
		
	return new Graph<ISynset>(edges, vertices);
	}
	public void ScoreVertices(Graph<ISynset> g)
	{
		gcScorer=GraphCentralityScorer.getInst();
//		gcScorer.setMetric()
		for(Vertex<ISynset> v: g.getVertices())
		{
			v.setScore(gcScorer.score(v,g));
		}
		
	}
	
}
