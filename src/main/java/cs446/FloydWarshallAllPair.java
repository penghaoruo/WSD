package cs446;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cs446.Edge;
import cs446.Vertex;

//calculates the all pair shortest paths in a undirected graph
public class FloydWarshallAllPair 
{
	private Map<Integer, Map<Integer,Double>> dist;
	private List<Vertex> vs;
	private List<Edge> es;
	public FloydWarshallAllPair(Graph G)
	{
		this.vs=G.getVertices();
		this.es=G.getEdges();
	}
	public Map<Integer, Map<Integer,Double>> computeDistances()
	{
		dist=new HashMap<Integer, Map<Integer,Double>>();
		for(Vertex v: vs)
		{
			dist.put(v.getID(), new HashMap<Integer,Double>());
			for(Vertex v1: vs)
			{
				dist.get(v.getID()).put(v1.getID(),0.0);
			}
		}	
		int u,w;
		for(Edge edge: es)
		{	
			u=edge.getSrc().getID();	// get node ids
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
	return dist;
	}
	public static void main(String args[]){
		List<Vertex<String>>v=new ArrayList<Vertex<String>>();
		List<Edge>e=new ArrayList<Edge>();
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		Vertex<String> c = new Vertex<String>("c");
		Vertex<String> d = new Vertex<String>("d");
		v.addAll(Arrays.asList(a,b,c,d));
		e.add(new Edge(a,c,-2.0));
		e.add(new Edge(b,a,4.0));
		e.add(new Edge(b,c,3.0));
		e.add(new Edge(c,d,2.0));
		e.add(new Edge(d,b,-1.0));
		Graph g = new Graph(e,v);
		FloydWarshallAllPair fw = new FloydWarshallAllPair(g);
		Map<Integer, Map<Integer, Double>> map = fw.computeDistances();
		for (Entry<Integer, Map<Integer, Double>> entry: map.entrySet())
		{
			System.out.println(entry.getKey());
			for(Entry<Integer, Double> entry2: entry.getValue().entrySet())
			{
				System.out.println(entry2.getKey()+" "+entry2.getValue());
				
			}
		}
		
	}
}
