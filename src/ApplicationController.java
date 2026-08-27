import datastructures.graph.Graph;
import datastructures.graph.DijkstraResult;
import datastructures.linear.LinkedList;
import datastructures.linear.DynamicArray;
import datastructures.tree.BinarySearchTree;
import datastructures.heap.PriorityQueue;
import algorithms.Sorting;
import algorithms.Searching;
import model.*;
import repository.*;
import service.RequestService;

import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * ApplicationController — wires the entire system together.
 *
 * Startup sequence:
 *   1. Load all data from DB into in-memory data structures
 *   2. Hand control to the console menu
 *
 * Every menu action operates on the in-memory structures first,
 * then persists changes back to the DB via repositories.
 */
public class ApplicationController {

    // ── Repositories ───────────────────────────────────
    private final LibraryRepository      libraryRepo      = new LibraryRepository();
    private final BookRepository         bookRepo         = new BookRepository();
    private final PatronRepository       patronRepo       = new PatronRepository();
    private final RoadRepository         roadRepo         = new RoadRepository();
    private final ResourceRepository     resourceRepo     = new ResourceRepository();
    private final ServiceRequestRepository requestRepo    = new ServiceRequestRepository();

    // ── In-memory data structures ──────────────────────
    private final Graph                         graph        = new Graph();
    private final BinarySearchTree<String>      bookIndex    = new BinarySearchTree<>();
    private final PriorityQueue<ServiceRequest> requestQueue = new PriorityQueue<>();
    private final RequestService                requestService = new RequestService();

    private LinkedList<Book>           books;
    private LinkedList<Patron>         patrons;
    private LinkedList<Library>        libraries;
    private LinkedList<Resource>       resources;

    private final Scanner scanner = new Scanner(System.in);

    // ── Entry point called by Application.run() ────────

    public void execute() {
        System.out.println("[System] Initialising...");
        loadDataIntoStructures();
        System.out.println("[System] Ready. " + libraries.size() + " libraries, "
            + books.size() + " books, " + patrons.size() + " patrons loaded.");
        runMenu();
    }

    // ── Step 1: Load DB → Data Structures ─────────────

    public synchronized void loadDataIntoStructures() {
        libraries = libraryRepo.findAll();
        if (libraries.isEmpty()) {
            System.out.println("[System] Database is empty. Automatically loading seed data from CSVs...");
            try {
                DataLoader.loadAll();
                libraries = libraryRepo.findAll();
            } catch (Exception e) {
                System.out.println("[System] Note on seed data: " + e.getMessage());
            }
        }

        // Load libraries into graph
        for (int i = 0; i < libraries.size(); i++)
            graph.addLibrary(libraries.get(i));

        // Load roads into graph
        LinkedList<Road> roads = roadRepo.findAll();
        for (int i = 0; i < roads.size(); i++)
            graph.addRoad(roads.get(i));

        // Load books into LinkedList + BST index (by title)
        books = bookRepo.findAll();
        for (int i = 0; i < books.size(); i++)
            bookIndex.insert(books.get(i).getTitle());

        // Load patrons
        patrons = patronRepo.findAll();

        // Load resources
        resources = resourceRepo.findAll();

        // Load pending service requests into priority queue
        LinkedList<ServiceRequest> requests = requestRepo.findAll();
        for (int i = 0; i < requests.size(); i++) {
            ServiceRequest r = requests.get(i);
            if (r.getStatus() == RequestStatus.PENDING) {
                requestQueue.enqueue(r);
                requestService.submitRequest(r);
            }
        }
    }

    public synchronized void addNewBook(Book book) {
        bookRepo.save(book);
        books = bookRepo.findAll();
        bookIndex.insert(book.getTitle());
    }

    public synchronized void addNewPatron(Patron patron) {
        patronRepo.save(patron);
        patrons = patronRepo.findAll();
    }

    public synchronized void submitNewRequest(ServiceRequest req) {
        requestRepo.save(req);
        requestQueue.enqueue(req);
        requestService.submitRequest(req);
    }

    public synchronized ServiceRequest processNextRequest() {
        if (requestService.hasPending()) {
            ServiceRequest next = requestService.processNext();
            requestRepo.updateStatus(next.getId(), RequestStatus.PROCESSING);
            return next;
        }
        return null;
    }

    public LinkedList<Book> getBooks() { return books; }
    public LinkedList<Patron> getPatrons() { return patrons; }
    public LinkedList<Library> getLibraries() { return libraries; }
    public LinkedList<Resource> getResources() { return resources; }
    public Graph getGraph() { return graph; }
    public BinarySearchTree<String> getBookIndex() { return bookIndex; }
    public PriorityQueue<ServiceRequest> getRequestQueue() { return requestQueue; }
    public RequestService getRequestService() { return requestService; }
    public BookRepository getBookRepo() { return bookRepo; }
    public PatronRepository getPatronRepo() { return patronRepo; }
    public ServiceRequestRepository getRequestRepo() { return requestRepo; }
    public LibraryRepository getLibraryRepo() { return libraryRepo; }
    public RoadRepository getRoadRepo() { return roadRepo; }
    public ResourceRepository getResourceRepo() { return resourceRepo; }

    // ── Step 2: Console Menu ───────────────────────────

    private void runMenu() {
        boolean running = true;
        while (running) {
            printMainMenu();
            System.out.print("Enter your choice: ");
            int choice = readInt();
            switch (choice) {
                case 1  -> bookManagement();
                case 2  -> patronManagement();
                case 3  -> serviceRequestMenu();
                case 4  -> searchBooks();
                case 5  -> sortBooks();
                case 6  -> decisionSupport();
                case 7  -> graphNavigation();
                case 8  -> systemStatistics();
                case 9  -> { running = false; System.out.println("\nGoodbye!"); }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n=================================================");
        System.out.println(" LIBRARY MANAGEMENT & DECISION SUPPORT SYSTEM");
        System.out.println("=================================================");
        System.out.println("1. Book Management");
        System.out.println("2. Patron Management");
        System.out.println("3. Service Requests");
        System.out.println("4. Search Books");
        System.out.println("5. Sort Books");
        System.out.println("6. Decision Support (Greedy / DP)");
        System.out.println("7. Graph Navigation (BFS/DFS/Dijkstra/MST)");
        System.out.println("8. System Statistics");
        System.out.println("9. Exit");
        System.out.println("=================================================");
    }

    // ── 1. Book Management ─────────────────────────────

    private void bookManagement() {
        System.out.println("\n===== BOOK MANAGEMENT =====");
        System.out.println("1. View all books");
        System.out.println("2. Add a book");
        System.out.println("3. Back");
        System.out.print("Choice: ");
        switch (readInt()) {
            case 1 -> {
                System.out.println("\nAll books (" + books.size() + "):");
                for (int i = 0; i < books.size(); i++) {
                    Book b = books.get(i);
                    System.out.printf("  [%d] %s by %s — %s%n",
                        b.getId(), b.getTitle(), b.getAuthor(),
                        b.isAvailable() ? "Available" : "Borrowed");
                }
            }
            case 2 -> {
                System.out.print("Title: ");    String title  = scanner.nextLine();
                System.out.print("Author: ");   String author = scanner.nextLine();
                System.out.print("ISBN: ");     String isbn   = scanner.nextLine();
                System.out.print("Library ID: "); int libId  = readInt();
                Book book = new Book(0, title, author, isbn, true, libId);
                bookRepo.save(book);
                books = bookRepo.findAll();
                bookIndex.insert(title);
                System.out.println("Book added.");
            }
            default -> { /* back */ }
        }
    }

    // ── 2. Patron Management ───────────────────────────

    private void patronManagement() {
        System.out.println("\n===== PATRON MANAGEMENT =====");
        System.out.println("1. View all patrons");
        System.out.println("2. Add patron");
        System.out.println("3. Back");
        System.out.print("Choice: ");
        switch (readInt()) {
            case 1 -> {
                System.out.println("\nAll patrons (" + patrons.size() + "):");
                for (int i = 0; i < patrons.size(); i++) {
                    Patron p = patrons.get(i);
                    System.out.printf("  [%d] %s — %s — %s%n",
                        p.getId(), p.getName(), p.getEmail(), p.getPhoneNumber());
                }
            }
            case 2 -> {
                System.out.print("Name: ");         String name  = scanner.nextLine();
                System.out.print("Email: ");        String email = scanner.nextLine();
                System.out.print("Phone number: "); String phone = scanner.nextLine();
                patronRepo.save(new Patron(0, name, email, phone));
                patrons = patronRepo.findAll();
                System.out.println("Patron added.");
            }
            default -> { /* back */ }
        }
    }

    // ── 3. Service Requests ────────────────────────────

    private void serviceRequestMenu() {
        System.out.println("\n===== SERVICE REQUESTS =====");
        System.out.println("1. View pending requests (priority order)");
        System.out.println("2. Submit new request");
        System.out.println("3. Process next request");
        System.out.println("4. Back");
        System.out.print("Choice: ");
        switch (readInt()) {
            case 1 -> {
                System.out.println("Pending: " + requestQueue.size() + " requests");
                System.out.println("Next up: " + (requestQueue.isEmpty() ? "none" : requestQueue.peek()));
            }
            case 2 -> {
                System.out.print("Patron ID: ");  int patronId = readInt();
                System.out.print("Book ID: ");    int bookId   = readInt();
                System.out.print("Type (BORROW/RETURN/RESERVE): "); String type = scanner.nextLine();
                System.out.print("Urgency (1-10): "); int urgency = readInt();
                ServiceRequest req = new ServiceRequest(0, patronId, bookId,
                    RequestType.valueOf(type.toUpperCase()),
                    urgency, RequestStatus.PENDING, LocalDateTime.now());
                requestRepo.save(req);
                requestQueue.enqueue(req);
                requestService.submitRequest(req);
                System.out.println("Request submitted.");
            }
            case 3 -> {
                if (requestService.hasPending()) {
                    ServiceRequest next = requestService.processNext();
                    System.out.println("Processing: " + next);
                    requestRepo.updateStatus(next.getId(), RequestStatus.PROCESSING);
                } else {
                    System.out.println("No pending requests.");
                }
            }
            default -> { /* back */ }
        }
    }

    // ── 4. Search Books ────────────────────────────────

    private void searchBooks() {
        System.out.println("\n===== SEARCH BOOKS =====");
        System.out.println("1. Linear search by title");
        System.out.println("2. BST search by title");
        System.out.println("3. Back");
        System.out.print("Choice: ");
        int choice = readInt();
        if (choice == 3) return;
        System.out.print("Enter title to search: ");
        String query = scanner.nextLine().toLowerCase();

        if (choice == 1) {
            // Linear search through books LinkedList
            boolean found = false;
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).getTitle().toLowerCase().contains(query)) {
                    Book b = books.get(i);
                    System.out.printf("  Found: [%d] %s by %s%n", b.getId(), b.getTitle(), b.getAuthor());
                    found = true;
                }
            }
            if (!found) System.out.println("  No books found.");
        } else {
            // BST contains check (exact title)
            boolean found = bookIndex.contains(query);
            System.out.println(found ? "  Found in BST index." : "  Not found in BST index.");
        }
    }

    // ── 5. Sort Books ──────────────────────────────────

    private void sortBooks() {
        System.out.println("\n===== SORT BOOKS =====");
        System.out.println("Sorting " + books.size() + " books by ID using merge sort...");

        DynamicArray<Integer> ids = new DynamicArray<>();
        for (int i = 0; i < books.size(); i++) ids.add(books.get(i).getId());

        long start = System.nanoTime();
        Sorting.mergeSort(ids, 0, ids.size() - 1);
        long elapsed = System.nanoTime() - start;

        System.out.println("Sorted " + ids.size() + " book IDs in " + elapsed + " ns.");
        System.out.print("First 10 IDs: ");
        for (int i = 0; i < Math.min(10, ids.size()); i++)
            System.out.print(ids.get(i) + " ");
        System.out.println();
    }

    // ── 6. Decision Support ────────────────────────────

    private void decisionSupport() {
        System.out.println("\n===== DECISION SUPPORT =====");
        System.out.println("Running 0/1 Knapsack on resources with budget = 5000...");

        DynamicArray<Resource> res = new DynamicArray<>();
        for (int i = 0; i < resources.size(); i++) res.add(resources.get(i));

        DynamicArray<Resource> chosen = algorithms.DynamicProgramming.knapsack(res, 5000);

        int totalValue = 0;
        double totalCost = 0;
        System.out.println("Optimal resource selection:");
        for (int i = 0; i < chosen.size(); i++) {
            Resource r = chosen.get(i);
            System.out.printf("  %s (cost=%.0f, value=%d)%n", r.getName(), r.getCost(), r.getValue());
            totalValue += r.getValue();
            totalCost  += r.getCost();
        }
        System.out.printf("Total cost: %.0f | Total value: %d%n", totalCost, totalValue);
    }

    // ── 7. Graph Navigation ────────────────────────────

    private void graphNavigation() {
        System.out.println("\n===== GRAPH NAVIGATION =====");
        System.out.println("Graph has " + graph.vertexCount() + " libraries and " + graph.edgeCount() + " roads.");
        System.out.println("1. BFS from library");
        System.out.println("2. DFS from library");
        System.out.println("3. Shortest path (Dijkstra)");
        System.out.println("4. Minimum Spanning Tree (Kruskal)");
        System.out.println("5. Back");
        System.out.print("Choice: ");
        switch (readInt()) {
            case 1 -> {
                System.out.print("Start library ID: "); int id = readInt();
                DynamicArray<Integer> bfs = graph.bfs(id);
                System.out.print("BFS order: ");
                for (int i = 0; i < bfs.size(); i++) System.out.print(bfs.get(i) + " ");
                System.out.println();
            }
            case 2 -> {
                System.out.print("Start library ID: "); int id = readInt();
                DynamicArray<Integer> dfs = graph.dfs(id);
                System.out.print("DFS order: ");
                for (int i = 0; i < dfs.size(); i++) System.out.print(dfs.get(i) + " ");
                System.out.println();
            }
            case 3 -> {
                System.out.print("Source library ID: ");      int src = readInt();
                System.out.print("Destination library ID: "); int dst = readInt();
                DijkstraResult result = graph.dijkstra(src);
                if (result.isReachable(dst)) {
                    System.out.printf("Shortest travel time: %.2f hours%n", result.distanceTo(dst));
                    LinkedList<Integer> path = result.pathTo(dst);
                    System.out.print("Path: ");
                    for (int i = 0; i < path.size(); i++) System.out.print(path.get(i) + " ");
                    System.out.println();
                } else {
                    System.out.println("No path found.");
                }
            }
            case 4 -> {
                LinkedList<Road> mst = graph.kruskal();
                System.out.println("MST has " + mst.size() + " roads:");
                double totalDist = 0;
                for (int i = 0; i < mst.size(); i++) {
                    Road r = mst.get(i);
                    System.out.printf("  Library %d ↔ Library %d (%.1f km)%n",
                        r.getSourceLibraryId(), r.getDestinationLibraryId(), r.getDistance());
                    totalDist += r.getDistance();
                }
                System.out.printf("Total MST distance: %.1f km%n", totalDist);
            }
            default -> { /* back */ }
        }
    }

    // ── 8. System Statistics ───────────────────────────

    private void systemStatistics() {
        System.out.println("\n===== SYSTEM STATISTICS =====");
        System.out.printf("Libraries  : %d%n", libraries.size());
        System.out.printf("Books      : %d%n", books.size());
        System.out.printf("Patrons    : %d%n", patrons.size());
        System.out.printf("Resources  : %d%n", resources.size());
        System.out.printf("Graph nodes: %d%n", graph.vertexCount());
        System.out.printf("Graph edges: %d%n", graph.edgeCount());
        System.out.printf("Pending requests: %d%n", requestQueue.size());
        }

    // ── Utility ────────────────────────────────────────

    private int readInt() {
        try {
            int val = Integer.parseInt(scanner.nextLine().trim());
            return val;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}