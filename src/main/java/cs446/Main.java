package cs446;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Main {

	public static void main(String[] args) {
		Doc[] docs = dataReader.readPlainText();
		dataReader.readTestXML(docs);
//		for(AmbWord w:docs[0].getAmbWords())
//			System.out.println("Word "+w.getWord()+w.getID());
		GraphHandler gh = new GraphHandler(docs[0].getAmbWords().subList(0, 100));
		Graph<Integer> g = gh.CreateGraph();
//		FloydWarshallAllPair fw = new FloydWarshallAllPair(g);
//		Map<Integer, Map<Integer, Double>> map = fw.computeDistances();
//		for (Entry<Integer, Map<Integer, Double>> entry: map.entrySet())
//		{
//			System.out.println(entry.getKey());
//			for(Entry<Integer, Double> entry2: entry.getValue().entrySet())
//			{
//				System.out.println(entry2.getKey()+" "+entry2.getValue());
//				
//			}
//		}
		gh.ScoreVertices(g);
//		for(Vertex v: g.getVertices())
//		{
//			if(v.getScore()>0.0)
//			System.out.println("Vertex "+v.getID()+"contains "+v.getVal()+"with score "+v.getScore());
//			else
//				System.out.println("Score was negative");
//		}
		Map<AmbWord, List<Vertex<Integer>>> vMap = gh.getVertexMap();
		Vertex<Integer> maxVertex;
		double val,maxval;
		for(AmbWord word:vMap.keySet())
		{
			System.out.println("AmbWord is "+word.getWord());
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
			System.out.println("Cluster index is "+maxVertex.getVal());
		}
	}
}
