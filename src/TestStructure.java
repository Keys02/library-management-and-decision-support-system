import datastructures.linear.LinkedList;
import datastructures.linear.LinkedStack;
import datastructures.linear.ArrayQueue;
import datastructures.linear.CircularQueue;
import datastructures.heap.IntMaxHeap;
import datastructures.heap.PriorityQueue;
import datastructures.hash.HashTable;
import datastructures.tree.BinarySearchTree;
import model.ServiceRequest;
import model.RequestType;
import model.RequestStatus;
import java.time.LocalDateTime;

public class TestStructure {
    public static void main(String[] args) {
        testLinkedList();
        testStack();
        testQueue();
        testCircularQueue();
        testHeap();
        testPriorityQueue();
        testHashTable();
        testBSTNode();
        System.out.println("\n All structure tests passed!");
    }

    static void testLinkedList() {
        System.out.println("Testing LinkedList...");
        LinkedList<String> list = new LinkedList<>();
        list.addLast("Accra");
        list.addLast("Kumasi");
        list.addLast("Tamale");
        assert list.size() == 3 : "Size should be 3";
        assert list.get(0).equals("Accra") : "First should be Accra";
        assert list.get(2).equals("Tamale") : "Last should be Tamale";
        list.remove(1);
        assert list.size() == 2 : "Size should be 2 after remove";
        assert list.get(1).equals("Tamale") : "Should be Tamale after remove";
        System.out.println(" LinkedList ");
    }

    static void testStack() {
        System.out.println("Testing Stack...");
        LinkedStack<Integer> stack = new LinkedStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assert stack.size() == 3 : "Size should be 3";
        assert stack.pop() == 3 : "Top should be 3";
        assert stack.pop() == 2 : "Top should be 2";
        assert stack.size() == 1 : "Size should be 1";
        System.out.println("  Stack ");
    }

    static void testQueue() {
        System.out.println("Testing Queue...");
        ArrayQueue<String> queue = new ArrayQueue<>();
        queue.enqueue("First");
        queue.enqueue("Second");
        queue.enqueue("Third");
        assert queue.size() == 3 : "Size should be 3";
        assert queue.dequeue().equals("First") : "First out should be First";
        assert queue.dequeue().equals("Second") : "Second out should be Second";
        assert queue.size() == 1 : "Size should be 1";
        System.out.println("  Queue ");
    }

    static void testCircularQueue() {
        System.out.println("Testing CircularQueue...");
        CircularQueue<String> queue = new CircularQueue<>();
        queue.enqueue("Branch A");
        queue.enqueue("Branch B");
        queue.enqueue("Branch C");
        assert queue.dequeue().equals("Branch A") : "First should be Branch A";
        queue.enqueue("Branch D");
        assert queue.dequeue().equals("Branch B") : "Next should be Branch B";
        System.out.println("  CircularQueue ");
    }

    static void testHeap() {
        System.out.println("Testing IntMaxHeap...");
        IntMaxHeap heap = new IntMaxHeap();
        heap.insert(5);
        heap.insert(10);
        heap.insert(3);
        heap.insert(8);
        assert heap.extractMax() == 10 : "Max should be 10";
        assert heap.extractMax() == 8 : "Next max should be 8";
        assert heap.extractMax() == 5 : "Next max should be 5";
        assert heap.extractMax() == 3 : "Next max should be 3";
        System.out.println("  IntMaxHeap ");
    }

    static void testPriorityQueue() {
        System.out.println("Testing PriorityQueue with ServiceRequests...");
        PriorityQueue<ServiceRequest> pq = new PriorityQueue<>();
        ServiceRequest low = new ServiceRequest(1, 1, 1, RequestType.BORROW, 2, RequestStatus.PENDING,
                LocalDateTime.now());
        ServiceRequest medium = new ServiceRequest(2, 2, 2, RequestType.BORROW, 5, RequestStatus.PENDING,
                LocalDateTime.now());
        ServiceRequest high = new ServiceRequest(3, 3, 3, RequestType.BORROW, 9, RequestStatus.PENDING,
                LocalDateTime.now());
        pq.enqueue(low);
        pq.enqueue(high);
        pq.enqueue(medium);
        assert pq.dequeue().getUrgency() == 9 : "Highest urgency should come first";
        assert pq.dequeue().getUrgency() == 5 : "Medium urgency should come second";
        assert pq.dequeue().getUrgency() == 2 : "Lowest urgency should come last";
        System.out.println("  PriorityQueue ");
    }

    static void testHashTable() {
        System.out.println("Testing HashTable...");
        HashTable<Integer, String> table = new HashTable<>();
        table.put(1, "Accra Central Library");
        table.put(2, "Kumasi Ashanti Library");
        table.put(3, "Tamale Northern Library");
        assert table.get(1).equals("Accra Central Library") : "Should return Accra";
        assert table.get(2).equals("Kumasi Ashanti Library") : "Should return Kumasi";
        assert table.containsKey(3) : "Should contain key 3";
        assert !table.containsKey(99) : "Should not contain key 99";
        table.remove(2);
        assert !table.containsKey(2) : "Key 2 should be removed";
        System.out.println("  HashTable ");
    }

    static void testBSTNode() {
        System.out.println("Testing BinarySearchTree...");
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.insert(5);
        bst.insert(3);
        bst.insert(7);
        bst.insert(1);
        bst.insert(4);
        assert bst.contains(5) : "Should contain 5";
        assert bst.contains(1) : "Should contain 1";
        assert !bst.contains(99) : "Should not contain 99";
        bst.delete(3);
        assert !bst.contains(3) : "3 should be deleted";
        assert bst.contains(4) : "4 should still exist";
        System.out.println("  BST");
    }
}