package org.UNESPAR.resources;

import org.UNESPAR.entities.City;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TSPFileReader {

    public static String fileName = "burma14.tsp";

    public static List<City> getCitiesFromFile(String fileName) {
        List<City> cities = new ArrayList<>();

        try{
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            String lineData = scanner.nextLine();

            while(!lineData.equals("NODE_COORD_SECTION")){
                lineData = scanner.nextLine();
            }

            while(scanner.hasNextLine()) {
                lineData = scanner.nextLine();
                if(!lineData.equals("EOF")){
                    String[] cityProperties = lineData.trim().split("\\s+");

                    City newCity = new City(cityProperties[0], Double.parseDouble(cityProperties[1]) ,Double.parseDouble(cityProperties[2]));

                    cities.add(newCity);
                }else{
                    break;
                }
            }
        }catch(FileNotFoundException e){
            System.out.println("File not found");
        }
        return cities;
    }
}
