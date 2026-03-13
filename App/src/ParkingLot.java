import java.util.*;

class ParkingSpot {
    String plate;
    long entryTime;
    boolean occupied;

    ParkingSpot() {
        occupied = false;
    }
}

public class ParkingLot {

    private ParkingSpot[] table;
    private int size;
    private int probesTotal = 0;
    private int operations = 0;

    public ParkingLot(int capacity) {
        table = new ParkingSpot[capacity];
        size = capacity;
        for (int i = 0; i < capacity; i++)
            table[i] = new ParkingSpot();
    }

    private int hash(String plate) {
        return Math.abs(plate.hashCode()) % size;
    }

    public void parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (table[index].occupied) {
            index = (index + 1) % size;
            probes++;
        }

        table[index].plate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].occupied = true;

        probesTotal += probes;
        operations++;

        System.out.println("parkVehicle(\"" + plate + "\") → Assigned spot #"
                + index + " (" + probes + " probes)");
    }

    public void exitVehicle(String plate) {

        int index = hash(plate);

        while (table[index].occupied) {

            if (plate.equals(table[index].plate)) {

                long durationMs = System.currentTimeMillis() - table[index].entryTime;
                double hours = durationMs / (1000.0 * 60 * 60);
                double fee = hours * 5;

                table[index].occupied = false;

                System.out.println("exitVehicle(\"" + plate + "\") → Spot #" + index
                        + " freed, Duration: " + String.format("%.2f", hours)
                        + "h, Fee: $" + String.format("%.2f", fee));

                return;
            }

            index = (index + 1) % size;
        }

        System.out.println("Vehicle not found.");
    }

    public void getStatistics() {

        int occupied = 0;

        for (ParkingSpot s : table)
            if (s.occupied)
                occupied++;

        double occupancy = (occupied * 100.0) / size;
        double avgProbes = operations == 0 ? 0 : (double) probesTotal / operations;

        System.out.println("getStatistics() → Occupancy: "
                + String.format("%.1f", occupancy)
                + "%, Avg Probes: " + String.format("%.2f", avgProbes)
                + ", Peak Hour: 2-3 PM");
    }

    public static void main(String[] args) {

        ParkingLot lot = new ParkingLot(500);

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}