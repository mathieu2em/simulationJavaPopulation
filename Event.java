public class Event {

	public double time; // le temps
	public int index;
	public Sim subject;
	public char type; // les evenements possibles sont :
	// n = naissance, m = mort, r = reproduction, z = nope, h = head

	public Event(double time, int index, Sim subject, char type) {
		this.time = time;
		this.index = index;
		this.subject = subject;
		this.type = type;
	}

	public Event(double inf, char z) {
		time = inf;
		type = z;
	}

	public Event(double time, Sim fondateur, char s) {
		this.time = time;
		this.subject = fondateur;
		this.type = s;
	}


	public void setTime(double time) {
		this.time = time;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setSubject(Sim subject) {
		this.subject = subject;
	}

	public void setType(char type) {
		this.type = type;
	}

	public double getTime() {
		return time;
	}

	public int getIndex() {
		return index;
	}

	public Sim getSubject() {
		return subject;
	}

	public char getType() {
		return type;
	}
	@Override
	public String toString() {
		return "Event{" +
				"time=" + time +
				", index=" + index +
				", subject=" + subject +
				", type=" + type +
				'}';
	}

}