import java.io.IOException;

public class Main {

    public static void main(String[] args ) throws IOException {

        int N = Integer.parseInt(args[0]);
        double Tmax = Double.parseDouble(args[1]);
        Simulation simulation = new Simulation();
        simulation.simulate(N, Tmax);
        Coalescence coalescence = new Coalescence(simulation.getPopulation());
        System.out.println(coalescence.toString());
    }
}
