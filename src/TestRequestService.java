import service.RequestService;
import model.*;
import java.time.LocalDateTime;

public class TestRequestService {
    public static void main(String[] args) {
        testFIFOOrder();
        testPriorityOrder();
        testCircularOrder();
        testPendingCount();
        testHasPending();
        testModeSwitch();
        System.out.println("\n All RequestService tests passed!");
    }

    static ServiceRequest makeRequest(int id, int urgency) {
        return new ServiceRequest(id, 1, 1, RequestType.BORROW,
            urgency, RequestStatus.PENDING, LocalDateTime.now());
    }

    static void testFIFOOrder() {
        System.out.println("Testing FIFO dispatch...");
        RequestService service = new RequestService();
        service.setMode(RequestService.DispatchMode.FIFO);
        service.submitRequest(makeRequest(1, 5));
        service.submitRequest(makeRequest(2, 9));
        service.submitRequest(makeRequest(3, 1));
        assert service.processNext().getId() == 1 : "FIFO: first submitted should be first out";
        assert service.processNext().getId() == 2 : "FIFO: second submitted should be second out";
        System.out.println("  FIFO dispatch");
    }

    static void testPriorityOrder() {
        System.out.println("Testing PRIORITY dispatch...");
        RequestService service = new RequestService();
        service.setMode(RequestService.DispatchMode.PRIORITY);
        service.submitRequest(makeRequest(1, 3));
        service.submitRequest(makeRequest(2, 9));
        service.submitRequest(makeRequest(3, 6));
        assert service.processNext().getUrgency() == 9 : "PRIORITY: highest urgency first";
        assert service.processNext().getUrgency() == 6 : "PRIORITY: second highest next";
        System.out.println("  PRIORITY dispatch");
    }

    static void testCircularOrder() {
        System.out.println("Testing CIRCULAR dispatch...");
        RequestService service = new RequestService();
        service.setMode(RequestService.DispatchMode.CIRCULAR);
        service.submitRequest(makeRequest(1, 5));
        service.submitRequest(makeRequest(2, 9));
        service.submitRequest(makeRequest(3, 1));
        assert service.processNext() != null : "CIRCULAR: should return first request";
        assert service.processNext() != null : "CIRCULAR: should return second request";
        System.out.println("  CIRCULAR dispatch");
    }

    static void testPendingCount() {
        System.out.println("Testing pending count...");
        RequestService service = new RequestService();
        service.setMode(RequestService.DispatchMode.FIFO);
        service.submitRequest(makeRequest(1, 5));
        service.submitRequest(makeRequest(2, 3));
        assert service.pendingCount() == 2 : "Should have 2 pending";
        service.processNext();
        assert service.pendingCount() == 1 : "Should have 1 pending after processing";
        System.out.println("  Pending count");
    }

    static void testHasPending() {
        System.out.println("Testing hasPending...");
        RequestService service = new RequestService();
        service.setMode(RequestService.DispatchMode.FIFO);
        assert !service.hasPending() : "Should have no pending initially";
        service.submitRequest(makeRequest(1, 5));
        assert service.hasPending() : "Should have pending after submit";
        System.out.println("  hasPending");
    }

    static void testModeSwitch() {
        System.out.println("Testing mode switch...");
        RequestService service = new RequestService();
        service.setMode(RequestService.DispatchMode.FIFO);
        assert service.getMode() == RequestService.DispatchMode.FIFO : "Mode should be FIFO";
        service.setMode(RequestService.DispatchMode.PRIORITY);
        assert service.getMode() == RequestService.DispatchMode.PRIORITY : "Mode should be PRIORITY";
        System.out.println("  Mode switch");
    }
}