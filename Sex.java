
public class Sex {
	
	public String sex;
	
	public Sex(){
		
		double rand = Math.random();
		if(rand > 0.5) sex = "femme";
		else sex = "homme";
		
	}
	
	/*public String setSex(){
		
		double rand = Math.random();
		if(rand > 0.5) return "femme";
		return "homme";
		
	}*/

}
