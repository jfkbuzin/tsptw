package tsptw;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		long startTime = System.currentTimeMillis();
			
		System.out.println(System.getProperty("user.dir"));
		
		File file = null;
		float temperature = 0;
		float r = 0;
		int stop1 = 0;
		int stop2 = 0;
		
		if (0 < args.length) {
			file = new File(args[0]);
			temperature = Float.parseFloat(args[1]);
			r = Float.parseFloat(args[2]); 
			stop1 = Integer.parseInt(args[3]);
			stop2 = Integer.parseInt(args[4]);
			// file path, t,r,stop1 e stop2 digitadas pelo user
		} else {
			System.err.println("Invalid arguments count:" + args.length);
			System.exit(0);
		}
		
		if(file == null){
			System.err.println("File not found");
			System.exit(0);
		}
		
		if(temperature == 0 || r ==0 || stop1 == 0 || stop2 == 0){
			System.err.println("Invalid arguments");
			System.exit(0);
		}
	
		ArrayList<Vertice> vertices = ReadFile.readFile(file);
		//ArrayList<Edge> edges =  new ArrayList<Edge>();
		
		
		//System.out.println(new Random().nextFloat());
		
		//Graph graph = new Graph(vertices,edges);
		
		System.out.println("T:" + temperature);
		System.out.println("r:" + r);
		System.out.println("stop1:" + stop1);
		System.out.println("stop2:" + stop2);
		
		Vertice v1 = vertices.get(0);
		Vertice v2 = vertices.get(1);
		
		double distanceTest = getTravelTime(v1.getxCoord(), v2.getxCoord(), v1.getyCoord(), v2.getyCoord());
		System.out.println("distancia entre 2 primeiros vertices:" + distanceTest);
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("tempo de execução:" + totalTime + " milisegundos");
	}
	
	/*
	public void processEdgeTravelTime(ArrayList<Vertice> vertices, ArrayList<Edge> edges) {
		
		for(Edge e : edges) {
			
			for(Vertice i : vertices) {

				for(Vertice j : vertices) {
					
					for(Vertice k : vertices) {
					
					}
				}
			}
			
		}	
	}*/
	public static double getTravelTime(int xa, int xb, int ya, int yb) {
		int xt = xa - xb;
		int yt = ya - yb;
		return Math.floor(Math.sqrt(xt*xt + yt*yt));
	}

}
