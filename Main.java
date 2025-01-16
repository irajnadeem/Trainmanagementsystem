import java.io.*;
import java.util.*;

// Abstract Class (Base for all train types)
abstract class Train {
    private String trainName;
    private String route;
    private String schedule;
    private double price;

    public Train(String trainName, String route, String schedule, double price) {
        this.trainName = trainName;
        this.route = route;
        this.schedule = schedule;
        this.price = price;
    }

    public String getRoute() {
        return route;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getSchedule() {
        return schedule;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getDetails(); // Abstract Method
}

// Inherited Class for Standard Train
class StandardTrain extends Train {
    public StandardTrain(String trainName, String route, String schedule, double price) {
        super(trainName, route, schedule, price);
    }

    @Override
    public String getDetails() {
        return "Train Name: " + getTrainName() + ", Route: " + getRoute() + ", Standard Train, Schedule: "
                + getSchedule() + ", Price: $" + getPrice();
    }
}

// Inherited Class for Luxury Train (Inheritance and Polymorphism)
class LuxuryTrain extends Train {
    private double luxuryCharge;

    public LuxuryTrain(String trainName, String route, String schedule, double price, double luxuryCharge) {
        super(trainName, route, schedule, price);
        this.luxuryCharge = luxuryCharge;
    }

    @Override
    public String getDetails() {
        return "Train Name: " + getTrainName() + ", Route: " + getRoute() + ", Luxury Train, Schedule: " + getSchedule()
                + ", Price: $" + (getPrice() + luxuryCharge);
    }
}

// Interface for File Handling
interface FileHandler {
    void saveToFile(String fileName) throws IOException;

    void loadFromFile(String fileName) throws IOException;
}

// Passenger Class
class Passenger {
    private String name;
    private int age;
    private String seatType;

    public Passenger(String name, int age, String seatType) {
        this.name = name;
        this.age = age;
        this.seatType = seatType;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return "Name: " + name + ", Age: " + age + ", Seat Type: " + seatType;
    }
}

// Booking Class (Containership: Contains Passenger and Train)
class Booking {
    private Passenger passenger;
    private Train train;

    public Booking(Passenger passenger, Train train) {
        this.passenger = passenger;
        this.train = train;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public String getDetails() {
        return passenger.getDetails() + ", Train: " + train.getDetails();
    }
}

// Train Management System Class
class TrainManagementSystem implements FileHandler {
    private List<Train> trains = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private Map<String, Integer> seatAvailability = new HashMap<>();
    private static int totalBookings = 0; // Static variable

    // Add Train
    public void addTrain(Train train) {
        trains.add(train);
        seatAvailability.put(train.getRoute(), 100); // Default seat capacity
    }

    // Display All Trains
    public void displayTrains() {
        System.out.println("Available Trains:");
        for (Train train : trains) {
            System.out.println(train.getDetails());
        }
    }

    // Book a Ticket
    public void bookTicket(String name, int age, String seatType, String route) {
        Train train = getTrainByRoute(route);
        if (train != null && seatAvailability.get(route) > 0) {
            Passenger passenger = new Passenger(name, age, seatType);
            bookings.add(new Booking(passenger, train));
            seatAvailability.put(route, seatAvailability.get(route) - 1);
            totalBookings++;
            System.out.println("Ticket booked successfully!");
        } else {
            System.out.println("No seats available for this route.");
        }
    }

    // Get Train by Route
    private Train getTrainByRoute(String route) {
        for (Train train : trains) {
            if (train.getRoute().equalsIgnoreCase(route)) {
                return train;
            }
        }
        return null;
    }

    // Cancel a Ticket
    public void cancelTicket(String name, String route) {
        Iterator<Booking> iterator = bookings.iterator();
        while (iterator.hasNext()) {
            Booking booking = iterator.next();
            if (booking.getPassenger().getName().equalsIgnoreCase(name) &&
                    booking.getDetails().contains(route)) {
                iterator.remove();
                seatAvailability.put(route, seatAvailability.get(route) + 1);
                System.out.println("Ticket canceled successfully!");
                return;
            }
        }
        System.out.println("No booking found for the given details.");
    }

    // Display Bookings
    public void displayBookings() {
        System.out.println("Current Bookings:");
        for (Booking booking : bookings) {
            System.out.println(booking.getDetails());
        }
    }

    // Display Available Seats
    public void displayAvailableSeats(String route) {
        if (seatAvailability.containsKey(route)) {
            System.out.println("Available Seats for " + route + ": " + seatAvailability.get(route));
        } else {
            System.out.println("No train found for this route.");
        }
    }

    // Static Method to Display Total Bookings
    public static void displayTotalBookings() {
        System.out.println("Total Bookings: " + totalBookings);
    }

    // Save Bookings to File
    @Override
    public void saveToFile(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Booking booking : bookings) {
                writer.write(booking.getDetails());
                writer.newLine();
            }
            System.out.println("Bookings saved to file successfully.");
        }
    }

    // Load Bookings from File
    @Override
    public void loadFromFile(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}

// Main Class
class Main {
    public static void main(String[] args) {
        TrainManagementSystem system = new TrainManagementSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nTrain Management System:");
            System.out.println("1. Add Train");
            System.out.println("2. Display Trains");
            System.out.println("3. Book Ticket");
            System.out.println("4. Cancel Ticket");
            System.out.println("5. Display Bookings");
            System.out.println("6. Display Available Seats");
            System.out.println("7. Save Bookings to File");
            System.out.println("8. Load Bookings from File");
            System.out.println("9. Display Total Bookings");
            System.out.println("10. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Add Train
                    System.out.print("Enter Train Name: ");
                    String trainName = scanner.nextLine();
                    System.out.print("Enter Route(e.g.,A-B): ");
                    String route = scanner.nextLine();
                    System.out.print("Enter Schedule(e.g.,10:00 AM): ");
                    String schedule = scanner.nextLine();
                    System.out.print("Enter Price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Luxury Train? (yes/no): ");
                    scanner.nextLine(); // Consume newline
                    String luxury = scanner.nextLine();
                    if (luxury.equalsIgnoreCase("yes")) {
                        System.out.print("Enter Luxury Charge: ");
                        double luxuryCharge = scanner.nextDouble();
                        system.addTrain(new LuxuryTrain(trainName, route, schedule, price, luxuryCharge));
                    } else {
                        system.addTrain(new StandardTrain(trainName, route, schedule, price));
                    }
                    System.out.println("Train added successfully!");
                    break;

                case 2:
                    // Display Trains
                    system.displayTrains();
                    break;

                case 3:
                    // Book Ticket
                    System.out.print("Enter Passenger Name: ");
                    String passengerName = scanner.nextLine();
                    System.out.print("Enter Passenger Age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Seat Type(e.g.,Window/Asile): ");
                    String seatType = scanner.nextLine();
                    System.out.print("Enter Route for Booking(e.g.,A-B): ");
                    String bookingRoute = scanner.nextLine();
                    system.bookTicket(passengerName, age, seatType, bookingRoute);
                    break;

                case 4:
                    // Cancel Ticket
                    System.out.print("Enter Passenger Name: ");
                    String cancelName = scanner.nextLine();
                    System.out.print("Enter Route for Cancellation: ");
                    String cancelRoute = scanner.nextLine();
                    system.cancelTicket(cancelName, cancelRoute);
                    break;

                case 5:
                    // Display Bookings
                    system.displayBookings();
                    break;

                case 6:
                    // Display Available Seats
                    System.out.print("Enter Route to Check Seats: ");
                    String seatRoute = scanner.nextLine();
                    system.displayAvailableSeats(seatRoute);
                    break;

                case 7:
                    // Save Bookings to File
                    System.out.print("Enter File Name to Save Bookings: ");
                    String saveFile = scanner.nextLine();
                    try {
                        system.saveToFile(saveFile);
                    } catch (IOException e) {
                        System.out.println("Error saving to file: " + e.getMessage());
                    }
                    break;

                case 8:
                    // Load Bookings from File
                    System.out.print("Enter File Name to Load Bookings: ");
                    String loadFile = scanner.nextLine();
                    try {
                        system.loadFromFile(loadFile);
                    } catch (IOException e) {
                        System.out.println("Error loading from file: " + e.getMessage());
                    }
                    break;

                case 9:
                    // Display Total Bookings
                    TrainManagementSystem.displayTotalBookings();
                    break;

                case 10:
                    // Exit
                    System.out.println("Exiting system. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
