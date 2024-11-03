import java.util.ArrayList;  
import java.util.List;  
  
enum ParkingType {  
    CAR, BIKE, TRUCK  
}  
  
class Capacity {  
    private int car;  
    private int bike;  
    private int truck;  
  
    public Capacity(int car, int bike, int truck) {  
        this.car = car;  
        this.bike = bike;  
        this.truck = truck;  
    }  
  
    public int getCarCapacity() {  
        return car;  
    }  
  
    public int getBikeCapacity() {  
        return bike;  
    }  
  
    public int getTruckCapacity() {  
        return truck;  
    }  
}  
  
class ParkingLotCapacity {  
    private int car;  
    private int bike;  
    private int truck;  
  
    public ParkingLotCapacity(Capacity cap) {  
        this.car = cap.getCarCapacity();  
        this.bike = cap.getBikeCapacity();  
        this.truck = cap.getTruckCapacity();  
    }  
  
    public int getCarCapacity() {  
        return car;  
    }  
  
    public int getBikeCapacity() {  
        return bike;  
    }  
  
    public int getTruckCapacity() {  
        return truck;  
    }  
  
    public void setCapacity(Capacity cap) {  
        this.car = cap.getCarCapacity();  
        this.bike = cap.getBikeCapacity();  
        this.truck = cap.getTruckCapacity();  
    }  
}  
  
class ParkingLotOccupied {  
    private int car;  
    private int bike;  
    private int truck;  
  
    public ParkingLotOccupied() {  
        this.car = 0;  
        this.bike = 0;  
        this.truck = 0;  
    }  
  
    public boolean isAvailable(ParkingLotCapacity capacity, ParkingType type) {  
        switch (type) {  
            case CAR:  
                return car < capacity.getCarCapacity();  
            case BIKE:  
                return bike < capacity.getBikeCapacity();  
            case TRUCK:  
                return truck < capacity.getTruckCapacity();  
            default:  
                throw new IllegalArgumentException("Unknown parking type: " + type);  
        }  
    }  
  
    public void addVehicle(ParkingType type) {  
        switch (type) {  
            case CAR:  
                car++;  
                break;  
            case BIKE:  
                bike++;  
                break;  
            case TRUCK:  
                truck++;  
                break;  
            default:  
                throw new IllegalArgumentException("Unknown parking type: " + type);  
        }  
    }  
  
    public void removeVehicle(ParkingType type) {  
        switch (type) {  
            case CAR:  
                car--;  
                break;  
            case BIKE:  
                bike--;  
                break;  
            case TRUCK:  
                truck--;  
                break;  
            default:  
                throw new IllegalArgumentException("Unknown parking type: " + type);  
        }  
    }  
}  
  
class ParkingLotLevel {  
    private ParkingLotCapacity capacity;  
    private ParkingLotOccupied occupied;  
  
    public ParkingLotLevel(Capacity cap) {  
        this.capacity = new ParkingLotCapacity(cap);  
        this.occupied = new ParkingLotOccupied();  
    }  
  
    public boolean parkVehicle(ParkingType type) {  
        if (occupied.isAvailable(capacity, type)) {  
            occupied.addVehicle(type);  
            return true;  
        }  
        return false;  
    }  
  
    public void removeVehicle(ParkingType type) {  
        occupied.removeVehicle(type);  
    }  
  
    public ParkingLotCapacity getCapacity() {  
        return capacity;  
    }  
  
    public ParkingLotOccupied getOccupied() {  
        return occupied;  
    }  
}  
  
class ParkingLotSystem {  
    private List<ParkingLotLevel> levels;  
  
    public ParkingLotSystem() {  
        this.levels = new ArrayList<>();  
    }  
  
    public void addLevel(Capacity cap) {  
        levels.add(new ParkingLotLevel(cap));  
    }  
  
    public boolean parkVehicle(ParkingType type) {  
        for (ParkingLotLevel level : levels) {  
            if (level.parkVehicle(type)) {  
                System.out.println("Parked " + type.toString().toLowerCase() + " at level " + levels.indexOf(level));  
                return true;  
            }  
        }
        System.out.println("Parking not available for " + type.toString().toLowerCase()); 
        return false;  
    }  
  
    public void removeVehicle(ParkingType type) {  
        for (ParkingLotLevel level : levels) {  
            level.removeVehicle(type);  
        }  
    }  
}  
  
public class MyClass {  
    public static void main(String[] args) {  
        int levels = 2;  
        int carCapacity = 1;  
        int bikeCapacity = 1;  
        int truckCapacity = 8;  
  
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem();  
        Capacity capacity = new Capacity(carCapacity, bikeCapacity, truckCapacity);  
  
        for (int i = 0; i < levels; i++) {  
            parkingLotSystem.addLevel(capacity);  
        }  
        System.out.println("Car Arrived for parking");  
        parkingLotSystem.parkVehicle(ParkingType.CAR);  
        System.out.println("Car Arrived for parking");  
        parkingLotSystem.parkVehicle(ParkingType.CAR);  
        System.out.println("Bike Arrived for parking");  
        parkingLotSystem.parkVehicle(ParkingType.BIKE);  
        System.out.println("Car Arrived for parking");  
        parkingLotSystem.parkVehicle(ParkingType.CAR);  
        System.out.println("Bike Arrived for parking");  
        parkingLotSystem.parkVehicle(ParkingType.BIKE);  
        System.out.println("Bike exit from parking");  
        parkingLotSystem.removeVehicle(ParkingType.BIKE);  
        System.out.println("Bike Arrived for parking");  
        parkingLotSystem.parkVehicle(ParkingType.BIKE);  
    }  
}  
