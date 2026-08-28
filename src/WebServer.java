import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import datastructures.graph.DijkstraResult;
import datastructures.linear.DynamicArray;
import datastructures.linear.LinkedList;
import model.*;
import algorithms.Sorting;
import algorithms.Searching;
import algorithms.DynamicProgramming;
import algorithms.Greedy;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class WebServer {

    private final ApplicationController controller;
    private final int port;
    private HttpServer server;

    public WebServer(ApplicationController controller, int port) {
        this.controller = controller;
        this.port = port;
    }

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.setExecutor(Executors.newFixedThreadPool(10));

            // Static files & root
            server.createContext("/", new StaticFileHandler());

            // API Endpoints
            server.createContext("/api/stats", new StatsHandler());
            server.createContext("/api/libraries", new LibrariesHandler());
            server.createContext("/api/books", new BooksHandler());
            server.createContext("/api/patrons", new PatronsHandler());
            server.createContext("/api/requests", new RequestsHandler());
            server.createContext("/api/requests/process", new ProcessRequestHandler());
            server.createContext("/api/resources", new ResourcesHandler());
            server.createContext("/api/roads", new RoadsHandler());
            server.createContext("/api/search", new SearchHandler());
            server.createContext("/api/sort", new SortHandler());
            server.createContext("/api/graph/bfs", new BfsHandler());
            server.createContext("/api/graph/dfs", new DfsHandler());
            server.createContext("/api/graph/dijkstra", new DijkstraHandler());
            server.createContext("/api/graph/mst", new MstHandler());
            server.createContext("/api/decision/knapsack", new KnapsackHandler());
            server.createContext("/api/decision/greedy", new GreedyHandler());
            server.createContext("/api/seed", new SeedHandler());

            server.start();
            System.out.println("==================================================================");
            System.out.println(" [WebServer] UI is LIVE at: http://localhost:" + port);
            System.out.println(" Automatically launching browser at http://localhost:" + port + " ...");
            System.out.println("==================================================================");

            // Attempt to automatically open the default browser for the user
            openBrowser("http://localhost:" + port);
        } catch (IOException e) {
            System.err.println("[WebServer] Failed to start HTTP server on port " + port + ": " + e.getMessage());
        }
    }

    private void openBrowser(String url) {
        new Thread(() -> {
            try {
                Thread.sleep(500); // Allow server to bind cleanly
                if (java.awt.Desktop.isDesktopSupported() && java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)) {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                    return;
                }
            } catch (Throwable ignored) {}

            // OS Fallbacks
            String os = System.getProperty("os.name", "").toLowerCase();
            try {
                if (os.contains("win")) {
                    new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", url).start();
                } else if (os.contains("mac")) {
                    new ProcessBuilder("open", url).start();
                } else if (os.contains("nix") || os.contains("nux")) {
                    new ProcessBuilder("xdg-open", url).start();
                }
            } catch (Throwable ignored) {}
        }).start();
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    // ── HTTP Utilities ────────────────────────────────────────────────────────

    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static String readRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }
            return baos.toString(StandardCharsets.UTF_8);
        }
    }

    private static Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query == null || query.isEmpty()) return params;
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            try {
                if (idx > 0) {
                    String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8.name());
                    String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8.name());
                    params.put(key, value);
                } else if (idx < 0) {
                    params.put(URLDecoder.decode(pair, StandardCharsets.UTF_8.name()), "");
                }
            } catch (Exception ignored) {}
        }
        return params;
    }

    private static Map<String, String> parseSimpleJson(String json) {
        Map<String, String> map = new HashMap<>();
        if (json == null) return map;
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1).trim();
        }
        if (json.isEmpty()) return map;

        StringBuilder key = new StringBuilder();
        StringBuilder val = new StringBuilder();
        boolean inQuotes = false;
        boolean parsingKey = true;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '\"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inQuotes = !inQuotes;
                continue;
            }
            if (!inQuotes && c == ':') {
                parsingKey = false;
                continue;
            }
            if (!inQuotes && c == ',') {
                String k = key.toString().trim().replaceAll("^\"|\"$", "");
                String v = val.toString().trim().replaceAll("^\"|\"$", "");
                if (!k.isEmpty()) map.put(k, v);
                key.setLength(0);
                val.setLength(0);
                parsingKey = true;
                continue;
            }
            if (parsingKey) {
                key.append(c);
            } else {
                val.append(c);
            }
        }
        String k = key.toString().trim().replaceAll("^\"|\"$", "");
        String v = val.toString().trim().replaceAll("^\"|\"$", "");
        if (!k.isEmpty()) map.put(k, v);
        return map;
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '\"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < ' ') {
                        String hex = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(hex.substring(hex.length() - 4));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.toString();
    }

    // ── Handlers ──────────────────────────────────────────────────────────────

    private class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path == null || path.equals("/") || path.isEmpty()) {
                path = "/index.html";
            }

            String cleanPath = path.startsWith("/") ? path.substring(1) : path;
            File file = null;
            String[] candidateDirs = { "web", "ui", "library-management-and-decision-support-system/web", "../web", "../../web" };
            for (String dir : candidateDirs) {
                File candidate = new File(dir, cleanPath);
                if (candidate.exists() && !candidate.isDirectory()) {
                    file = candidate;
                    break;
                }
            }

            if (file.exists() && !file.isDirectory()) {
                String contentType = "text/html; charset=UTF-8";
                if (path.endsWith(".css")) contentType = "text/css; charset=UTF-8";
                else if (path.endsWith(".js")) contentType = "application/javascript; charset=UTF-8";
                else if (path.endsWith(".svg")) contentType = "image/svg+xml";
                else if (path.endsWith(".json")) contentType = "application/json; charset=UTF-8";
                else if (path.endsWith(".png")) contentType = "image/png";
                else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) contentType = "image/jpeg";

                byte[] data = Files.readAllBytes(file.toPath());
                exchange.getResponseHeaders().set("Content-Type", contentType);
                exchange.sendResponseHeaders(200, data.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(data);
                }
            } else {
                String notFound = "<h1>404 Not Found</h1>";
                exchange.sendResponseHeaders(404, notFound.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(notFound.getBytes(StandardCharsets.UTF_8));
                }
            }
        }
    }

    private class StatsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            int libCount = controller.getLibraries() != null ? controller.getLibraries().size() : 0;
            int bookCount = controller.getBooks() != null ? controller.getBooks().size() : 0;
            int patronCount = controller.getPatrons() != null ? controller.getPatrons().size() : 0;
            int resCount = controller.getResources() != null ? controller.getResources().size() : 0;
            int vCount = controller.getGraph() != null ? controller.getGraph().vertexCount() : 0;
            int eCount = controller.getGraph() != null ? controller.getGraph().edgeCount() : 0;
            int pendingCount = controller.getRequestQueue() != null ? controller.getRequestQueue().size() : 0;

            String json = String.format("""
                {
                    "libraries": %d,
                    "books": %d,
                    "patrons": %d,
                    "resources": %d,
                    "graphVertices": %d,
                    "graphEdges": %d,
                    "pendingRequests": %d
                }
                """, libCount, bookCount, patronCount, resCount, vCount, eCount, pendingCount);

            sendJsonResponse(exchange, 200, json);
        }
    }

    private class LibrariesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            LinkedList<Library> libs = controller.getLibraries();
            StringBuilder sb = new StringBuilder("[");
            if (libs != null) {
                for (int i = 0; i < libs.size(); i++) {
                    Library l = libs.get(i);
                    if (i > 0) sb.append(",");
                    sb.append(String.format("""
                        {"id":%d,"name":"%s","location":"%s","openHours":"%s"}
                        """, l.getId(), escapeJson(l.getLibraryName()), escapeJson(l.getLocation()), escapeJson(l.getOpenHours())));
                }
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private class BooksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            if ("OPTIONS".equalsIgnoreCase(method)) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            if ("POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                Map<String, String> data = parseSimpleJson(body);
                String title = data.getOrDefault("title", "").trim();
                String author = data.getOrDefault("author", "").trim();
                String isbn = data.getOrDefault("isbn", "").trim();
                int libraryId = 1;
                try {
                    libraryId = Integer.parseInt(data.getOrDefault("libraryId", "1").trim());
                } catch (NumberFormatException ignored) {}

                if (title.isEmpty() || author.isEmpty()) {
                    sendJsonResponse(exchange, 400, "{\"error\":\"Title and author are required\"}");
                    return;
                }

                Book book = new Book(0, title, author, isbn.isEmpty() ? "978-0000000000" : isbn, true, libraryId);
                controller.addNewBook(book);
                sendJsonResponse(exchange, 201, "{\"success\":true,\"message\":\"Book added successfully\"}");
                return;
            }

            // GET
            LinkedList<Book> books = controller.getBooks();
            StringBuilder sb = new StringBuilder("[");
            if (books != null) {
                for (int i = 0; i < books.size(); i++) {
                    Book b = books.get(i);
                    if (i > 0) sb.append(",");
                    sb.append(String.format("""
                        {"id":%d,"title":"%s","author":"%s","isbn":"%s","available":%b,"libraryId":%d}
                        """, b.getId(), escapeJson(b.getTitle()), escapeJson(b.getAuthor()), escapeJson(b.getIsbn()), b.isAvailable(), b.getLibraryId()));
                }
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private class PatronsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            if ("OPTIONS".equalsIgnoreCase(method)) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            if ("POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                Map<String, String> data = parseSimpleJson(body);
                String name = data.getOrDefault("name", "").trim();
                String email = data.getOrDefault("email", "").trim();
                String phone = data.getOrDefault("phoneNumber", data.getOrDefault("phone", "")).trim();

                if (name.isEmpty()) {
                    sendJsonResponse(exchange, 400, "{\"error\":\"Name is required\"}");
                    return;
                }

                Patron patron = new Patron(0, name, email.isEmpty() ? "user@example.com" : email, phone.isEmpty() ? "0240000000" : phone);
                controller.addNewPatron(patron);
                sendJsonResponse(exchange, 201, "{\"success\":true,\"message\":\"Patron registered successfully\"}");
                return;
            }

            // GET
            LinkedList<Patron> patrons = controller.getPatrons();
            StringBuilder sb = new StringBuilder("[");
            if (patrons != null) {
                for (int i = 0; i < patrons.size(); i++) {
                    Patron p = patrons.get(i);
                    if (i > 0) sb.append(",");
                    sb.append(String.format("""
                        {"id":%d,"name":"%s","email":"%s","phoneNumber":"%s"}
                        """, p.getId(), escapeJson(p.getName()), escapeJson(p.getEmail()), escapeJson(p.getPhoneNumber())));
                }
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private class RequestsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            if ("OPTIONS".equalsIgnoreCase(method)) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            if ("POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                Map<String, String> data = parseSimpleJson(body);
                int patronId = 1, bookId = 1, urgency = 5;
                try {
                    patronId = Integer.parseInt(data.getOrDefault("patronId", "1").trim());
                    bookId = Integer.parseInt(data.getOrDefault("bookId", "1").trim());
                    urgency = Integer.parseInt(data.getOrDefault("urgency", "5").trim());
                } catch (NumberFormatException ignored) {}

                String typeStr = data.getOrDefault("requestType", data.getOrDefault("type", "BORROW")).trim().toUpperCase();
                RequestType type;
                try {
                    type = RequestType.valueOf(typeStr);
                } catch (Exception e) {
                    type = RequestType.BORROW;
                }

                ServiceRequest req = new ServiceRequest(0, patronId, bookId, type, urgency, RequestStatus.PENDING, LocalDateTime.now());
                controller.submitNewRequest(req);
                sendJsonResponse(exchange, 201, "{\"success\":true,\"message\":\"Service request submitted\"}");
                return;
            }

            // GET all requests
            LinkedList<ServiceRequest> requests = controller.getRequestRepo().findAll();
            StringBuilder sb = new StringBuilder("[");
            if (requests != null) {
                for (int i = 0; i < requests.size(); i++) {
                    ServiceRequest r = requests.get(i);
                    if (i > 0) sb.append(",");
                    sb.append(String.format("""
                        {"id":%d,"patronId":%d,"bookId":%d,"requestType":"%s","urgency":%d,"status":"%s","createdAt":"%s"}
                        """, r.getId(), r.getPatronId(), r.getBookId(), r.getRequestType().name(), r.getUrgency(), r.getStatus().name(), r.getCreatedAt().toString()));
                }
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private class ProcessRequestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            ServiceRequest processed = controller.processNextRequest();
            if (processed != null) {
                String json = String.format("""
                    {"success":true,"processed":{"id":%d,"patronId":%d,"bookId":%d,"requestType":"%s","urgency":%d,"status":"%s"}}
                    """, processed.getId(), processed.getPatronId(), processed.getBookId(), processed.getRequestType().name(), processed.getUrgency(), processed.getStatus().name());
                sendJsonResponse(exchange, 200, json);
            } else {
                sendJsonResponse(exchange, 200, "{\"success\":false,\"message\":\"No pending requests in queue\"}");
            }
        }
    }

    private class ResourcesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            LinkedList<Resource> res = controller.getResources();
            StringBuilder sb = new StringBuilder("[");
            if (res != null) {
                for (int i = 0; i < res.size(); i++) {
                    Resource r = res.get(i);
                    if (i > 0) sb.append(",");
                    sb.append(String.format("""
                        {"id":%d,"name":"%s","type":"%s","cost":%.2f,"quantity":%d,"value":%d}
                        """, r.getId(), escapeJson(r.getName()), r.getType().name(), r.getCost(), r.getQuantity(), r.getValue()));
                }
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private class RoadsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            LinkedList<Road> roads = controller.getRoadRepo().findAll();
            StringBuilder sb = new StringBuilder("[");
            if (roads != null) {
                for (int i = 0; i < roads.size(); i++) {
                    Road r = roads.get(i);
                    if (i > 0) sb.append(",");
                    sb.append(String.format("""
                        {"id":%d,"sourceLibraryId":%d,"destinationLibraryId":%d,"distance":%.2f,"travelTime":%.2f}
                        """, r.getId(), r.getSourceLibraryId(), r.getDestinationLibraryId(), r.getDistance(), r.getTravelTime()));
                }
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private class SearchHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            String query = queryParams.getOrDefault("q", "").trim();
            String type = queryParams.getOrDefault("type", "linear").trim().toLowerCase();

            long startTime = System.nanoTime();
            StringBuilder resultsJson = new StringBuilder("[");
            boolean found = false;

            if ("bst".equals(type)) {
                found = controller.getBookIndex().contains(query);
                if (found) {
                    LinkedList<Book> all = controller.getBooks();
                    for (int i = 0; i < all.size(); i++) {
                        Book b = all.get(i);
                        if (b.getTitle().equalsIgnoreCase(query)) {
                            resultsJson.append(String.format("""
                                {"id":%d,"title":"%s","author":"%s","isbn":"%s","available":%b,"libraryId":%d}
                                """, b.getId(), escapeJson(b.getTitle()), escapeJson(b.getAuthor()), escapeJson(b.getIsbn()), b.isAvailable(), b.getLibraryId()));
                            break;
                        }
                    }
                }
            } else {
                LinkedList<Book> all = controller.getBooks();
                int count = 0;
                if (all != null) {
                    for (int i = 0; i < all.size(); i++) {
                        Book b = all.get(i);
                        if (query.isEmpty() || b.getTitle().toLowerCase().contains(query.toLowerCase()) || b.getAuthor().toLowerCase().contains(query.toLowerCase())) {
                            if (count > 0) resultsJson.append(",");
                            resultsJson.append(String.format("""
                                {"id":%d,"title":"%s","author":"%s","isbn":"%s","available":%b,"libraryId":%d}
                                """, b.getId(), escapeJson(b.getTitle()), escapeJson(b.getAuthor()), escapeJson(b.getIsbn()), b.isAvailable(), b.getLibraryId()));
                            count++;
                            found = true;
                        }
                    }
                }
            }
            resultsJson.append("]");
            long elapsedNs = System.nanoTime() - startTime;

            String response = String.format("""
                {
                    "type": "%s",
                    "query": "%s",
                    "found": %b,
                    "executionTimeNs": %d,
                    "results": %s
                }
                """, type, escapeJson(query), found, elapsedNs, resultsJson.toString());

            sendJsonResponse(exchange, 200, response);
        }
    }

    private class SortHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            String algo = queryParams.getOrDefault("algo", "mergesort").trim().toLowerCase();

            LinkedList<Book> books = controller.getBooks();
            DynamicArray<Integer> ids = new DynamicArray<>();
            if (books != null) {
                for (int i = 0; i < books.size(); i++) {
                    ids.add(books.get(i).getId());
                }
            }

            long start = System.nanoTime();
            switch (algo) {
                case "quicksort" -> {
                    if (ids.size() > 0) Sorting.quickSort(ids, 0, ids.size() - 1);
                }
                case "insertionsort" -> Sorting.insertionSort(ids);
                case "selectionsort" -> Sorting.selectionSort(ids);
                default -> { // mergesort
                    if (ids.size() > 0) Sorting.mergeSort(ids, 0, ids.size() - 1);
                }
            }
            long elapsedNs = System.nanoTime() - start;

            StringBuilder idsJson = new StringBuilder("[");
            for (int i = 0; i < ids.size(); i++) {
                if (i > 0) idsJson.append(",");
                idsJson.append(ids.get(i));
            }
            idsJson.append("]");

            String response = String.format("""
                {
                    "algorithm": "%s",
                    "count": %d,
                    "executionTimeNs": %d,
                    "sortedIds": %s
                }
                """, algo, ids.size(), elapsedNs, idsJson.toString());

            sendJsonResponse(exchange, 200, response);
        }
    }

    private class BfsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            int start = 1;
            try {
                start = Integer.parseInt(queryParams.getOrDefault("start", "1").trim());
            } catch (NumberFormatException ignored) {}

            DynamicArray<Integer> bfs = controller.getGraph().bfs(start);
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < bfs.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(bfs.get(i));
            }
            sb.append("]");

            String json = String.format("""
                {"startId":%d,"count":%d,"traversalOrder":%s}
                """, start, bfs.size(), sb.toString());
            sendJsonResponse(exchange, 200, json);
        }
    }

    private class DfsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            int start = 1;
            try {
                start = Integer.parseInt(queryParams.getOrDefault("start", "1").trim());
            } catch (NumberFormatException ignored) {}

            DynamicArray<Integer> dfs = controller.getGraph().dfs(start);
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < dfs.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(dfs.get(i));
            }
            sb.append("]");

            String json = String.format("""
                {"startId":%d,"count":%d,"traversalOrder":%s}
                """, start, dfs.size(), sb.toString());
            sendJsonResponse(exchange, 200, json);
        }
    }

    private class DijkstraHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            int src = 1, dst = 2;
            try {
                src = Integer.parseInt(queryParams.getOrDefault("src", "1").trim());
                dst = Integer.parseInt(queryParams.getOrDefault("dst", "2").trim());
            } catch (NumberFormatException ignored) {}

            DijkstraResult result = controller.getGraph().dijkstra(src);
            boolean reachable = result.isReachable(dst);
            double dist = reachable ? result.distanceTo(dst) : -1;
            LinkedList<Integer> path = reachable ? result.pathTo(dst) : new LinkedList<>();

            StringBuilder pathJson = new StringBuilder("[");
            for (int i = 0; i < path.size(); i++) {
                if (i > 0) pathJson.append(",");
                pathJson.append(path.get(i));
            }
            pathJson.append("]");

            String json = String.format("""
                {
                    "sourceId": %d,
                    "destinationId": %d,
                    "isReachable": %b,
                    "shortestTimeHours": %.2f,
                    "path": %s
                }
                """, src, dst, reachable, dist, pathJson.toString());
            sendJsonResponse(exchange, 200, json);
        }
    }

    private class MstHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            String algo = queryParams.getOrDefault("algo", "kruskal").trim().toLowerCase();
            int start = 1;
            try {
                start = Integer.parseInt(queryParams.getOrDefault("start", "1").trim());
            } catch (NumberFormatException ignored) {}

            LinkedList<Road> mst;
            if ("prim".equals(algo)) {
                mst = controller.getGraph().prim(start);
            } else {
                mst = controller.getGraph().kruskal();
            }

            double totalDist = 0;
            StringBuilder edgesJson = new StringBuilder("[");
            for (int i = 0; i < mst.size(); i++) {
                Road r = mst.get(i);
                totalDist += r.getDistance();
                if (i > 0) edgesJson.append(",");
                edgesJson.append(String.format("""
                    {"source":%d,"destination":%d,"distance":%.2f,"travelTime":%.2f}
                    """, r.getSourceLibraryId(), r.getDestinationLibraryId(), r.getDistance(), r.getTravelTime()));
            }
            edgesJson.append("]");

            String json = String.format("""
                {
                    "algorithm": "%s",
                    "edgeCount": %d,
                    "totalDistance": %.2f,
                    "edges": %s
                }
                """, algo, mst.size(), totalDist, edgesJson.toString());
            sendJsonResponse(exchange, 200, json);
        }
    }

    private class KnapsackHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            int budget = 5000;
            try {
                budget = Integer.parseInt(queryParams.getOrDefault("budget", "5000").trim());
            } catch (NumberFormatException ignored) {}

            LinkedList<Resource> all = controller.getResources();
            DynamicArray<Resource> res = new DynamicArray<>();
            if (all != null) {
                for (int i = 0; i < all.size(); i++) res.add(all.get(i));
            }

            DynamicArray<Resource> chosen = DynamicProgramming.knapsack(res, budget);
            int totalValue = 0;
            double totalCost = 0;
            StringBuilder chosenJson = new StringBuilder("[");
            for (int i = 0; i < chosen.size(); i++) {
                Resource r = chosen.get(i);
                totalValue += r.getValue();
                totalCost += r.getCost();
                if (i > 0) chosenJson.append(",");
                chosenJson.append(String.format("""
                    {"id":%d,"name":"%s","type":"%s","cost":%.2f,"quantity":%d,"value":%d}
                    """, r.getId(), escapeJson(r.getName()), r.getType().name(), r.getCost(), r.getQuantity(), r.getValue()));
            }
            chosenJson.append("]");

            String json = String.format("""
                {
                    "budget": %d,
                    "totalCost": %.2f,
                    "totalValue": %d,
                    "itemCount": %d,
                    "chosen": %s
                }
                """, budget, totalCost, totalValue, chosen.size(), chosenJson.toString());
            sendJsonResponse(exchange, 200, json);
        }
    }

    private class GreedyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            double budget = 5000;
            try {
                budget = Double.parseDouble(queryParams.getOrDefault("budget", "5000").trim());
            } catch (NumberFormatException ignored) {}

            LinkedList<Resource> all = controller.getResources();
            DynamicArray<Resource> res = new DynamicArray<>();
            if (all != null) {
                for (int i = 0; i < all.size(); i++) res.add(all.get(i));
            }

            DynamicArray<Resource> chosen = Greedy.greedyByValue(res, budget);
            int totalValue = 0;
            double totalCost = 0;
            StringBuilder chosenJson = new StringBuilder("[");
            for (int i = 0; i < chosen.size(); i++) {
                Resource r = chosen.get(i);
                totalValue += r.getValue();
                totalCost += r.getCost();
                if (i > 0) chosenJson.append(",");
                chosenJson.append(String.format("""
                    {"id":%d,"name":"%s","type":"%s","cost":%.2f,"quantity":%d,"value":%d}
                    """, r.getId(), escapeJson(r.getName()), r.getType().name(), r.getCost(), r.getQuantity(), r.getValue()));
            }
            chosenJson.append("]");

            String json = String.format("""
                {
                    "budget": %.2f,
                    "totalCost": %.2f,
                    "totalValue": %d,
                    "itemCount": %d,
                    "chosen": %s
                }
                """, budget, totalCost, totalValue, chosen.size(), chosenJson.toString());
            sendJsonResponse(exchange, 200, json);
        }
    }

    private class SeedHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            try {
                DataLoader.loadAll();
                controller.loadDataIntoStructures();
                sendJsonResponse(exchange, 200, "{\"success\":true,\"message\":\"Database seeded and state refreshed successfully\"}");
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        }
    }
}
