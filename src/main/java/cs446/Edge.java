package cs446;

import java.util.ArrayList;

// edges b/w two vertices of type T

public class Edge
{
        @SuppressWarnings("rawtypes")
		private Vertex src;
        private Vertex dst;
        private Double cost;

        public Edge(Vertex src,Vertex dst, Double cost) {
                this.src = src;
                this.dst = dst;
                this.cost = cost;
        }
        public Vertex getSrc() {
                return src;
        }
        public Vertex getDst() {
        	return dst;
        }
        public Double getCost() {
                return cost;
        }
}
