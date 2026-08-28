# AI Disclosure

Required by brief Section 15: "All AI assistance, if used, must be
acknowledged, and students must be able to explain and modify their own
implementation (with the supporting prompts used)."

## Tool used

Claude (Anthropic) — used by team members individually, primarily by the
Java/database track, in a chat interface (not an autonomous coding agent
committing directly to the repo). All AI-suggested code was reviewed,
understood, and committed by a human team member.

## Scope of AI assistance

- **Environment/tooling setup** — diagnosing a VS Code Java language
  server misconfiguration (false "text blocks unsupported" errors on a
  JDK 17 project) and a Windows PATH conflict between an Oracle JDK shim
  and Microsoft JDK 17. Fix: `.settings/org.eclipse.jdt.core.prefs` and
  `.vscode/settings.json` changes, both committed to this repo.
- **Database layer** — schema design (`schema.sql`, `DatabaseManager.java`)
  and the repository pattern (`src/repository/*`), using
  `LibraryRepository` as the first hand-written example that the rest of
  the repositories then followed.
- **Planning and coordination documents** — `TEAM_PLAN.md`,
  `DATABASE_SETUP.md`, `JAVA_PROGRAMMING.md`, `DOCS_WRITING.md`,
  `BRANCHING.md`, this file, and the `doc/0X_.../instructions.md` files
  guiding each documentation writer.
- **Code review** — reviewing teammate branches for issues such as
  filename/class-name mismatches (e.g. `BookUnavailableexception.java`
  vs. the `BookUnavailableException` class it declared) and
  package-naming inconsistencies (`repo/` vs. `repository/`) before merge.
- **Gap analysis against the project brief** — identifying missing
  pieces (e.g. no performance-timing harness yet, no counterexample
  evidence for the greedy algorithm, unused `Stack`/`audit_events` code)
  by comparing the current codebase against brief Sections 6, 9, and 10.

## What was NOT done by AI

- No AI tool has direct write access to this repository. Every commit in
  the git history was made by a named team member.
- Core data-structure and algorithm implementations (dynamic array,
  linked list, stack, queues, heap, BST, red-black tree, B-tree, hash
  table, disjoint set, graph traversal/Dijkstra/Prim/Kruskal, sorting,
  searching, greedy, DP) were written and are understood by the Java
  programmers responsible for them, per the oral-defense requirement in
  brief Section 2. AI assistance on these was limited to review/debugging
  suggestions, not first-draft authorship, unless a specific commit
  message says otherwise.

## Representative prompts used

- "Why is VS Code flagging this text block as a syntax error when javac
  compiles it fine?"
- "Review this repository class against our existing
  `LibraryRepository` pattern — does it handle the enum-to-TEXT
  conversion correctly?"
- "Compare our current codebase against Section 9 of the project brief —
  what's missing for the performance-analysis requirement?"
- "Draft instructions for the documentation writers covering the
  data-structures section, based on what's actually implemented so far."

## Verification

Every team member listed in the individual-contribution statement
(`doc/04_performance_and_integration/draft.md`, final report Section 11)
confirms they can explain and modify, unassisted, the specific data
structure and algorithm they list there — this is required for the oral
defense regardless of whether AI assistance touched that code during
development.
