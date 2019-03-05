
import java.io.IOException;
import java.util.*;

public class Coalescence {

    //pères
    private TreeMap pointsCoalescenceP = new TreeMap();
    //mères
    private TreeMap pointsCoalescenceM = new TreeMap();
    //verificateur
    private HashSet<Sim> verif;

    public Coalescence(TasBinaire populationEntrante) throws IOException {

        System.out.println("transformation du heap");
        // on cree des copies de la population et
        // on transforme l'ordre du heap
        TasBinaire populationF = new TasBinaire();
        TasBinaire populationF2 = new TasBinaire();
        TasBinaire populationH2 = new TasBinaire();
        TasBinaire populationH = new TasBinaire();


        while (!populationEntrante.isEmpty()) {
            // on transfer la population dans un minPQ
            // (maxPQ mais avec leurs naissance negative)
            Event E = populationEntrante.deleteMin();
            E.setTime(E.getTime() * (-1));
            if (E.getSubject().getSex().equals(Sim.Sex.F)) {
                populationF.insert(E);
                populationF2.insert(E); // pour les meres des femmes
            } else {
                populationH.insert(E);
                populationH2.insert(E);// pour les meres des hommes
            }
        }

        checkAncestorsDAD(populationH);
        checkAncestorsMOM(populationH2);
        checkAncestorsDAD(populationF);
        checkAncestorsMOM(populationF2);
    }

    public void checkAncestorsDAD(TasBinaire tasAnalyse) throws IOException {

        verif = new HashSet<Sim>();
        // on gere la coalescence pour les peres
        while (!tasAnalyse.isEmpty()) {
            Event E = tasAnalyse.deleteMin();
            if (!E.getSubject().isFounder()) {

                Sim pere = E.getSubject().getFather();
                if (!verif.contains(pere)) {
                    Event P = new Event(pere.getBirthTime() * (-1), pere, 'z');
                    tasAnalyse.insert(P);
                    verif.add(pere);
                } else {
                    PointCoalescence point = new PointCoalescence((float)E.getSubject().getBirthTime(), tasAnalyse.size());
                    pointsCoalescenceP.put(point.getT(), point.getN());
                }
            } else {
                break;
            }
        }
    }

    public void checkAncestorsMOM(TasBinaire tasAnalyse) throws IOException {

        verif = new HashSet<Sim>();
        // gere la coalescence pour les meres
        while (!tasAnalyse.isEmpty()) {
            Event E = tasAnalyse.deleteMin();
            if (!E.getSubject().isFounder()) {

                Sim mere = E.getSubject().getMother();
                if (!verif.contains(mere)) {
                    Event P = new Event(mere.getBirthTime() * (-1), mere, 'z');
                    tasAnalyse.insert(P);
                    verif.add(mere);
                } else {
                    PointCoalescence point = new PointCoalescence((float)E.getSubject().getBirthTime(), tasAnalyse.size());
                    pointsCoalescenceM.put(point.getT(), point.getN());
                }
            }
            else {
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "peres : "+ pointsCoalescenceP.size()+ ":" + pointsCoalescenceP.toString() + " \n" + "meres : "+ pointsCoalescenceM.size() + ":" + pointsCoalescenceM.toString();
    }

    private class PointCoalescence {

        private float t;
        private int n = 0;

        public PointCoalescence(float t, int n) {
            this.t = t;
            this.n= n;
        }

        public void setN() {
            this.n++;
        }

        public float getT() {
            return t;
        }

        public int getN() {
            return n;
        }

        @Override
        public String toString() {
            return "{" +
                    "t=" + t +
                    ", n=" + n +
                    '}';
        }
    }

}
