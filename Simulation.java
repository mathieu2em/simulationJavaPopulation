import java.util.PriorityQueue;

public class Simulation {

	private Queue femmes;
	private Queue hommes;
	private TasBinaire hommesParTempsDeMort;
	private static int nhommes = 0;
	
	public Simulation(){
		
		femmes = new Queue();
		hommes = new Queue();
		hommesParTempsDeMort = new TasBinaire();
		
		
	}
	
	public void simulate(int n, double Tmax)
	{
		
		PriorityQueue<Event> eventQ = new PriorityQueue<Event>();
		
		for(int i = 0; i < n; i++){
			
			Sex sex = new Sex();
			Sim fondateur = new Sim(sex.sex);
			if(fondateur.getSex() == "homme"){ 
				
				nhommes++;
				hommes.enqueue(fondateur);
				hommesParTempsDeMort.insert(fondateur, nhommes);
				
			}
			else{
				
				femmes.enqueue(fondateur);
				Event E = new Event(fondateur, fondateur.reproTime);  //TODO
				eventQ.add(E);
				
			}
			
						
		}
		
		while(!eventQ.isEmpty())
		{
			
			Event E = eventQ.poll();
			if(E.getTime() > Tmax) break;
			if(E.subject.getDeathTime() > E.getTime())
			{
				
				//TODO Supprimer tous les sims deathTime < E.time
				//TODO choisir mate
				//TODO creer un noveau sim
				
			}
			
		}
		
	}
}
