package tsptw;

import java.util.ArrayList;

public class Graph {

	public ArrayList<Vertice> vertices;
	public ArrayList<Edge> edges;
	
	public Graph(ArrayList<Vertice> vertices, ArrayList<Edge> edges) {
		this.vertices = vertices;
		this.edges = edges;
	}

	public ArrayList<Vertice> getVertices() {
		return vertices;
	}

	public void setVertices(ArrayList<Vertice> vertices) {
		this.vertices = vertices;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}
	
	
}
