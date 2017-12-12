package tsptw;

public class Cost {

	//TODO: remove this unused class if it is not necessary
	public Vertice unvisited;
	public Vertice source;
	public Vertice destiny;
	//public int travelDistance;
	public double c1;

	public Cost(Vertice unvisited, Vertice source, Vertice destiny, double c1) {
		this.unvisited = unvisited;
		this.source = source;
		this.destiny = destiny;
		//this.travelDistance = travelDistance;
		this.c1 = c1;
	}

	public Vertice getUnvisited() {
		return unvisited;
	}

	public void setUnvisited(Vertice unvisited) {
		this.unvisited = unvisited;
	}

	public Vertice getSource() {
		return source;
	}

	public void setSource(Vertice source) {
		this.source = source;
	}

	public Vertice getDestiny() {
		return destiny;
	}

	public void setDestiny(Vertice destiny) {
		this.destiny = destiny;
	}

	public double getC1() {
		return c1;
	}

	public void setC1(double c1) {
		this.c1 = c1;
	}
	

	
}