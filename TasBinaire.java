
public class TasBinaire {
	
	private Sim tas[];
	
	
	public void insert(Sim sujet, int n){
		
		swim(sujet, n+1, tas);
		
	}
	
	public void swim(Sim sujet, int i, Sim tas[]){
		
		int p = (int)Math.floor(i/2);
		while(p != 0 && tas[p].getDeathTime() > sujet.getDeathTime()){
			
			tas[i] = tas[p];
			i = p;
			p = (int)Math.floor(i/2);
			
		}
		
		tas[i] = sujet;
		
	}
	
	

}
