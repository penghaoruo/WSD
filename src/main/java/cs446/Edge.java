package cs446;

import java.util.ArrayList;

public class Edge {

        private Node src;
        private Node dst;
        private Double cost;

        public Edge(Node src,Node dst, Double cost) {
                this.src = src;
                this.dst = dst;
                this.cost = cost;
        }
        public Node getSrc() {
                return src;
        }
        public Node getDst() {
        	return dst;
        }
        public Double getCost() {
                return cost;
        }
}
