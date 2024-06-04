import java.util.Scanner;

public class Phase2 {
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
                scanner.nextLine();
            } else {
                System.exit(0);
            }
        }
    }

    static RoomInfo[] choiceOfRoom() {
        Scanner scanner = new Scanner(System.in);

        RoomInfo[] roomInfo = new RoomInfo[3];

        System.out.println("\nEnter details for Gold Room: ");
        roomInfo[0] = getRoomDetails(scanner, "Gold", 5000, 2500, 500, 2000);

        System.out.println("\nEnter details for Silver Room: ");
        roomInfo[1] = getRoomDetails(scanner, "Silver", 4000, 2000, 400, 1500);

        System.out.println("\nEnter details for Bronze Room: ");
        roomInfo[2] = getRoomDetails(scanner, "Bronze", 3000, 1500, 300, 1000);

        return roomInfo;
    }

    static RoomInfo getRoomDetails(Scanner scanner, String type, int rent, int spaRate, int laundryRate, int foodRate) {
        System.out.println("Enter No. of Days you will stay: ");
        int days = scanner.nextInt();

        System.out.println("No. of " + type + " Room required: ");
        int count = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        System.out.print("Do you need spa (yes/no): ");
        String spa = scanner.nextLine();
        int spaPrice = spa.equalsIgnoreCase("yes") ? spaRate : 0;

        System.out.print("Do you need laundry (yes/no): ");
        String laundry = scanner.nextLine();
        int laundryPrice = laundry.equalsIgnoreCase("yes") ? laundryRate : 0;

        System.out.print("Do you need food (yes/no): ");
        String food = scanner.nextLine();
        int foodPrice = food.equalsIgnoreCase("yes") ? foodRate : 0;

        return new RoomInfo(type, days, count, rent, spaPrice, laundryPrice, foodPrice);
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
        System.out.println("\nDetailed Bill:");
        System.out.println("ID: " + person.id);
        System.out.println("Name: " + person.name);
        for (RoomInfo info : roomInfo) {
            if (info.count > 0) {
                System.out.println(info.type + " Room:");
                System.out.println("  Days: " + info.days);
                System.out.println("  Count: " + info.count);
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
}
