# Development Log

## 24 August 2026

### Contributor
Ivanawan666 — Documentation Writer (DW1)

### Progress
- Reviewed the project brief and repository documentation for the documentation-writing role.
- Selected the DW1 responsibility covering the report overview, problem statement, dataset description and system architecture.
- Created `doc/01_overview_and_architecture/draft.md`.
- Drafted the project cover-page framework.
- Wrote the problem statement, key decision-support questions, assumptions, input definitions, output definitions and system boundaries.
- Documented the current SQLite database structure using `schema.sql`.
- Added the dataset mapping, data dictionary, database relationships, dataset-size placeholders and data-provenance note.
- Documented the system architecture, domain model, entity relationships, database integration and algorithm/data-structure layer.
- Added the UML domain diagram reference and the M1–M10 module breakdown.
- Committed and pushed the DW1 documentation work to the `main` branch.

### Decisions
- Used the existing project files such as `TEAM_PLAN.md`, `DATABASE_SETUP.md`, `schema.sql`, `uml_diagram.jpg` and `DOCS_WRITING.md` as the source of truth for the documentation.
- Left unknown values such as final team-member names, final row counts, data-generation details and module completion percentages as placeholders instead of inventing them.
- Followed the repository rule that documentation writers commit directly to `main`.

### Challenges
- Some final documentation details depend on work that is still being completed by the database and programming teams.
- The current database schema has eight implemented tables while the documentation plan expects a future `audit_events` table.
- Final dataset row counts and data-provenance wording still require confirmation from DB2.

### Next Steps
- Update the report with final dataset row counts after seed data is completed.
- Confirm the final data-provenance wording with DB2.
- Replace team-name and team-member placeholders when the information is confirmed.
- Update module completion percentages as the project progresses.
- Review DW1 sections again before final report assembly.