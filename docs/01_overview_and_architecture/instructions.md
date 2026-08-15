# Instructions — 01_overview_and_architecture

**Owner:** DW1
**Status:** Can start immediately — zero code dependency.
**Produces:** Report sections 1–4 (brief Section 11): cover page, problem
statement, dataset description, system architecture.

Save your working draft as `draft.md` in this folder. Drop supporting
images (the architecture diagram, any sketches) in this same folder as
`.png`/`.svg` files, named descriptively (`architecture-diagram.svg`, not
`image1.png`).

---

## 1. Cover page

Keep this short — one page equivalent. Include:
- Project title: **Ghana Smart Service Operations Optimizer**
- Team name
- Team members (all 15, with role(s) — pull from `TEAM_PLAN.md`'s
  generic labels once real names are assigned)
- Selected Ghana context: **Library / Records Office**
- Selected organisation/problem: a public library network across
  multiple Ghanaian branches, connected by a road network, handling
  book borrow/return/reserve/renew requests with urgency-based dispatch

This maps directly to the `Joint_DSA_Project_Checklist_Cover_Sheet.docx`
fields — fill those in too, they're the same information.

---

## 2. Problem statement, assumptions, input-output definitions, system boundaries

Write this as a short narrative, then a clean list. Use the brief's own
Section 3 questions as your structure — answer each one specifically for
our library context, not generically:

- **Which service request should be handled next** — under FIFO, urgency,
  and priority-based rules. (Ties to `ServiceRequest.calculatePriority()`
  and the priority queue JP2 is building.)
- **What is the fastest route from one library branch to another** — under
  weighted-road conditions. (Ties to `Road.java` and Dijkstra, JP3.)
- **Which branches are reachable from a given dispatch point** — BFS/DFS,
  JP3.
- **Which subset of requests/resources can be selected under a budget or
  capacity constraint** — greedy/DP, JP4.
- **How do alternative data structures/algorithms perform as data
  grows** — the empirical study, JP4 + DB1.
- **How does the system persist and reload data** — SQLite via JDBC, see
  `DATABASE_SETUP.md`.

**Assumptions** (state these explicitly — the brief rewards clear
precondition/edge-case thinking):
- Road weights (distance, travel time) are non-negative — this is why
  Dijkstra is valid here rather than Bellman-Ford.
- Each service request belongs to exactly one patron and references
  exactly one book.
- A book belongs to exactly one library branch at a time.

**System boundaries:** the system does not handle payment processing,
does not handle physical hardware (RFID scanners, etc.), and assumes
single-machine operation (no distributed/concurrent access) for this
project's scope.

---

## 3. Dataset description, data dictionary, database schema

Pull directly from `DATABASE_SETUP.md` Section 1 — the schema table and
`schema.sql` are already written. Your job here is to:

1. Copy the mapping table (brief's generic terms → our actual tables)
2. Copy the full `schema.sql` as an appendix or inline code block
3. Add a **data dictionary** table for each entity — one row per column,
   with type and a one-line description. Example for `service_requests`:

   | Column | Type | Description |
   | --- | --- | --- |
   | id | INTEGER | Primary key |
   | patron_id | INTEGER | References the patron who submitted the request |
   | book_id | INTEGER | References the requested book |
   | request_type | TEXT | One of BORROW / RETURN / RESERVE / RENEW |
   | urgency | INTEGER | Priority signal used for dispatch ordering |
   | status | TEXT | One of PENDING / PROCESSING / COMPLETED / CANCELLED |
   | created_at | TEXT | ISO-8601 timestamp |

   Do this for all 9 tables. Tedious but mechanical — can be done today
   without waiting on anyone.

4. **Row counts** — leave placeholders for now (`[TBD — pending DB2 seed
   data]`), fill in once DB2 finishes generating CSVs. Brief minimums to
   track against: 50 libraries, 100 roads, 300 service requests, 30
   resources, 30+ algorithm runs.

5. **Evidence note on data provenance** (brief Section 2 requires this):
   one paragraph explaining how the dataset was constructed — e.g.
   "Library branch names and areas are based on real Ghanaian towns/
   neighborhoods; specific patron/book records are synthetically
   generated to avoid exposing personal data, using a script that
   randomizes realistic combinations." Confirm actual wording with DB2
   once their generation script exists.

---

## 4. System architecture and module design

This is already visually done — the architecture diagram (4-layer:
UI → Service layer → in-memory engine ↔ database access → SQLite) was
produced earlier in this project's planning. Include that image here.

Write 2–3 sentences per layer, not a line-by-line narration of the
diagram:

- **User interface (Swing):** describes what the examiner sees and
  interacts with; never talks to the database or algorithms directly.
- **Service layer:** the single point every UI action routes through;
  decides which repository or in-memory operation to invoke.
- **In-memory engine:** the custom data structures and algorithms
  (Section 6/7 of the brief) — this is where the actual computation
  happens, and what the performance experiments measure.
- **Database access (JDBC):** loads seed data into the in-memory engine
  at startup, and persists changes (new requests, status updates,
  algorithm run logs) back out.

Then briefly describe the **module breakdown** (brief Section 5, M1–M10)
as a table: module name, one-line description, status (owner + rough
% complete, updated as the week progresses).

---

## 5. Checklist before marking this folder "done"

- [ ] Cover page fields all filled in, matches the checklist cover sheet
- [ ] All 6 problem-statement questions answered specifically for our context
- [ ] At least 3 explicit assumptions stated
- [ ] System boundaries stated
- [ ] Schema mapping table + full `schema.sql` included
- [ ] Data dictionary for all 9 tables
- [ ] Data provenance/evidence note included
- [ ] Architecture diagram embedded + layer descriptions
- [ ] Module breakdown table (M1–M10) included