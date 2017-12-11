package tsptw;

public class Edge {

	//TODO: remove this unused class if it is not necessary
	public int edgeId;
	public Vertice source;
	public Vertice destiny;
	//public int travelDistance;
	public double travelTime;
	public boolean violation;
	
	public boolean isViolation() {
		return violation;
	}

	public Edge(int edgeId, Vertice source, Vertice destiny, double travelTime) {
		this.edgeId = edgeId;
		this.source = source;
		this.destiny = destiny;
		//this.travelDistance = travelDistance;
		this.travelTime = travelTime;
	}
	
	public void setViolation(boolean violation) {
		this.violation = violation;
	}

	public int getEdgeId() {
		return edgeId;
	}

	public void setEdgeId(int edgeId) {
		this.edgeId = edgeId;
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

	/*
	public int getTravelDistance() {
		return travelDistance;
	}

	public void setTravelDistance(int travelDistance) {
		this.travelDistance = travelDistance;
	}*/

	public double getTravelTime() {
		return travelTime;
	}
	
	public void setTravelTime(double travelTime) {
		this.travelTime = travelTime;
	}
	
}