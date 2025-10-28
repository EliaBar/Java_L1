import java.util.*;

class Human {
    private final String name;

    public Human(String name) { this.name = name; }

    public String getName() { return name; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + name;
    }
}

class Firefighter extends Human {
    public Firefighter(String name) { super(name); }
}

class Policeman extends Human {
    public Policeman(String name) { super(name); }
}

abstract class Vehicle<T extends Human> {
    protected int capacity;
    protected List<T> passengers = new ArrayList<>();

    public Vehicle(int capacity) { this.capacity = capacity; }

    public int getMaxSeats() { return capacity; }

    public int getOccupiedSeats() { return passengers.size(); }

    public void boardPassenger(T passenger) throws Exception {
        if (passengers.size() >= capacity)
            throw new Exception("No free seats available!");
        passengers.add(passenger);
    }

    public void removePassenger(T passenger) throws Exception {
        if (!passengers.contains(passenger))
            throw new Exception("Passenger not found in vehicle!");
        passengers.remove(passenger);
    }

    public List<T> getPassengers() { return passengers; }
}

class Bus extends Vehicle<Human> {
    public Bus(int capacity) { super(capacity); }
}

class Taxi extends Vehicle<Human> {
    public Taxi(int capacity) { super(capacity); }
}

class FireTruck extends Vehicle<Firefighter> {
    public FireTruck(int capacity) { super(capacity); }
}

class PoliceCar extends Vehicle<Policeman> {
    public PoliceCar(int capacity) { super(capacity); }
}

class Road {
    private final List<Vehicle<? extends Human>> carsInRoad = new ArrayList<>();

    public void addCarToRoad(Vehicle<? extends Human> vehicle) {
        carsInRoad.add(vehicle);
    }

    public int getCountOfHumans() {
        int count = 0;
        for (Vehicle<? extends Human> v : carsInRoad)
            count += v.getOccupiedSeats();
        return count;
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        Road road = new Road();

        Bus bus = new Bus(3);
        bus.boardPassenger(new Human("Anna"));
        bus.boardPassenger(new Policeman("Bob"));

        Taxi taxi = new Taxi(2);
        taxi.boardPassenger(new Human("Tom"));

        FireTruck fireTruck = new FireTruck(2);
        fireTruck.boardPassenger(new Firefighter("John"));

        PoliceCar policeCar = new PoliceCar(1);
        policeCar.boardPassenger(new Policeman("Max"));

        road.addCarToRoad(bus);
        road.addCarToRoad(taxi);
        road.addCarToRoad(fireTruck);
        road.addCarToRoad(policeCar);

        System.out.println("Total people on the road: " + road.getCountOfHumans());
    }
}
