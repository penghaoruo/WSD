package cs446;

import java.util.ArrayList;
import java.util.List;

import cs446.Edge;
import cs446.Vertex;

public class Graph {
	private List<Edge> e;
	private List<Vertex> v;
	public Graph(ArrayList<Edge>e, ArrayList<Vertex>v){
		this.e=e;
		this.v=v;
	}
	public List<Vertex> getVertices(){
		return v; 
	}
	public List<Edge> getEdges(){
		return e;
	}
}
