import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.time.LocalDateTime;
import java.util.Random;

import datastructures.graph.Graph;
import datastructures.graph.DijkstraResult;
import datastructures.linear.LinkedList;
import datastructures.linear.DynamicArray;
import datastructures.tree.BinarySearchTree;
import datastructures.heap.PriorityQueue;
import algorithms.Sorting;
import algorithms.Searching;
import algorithms.DynamicProgramming;
import model.*;
import repository.*;
import service.RequestService;

public class LibraryUI extends JFrame {

    // ── Colours ────────────────────────────────────────
    private static final Color BG_DARK    = new Color(28, 37, 54);
    private static final Color BG_MENU    = new Color(20, 27, 42);
    private static final Color BG_CONTENT = new Color(245, 247, 250);
    private static final Color ACCENT     = new Color(52, 152, 219);
    private static final Color ACCENT_HOV = new Color(41, 128, 185);
    private static final Color GREEN      = new Color(39, 174, 96);
    private static final Color ORANGE     = new Color(230, 126, 34);
    private static final Color TEXT_LIGHT = new Color(236, 240, 241);
    private static final Color TEXT_DARK  = new Color(44, 62, 80);
    private static final Color CARD_BG    = Color.WHITE;
    private static final Color BORDER_COL = new Color(220, 230, 240);

    // ── Repositories ───────────────────────────────────
    private final LibraryRepository        libraryRepo   = new LibraryRepository();
    private final BookRepository           bookRepo      = new BookRepository();
    private final PatronRepository         patronRepo    = new PatronRepository();
    private final RoadRepository           roadRepo      = new RoadRepository();
    private final ResourceRepository       resourceRepo  = new ResourceRepository();
    private final ServiceRequestRepository requestRepo   = new ServiceRequestRepository();
    private final AlgorithmRunRepository   algorithmRepo = new AlgorithmRunRepository();

    // ── In-memory structures ───────────────────────────
    private final Graph                         graph          = new Graph();
    private final BinarySearchTree<String>      bookIndex      = new BinarySearchTree<>();
    private final PriorityQueue<ServiceRequest> requestQueue   = new PriorityQueue<>();
    private final RequestService                requestService = new RequestService();

    private LinkedList<Book>     books;
    private LinkedList<Patron>   patrons;
    private LinkedList<Library>  libraries;
    private LinkedList<Resource> resources;

    // ── UI ─────────────────────────────────────────────
    private JPanel   contentPanel;
    private JLabel   titleLabel;
    private JButton  activeButton;
    private JLabel   statusLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryUI().setVisible(true));
    }

    public LibraryUI() {
        super("Library Management & Decision Support System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1150, 720);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(950, 620));
        loadData();
        buildUI();
        showWelcome();
    }

    // ── Load data ──────────────────────────────────────

    private void loadData() {
        libraries = libraryRepo.findAll();
        for (int i = 0; i < libraries.size(); i++) graph.addLibrary(libraries.get(i));
        LinkedList<Road> roads = roadRepo.findAll();
        for (int i = 0; i < roads.size(); i++) graph.addRoad(roads.get(i));
        books = bookRepo.findAll();
        for (int i = 0; i < books.size(); i++) bookIndex.insert(books.get(i).getTitle());
        patrons   = patronRepo.findAll();
        resources = resourceRepo.findAll();
        LinkedList<ServiceRequest> reqs = requestRepo.findAll();
        for (int i = 0; i < reqs.size(); i++) {
            ServiceRequest r = reqs.get(i);
            if (r.getStatus() == RequestStatus.PENDING) {
                requestQueue.enqueue(r);
                requestService.submitRequest(r);
            }
        }
    }

    // ── Build UI ───────────────────────────────────────

    private void buildUI() {
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_DARK);
        header.setPreferredSize(new Dimension(0, 55));
        header.setBorder(new EmptyBorder(10, 20, 10, 20));
        JLabel logo = new JLabel("LMDSS");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logo.setForeground(ACCENT);
        titleLabel = new JLabel("Welcome");
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(new Color(149, 165, 166));
        header.add(logo, BorderLayout.WEST);
        header.add(titleLabel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Side menu
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(BG_MENU);
        menu.setPreferredSize(new Dimension(185, 0));
        menu.setBorder(new EmptyBorder(15, 0, 15, 0));

        String[] labels = {"Welcome","Books","Patrons","Requests","Search","Sort","Graph","Decision","Statistics","Performance"};
        for (String label : labels) {
            JButton btn = menuBtn(label);
            btn.addActionListener(e -> handleMenu(label, btn));
            menu.add(btn);
            menu.add(Box.createVerticalStrut(3));
        }
        menu.add(Box.createVerticalGlue());
        JLabel ver = new JLabel("  Ghana Library Network");
        ver.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        ver.setForeground(new Color(80, 95, 120));
        menu.add(ver);
        add(menu, BorderLayout.WEST);

        // Content
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG_CONTENT);
        add(contentPanel, BorderLayout.CENTER);

        // Status bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        statusBar.setBackground(new Color(230, 235, 240));
        statusBar.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_COL));
        statusLabel = new JLabel(statusText());
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(new Color(80, 90, 110));
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);
    }

    private String statusText() {
        return "Libraries: " + libraries.size() + "   |   Books: " + books.size()
             + "   |   Patrons: " + patrons.size() + "   |   Graph nodes: " + graph.vertexCount()
             + "   |   Graph edges: " + graph.edgeCount() + "   |   Pending requests: " + requestQueue.size();
    }

    private JButton menuBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(TEXT_LIGHT);
        btn.setBackground(BG_MENU);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(185, 40));
        btn.setPreferredSize(new Dimension(185, 40));
        btn.setBorder(new EmptyBorder(5, 18, 5, 5));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { if (btn != activeButton) btn.setBackground(new Color(35,47,68)); }
            public void mouseExited(MouseEvent e)  { if (btn != activeButton) btn.setBackground(BG_MENU); }
        });
        return btn;
    }

    private void activate(JButton btn) {
        if (activeButton != null) activeButton.setBackground(BG_MENU);
        activeButton = btn; btn.setBackground(ACCENT);
    }

    // ── Menu routing ───────────────────────────────────

    private void handleMenu(String module, JButton btn) {
        activate(btn);
        titleLabel.setText(module);
        contentPanel.removeAll();
        switch (module) {
            case "Welcome"     -> showWelcome();
            case "Books"       -> showBooks();
            case "Patrons"     -> showPatrons();
            case "Requests"    -> showRequests();
            case "Search"      -> showSearch();
            case "Sort"        -> showSort();
            case "Graph"       -> showGraph();
            case "Decision"    -> showDecision();
            case "Statistics"  -> showStatistics();
            case "Performance" -> showPerformance();
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ── Shared helpers ─────────────────────────────────

    private JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COL, 1, true),
            new EmptyBorder(18, 22, 18, 22)));
        return p;
    }

    private JLabel heading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 16));
        l.setForeground(TEXT_DARK);
        return l;
    }

    private JLabel sub(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(new Color(100, 120, 140));
        return l;
    }

    private JButton actionBtn(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(color); }
        });
        return btn;
    }

    private JTextArea resultArea() {
        JTextArea ta = new JTextArea(10, 40);
        ta.setFont(new Font("Consolas", Font.PLAIN, 12));
        ta.setForeground(TEXT_DARK);
        ta.setBackground(new Color(250, 252, 255));
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(new EmptyBorder(10, 12, 10, 12));
        return ta;
    }

    private JTextField field(String placeholder, int cols) {
        JTextField tf = new JTextField(cols);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COL, 1, true),
            new EmptyBorder(6, 10, 6, 10)));
        tf.setForeground(new Color(120, 130, 140));
        tf.setText(placeholder);
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) { tf.setText(""); tf.setForeground(TEXT_DARK); }
            }
            public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) { tf.setText(placeholder); tf.setForeground(new Color(120,130,140)); }
            }
        });
        return tf;
    }

    private String fieldVal(JTextField tf, String placeholder) {
        String v = tf.getText().trim();
        return v.equals(placeholder) ? "" : v;
    }

    private JScrollPane scrollWrap(JTextArea ta) {
        JScrollPane sp = new JScrollPane(ta);
        sp.setBorder(new LineBorder(BORDER_COL, 1, true));
        return sp;
    }

    // ── Welcome ────────────────────────────────────────

    private void showWelcome() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BG_CONTENT);

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(BG_CONTENT);
        inner.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Welcome to LMDSS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub1 = new JLabel("Ghana Library Network — Decision Support System");
        sub1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub1.setForeground(new Color(100, 120, 140));
        sub1.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(title);
        inner.add(Box.createVerticalStrut(8));
        inner.add(sub1);
        inner.add(Box.createVerticalStrut(30));

        // Stats grid
        JPanel stats = new JPanel(new GridLayout(2, 3, 15, 15));
        stats.setOpaque(false);
        stats.add(statCard("Libraries", String.valueOf(libraries.size()), ACCENT));
        stats.add(statCard("Books", String.valueOf(books.size()), GREEN));
        stats.add(statCard("Patrons", String.valueOf(patrons.size()), ORANGE));
        stats.add(statCard("Road Connections", String.valueOf(graph.edgeCount()), new Color(142, 68, 173)));
        stats.add(statCard("Resources", String.valueOf(resources.size()), new Color(22, 160, 133)));
        stats.add(statCard("Pending Requests", String.valueOf(requestQueue.size()), new Color(192, 57, 43)));
        inner.add(stats);
        inner.add(Box.createVerticalStrut(30));

        JLabel prompt = new JLabel("What would you like to do? Select a module from the left menu.");
        prompt.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        prompt.setForeground(new Color(120, 140, 160));
        prompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(prompt);

        outer.add(inner);
        contentPanel.add(outer, BorderLayout.CENTER);
    }

    private JPanel statCard(String label, String value, Color color) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color, 2, true),
            new EmptyBorder(15, 20, 15, 20)));
        JLabel val = new JLabel(value, SwingConstants.CENTER);
        val.setFont(new Font("Segoe UI", Font.BOLD, 26));
        val.setForeground(color);
        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(120, 130, 140));
        p.add(val, BorderLayout.CENTER);
        p.add(lbl, BorderLayout.SOUTH);
        return p;
    }

    // ── Books ──────────────────────────────────────────

    private void showBooks() {
        JPanel outer = new JPanel(new BorderLayout(15, 15));
        outer.setBackground(BG_CONTENT);
        outer.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top card — prompt
        JPanel top = card();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(heading("Book Management"));
        top.add(Box.createVerticalStrut(4));
        top.add(sub("Browse all books or add a new book to the library network."));
        outer.add(top, BorderLayout.NORTH);

        // Middle — controls + results
        JPanel mid = new JPanel(new BorderLayout(0, 12));
        mid.setOpaque(false);

        // Controls card
        JPanel ctrl = card();
        ctrl.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 8));
        JButton viewBtn = actionBtn("View All Books", ACCENT);
        JButton addBtn  = actionBtn("Add New Book", GREEN);
        ctrl.add(viewBtn);
        ctrl.add(addBtn);
        mid.add(ctrl, BorderLayout.NORTH);

        // Add form card (hidden initially)
        JPanel form = card();
        form.setLayout(new GridBagLayout());
        form.setVisible(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField titleF  = field("Book title", 20);
        JTextField authorF = field("Author name", 20);
        JTextField isbnF   = field("ISBN number", 15);
        JTextField libIdF  = field("Library ID (1-50)", 8);
        JButton saveBtn    = actionBtn("Save Book", GREEN);
        JButton cancelBtn  = actionBtn("Cancel", new Color(150,150,150));

        gbc.gridx=0; gbc.gridy=0; form.add(new JLabel("Title:"), gbc);
        gbc.gridx=1; form.add(titleF, gbc);
        gbc.gridx=2; form.add(new JLabel("Author:"), gbc);
        gbc.gridx=3; form.add(authorF, gbc);
        gbc.gridx=0; gbc.gridy=1; form.add(new JLabel("ISBN:"), gbc);
        gbc.gridx=1; form.add(isbnF, gbc);
        gbc.gridx=2; form.add(new JLabel("Library ID:"), gbc);
        gbc.gridx=3; form.add(libIdF, gbc);
        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2; form.add(saveBtn, gbc);
        gbc.gridx=2; form.add(cancelBtn, gbc);

        // Result area
        JTextArea result = resultArea();
        result.setText("Click 'View All Books' to see the full catalogue,\nor 'Add New Book' to register a new title.");
        JScrollPane scroll = scrollWrap(result);

        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setOpaque(false);
        content.add(form, BorderLayout.NORTH);
        content.add(scroll, BorderLayout.CENTER);
        mid.add(content, BorderLayout.CENTER);
        outer.add(mid, BorderLayout.CENTER);

        // Actions
        viewBtn.addActionListener(e -> {
            form.setVisible(false);
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-5s %-42s %-22s %s%n", "ID", "Title", "Author", "Status"));
            sb.append("-".repeat(85)).append("\n");
            for (int i = 0; i < books.size(); i++) {
                Book b = books.get(i);
                sb.append(String.format("%-5d %-42s %-22s %s%n",
                    b.getId(), truncate(b.getTitle(), 40), truncate(b.getAuthor(), 20),
                    b.isAvailable() ? "Available" : "Borrowed"));
            }
            result.setText(sb.toString());
            mid.revalidate();
        });

        addBtn.addActionListener(e -> {
            form.setVisible(true);
            result.setText("Fill in the form above and click 'Save Book'.");
            mid.revalidate();
        });

        saveBtn.addActionListener(e -> {
            String t = fieldVal(titleF, "Book title");
            String a = fieldVal(authorF, "Author name");
            String isbn = fieldVal(isbnF, "ISBN number");
            String lid = fieldVal(libIdF, "Library ID (1-50)");
            if (t.isEmpty() || a.isEmpty()) { result.setText("Please fill in at least Title and Author."); return; }
            try {
                int libId = lid.isEmpty() ? 1 : Integer.parseInt(lid);
                bookRepo.save(new Book(0, t, a, isbn, true, libId));
                books = bookRepo.findAll();
                bookIndex.insert(t);
                form.setVisible(false);
                result.setText("Book added successfully!\n\nTitle:  " + t + "\nAuthor: " + a + "\nISBN:   " + isbn + "\nLibrary ID: " + libId + "\n\nTotal books: " + books.size());
                titleF.setText("Book title"); authorF.setText("Author name");
                isbnF.setText("ISBN number"); libIdF.setText("Library ID (1-50)");
                mid.revalidate();
            } catch (NumberFormatException ex) { result.setText("Library ID must be a number (1-50)."); }
        });

        cancelBtn.addActionListener(e -> { form.setVisible(false); result.setText(""); mid.revalidate(); });

        contentPanel.add(outer, BorderLayout.CENTER);
    }

    // ── Patrons ────────────────────────────────────────

    private void showPatrons() {
        JPanel outer = new JPanel(new BorderLayout(15, 15));
        outer.setBackground(BG_CONTENT);
        outer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel top = card();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(heading("Patron Management"));
        top.add(Box.createVerticalStrut(4));
        top.add(sub("View registered patrons or register a new library member."));
        outer.add(top, BorderLayout.NORTH);

        JPanel mid = new JPanel(new BorderLayout(0, 12));
        mid.setOpaque(false);

        JPanel ctrl = card();
        ctrl.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 8));
        JButton viewBtn = actionBtn("View All Patrons", ACCENT);
        JButton addBtn  = actionBtn("Register Patron", GREEN);
        ctrl.add(viewBtn); ctrl.add(addBtn);
        mid.add(ctrl, BorderLayout.NORTH);

        JPanel form = card();
        form.setLayout(new GridBagLayout());
        form.setVisible(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField nameF  = field("Full name", 20);
        JTextField emailF = field("Email address", 20);
        JTextField phoneF = field("Phone number (e.g. 0241234567)", 18);
        JButton saveBtn   = actionBtn("Register", GREEN);
        JButton cancelBtn = actionBtn("Cancel", new Color(150,150,150));

        gbc.gridx=0; gbc.gridy=0; form.add(new JLabel("Name:"), gbc);
        gbc.gridx=1; form.add(nameF, gbc);
        gbc.gridx=2; form.add(new JLabel("Email:"), gbc);
        gbc.gridx=3; form.add(emailF, gbc);
        gbc.gridx=0; gbc.gridy=1; form.add(new JLabel("Phone:"), gbc);
        gbc.gridx=1; form.add(phoneF, gbc);
        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2; form.add(saveBtn, gbc);
        gbc.gridx=2; form.add(cancelBtn, gbc);

        JTextArea result = resultArea();
        result.setText("Click 'View All Patrons' to see registered members,\nor 'Register Patron' to add a new member.");
        JScrollPane scroll = scrollWrap(result);

        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setOpaque(false);
        content.add(form, BorderLayout.NORTH);
        content.add(scroll, BorderLayout.CENTER);
        mid.add(content, BorderLayout.CENTER);
        outer.add(mid, BorderLayout.CENTER);

        viewBtn.addActionListener(e -> {
            form.setVisible(false);
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-5s %-25s %-30s %s%n", "ID", "Name", "Email", "Phone"));
            sb.append("-".repeat(80)).append("\n");
            for (int i = 0; i < patrons.size(); i++) {
                Patron p = patrons.get(i);
                sb.append(String.format("%-5d %-25s %-30s %s%n",
                    p.getId(), truncate(p.getName(), 23), truncate(p.getEmail(), 28), p.getPhoneNumber()));
            }
            result.setText(sb.toString());
            mid.revalidate();
        });

        addBtn.addActionListener(e -> { form.setVisible(true); result.setText("Fill in the form above and click 'Register'."); mid.revalidate(); });

        saveBtn.addActionListener(e -> {
            String n = fieldVal(nameF, "Full name");
            String em = fieldVal(emailF, "Email address");
            String ph = fieldVal(phoneF, "Phone number (e.g. 0241234567)");
            if (n.isEmpty()) { result.setText("Please enter a name."); return; }
            patronRepo.save(new Patron(0, n, em, ph));
            patrons = patronRepo.findAll();
            form.setVisible(false);
            result.setText("Patron registered!\n\nName:  " + n + "\nEmail: " + em + "\nPhone: " + ph + "\n\nTotal patrons: " + patrons.size());
            nameF.setText("Full name"); emailF.setText("Email address"); phoneF.setText("Phone number (e.g. 0241234567)");
            mid.revalidate();
        });

        cancelBtn.addActionListener(e -> { form.setVisible(false); result.setText(""); mid.revalidate(); });

        contentPanel.add(outer, BorderLayout.CENTER);
    }

    // ── Requests ───────────────────────────────────────

    private void showRequests() {
        JPanel outer = new JPanel(new BorderLayout(15, 15));
        outer.setBackground(BG_CONTENT);
        outer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel top = card();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(heading("Service Request Dispatch"));
        top.add(Box.createVerticalStrut(4));
        top.add(sub("Manage service requests using FIFO, Circular, or Priority (urgency-based) dispatch."));
        outer.add(top, BorderLayout.NORTH);

        JPanel mid = new JPanel(new BorderLayout(0, 12));
        mid.setOpaque(false);

        // Mode selector
        JPanel modeCard = card();
        modeCard.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 8));
        modeCard.add(new JLabel("Dispatch Mode:"));
        JToggleButton fifoBtn = modeToggle("FIFO");
        JToggleButton prioBtn = modeToggle("PRIORITY");
        JToggleButton circBtn = modeToggle("CIRCULAR");
        ButtonGroup bg = new ButtonGroup();
        bg.add(fifoBtn); bg.add(prioBtn); bg.add(circBtn);
        fifoBtn.setSelected(true);
        modeCard.add(fifoBtn); modeCard.add(prioBtn); modeCard.add(circBtn);

        JPanel actionCard = card();
        actionCard.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 8));
        JButton peekBtn    = actionBtn("Peek Next", ACCENT);
        JButton processBtn = actionBtn("Process Next", ORANGE);
        JButton submitBtn  = actionBtn("Submit Request", GREEN);
        actionCard.add(peekBtn); actionCard.add(processBtn); actionCard.add(submitBtn);

        // Submit form
        JPanel form = card();
        form.setLayout(new GridBagLayout());
        form.setVisible(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField patronIdF  = field("Patron ID", 8);
        JTextField bookIdF    = field("Book ID", 8);
        String[] types = {"BORROW", "RETURN", "RESERVE"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        JSlider urgencySlider = new JSlider(1, 10, 5);
        urgencySlider.setMajorTickSpacing(1);
        urgencySlider.setPaintTicks(true);
        urgencySlider.setPaintLabels(true);
        JButton saveReqBtn    = actionBtn("Submit", GREEN);
        JButton cancelReqBtn  = actionBtn("Cancel", new Color(150,150,150));

        gbc.gridx=0; gbc.gridy=0; form.add(new JLabel("Patron ID:"), gbc);
        gbc.gridx=1; form.add(patronIdF, gbc);
        gbc.gridx=2; form.add(new JLabel("Book ID:"), gbc);
        gbc.gridx=3; form.add(bookIdF, gbc);
        gbc.gridx=0; gbc.gridy=1; form.add(new JLabel("Type:"), gbc);
        gbc.gridx=1; form.add(typeBox, gbc);
        gbc.gridx=2; form.add(new JLabel("Urgency (1-10):"), gbc);
        gbc.gridx=3; gbc.fill=GridBagConstraints.HORIZONTAL; form.add(urgencySlider, gbc);
        gbc.fill=GridBagConstraints.NONE;
        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2; form.add(saveReqBtn, gbc);
        gbc.gridx=2; form.add(cancelReqBtn, gbc);

        JTextArea result = resultArea();
        result.setText("Pending requests: " + requestService.pendingCount()
            + "\nCurrent mode: " + requestService.getMode()
            + "\n\nUse the buttons above to manage requests.");
        JScrollPane scroll = scrollWrap(result);

        JPanel ctrl = new JPanel(new BorderLayout(0, 8));
        ctrl.setOpaque(false);
        ctrl.add(modeCard, BorderLayout.NORTH);
        ctrl.add(actionCard, BorderLayout.SOUTH);

        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setOpaque(false);
        content.add(form, BorderLayout.NORTH);
        content.add(scroll, BorderLayout.CENTER);

        mid.add(ctrl, BorderLayout.NORTH);
        mid.add(content, BorderLayout.CENTER);
        outer.add(mid, BorderLayout.CENTER);

        fifoBtn.addActionListener(e -> { requestService.setMode(RequestService.DispatchMode.FIFO);     result.setText("Mode set to FIFO.\nPending: " + requestService.pendingCount()); });
        prioBtn.addActionListener(e -> { requestService.setMode(RequestService.DispatchMode.PRIORITY); result.setText("Mode set to PRIORITY (highest urgency first).\nPending: " + requestService.pendingCount()); });
        circBtn.addActionListener(e -> { requestService.setMode(RequestService.DispatchMode.CIRCULAR); result.setText("Mode set to CIRCULAR (round-robin).\nPending: " + requestService.pendingCount()); });

        peekBtn.addActionListener(e -> {
            if (requestService.hasPending()) {
                ServiceRequest r = requestService.peekNext();
                result.setText("Next request (not yet processed):\n\n" + formatRequest(r) + "\n\nPending: " + requestService.pendingCount());
            } else result.setText("No pending requests in the queue.");
        });

        processBtn.addActionListener(e -> {
            if (requestService.hasPending()) {
                ServiceRequest r = requestService.processNext();
                requestRepo.updateStatus(r.getId(), RequestStatus.PROCESSING);
                result.setText("Dispatched and processing:\n\n" + formatRequest(r) + "\n\nRemaining: " + requestService.pendingCount());
            } else result.setText("No pending requests to process.");
        });

        submitBtn.addActionListener(e -> { form.setVisible(true); result.setText("Fill in the form above."); mid.revalidate(); });

        saveReqBtn.addActionListener(e -> {
            String pid = fieldVal(patronIdF, "Patron ID");
            String bid = fieldVal(bookIdF, "Book ID");
            if (pid.isEmpty() || bid.isEmpty()) { result.setText("Please enter Patron ID and Book ID."); return; }
            try {
                ServiceRequest req = new ServiceRequest(0, Integer.parseInt(pid), Integer.parseInt(bid),
                    RequestType.valueOf((String)typeBox.getSelectedItem()),
                    urgencySlider.getValue(), RequestStatus.PENDING, LocalDateTime.now());
                requestRepo.save(req);
                requestQueue.enqueue(req);
                requestService.submitRequest(req);
                form.setVisible(false);
                result.setText("Request submitted!\n\n" + formatRequest(req) + "\n\nTotal pending: " + requestService.pendingCount());
                patronIdF.setText("Patron ID"); bookIdF.setText("Book ID");
                mid.revalidate();
            } catch (Exception ex) { result.setText("Error: " + ex.getMessage()); }
        });

        cancelReqBtn.addActionListener(e -> { form.setVisible(false); mid.revalidate(); });

        contentPanel.add(outer, BorderLayout.CENTER);
    }

    private JToggleButton modeToggle(String text) {
        JToggleButton btn = new JToggleButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 14, 6, 14));
        return btn;
    }

    private String formatRequest(ServiceRequest r) {
        return "  ID:       " + r.getId()
             + "\n  Patron:   " + r.getPatronId()
             + "\n  Book:     " + r.getBookId()
             + "\n  Type:     " + r.getRequestType()
             + "\n  Urgency:  " + r.getUrgency() + " / 10"
             + "\n  Status:   " + r.getStatus();
    }

    // ── Search ─────────────────────────────────────────

    private void showSearch() {
        JPanel outer = new JPanel(new BorderLayout(15, 15));
        outer.setBackground(BG_CONTENT);
        outer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel top = card();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(heading("Search for a Book"));
        top.add(Box.createVerticalStrut(4));
        top.add(sub("Linear search scans every record O(n). BST search uses the binary search tree index O(log n)."));
        outer.add(top, BorderLayout.NORTH);

        JPanel mid = new JPanel(new BorderLayout(0, 12));
        mid.setOpaque(false);

        JPanel searchCard = card();
        searchCard.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));
        JTextField queryF  = field("Type a book title or keyword...", 30);
        JButton linearBtn  = actionBtn("Linear Search  O(n)", ACCENT);
        JButton bstBtn     = actionBtn("BST Search  O(log n)", new Color(142, 68, 173));
        searchCard.add(queryF);
        searchCard.add(linearBtn);
        searchCard.add(bstBtn);
        mid.add(searchCard, BorderLayout.NORTH);

        JTextArea result = resultArea();
        result.setText("Enter a book title or keyword above, then click a search method.\n\n"
            + "Linear search — scans every book, works with partial matches.\n"
            + "BST search    — uses the tree index, requires exact title.");
        mid.add(scrollWrap(result), BorderLayout.CENTER);
        outer.add(mid, BorderLayout.CENTER);

        linearBtn.addActionListener(e -> {
            String q = fieldVal(queryF, "Type a book title or keyword...").toLowerCase();
            if (q.isEmpty()) { result.setText("Please enter something to search."); return; }
            long start = System.nanoTime();
            int found = 0;
            StringBuilder sb = new StringBuilder();
            sb.append("Linear Search: \"").append(q).append("\"\n");
            sb.append("-".repeat(60)).append("\n");
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).getTitle().toLowerCase().contains(q)) {
                    Book b = books.get(i);
                    sb.append(String.format("[%d] %s by %s%n", b.getId(), b.getTitle(), b.getAuthor()));
                    found++;
                }
            }
            long elapsed = System.nanoTime() - start;
            sb.append("\nFound: ").append(found).append(" result(s)");
            sb.append("\nTime:  ").append(elapsed).append(" ns (").append(elapsed / 1_000_000).append(" ms)");
            result.setText(sb.toString());
        });

        bstBtn.addActionListener(e -> {
            String q = fieldVal(queryF, "Type a book title or keyword...");
            if (q.isEmpty()) { result.setText("Please enter a title to search."); return; }
            long start = System.nanoTime();
            boolean found = bookIndex.contains(q);
            long elapsed = System.nanoTime() - start;
            result.setText("BST Search: \"" + q + "\"\n" + "-".repeat(60)
                + "\nResult: " + (found ? "FOUND in BST index" : "NOT FOUND (BST requires exact title match)")
                + "\nTime:   " + elapsed + " ns"
                + "\n\nNote: BST search is O(log n) but needs the exact title."
                + "\nUse Linear Search for partial keyword matching.");
        });

        contentPanel.add(outer, BorderLayout.CENTER);
    }

    // ── Sort ───────────────────────────────────────────

    private void showSort() {
        JPanel outer = new JPanel(new BorderLayout(15, 15));
        outer.setBackground(BG_CONTENT);
        outer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel top = card();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(heading("Sorting Engine"));
        top.add(Box.createVerticalStrut(4));
        top.add(sub("Sort " + books.size() + " book IDs using different algorithms. Compare their performance."));
        outer.add(top, BorderLayout.NORTH);

        JPanel mid = new JPanel(new BorderLayout(0, 12));
        mid.setOpaque(false);

        JPanel ctrl = card();
        ctrl.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 8));
        JButton selBtn  = actionBtn("Selection Sort", new Color(142, 68, 173));
        JButton insBtn  = actionBtn("Insertion Sort", ORANGE);
        JButton merBtn  = actionBtn("Merge Sort", ACCENT);
        JButton quiBtn  = actionBtn("Quick Sort", GREEN);
        JButton allBtn  = actionBtn("Compare All", new Color(44, 62, 80));
        ctrl.add(selBtn); ctrl.add(insBtn); ctrl.add(merBtn); ctrl.add(quiBtn); ctrl.add(allBtn);
        mid.add(ctrl, BorderLayout.NORTH);

        JTextArea result = resultArea();
        result.setText("Click any sort button to sort all " + books.size() + " book IDs.\n"
            + "Click 'Compare All' to run all four and see which is fastest.\n\n"
            + "Selection Sort: O(n²) — always slow\n"
            + "Insertion Sort: O(n²) worst, O(n) best (nearly sorted)\n"
            + "Merge Sort:     O(n log n) — consistent\n"
            + "Quick Sort:     O(n log n) average — fastest in practice");
        mid.add(scrollWrap(result), BorderLayout.CENTER);
        outer.add(mid, BorderLayout.CENTER);

        selBtn.addActionListener(e -> runSort("Selection Sort", 1, result));
        insBtn.addActionListener(e -> runSort("Insertion Sort", 2, result));
        merBtn.addActionListener(e -> runSort("Merge Sort", 3, result));
        quiBtn.addActionListener(e -> runSort("Quick Sort", 4, result));

        allBtn.addActionListener(e -> {
            String[] names = {"Selection Sort", "Insertion Sort", "Merge Sort", "Quick Sort"};
            long[] times = new long[4];
            for (int t = 0; t < 4; t++) {
                DynamicArray<Integer> arr = bookIds();
                long start = System.nanoTime();
                if (t == 0) Sorting.selectionSort(arr);
                else if (t == 1) Sorting.insertionSort(arr);
                else if (t == 2) Sorting.mergeSort(arr, 0, arr.size()-1);
                else Sorting.quickSort(arr, 0, arr.size()-1);
                times[t] = System.nanoTime() - start;
            }
            StringBuilder sb = new StringBuilder("Sort Comparison — " + books.size() + " books\n");
            sb.append("-".repeat(50)).append("\n");
            int fastest = 0;
            for (int t = 1; t < 4; t++) if (times[t] < times[fastest]) fastest = t;
            for (int t = 0; t < 4; t++) {
                sb.append(String.format("%-18s %10d ns %s%n", names[t], times[t], t == fastest ? "<-- fastest" : ""));
            }
            sb.append("\nWinner: ").append(names[fastest]);
            result.setText(sb.toString());
        });

        contentPanel.add(outer, BorderLayout.CENTER);
    }

    private void runSort(String name, int type, JTextArea result) {
        DynamicArray<Integer> arr = bookIds();
        long start = System.nanoTime();
        if (type == 1) Sorting.selectionSort(arr);
        else if (type == 2) Sorting.insertionSort(arr);
        else if (type == 3) Sorting.mergeSort(arr, 0, arr.size()-1);
        else Sorting.quickSort(arr, 0, arr.size()-1);
        long elapsed = System.nanoTime() - start;
        StringBuilder sb = new StringBuilder(name + " — " + books.size() + " books\n");
        sb.append("-".repeat(50)).append("\n");
        sb.append("First 15 sorted IDs: ");
        for (int i = 0; i < Math.min(15, arr.size()); i++) sb.append(arr.get(i)).append(" ");
        sb.append("\n\nTime: ").append(elapsed).append(" ns (").append(elapsed / 1_000_000).append(" ms)");
        result.setText(sb.toString());
    }

    private DynamicArray<Integer> bookIds() {
        DynamicArray<Integer> arr = new DynamicArray<>();
        for (int i = 0; i < books.size(); i++) arr.add(books.get(i).getId());
        return arr;
    }

    // ── Graph ──────────────────────────────────────────

    private void showGraph() {
        JPanel outer = new JPanel(new BorderLayout(15, 15));
        outer.setBackground(BG_CONTENT);
        outer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel top = card();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(heading("Graph Navigation — Ghana Library Network"));
        top.add(Box.createVerticalStrut(4));
        top.add(sub(graph.vertexCount() + " library nodes   |   " + graph.edgeCount() + " road connections"));
        outer.add(top, BorderLayout.NORTH);

        // Split: left = visual graph, right = controls + results
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.55);
        split.setDividerSize(5);
        split.setOpaque(false);

        // Visual graph panel
        GraphPanel graphPanel = new GraphPanel();
        split.setLeftComponent(graphPanel);

        // Right side
        JPanel right = new JPanel(new BorderLayout(0, 12));
        right.setOpaque(false);

        JPanel ctrl = card();
        ctrl.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 6, 5, 6);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField srcF = field("Source library ID", 10);
        JTextField dstF = field("Destination ID", 10);
        JButton bfsBtn  = actionBtn("BFS", ACCENT);
        JButton dfsBtn  = actionBtn("DFS", new Color(142, 68, 173));
        JButton dijBtn  = actionBtn("Dijkstra", GREEN);
        JButton mstBtn  = actionBtn("Kruskal MST", ORANGE);

        gbc.gridx=0; gbc.gridy=0; ctrl.add(new JLabel("From:"), gbc);
        gbc.gridx=1; ctrl.add(srcF, gbc);
        gbc.gridx=2; ctrl.add(new JLabel("To:"), gbc);
        gbc.gridx=3; ctrl.add(dstF, gbc);
        gbc.gridx=0; gbc.gridy=1; gbc.gridwidth=1; ctrl.add(bfsBtn, gbc);
        gbc.gridx=1; ctrl.add(dfsBtn, gbc);
        gbc.gridx=2; ctrl.add(dijBtn, gbc);
        gbc.gridx=3; ctrl.add(mstBtn, gbc);
        right.add(ctrl, BorderLayout.NORTH);

        JTextArea result = resultArea();
        result.setText("Enter a source library ID (1-50) and click an algorithm.\n\n"
            + "BFS — breadth-first, visits nearest branches first\n"
            + "DFS — depth-first, follows one path as deep as possible\n"
            + "Dijkstra — finds fastest route between two libraries\n"
            + "Kruskal — finds minimum road network connecting all libraries");
        right.add(scrollWrap(result), BorderLayout.CENTER);
        split.setRightComponent(right);
        outer.add(split, BorderLayout.CENTER);

        bfsBtn.addActionListener(e -> {
            int src = parseInt(fieldVal(srcF, "Source library ID"), 1);
            long start = System.nanoTime();
            DynamicArray<Integer> visited = graph.bfs(src);
            long elapsed = System.nanoTime() - start;
            graphPanel.highlight(visited, null);
            StringBuilder sb = new StringBuilder("BFS from Library " + src + "\n" + "-".repeat(50) + "\n");
            sb.append("Visited ").append(visited.size()).append(" libraries:\n");
            for (int i = 0; i < visited.size(); i++) { sb.append(visited.get(i)); if (i < visited.size()-1) sb.append(" -> "); if ((i+1)%8==0) sb.append("\n"); }
            sb.append("\n\nTime: ").append(elapsed).append(" ns");
            result.setText(sb.toString());
        });

        dfsBtn.addActionListener(e -> {
            int src = parseInt(fieldVal(srcF, "Source library ID"), 1);
            long start = System.nanoTime();
            DynamicArray<Integer> visited = graph.dfs(src);
            long elapsed = System.nanoTime() - start;
            graphPanel.highlight(visited, null);
            StringBuilder sb = new StringBuilder("DFS from Library " + src + "\n" + "-".repeat(50) + "\n");
            sb.append("Visited ").append(visited.size()).append(" libraries:\n");
            for (int i = 0; i < visited.size(); i++) { sb.append(visited.get(i)); if (i < visited.size()-1) sb.append(" -> "); if ((i+1)%8==0) sb.append("\n"); }
            sb.append("\n\nTime: ").append(elapsed).append(" ns");
            result.setText(sb.toString());
        });

        dijBtn.addActionListener(e -> {
            int src = parseInt(fieldVal(srcF, "Source library ID"), 1);
            int dst = parseInt(fieldVal(dstF, "Destination ID"), 31);
            long start = System.nanoTime();
            DijkstraResult dr = graph.dijkstra(src);
            long elapsed = System.nanoTime() - start;
            if (dr.isReachable(dst)) {
                LinkedList<Integer> path = dr.pathTo(dst);
                DynamicArray<Integer> pathArr = new DynamicArray<>();
                for (int i = 0; i < path.size(); i++) pathArr.add(path.get(i));
                graphPanel.highlight(pathArr, pathArr);
                StringBuilder sb = new StringBuilder("Dijkstra: Library " + src + " to Library " + dst + "\n" + "-".repeat(50) + "\n");
                sb.append(String.format("Shortest travel time: %.2f hours%n", dr.distanceTo(dst)));
                sb.append("Path: ");
                for (int i = 0; i < path.size(); i++) { sb.append(path.get(i)); if (i < path.size()-1) sb.append(" -> "); }
                sb.append("\n\nTime: ").append(elapsed).append(" ns");
                result.setText(sb.toString());
            } else result.setText("No path found between Library " + src + " and Library " + dst);
        });

        mstBtn.addActionListener(e -> {
            long start = System.nanoTime();
            LinkedList<Road> mst = graph.kruskal();
            long elapsed = System.nanoTime() - start;
            DynamicArray<Integer> mstNodes = new DynamicArray<>();
            double total = 0;
            for (int i = 0; i < mst.size(); i++) { mstNodes.add(mst.get(i).getSourceLibraryId()); total += mst.get(i).getDistance(); }
            graphPanel.highlight(mstNodes, null);
            StringBuilder sb = new StringBuilder("Kruskal Minimum Spanning Tree\n" + "-".repeat(50) + "\n");
            sb.append("MST edges: ").append(mst.size()).append("\n\n");
            for (int i = 0; i < Math.min(20, mst.size()); i++) {
                Road r = mst.get(i);
                sb.append(String.format("Library %2d <-> Library %2d   %.1f km%n", r.getSourceLibraryId(), r.getDestinationLibraryId(), r.getDistance()));
            }
            if (mst.size() > 20) sb.append("... and ").append(mst.size()-20).append(" more\n");
            sb.append(String.format("%nTotal MST distance: %.1f km%n", total));
            sb.append("Time: ").append(elapsed).append(" ns");
            result.setText(sb.toString());
        });

        contentPanel.add(outer, BorderLayout.CENTER);
    }

    // ── Visual Graph Panel ─────────────────────────────

    class GraphPanel extends JPanel {
        private DynamicArray<Integer> highlighted;
        private DynamicArray<Integer> path;
        private final int[][] positions;
        private final Random rng = new Random(42);

        GraphPanel() {
            setBackground(new Color(18, 24, 38));
            setBorder(new LineBorder(BORDER_COL, 1));
            // Generate stable positions for 50 nodes
            positions = new int[51][2];
            int W = 500, H = 500, margin = 40;
            // Cluster by region
            int[][] regions = {{W/2, H*4/5}, {W/4, H*3/4}, {W*3/4, H*3/4}, {W/2, H/2}, {W*1/4, H/2},
                               {W*3/4, H/2}, {W/2, H*1/4}, {W/4, H/4}, {W*3/4, H/4}, {W/2, H/8}};
            for (int i = 1; i <= 50; i++) {
                int region = (i-1) / 5;
                int rx = regions[region][0];
                int ry = regions[region][1];
                positions[i][0] = margin + rx/2 + rng.nextInt(90) - 45;
                positions[i][1] = margin + ry/2 + rng.nextInt(90) - 45;
            }
        }

        void highlight(DynamicArray<Integer> nodes, DynamicArray<Integer> pathNodes) {
            this.highlighted = nodes;
            this.path = pathNodes;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int W = getWidth(), H = getHeight();
            float scaleX = (float) W / 340f;
            float scaleY = (float) H / 340f;

            // Draw edges (sample — draw first 100 roads as lines)
            LinkedList<Road> roads = roadRepo.findAll();
            g2.setStroke(new BasicStroke(0.8f));
            for (int i = 0; i < Math.min(roads.size(), 100); i++) {
                Road r = roads.get(i);
                int s = r.getSourceLibraryId(), d = r.getDestinationLibraryId();
                if (s < 1 || s > 50 || d < 1 || d > 50) continue;
                int x1 = (int)(positions[s][0] * scaleX);
                int y1 = (int)(positions[s][1] * scaleY);
                int x2 = (int)(positions[d][0] * scaleX);
                int y2 = (int)(positions[d][1] * scaleY);
                g2.setColor(new Color(60, 80, 110));
                g2.drawLine(x1, y1, x2, y2);
            }

            // Draw highlighted path edges
            if (path != null && path.size() > 1) {
                g2.setStroke(new BasicStroke(2.5f));
                g2.setColor(new Color(255, 200, 0));
                for (int i = 0; i < path.size()-1; i++) {
                    int s = path.get(i), d = path.get(i+1);
                    if (s < 1 || s > 50 || d < 1 || d > 50) continue;
                    g2.drawLine((int)(positions[s][0]*scaleX), (int)(positions[s][1]*scaleY),
                                (int)(positions[d][0]*scaleX), (int)(positions[d][1]*scaleY));
                }
            }

            // Draw nodes
            for (int id = 1; id <= 50; id++) {
                int x = (int)(positions[id][0] * scaleX);
                int y = (int)(positions[id][1] * scaleY);
                boolean isHighlighted = highlighted != null && contains(highlighted, id);
                boolean inPath = path != null && contains(path, id);

                int r = inPath ? 9 : isHighlighted ? 7 : 5;
                Color fill = inPath ? new Color(255, 200, 0) : isHighlighted ? ACCENT : new Color(52, 73, 94);
                Color border = inPath ? Color.WHITE : isHighlighted ? Color.WHITE : new Color(80, 100, 130);

                g2.setColor(fill);
                g2.fillOval(x - r, y - r, r*2, r*2);
                g2.setColor(border);
                g2.setStroke(new BasicStroke(1f));
                g2.drawOval(x - r, y - r, r*2, r*2);

                // Label for highlighted nodes only
                if (isHighlighted || inPath) {
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 8));
                    g2.setColor(Color.WHITE);
                    g2.drawString(String.valueOf(id), x + r + 2, y + 4);
                }
            }

            // Legend
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g2.setColor(new Color(149, 165, 166));
            g2.drawString("Ghana Library Network — " + graph.vertexCount() + " nodes, " + graph.edgeCount() + " edges", 10, H - 8);
        }

        private boolean contains(DynamicArray<Integer> arr, int val) {
            for (int i = 0; i < arr.size(); i++) if (arr.get(i) == val) return true;
            return false;
        }
    }

    // ── Decision ───────────────────────────────────────

    private void showDecision() {
        JPanel outer = new JPanel(new BorderLayout(15, 15));
        outer.setBackground(BG_CONTENT);
        outer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel top = card();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(heading("Decision Support — Resource Optimisation"));
        top.add(Box.createVerticalStrut(4));
        top.add(sub("Given a budget, decide which resources to acquire. Compare Greedy vs Dynamic Programming."));
        outer.add(top, BorderLayout.NORTH);

        JPanel mid = new JPanel(new BorderLayout(0, 12));
        mid.setOpaque(false);

        JPanel ctrl = card();
        ctrl.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));
        JTextField budgetF = field("Budget in GHS (e.g. 5000)", 20);
        JButton greedyBtn  = actionBtn("Greedy Algorithm", ORANGE);
        JButton dpBtn      = actionBtn("DP Knapsack", GREEN);
        JButton compareBtn = actionBtn("Compare Both", ACCENT);
        ctrl.add(new JLabel("Budget (GHS):"));
        ctrl.add(budgetF); ctrl.add(greedyBtn); ctrl.add(dpBtn); ctrl.add(compareBtn);
        mid.add(ctrl, BorderLayout.NORTH);

        JTextArea result = resultArea();
        result.setText("Enter a budget and choose an algorithm.\n\n"
            + "Greedy — picks the highest-value resource at each step. Fast but not always optimal.\n"
            + "DP Knapsack — considers every combination. Slower but always finds the optimal solution.\n\n"
            + "Try both with the same budget to see the difference!");
        mid.add(scrollWrap(result), BorderLayout.CENTER);
        outer.add(mid, BorderLayout.CENTER);

        greedyBtn.addActionListener(e -> {
            int budget = parseInt(fieldVal(budgetF, "Budget in GHS (e.g. 5000)"), 5000);
            DynamicArray<Resource> res = toArr(resources);
            long start = System.nanoTime();
            DynamicArray<Resource> sel = algorithms.Greedy.greedyByValue(res, budget);
            long elapsed = System.nanoTime() - start;
            result.setText(formatOptResult("Greedy Algorithm", budget, sel, elapsed));
        });

        dpBtn.addActionListener(e -> {
            int budget = parseInt(fieldVal(budgetF, "Budget in GHS (e.g. 5000)"), 5000);
            DynamicArray<Resource> res = toArr(resources);
            long start = System.nanoTime();
            DynamicArray<Resource> sel = DynamicProgramming.knapsack(res, budget);
            long elapsed = System.nanoTime() - start;
            result.setText(formatOptResult("DP Knapsack (Optimal)", budget, sel, elapsed));
        });

        compareBtn.addActionListener(e -> {
            int budget = parseInt(fieldVal(budgetF, "Budget in GHS (e.g. 5000)"), 5000);
            DynamicArray<Resource> res1 = toArr(resources);
            DynamicArray<Resource> res2 = toArr(resources);
            long s1 = System.nanoTime(); DynamicArray<Resource> gSel = algorithms.Greedy.greedyByValue(res1, budget); long t1 = System.nanoTime() - s1;
            long s2 = System.nanoTime(); DynamicArray<Resource> dSel = DynamicProgramming.knapsack(res2, budget); long t2 = System.nanoTime() - s2;
            int gVal = 0; double gCost = 0; for (int i = 0; i < gSel.size(); i++) { gVal += gSel.get(i).getValue(); gCost += gSel.get(i).getCost(); }
            int dVal = 0; double dCost = 0; for (int i = 0; i < dSel.size(); i++) { dVal += dSel.get(i).getValue(); dCost += dSel.get(i).getCost(); }
            result.setText("COMPARISON — Budget: GHS " + budget + "\n" + "=".repeat(55) + "\n\n"
                + String.format("%-25s %8s %10s %10s%n", "Algorithm", "Items", "Cost", "Value")
                + "-".repeat(55) + "\n"
                + String.format("%-25s %8d %10.0f %10d%n", "Greedy", gSel.size(), gCost, gVal)
                + String.format("%-25s %8d %10.0f %10d%n", "DP Knapsack (optimal)", dSel.size(), dCost, dVal)
                + "\n" + (dVal > gVal ? "DP found a BETTER solution by " + (dVal - gVal) + " value points." : "Both found the same value — greedy was optimal here!")
                + "\nGreedy time: " + t1 + " ns   |   DP time: " + t2 + " ns");
        });

        contentPanel.add(outer, BorderLayout.CENTER);
    }

    private String formatOptResult(String name, int budget, DynamicArray<Resource> sel, long elapsed) {
        StringBuilder sb = new StringBuilder(name + " — Budget: GHS " + budget + "\n" + "=".repeat(55) + "\n\n");
        sb.append(String.format("%-35s %8s %8s%n", "Resource", "Cost", "Value"));
        sb.append("-".repeat(55)).append("\n");
        double totalCost = 0; int totalVal = 0;
        for (int i = 0; i < sel.size(); i++) {
            Resource r = sel.get(i);
            sb.append(String.format("%-35s %8.0f %8d%n", truncate(r.getName(), 33), r.getCost(), r.getValue()));
            totalCost += r.getCost(); totalVal += r.getValue();
        }
        sb.append("-".repeat(55)).append("\n");
        sb.append(String.format("TOTAL%43.0f%8d%n", totalCost, totalVal));
        sb.append("\nRemaining budget: GHS ").append((int)(budget - totalCost));
        sb.append("\nTime: ").append(elapsed).append(" ns");
        return sb.toString();
    }

    // ── Statistics ─────────────────────────────────────

    private void showStatistics() {
        JPanel outer = new JPanel(new BorderLayout(15, 15));
        outer.setBackground(BG_CONTENT);
        outer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel top = card();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(heading("System Statistics"));
        top.add(Box.createVerticalStrut(4));
        top.add(sub("Live snapshot of the entire Ghana library network."));
        outer.add(top, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 15, 15));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(10, 0, 10, 0));
        grid.add(statCard("Libraries", String.valueOf(libraries.size()), ACCENT));
        grid.add(statCard("Books", String.valueOf(books.size()), GREEN));
        grid.add(statCard("Patrons", String.valueOf(patrons.size()), ORANGE));
        grid.add(statCard("Graph Nodes", String.valueOf(graph.vertexCount()), new Color(142, 68, 173)));
        grid.add(statCard("Graph Edges", String.valueOf(graph.edgeCount()), new Color(22, 160, 133)));
        grid.add(statCard("Pending Requests", String.valueOf(requestQueue.size()), new Color(192, 57, 43)));
        outer.add(grid, BorderLayout.CENTER);

        JPanel bottom = card();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        long total = Runtime.getRuntime().totalMemory() / 1024;
        long free  = Runtime.getRuntime().freeMemory()  / 1024;
        LinkedList<AlgorithmRun> runs = algorithmRepo.findAll();
        bottom.add(sub("Memory used: " + (total - free) + " KB  |  Total JVM memory: " + total + " KB"));
        bottom.add(Box.createVerticalStrut(4));
        bottom.add(sub("Dispatch mode: " + requestService.getMode() + "  |  Algorithm runs recorded: " + runs.size()));
        outer.add(bottom, BorderLayout.SOUTH);

        contentPanel.add(outer, BorderLayout.CENTER);
    }

    // ── Performance ────────────────────────────────────

    private void showPerformance() {
        JPanel outer = new JPanel(new BorderLayout(15, 15));
        outer.setBackground(BG_CONTENT);
        outer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel top = card();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(heading("Performance Experiment Results"));
        top.add(Box.createVerticalStrut(4));
        top.add(sub("Empirical timing data from all 6 performance experiments. Run PerformanceExperiment.java to regenerate."));
        outer.add(top, BorderLayout.NORTH);

        JTextArea result = resultArea();
        LinkedList<AlgorithmRun> runs = algorithmRepo.findAll();
        if (runs.size() == 0) {
            result.setText("No performance data found.\nRun: java -cp \"out;lib\\sqlite-jdbc-3.53.2.1.jar\" PerformanceExperiment");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-22s %12s %16s %14s%n", "Algorithm", "Input Size", "Time (ns)", "Memory (KB)"));
            sb.append("=".repeat(70)).append("\n");
            String lastAlg = "";
            for (int i = 0; i < runs.size(); i++) {
                AlgorithmRun r = runs.get(i);
                if (!r.getAlgorithmName().equals(lastAlg) && !lastAlg.isEmpty()) sb.append("\n");
                sb.append(String.format("%-22s %12d %16d %14d%n",
                    r.getAlgorithmName(), r.getInputSize(), r.getTimeNs(), r.getMemoryKb()));
                lastAlg = r.getAlgorithmName();
            }
            sb.append("\nTotal runs: ").append(runs.size());
            result.setText(sb.toString());
        }
        outer.add(scrollWrap(result), BorderLayout.CENTER);
        contentPanel.add(outer, BorderLayout.CENTER);
    }

    // ── Utility ────────────────────────────────────────

    private String truncate(String s, int max) { return s.length() > max ? s.substring(0, max-2) + ".." : s; }
    private int parseInt(String s, int def) { try { return Integer.parseInt(s.trim()); } catch (Exception e) { return def; } }
    private DynamicArray<Resource> toArr(LinkedList<Resource> l) { DynamicArray<Resource> a = new DynamicArray<>(); for (int i = 0; i < l.size(); i++) a.add(l.get(i)); return a; }
}