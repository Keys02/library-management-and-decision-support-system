// Global App State
const API_BASE = '';
let state = {
    books: [],
    patrons: [],
    libraries: [],
    requests: [],
    resources: [],
    stats: {}
};

// ── Initialization ────────────────────────────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
    setupNavigation();
    fetchAllData();
    // Refresh stats periodically every 10 seconds
    setInterval(fetchStats, 10000);
});

// ── Navigation & View Switching ──────────────────────────────────────────
function setupNavigation() {
    const navItems = document.querySelectorAll('.nav-item');
    navItems.forEach(item => {
        item.addEventListener('click', () => {
            const view = item.getAttribute('data-view');
            switchView(view);
        });
    });
}

function switchView(viewName) {
    // Update nav classes
    document.querySelectorAll('.nav-item').forEach(item => {
        if (item.getAttribute('data-view') === viewName) {
            item.classList.add('active');
        } else {
            item.classList.remove('active');
        }
    });

    // Update section visibility
    document.querySelectorAll('.view-section').forEach(sec => {
        sec.classList.remove('active');
    });

    const targetSection = document.getElementById(`view-${viewName}`);
    if (targetSection) {
        targetSection.classList.add('active');
    }

    // Update Topbar Title
    const titleMap = {
        'dashboard': ['Dashboard & Overview', 'Operational telemetry and data structure analytics'],
        'books': ['Book Inventory & BST Catalog', 'View and manage all books stored in LinkedList and BST index'],
        'patrons': ['Patron Directory', 'Manage registered patrons and contact records'],
        'requests': ['Service Requests (MaxHeap Priority Queue)', 'Process requests prioritized by urgency metric (1-10)'],
        'search-sort': ['Search & Sort Algorithm Lab', 'Analyze time complexities and compare search & sort benchmarks in real-time'],
        'graph': ['Inter-Library Graph Navigation', 'BFS/DFS traversals, Dijkstra shortest routes, and Kruskal/Prim MST'],
        'decision': ['Decision Support System (Knapsack vs Greedy)', '0/1 Knapsack dynamic programming resource optimization against greedy heuristic'],
        'libraries': ['Library Branch Network (Ghana)', 'All connected regional library branches']
    };

    if (titleMap[viewName]) {
        document.getElementById('page-title').innerText = titleMap[viewName][0];
        document.getElementById('page-desc').innerText = titleMap[viewName][1];
    }
}

// ── Data Fetching ─────────────────────────────────────────────────────────
async function fetchAllData() {
    await Promise.all([
        fetchStats(),
        fetchBooks(),
        fetchPatrons(),
        fetchRequests(),
        fetchLibraries(),
        fetchResources()
    ]);
}

async function refreshAllData() {
    showToast('Refreshing system records...', 'info');
    await fetchAllData();
    showToast('System data synchronised.', 'success');
}

async function fetchStats() {
    try {
        const res = await fetch(`${API_BASE}/api/stats`);
        if (!res.ok) throw new Error('Failed to fetch stats');
        const data = await res.json();
        state.stats = data;

        document.getElementById('stat-books').innerText = data.books || 0;
        document.getElementById('stat-patrons').innerText = data.patrons || 0;
        document.getElementById('stat-libraries').innerText = data.libraries || 0;
        document.getElementById('stat-pending').innerText = data.pendingRequests || 0;
        document.getElementById('stat-edges').innerText = data.graphEdges || 0;
        document.getElementById('stat-resources').innerText = data.resources || 0;

        document.getElementById('badge-books').innerText = data.books || 0;
        document.getElementById('badge-patrons').innerText = data.patrons || 0;
        document.getElementById('badge-requests').innerText = data.pendingRequests || 0;

        document.getElementById('status-text').innerText = 'Engine Online (Port 8080)';
        document.querySelector('.status-dot').style.background = '#10b981';
    } catch (e) {
        document.getElementById('status-text').innerText = 'Connection Offline';
        document.querySelector('.status-dot').style.background = '#f43f5e';
    }
}

async function fetchBooks() {
    try {
        const res = await fetch(`${API_BASE}/api/books`);
        const books = await res.json();
        state.books = books;
        renderBooksTable(books);
    } catch (e) {
        console.error(e);
    }
}

async function fetchPatrons() {
    try {
        const res = await fetch(`${API_BASE}/api/patrons`);
        const patrons = await res.json();
        state.patrons = patrons;
        renderPatronsTable(patrons);
    } catch (e) {
        console.error(e);
    }
}

async function fetchRequests() {
    try {
        const res = await fetch(`${API_BASE}/api/requests`);
        const requests = await res.json();
        state.requests = requests;
        renderRequestsTable(requests);
        renderDashboardQueue(requests);
    } catch (e) {
        console.error(e);
    }
}

async function fetchLibraries() {
    try {
        const res = await fetch(`${API_BASE}/api/libraries`);
        const libs = await res.json();
        state.libraries = libs;
        renderLibrariesTable(libs);
    } catch (e) {
        console.error(e);
    }
}

async function fetchResources() {
    try {
        const res = await fetch(`${API_BASE}/api/resources`);
        const resources = await res.json();
        state.resources = resources;
        renderResourcesTable(resources);
    } catch (e) {
        console.error(e);
    }
}

// ── Rendering Tables ──────────────────────────────────────────────────────

function renderBooksTable(books) {
    const tbody = document.getElementById('books-table-body');
    if (!books || books.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6" style="text-align:center; color: var(--text-muted);">No books in library. Click "Seed Dataset" or "Add Book".</td></tr>`;
        return;
    }

    tbody.innerHTML = books.map(b => `
        <tr>
            <td><strong style="color:var(--primary-light); font-family:'Fira Code', monospace;">#${b.id}</strong></td>
            <td><strong>${escapeHtml(b.title)}</strong></td>
            <td style="color:var(--text-secondary);">${escapeHtml(b.author)}</td>
            <td><code>${escapeHtml(b.isbn)}</code></td>
            <td><span class="badge-tag" style="background:rgba(255,255,255,0.05); color:var(--text-secondary);">Branch #${b.libraryId}</span></td>
            <td>
                <span class="badge-tag ${b.available ? 'available' : 'borrowed'}">
                    ${b.available ? 'Available' : 'Borrowed'}
                </span>
            </td>
        </tr>
    `).join('');
}

function getInitials(name) {
    if (!name) return 'U';
    const parts = name.trim().split(/\s+/);
    if (parts.length === 1) return parts[0].substring(0, 2).toUpperCase();
    return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
}

function renderPatronsTable(patrons) {
    const tbody = document.getElementById('patrons-table-body');
    if (!patrons || patrons.length === 0) {
        tbody.innerHTML = `<tr><td colspan="4" style="text-align:center; color: var(--text-muted);">No patrons registered.</td></tr>`;
        return;
    }

    tbody.innerHTML = patrons.map(p => `
        <tr>
            <td><strong style="color:var(--accent-cyan); font-family:'Fira Code', monospace;">#${p.id}</strong></td>
            <td>
                <div class="patron-cell">
                    <div class="patron-avatar">${getInitials(p.name)}</div>
                    <strong>${escapeHtml(p.name)}</strong>
                </div>
            </td>
            <td>${escapeHtml(p.email)}</td>
            <td><code>${escapeHtml(p.phoneNumber)}</code></td>
        </tr>
    `).join('');
}

function renderRequestsTable(requests) {
    const tbody = document.getElementById('requests-table-body');
    if (!requests || requests.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align:center; color: var(--text-muted);">No service requests found.</td></tr>`;
        return;
    }

    tbody.innerHTML = requests.map(r => {
        const urgencyClass = r.urgency >= 8 ? 'urgency-high' : (r.urgency >= 5 ? 'urgency-med' : 'urgency-low');
        const statusClass = r.status.toLowerCase();
        return `
            <tr>
                <td><strong style="font-family:'Fira Code', monospace;">#${r.id}</strong></td>
                <td>Patron #${r.patronId}</td>
                <td>Book #${r.bookId}</td>
                <td><span style="font-weight:600; color:var(--accent-purple); font-size:12px;">${r.requestType}</span></td>
                <td>
                    <span class="urgency-indicator ${urgencyClass}">${r.urgency}</span>
                </td>
                <td><span class="badge-tag ${statusClass}">${r.status}</span></td>
                <td style="font-size:12px; color:var(--text-muted); font-family:'Fira Code', monospace;">${r.createdAt.replace('T', ' ')}</td>
            </tr>
        `;
    }).join('');
}

function renderDashboardQueue(requests) {
    const tbody = document.getElementById('dashboard-queue-body');
    const pending = requests.filter(r => r.status === 'PENDING').sort((a, b) => b.urgency - a.urgency);

    if (pending.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6" style="text-align:center; color: var(--text-muted);">Priority queue is currently empty.</td></tr>`;
        return;
    }

    tbody.innerHTML = pending.slice(0, 5).map(r => {
        const urgencyClass = r.urgency >= 8 ? 'urgency-high' : (r.urgency >= 5 ? 'urgency-med' : 'urgency-low');
        return `
            <tr>
                <td><strong style="font-family:'Fira Code', monospace;">#${r.id}</strong></td>
                <td>Patron #${r.patronId}</td>
                <td>Book #${r.bookId}</td>
                <td><span style="color:var(--accent-purple); font-weight:600; font-size:12px;">${r.requestType}</span></td>
                <td><span class="urgency-indicator ${urgencyClass}">${r.urgency}</span></td>
                <td><span class="badge-tag pending">PENDING</span></td>
            </tr>
        `;
    }).join('');
}

function renderLibrariesTable(libs) {
    const tbody = document.getElementById('libraries-table-body');
    if (!libs || libs.length === 0) {
        tbody.innerHTML = `<tr><td colspan="4" style="text-align:center;">No library branches loaded.</td></tr>`;
        return;
    }
    tbody.innerHTML = libs.map(l => `
        <tr>
            <td><strong style="color:var(--accent-emerald); font-family:'Fira Code', monospace;">Branch #${l.id}</strong></td>
            <td><strong>${escapeHtml(l.name)}</strong></td>
            <td>${escapeHtml(l.location)}</td>
            <td style="color:var(--text-secondary);">${escapeHtml(l.openHours)}</td>
        </tr>
    `).join('');
}

function renderResourcesTable(resources) {
    const tbody = document.getElementById('resources-table-body');
    if (!resources || resources.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;">No resources found.</td></tr>`;
        return;
    }
    tbody.innerHTML = resources.map(r => `
        <tr>
            <td style="font-family:'Fira Code', monospace;">#${r.id}</td>
            <td><strong>${escapeHtml(r.name)}</strong></td>
            <td><span class="badge-tag" style="background:rgba(99,102,241,0.1); color:var(--primary-light);">${r.type}</span></td>
            <td>$${r.cost.toFixed(2)}</td>
            <td>${r.quantity}</td>
            <td><strong style="color:var(--accent-cyan);">${r.value}</strong></td>
            <td>$${(r.cost * r.quantity).toFixed(2)}</td>
        </tr>
    `).join('');
}

// ── Filtering Tables ──────────────────────────────────────────────────────
function filterBooksTable() {
    const q = document.getElementById('books-filter-input').value.toLowerCase();
    const filtered = state.books.filter(b => 
        b.title.toLowerCase().includes(q) || 
        b.author.toLowerCase().includes(q) || 
        b.isbn.toLowerCase().includes(q)
    );
    renderBooksTable(filtered);
}

function filterPatronsTable() {
    const q = document.getElementById('patrons-filter-input').value.toLowerCase();
    const filtered = state.patrons.filter(p => 
        p.name.toLowerCase().includes(q) || 
        p.email.toLowerCase().includes(q) || 
        p.phoneNumber.includes(q)
    );
    renderPatronsTable(filtered);
}

// ── Modals & Form Submissions ─────────────────────────────────────────────
function openModal(id) {
    const el = document.getElementById(id);
    if (el) {
        el.classList.add('open');
        el.classList.add('show');
    }
}

function closeModal(id) {
    const el = document.getElementById(id);
    if (el) {
        el.classList.remove('open');
        el.classList.remove('show');
    }
}

async function submitAddBook(e) {
    e.preventDefault();
    const title = document.getElementById('new-book-title').value;
    const author = document.getElementById('new-book-author').value;
    const isbn = document.getElementById('new-book-isbn').value;
    const libraryId = document.getElementById('new-book-libid').value;

    try {
        const res = await fetch(`${API_BASE}/api/books`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ title, author, isbn, libraryId })
        });
        if (res.ok) {
            showToast(`Added book "${title}" successfully.`, 'success');
            closeModal('modal-book');
            document.getElementById('form-add-book').reset();
            await fetchBooks();
            await fetchStats();
        } else {
            showToast('Failed to add book.', 'error');
        }
    } catch (err) {
        showToast(err.message, 'error');
    }
}

async function submitAddPatron(e) {
    e.preventDefault();
    const name = document.getElementById('new-patron-name').value;
    const email = document.getElementById('new-patron-email').value;
    const phone = document.getElementById('new-patron-phone').value;

    try {
        const res = await fetch(`${API_BASE}/api/patrons`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, email, phone })
        });
        if (res.ok) {
            showToast(`Registered patron "${name}".`, 'success');
            closeModal('modal-patron');
            document.getElementById('form-add-patron').reset();
            await fetchPatrons();
            await fetchStats();
        } else {
            showToast('Failed to register patron.', 'error');
        }
    } catch (err) {
        showToast(err.message, 'error');
    }
}

async function submitAddRequest(e) {
    e.preventDefault();
    const patronId = document.getElementById('req-patron-id').value;
    const bookId = document.getElementById('req-book-id').value;
    const type = document.getElementById('req-type').value;
    const urgency = document.getElementById('req-urgency').value;

    try {
        const res = await fetch(`${API_BASE}/api/requests`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ patronId, bookId, requestType: type, urgency })
        });
        if (res.ok) {
            showToast(`Enqueued ${type} request with urgency ${urgency}.`, 'success');
            closeModal('modal-request');
            await fetchRequests();
            await fetchStats();
        } else {
            showToast('Failed to submit request.', 'error');
        }
    } catch (err) {
        showToast(err.message, 'error');
    }
}

async function processNextRequest() {
    try {
        const res = await fetch(`${API_BASE}/api/requests/process`, { method: 'POST' });
        const data = await res.json();
        if (data.success && data.processed) {
            const p = data.processed;
            showToast(`Processed Request #${p.id}: ${p.requestType} for Book #${p.bookId} (Urgency: ${p.urgency})`, 'success');
            await fetchRequests();
            await fetchStats();
        } else {
            showToast(data.message || 'No pending requests in queue', 'info');
        }
    } catch (e) {
        showToast('Error processing request: ' + e.message, 'error');
    }
}

// ── Search & Sort Algorithms ──────────────────────────────────────────────
async function executeSearch(type) {
    const q = document.getElementById('search-query-input').value.trim();
    const output = document.getElementById('search-output');
    output.innerHTML = `// Running ${type.toUpperCase()} search for "${escapeHtml(q)}"...`;

    try {
        const res = await fetch(`${API_BASE}/api/search?type=${type}&q=${encodeURIComponent(q)}`);
        const data = await res.json();

        let html = `Algorithm: ${data.type === 'bst' ? 'Binary Search Tree Index (O(log n))' : 'Linear Search (O(n))'}\n`;
        html += `Execution Time: ${data.executionTimeNs.toLocaleString()} ns (${(data.executionTimeNs / 1000000).toFixed(4)} ms)\n`;
        html += `Matches Found: ${data.results.length}\n\n`;

        if (data.results.length > 0) {
            html += `--- Matches ---\n`;
            data.results.slice(0, 8).forEach(b => {
                html += `[#${b.id}] "${b.title}" by ${b.author} (ISBN: ${b.isbn})\n`;
            });
            if (data.results.length > 8) html += `... and ${data.results.length - 8} more matches\n`;
        } else {
            html += `No matching records found for query "${q}".`;
        }
        output.innerText = html;
    } catch (e) {
        output.innerText = `Error: ${e.message}`;
    }
}

async function executeSort(algo) {
    const output = document.getElementById('sort-output');
    output.innerText = `// Benchmarking ${algo.toUpperCase()} on in-memory book IDs...`;

    try {
        const res = await fetch(`${API_BASE}/api/sort?algo=${algo}`);
        const data = await res.json();

        let html = `Algorithm: ${data.algorithm.toUpperCase()}\n`;
        html += `Item Count: ${data.count} elements\n`;
        html += `Execution Time: ${data.executionTimeNs.toLocaleString()} ns (${(data.executionTimeNs / 1000000).toFixed(4)} ms)\n\n`;
        html += `Sorted Output Sample:\n[ ${data.sortedIds.slice(0, 15).join(', ')}${data.sortedIds.length > 15 ? ' ...' : ''} ]`;

        output.innerHTML = html;
    } catch (e) {
        output.innerText = `Error: ${e.message}`;
    }
}

// ── Graph Algorithms ──────────────────────────────────────────────────────
async function runDijkstra() {
    const src = document.getElementById('dijkstra-src').value;
    const dst = document.getElementById('dijkstra-dst').value;
    const output = document.getElementById('dijkstra-output');
    output.innerText = `// Computing shortest route from Library #${src} to Library #${dst}...`;

    try {
        const res = await fetch(`${API_BASE}/api/graph/dijkstra?src=${src}&dst=${dst}`);
        const data = await res.json();

        if (data.isReachable) {
            let pathSequence = data.path.map(id => `Branch #${id}`).join(' ➔ ');
            output.innerHTML = `Shortest Travel Time: ${data.shortestTimeHours.toFixed(2)} hours\nPath Length: ${data.path.length} hops\n\nOptimal Route:\n${pathSequence}`;
        } else {
            output.innerText = `No connected road path found between Branch #${src} and Branch #${dst}.`;
        }
    } catch (e) {
        output.innerText = `Error: ${e.message}`;
    }
}

async function runMst(algo) {
    const output = document.getElementById('mst-output');
    output.innerText = `// Computing Minimum Spanning Tree using ${algo.toUpperCase()}...`;

    try {
        const res = await fetch(`${API_BASE}/api/graph/mst?algo=${algo}`);
        const data = await res.json();

        let html = `Algorithm: ${data.algorithm.toUpperCase()}\n`;
        html += `Total Spanning Distance: ${data.totalDistance.toFixed(2)} km\n`;
        html += `Total MST Roads: ${data.edgeCount} edges connecting all branches\n\n`;
        html += `--- First 6 Road Connections ---\n`;
        data.edges.slice(0, 6).forEach(e => {
            html += `Branch #${e.source} ⟷ Branch #${e.destination} (${e.distance.toFixed(1)} km, ${e.travelTime.toFixed(1)} hrs)\n`;
        });
        if (data.edges.length > 6) html += `... and ${data.edges.length - 6} more road segments\n`;

        output.innerText = html;
    } catch (e) {
        output.innerText = `Error: ${e.message}`;
    }
}

async function runTraversal(type) {
    const start = document.getElementById('traversal-start-id').value;
    const output = document.getElementById('traversal-output');
    output.innerText = `// Running ${type.toUpperCase()} from Branch #${start}...`;

    try {
        const res = await fetch(`${API_BASE}/api/graph/${type}?start=${start}`);
        const data = await res.json();

        let order = data.traversalOrder.slice(0, 20).join(' ➔ ');
        if (data.traversalOrder.length > 20) order += ` ➔ ... (${data.traversalOrder.length - 20} more nodes)`;

        output.innerHTML = `Traversal Method: ${type.toUpperCase()}\nTotal Visited Libraries: ${data.count} nodes\n\nOrder of Exploration:\n${order}`;
    } catch (e) {
        output.innerText = `Error: ${e.message}`;
    }
}

// ── Decision Support (Knapsack vs Greedy) ──────────────────────────────────
function updateBudgetDisplay(val) {
    document.getElementById('budget-val-display').innerText = `$${Number(val).toLocaleString()}`;
}

async function runKnapsack() {
    const budget = document.getElementById('budget-range').value;
    const container = document.getElementById('knapsack-results');
    container.innerHTML = `Computing optimal 0/1 knapsack combination...`;

    try {
        const res = await fetch(`${API_BASE}/api/decision/knapsack?budget=${budget}`);
        const data = await res.json();

        container.innerHTML = `
            <div style="display:flex; justify-content:space-between; margin-bottom:8px; font-weight:600;">
                <span>Total Value Score: <strong style="color:var(--accent-cyan); font-size:16px;">${data.totalValue}</strong></span>
                <span>Total Cost: <strong>$${data.totalCost.toFixed(2)}</strong> / $${data.budget}</span>
            </div>
            <div style="font-size:12px; color:var(--text-muted); margin-bottom:8px;">Selected ${data.itemCount} optimal resource packages:</div>
            <ul style="padding-left:18px; font-family: 'Fira Code', monospace; font-size:12px; max-height:160px; overflow-y:auto;">
                ${data.chosen.map(item => `<li>${escapeHtml(item.name)} — $${item.cost} (Value: ${item.value})</li>`).join('')}
            </ul>
        `;
    } catch (e) {
        container.innerText = `Error: ${e.message}`;
    }
}

async function runGreedy() {
    const budget = document.getElementById('budget-range').value;
    const container = document.getElementById('greedy-results');
    container.innerHTML = `Running greedy-by-value heuristic...`;

    try {
        const res = await fetch(`${API_BASE}/api/decision/greedy?budget=${budget}`);
        const data = await res.json();

        container.innerHTML = `
            <div style="display:flex; justify-content:space-between; margin-bottom:8px; font-weight:600;">
                <span>Total Value Score: <strong style="color:var(--accent-amber); font-size:16px;">${data.totalValue}</strong></span>
                <span>Total Cost: <strong>$${data.totalCost.toFixed(2)}</strong> / $${data.budget}</span>
            </div>
            <div style="font-size:12px; color:var(--text-muted); margin-bottom:8px;">Selected ${data.itemCount} items by highest individual value:</div>
            <ul style="padding-left:18px; font-family: 'Fira Code', monospace; font-size:12px; max-height:160px; overflow-y:auto;">
                ${data.chosen.map(item => `<li>${escapeHtml(item.name)} — $${item.cost} (Value: ${item.value})</li>`).join('')}
            </ul>
        `;
    } catch (e) {
        container.innerText = `Error: ${e.message}`;
    }
}

// ── Database Seeding ──────────────────────────────────────────────────────
async function seedDatabase() {
    if (!confirm('This will reload seed data from CSV files into the SQLite database. Continue?')) {
        return;
    }
    showToast('Seeding database from CSVs...', 'info');
    try {
        const res = await fetch(`${API_BASE}/api/seed`, { method: 'POST' });
        const data = await res.json();
        if (data.success) {
            showToast('Database seeded and refreshed successfully.', 'success');
            await fetchAllData();
        } else {
            showToast('Seed error: ' + data.error, 'error');
        }
    } catch (e) {
        showToast('Error seeding: ' + e.message, 'error');
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────
function showToast(message, type = 'info') {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `<span>${escapeHtml(message)}</span>`;
    container.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(50px)';
        toast.style.transition = 'all 0.3s ease-out';
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}

function escapeHtml(text) {
    if (!text) return '';
    return text.toString()
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}
