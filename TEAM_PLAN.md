# DCIT 204/308 Joint Project — Team Plan
**Context:** Library / Records Office (Ghana) — "Ghana Smart Service Operations Optimizer"
**Team size:** 15 | **Deadline:** This week (crunch schedule)
**Roles:** 4 Java Programmers (JP1–JP4), 2 Database Designers (DB1–DB2), 6 UI Designers (UI1–UI6), 4 Docs Writers (DW1–DW4)
*(Some members hold two roles — assign real names to the generic labels below.)*

---

## 0. Ground truth: where we are right now

Already built (do not redo):
- `model/` package: `Library`, `Book`, `Librarian`, `Patron`, `ServiceRequest`, `Road`, `Resource`, `AlgorithmRun`
- `model/RequestStatus`, `RequestType`, `ResourceType` enums
- `exception/` package: `BookUnavailableException`, `LibraryConnectionException`, `InvalidWeightTypeException`
- `Application` (singleton), `Main`, `ApplicationController` (stub, not wired)

Everything below builds **on top of** this. Nobody should touch the existing model classes without checking with the Java lead first — other work depends on their current shape.

---

## 1. Why this schedule looks the way it does

The brief's grading weight is concentrated in things we have not started: custom data structures (20 marks), algorithms (20 marks), database integration (10 marks), correctness/testing (15 marks), efficiency analysis (15 marks). UI is not separately marked in the rubric at all — it only matters because the brief requires "a console menu or simple GUI so an examiner can run demonstrations." **UI is a demo requirement, not a grading category.** Given the time left, data structures + algorithms + database + evidence come first; UI polish comes last and can run on stub data while the backend catches up.

---

## 2. Parallel workstreams (start simultaneously, Day 1)

### Track A — Custom Data Structures & Algorithms (JP1–JP4)
See `JAVA_PROGRAMMING.md` for the full breakdown. Summary of split:
- **JP1:** Linear structures — linked list, stack, queue, circular queue, deque
- **JP2:** Priority queue/heap, BST, red-black (or simplified balanced) tree, hash table
- **JP3:** Graph (adjacency list + matrix), disjoint set, BFS/DFS, Dijkstra, Prim, Kruskal
- **JP4:** Search/sort algorithms, greedy + DP, `AlgorithmRun` timing harness, service-layer wiring

### Track B — Database (DB1–DB2)
See `DATABASE_SETUP.md`. Summary:
- **DB1:** Schema design (`schema.sql`), table creation, connection manager
- **DB2:** CSV seed data generation (Ghana-localized, meets minimum record counts) + JDBC DAO classes

### Track C — UI (UI1–UI6)
See `UI_SETUP.md`. Summary: six Swing panels, each owned by one person, all built against a **stub service layer** so UI work doesn't block on Track A/B finishing.

### Track D — Documentation (DW1–DW4)
Start the report skeleton **today**, in parallel — do not wait for code to finish.
- **DW1:** Sections 1–3 (problem statement, dataset description, data dictionary, system architecture) — can be written now from this plan and the brief.
- **DW2:** Section 4–5 (data-structure implementation write-ups + diagrams) — shadows JP1/JP2, writes as they finish each structure.
- **DW3:** Section 6–7 (algorithm write-ups, pseudocode, correctness evidence: trace tables, proof sketches, counterexamples) — shadows JP3/JP4.
- **DW4:** Section 8–11 (performance analysis, database integration evidence, individual contributions, references) + assembles the final PDF/DOCX, coordinates screenshots and the demo video script.

**Docs writers should be in the same chat/channel as their shadowed programmers** so they capture trace tables and evidence as it's produced, not after the fact.

---

## 3. Day-by-day (adjust to your actual dates — treat "Day 1" as today)

**Day 1**
- JP1–JP4: scaffold empty classes for every assigned structure/algorithm with method signatures + unit test skeletons (compiles, not implemented). Push immediately so DW2/DW3 can see the shape.
- DB1: finalize `schema.sql`, create the SQLite file, verify tables via `sqlite3` CLI.
- DB2: start generating seed CSVs (script it — do not hand-type 300+ rows).
- UI1–UI6: scaffold `JFrame` + empty panels per `UI_SETUP.md`, wired to a stub service returning hardcoded data.
- DW1: draft sections 1–3.

**Day 2**
- JP1–JP2: finish and unit-test all structures assigned. Push.
- JP3: finish graph representation + BFS/DFS. Dijkstra/Prim/Kruskal in progress.
- JP4: finish all six sort/search algorithms + tests.
- DB1: connection manager + first DAO (e.g. `LibraryDao`) working end-to-end against real data.
- DB2: finish seed CSVs, hand off row counts to DW1 for the data dictionary.
- DW2: writing up JP1/JP2 structures as they land.

**Day 3**
- JP3: finish Dijkstra/Prim/Kruskal + disjoint set. Push.
- JP4: greedy + DP implementation, including the required greedy counterexample.
- DB1+DB2: remaining DAOs finished; full CSV import path working.
- UI1–UI6: swap stub service calls for real service-layer calls as Track A/B land.
- DW3: trace tables for binary search, insertion sort, merge/quicksort as JP4 finishes them.

**Day 4**
- All: integration day. Wire `ApplicationController` end-to-end: DB load → data structures → algorithms → UI display.
- JP4 + DB1: performance harness — run the six required experiments (Section 9 of brief), log to `algorithm_runs` table/CSV.
- DW3: remaining trace tables (Dijkstra, Kruskal/Prim, DP) + proof sketches + counterexamples.
- DW4: plug in performance graphs, database screenshots.

**Day 5 (submission)**
- Morning: full run-through by someone who didn't write the code (catches integration bugs and doubles as oral-defense rehearsal).
- Freeze code by midday. No new features — only bug fixes.
- DW4: final assembly, PDF + DOCX export, ZIP/repo export, record the demo video (5–8 min, per Section 12).
- Everyone: read your own section of the report — every member must be able to explain one data structure and one algorithm live.

---

## 4. Submission checklist (map to Section 12 of the brief)

| Item | Owner | Format |
| --- | --- | --- |
| Source code | JP1–JP4 (assembled by Java lead) | Git export / ZIP, with README |
| Database scripts | DB1–DB2 | `schema.sql` + seed CSVs |
| Technical report | DW1–DW4 | PDF + DOCX |
| Performance results | JP4 + DB1 | CSV + graphs |
| Demo video | DW4 (records), UI team (drives the demo) | 5–8 min |
| Oral defense | Everyone | Each person explains one structure + one algorithm |

---

## 5. Hard rule to avoid losing marks

Do **not** use `java.util.HashMap`, `PriorityQueue`, `Stack`, `ArrayDeque`, `TreeMap`, or similar built-ins anywhere in graded logic. Built-ins are fine for file I/O, JDBC, and test scaffolding only. If a JP accidentally imports one to save time under pressure, it will look like exactly what the "AI-resistance" section of the brief is checking for — flag it in code review before merging.