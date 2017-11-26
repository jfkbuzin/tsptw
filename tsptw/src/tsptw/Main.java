package tsptw;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
		
		System.out.println("ok");
		
		Graph graph = new Graph(vertices,edges);
		
		
	}

}
