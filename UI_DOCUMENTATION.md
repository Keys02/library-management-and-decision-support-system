# Library Smart Service & Decision Support System — UI Documentation

**DCIT 204 / 308 Joint Project — Comprehensive UI & System Specification**  
*Ghana Library & Operations Optimization Network*

---

## 1. System Overview & Architecture

The **Library Smart Service & Decision Support System** provides an interactive web-based dashboard and visualization suite built directly on top of the custom Java data structures, algorithms, and SQLite persistence layer.

### Architecture Highlights:
* **Zero External Web Framework Dependencies**: Powered by Java’s built-in `com.sun.net.httpserver.HttpServer` (`WebServer.java`).
* **Clean Single-Page Application (SPA)**: Pure HTML5, CSS3, and modern Vanilla JavaScript without external build tools (no Node.js/Webpack required).
* **Dual Execution Model**: The Web UI and Terminal CLI run **simultaneously**, sharing the exact same in-memory data structures (`Graph`, `BinarySearchTree`, `PriorityQueue`, `LinkedList`, `DynamicArray`) and SQLite database (`library.db`).
* **Real-Time Data Synchronization**: Any modification made in either interface (adding books, patrons, or processing requests) is immediately persisted and visible in both.

```
┌─────────────────────────────────────────────────────────────┐
│                       USER INTERFACE                        │
│   Web Dashboard (HTML5/CSS3/JS)   │   Terminal CLI (Console)│
└──────────────────────────────┬──────────────────────────────┘
                               │ HTTP REST API / Direct Invocation
┌──────────────────────────────▼──────────────────────────────┐
│                    APPLICATION CONTROLLER                   │
│          Central Orchestrator & State Synchronization       │
└──────────────┬──────────────────────────────┬───────────────┘
               │                              │
┌──────────────▼─────────────┐ ┌──────────────▼───────────────┐
│   IN-MEMORY DATA ENGINE    │ │       DATABASE LAYER         │
│ • Graph (Dijkstra/MST/BFS) │ │ • SQLite (library.db)        │
│ • MaxHeap (PriorityQueue)  │ │ • Repository Pattern         │
│ • Binary Search Tree (BST) │ │ • CRUD & Audit Persistence   │
│ • LinkedList & DynamicArray│ │                              │
└────────────────────────────┘ └──────────────────────────────┘
```

---

## 2. How to Launch the UI

### Step 1: Compile the Project
In PowerShell (Windows):
```powershell
javac -cp "lib\sqlite-jdbc-3.53.2.1.jar" -d out (Get-ChildItem -Recurse -Filter *.java src -Exclude *Test*.java | ForEach-Object { $_.FullName })
```
In Bash (macOS / Linux):
```bash
javac -cp "lib/sqlite-jdbc-3.53.2.1.jar" -d out $(find src -name "*.java" ! -name "*Test*.java")
```

### Step 2: Run the Application
In PowerShell (Windows):
```powershell
java -cp "out;lib\sqlite-jdbc-3.53.2.1.jar" Main
```
In Bash (macOS / Linux):
```bash
java -cp "out:lib/sqlite-jdbc-3.53.2.1.jar" Main
```

### Step 3: Accessing the Dashboard
* **Automatic Launch**: The application automatically launches your default web browser to `http://localhost:8080`.
* **Manual Access**: If your browser doesn't open automatically, open your browser and navigate to:
  **`http://localhost:8080`**

---

## 3. UI Navigation & Screen Breakdown

### 3.1 Dashboard & Operational Telemetry (`#view-dashboard`)
The central landing screen displays system-wide metrics and fast-action tools:
* **Metric Cards**:
  * **Total Books**: Number of books stored in `LinkedList` and indexed in the `BinarySearchTree`.
  * **Registered Patrons**: Active patron records.
  * **Connected Branches**: Ghana library branches loaded in the `Graph`.
  * **Pending Queue**: Number of unresolved requests in the `MaxHeap PriorityQueue`.
  * **Road Network Edges**: Inter-branch transit connections in the adjacency graph.
  * **Managed Resources**: Resources available for budgeting/allocation.
* **Live Priority Queue Widget**: Displays top 5 highest-priority requests waiting in the `MaxHeap`. Click **"Process Next"** to immediately dequeue and execute the highest-urgency task.
* **Quick Operations**: One-click shortcuts to add books, register patrons, submit service requests, or jump directly to algorithms.

---

### 3.2 Book Inventory & BST Catalog (`#view-books`)
* **Live Filtering**: Instant search bar filter that searches books by Title, Author, or ISBN as you type.
* **Tabular Display**: Shows Book ID, Title, Author, ISBN, Assigned Library Branch ID, and Availability Status (`Available` or `Borrowed`).
* **Add Book Modal (`#modal-book`)**: Form to register a new book into the system. Submitting updates SQLite and inserts the title into the in-memory `BinarySearchTree` index in $O(\log n)$ time.

---

### 3.3 Patron Directory (`#view-patrons`)
* **Patron Cards & Table**: Displays Patron ID, Full Name with deterministic avatar initials (`AD`, `KO`), Email address, and Phone number.
* **Search & Filter**: Real-time filtering by name, email, or contact number.
* **Register Patron Modal (`#modal-patron`)**: Add new library users directly into the system.

---

### 3.4 Service Requests Queue (`#view-requests`)
* **Urgency-Based Ordering**: Shows all requests prioritized by Urgency Metric ($1$ to $10$).
* **Urgency Color Indicators**:
  * High Urgency ($8-10$): Red badge
  * Medium Urgency ($5-7$): Amber badge
  * Standard Urgency ($1-4$): Blue badge
* **Request Lifecycle**: `PENDING` $\rightarrow$ `PROCESSING` $\rightarrow$ `COMPLETED` / `CANCELLED`.
* **Process Highest Urgency**: Dequeues the maximum element from the `PriorityQueue` with a single click and shows a notification toast.

---

### 3.5 Search & Sort Algorithm Lab (`#view-search-sort`)
A dedicated experimental playground to evaluate algorithm runtimes:
* **Search Comparison**:
  * **Linear Search $O(n)$**: Traverses the `LinkedList` sequentially.
  * **BST Search $O(\log n)$**: Traverses the `BinarySearchTree` index.
  * **Metrics Output**: Displays exact execution time in nanoseconds ($\text{ns}$) and milliseconds ($\text{ms}$) alongside all matching results.
* **Sorting Benchmarks**:
  * Runs in-memory sorting algorithms over book ID dynamic arrays:
    * **MergeSort**: $O(n \log n)$ stable divide-and-conquer.
    * **QuickSort**: $O(n \log n)$ partition-based sort.
    * **InsertionSort**: $O(n^2)$ comparison sort.
    * **SelectionSort**: $O(n^2)$ quadratic sort.
  * Displays item counts, nanosecond runtimes, and sample sorted outputs.

---

### 3.6 Inter-Library Graph Navigation (`#view-graph`)
Visualizes and computes route optimization across 50 Ghanaian library branches:
* **Dijkstra Shortest Route**:
  * Input Source Branch ID and Destination Branch ID.
  * Computes the fastest transit route, total estimated travel time (hours), and displays the step-by-step route sequence (e.g. `Branch #1 ➔ Branch #4 ➔ Branch #10`).
* **Minimum Spanning Tree (MST)**:
  * Compare **Kruskal's Algorithm** (Disjoint Set / Union-Find) vs **Prim's Algorithm** (Priority Queue).
  * Computes the minimum total distance to interconnect all branches without cycles, displaying total road kilometers and connecting segments.
* **Traversals (BFS & DFS)**:
  * Run **Breadth-First Search (BFS)** using `ArrayQueue` or **Depth-First Search (DFS)** using `LinkedStack` from any starting node to inspect connectivity.

---

### 3.7 Decision Support & Resource Allocation (`#view-decision`)
Simulates resource allocation under tight operational budgets:
* **Dynamic Budget Slider**: Adjust budget limit from $\$500$ to $\$25,000$.
* **0/1 Knapsack Dynamic Programming**: Computes the mathematically optimal combination of resource packages to maximize value score without exceeding budget ($O(n \cdot W)$ DP table).
* **Greedy Heuristic Comparison**: Selects resources by individual value score ($O(n \log n)$).
* **Comparison Cards**: Directly compares total value achieved and budget utilized between DP optimal vs Greedy heuristic.
* **Inventory Table**: Complete catalogue of resource equipment, categories, unit costs, quantities, and value scores.

---

### 3.8 Library Branch Network (`#view-libraries`)
* Displays all 50 regional library branches across Ghana, their physical city/region locations, and operating hours.

---

## 4. REST API Endpoint Reference

The backend `WebServer.java` exposes the following JSON endpoints:

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/stats` | System counts (books, patrons, libraries, pending queue, graph edges). |
| `GET` | `/api/books` | Returns all books as a JSON array. |
| `POST` | `/api/books` | Adds a new book `{title, author, isbn, libraryId}`. |
| `GET` | `/api/patrons` | Returns all registered patrons. |
| `POST` | `/api/patrons` | Registers a new patron `{name, email, phone}`. |
| `GET` | `/api/requests` | Returns all service requests. |
| `POST` | `/api/requests` | Submits a request `{patronId, bookId, requestType, urgency}`. |
| `POST` | `/api/requests/process` | Processes and dequeues the highest-urgency request. |
| `GET` | `/api/libraries` | Returns all regional library branches. |
| `GET` | `/api/roads` | Returns all inter-library road network edges. |
| `GET` | `/api/resources` | Returns the resource catalogue. |
| `GET` | `/api/search?type=bst&q=Title` | Runs BST ($O(\log n)$) or Linear ($O(n)$) search with timings. |
| `GET` | `/api/sort?algo=quicksort` | Runs QuickSort, MergeSort, InsertionSort, or SelectionSort. |
| `GET` | `/api/graph/dijkstra?src=1&dst=10`| Computes shortest route via Dijkstra's algorithm. |
| `GET` | `/api/graph/mst?algo=kruskal` | Computes Minimum Spanning Tree via Kruskal or Prim. |
| `GET` | `/api/graph/bfs?start=1` | Runs Breadth-First Search traversal. |
| `GET` | `/api/graph/dfs?start=1` | Runs Depth-First Search traversal. |
| `GET` | `/api/decision/knapsack?budget=5000` | Solves 0/1 Knapsack DP optimization. |
| `GET` | `/api/decision/greedy?budget=5000` | Solves Greedy resource allocation. |
| `POST` | `/api/seed` | Resets and reloads seed datasets from CSV files. |

---

## 5. Teammate & Multi-User Collaboration Guide

1. **Git Integration**: When teammates pull the repo (`git pull origin main`), all files (`web/index.html`, `web/styles.css`, `web/app.js`, `WebServer.java`) are ready immediately without extra setup.
2. **Port Handling**: Runs by default on port `8080`. If another instance was left running, terminate the old Java process or change the port parameter in `Application.java` (`new WebServer(applicationController, 8080)`).
3. **Database Portability**: The SQLite database file `library.db` is managed locally and seeded automatically from `data/*.csv` files on first launch.
