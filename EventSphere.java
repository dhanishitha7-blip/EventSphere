import java.util.*;
import java.nio.file.*;
import java.io.*;

// MODULE 4 : INTERFACE
interface Registrable {
    boolean registerParticipant(Participant participant, Event event);
}

// MODULE 4 : PARENT CLASS
class Person {
    private int id;
    private String name;

    Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    int getId() {
        return id;
    }

    String getName() {
        return name;
    }
}

// MODULE 4 : CHILD CLASS
class Participant extends Person implements Registrable, Comparable<Participant> {
    private String email;
    private String category;
    private double payment;
    private boolean registered;

    Participant(int id, String name, String email, String category, double payment) {
        super(id, name);
        this.email = email;
        this.category = category;
        this.payment = payment;
        this.registered = false;
    }

    String getEmail() {
        return email;
    }

    String getCategory() {
        return category;
    }

    double getPayment() {
        return payment;
    }

    boolean isRegistered() {
        return registered;
    }

    // MODULE 2 : if-else-if
    String getEligibilityStatus() {
        if (category.equalsIgnoreCase("VIP")) {
            return "Priority Eligible";
        } else if (category.equalsIgnoreCase("Speaker")) {
            return "Conference Speaker";
        } else if (category.equalsIgnoreCase("Organizer")) {
            return "Event Staff";
        } else if (category.equalsIgnoreCase("Student")) {
            return "Student Discount Eligible";
        } else {
            return "General Admission";
        }
    }

    // MODULE 4 : Method Overriding / Interface implementation
    public boolean registerParticipant(Participant participant, Event event) {
        if (participant == null || event == null) {
            return false;
        }
        if (event.getSeatsAvailable() <= 0) {
            return false;
        }

        event.addParticipant(participant);
        participant.registered = true;
        participant.payment = event.getFee();
        return true;
    }

    // MODULE 5 : toString()
    public String toString() {
        return getId() + ","
                + getName() + ","
                + email + ","
                + category + ","
                + payment;
    }

    // MODULE 5 : Comparable
    public int compareTo(Participant p) {
        return Double.compare(this.payment, p.payment);
    }
}

class Organizer extends Person {
    private String organization;

    Organizer(int id, String name, String organization) {
        super(id, name);
        this.organization = organization;
    }

    String getOrganization() {
        return organization;
    }
}

class Speaker extends Person {
    private String expertise;

    Speaker(int id, String name, String expertise) {
        super(id, name);
        this.expertise = expertise;
    }

    String getExpertise() {
        return expertise;
    }
}

class Venue {
    private String name;
    private String city;
    private int capacity;
    private boolean hasProjector;

    Venue(String name, String city, int capacity, boolean hasProjector) {
        this.name = name;
        this.city = city;
        this.capacity = capacity;
        this.hasProjector = hasProjector;
    }

    String getName() {
        return name;
    }

    String getCity() {
        return city;
    }

    int getCapacity() {
        return capacity;
    }

    boolean isHasProjector() {
        return hasProjector;
    }

    public String toString() {
        return name + "," + city + "," + capacity;
    }
}

class Event {
    private int id;
    private String name;
    private String category;
    private String date;
    private double fee;
    private Venue venue;
    private int totalSeats;
    private int seatsBooked;
    private ArrayList<Participant> registrants = new ArrayList<>();

    Event(int id, String name, String category, String date, double fee, Venue venue, int totalSeats) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.date = date;
        this.fee = fee;
        this.venue = venue;
        this.totalSeats = totalSeats;
        this.seatsBooked = 0;
    }

    int getId() {
        return id;
    }

    String getName() {
        return name;
    }

    String getCategory() {
        return category;
    }

    String getDate() {
        return date;
    }

    double getFee() {
        return fee;
    }

    Venue getVenue() {
        return venue;
    }

    int getSeatsAvailable() {
        return totalSeats - seatsBooked;
    }

    void addParticipant(Participant participant) {
        if (getSeatsAvailable() > 0) {
            registrants.add(participant);
            seatsBooked++;
        }
    }

    ArrayList<Participant> getRegistrants() {
        return registrants;
    }

    public String toString() {
        return id + ","
                + name + ","
                + category + ","
                + date + ","
                + fee + ","
                + venue.getName();
    }
}

class OnlineEvent extends Event {
    private String platform;

    OnlineEvent(int id, String name, String date, double fee, String platform, int totalSeats) {
        super(id, name, "Online", date, fee, new Venue("Virtual Platform", "Remote", totalSeats, true), totalSeats);
        this.platform = platform;
    }

    String getPlatform() {
        return platform;
    }
}

class OfflineEvent extends Event {
    private String venueLocation;

    OfflineEvent(int id, String name, String category, String date, double fee, Venue venue, int totalSeats) {
        super(id, name, category, date, fee, venue, totalSeats);
        this.venueLocation = venue.getCity();
    }

    String getVenueLocation() {
        return venueLocation;
    }
}

// MAIN CLASS
public class EventSphere {
    // MODULE 6 : ArrayList
    static ArrayList<Participant> participants = new ArrayList<>();
    static ArrayList<Event> events = new ArrayList<>();

    // MODULE 3 : METHOD
    static double calculateRegistrationFee(double baseFee, int eventCount) {
        double discount = 0;
        if (eventCount >= 2) {
            discount = 0.10;
        }
        return baseFee - (baseFee * discount);
    }

    // MODULE 3 : SEARCHING
    static Participant searchParticipant(int id) {
        for (Participant p : participants) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    static Event searchEvent(String eventName) {
        for (Event e : events) {
            if (e.getName().equalsIgnoreCase(eventName)) {
                return e;
            }
        }
        return null;
    }

    // MODULE 5 : SAVE TO FILE
    static void saveToFile() {
        Path file = Paths.get("event_records.txt");
        try {
            ArrayList<String> records = new ArrayList<>();
            for (Participant p : participants) {
                records.add("PARTICIPANT," + p.toString());
            }
            for (Event e : events) {
                records.add("EVENT," + e.toString());
            }
            Files.write(file, records);
            System.out.println("Event data saved to file.");
        } catch (IOException e) {
            System.out.println("File error.");
        }
    }

    // MODULE 5 : READ FROM FILE
    static void readFromFile() {
        Path file = Paths.get("event_records.txt");
        try {
            List<String> records = Files.readAllLines(file);
            System.out.println("\nRecords in File:");
            for (String record : records) {
                System.out.println(record);
            }
        } catch (IOException e) {
            System.out.println("File not found.");
        }
    }

    // MODULE 6 : STREAM
    static void streamReport() {
        System.out.println("\nParticipants with payments > 500:");
        participants.stream()
                .filter(p -> p.getPayment() > 500)
                .forEach(System.out::println);

        System.out.println("\nParticipant Names:");
        participants.stream()
                .map(Person::getName)
                .forEach(System.out::println);

        long registeredCount = participants.stream()
                .filter(Participant::isRegistered)
                .count();
        System.out.println("\nRegistered Participants = " + registeredCount);
    }

    // MAIN METHOD
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int choice;

        // MODULE 1 : BASIC DATA
        System.out.println("===== EVENTSPHERE =====");

        // Sample events
        Venue techHub = new Venue("Tech Hub", "Colombo", 200, true);
        Venue expoArena = new Venue("Expo Arena", "Kandy", 300, true);
        events.add(new OfflineEvent(101, "AI Summit", "Technology", "2026-11-10", 2500.0, techHub, 200));
        events.add(new OnlineEvent(102, "Startup Webinar", "2026-11-15", 1200.0, "Zoom", 500));
        events.add(new OfflineEvent(103, "Cultural Festival", "Festival", "2026-11-20", 1800.0, expoArena, 300));

        // MODULE 2 : MENU
        do {
            System.out.println("\n===== EVENTSPHERE MENU =====");
            System.out.println("1. Register Participant");
            System.out.println("2. Display Events");
            System.out.println("3. Search Participant");
            System.out.println("4. Sort Participants");
            System.out.println("5. Calculate Registration Fee");
            System.out.println("6. Save to File");
            System.out.println("7. Read from File");
            System.out.println("8. Stream Report");
            System.out.println("9. Exit");
            System.out.print("Enter choice: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Participant ID: ");
                    int id = input.nextInt();
                    input.nextLine();
                    System.out.print("Enter Participant Name: ");
                    String name = input.nextLine();
                    System.out.print("Enter Email: ");
                    String email = input.nextLine();
                    System.out.print("Enter Category (VIP/Student/Speaker/Organizer): ");
                    String category = input.nextLine();
                    System.out.print("Enter Event Name to Register: ");
                    String eventName = input.nextLine();

                    Event selectedEvent = searchEvent(eventName);
                    Participant participant = new Participant(id, name, email, category, 0.0);

                    if (selectedEvent != null) {
                        participant.registerParticipant(participant, selectedEvent);
                        participants.add(participant);
                        System.out.println("Participant registered successfully.");
                        System.out.println("Eligibility: " + participant.getEligibilityStatus());
                    } else {
                        System.out.println("Event not found.");
                    }
                    break;

                case 2:
                    System.out.println("\nID  Event Name  Category  Date  Fee  Venue");
                    for (Event e : events) {
                        System.out.println(e);
                    }
                    break;

                case 3:
                    System.out.print("Enter participant ID to search: ");
                    int searchId = input.nextInt();
                    Participant found = searchParticipant(searchId);
                    if (found != null) {
                        System.out.println("Participant found: " + found);
                    } else {
                        System.out.println("Participant not found.");
                    }
                    break;

                case 4:
                    Collections.sort(participants);
                    System.out.println("\nParticipants sorted by payment:");
                    for (Participant p : participants) {
                        System.out.println(p);
                    }
                    break;

                case 5:
                    System.out.print("Enter base fee: ");
                    double baseFee = input.nextDouble();
                    System.out.print("Enter number of events attended: ");
                    int eventCount = input.nextInt();
                    double finalFee = calculateRegistrationFee(baseFee, eventCount);
                    System.out.println("Calculated Registration Fee = " + finalFee);
                    break;

                case 6:
                    saveToFile();
                    break;

                case 7:
                    readFromFile();
                    break;

                case 8:
                    streamReport();
                    break;

                case 9:
                    System.out.println("Thank you for using EventSphere.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 9);

        input.close();
    }
}
