package cs446;

import java.util.ArrayList;

// edges b/w two nodes of type T

public class Edge<T>
{
        private Node<T> src;
        private Node<T> dst;
        private Double cost;

        public Edge(Node<T> src,Node<T> dst, Double cost) {
                this.src = src;
                this.dst = dst;
                this.cost = cost;
        }
        public Node<T> getSrc() {
                return src;
        }
        public Node<T> getDst() {
        	return dst;
        }
        public Double getCost() {
                return cost;
        }
}
