package cs446;

import java.util.List;

import cs446.Edge;
import cs446.Vertex;

public class Graph<T> {
	private List<Edge> e;
	private List<Vertex<T>> v;
	public Graph(List<Edge>e, List<Vertex<T>>v){
		this.e=e;
		this.v=v;
	}
	public List<Vertex<T>> getVertices(){
		return v; 
	}
	public List<Edge> getEdges(){
		return e;
	}
	
}
