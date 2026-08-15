# Java Programming Plan — JP1–JP4

**Owners:** 4 Java Programmers
**Produces:** brief Modules M3 (custom data-structure library), M4
(search/sort), M5 (scheduling), M6 (indexing), M7 (graph), M8
(optimisation) — the single largest chunk of the grading rubric (40 of
100 marks between data structures + algorithms, before testing/efficiency
marks that also depend on this work existing).

---

## 0. Where we are right now

Already built — do not redo, build on top of:
- `model/` — `Library`, `Book`, `Librarian`, `Patron`, `ServiceRequest`,
  `Road`, `Resource`, `AlgorithmRun`
- `model/RequestStatus`, `RequestType`, `ResourceType` enums
- `exception/` — `BookUnavailableException`, `LibraryConnectionException`,
  `InvalidWeightTypeException`
- `db/DatabaseManager`, `db/LibraryRepository` (working end-to-end,
  proven against live SQLite)

Not yet built — this is everything below.

---

## 1. Hard rule — read this before writing anything

**Do not use `java.util.HashMap`, `PriorityQueue`, `Stack`, `ArrayDeque`,
`TreeMap`, `LinkedList`, or any built-in collection for graded logic.**
Built-ins are fine only for file I/O, JDBC (already using `ResultSet`/
`PreparedStatement`, that's fine), and test scaffolding. Every structure
in Section 6 of the brief must be your own implementation. This is
checked — using a built-in here isn't a shortcut, it's the exact thing
the brief's "AI-resistance" controls are looking for, and it zeroes out
that structure's marks entirely rather than partially.

---

## 2. Package structure to build

```
src/
├── datastructures/     (new — JP1, JP2, JP3)
│   ├── LinkedList.java / DoublyLinkedList.java
│   ├── MyStack.java
│   ├── MyQueue.java / CircularQueue.java
│   ├── MyDeque.java
│   ├── MyPriorityQueue.java (heap-backed)
│   ├── BST.java
│   ├── RedBlackTree.java
│   ├── MyHashMap.java (+ MySet on top of it)
│   ├── DisjointSet.java
│   └── Graph.java (adjacency list + matrix)
├── algorithms/          (new — JP4, with graph algorithms alongside JP3's Graph)
│   ├── SearchAlgorithms.java   (linear, binary)
│   ├── SortAlgorithms.java     (selection, insertion, merge, quicksort)
│   ├── GraphAlgorithms.java    (BFS, DFS, Dijkstra, Prim, Kruskal)
│   ├── GreedyAssignment.java
│   └── KnapsackDP.java
├── service/             (new — JP4, wires everything together)
│   └── AlgorithmTimer.java (records AlgorithmRun entries)
├── model/                (existing)
├── exception/            (existing)
└── db/                   (existing)
```

Class names above are suggestions, not mandates — keep them consistent
across the team once chosen, since DW2/DW3 will reference exact class
names in the report.

---

## 3. Assignment breakdown

### JP1 — Linear structures
- **Singly or doubly linked list**: `addFirst`, `addLast`, `insertAfter`,
  `remove`, and a working `Iterator` (implement `Iterable<T>` properly —
  the brief explicitly wants an "iterator demo," not just internal use).
- **Stack**: `push`, `pop`, `peek`, `isEmpty`. Use it for something real —
  e.g. an undo log for `ServiceRequest` status changes, feeding
  `audit_events`.
- **Queue + circular queue**: `enqueue`, `dequeue`, correct wrap-around
  handling on the circular version. This backs FIFO-rule dispatch.
- **Deque**: `addFront`, `addRear`, `removeFront`, `removeRear`. Use it
  for the "urgent request insertion" case the brief calls out — an urgent
  `ServiceRequest` can jump to the front instead of the back.
- **Tests**: normal case, boundary case (empty, single element), invalid
  input case (e.g. `removeFirst()` on empty list should throw, not crash
  silently) — for all four structures.

### JP2 — Priority queue, trees, hash table
- **Priority queue / heap**: `insert`, `extractMin`/`extractMax`,
  `heapify`. This is what actually drives urgency-based dispatch for
  `ServiceRequest.calculatePriority()` — right now that method just
  returns `urgency` with nothing consuming it; this heap is the consumer.
- **BST**: `insert`, `search`, inorder traversal. Use it as a search index
  over books (by ISBN or title) — ties into module M6.
- **Red-black tree** (or a clearly-documented simplified self-balancing
  tree — brief allows either): insertion with rotation/recoloring, with
  before/after diagrams for the report.
- **Hash table** (`MyHashMap`): `put`, `get`, `remove`, your own collision
  handling (chaining is simplest to implement and explain). Build `MySet`
  on top of it. This is what the empirical "hash table load factor"
  experiment (brief Section 9) will be timed against.
- **Tests**: same normal/boundary/invalid pattern, plus specifically
  collision-heavy inputs for the hash table (needed for the load-factor
  experiment's collision statistics).

### JP3 — Graph structure + disjoint set + graph algorithms
- **Graph**: both adjacency list and adjacency matrix representations,
  built from `Library` (nodes) and `Road` (edges) — this is the real
  library network, not an abstract example.
- **Disjoint set**: `makeSet`, `find`, `union` (by rank or size), path
  compression — needed for Kruskal's connectivity tracking.
- **BFS / DFS**: reachable-libraries-from-a-dispatch-point (brief's core
  question #3).
- **Dijkstra**: shortest route between two library branches, using
  `Road.getWeight("distance")` or `"time"` — already built, just needs a
  consumer. Produces a distance table + predecessor path for the report.
- **Prim and Kruskal**: minimum-cost network connecting all branches.
  Kruskal needs your disjoint set above for cycle detection.
- **Tests**: disconnected graph case, single-node case, and one deliberate
  precondition-violation case (e.g. negative edge weight fed to Dijkstra)
  for the counterexamples DW3 needs.

### JP4 — Search/sort, greedy, DP, and the timing harness
- **Linear + binary search**, both implemented and tested. State binary
  search's precondition explicitly (input must be sorted) — the brief
  wants this precondition tested with a violation case, not just the
  happy path.
- **Selection, insertion, merge sort, quicksort**, all from scratch.
- **Greedy algorithm**: priority-based resource assignment (e.g. assign
  available `Resource`s to pending `ServiceRequest`s by urgency, greedily
  taking the highest-value assignment first). Must include one
  documented counterexample showing greedy failing to find the optimal
  solution — this is a required rubric item, not optional polish.
- **Dynamic programming**: knapsack-style request/resource selection
  under a budget constraint (`Resource.cost`/`totalCost()` already exist
  for this). Provide a memoization or tabulation table plus solution
  reconstruction (which items were chosen), not just the optimal value.
- **`AlgorithmTimer`**: wraps any algorithm call, records
  `System.nanoTime()` before/after, estimates memory (or use
  `Runtime.getRuntime().totalMemory() - freeMemory()` as a rough proxy),
  and writes the result as an `AlgorithmRun` via a new
  `AlgorithmRunRepository` (ask DB2 for this once it exists, or write a
  minimal one yourself following `LibraryRepository`'s pattern — don't
  block on DB2 if they haven't gotten to it yet).
- **Runs the six required experiments** (brief Section 9) once the other
  three JPs' structures exist: search comparison, sorting comparison,
  hash table load factor, BST vs balanced tree, heap priority dispatch,
  graph algorithms — each at the brief's specified input sizes, each run
  3+ times, averaged.

---

## 4. Required algorithm parameters from index numbers

Brief Section 2 requires **at least three algorithm parameters derived
from team member index numbers** — e.g. priority weight, route penalty,
hash-table initial size, random seed, or budget constraint. Decide these
as a team today and document the derivation (e.g. "hash table initial
size = sum of last two digits of two members' index numbers, rounded up
to the next prime"). This is a quick decision but must not be skipped —
it's explicitly called out as an AI-resistance check.

---

## 5. Testing target

Brief requires **40+ unit tests minimum** across all structures/
algorithms. With 4 JPs, that's roughly 10 each if evenly split — but
JP3/JP4 have more surface area (graph algorithms + search/sort + greedy/DP
all need coverage), so lean toward JP1/JP2 covering slightly more of
their own simpler structures to balance total effort.

Every structure needs: **normal case, boundary case, invalid input
case** — minimum 3 tests per structure, more where it's cheap to add
(e.g. hash table collision cases).

---

## 6. How this connects to what already exists

- `ServiceRequest.calculatePriority()` currently returns `urgency` and
  nothing consumes it — JP2's priority queue is the first real consumer.
- `Road.getWeight(String)` already exists and already throws
  `InvalidWeightTypeException` — JP3's Dijkstra/Prim/Kruskal call this
  directly, no changes needed to `Road`.
- Repository `findAll()` calls (once DB2 finishes the remaining ones)
  return `List<Book>`, `List<Library>`, etc. — loading into your custom
  structures means iterating that list and calling your structure's
  `insert`/`add`, not using the `List` itself as the data structure.
- `AlgorithmRun` model already exists with exactly the fields
  `AlgorithmTimer` needs to populate (`algorithmName`, `inputSize`,
  `timeNs`, `memoryKb`, `dateRun`).

---

## 7. Evidence handoff to docs writers

Whenever you finish a structure or algorithm, ping DW2 (structures) or
DW3 (algorithms/correctness) immediately with:
- The actual console output from a test run (raw trace, not retyped)
- A one-line note on which real operational question this solves (e.g.
  "Dijkstra here answers 'fastest route between two branches'")

Don't wait until everything is done to hand off evidence — DW2/DW3 are
writing in parallel and need this as it lands, per `DOCS_WRITING.md`.