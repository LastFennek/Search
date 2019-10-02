package com.company;

import org.w3c.dom.ls.LSOutput;

import java.lang.reflect.Array;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.time.Duration;


public class Main {

    public static void main(String[] args) {
        ArrayList<Flight> test = new ArrayList<>();
        Airport a0 = new Airport("a0");
        Airport a1 = new Airport("a1");
        Airport a2 = new Airport("a2");
        Airport a3 = new Airport("a3");
        Airport a4 = new Airport("a4");
        Airport a5 = new Airport("a5");
        Airport a6 = new Airport("a6");
        Airport a7 = new Airport("a7");
        Airport a8 = new Airport("a8");
        Airport a9 = new Airport("a9");
        Airport a10 = new Airport("a10");
        Airport a11 = new Airport("a11");
        Airport a12 = new Airport("a12");
        test.add(new Flight(a1,a2,Duration.ofHours(1),2));
        test.add(new Flight(a1,a6,Duration.ofHours(5),2));
        test.add(new Flight(a6,a4,Duration.ofHours(5),2));
        test.add(new Flight(a3,a4,Duration.ofHours(5),2));
        test.add(new Flight(a5,a6,Duration.ofHours(5),2));
        test.add(new Flight(a4,a5,Duration.ofHours(5),2));
        test.add(new Flight(a4,a0,Duration.ofHours(5),2));
        test.add(new Flight(a0,a2,Duration.ofHours(5),2));
        test.add(new Flight(a7,a2,Duration.ofHours(5),2));
        test.add(new Flight(a11,a7,Duration.ofHours(5),2));
        test.add(new Flight(a12,a9,Duration.ofHours(5),2));
        test.add(new Flight(a9,a10,Duration.ofHours(5),2));
        test.add(new Flight(a10,a11,Duration.ofHours(5),2));
        test.add(new Flight(a11,a9,Duration.ofHours(5),2));
        test.add(new Flight(a2,a1,Duration.ofHours(5),2));
        test.add(new Flight(a2,a0,Duration.ofHours(5),2));
        test.add(new Flight(a0,a4,Duration.ofHours(5),2));
        test.add(new Flight(a12,a5,Duration.ofHours(250),2));
        test.add(new Flight(a7,a0,Duration.ofHours(1),2));

        //test.add(new Flight(a,d,Duration.ofHours(2),2));
        Main main = new Main(test);

        main.getShortestFlightByRoute(a12,a5);
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

        //worst path finding ever

        HashMap<String,Integer> temp = new HashMap<>();
        HashMap<String, Flight> reversTempFlight = new HashMap<>();
        HashMap<Integer, String> reversTemp = new HashMap<>();

        int counter = 0;
        for(Flight x : availableFlights){
            if(!temp.containsKey(x.getDestination().getName())){
                temp.put(x.getDestination().getName(),counter);
                reversTemp.put(counter,x.getDestination().getName());
                counter++;
            }
            if(!temp.containsKey(x.getOrigin().getName())){
                temp.put(x.getOrigin().getName(),counter);
                reversTemp.put(counter,x.getOrigin().getName());
                counter++;
            }
            reversTempFlight.put(x.getOrigin().getName()+x.getDestination().getName(),x);
        }

        int[][] arr = new int[temp.size()][temp.size()];

        for(Flight x : availableFlights){
            arr[temp.get(x.getOrigin().getName())][temp.get(x.getDestination().getName())] = (int)x.getDuration().toMinutes();
        }


        Object[] way = findWay(arr,temp.get(origin.getName()),temp.get(destination.getName()));
        System.out.println(way[0]);
        if(way[1] == null){
            return new ArrayList<Flight>();
        }
        ArrayList<Integer> flightListString = (ArrayList<Integer>) way[1];
        Collections.reverse(flightListString);

        //System.out.println(flightListString);
        System.out.println(way[1]+"RAW");

        ArrayList<Integer> newList = new ArrayList<>();

        //newList.add(temp.get(origin.getName()));
        for (int x = 0; x < flightListString.size();x++){
            newList.add(flightListString.get(x));
            newList.add(flightListString.get(x));
        }
        newList.remove(newList.size()-1);
        newList.remove(0);
        System.out.println(newList + "!");

        ArrayList<Flight> answer = new ArrayList<>();
        for(int y = 0; y < newList.size()/2;y++){
            answer.add(reversTempFlight.get(reversTemp.get(newList.get(y*2))+reversTemp.get(newList.get(y*2+1))));
        }
        System.out.println(answer);

        return answer;
    }

    private Object[] findWay(int[][] arr, int origin, int dest){
        return findWay(origin,dest,0,arr,new HashSet<>(), new ArrayList<Integer>());
    }

    private Object[] findWay(int self, int dest, int way, int[][] arr, HashSet<Integer> soFar, ArrayList<Integer> path){
        int thisWay = Integer.MAX_VALUE;
        if(self == dest){
            path = new ArrayList<>();
            path.add(self);
            return new Object[]{way,path};
        }else{
            for(int col = 0; col < arr[self].length; col++){
                if(arr[self][col] > 0){
                    if(!soFar.contains(col)){
                        soFar.add(self);
                        Object[] ret = findWay(col,dest,way + arr[self][col],arr, soFar, path);
                        soFar.remove(self);
                        if(thisWay > (int)ret[0] && (int)ret[0] > 0){
                            thisWay = (int)ret[0];
                            path = (ArrayList<Integer>) ret[1];
                            path.add(self);
                        }
                    }
                }
            }
            return new Object[]{thisWay,path};
        }
    }
}

