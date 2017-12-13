package tsptw;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;

public class Main {
	
	public static boolean debug = false;
	public static long startTime;
	public static double[][] distMatrix;

	public static void main(String[] args) throws FileNotFoundException, ParseException {
		
		startTime = System.currentTimeMillis();
			
		if(debug)System.out.println(System.getProperty("user.dir"));
		
		File file = null;
		float temperature = 0;
		float r = 0;
		int stop1 = 0;
		int stop2 = 0;
		long seed = 0;
		
		if (0 < args.length) {
			file = new File(args[0]);
			temperature = Float.parseFloat(args[1]);
			r = Float.parseFloat(args[2]); 
			stop1 = Integer.parseInt(args[3]);
			stop2 = Integer.parseInt(args[4]);
			seed = Integer.parseInt(args[5]);
			// file path, t,r,stop1,stop2 e seed digitadas pelo user
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
		
		distMatrix = new double[vertices.size()][vertices.size()];
		
		for(Vertice v1 : vertices) {
			if(debug)System.out.println("Line:" + v1.getVerticeId());
			for(Vertice v2 : vertices) {
				if(v1 == v2) {
					distMatrix[v1.getVerticeId()][v2.getVerticeId()] = 0;
				}
				else
					distMatrix[v1.getVerticeId()][v2.getVerticeId()] = getTravelTime(v1.getxCoord(), v2.getxCoord(), v1.getyCoord(), v2.getyCoord());
				if(debug)System.out.print(" " + distMatrix[v1.getVerticeId()][v2.getVerticeId()]);
			}
			if(debug)System.out.println("");
		}

		
		if(debug)System.out.println("Corrected matrix:");
		
		//vertices = SimulatedAnnealing.simulatedAnnealing(vertices,temperature,r,stop1,stop2, seed);
		
		triangleInequality(vertices, distMatrix);
		
		for(Vertice v1 : vertices) {
			if(debug)System.out.println("Line:" + v1.getVerticeId());
			for(Vertice v2 : vertices) {
				if(debug)System.out.print(" | " + distMatrix[v1.getVerticeId()][v2.getVerticeId()]);
			}
			if(debug)System.out.println("");
		}

		vertices = SimulatedAnnealing.simulatedAnnealing(vertices,temperature,r,stop1,stop2, seed);
		
		System.out.println("T:" + temperature);
		System.out.println("r:" + r);
		System.out.println("stop1:" + stop1);
		System.out.println("stop2:" + stop2);
		System.out.println("seed:" + seed);
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("tempo de execução:" + totalTime + " milisegundos");
		System.exit(0);
	}
	
	public static double getTravelTime(int xa, int xb, int ya, int yb) {
		int xt = xa - xb;
		int yt = ya - yb;
		return Math.floor(Math.sqrt(xt*xt + yt*yt));
	}
	
	public static void triangleInequality(ArrayList<Vertice> vertices, double[][] distMatrix) {
	
		for(Vertice i : vertices) {
			for(Vertice j : vertices) {
				for(Vertice k : vertices) {
					if(distMatrix[i.getVerticeId()][j.getVerticeId()] > distMatrix[i.getVerticeId()][k.getVerticeId()] + distMatrix[k.getVerticeId()][j.getVerticeId()]) 
						distMatrix[i.getVerticeId()][j.getVerticeId()] = distMatrix[i.getVerticeId()][k.getVerticeId()] + distMatrix[k.getVerticeId()][j.getVerticeId()];
				}
			}
		}
			
	
	}

}