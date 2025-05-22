import org.UNESPAR.entities.Population;

import org.UNESPAR.resources.Chart;
import org.UNESPAR.resources.TSPFileReader;
import org.UNESPAR.services.Evolution;

public class Main {


    public static void main(String[] args) {

        long startBenchmark = System.nanoTime();
            Population.populateFromFile(3000,"./tsp-files/"+ TSPFileReader.fileName);
            Evolution.evolveThroughGenerations();
        long finishBenchmark = System.nanoTime();

        long durationInNano = finishBenchmark - startBenchmark;
        double durationInSeconds = durationInNano / 1_000_000_000.0;

        System.out.println("duration: "+durationInSeconds+" s");

        Chart.getDistanceChartFile();
    }
}