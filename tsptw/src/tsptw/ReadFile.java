package tsptw;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadFile {
	
	public static ArrayList<Vertice> readFile(File file){
		
		ArrayList<Vertice> vertices =  new ArrayList<Vertice>();
		
        BufferedReader br = null;

        try {
        	
        	//TODO: fix execution of this code on the command prompt(low priority)
    		Scanner s = new Scanner(file); //exception in the jar file execution
    	

    		// Skip first two lines.
    		s.nextLine();
    		s.nextLine();

    		
    		// Read each line, ensuring correct format.
    		while (s.hasNext())
    		{
    			int id = s.nextInt();
    			if(id != 999){
        			int x = s.nextInt();  
        			int y = s.nextInt();  
        		    s.nextInt();
        		    int readyTime = s.nextInt(); 
        		    int dueDate =  s.nextInt(); 
        		    s.nextInt(); 
        		    Vertice vertice = new Vertice(id,x,y,readyTime,dueDate);
        		    vertices.add(vertice);
    			}
    			else{
    				break;
    			}

    		}
    		
    		//adicionar o primeiro vertice de novo para completar um ciclo
    		vertices.add(vertices.get(0));
    		
    		for(Vertice vertice : vertices){
    			System.out.println(vertice.getVerticeId() + "_" + vertice.getxCoord() + "_" +  vertice.getyCoord() + "_" + 
    					vertice.getReadyTime() + "_" +  vertice.getDueDate());

    		}

        } 
        catch (IOException e) {
        	System.err.println("here");
            e.printStackTrace();
        } 
        finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
            	System.err.println("File not found");
                ex.printStackTrace();
            }
        }
		return vertices;
		
	}
	
}
