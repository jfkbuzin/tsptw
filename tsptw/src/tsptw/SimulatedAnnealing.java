package tsptw;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class SimulatedAnnealing {
	
	public static int sucessRate = 0;
	
	public static double alpha1 = 0.6;
	public static double alpha2 = 0.4;
	
	//em geral - a1 = 0,6, a2 = 0,4
	//n80w140-2 - a1 = 0.89, a2 = 0.11
	//n150w140 - a1 = 0,88, a2 = 0,12
	//n200w140 = a1 = 0,92, a2 = 0,08

	public static ArrayList<Vertice> simulatedAnnealing(ArrayList<Vertice> initialPath, float t, float r, int stop1, int stop2, long seed) throws ParseException {
		
		Random rNeighbor1 = new Random(seed);
		Random rNeighbor2 = new Random(seed*3);
		Random rNeighbor3 = new Random(seed*9);
		Random randomSimulated = new Random(seed*3);
		//System.out.println("r1: " + rNeighbor1.nextInt(vertices.size()-1));
		//System.out.println("r2: " + rNeighbor1.nextInt(vertices.size()-1));
		//System.out.println("r3: " + randomSimulated.nextFloat());
		
		//System.out.println("r1a: " + rNeighbor1.nextInt(vertices.size()-1));
		//System.out.println("r2a: " + rNeighbor1.nextInt(vertices.size()-1));
		//System.out.println("r3a: " + randomSimulated.nextFloat());
		
		ArrayList<Vertice> currentPath = initialPath;
		
		currentPath = sortByDueDate(currentPath);
		
		if(Main.debug)System.out.println("Original Path:");
		if(Main.debug)printPath(currentPath);

		currentPath = insertionHeuristic(currentPath);
		
		if(Main.debug)System.out.println("Possible Solution Path:");
		if(Main.debug)printPath(currentPath);
		
		double currentSolution = isValidSolution(currentPath);
		
		
		while(currentSolution == -1) {
			
			alpha2 = randomSimulated.nextDouble();
			DecimalFormat df = new DecimalFormat("#.00");    
			String formate = df.format(alpha2); 
			alpha2 = (Double)df.parse(formate);
			
			alpha1 = 1 - alpha2;
			if(Main.debug)System.out.println("Alpha1: " + alpha1);
			if(Main.debug)System.out.println("Alpha2: " + alpha2);
			
			currentPath = sortByDueDate(currentPath);
			
			if(Main.debug)System.out.println("Original Path:");
			if(Main.debug)printPath(currentPath);

			currentPath = insertionHeuristic(currentPath);
			
			if(Main.debug)System.out.println("Possible Solution Path:");
			if(Main.debug)printPath(currentPath);
			
			currentSolution = isValidSolution(currentPath);
			
		}
		
		ArrayList<Vertice> firstSolutionPath = currentPath;
		
		while(stop2 > 0) {
			while(stop1 > 0) {
				
				
				if(sucessRate == 100){
					System.out.println("Minimum time to travel all vertices found: " + currentSolution);
					System.out.println("Seed used: " + seed);
					System.out.println("Sucess rate: " + sucessRate);
					System.out.println("Final Path:");
					printPath(currentPath);
					return currentPath;
				}
				
				
				ArrayList<Vertice> candidatePath = opt3(currentPath, rNeighbor1, rNeighbor2, rNeighbor3);

				double candidateSolution = isValidSolution(candidatePath);
				
				if(Main.debug)System.out.println("Original Neighbor Path:");
				if(Main.debug)printPath(candidatePath);
				
				/*
				int s = 0;
				while(candidateSolution == -1 && s < 5) {
					candidatePath = neighbor(currentPath, rNeighbor1, rNeighbor2);
					//test 3-opt
					candidateSolution = isValidSolution(candidatePath);
					s++;
				}
				
				if(candidateSolution == -1){
					candidateSolution = currentSolution;
				}*/
				
				if(candidateSolution <= currentSolution) {
					currentPath = candidatePath;
					currentSolution = candidateSolution;
				}
				else {
					double delta = candidateSolution - currentSolution;
					if(Math.exp(-delta/t) > randomSimulated.nextDouble()) {
						currentPath = candidatePath;
						currentSolution = candidateSolution;
					}
					
					/*
					//we have to go back
					if(candidateSolution == 8000){
						candidatePath = firstSolutionPath;
						candidateSolution = currentSolution;
					}*/
					
				}
				
				stop1--;
			}
			t = t * r;
			
			stop2--;
		}
		
		//System.out.println("All neighbors:");
		//for(Graph g : testedPaths) {
		//	printPath(g.getVertices());
		//}
		
		System.out.println("Minimum time to travel all vertices found: " + currentSolution);
		System.out.println("Seed used: " + seed);
		System.out.println("Sucess rate: " + sucessRate);
		System.out.println("Final Path:");
		printPath(currentPath);
		return currentPath;
		
	}

	
	public static ArrayList<Vertice> insertionHeuristic(ArrayList<Vertice> currentPath){
		
		ArrayList<Vertice> testPath = new ArrayList<Vertice>(currentPath);
		testPath.remove(0);
		testPath.remove(testPath.size()-1);
		
		Vertice origin = currentPath.get(0);
		ArrayList<Vertice> solutionPath = new ArrayList<Vertice>();
		solutionPath.add(origin);
		double distance = 0;
		
		//get element with biggest due date
		Vertice second = currentPath.get(currentPath.size()-2);
		
		solutionPath.add(second);
		testPath.remove(second);
		//solution has 2 vertices
		
		while(!testPath.isEmpty()) {
			
			ArrayList<Cost> placesToInsertVertice = new ArrayList<Cost>();
			int index = 1;
			
			for(Vertice i : solutionPath){
				if(solutionPath.size() != index) {
					Vertice j = solutionPath.get(index);
					index++;
					
					for(Vertice u : testPath){
						distance = calculateTotalDistance(solutionPath, i);
						
						double diu = calculateDistance(i, u, distance);
						
						double duj = calculateDistance(u, j, diu);
						double dij = calculateDistance(i, j, distance);
						
						
						double c1 = c1(diu,duj,dij, u.getReadyTime(), j.getReadyTime());
						//double c1 = c1(diu,duj,dij, bju, bj);
						
						Cost cost = new Cost(u, i, j, c1);
						placesToInsertVertice.add(cost);
					}
					
				}

			}
			
			ArrayList<Cost> validPlacesToInsertVertice = new ArrayList<Cost>();
			for(Cost c : placesToInsertVertice) {
				if(c.getUnvisited().getReadyTime() < c.getDestiny().getDueDate()) {
					distance = calculateTotalDistance(solutionPath, c.getSource());
					double diu = calculateDistance(c.getSource(), c.getUnvisited(), distance);
					if(diu < c.getUnvisited().getDueDate()) {
						validPlacesToInsertVertice.add(c);
					}
				}
			}
			
			if(validPlacesToInsertVertice.isEmpty()) {
				System.out.println("Invalid alphas ");
				return currentPath;

			}
			
			Cost c;
			if(validPlacesToInsertVertice.size() > 1) {
				c = validPlacesToInsertVertice.stream().min((f, s) -> Double.compare(f.getC1(), s.getC1())).get();
			}else
				c = validPlacesToInsertVertice.get(0);
			

			Vertice placeToPut = c.getDestiny();
			solutionPath.add(solutionPath.indexOf(placeToPut), c.getUnvisited());
			testPath.remove(c.getUnvisited());
			
		}
		
		solutionPath.add(origin);
		
		return solutionPath;
		
	}
	
	
	
	public static double c1(double diu,double duj, double dij, int bju, int bj){
		return alpha1*(diu + duj - dij) + alpha2*(bju - bj); //0,6 e 0,4
	}
	
	
	
	public static double c2(double dou,double c1){
		return dou - c1;
	}
	

	public static double calculateDistance(Vertice origin, Vertice candidadeVertice, double candidateTimeTravel) {
		double distance = candidateTimeTravel + Main.distMatrix[origin.getVerticeId()][candidadeVertice.getVerticeId()];
		//adicionar tempo de espera à solução se necessário
		if(candidadeVertice.getReadyTime() >  distance){
			distance = distance + (candidadeVertice.getReadyTime() - distance);
		}
		return distance;
	}
	
	public static double calculateTotalDistance(ArrayList<Vertice> currentPath, Vertice last) {
		
		ArrayList<Vertice> tempPath = new ArrayList<Vertice>(currentPath);
		
		Vertice origin = tempPath.get(0);
		tempPath.remove(0);
		double distance = 0;
		
		for(Vertice destiny : currentPath){
			
			if(destiny == last) {
				distance = calculateDistance(origin, destiny, distance);
				break;
			}
			
			distance = calculateDistance(origin, destiny, distance);
			origin = destiny;
		}

		return distance;
	}
	
	public static ArrayList<Vertice> opt3(ArrayList<Vertice> currentPath, Random rNeighbor1, Random rNeighbor2, Random rNeighbor3){
		ArrayList<Edge> edges =  new ArrayList<Edge>();
		
		ArrayList<Vertice> neighborPath =  new ArrayList<>();
		neighborPath.addAll(currentPath); 
		neighborPath.remove(0);
		
		double solution = -1;
		
		ArrayList<Vertice> returnPath =  new ArrayList<>();
		
		while(solution == -1){
			
			Vertice origin = currentPath.get(0);
			
			for(Vertice v : neighborPath){
				double distance = calculateDistance(origin, v, 0);
				Edge e = new Edge(0, origin, v, distance);
				edges.add(e);
				origin = v;
			}
			int index1 = rNeighbor1.nextInt(edges.size()-1);
			int index2 = rNeighbor2.nextInt(edges.size()-1);
			int index3 = rNeighbor2.nextInt(edges.size()-1);
			
			while(Math.abs(index1 - index2) ==1 || Math.abs(index1 - index3) ==1 || Math.abs(index2 - index3) ==1 
					|| index1 == index2 || index2 == index3 || index1 == index3 || index1 == 0
					|| index2 == 0
					|| index3 == 0){
				
				index1 = rNeighbor1.nextInt(edges.size()-1);
				index2 = rNeighbor2.nextInt(edges.size()-1);
				index3 = rNeighbor2.nextInt(edges.size()-1);
			}
			
			ArrayList<Integer> indexes = new ArrayList<Integer>();
			indexes.add(index1);
			indexes.add(index2);
			indexes.add(index3);
			Collections.sort(indexes);
			
			for(int i : indexes){
				Edge delete = edges.get(i);
				delete.setDestiny(null);
				delete.setSource(null);
			}
			

			
			int edgeIndex = indexes.get(0);
			
			int index = edges.indexOf(edges.get(edgeIndex));
			
			
			for(int i : indexes){
				index = connectRoute(edges, edges.get(i-1),index);
			}
			
			
			
			returnPath.add(edges.get(0).getSource());
			returnPath.add(edges.get(0).getDestiny());
			
			Vertice connect = (edges.get(0).getDestiny());
			while(returnPath.size() < currentPath.size()){
				for(Edge e : edges){
					if(e.getSource() == connect){
						returnPath.add(e.getDestiny());
						connect = e.getDestiny();
						break;
					}
						
				}
			}

			System.out.println("New neighbor to add to the list:");
			printPath(returnPath);
			
			solution = isValidSolution(returnPath);
			
			if(solution == -1){
				returnPath.clear();
				edges.clear();
			}
			
		}
		


		return returnPath;
	}
	
	public static int connectRoute(ArrayList<Edge> edges, Edge edge, int index){
		
		Vertice toChange = edge.getDestiny();
		int indexNext = index+1;
		
		boolean madeNewEdge = false;
		
		while(!madeNewEdge){
			
			for(int j=indexNext; j<edges.size(); j++){
				Edge e = edges.get(j);
				
				if(e.getSource() == null){
					
					Edge next = edges.get(j+1);
					Vertice toSwap = next.getSource();
					
					Edge newEdge = new Edge(0, toChange, toSwap, 0);
					edges.add(newEdge);
					madeNewEdge = true;
					
					index = j;
					break;
				}
					
			}
			if(!madeNewEdge){
				index = 0;
				indexNext = 0;
			}
				
		}
		return index;
	}
	
	public static ArrayList<Vertice> neighbor(ArrayList<Vertice> currentPath, Random rNeighbor1, Random rNeighbor2) {
		
		//trocar 2 vertices do caminho original em cada subrota de 5 vertices
		
		ArrayList<Vertice> neighborPath =  new ArrayList<>();
		neighborPath.addAll(currentPath); 
		neighborPath.remove(0);
		neighborPath.remove(neighborPath.size()-1);
		
		int listSize = neighborPath.size()-1;
		int clusterSize = listSize/4;
		int clusterBegin = 0;
		int clusterEnd = clusterSize;
		
		while(clusterEnd < listSize){
			ArrayList<Vertice> clusterPath =  new ArrayList<>();
			for(int i =clusterBegin; i<= clusterEnd;i++){
				clusterPath.add(neighborPath.get(i));
			}
			int index1 = rNeighbor1.nextInt(clusterPath.size()-1);
			int index2 = rNeighbor2.nextInt(clusterPath.size()-1);
			
			while(index1 == index2){
				index1 = rNeighbor1.nextInt(clusterPath.size()-1);
				index2 = rNeighbor2.nextInt(clusterPath.size()-1);
			}
			
			Collections.swap(neighborPath, index1+clusterBegin, index2+clusterBegin);
			
			clusterBegin = clusterEnd+1;
			clusterEnd = clusterEnd+clusterSize+1;
			
			//test if found solution
			ArrayList<Vertice> testPath =  new ArrayList<>();
			testPath.add(currentPath.get(0));
			testPath.addAll(neighborPath);
			testPath.add(currentPath.get(0));
			double validsolution = isValidSolution(testPath);
			if(validsolution != -1){
				break;
			}
			
		}
		
		ArrayList<Vertice> returnPath =  new ArrayList<>();
		returnPath.add(currentPath.get(0));
		returnPath.addAll(neighborPath);
		returnPath.add(currentPath.get(0));
        
		System.out.println("New neighbor to add to the list:");
		printPath(returnPath);
        
		
		return returnPath;
		
	}

	public static double isValidSolution(ArrayList<Vertice> path) {
		//a partir de um grafo procurar uma soluçao possivel para o problema:
		//retorna a soma dos tempos se achou uma solução viável ou -1 se não há como formar um ciclo a partir do caminho informado
		
		int index = 0;
		
		//soma do tempo total do menor caminho a ser retornada
		double minTimeTravel = 0;
		
		Vertice v1 = path.get(index);
		
		if(v1.getDueDate() >= minTimeTravel){
			index++;
			while(index < path.size()){
				Vertice v2 = path.get(index);
				
				minTimeTravel = calculateDistance(v1, v2, minTimeTravel);
				
				//tempo está fora da janela de tempo do proximo vertice
				if(v2.getDueDate() < minTimeTravel){
					if(Main.debug)System.out.println("Invalid path, time passed:" + minTimeTravel);
					if(Main.debug)System.out.println("Stopped on vertice: v" + v2.getVerticeId());
			        return -1;
				}

				v1 = path.get(index);
				index++;
			}
		}
		else{
			if(Main.debug)System.out.println("Invalid path, not even entered loop, time passed:" + minTimeTravel);
	        return -1;
		}
		
		if(Main.debug)System.out.println("Valid path found:" + minTimeTravel);
		sucessRate++;
		return minTimeTravel;
		
	}

	
	public static void printPath(ArrayList<Vertice> path) {
		for(Vertice v : path){
			System.out.print(" v" + v.getVerticeId());
		}
		System.out.println("");
	}
	
	public static ArrayList<Vertice> sortByDueDate(ArrayList<Vertice> path) {
		Collections.sort(path, new Comparator<Vertice>() {
			@Override
			public int compare(Vertice o1, Vertice o2) {
				return o1.getDueDate() - o2.getDueDate();
			}
		});
        //se o primeiro vertice trocou, trocar o ultimo tambem
        if(path.get(0).getVerticeId() != path.get(path.size()-1).getVerticeId()){
        	
        	Vertice lastVertice = path.get(path.size()-1);
        	path.remove(lastVertice);
        	Vertice firstVertice = path.get(0);
        	path.add(firstVertice);

        }
        ArrayList<Vertice> newPath =  new ArrayList<>();
        newPath.add(path.get(path.size()-2));
      
        for(Vertice v: path) {
        	newPath.add(v);
        }
        
        newPath.remove(newPath.size()-1);
        
        return newPath;
	}
	
}