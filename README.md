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
| Repository classes (`LibraryRepository` + 8 more) | 🔶 1 of 9 done |
| Custom data structures (linked list, stack, queue, heap, BST, hash table, graph, etc.) | ⬜ Not started |
| Algorithms (search, sort, BFS/DFS, Dijkstra, Prim/Kruskal, greedy, DP) | ⬜ Not started |
| Service layer (`ApplicationController`) | ⬜ Stub only, not wired |
| UI (Swing) | ⬜ Not started |
| Seed data (50+ libraries, 100+ roads, 300+ requests) | ⬜ Not started |

## Project documentation

- **[`TEAM_PLAN.md`](./TEAM_PLAN.md)** — role assignments and day-by-day
  schedule
- **[`DATABASE_SETUP.md`](./DATABASE_SETUP.md)** — SQLite + JDBC setup,
  schema, repository pattern
- **[`JAVA_PROGRAMMING.md`](./JAVA_PROGRAMMING.md)** — data structure and
  algorithm assignments
- **[`DOCS_WRITING.md`](./DOCS_WRITING.md)** — technical report structure
  and ownership
- **[`BRANCHING.md`](./BRANCHING.md)** — git branching strategy
- **[`docs/`](./docs)** — the actual report draft, one numbered folder
  per section

## Running the project

No Maven/Gradle — this project compiles with plain `javac`. The SQLite
JDBC driver jar must be present at `lib/sqlite-jdbc-3.53.2.1.jar` (already
committed to the repo).

```powershell
javac -cp lib\sqlite-jdbc-3.53.2.1.jar src\Main.java src\Application.java src\ApplicationController.java src\model\*.java src\exception\*.java src\db\*.java src\repo\*.java -d out
java -cp "out;lib\sqlite-jdbc-3.53.2.1.jar" Main
```

(`Main.java` currently has no functioning entry point wired up yet — see
`JAVA_PROGRAMMING.md` for what's still needed.)