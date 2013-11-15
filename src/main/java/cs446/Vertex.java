package cs446;

import java.util.ArrayList;
// node which contains objects of type T
public class Vertex<T> implements Comparable<Vertex<T>> 
{

        private static Integer idCount = 0;
        
        private ArrayList<Edge> outGoingEdges = new ArrayList<Edge>();
        private ArrayList<Edge> inComingEdges = new ArrayList<Edge>();
        
        private T Content;
        private Integer ID;
        
        private boolean visited;
        private Double distance = Double.POSITIVE_INFINITY;
        
        public Vertex(T content)
        {
        	this.Content=content;
        	this.ID=idCount++;
        }

        public void setVisited(boolean visited) {
                
                System.out.println("Visited"+ this.Content);
                this.visited = visited;
        }
        
        public void AddOutgoingEdge(Vertex<T> to,Double cost) 
        {
        		Edge e=new Edge(this,to,cost);
        		this.outGoingEdges.add(e);
                to.AddInComingEdge(e);
        }
        
        public void AddInComingEdge(Edge e)
        {
        	this.inComingEdges.add(e);
        }
        
        public ArrayList<Edge> getOutGoingEdges() 
        {
                return outGoingEdges;
        }
        
        public ArrayList<Edge> getInComingEdges() 
        {
        	return inComingEdges;
        }

        public T getVal() 
        {
                return Content;
        }
        public void setVal(T val) 
        {
                this.Content = val;
        }
        public Integer getID() 
        {
                return ID;
        }
        
        
        public int compareTo(Vertex<T> arg0) {
                return this.distance.compareTo(arg0.getDistance());
        }
        public Double getDistance() {
                return distance;
        }
        public void setDistance(Double distance) {
                this.distance = distance;
        }


}