[33mcommit 66d094ad9c59bb211dbbac0baa34e6733526ad7b[m[33m ([m[1;36mHEAD[m[33m -> [m[1;32mmain[m[33m, [m[1;31morigin/main[m[33m, [m[1;31morigin/HEAD[m[33m)[m
Author: Samuellopez77 <slasumani@st.ug.edu.gh>
Date:   Thu Aug 20 19:11:29 2026 -0700

    feat: add remaining repository classes
    
    Add BookRepository, LibrarianRepository, PatronRepository,
    RoadRepository, ResourceRepository, ServiceRequestRepository,
    AlgorithmRunRepository, and AuditEventRepository — all 9 tables
    now have a repository, following LibraryRepository's save/findAll/
    findById pattern.
    
    Also add the missing AuditEvent model class (had a table in
    schema.sql but no corresponding Java class until now).
    
    ResourceRepository and ServiceRequestRepository handle enum<->TEXT
    conversion (.name() / .valueOf()) for their enum fields.

[33mcommit ed99223a6666fcebf883c7212ed972442d2e2a60[m
Author: Samuellopez77 <slasumani@st.ug.edu.gh>
Date:   Sun Aug 16 05:08:29 2026 -0700

    add uml diagram

[33mcommit 8d1e69f240dea5b0391d50be4492fbc6ed85e395[m
Author: Samuellopez77 <slasumani@st.ug.edu.gh>
Date:   Sun Aug 16 04:24:47 2026 -0700

    docs: add README
    
    Rewrite README.md with a plain-language system explanation,
    architecture summary, and current status table.

[33mcommit c6b1268b2cc16bbfa6a90d236ffe7ac757eec6a2[m
Author: Samuellopez77 <slasumani@st.ug.edu.gh>
Date:   Sat Aug 15 16:48:35 2026 -0700

    add java programming setup

[33mcommit 7485c14f011109c7f4b755181525b7a846f88205[m
Author: Samuellopez77 <slasumani@st.ug.edu.gh>
Date:   Fri Aug 14 19:43:06 2026 -0700

    rename file to prevent mismatch

[33mcommit 00803bc83b60b119016865c46a7dffb3872fdfd0[m
Author: Samuellopez77 <slasumani@st.ug.edu.gh>
Date:   Fri Aug 14 19:40:33 2026 -0700

    chore: stop tracking library.db (generated file, already gitignored)

[33mcommit 3f913b964dc56a7c9acb97c750c083ec87eb75ee[m
Author: Samuellopez77 <slasumani@st.ug.edu.gh>
Date:   Fri Aug 14 19:33:40 2026 -0700

    docs: add team plan, docs structure, and branching guide
    
    - Add TEAM_PLAN.md (previously drafted but never actually committed)
    - Update DOCS_WRITING.md with the doc/ folder structure (01-04,
      one per docs writer)
    - Add doc/01_overview_and_architecture/INSTRUCTIONS.md for DW1
    - Add BRANCHING.md covering per-person branch structure, daily
      merge cadence, and what to (not) commit

[33mcommit 1bb02f4a6a9ffe78f178a4e4c3d53dd103303070[m
Author: Keys <keys14052002@gmail.com>
Date:   Fri Aug 14 21:47:45 2026 +0000

    docs: edit readme file

[33mcommit 40b3d131b2f93dbac8b74dfa885d69a77f6829e1[m
Author: Samuellopez77 <slasumani@st.ug.edu.gh>
Date:   Fri Aug 14 12:52:43 2026 -0700

    chore: fix VS Code false-positive Java 17 errors
    
    Add .settings/org.eclipse.jdt.core.prefs to pin the compiler
    compliance level to 17 for this unmanaged project. Fixes the
    language server flagging text blocks as errors even though
    javac already compiles them fine.

[33mcommit c85cb9e77063a0e59e5221aaec1317f39fe195e1[m
Author: Samuellopez77 <slasumani@st.ug.edu.gh>
Date:   Fri Aug 14 12:10:34 2026 -0700

    feat: add SQLite database layer
    
    Add DatabaseManager (JDBC singleton, auto-creates tables on startup)
    and LibraryRepository (save/findAll/findById) as the pattern for the
    remaining repositories. Also pin the project to JDK 17 in VS Code
    settings so the editor stops flagging valid Java 17 syntax.
    
    Verified end-to-end against a live SQLite database.
    
    fix: correct exception class filenames
    
    Renamed BookUnavailableexception.java, Invalidweighttypeexeception.java,
    and Libraryconnectionexception.java to match their public class names
    exactly. Java requires this; the mismatch broke plain javac builds.

[33mcommit 7361e0a48d9c3064a8856207628beb81fc93b310[m
Author: Samuellopez77 <slasumani@st.ug.edu.gh>
Date:   Fri Aug 14 04:51:03 2026 -0700

    refactor: replace string fields with enums and custom exceptions
    
    - Add RequestStatus, RequestType, and ResourceType enums[cite: 1]
    - Add domain exceptions in new src/exception/ package[cite: 1]
    - Update Book, Road, ServiceRequest, Patron, and Resource models[cite: 1]

[33mcommit fe501c1d62371e6b6b3e52da00deebbd507c5fd8[m
Author: Keys <keys14052002@gmail.com>
Date:   Wed Aug 12 14:46:26 2026 +0000

    feat: define the algorithm run model

[33mcommit 3abe25d2c692ef0e98129331f4c235da9a642bb3[m
Author: Keys <keys14052002@gmail.com>
Date:   Wed Aug 12 12:13:52 2026 +0000

    feat: add methods to manage books and patrons in the library model

[33mcommit b5c60d2e51b0eed5fa8a0489ecc589a4363e0ae6[m
Author: Keys <keys14052002@gmail.com>
Date:   Wed Aug 12 12:11:49 2026 +0000

    feat: add methods to check if a book is available, mark a book as borrowed and returned

[33mcommit 97906c8fab6857b5ccadefc79397e8aa7086e155[m
Author: Keys <keys14052002@gmail.com>
Date:   Wed Aug 12 12:10:42 2026 +0000

    feat: add methods to get patron details and handle service requests

[33mcommit 7734461877ccf165ebe53cc1bbd9a0694a87339b[m
Author: Keys <keys14052002@gmail.com>
Date:   Wed Aug 12 12:01:22 2026 +0000

    feat: add methods to update a librarian email and get librarian details

[33mcommit 70ab98fd70724a85e8cbb6da1e93828ae4aecc4e[m
Author: Keys <keys14052002@gmail.com>
Date:   Wed Aug 12 11:59:06 2026 +0000

    feat: add a method to update service request status and calculate service request priority

[33mcommit 4f517388a2d7caa2a86253c125f07790fc54fcf1[m
Author: Keys <keys14052002@gmail.com>
Date:   Wed Aug 12 11:56:18 2026 +0000

    feat: add the method to get a road weight and the libraries the road connect

[33mcommit 73fc7bb30a98c887aa71a4c610e0230625762058[m
Author: Keys <keys14052002@gmail.com>
Date:   Wed Aug 12 11:36:52 2026 +0000

    feat: add a method to get a resource total cost and value

[33mcommit 692b90de1da4f542a4cac3f1a0073a463c69c645[m
Author: Keys <keys14052002@gmail.com>
Date:   Wed Aug 12 09:39:46 2026 +0000

    feat: define the resource model

[33mcommit 499531208d10aef609587e151c09bac92b9df571[m
Author: Keys <keys14052002@gmail.com>
Date:   Wed Aug 12 09:34:33 2026 +0000

    feat: define the road model

[33mcommit 1e424c9b41b48c4fca4da8e696138d66c3834407[m
Author: Keys <keys14052002@gmail.com>
Date:   Wed Aug 12 09:28:01 2026 +0000

    feat: define the service request model

[33mcommit e2b927730287dc8447805575315458d49ab7abd7[m
Author: Keys <keys14052002@gmail.com>
Date:   Wed Aug 12 09:26:27 2026 +0000

    feat: define the librarian model

[33mcommit cf995cd269adb9a810a382de2adcdbc2340166ec[m
Author: Keys <keys14052002@gmail.com>
Date:   Wed Aug 12 09:23:33 2026 +0000

    feat: define the patron model

[33mcommit e825535f1e15d390fbb8327e429700554fc7a415[m
Author: Keys <keys14052002@gmail.com>
Date:   Wed Aug 12 09:21:55 2026 +0000

    feat: define the book model

[33mcommit 88dff9bb165d6d4d2419cc5720746d6ebd404ba0[m
Author: Keys <keys14052002@gmail.com>
Date:   Wed Aug 12 09:17:41 2026 +0000

    feat: define the library model

[33mcommit d8e6a638ccb8b48d776b3937b7ef41d948d30b16[m
Author: Keys <keys14052002@gmail.com>
Date:   Mon Aug 3 22:10:51 2026 +0000

    chore: bootstrap project using singleton design pattern

[33mcommit 3be25a3b0edc3b26931590366282a4689016229f[m
Author: clifford kpeli DZIDZORM <cdkpeli@st.ug.edu.gh>
Date:   Fri Jul 31 20:35:44 2026 +0000

    Initial commit
