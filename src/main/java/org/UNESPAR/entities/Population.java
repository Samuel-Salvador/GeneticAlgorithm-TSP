package org.UNESPAR.entities;

import org.UNESPAR.resources.TSPFileReader;
import org.UNESPAR.services.Evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Population {

    private static int size;
    private static City originCity;
    private static Integer cityQuantityPerChromosome;
    private static final List<Chromosome> population = new ArrayList<>();
    private static double fitnessSum;
    private static final Stack<Chromosome> evolvingStack = new Stack<>();

    public static List<Chromosome> get() {
        return population;
    }

    public static double getFitnessSum(){
        return fitnessSum;
    }

    public static void setFitnessSum(){
        double fitnessSum = 0.0;

        for(Chromosome chr : population){
            fitnessSum += chr.getFitness();
        }
        Population.fitnessSum = fitnessSum;
    }

    public static Integer getCityQuantityPerChromosome() {
        return cityQuantityPerChromosome;
    }

    public static City getOriginCity() {
        return originCity;
    }

    public static void populateFromFile(int size, String fileName ){
        Population.size = size;
        List<City> allCities = TSPFileReader.getCitiesFromFile(fileName);
        Population.cityQuantityPerChromosome = allCities.size();
        Collections.shuffle(allCities);
        Population.originCity = allCities.getFirst();

        for( int i=0 ; i<size ; i++ ){
            List<City> citySequence = new ArrayList<>(List.copyOf(allCities));
            citySequence.remove(originCity);
            Collections.shuffle(citySequence);
            citySequence.addFirst(originCity);
            Chromosome newChromosome = new Chromosome(citySequence);

            population.add(newChromosome);
        }
        setFitnessSum();

        for(Chromosome chr : population){
            chr.setStandOutPercent();
        }
    }

    public static void print(){
        for(Chromosome chr : population){
            chr.print();
        }
    }

    public static void printRouletteChart(){
        System.out.println("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _");
        System.out.print("Chromosome\t\tDistance\t\tFitness\t\t\t\tfA(x)\n");

        for(int i=0;i<size;i++){
            System.out.printf("%d\t\t\t\t%.2f\t\t\t%.10f\t\t%.2f %%\n",
                    i+1,
                    population.get(i).getDistanceTraveled(),
                    population.get(i).getFitness(),
                    population.get(i).getStandOutPercent());
        }
        System.out.println("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _");


    }

    public static void selectChromosome(int quantity){
        List<Chromosome> selectedChromosomes = new ArrayList<>();
        for (Chromosome chr : population){
            if(chr.isSelected()){
                chr.setSelected(false);
            }
        }
        List<Chromosome> notSelectedChromosomes = new ArrayList<>();
        for (Chromosome chr : population){
            if(!chr.isSelected()){
                notSelectedChromosomes.add(chr);
            }
        }
        for(int i=0 ; i < quantity ; i++){

            double percentageSeen = 0.0;
            double drawnNumber;
            if(selectedChromosomes.isEmpty()){
                drawnNumber = (Math.random()*100);
            }else{
                drawnNumber = (Math.random()*(100-selectedChromosomes.getFirst().getStandOutPercent()));
            }

            for (Chromosome chr : notSelectedChromosomes){
                percentageSeen += chr.getStandOutPercent();

                if(percentageSeen >= drawnNumber){

                    notSelectedChromosomes.remove(chr);
                    chr.setSelected(true);
                    selectedChromosomes.add(chr);
                    break;
                }
            }
        }

        evolvingStack.addAll(selectedChromosomes);
    }

    public static void evolve(){
        for(int i=0 ; i < size/2 ; i++){
            selectChromosome(2);
        }
        List<Chromosome> chromosomesList = new ArrayList<>();

        while(!evolvingStack.empty()){
            chromosomesList.add(evolvingStack.pop());
            chromosomesList.add(evolvingStack.pop());
            List<List<Chromosome>> evolutionList = Evolution.preserveCO(chromosomesList);

            int firstParentIndex = get().indexOf(evolutionList.getFirst().getFirst());
            int secondParentIndex = get().indexOf(evolutionList.getFirst().getLast());

            get().get(firstParentIndex).evolve(evolutionList.getLast().getFirst().getCitySequence());
            get().get(secondParentIndex).evolve(evolutionList.getLast().getLast().getCitySequence());
        }

        Population.setFitnessSum();

        for(Chromosome chr : get()){
            chr.setStandOutPercent();
            chr.setWorstFitness(false);
        }
    }

    public static Chromosome selectSolution(){
        Chromosome solution = population.getFirst();
        for(Chromosome chr: population){
            if(solution.getFitness()<=chr.getFitness()){
                solution = chr;
            }
        }
        return solution;
    }
}
