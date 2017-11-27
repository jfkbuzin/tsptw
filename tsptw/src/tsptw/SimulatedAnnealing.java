package tsptw;

import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {

	public Graph simulatedAnnealing(Graph initialSolution, int t, int r, int stop1, int stop2) {
		
		//criterio de parada = numero de iteraçoes de stop1 e 2(?)
		
		Graph current = initialSolution;
		
		while(stop2 > 0) {
			while(stop1 > 0) {
				
				Graph candidate = neighbor(current);
				
				int candidateSolution = solution(candidate);
				int currentSolution = solution(current);
				
				if(candidateSolution <= currentSolution) {
					current = candidate;
				}
				else {
					int delta = candidateSolution - currentSolution;
					if(Math.exp(-delta/t) > new Random().nextFloat()) {
						current = candidate;
					}
				}
				
				stop1--;
			}
			t = t * r;
			
			stop2--;
		}
		
		return current;
	}
	
	public Graph neighbor(Graph graph) {
		
		//tirar 2 arcos do grafo original e inserir 2 novos arcos, retornar o grafo modificado
		
		Graph neighbor = null;
		
		return neighbor;
		
	}
	
	public int solution(Graph graph) {
		//a partir de um grafo procurar uma soluçao possivel para o problema:
		//achar caminho com menor soma dos tempos de viagem entre todos os vertices
		ArrayList<Vertice> minTimeTravelVertices =  new ArrayList<Vertice>();
		
		//soma do tempo total do menor caminho a ser retornada
		int minTimeTravel = 0;
		
		return minTimeTravel;
		
	}
}
