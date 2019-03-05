import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TasBinaire {

	public Event[] heap;
	private int size;
	private static final int C = 4;
	private static final double minus_inf = Double.NEGATIVE_INFINITY;
	private static final double inf = Double.POSITIVE_INFINITY;
	private static final double logOf2 = (double) Math.log(2);

	public TasBinaire() {
		heap = new Event[C];
		Arrays.fill(heap, new Event(inf, 'z'));
		heap[0] = new Event(minus_inf, 's');
		heap[0].setIndex(0);
		size = 0;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty(){
		return ( size == 0 );
	}

	public void insert(Event event) {
		if (size+2 == heap.length) {
			resize(2);
		}
		size++;
		swim(size, event);
	}

	public Sim findMate(){
		Random RND = new Random();
		int sim2 = RND.nextInt(size)+1;
		return heap[sim2].getSubject();
	}

	private void swim(int i, Event event) {
		double p = event.getTime();
		while (p < parent_pr(i)) {
			set(i, heap[parent(i)]);
			i = parent(i);
		}
		set(i, event);
	}

	public void delete(Event event) throws IOException {
		if (event == null) {
			throw new IOException("delete() called with null event");
		}
		int i = event.getIndex();
		if (i > size) {
			throw new IOException("delete() called with event's index outside range");
		}
		Event leaf = heap[size];
		heap[size] = new Event(inf, 'z');
		if (size > 1) {
			sink_or_swim(i, leaf);
		}
		size--;
		if (heap.length/(double) (size+1) > C) {
			resize(1/(double)C);
		}
	}

	private void sink_or_swim(int i, Event event) {
		double pr = event.getTime();
		if (pr < parent_pr(i)) {
			swim(i, event);
		} else if (pr > lc_pr(i) || pr > rc_pr(i)) {
			sink(i, event);
		}
	}

	public Event deleteMin() {
		Event root = heap[1];
		Event leaf = heap[size];
		heap[size] = new Event(inf, 'z');
		if (size > 1) {
			sink(1, leaf);
		}
		size--;
		if (heap.length/(double) (size+1) > C) {
			resize(1/(double)C);
		}
		return root;
	}

	private void sink(int i, Event event) {
		double p_l = lc_pr(i);
		double p_r = rc_pr(i);
		double p = event.getTime();
		while (p > p_l || p > p_r) {
			if (p_r < p_l) {
				set(i, heap[right_child(i)]);
				i = right_child(i);
			}
			else {
				set(i, heap[left_child(i)]);
				i = left_child(i);
			}
			p_l = lc_pr(i);
			p_r = rc_pr(i);
		}
		set(i, event);
	}

	private void set(int i, Event event) {
		event.setIndex(i);
		heap[i] = event;
	}

	public ArrayList<Sim> plus_petits(double k) {
		ArrayList<Sim> l = new ArrayList<>();
		plus_petits(1, k, l);
		return l;
	}

	private void plus_petits(int i, double k, ArrayList<Sim> l) {
		if (i < heap.length && heap[i].getTime() <= k) {
			l.add(heap[i].getSubject());
			plus_petits(left_child(i), k, l);
			plus_petits(right_child(i), k, l);
		}
	}

	private void resize(double factor) {
		Event[] new_heap = new Event[(int)Math.ceil(heap.length*factor)];
		Arrays.fill(new_heap, new Event(inf, 'z'));
		int m = new_heap.length;
		for (int i = 0; i < size+1; i++) {
			new_heap[i] = heap[i];
		}
		heap = new_heap;
	}

	public String toString() {
		String s = "{";
		int i = 1;
		for (i = 1; i < heap.length-1; i++) {
			if (heap[i+1].getTime() == inf) {
				break;
			}
			s += "(" + heap[i].getTime() + "," + heap[i].getSubject() + "), ";
		}
		s += "(" + heap[i].getTime() + "," + heap[i].getSubject() + ")}";
		return s;
	}

	private int get_level(int i) {
		return (int) Math.floor(Math.log(i)/logOf2);
	}

	public String toStringFull() {
		String s = "{";
		for (int i = 0; i < heap.length-1; i++) {
			s += "(" + heap[i].getTime() + "," + heap[i].getSubject() + "), ";
		}
		s += "(" + heap[heap.length-1].getTime() + "," + heap[heap.length-1].getSubject() + ")}";
		return s;
	}

	private int parent(int i) {
		return i/2;
	}

	private double parent_pr(int i) {
		return heap[parent(i)].getTime();
	}

	private int left_child(int i) {
		return 2*i;
	}

	private double lc_pr(int i) {
		if (left_child(i) > heap.length-1) {
			return inf;
		}
		return heap[left_child(i)].getTime();
	}

	private int right_child(int i) {
		return 2*i + 1;
	}

	private double rc_pr(int i) {
		if (right_child(i) > heap.length-1) {
			return inf;
		}
		return heap[right_child(i)].getTime();
	}

}