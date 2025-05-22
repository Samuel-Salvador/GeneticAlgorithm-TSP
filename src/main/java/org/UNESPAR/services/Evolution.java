package org.UNESPAR.services;

import org.UNESPAR.entities.Chromosome;
import org.UNESPAR.entities.City;
import org.UNESPAR.entities.Population;

import java.util.ArrayList;
import java.util.List;

public class Evolution {

    private static final List<Double> distanceTraveledThroughGenerations = new ArrayList<>();
    private static double mutationChance = 5;
    private static final int maxGenerations = 500;

    public static int getMaxGenerations() {
        return maxGenerations;
    }

    public static List<Double> getDistanceTraveledThroughGenerations() {
        return distanceTraveledThroughGenerations;
    }

    public static void addDistance(double distance) {
        distanceTraveledThroughGenerations.add(distance);
    }

    public static double getMutationChance() {
        return mutationChance;
    }

    public static void setMutationChance(double mutationChance) {
        Evolution.mutationChance = mutationChance;
    }

    public static void evolveThroughGenerations(){

        int i = 0;
        while(i<1000){
            double lastDistanceTraveled = Population.selectSolution().getDistanceTraveled();
            Population.evolve();
            if(i==0){
                setMutationChance(5);
            }
            if(i==500){
                setMutationChance(10);
            }
            if(lastDistanceTraveled != Population.selectSolution().getDistanceTraveled()){
                i = 0;
            } else {
                i++;
            }
            System.out.printf("%d -- %.3f un.d.\n",i,Population.selectSolution().getDistanceTraveled());
            Evolution.addDistance(Population.selectSolution().getDistanceTraveled());
        }

        System.out.println("------------");
        System.out.print("Best solution: ");
        Population.selectSolution().print();
        System.out.print("Best solution total distance traveled: ");
        System.out.println(Population.selectSolution().getDistanceTraveled());
        //Graph.createGraphFile();
    }

    public static Chromosome diminishCO(List<Chromosome> selectedChromosomes){
        int currentIndex = 0;
        int rangeStart = (int)(Math.random() * Population.getCityQuantityPerChromosome());
        int rangeEnd = rangeStart;
        while(rangeEnd <= rangeStart){
            rangeEnd = (int)(Math.random() * Population.getCityQuantityPerChromosome());
        }

        List<City> childCityList = new ArrayList<>(selectedChromosomes.getFirst().getCitySequence().subList(rangeStart, rangeEnd+1));

        while(currentIndex<rangeStart){
            City currentCity = selectedChromosomes.getLast().getCitySequence().get(currentIndex);

            if(!childCityList.contains(currentCity)){
                childCityList.addFirst(selectedChromosomes.getLast().getCitySequence().get(currentIndex));
            }
            currentIndex++;
        }
        addIfNotPresent(selectedChromosomes.getLast(),childCityList);

       return new Chromosome(childCityList);
    }

    public static List<List<Chromosome>> preserveCO(List<Chromosome> parentsList){

        int breach = (int)(Math.random() * Population.getCityQuantityPerChromosome());

        List<City> childOneCitySequence = new ArrayList<>(parentsList.getFirst().getCitySequence().subList(0, breach+1));
        List<City> childTwoCitySequence = new ArrayList<>(parentsList.getLast().getCitySequence().subList(0, breach+1));

        addIfNotPresent(parentsList.getLast(),childOneCitySequence);
        addIfNotPresent(parentsList.getFirst(),childTwoCitySequence);

        Chromosome childOne = new Chromosome(childOneCitySequence);
        Chromosome childTwo = new Chromosome(childTwoCitySequence);
        List<Chromosome> childrenList = new ArrayList<>();
        childrenList.add(childOne);
        childrenList.add(childTwo);

        for(Chromosome child : childrenList){
            inversionMutation(child);
        }

        List<List<Chromosome>> evolutionList = new ArrayList<>();
        List<Chromosome> worstChromosomes = new ArrayList<>();
        worstChromosomes.add(selectWorstChromosomes());
        worstChromosomes.add(selectWorstChromosomes());



        evolutionList.add(worstChromosomes);
        evolutionList.add(childrenList);

        return evolutionList;
    }

    public static void inversionMutation(Chromosome chr){

        int drawChance = (int) (Math.random() * 100);
        if(drawChance <= mutationChance){
            int breach = (int)(Math.random() * Population.getCityQuantityPerChromosome());
            List<City> temporaryList = new ArrayList<>();
            List<City> invertedCityList = new ArrayList<>();
            for(int i = Population.getCityQuantityPerChromosome()-1; i > breach ; i--){
                temporaryList.add(chr.getCitySequence().get(i));
            }
            for (int i = 0 ; i <= breach ; i++){
                invertedCityList.add(chr.getCitySequence().get(i));
            }
            invertedCityList.addAll(temporaryList);
            invertedCityList.add(Population.getOriginCity());

            chr.evolve(invertedCityList);
        }
    }

    private static Chromosome selectWorstChromosomes(){
        Chromosome worstChromosome = Population.get().getFirst();
        for(Chromosome chr: Population.get()){
            if(chr.getFitness() < worstChromosome.getFitness()){
                if(!chr.hasWorstFitness()){

                    worstChromosome = chr;
                    chr.setWorstFitness(true);
                }
            }
        }
        return worstChromosome;
    }

    private static void addIfNotPresent(Chromosome fromList, List<City> toList){
        int currentIndex = 0;
        while(!(toList.size() == Population.getCityQuantityPerChromosome())){

            City currentCity = fromList.getCitySequence().get(currentIndex);
            if(!toList.contains(currentCity)){
                toList.add(currentCity);
            }
            currentIndex++;
        }
    }
}
