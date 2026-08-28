import repository.*;
import datastructures.linear.LinkedList;
import model.*;

public class TestRepositories {
    public static void main(String[] args) {
        testLibraryRepository();
        testBookRepository();
        testPatronRepository();
        testRoadRepository();
        testResourceRepository();
        testServiceRequestRepository();
        testLibraryFindById();
        testBookFindById();
        System.out.println("\n All repository tests passed!");
    }

    static void testLibraryRepository() {
        System.out.println("Testing LibraryRepository...");
        LibraryRepository repo = new LibraryRepository();
        LinkedList<Library> all = repo.findAll();
        assert all.size() == 50 : "Should have 50 libraries, got " + all.size();
        System.out.println("  LibraryRepository (" + all.size() + " libraries)");
    }

    static void testBookRepository() {
        System.out.println("Testing BookRepository...");
        BookRepository repo = new BookRepository();
        LinkedList<Book> all = repo.findAll();
        assert all.size() == 50 : "Should have 50 books, got " + all.size();
        System.out.println("  BookRepository (" + all.size() + " books)");
    }

    static void testPatronRepository() {
        System.out.println("Testing PatronRepository...");
        PatronRepository repo = new PatronRepository();
        LinkedList<Patron> all = repo.findAll();
        assert all.size() == 50 : "Should have 50 patrons, got " + all.size();
        System.out.println("  PatronRepository (" + all.size() + " patrons)");
    }

    static void testRoadRepository() {
        System.out.println("Testing RoadRepository...");
        RoadRepository repo = new RoadRepository();
        LinkedList<Road> all = repo.findAll();
        assert all.size() == 100 : "Should have 100 roads, got " + all.size();
        System.out.println("  RoadRepository (" + all.size() + " roads)");
    }

    static void testResourceRepository() {
        System.out.println("Testing ResourceRepository...");
        ResourceRepository repo = new ResourceRepository();
        LinkedList<Resource> all = repo.findAll();
        assert all.size() == 30 : "Should have 30 resources, got " + all.size();
        System.out.println("  ResourceRepository (" + all.size() + " resources)");
    }

    static void testServiceRequestRepository() {
        System.out.println("Testing ServiceRequestRepository...");
        ServiceRequestRepository repo = new ServiceRequestRepository();
        LinkedList<ServiceRequest> all = repo.findAll();
        assert all.size() == 300 : "Should have 300 requests, got " + all.size();
        System.out.println("  ServiceRequestRepository (" + all.size() + " requests)");
    }

    static void testLibraryFindById() {
        System.out.println("Testing LibraryRepository.findById...");
        LibraryRepository repo = new LibraryRepository();
        Library lib = repo.findById(1);
        assert lib != null : "Library 1 should exist";
        assert lib.getId() == 1 : "ID should be 1";
        System.out.println("  findById: " + lib.getLibraryName());
    }

    static void testBookFindById() {
        System.out.println("Testing BookRepository.findById...");
        BookRepository repo = new BookRepository();
        Book book = repo.findById(1);
        assert book != null : "Book 1 should exist";
        assert book.getId() == 1 : "ID should be 1";
        System.out.println("  findById: " + book.getTitle());
    }
}