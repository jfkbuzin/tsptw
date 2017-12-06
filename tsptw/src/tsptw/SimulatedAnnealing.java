package tsptw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SimulatedAnnealing {
	//TODO: tudo que está aqui ainda nao foi testado

	public static ArrayList<Vertice> simulatedAnnealing(ArrayList<Vertice> initialPath, float t, float r, int stop1, int stop2, long seed) {
		
		
		
		Random rNeighbor1 = new Random(seed);
		Random rNeighbor2 = new Random(seed*3);
		Random randomSimulated = new Random(seed*3);
		//System.out.println("r1: " + rNeighbor1.nextInt(vertices.size()-1));
		//System.out.println("r2: " + rNeighbor1.nextInt(vertices.size()-1));
		//System.out.println("r3: " + randomSimulated.nextFloat());
		
		//System.out.println("r1a: " + rNeighbor1.nextInt(vertices.size()-1));
		//System.out.println("r2a: " + rNeighbor1.nextInt(vertices.size()-1));
		//System.out.println("r3a: " + randomSimulated.nextFloat());
		
		//criterio de parada = numero de iteraçoes de stop1 e 2(?)
		
		ArrayList<Vertice> currentPath = initialPath;
		
		double currentSolution = solution(currentPath);
		
		//if current solution is invalid. change path until solution is found.
		int i = 0;
		while(currentSolution == -1 && i < 5){
			currentPath = neighbor(currentPath, rNeighbor1, rNeighbor2);
			currentSolution = solution(currentPath);
			i++;
		}

		while(stop2 > 0) {
			while(stop1 > 0) {
				
				ArrayList<Vertice> candidatePath = neighbor(currentPath, rNeighbor1, rNeighbor2);
				double candidateSolution = solution(candidatePath);
				
				//if candidate solution is invalid
				i =0;
				while(candidateSolution == -1 && i < 5){
					candidatePath = neighbor(candidatePath, rNeighbor1, rNeighbor2);
					candidateSolution = solution(candidatePath);
				}
				
				if(candidateSolution <= currentSolution) {
					currentPath = candidatePath;
					currentSolution = candidateSolution;
				}
				else {
					double delta = candidateSolution - currentSolution;
					if(Math.exp(-delta/t) > randomSimulated.nextFloat()) {
						currentPath = candidatePath;
						currentSolution = candidateSolution;
					}
				}
				
				stop1--;
				t = t * r;
			}
			//t = t * r;
			
			stop2--;
		}
		
		System.out.println("Minimum time to travel all vertices found: " + currentSolution);
		return currentPath;
	}
	
	public static ArrayList<Vertice> neighbor(ArrayList<Vertice> currentPath, Random rNeighbor1, Random rNeighbor2) {
		
		//trocar 2 vertices do caminho original
		
		ArrayList<Vertice> neighborPath =  new ArrayList<>();
		neighborPath.addAll(currentPath); //must confirm if changing this list will not change the solutionVertices list(does this even matter?)
		
		//-1 para nao trocar o ultimo vertice do caminho(sempre sera o mesmo que o primeiro)
		int index1 = rNeighbor1.nextInt(neighborPath.size()-1);
		int index2 = rNeighbor2.nextInt(neighborPath.size()-1);
		
        Collections.swap(neighborPath, index1, index2);
       
        //se o primeiro vertice trocou, trocar o ultimo tambem
        if(neighborPath.get(0) != neighborPath.get(neighborPath.size())){
        	
        	Vertice lastVertice = neighborPath.get(neighborPath.size());
        	neighborPath.remove(lastVertice);
        	Vertice firstVertice = neighborPath.get(0);
        	neighborPath.add(firstVertice);

        }
        
        System.out.println("Neighbor path, might not be a valid solution:");
		for(Vertice vertice : neighborPath){
			System.out.println(vertice.getVerticeId() + "_" + vertice.getxCoord() + "_" +  vertice.getyCoord() + "_" + 
					vertice.getReadyTime() + "_" +  vertice.getDueDate());

		}
		
		return neighborPath;
		
	}
	
	public static double solution(ArrayList<Vertice> path) {
		//a partir de um grafo procurar uma soluçao possivel para o problema:
		//retorna a soma dos tempos se achou uma solução viável ou -1 se não há como formar um ciclo a partir do caminho informado
		//ArrayList<Vertice> minTimeTravelVertices =  new ArrayList<Vertice>();
		
		int index = 0;
		
		//soma do tempo total do menor caminho a ser retornada
		double minTimeTravel = 0;
		
		Vertice v1 = path.get(index);
		//adicionar tempo de espera à solução se necessário(intervalo do primeiro vertice não começa em zero)
		if(v1.getReadyTime() >  minTimeTravel){
			minTimeTravel = minTimeTravel + v1.getReadyTime();
		}
		
		if(v1.getDueDate() >= minTimeTravel){
			index++;
			while(index <= path.size()){
				Vertice v2 = path.get(index);
				
				minTimeTravel = minTimeTravel + getTravelTime(v1.getxCoord(), v2.getxCoord(), v1.getyCoord(), v2.getyCoord());
					
				//adicionar tempo de espera à solução se necessário
				if(v2.getReadyTime() >  minTimeTravel){
					minTimeTravel = minTimeTravel + (v2.getReadyTime() - minTimeTravel);
				}
				
				//tempo está fora da janela de tempo do proximo vertice
				if(v2.getDueDate() < minTimeTravel){
			        System.out.println("Invalid path");
			        return -1;
				}

				v1 = path.get(index);
				index++;
			}
		}
		else{
	        System.out.println("Invalid path");
	        return -1;
		}
		
		return minTimeTravel;
		
	}
	public static double getTravelTime(int xa, int xb, int ya, int yb) {
		int xt = xa - xb;
		int yt = ya - yb;
		return Math.floor(Math.sqrt(xt*xt + yt*yt));
	}
}
