package tsptw;

public class Vertice {

	public int verticeId;
	public int xCoord;
	public int yCoord;
	public int readyTime;
	public int dueDate;
	
	public Vertice(int verticeId, int xCoord, int yCoord, int readyTime, int dueDate) {
		this.verticeId = verticeId;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.readyTime = readyTime;
		this.dueDate = dueDate;
	}

	public int getVerticeId() {
		return verticeId;
	}

	public void setVerticeId(int verticeId) {
		this.verticeId = verticeId;
	}

	public int getxCoord() {
		return xCoord;
	}

	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	public int getyCoord() {
		return yCoord;
	}

	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}

	public int getReadyTime() {
		return readyTime;
	}

	public void setReadyTime(int readyTime) {
		this.readyTime = readyTime;
	}

	public int getDueDate() {
		return dueDate;
	}

	public void setDueDate(int dueDate) {
		this.dueDate = dueDate;
	}
	
	
	
}
