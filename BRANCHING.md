# Git Branching Strategy

**Rule:** `main` must always compile. Nobody pushes broken code directly
to `main` — work happens on a branch, then merges in once it builds.

---

## Branch structure

```
main                                    (protected — always compiles)
├── java/jp1-linear-structures            linked list, stack, queue, deque
├── java/jp2-priority-heap-tree            priority queue/heap, BST,
│                                          red-black tree, hash table
├── java/jp3-graph                         adjacency list/matrix, BFS/DFS,
│                                          disjoint set, Dijkstra, Prim, Kruskal
├── java/jp4-search-sort-optimization      search/sort algorithms,
│                                          greedy, DP, timing harness
├── db/db1-schema-connection               DatabaseManager, first repository
│                                          (already merged — see note below)
├── db/db2-repositories                    remaining 8 repository classes
├── ui/ui1-main-menu                       one branch per Swing panel
├── ui/ui2-...  through ui/ui6-...
└── (docs writers commit directly to main — see note below)
```

**Why per-person, not per-role:** four Java programmers pushing to one
shared `java` branch means constant merge conflicts on the same files.
Isolating each person's branch means conflicts only surface once, at
merge time — not continuously.

---

## Merge cadence — more important than the branch names

`TEAM_PLAN.md` has Day 4 marked as "integration day." That does NOT mean
"merge everything for the first time on Day 4." That's how you end up
with four days of divergence colliding at once, the night before
submission.

**Merge into `main` at least once per day**, even if your structure/panel
isn't fully finished — as long as it compiles and doesn't break anything
already in `main`. Small, frequent merges are far less painful than one
giant merge at the end.

---

## Basic workflow (for anyone unfamiliar with branches)

```bash
git checkout main
git pull
git checkout -b java/jp1-linear-structures

# ... do your work, commit as normal ...

git add .
git commit -m "feat: implement custom linked list with iterator"
git push -u origin java/jp1-linear-structures
```

Then open a Pull Request into `main` on GitHub. Have at least one other
team member glance at it before merging — doesn't need to be a deep
review, just a second pair of eyes to catch obvious breakage.

---

## Docs writers — no branch needed

`doc/01_...` through `doc/04_...` are separate files/folders per writer.
Markdown conflicts are rare since you're not editing the same lines.
DW1–DW4 can commit directly to `main`, or to one shared `docs` branch if
the team prefers extra safety — branching overhead isn't worth it here,
save that discipline for the code.

---

## What NOT to commit

- `library.db` (or any `*.db` file) — generated locally, differs per
  machine/test run, causes noisy conflicts. Already added to `.gitignore`.
- `out/`, `bin/` — compiled `.class` output, regenerate anytime.

## What TO commit, even though it looks like a "binary blob"

- `lib/sqlite-jdbc-3.53.2.1.jar` — since this project has no Maven/Gradle,
  every teammate needs this exact jar available locally. Commit it once,
  to a `lib/` folder at the project root, so `git pull` gives everyone
  the dependency automatically instead of each person hunting it down on
  Maven Central individually.

---

## Heads-up: `.vscode/settings.json` has a machine-specific path in it

`java.configuration.runtimes` and `java.jdt.ls.java.home` in
`.vscode/settings.json` point at:
```
C:\Program Files\Microsoft\jdk-17.0.17.10-hotspot
```
That's where JDK 17 happened to install on the machine this was set up
on. If your JDK 17 installed somewhere else (different drive, different
version patch number, different install folder name), **update that path
locally after pulling** — don't assume it's broken just because it
doesn't match your install. If VS Code still shows the JDK 17
text-block false-errors after pulling this repo, check this path first
before troubleshooting anything else.