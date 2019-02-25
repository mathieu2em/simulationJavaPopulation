
public class Event {

	private float time;
	public Sim subject;
	
	public Event(Sim subject, float time){
		
		this.time = time;
		this.subject = subject;
		
	}
	
	public float getTime(){
		
		return time;
		
	}
}
