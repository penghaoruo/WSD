package cs446;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs446.Edge;
import cs446.Vertex;

public class FloydWarshallAllPair {
	private Map<Integer, Map<Integer,Double>> dist;
	private List<Vertex> vs;
	private List<Edge> es;
	public FloydWarshallAllPair(Graph G)
	{
		this.vs=G.getVertices();
		this.es=G.getEdges();
	}
	public void ComputeDistances()
	{
		dist=new HashMap<Integer, Map<Integer,Double>>();
		for(Vertex v: vs)
		{
			dist.put(v.getID(), new HashMap<Integer,Double>());
			dist.get(v.getID()).put(v.getID(),0.0);
		}	
		int u,w;
		for(Edge edge: es)
		{	
			u=edge.getSrc().getID();
			w=edge.getDst().getID();
			dist.get(u).put(w,edge.getCost());
		}
		int I,J,K;
		for(Vertex k: vs)
		{
			K=k.getID();
			for(Vertex i: vs)
			{
				I=i.getID();
				for(Vertex j: vs)
				{
					J=j.getID();
					if(dist.get(I).get(K)+dist.get(K).get(J) < dist.get(I).get(J))
						dist.get(I).put(K,dist.get(I).get(K)+dist.get(K).get(J));
				}
	
			}
		}
	}
}
