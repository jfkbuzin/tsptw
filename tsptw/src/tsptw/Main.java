package tsptw;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		ArrayList<Vertice> vertices =  new ArrayList<Vertice>();
		ArrayList<Edge> edges =  new ArrayList<Edge>();
		
		/*
		Scanner s = new Scanner(new File("n10w140.txt"));
		ArrayList<String> ids = new ArrayList<String>();

		// Skip column headings.

		// Read each line, ensuring correct format.
		while (s.hasNext())
		{
			ids.add(s.next());
			s.nextInt();         // read and skip 'id'
			s.nextInt();  // read and store 'name'
		    s.nextInt();
		    s.nextInt(); 
		    s.nextInt(); 
		    s.nextInt(); // read and skip 'age'
		}

		for (String id: ids)
		{
		    System.out.println(id);
		}*/
		
		System.out.println(new Random().nextFloat());
		
		Graph graph = new Graph(vertices,edges);
		
		
	}
	public void processEdgeTravelTime(ArrayList<Vertice> vertices, ArrayList<Edge> edges) {
		
		for(Edge e : edges) {
			
			for(Vertice i : vertices) {

				for(Vertice j : vertices) {
					
					for(Vertice k : vertices) {
					
					}
				}
			}
			
		}	
	}
	public double getTravelTime(int xa, int xb, int ya, int yb) {
		int xt = xa - xb;
		int yt = ya - yb;
		return Math.floor(Math.sqrt(xt*xt + yt*yt));
	}

}
