package com.company;

import java.time.Duration;

public class Flight {
    private Airport origin;
    private Airport destination;
    private Duration duration;
    private Integer availableSeats;


    public Flight(Airport origin, Airport destination, Duration duration, Integer availableSeats) {
        this.origin = origin;
        this.destination = destination;
        this.duration = duration;
        this.availableSeats = availableSeats;
    }

    public Airport getOrigin() {
        return origin;
    }

    public Airport getDestination() {
        return destination;
    }

    public Duration getDuration() {
        return duration;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public String toString() {
        return this.getOrigin().getName() + " -> " + this.getDestination().getName();
    }

    public void setAvailableSeats(int number) {
        this.availableSeats = number;
    }
}
