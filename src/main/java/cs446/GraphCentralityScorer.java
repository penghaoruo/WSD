package cs446;

import edu.mit.jwi.item.ISynset;

public class GraphCentralityScorer {
	private static GraphCentralityScorer instance=new GraphCentralityScorer();
	
	private GraphCentralityScorer()
	{
		
	}
	public static GraphCentralityScorer getInst() {
		return instance;
	}
	
	// uses metrics defined in the paper to find similarity
	public Double score(Vertex<ISynset> v, Graph<ISynset> g) {
		// TODO Auto-generated method stub
		return null;
	}

}
