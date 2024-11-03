import java.io.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
enum VehicleType {
    CAR, BIKE, TRUCK
}
abstract class Vehicle {
    protected String licenseNumber;
    protected VehicleType type;
    public Vehicle(String license, VehicleType type) {
        this.licenseNumber = license;
        this.type = type;
    }
    public VehicleType getType() {
        return type;
    }
}

class Car extends Vehicle {
    public Car(String lisense)
    {
        super(lisense, VehicleType.CAR);
    }
}

class ParkingSpot {
    public int id;
    public VehicleType type;
    public Vehicle parked;
    public ParkingSpot(int id, VehicleType type) {
        this.id = id;
        this.type = type;
    }
    public synchronized boolean isAvailable()
    {
        return parked == null;
    }
    public synchronized void parkVehicle(Vehicle vehicle)
    {
        if(isAvailable() && vehicle.type == type)
        {
            parked = vehicle;
        }
    }
    public synchronized void unparkVehicle()
    {
        parked = null;
    }
    public VehicleType getVehicleType()
    {
        return type;
    }
    public int getSpotNumber()
    {
        return id;
    } 
    public Vehicle getVehicle()
    {
        return parked;
    }
}

class Level {
    private final int floor;
    private final List<ParkingSpot> parkingSpots;
    public Level(int floor, int car, int bike, int truck)
    {
        int i=1;
        parkingSpots = new ArrayList<>();
        this.floor = floor;
        for(i=1; i<car ; i++) {
            ParkingSpot parkingSpot = new ParkingSpot(i, VehicleType.CAR);
            parkingSpots.add(parkingSpot);
        }
        for(; i < car+bike ; i++) {
            ParkingSpot parkingSpot = new ParkingSpot(i, VehicleType.BIKE);
            parkingSpots.add(parkingSpot);
        }
        for(; i < car+bike+truck ; i++) {
            ParkingSpot parkingSpot = new ParkingSpot(i, VehicleType.TRUCK);
            parkingSpots.add(parkingSpot);
        }
    }
    public synchronized boolean parkVehicle(Vehicle vehicle)
    {
        for(ParkingSpot spot : parkingSpots) 
        {
            if(spot.isAvailable() && spot.getVehicleType() == vehicle.getType())
            {
                spot.parkVehicle(vehicle);
                return true;
            }
        }
        return false;
    }
    public synchronized boolean unparkVehicle(Vehicle vehicle)
    {
        for(ParkingSpot spot : parkingSpots) 
        {
            if(!spot.isAvailable() && spot.getVehicle().equals(vehicle))
            {
                spot.unparkVehicle();
                return true;
            }
        }
        return false;
    }
    public void displayAvailability()
    {
        System.out.println("Level : "+ floor+" available spots are :");
        for(ParkingSpot spot : parkingSpots)
        {
            if(spot.isAvailable())
            System.out.println("Type : " + spot.getVehicleType());
        }
    }
}
class ParkingLot {
    private static ParkingLot parkingLot;
    private List<Level> levels;
    private ParkingLot()
    {
        levels = new ArrayList<>();
    }
    public static ParkingLot getInstance()
    {
        if(parkingLot == null)
            parkingLot = new ParkingLot();
        return parkingLot;
    }
    public void addLevel(Level level)
    {
        levels.add(level);
    }
    public boolean parkVehicle(Vehicle vehicle)
    {
        for(Level level : levels)
        {
            if(level.parkVehicle(vehicle))
            {
                System.out.print("Parked vehicle "+ vehicle.licenseNumber);
                return true;
            }
        }
        return false;
    }
    public boolean unparkVehicle(Vehicle vehicle)
    {
        for(Level level : levels)
        {
            if(level.unparkVehicle(vehicle))
            {
                System.out.print("un Parked vehicle "+ vehicle.licenseNumber);
                return true;
            }
        }
        return false;
    }
    public void displayAvailability()
    {
        for(Level level : levels)
        {
            level.displayAvailability();
        }
    }
}

class Solution {
    public static void main(String[] args) {
        ParkingLot parkingLot = ParkingLot.getInstance();
        parkingLot.addLevel(new Level(1, 2, 3, 1));
        parkingLot.addLevel(new Level(2, 2, 3, 1));
        Vehicle car = new Car("ASDF");
        parkingLot.parkVehicle(car);
        parkingLot.displayAvailability();
        
    }
}
