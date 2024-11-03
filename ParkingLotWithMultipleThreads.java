import java.util.*;  
import java.util.List;  
import java.util.concurrent.locks.Lock;  
import java.util.concurrent.locks.ReentrantLock; 

enum VehicleType{
    CAR,BIKE,TRUCK
}

class Capacity {
    int bike, car, truck;
    public Capacity(int bike, int car, int truck)
    {
        this.bike = bike;
        this.car = car;
        this.truck = truck;
    }
}

class ParkingCapacity {
    int bike, car, truck;
    public ParkingCapacity(Capacity cap)
    {
        this.bike = cap.bike;
        this.car = cap.car;
        this.truck = cap.truck;
    }
}

class ParkingOccupied {
    int bike, car, truck;
    public ParkingOccupied()
    {
        this.bike = 0;
        this.car = 0;
        this.truck = 0;
    }
}

class Level {
    ParkingCapacity capacity;
    ParkingOccupied occ;
    final Lock lock = new ReentrantLock();
    public Level(Capacity cap)
    {
        capacity = new ParkingCapacity(cap);
        occ = new ParkingOccupied();
    }
    
    boolean parkingAvailability(VehicleType type)
    {
        try
        {
            lock.lock();
            switch(type){
                case VehicleType.CAR:
                    return capacity.car > occ.car;
                case VehicleType.BIKE:
                    return capacity.bike > occ.bike;
                case VehicleType.TRUCK:
                    return capacity.truck > occ.truck;
                default:
                    return false;
            }
        } finally{
            lock.unlock();
        }
    }
    
    boolean parkVehicle(VehicleType type)
    {
        try
        {
            lock.lock();
            switch(type){
                case VehicleType.CAR:
                    occ.car++;
                   return true;
                case VehicleType.BIKE:
                     occ.bike++;
                     return true;
                case VehicleType.TRUCK:
                     occ.truck++;
                     return true;
                default:
                    return false;
            }
        } finally{
            lock.unlock();
        }
    }
    boolean unParkVehicle(VehicleType type)
    {
        
        try
        {
            lock.lock();
            switch(type){
                case VehicleType.CAR:
                    occ.car--;
                    return true;
                case VehicleType.BIKE:
                    occ.bike--;
                    return true;
                case VehicleType.TRUCK:
                    occ.truck--;
                    return true;
                default:
                    return false;
            }
        } finally {
            lock.unlock();
        }
    }
}

class ParkingSystem {
    
    private List<Level> parkingLevel;
    public List<Level> getLevels()
    {
        return parkingLevel;
    }
    public ParkingSystem()
    {
        parkingLevel = new ArrayList<>();
    }
    
    public void addLevel(Capacity capacity)
    {
        Level level = new Level(capacity);
        parkingLevel.add(level);
    }
    
    public boolean parkVehicle(VehicleType type)
    {
        for(Level level : parkingLevel)
        {
            if(level.parkingAvailability(type))
            {
                level.parkVehicle(type);
                System.out.println("Prking done ");
                return true;
            }
        }
        System.out.println("Prking not available ");
        return false;
    }
    
    public boolean unParkVehicle(Level level, VehicleType type)
    {
        return level.unParkVehicle(type);
    }
}

public class MyClass {  
    public static void main(String[] args) {  
        int levels = 2;  
        int carCapacity = 1;  
        int bikeCapacity = 1;  
        int truckCapacity = 8;  
        
        ParkingSystem parkingSystem = new ParkingSystem();  
        Capacity capacity = new Capacity(carCapacity, bikeCapacity, truckCapacity);  
  
        for (int i = 0; i < levels; i++) {  
            parkingSystem.addLevel(capacity);  
        }  
        Runnable car = ()-> {
            System.out.println("Car Arrived for parking"); 
            parkingSystem.parkVehicle(VehicleType.CAR);  
        };
        Runnable bike = ()->{
            System.out.println("Bike Arrived for parking");  
            parkingSystem.parkVehicle(VehicleType.BIKE);  
        };
        Runnable truck = ()->{
            System.out.println("Truck Arrived for parking");  
         parkingSystem.parkVehicle(VehicleType.TRUCK);  
        };
        Thread t1 = new Thread(car);
        t1.start();
        System.out.println("Car Arrived for parking");  
        parkingSystem.parkVehicle(VehicleType.CAR);  

        System.out.println("Car Arrived for parking");  
        parkingSystem.parkVehicle(VehicleType.CAR);  
        System.out.println("Bike Arrived for parking");  
        parkingSystem.parkVehicle(VehicleType.BIKE);  
        System.out.println("Bike exit from parking");  
        parkingSystem.unParkVehicle(parkingSystem.getLevels().get(0), VehicleType.BIKE);  
        System.out.println("Bike Arrived for parking");  
        parkingSystem.parkVehicle(VehicleType.BIKE);  
    }  
}  
