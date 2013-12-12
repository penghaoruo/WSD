package cs446;

import java.util.List;
import java.util.Map;

import edu.mit.jwi.item.ISynset;

public class GraphCentralityScorer {
	private static GraphCentralityScorer instance=new GraphCentralityScorer();
	private FloydWarshallAllPair fw;
	Map<Integer, Map<Integer, Double>> dist;	// nodeId -> nodeId -> Distance
	// 
	private GraphCentralityScorer()
	{
		// do nothing
	}
	
	public static GraphCentralityScorer getInst() 
	{
		return instance;
	}
	
	public void resetDistances()
	{
		dist=null;
	}
	public void getDistances(Graph<Integer> G)
	{
		fw=new FloydWarshallAllPair(G);
		dist=fw.computeDistances();
		
	}
	// uses metrics defined in the paper to find similarity
	public Double score(Vertex<ISynset> v, Graph<ISynset> g) 
	{
		
		return null;
	}
	public Double closeness(Vertex<Integer> v,Graph<Integer> g)
	{
		assert dist!=null : "Calculate distances first!";
		Map<Integer, Double> distv = dist.get(v.getID());
		Double total=0.0;
		for(Double d: distv.values()){
			if(d.equals(Double.POSITIVE_INFINITY))
			{
//				System.out.println("Ignoring infinite distances");
			}
			else if(d.equals(0.0))
			{
//				System.out.println("Ignoring 0 distances");
			}
			else
				total+=d;
		}
		if(total==0.0)
		{
//			for(Double d: distv.values()){
//				System.out.print(d+" ");
//			}
			//System.out.println("This is not right!");
			return -1.0;
		}
//		System.out.println(total);
		//System.out.println("Closeness "+1.0/total);
		return 1.0/total;
		
	}
	public Integer indegree(Vertex<Integer> v,Graph<Integer> g)
	{
		return v.getInComingEdges().size();
	}
	// remember that the weight of the edges themselves need to be normalized b/w their ranges
	
}
