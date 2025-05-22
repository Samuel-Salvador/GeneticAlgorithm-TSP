package org.UNESPAR.resources;

import org.UNESPAR.services.Evolution;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.markers.None;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Chart {

    public static void getDistanceChartFile(){
        List<Double> distanceTraveledThroughGenerations = Evolution.getDistanceTraveledThroughGenerations();
        List<Integer> generations = new ArrayList<>();
        distanceTraveledThroughGenerations.forEach(x-> generations.add(distanceTraveledThroughGenerations.indexOf(x)));

        XYChart distanceChart = new XYChartBuilder()
                .width(600)
                .height(400)
                .title("Distance Through Generations")
                .xAxisTitle("Generations")
                .yAxisTitle("Distance")
                .build();

        distanceChart.addSeries(TSPFileReader.fileName,generations,distanceTraveledThroughGenerations).setMarker(new None());

        try{
            BitmapEncoder.saveBitmap(distanceChart, "./distanceChart",BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
