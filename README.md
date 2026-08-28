# Library Smart Service System

**DCIT 204/308 Joint Project — Ghana Smart Service Operations Optimizer**
Ghana context: Library / Records Office

---

## What this is

A library network management system for a set of library branches across
Ghana, connected by roads, each holding books, staffed by librarians, and
serving patrons who borrow, return, reserve, or renew books. The system
answers real operational questions:

- Which pending service request should be handled next — FIFO, urgency,
  or priority-based?
- What's the fastest route between two library branches?
- Which branches are reachable from a given dispatch point?
- Which requests/resources can be served under a budget or capacity
  constraint?
- How do different data structures and algorithms perform as the dataset
  grows?

## How a request flows through the system

1. A patron submits a request (e.g. "borrow this book") through the UI.
2. The UI hands it to the service layer, which creates a `ServiceRequest`
   with a status and urgency level.
3. The request joins a priority queue — urgent requests can jump ahead of
   others, not strictly first-come-first-served.
4. If the book lives at a different branch than the patron, a graph
   algorithm (Dijkstra) finds the fastest route between branches using
   the road network.
5. Every change is persisted to a SQLite database — nothing lives only in
   memory, so state survives a restart.
6. Algorithm run times/memory are logged for the performance analysis
   required by the project brief.

## Architecture

Four layers, each only talking to the one directly below it:

```
UI (Swing)
   ↓
Service layer  (orchestrates requests, decides what to call)
   ↓                              ↓
In-memory engine              Database access (JDBC)
(custom data structures            ↓
 and algorithms)                SQLite (library.db)
```

The database loads data into the custom data structures at startup;
algorithms then run entirely in memory (this is what gets timed for the
performance experiments); results get written back through the database
layer.

## Current status

| Layer | Status |
| --- | --- |
| Model classes (`Library`, `Book`, `Patron`, `ServiceRequest`, `Road`, `Resource`, `AlgorithmRun`, `Librarian`) | ✅ Done |
| Enums (`RequestStatus`, `RequestType`, `ResourceType`) | ✅ Done |
| Custom exceptions | ✅ Done |
| Database connection + schema (`DatabaseManager`) | ✅ Done |
| Repository classes (`LibraryRepository`, `BookRepository`, `PatronRepository`, `RoadRepository`, etc.) | ✅ Done |
| Custom data structures (LinkedList, Stack, Queue, MaxHeap, BST, Hash Table, Graph) | ✅ Done |
| Algorithms (Linear/BST Search, QuickSort, MergeSort, BFS/DFS, Dijkstra, Kruskal/Prim MST, Knapsack DP, Greedy) | ✅ Done |
| Service layer (`ApplicationController`) | ✅ Done |
| Web UI & Decision Support Dashboard | ✅ Done |
| Interactive Console CLI Menu | ✅ Done |
| Seed data (50 libraries, 100 roads, 50 books, 50 patrons) | ✅ Done |

## Project documentation

- **[`TEAM_PLAN.md`](./TEAM_PLAN.md)** — role assignments and day-by-day schedule
- **[`DATABASE_SETUP.md`](./DATABASE_SETUP.md)** — SQLite + JDBC setup, schema, repository pattern
- **[`JAVA_PROGRAMMING.md`](./JAVA_PROGRAMMING.md)** — data structure and algorithm assignments
- **[`DOCS_WRITING.md`](./DOCS_WRITING.md)** — technical report structure and ownership
- **[`BRANCHING.md`](./BRANCHING.md)** — git branching strategy

## Running the project (Web UI & Console)

No Maven/Gradle needed — this project compiles with standard `javac`. The SQLite JDBC driver is located in `lib/sqlite-jdbc-3.53.2.1.jar`.

### 1. Compile
```powershell
javac -cp "lib\sqlite-jdbc-3.53.2.1.jar" -d out (Get-ChildItem -Recurse -Filter *.java src -Exclude *Test*.java | ForEach-Object { $_.FullName })
```

*On macOS / Linux:*
```bash
javac -cp "lib/sqlite-jdbc-3.53.2.1.jar" -d out $(find src -name "*.java" ! -name "*Test*.java")
```

### 2. Run
```powershell
java -cp "out;lib\sqlite-jdbc-3.53.2.1.jar" Main
```

*On macOS / Linux:*
```bash
java -cp "out:lib/sqlite-jdbc-3.53.2.1.jar" Main
```

### 3. Access the Web UI
When you run the application:
1. **The Web UI automatically opens in your default browser** at **`http://localhost:8080`**.
2. **The interactive CLI menu** also runs simultaneously in your terminal.
3. Both interfaces share the same real-time database and in-memory engine.