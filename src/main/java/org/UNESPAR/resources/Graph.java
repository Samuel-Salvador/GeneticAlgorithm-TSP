package org.UNESPAR.resources;

import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.view.mxGraph;
import org.UNESPAR.entities.City;
import org.UNESPAR.entities.Population;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Graph {

    public static void createGraphFile() {
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        Object originCity = graph.insertVertex(
                parent,
                Population.getOriginCity().getName(),
                Population.getOriginCity().getName(),
                Population.getOriginCity().getCordX()*100,
                Population.getOriginCity().getCordY()*100,
                15,
                15);

        try {
            for(int i=0 ; i<Population.selectSolution().getCitySequence().size() ; i++){
                City currCity = Population.selectSolution().getCitySequence().get(i);

                Object city1 = graph.insertVertex(
                        parent,
                        currCity.getName(),
                        currCity.getName(),
                        currCity.getCordX()*100,
                        currCity.getCordY()*100,
                        15,
                        15);

                City currCity2 = null;
                Object city2 = null;
                try{
                    currCity2 = Population.selectSolution().getCitySequence().get(i+1);
                    city2 = graph.insertVertex(
                            parent,
                            currCity2.getName(),
                            currCity2.getName(),
                            currCity2.getCordX()*100,
                            currCity2.getCordY()*100,
                            15,
                            15);

                }catch (IndexOutOfBoundsException e){

                    break;
                }finally {
                    double distanceTraveled = getDistanceTraveled(currCity2, currCity);
                    BigDecimal bd = new BigDecimal(distanceTraveled);
                    bd = bd.setScale(2, RoundingMode.HALF_UP);

                    graph.insertEdge(parent, null, bd, city1, city2);
                }
            }

        } finally {
            graph.getModel().endUpdate();
        }

        BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 1, Color.WHITE, true, null);

        File outputfile = new File("graph.jpeg");
        try {
            ImageIO.write(image, "jpeg", outputfile);
        }catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Image exported as graph.jpeg");
    }

    private static double getDistanceTraveled(City currCity2, City currCity) {
        double distanceTraveled;
        try{
            distanceTraveled = Math.sqrt(
                    Math.pow(currCity2.getCordX()- currCity.getCordX(),2) +
                            Math.pow(currCity2.getCordY()- currCity.getCordY(),2));
        }catch(NullPointerException e2){
            distanceTraveled = Math.sqrt(
                    Math.pow(Population.getOriginCity().getCordX()- currCity.getCordX(),2) +
                            Math.pow(Population.getOriginCity().getCordY()- currCity.getCordY(),2));
    }
        return distanceTraveled;
    }

}
