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
        Airport a = new Airport("a");
        Airport b = new Airport("b");
        Airport c = new Airport("c");
        Airport d = new Airport("d");
        Airport e = new Airport("e");
        Airport f = new Airport("f");
        Airport g = new Airport("g");
        test.add(new Flight(a,b,Duration.ofHours(5),2));
        test.add(new Flight(c,b,Duration.ofHours(7),2));
        test.add(new Flight(b,c,Duration.ofHours(4),2));
        test.add(new Flight(b,e,Duration.ofHours(2),2));
        test.add(new Flight(e,f,Duration.ofHours(1),2));
        test.add(new Flight(f,g,Duration.ofHours(10),2));
        test.add(new Flight(g,d,Duration.ofHours(12),2));
        test.add(new Flight(a,d,Duration.ofHours(2),2));
        Main main = new Main(test);

        main.getShortestFlightByRoute(a,d);
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
        ArrayList<Integer> flightListString = (ArrayList<Integer>) way[1];
        Collections.reverse(flightListString);

        System.out.println(flightListString);
        System.out.println(way[0]);

        ArrayList<Integer> newList = new ArrayList<>();

        newList.add(temp.get(origin.getName()));
        for (int x = 0; x < flightListString.size();x++){
            newList.add(flightListString.get(x));
            newList.add(flightListString.get(x));
        }
        newList.remove(newList.size()-1);
        System.out.println(newList);

        ArrayList<Flight> answer = new ArrayList<>();
        for(int y = 0; y < newList.size()/2;y++){
            answer.add(reversTempFlight.get(reversTemp.get(newList.get(y*2))+reversTemp.get(newList.get(y*2+1))));
        }
        System.out.println(answer);

        return answer;
    }

    private Object[] findWay(int[][] arr, int origin, int dest){
        return findWay(origin,dest,0,arr,new HashSet<>(), new ArrayList<Integer>(), new HashMap<>());
    }

    private Object[] findWay(int self, int dest, int way, int[][] arr, HashSet<Integer> soFar, ArrayList<Integer> path, HashMap<Integer,Object[]> known){
        ArrayList<Integer> tempPath = new ArrayList<>();
        int thisWay = Integer.MAX_VALUE;
        if(self == dest){
            tempPath.add(self);
            return new Object[]{way,path,tempPath};
        }else if(known.containsKey(self)){
            return new Object[]{way + (int)known.get(self)[0],path,tempPath};
        }else{
            for(int col = 0; col < arr[self].length; col++){
                if(arr[self][col] > 0){
                    if(!soFar.contains(col)){
                        soFar.add(self);
                        Object[] ret = findWay(col,dest,way + arr[self][col],arr, soFar, path, known);
                        soFar.remove(self);
                        if(thisWay > (int)ret[0] && (int)ret[0] > 0){
                            path.addAll((ArrayList)ret[2]);
                            thisWay = (int)ret[0];
                        }
                    }
                }
            }
            tempPath.add(self);
            known.put(self,new Object[]{thisWay,tempPath});
            return new Object[]{thisWay,path,tempPath};
        }
    }


}

