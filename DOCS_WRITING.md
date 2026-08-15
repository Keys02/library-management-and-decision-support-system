# Documentation Drafting Plan — DW1–DW4

**Owners:** 4 Docs Writers (DW1–DW4)
**Deliverable:** Final technical report (PDF + DOCX), per brief Section 11
**Rule:** Start writing NOW, in parallel with the code — do not wait for
Track A/B/C to finish. Write what you can today; fill in evidence
(screenshots, trace tables, numbers) as each track produces it.

---

## 0. Folder structure

Create a `doc/` folder at the project root with one subfolder per writer.
Each writer works in their own numbered folder — drop in drafts, raw
notes, screenshots, and evidence as you collect them. Don't wait for
polished prose; a folder with rough notes and screenshots is more useful
to the team than an empty folder while you wait to write something "final."

```
doc/
├── 01_overview_and_architecture/     (DW1 — cover page, problem statement,
│                                       dataset description, architecture)
├── 02_data_structures/               (DW2 — data-structure implementation
│                                       section: diagrams + explanations)
├── 03_algorithms_and_correctness/    (DW3 — algorithm pseudocode, trace
│                                       tables, proof sketches,
│                                       counterexamples, responsible
│                                       algorithm selection)
└── 04_performance_and_integration/   (DW4 — performance analysis,
                                        database integration evidence,
                                        individual contributions,
                                        references)
```

Numbering matches the order these sections appear in the final report
(brief Section 11), so assembling the final PDF/DOCX at the end is just
concatenating the four folders in order, not reshuffling content.

Inside each folder, keep a simple convention: a `draft.md` for your
running prose, plus loose files for screenshots/CSVs/images
(`screenshot-01.png`, `trace-dijkstra.txt`, etc.) that get embedded into
the draft or the final document later. Don't worry about final formatting
inside these folders — that happens once, at assembly time, in whatever
tool produces the final DOCX/PDF.

---

## 1. Where evidence comes from

You are not inventing content — you are capturing and formatting evidence
that other tracks produce. Sit with (or stay in constant chat with) the
person you're shadowing so you catch trace tables, screenshots, and
numbers as they're generated, not after the fact when people have moved on
and forgotten the details.

| Writer | Shadows | Captures |
| --- | --- | --- |
| DW1 | Whole team, at the start | Problem statement, dataset plan, architecture (already drafted — see `TEAM_PLAN.md` and the architecture diagram) |
| DW2 | JP1, JP2 | Data-structure diagrams, implementation notes |
| DW3 | JP3, JP4 | Algorithm pseudocode, trace tables, proof sketches, counterexamples |
| DW4 | DB1, DB2, JP4's performance harness | Database screenshots, performance graphs, final assembly |

---

## 1. Report structure and ownership (maps to brief Section 11)

1. **Cover page** — title, team members, Ghana context (Library/Records
   Office), selected organisation/problem.
   **Owner: DW1.** Can be done today — no code dependency.

2. **Problem statement, assumptions, input-output definitions, system
   boundaries.**
   **Owner: DW1.** Draft today from the brief + `TEAM_PLAN.md`. Refine once
   `ApplicationController` wiring clarifies real system boundaries.

3. **Dataset description, data dictionary, database schema.**
   **Owner: DW1**, using `DATABASE_SETUP.md`'s schema section directly —
   it's already written in table form, just needs prose framing and the
   actual row counts once DB2's seed data is generated.

4. **System architecture and module design.**
   **Owner: DW1.** Use the architecture diagram already produced
   (UI → Service layer → in-memory engine / database access → SQLite).
   Describe each layer in 2-3 sentences; don't re-explain the diagram
   line by line.

5. **Data-structure implementation section — diagrams and explanations.**
   **Owner: DW2.** For each structure JP1/JP2 finish (linked list, stack,
   queue, deque, priority queue/heap, BST, red-black tree, hash table,
   disjoint set): one diagram + a short explanation of what operations
   were implemented and why the structure fits the use case (e.g. "priority
   queue backs the urgent-request dispatch order"). Do NOT write generic
   textbook explanations with no reference to our actual code — the brief
   explicitly penalizes this (Section 15).

6. **Algorithm implementation section — pseudocode and Java snippets.**
   **Owner: DW3.** For each algorithm (search/sort, BFS/DFS, Dijkstra,
   Prim/Kruskal, greedy, DP): pseudocode block + a short, real code
   snippet (not the whole file) + one sentence on why this algorithm
   suits this operational question.

7. **Correctness evidence — trace tables, invariants, proof sketches,
   edge-case tests.**
   **Owner: DW3.** Minimum required (brief Section 10):
   - 6 trace tables: binary search, insertion sort, merge sort/quicksort,
     Dijkstra, Kruskal/Prim, DP
   - 3 proof sketches: loop invariant, induction/recursion, greedy or DP
     correctness idea
   - 2 counterexamples: one greedy failure, one invalid precondition
     (e.g. unsorted binary search input)
   Get the RAW trace output from JP3/JP4 directly (console printouts are
   fine) — don't hand-simulate traces yourself, they must reflect the
   actual running code.

8. **Performance analysis — method, input sizes, tables, graphs,
   interpretation.**
   **Owner: DW4.** Pulls CSV output from the `algorithm_runs` table
   (populated by JP4's timing harness) once the six required experiments
   (brief Section 9) have been run at least 3 times each. Write one
   paragraph per experiment on whether the observed runtime matched the
   theoretical Big-O — mismatches are expected and fine to report
   honestly, that's what Section 9 asks for.

9. **Database integration evidence — schema, sample records, screenshots,
   run logs.**
   **Owner: DW4.** Screenshot the SQLite Explorer table view (same one
   used to verify `LibraryRepository` worked) for 2-3 populated tables.
   Include the actual `schema.sql`.

10. **Responsible algorithm selection — when each algorithm is
    appropriate vs. not.**
    **Owner: DW3.** One short paragraph per major algorithm choice — e.g.
    "Dijkstra assumes non-negative edge weights; if road conditions ever
    produced negative adjustments this would need Bellman-Ford instead."

11. **Individual contribution statement + oral-defense prep notes.**
    **Owner: DW4, but every team member fills in their own paragraph.**
    Send a simple template to all 15 members: name, role(s), what you
    built, which one data structure and one algorithm you personally can
    defend live (per brief Section 2's oral-defense requirement).

12. **References and appendices.**
    **Owner: DW1 or DW4** (whoever has capacity closer to submission) —
    use the brief's Section 16 reference list as the base.

---

## 2. Format requirements

- Final report: **PDF and DOCX**, per brief Section 12.
- Must include actual screenshots and trace tables, not only code
  listings (brief Section 8) — a wall of code with no evidence
  annotation will be marked as incomplete.
- Keep code snippets short and targeted; link to the full source (ZIP /
  repo) rather than pasting entire files into the report.

---

## 3. Immediate action items (today)

- **DW1:** Start cover page + problem statement + architecture section.
  Nothing blocks this — everything needed already exists in
  `TEAM_PLAN.md` and the architecture diagram.
- **DW2:** Get access to JP1/JP2's channel/branch now, so you're present
  the moment the first data structure lands.
- **DW3:** Same, but for JP3/JP4 — and specifically ask JP4 to print raw
  console output for every trace table as they build each algorithm,
  rather than relying on memory afterward.
- **DW4:** Draft the performance-analysis section headers and the
  database-integration section headers now (empty tables ready to fill),
  and prepare the individual-contribution template to send to all 15
  members today — this has the longest lead time since it depends on 15
  separate people responding.

---

## 4. Hard rule

Do not write generic, code-free explanations to "fill space." The brief's
AI-resistance section (15) specifically flags reports that could describe
any generic system rather than this specific one. Every section should
reference something concrete from our actual dataset, our actual code, or
our actual test output — not textbook definitions alone.