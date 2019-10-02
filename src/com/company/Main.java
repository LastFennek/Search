package com.company;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import java.time.Duration;


public class Main {

    public static void main(String[] args) {
        ArrayList<Flight> test = new ArrayList<>();
        Airport a = new Airport("a");
        Airport b = new Airport("b");
        Airport c = new Airport("c");
        Airport d = new Airport("d");
        Airport e = new Airport("e");
        Airport f = new Airport("f");
        Airport g = new Airport("g");
        test.add(new Flight(a,b,Duration.ofHours(5),2));
        test.add(new Flight(c,b,Duration.ofHours(4),2));
        test.add(new Flight(b,c,Duration.ofHours(4),2));
        test.add(new Flight(b,e,Duration.ofHours(2),2));
        test.add(new Flight(e,f,Duration.ofHours(1),2));
        test.add(new Flight(f,g,Duration.ofHours(10),2));
        test.add(new Flight(g,d,Duration.ofHours(12),2));
        Main main = new Main(test);

        main.getShortestFlightByRoute(a,c);
    }

    private Collection<Flight> availableFlights;

    public static Main of(Collection<Flight> availableFlights) {
        return new Main(availableFlights);
    }

    private Main(Collection<Flight> availableFlights) {
        this.availableFlights = availableFlights;
    }

    public Stream<Flight> getFullFlights() {
        return this.availableFlights.stream().filter(x -> x.getAvailableSeats() == 0);
    }

    public Stream<Flight> getFlightsForDestination(Airport destination) {
        return this.availableFlights.stream().filter(x -> x.getDestination().equals(destination));
    }

    public Stream<Flight> getFlightsForOrigin(Airport origin) {
        return this.availableFlights.stream().filter(x -> x.getOrigin().equals(origin));
    }

    public List<Flight> getShortestFlightByRoute(Airport origin, Airport destination) {

        List list = new ArrayList<>();

        //worst path finding ever

        HashMap<String,Integer> temp = new HashMap<>();

        int counter = 0;
        for(Flight x : availableFlights){
            if(!temp.containsKey(x.getDestination().getName())){
                temp.put(x.getDestination().getName(),counter);
                counter++;
            }
            if(!temp.containsKey(x.getOrigin().getName())){
                temp.put(x.getOrigin().getName(),counter);
                counter++;
            }
        }

        int[][] arr = new int[temp.size()][temp.size()];

        for(Flight x : availableFlights){
            arr[temp.get(x.getOrigin().getName())][temp.get(x.getDestination().getName())] = (int)x.getDuration().toMinutes();
        }


        for(int[] x : arr){
            for(int y : x){
                System.out.print(y+"\t");
            }
            System.out.println("\n");
        }

        System.out.println(findWay(arr,1,6));



        //-------------------------------------------

        return list;
    }

    private int findWay(int[][] arr, int origin, int dest){
        return findWay(origin,dest,0,arr,new HashSet<>());
    }

    private int findWay(int self, int dest, int way, int[][] arr, HashSet<Integer> soFar){
        int thisWay = Integer.MAX_VALUE;
        soFar.add(self);
        if(self == dest){
            return way;
        }else{
            for(int col = 0; col < arr[self].length; col++){
                if(arr[self][col] > 0){
                    if(!soFar.contains(col)){
                        int ret = findWay(col,dest,way + arr[self][col],arr, soFar);
                        if(thisWay > ret){
                            thisWay = ret;
                        }
                    }
                }
            }
            return thisWay;
        }
    }


}

