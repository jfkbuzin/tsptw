package tsptw;

public class Vertice {

	public int verticeId;
	public int xCoord;
	public int yCoord;
	public int readyTime;
	public int dueDate;
	public int timeWindow;
	public boolean isOrigin;
	
	public Vertice(int verticeId, int xCoord, int yCoord, int readyTime, int dueDate,boolean isOrigin) {
		this.verticeId = verticeId;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.readyTime = readyTime;
		this.dueDate = dueDate;
		this.isOrigin = isOrigin;
		this.timeWindow  = dueDate - readyTime;
	}

	public boolean isOrigin() {
		return isOrigin;
	}

	public int getTimeWindow() {
		return timeWindow;
	}

	public void setTimeWindow(int timeWindow) {
		this.timeWindow = timeWindow;
	}

	public void setOrigin(boolean isOrigin) {
		this.isOrigin = isOrigin;
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