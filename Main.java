import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

class Rooms {
    int rent;
    int spa;
    int laundry;
    int food;
    int total;

    Rooms(int rent, int spa, int laundry, int food) {
        this.rent = rent;
        this.spa = spa;
        this.laundry = laundry;
        this.food = food;
    }

    int calculate(int days) {
        total = days * (rent + spa + laundry + food);
        return total;
    }
}

class RoomInfo {
    String type;
    int days;
    int count;
    int rent;
    int spa;
    int laundry;
    int food;

    RoomInfo(String type, int days, int count, int rent, int spa, int laundry, int food) {
        this.type = type;
        this.days = days;
        this.count = count;
        this.rent = rent;
        this.spa = spa;
        this.laundry = laundry;
        this.food = food;
    }
}

class Person {
    int id;
    String name;

    Person(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            showFeatures();

            System.out.println("Do you want to avail services: ");
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("yes")) {
                System.out.print("\nEnter ID: ");
                int id = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Enter Name: ");
                String name = scanner.nextLine();

                Person person = new Person(id, name);
                RoomInfo[] roomInfo = choiceOfRoom();
                int amount = calculateTotal(roomInfo);
                showBill(person, roomInfo);
                System.out.println("Dear, " + name + "\nThe total amount you need to pay: " + amount);
                storeToDatabase(person, roomInfo);
                scanner.nextLine();
            } else {
                System.exit(0);
            }
        }
    }

    static RoomInfo[] choiceOfRoom() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter No. of Days you will stay: ");
        int days = scanner.nextInt();

        System.out.println("No. of Gold Room required: ");
        int gold = scanner.nextInt();

        System.out.println("No. of Silver Room required: ");
        int silver = scanner.nextInt();

        System.out.println("No. of Bronze Room required: ");
        int bronze = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        System.out.print("Do you need spa (yes/no): ");
        String spa = scanner.nextLine();

        System.out.print("Do you need laundry (yes/no): ");
        String laundry = scanner.nextLine();

        System.out.print("Do you need food (yes/no): ");
        String food = scanner.nextLine();

        RoomInfo[] roomInfo = new RoomInfo[3];
        int spaPrice = spa.equalsIgnoreCase("yes") ? 2500 : 0;
        int laundryPrice = laundry.equalsIgnoreCase("yes") ? 500 : 0;
        int foodPrice = food.equalsIgnoreCase("yes") ? 2000 : 0;
        roomInfo[0] = new RoomInfo("Gold", days, gold, 5000, spaPrice, laundryPrice, foodPrice);

        spaPrice = spa.equalsIgnoreCase("yes") ? 2000 : 0;
        laundryPrice = laundry.equalsIgnoreCase("yes") ? 400 : 0;
        foodPrice = food.equalsIgnoreCase("yes") ? 1500 : 0;
        roomInfo[1] = new RoomInfo("Silver", days, silver, 4000, spaPrice, laundryPrice, foodPrice);

        spaPrice = spa.equalsIgnoreCase("yes") ? 1500 : 0;
        laundryPrice = laundry.equalsIgnoreCase("yes") ? 300 : 0;
        foodPrice = food.equalsIgnoreCase("yes") ? 1000 : 0;
        roomInfo[2] = new RoomInfo("Bronze", days, bronze, 3000, spaPrice, laundryPrice, foodPrice);

        return roomInfo;
    }

    static int calculateTotal(RoomInfo[] roomInfo) {
        int grandTotal = 0;
        for (RoomInfo info : roomInfo) {
            if (info.count > 0) {
                Rooms room = new Rooms(info.rent, info.spa, info.laundry, info.food);
                grandTotal += room.calculate(info.days * info.count);
            }
        }
        return grandTotal;
    }

    static void showBill(Person person, RoomInfo[] roomInfo) {
        System.out.println("\nBill Details:");
        System.out.println("ID: " + person.id);
        System.out.println("Name: " + person.name);
        for (RoomInfo info : roomInfo) {
            if (info.count > 0) {
                System.out.println(info.type + " Room:");
                System.out.println("  Days count: " + info.days);
                System.out.println("  Rooms count: " + info.count);
                System.out.println("  Rent per room: " + info.rent);
                System.out.println("  Spa: " + (info.spa > 0 ? "Yes (" + info.spa + ")" : "No"));
                System.out.println("  Laundry: " + (info.laundry > 0 ? "Yes (" + info.laundry + ")" : "No"));
                System.out.println("  Food: " + (info.food > 0 ? "Yes (" + info.food + ")" : "No"));
                System.out.println();
            }
        }
    }

    static void showFeatures() {
        System.out.println("Welcome to Our Hotel \nThere are 3 types of room available.");
        System.out.println("1. Gold \t2. Silver \t3.Bronze");
        System.out.println("Services available for each room are: ");
        System.out.println("1. Spa\t2. Laundry \t3. Food");

        System.out.println("\nRent for Rooms (per day): \n");
        System.out.println("Gold: ₹5000 \t Silver: ₹4000 \t Bronze: ₹3000");

        // Gold
        System.out.println("\nServices price for Gold room: ");
        System.out.println("1. Spa: ₹2500 \n2. Laundry: ₹500 \n3. Food: ₹2000");

        // Silver
        System.out.println("\nServices price for Silver room: ");
        System.out.println("1. Spa: ₹2000 \n2. Laundry: ₹400 \n3. Food: ₹1500");

        // Bronze
        System.out.println("\nServices price for Bronze room: ");
        System.out.println("1. Spa: ₹1500 \n2. Laundry: ₹300 \n3. Food: ₹1000");
    }

    static void storeToDatabase(Person person, RoomInfo[] roomInfo){
        String url = "jdbc:mysql://localhost:3306/hm";
        String user = "root";
        String password = "";

        try(Connection conn = DriverManager.getConnection(url, user, password)) {

            String personInsert="insert into customer(id, name) values(?,?)";

            try(PreparedStatement personStmt=conn.prepareStatement(personInsert)) {
                personStmt.setInt(1,person.id);
                personStmt.setString(2,person.name);
                personStmt.executeUpdate();
            }

            String roomInfoInsert = "INSERT INTO roomdetail (type, daycount, roomcount, rent, spa, laundry, food, person_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement roomInfoStmt = conn.prepareStatement(roomInfoInsert)) {
                for (RoomInfo info : roomInfo) {
                    if (info.count > 0) {
                        roomInfoStmt.setString(1, info.type);
                        roomInfoStmt.setInt(2, info.days);
                        roomInfoStmt.setInt(3, info.count);
                        roomInfoStmt.setInt(4, info.rent);
                        roomInfoStmt.setInt(5, info.spa);
                        roomInfoStmt.setInt(6, info.laundry);
                        roomInfoStmt.setInt(7, info.food);
                        roomInfoStmt.setInt(8, person.id);
                        roomInfoStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}