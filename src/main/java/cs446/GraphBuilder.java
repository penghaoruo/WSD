package cs446;

import java.util.List;
import cs446.WNWrapper;

public class GraphBuilder {
	private List<AmbWord> words;
	private int seq_length;
	public GraphBuilder(List<AmbWord> words)
	{
		this.words=words;
		seq_length=words.size();		
		WNWrapper wn=new WNWrapper("data/WordNet-2.1/dict");
		for(AmbWord w: words)
		{
			for(int i=0;i< wn.getClusterRange(w.getStrID());i++)
			{
				Vertex<AmbWord> v = new Vertex<AmbWord>(w);
			}
			
		}
	}
	

}
