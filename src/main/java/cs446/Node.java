package cs446;

import java.util.ArrayList;
// node which contains objects of type T
public class Node<T> implements Comparable<Node<T>> 
{

        private static Integer idCount = 0;
        
        private ArrayList<Edge<T,Double>> outGoingEdges = new ArrayList<Edge<T,Double>>();
        private ArrayList<Edge<T,Double>> inComingEdges = new ArrayList<Edge<T,Double>>();
        
        private T Content;
        private Integer ID;
        
        private boolean visited;
        private Double distance = Double.POSITIVE_INFINITY;
        
        public Node(T content)
        {
        	this.Content=content;
        	this.ID=idCount++;
        }

        public void setVisited(boolean visited) {
                
                System.out.println("Visited"+ this.Content);
                this.visited = visited;
        }
        
        public void AddOutgoingEdge(Node<T> to,Double cost) 
        {
        		Edge<T,Double> e=new Edge<T,Double>(this,to,cost);
        		this.outGoingEdges.add(e);
                to.AddInComingEdge(e);
        }
        
        public void AddInComingEdge(Edge<T,Double> e)
        {
        	this.inComingEdges.add(e);
        }
        
        public ArrayList<Edge<T,Double>> getOutGoingEdges() 
        {
                return outGoingEdges;
        }
        
        public ArrayList<Edge<T,Double>> getInComingEdges() 
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
        
        
        public int compareTo(Node<T> arg0) {
                return this.distance.compareTo(arg0.getDistance());
        }
        public Double getDistance() {
                return distance;
        }
        public void setDistance(Double distance) {
                this.distance = distance;
        }


}