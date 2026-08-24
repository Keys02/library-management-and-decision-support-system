package demo;

import java.util.Scanner;

public class LibraryManagementDemo {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        boolean running = true;

        while (running) {

            printMainMenu();

            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {

                case 1:
                    bookManagementMenu();
                    break;

                case 2:
                    System.out.println("\nPatron Management coming soon...\n");
                    break;

                case 3:
                    System.out.println("\nService Requests coming soon...\n");
                    break;

                case 4:
                    System.out.println("\nSearch Books coming soon...\n");
                    break;

                case 5:
                    System.out.println("\nSort Books coming soon...\n");
                    break;

                case 6:
                    System.out.println("\nDecision Support coming soon...\n");
                    break;

                case 7:
                    System.out.println("\nGraph Navigation coming soon...\n");
                    break;

                case 8:
                    System.out.println("\nSystem Statistics coming soon...\n");
                    break;

                case 9:
                    running = false;
                    System.out.println("\nThank you for using the system!");
                    break;

                default:
                    System.out.println("\nInvalid choice. Try again.\n");

            }

        }

        scanner.close();

    }

    private static void printMainMenu() {

        System.out.println();
        System.out.println("=================================================");
        System.out.println(" LIBRARY MANAGEMENT & DECISION SUPPORT SYSTEM");
        System.out.println("=================================================");
        System.out.println("1. Book Management");
        System.out.println("2. Patron Management");
        System.out.println("3. Service Requests");
        System.out.println("4. Search Books");
        System.out.println("5. Sort Books");
        System.out.println("6. Decision Support");
        System.out.println("7. Graph Navigation");
        System.out.println("8. System Statistics");
        System.out.println("9. Exit");
        System.out.println("=================================================");

    }

    private static void bookManagementMenu() {

        boolean back = false;

        while (!back) {

            System.out.println();
            System.out.println("========== BOOK MANAGEMENT ==========");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Search Book");
            System.out.println("4. Delete Book");
            System.out.println("5. Back");
            System.out.println("=====================================");

            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    System.out.println("\nAdd Book coming soon...\n");
                    break;

                case 2:
                    System.out.println("\nView Books coming soon...\n");
                    break;

                case 3:
                    System.out.println("\nSearch Book coming soon...\n");
                    break;

                case 4:
                    System.out.println("\nDelete Book coming soon...\n");
                    break;

                case 5:
                    back = true;
                    break;

                default:
                    System.out.println("\nInvalid choice.\n");

            }

        }

    }

}