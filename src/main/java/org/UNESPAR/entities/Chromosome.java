package org.UNESPAR.entities;

import java.util.List;

public class Chromosome {

    private List<City> citySequence;

    private double distanceTraveled;
    private double fitness;
    private double standOutPercent;
    private boolean selected = false;
    private boolean worstFitness = false;

    public Chromosome( List<City> citySequence) {
        this.citySequence = citySequence;
        this.citySequence.add(this.citySequence.getFirst());
        this.distanceTraveled = calculateDistanceTraveled();
        this.fitness = 1 / distanceTraveled;
    }

    public boolean hasWorstFitness() {
        return worstFitness;
    }

    public void setWorstFitness(boolean worstFitness) {
        this.worstFitness = worstFitness;
    }

    public List<City> getCitySequence() {
        return citySequence;
    }

    public void setCitySequence(List<City> citySequence) {
        this.citySequence = citySequence;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(double distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }

    public double getStandOutPercent() {
        return standOutPercent;
    }

    public void setStandOutPercent(){
        this.standOutPercent = ( getFitness() / Population.getFitnessSum() ) * 100;
    }

    public double calculateDistanceTraveled(){
        double distanceTraveled = 0.0;
        for (int i=0 ; i<citySequence.size()-1 ; i++){
            distanceTraveled+= Math.sqrt(
                    Math.pow(citySequence.get(i+1).getCordX()-citySequence.get(i).getCordX(),2) +
                            Math.pow(citySequence.get(i+1).getCordY()-citySequence.get(i).getCordY(),2));
        }
        return distanceTraveled;
    }

    public void print(){
        for(int i=0 ; i<citySequence.size() ; i++){
            if( !( i == citySequence.size()-1 ) ){
                System.out.print(citySequence.get(i).getName() + " - ");
            }else{
                System.out.print(citySequence.get(i).getName());
            }
        }
        System.out.println();
    }

    public void evolve( List<City> newCitySequence ) {
        setCitySequence(newCitySequence);
        setDistanceTraveled(calculateDistanceTraveled());
        setFitness(1 / getDistanceTraveled());
        setWorstFitness(false);
    }


}
