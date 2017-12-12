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

	public static ArrayList<Vertice> simulatedAnnealing(ArrayList<Vertice> initialPath, float t, float r, int stop1, int stop2, long seed) throws ParseException {
		
		ArrayList<Graph> testedPaths =  new ArrayList<>();
		
		Random rNeighbor1 = new Random(seed);
		Random rNeighbor2 = new Random(seed*3);
		Random randomSimulated = new Random(seed*3);
		//System.out.println("r1: " + rNeighbor1.nextInt(vertices.size()-1));
		//System.out.println("r2: " + rNeighbor1.nextInt(vertices.size()-1));
		//System.out.println("r3: " + randomSimulated.nextFloat());
		
		//System.out.println("r1a: " + rNeighbor1.nextInt(vertices.size()-1));
		//System.out.println("r2a: " + rNeighbor1.nextInt(vertices.size()-1));
		//System.out.println("r3a: " + randomSimulated.nextFloat());
		
		ArrayList<Vertice> currentPath = initialPath;
		
		//currentPath = sortByReadyTime(currentPath);
		currentPath = sortByDueDate(currentPath);
		
		System.out.println("Original Path:");
		printPath(currentPath);

		currentPath = insertionHeuristic(currentPath);
		//currentPath = findPossibleSolution(currentPath, Main.distMatrix);
		
		System.out.println("Possible Solution Path:");
		printPath(currentPath);
		
		double currentSolution = isValidSolution(currentPath);
		
		while(currentSolution == -1) {
			
			alpha2 = randomSimulated.nextDouble();
			DecimalFormat df = new DecimalFormat("#.00");    
			String formate = df.format(alpha2); 
			alpha2 = (Double)df.parse(formate);
			
			alpha1 = 1 - alpha2;
			System.out.println("Alpha1: " + alpha1);
			System.out.println("Alpha2: " + alpha2);
			
			currentPath = sortByDueDate(currentPath);
			
			System.out.println("Original Path:");
			printPath(currentPath);

			currentPath = insertionHeuristic(currentPath);
			//currentPath = findPossibleSolution(currentPath, Main.distMatrix);
			
			System.out.println("Possible Solution Path:");
			printPath(currentPath);
			
			currentSolution = isValidSolution(currentPath);
			
		}
		
		ArrayList<Vertice> firstSolutionPath = currentPath;
		
		while(stop2 > 0) {
			while(stop1 > 0) {
				
				ArrayList<Vertice> candidatePath = neighbor(currentPath, rNeighbor1, rNeighbor2,testedPaths);

				double candidateSolution = isValidSolution(candidatePath);
				
				if(Main.debug)System.out.println("Original Neighbor Path:");
				if(Main.debug)printPath(candidatePath);
				
				int s = 0;
				while(candidateSolution == -1 && s < 5) {
					candidatePath = neighbor(candidatePath, rNeighbor1, rNeighbor2,testedPaths);
					//test 3-opt
					candidateSolution = isValidSolution(candidatePath);
					s++;
				}
				
				if(candidateSolution == -1){
					candidateSolution = 80000;
				}
				
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
					
					//we have to go back
					if(candidateSolution == 8000){
						candidatePath = firstSolutionPath;
						candidateSolution = currentSolution;
					}
					
				}
				
				stop1--;
				t = t * r;
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
		//v1 n pode ser inserido em outro lugar
		testPath.remove(testPath.size()-1);
		
		Vertice origin = currentPath.get(0);
		Vertice first = origin;
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
					
					ArrayList<Cost> innerCost = new ArrayList<Cost>();
					
					for(Vertice u : testPath){
						distance = calculateTotalDistance(solutionPath, i);
						
						double diu = calculateDistance(i, u, distance);
						
						double duj = calculateDistance(u, j, diu);
						double dij = calculateDistance(i, j, distance);
						
						double c1 = c1(diu,duj,dij, u.getReadyTime(), j.getReadyTime());
						
						
						Cost cost = new Cost(u, i, j, c1);
						placesToInsertVertice.add(cost);
					}
					//Cost c = innerCost.stream().min((f, s) -> Double.compare(f.getC1(), s.getC1())).get();
					//placesToInsertVertice.add(c);
					
				}

			}
			/*
			ArrayList<Cost> c2List = new ArrayList<Cost>();
			for(Cost c: placesToInsertVertice) {
				double dou = calculateDistance(origin, c.getUnvisited(), distance);
				double c2 = c2(dou, c.getC1());
				
				Cost cost = new Cost(c.getUnvisited(), c.getSource(), c.getDestiny(), c2);
				c2List.add(cost);
			}*/
			
			//min is the optimal?
			
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
	
	
	public static Vertice getVerticeMinDistance(Vertice first, ArrayList<Vertice> testPath){
		
		ArrayList<Edge> minDistanceEdges = new ArrayList<Edge>();

		int index = 1;
		for(Vertice second: testPath) {
			double distance = calculateDistance(first, second, 0);
			Edge e = new Edge(index, first, second, distance);
			index++;
			minDistanceEdges.add(e);
		}
		
		Edge e = minDistanceEdges.stream().min((f, s) -> Double.compare(f.getTravelTime(), s.getTravelTime())).get();
		
		return e.getDestiny();
	}
	
	public Vertice getVerticeMinTotalDistance(Vertice first, ArrayList<Vertice> testPath, double totalDistance){
		
		ArrayList<Edge> minDistanceEdges = new ArrayList<Edge>();

		int index = 1;
		for(Vertice second: testPath) {
			double distance = calculateDistance(first, second, totalDistance);
			Edge e = new Edge(index, first, second, distance);
			index++;
			minDistanceEdges.add(e);
		}
		
		Edge e = minDistanceEdges.stream().min((f, s) -> Double.compare(f.getTravelTime(), s.getTravelTime())).get();
		
		return e.getDestiny();
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
	
	public static ArrayList<Vertice> neighbor(ArrayList<Vertice> currentPath, Random rNeighbor1, Random rNeighbor2, ArrayList<Graph> testedPaths) {
		
		//trocar 2 vertices do caminho original
		
		ArrayList<Vertice> neighborPath =  new ArrayList<>();
		neighborPath.addAll(currentPath); //must confirm if changing this list will not change the solutionVertices list(does this even matter?)
		neighborPath.remove(0);
		neighborPath.remove(neighborPath.size()-1);
		
		//boolean isNewPath = false;
		//int limitCounter = 0;
		
		//while(!isNewPath && limitCounter < 10) {
			int index1 = rNeighbor1.nextInt(neighborPath.size()-1);
			int index2 = rNeighbor2.nextInt(neighborPath.size()-1);
			
			int neighbors = Math.abs(index1-index2);
			
			while(neighbors == 1 ) {
				index1 = rNeighbor1.nextInt(neighborPath.size()-1);
				index2 = rNeighbor2.nextInt(neighborPath.size()-1);
				neighbors = Math.abs(index1-index2);
			}
			
			Collections.swap(neighborPath, index1, index2);
			
			/*
			if(!testedPaths.isEmpty()) {
				for(Graph g : testedPaths) {
					if(neighborPath == g.getVertices()) {
						isNewPath = false;
						break;
					}
					else {
						if(testedPaths.indexOf(g) == testedPaths.size() -1) {
							isNewPath = true;
						}
					}
				}
			}
			else
				isNewPath = true;
			 
			limitCounter++;
			*/
		//}
		ArrayList<Vertice> returnPath =  new ArrayList<>();
		returnPath.add(currentPath.get(0));
		returnPath.addAll(neighborPath);
		returnPath.add(currentPath.get(0));
        
		/*
        if(isNewPath) {
        	Graph g = new Graph(returnPath, null);
        	testedPaths.add(g);
        }*/
        
		System.out.println("New neighbor to add to the list:");
		printPath(returnPath);
        
		
		return returnPath;
		
	}

	
	
	public static ArrayList<Vertice> findPossibleSolution(ArrayList<Vertice> currentPath) {
		
		ArrayList<Vertice> testPath = new ArrayList<Vertice>(currentPath);
		testPath.remove(0);
		testPath.remove(testPath.size()-1);
		Vertice origin = currentPath.get(0);
		Vertice first = origin;
		ArrayList<Vertice> solutionPath = new ArrayList<Vertice>();
		solutionPath.add(origin);
		double totalDistance = 0;
		
		//loop principal
		while(!testPath.isEmpty()) {
			Vertice vMinDueDate = testPath.stream().min((f, s) -> Double.compare(f.getDueDate(), s.getDueDate())).get();
			double vMinDistance = calculateDistance(first, vMinDueDate, totalDistance);
			
			/*
			ArrayList<Vertice> secondTestPath = new ArrayList<Vertice>(testPath);
			
			Vertice vDistance = getClosestVerticeWithinTimeWindow(first, testPath, distMatrix);
			double vNewDistance = calculateDistance(first, vDistance, totalDistance, distMatrix);

			//new test
			
			while(vMinDistance > vNewDistance) {
				secondTestPath.remove(vMinDueDate);
				vMinDueDate = secondTestPath.stream().min((f, s) -> Double.compare(f.getDueDate(), s.getDueDate())).get();
				vMinDistance = calculateDistance(first, vMinDueDate, totalDistance, distMatrix);
			}*/
			
			ArrayList<Edge> maxDistanceEdges = new ArrayList<Edge>();

			
			int index = 1;
			for(Vertice second: testPath) {
				double distance = calculateDistance(first, second, totalDistance);
				Edge e = new Edge(index, first, second, distance);
				index++;
				maxDistanceEdges.add(e);
			}
			
			
			while(!maxDistanceEdges.isEmpty()) {
				
				
				if(vMinDistance > vMinDueDate.getDueDate()) {
					//remove latest vertice and subtract total distance, add vmin DueDate
					//this solution ignores wainting times(so far)
					
					double problemInterval = vMinDistance - vMinDueDate.getDueDate();
					Double distance;
					
					problemInterval = vMinDueDate.getDueDate() - problemInterval;
					int i = 1;
					
					Vertice second = solutionPath.get(solutionPath.size()-i);
					
					
					//234 <  236
					while(second.getDueDate() > problemInterval){
						first = second;
						i++;
						second = solutionPath.get(solutionPath.size()-i);
					}
					
					i++;
					first = solutionPath.get(solutionPath.size()-i);
					
					int j = 1;
					while(j < i){
						Vertice retrace = solutionPath.get(solutionPath.size()-1);
						Vertice retraceTwoSteps = solutionPath.get(solutionPath.size()-2);
						
						distance = calculateDistance(retraceTwoSteps, retrace, 0);
						
						totalDistance = totalDistance - distance;
						
						solutionPath.remove(retrace);
						testPath.add(retrace);
						j++;
					}
					
					totalDistance = calculateDistance(first, vMinDueDate, totalDistance);
					solutionPath.add(vMinDueDate);
					testPath.remove(vMinDueDate);
					first = vMinDueDate;
					
					/*
					first = solutionPath.get(solutionPath.size()-2);
					Vertice second = solutionPath.get(solutionPath.size()-1);
					Double distance = calculateDistance(first, second, 0, distMatrix);
					
					totalDistance = totalDistance - distance;
					solutionPath.remove(second);
					testPath.add(second);
					
					totalDistance = calculateDistance(first, vMinDueDate, totalDistance, distMatrix);
					solutionPath.add(vMinDueDate);
					testPath.remove(vMinDueDate);
					first = vMinDueDate;
					*/
					
					System.out.println("Problem");
					break;
				}
				
				/*
				while(vMinDistance > vMinDueDate.getDueDate()) {
										
					if(secondTestPath.isEmpty()) {
						System.out.println("Terrible Error");
						return null;
					}
					
					secondTestPath.remove(vMinDueDate);
					
					if(!secondTestPath.isEmpty()) {
						if(secondTestPath.size() != 1) {
							vMinDueDate = secondTestPath.stream().max((f, s) -> Double.compare(f.getDueDate(), s.getDueDate())).get();
						}
						else
							vMinDueDate = secondTestPath.get(0);
						
						if(vMinDueDate.getVerticeId() == 28) {
							System.out.println("Problem");
						}
						
						vMinDistance = calculateDistance(first, vMinDueDate, totalDistance, distMatrix);
						
					}
					System.out.println("HATE");
				}
				*/
				Edge e = maxDistanceEdges.stream().min((f, s) -> Double.compare(f.getTravelTime(), s.getTravelTime())).get();
				
				if(e.getTravelTime() > e.getDestiny().getDueDate()){
					System.out.println("Problem");
				}
				
				
				if(e.getTravelTime() <= vMinDueDate.getDueDate()) {
					
					if(e.getTravelTime() + vMinDistance > vMinDueDate.getDueDate()) {
						totalDistance = calculateDistance(first, vMinDueDate, totalDistance);
						solutionPath.add(vMinDueDate);
						testPath.remove(vMinDueDate);
						first = vMinDueDate;
						break;
					}
					

					Vertice vMaxDistance = e.getDestiny();
					
					
					totalDistance = calculateDistance(first, vMaxDistance, totalDistance);
					solutionPath.add(vMaxDistance);
					testPath.remove(vMaxDistance);
					first = vMaxDistance;
					break;
				}
				else {
					
					maxDistanceEdges.remove(e);
					//System.out.println("Size of neighbor Edges available: " + maxDistanceEdges.size());
				}	
			}
			
		}
		totalDistance = calculateDistance(first, origin, totalDistance);
		System.out.println("Time travelled: " + totalDistance);
		solutionPath.add(origin);
		return solutionPath;
		

	}
	
	
	public static double isValidSolution(ArrayList<Vertice> path) {
		//a partir de um grafo procurar uma soluçao possivel para o problema:
		//retorna a soma dos tempos se achou uma solução viável ou -1 se não há como formar um ciclo a partir do caminho informado
		//ArrayList<Vertice> minTimeTravelVertices =  new ArrayList<Vertice>();
		
		int index = 0;
		
		//soma do tempo total do menor caminho a ser retornada
		double minTimeTravel = 0;
		
		Vertice v1 = path.get(index);
		//adicionar tempo de espera à solução se necessário(intervalo do primeiro vertice não começa em zero)
		//if(v1.getReadyTime() >  minTimeTravel){
		//	minTimeTravel = minTimeTravel + v1.getReadyTime();
		//}
		
		if(v1.getDueDate() >= minTimeTravel){
			index++;
			while(index < path.size()){
				Vertice v2 = path.get(index);
				
				minTimeTravel = calculateDistance(v1, v2, minTimeTravel);
				
				//tempo está fora da janela de tempo do proximo vertice
				if(v2.getDueDate() < minTimeTravel){
					if(Main.debug)System.err.println("Invalid path, time passed:" + minTimeTravel);
					if(Main.debug)System.err.println("Stopped on vertice: v" + v2.getVerticeId());
			        return -1;
				}

				v1 = path.get(index);
				index++;
			}
		}
		else{
			if(Main.debug)System.err.println("Invalid path, not even entered loop, time passed:" + minTimeTravel);
	        return -1;
		}
		
		System.out.println("Valid path found:" + minTimeTravel);
		sucessRate++;
		return minTimeTravel;
		
	}

	
	public static Vertice getClosestVerticeWithinTimeWindow(Vertice v1, ArrayList<Vertice> path,double[][] distMatrix) {
		ArrayList<Edge> validEdges =  new ArrayList<>();
		
		double distance = 0;
		//double range = 0;
		
		for(Vertice v2 : path) {
			if(v2 != v1 && v2 != path.get(path.size()-1)) {
				distance =  distMatrix[v1.getVerticeId()][v2.getVerticeId()];
				//range = v1.getReadyTime() + distance;
				
				//if(range > v2.getReadyTime() && range < v2.getDueDate()) {
					 Edge e = new Edge(v1.getVerticeId(), v1, v2, distance);
					 validEdges.add(e);
				//}
			}
		}
		
		if(!validEdges.isEmpty()) {
			Edge e = validEdges.stream().min((first, second) -> Double.compare(first.getTravelTime(), second.getTravelTime())).get();
			Vertice v = e.getDestiny();
			return v;
		}
		
		System.out.println("Unknown error");
		return null;
	}
	
	
	
	public static double getTravelTime(int xa, int xb, int ya, int yb) {
		int xt = xa - xb;
		int yt = ya - yb;
		return Math.floor(Math.sqrt(xt*xt + yt*yt));
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
	
	public static ArrayList<Vertice> sortByReadyTime(ArrayList<Vertice> path) {
		Vertice origin = path.get(0);
		
		ArrayList<Vertice> oldPath =  new ArrayList<>();
		oldPath.addAll(path);
		oldPath.remove(origin);
		oldPath.remove(path.get(path.size()-1));
		
		Collections.sort(oldPath, new Comparator<Vertice>() {
			@Override
			public int compare(Vertice o1, Vertice o2) {
				return o1.getReadyTime() - o2.getReadyTime();
			}
		});

        ArrayList<Vertice> newPath =  new ArrayList<>();
        newPath.add(origin);
        newPath.addAll(oldPath);
        newPath.add(origin);
        
        return newPath;
	}
	
	public static ArrayList<Vertice> sortByDistance(ArrayList<Vertice> path, double[][] distMatrix) {
		ArrayList<Integer> verticesToVisit = new ArrayList<>();
		
		for(Vertice v : path) {
			verticesToVisit.add(v.getVerticeId());
		}
		
		ArrayList<Vertice> newPath =  new ArrayList<Vertice>();
		
		ArrayList<Vertice> oldPath =  new ArrayList<Vertice>();
		oldPath.addAll(path);
		
		newPath.add(path.get(0));
		Vertice va = path.get(0);
		
		verticesToVisit.remove(0);
		verticesToVisit.remove(verticesToVisit.size() -1);
		
		while(!verticesToVisit.isEmpty()) {
			Vertice vb = getClosestVerticeWithinTimeWindow(va,oldPath,distMatrix);
			newPath.add(vb);
			verticesToVisit.remove((Integer)vb.getVerticeId());
			oldPath.remove(va);
			va = vb;
			

		}
		
		newPath.add(path.get(0));
		
		return newPath;
	}
}