
public class Queue {

	private int debut;
	private int nelem;
	private Sim[] Q;
	private static final int CAPACITE_DEFAUT = 10000000;
	
	public Queue(){
		
		debut = 0;
		nelem = 0;
		Q = new Sim[CAPACITE_DEFAUT];
		
	}
	
	public Queue(int nombreSims){
		
		debut = 0;
		nelem = 0;
		Q = new Sim[nombreSims];
		
	}
	
	public boolean isEmpty(){
		
		return (nelem == 0);
		
	}
	
	public Sim dequeue() throws UnderflowException{
		
		if(isEmpty()) throw new UnderflowException(); //TODO
		Sim dequeue = Q[debut];
		Q[debut] = null;
		debut = (debut + 1) % Q.length;
		nelem--;
		return dequeue;
		
	}
	
	public void enqueue(Sim sujet) throws OverflowException{
		
		if(nelem == Q.length) throw new OverflowException(); //TODO
		int fin = (debut + nelem) % Q.length;
		Q[fin] = sujet;
		nelem++;
		
	}
	
}
