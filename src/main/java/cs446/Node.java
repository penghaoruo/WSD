package cs446;

import java.util.ArrayList;

public class Node implements Comparable<Node> 
{

        private static Integer id = 0;
        private ArrayList<Edge> outGoingEdges = new ArrayList<Edge>();
        private ArrayList<Edge> inComingEdges = new ArrayList<Edge>();
        private String val;
        private Integer ID;
        private boolean visited;
        private Double distance = Double.POSITIVE_INFINITY;
        
        public Node(String value) {
                this.init(value);
        }
        public Node(){
                this.init("");
        }
        private void init(String nodeVal){
                this.val = nodeVal;
                this.ID = Node.id++;
                this.visited = false;
        }
        public void setVisited(boolean visited) {
                
                System.out.println("Visited"+ this.val);
                this.visited = visited;
        }
        public void AddOutgoingEdge(Node to,Double cost) 
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

        public String getVal() 
        {
                return val;
        }
        public void setVal(String val) 
        {
                this.val = val;
        }
        public Integer getID() 
        {
                return ID;
        }
        
        
        public int compareTo(Node arg0) {
                return this.distance.compareTo(arg0.getDistance());
        }
        public Double getDistance() {
                return distance;
        }
        public void setDistance(Double distance) {
                this.distance = distance;
        }


}