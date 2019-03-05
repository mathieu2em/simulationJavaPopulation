import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Simulation{

	// evenements
	private TasBinaire eventQ;

	public TasBinaire getPopulation() {
		return population;
	}

	// population vivante
	private TasBinaire population = new TasBinaire();
	// objets pour generer les nombres aleatoires
	private AgeModel ageModel = new AgeModel();
	private Random RND = new Random();
	// compte rendu des individus
	static ArrayList<Integer> log = new ArrayList<Integer>();
	// cote de fidelité
	private double fidelite = 90/100;
	// ratio de naissances par femme
	private double ratio = 2.001;



	void simulate(int n, double Tmax) throws IOException {

		System.out.println("simulation en cours, un moment svp...");
		//creation du heap binaire d'evenements
		eventQ = new TasBinaire(); // file de priorité

		for (int i = 0; i < n; i++) {

			// creation des sim fondateurs
			naissance(0.0,null,null);
		}

		// compteur pour le log des annees
		double compteur = 0;

		while (!eventQ.isEmpty()) {
			Event E = eventQ.deleteMin(); // prochain événement
			// ajoute la population au log
			if ( E.getTime() > compteur ) {
				log.add(population.size());
				compteur += 100;
			}
			if (E.time > Tmax) {
				break; // arrêter à Tmax
			}
			if (E.subject.getDeathTime() > E.time) {
				gererEvent(E);
				// ligne necessaire pour gerer les evenements de mort
			} else if (E.getType() == 'm' && !(E.getTime() > Tmax) ) {
				gererEvent(E);
			}
		}
		System.out.println();
		System.out.println( " NOMBRE D'INDIVIDUS A CHAQUE 100 ANS :");
		System.out.println(log.toString());
		System.out.println(log.size());

	}

	// dispatch les evenements selon leur type
	public void gererEvent(Event E) throws IOException {

		switch(E.getType()){
			case 'm':
				gererMort(E);
			break;
			case 'r':
				gererReproduction(E);
			break;
			case 'n':
				gererNaissance(E);
			break;
		}
	}

	public void gererReproduction(Event E) {

		// si sims mort
		if (E.getSubject().getDeathTime() < E.getTime()){
			return;
		} else {

			if (E.getSubject().isMatingAge(E.getTime())) {

				conditionPartenaire(E);
				naissance(E.getTime(), E.getSubject(), E.getSubject().getMate());

				setMatingEvent(E.getTime(), E.getSubject());

			} else if (E.getTime() - E.getSubject().getBirthTime() < Sim.MAX_MATING_AGE_F){ // si elle est encore assez jeune pour se reproduire
				setMatingEvent(E.getTime(), E.getSubject());
			}
		}
	}
	// seule chose a gerer si mort est retirer de la population vivante
	public void gererMort(Event E) throws IOException {
		population.delete(E);
	}
	// comme le sim est deja cree il ne reste qu'a le mettre dans la liste de population vivante
	public void gererNaissance(Event E){

		population.insert(E);
	}

	public void naissance(double time, Sim maman, Sim papa){
		// cree un nouveau sim bb
		Sim bb = new Sim(maman,papa,time, (Math.random()<0.5)? Sim.Sex.F : Sim.Sex.M );

		setDeathTime(time,bb);
		// si femelle setup le temps de reproduction
		if( Sim.Sex.F.equals(bb.getSex())){
			setMatingEvent(time, bb);
		}

		Event N = new Event(time, bb, 'n');
		eventQ.insert(N);
	}

	public void setMatingEvent(double time, Sim bb){
		double minAge = (Sim.Sex.F.equals(bb.getSex()))? Sim.MIN_MATING_AGE_F : Sim.MIN_MATING_AGE_M;
		double maxAge = (Sim.Sex.F.equals(bb.getSex()))? Sim.MAX_MATING_AGE_F : Sim.MAX_MATING_AGE_M;
		double A = ageModel.randomWaitingTime(RND, ratio/ageModel.expectedParenthoodSpan(minAge,maxAge));
		Event R = new Event(A+time, bb, 'r');
		eventQ.insert(R);
	}
	// cree un temps de mort pour le sim et un event mort au bon moment.
	public void setDeathTime(double time, Sim bb){
		double d = ageModel.randomAge(RND);
		bb.setDeathTime(d+time);
		Event D = new Event( d+time, bb, 'm');
		eventQ.insert(D);
	}

	// verifie les conditions pour le partenaire
	public void conditionPartenaire(Event E){
		Random RND = new Random(); // générateur de nombres pseudoaléatoires
		Sim x = E.getSubject();
		Sim y = null;
		if (!x.isInARelationship(E.getTime()) || RND.nextDouble() > fidelite )
		{ // partenaire au hasard
			do
			{
				Sim z = population.findMate();
				if (z.getSex()!=x.getSex() && z.isMatingAge(E.time)) // isMatingAge() vérifie si z est de l'age adéquat
				{
					if (x.isInARelationship() // z accepte si x est infidèle
							|| !z.isInARelationship(E.getTime())
							|| RND.nextDouble()>fidelite)
					{  y = z;}
				}
			} while (y==null);
		} else
		{
			y = x.getMate();
		}
		x.setMate(y);
		y.setMate(x);
	}
	// tests personnelles
	public static void main(String[] args) throws IOException {
		Simulation simulation = new Simulation();
		simulation.simulate(1000,20000.0);
		Coalescence coalescence = new Coalescence(simulation.population);
		System.out.println(coalescence.toString());
	}
}