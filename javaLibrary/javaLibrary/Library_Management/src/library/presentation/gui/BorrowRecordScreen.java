package library.presentation.gui;

import library.buisness.service.IBorrowRecordService;
import library.buisness.service.IBookService;
import library.buisness.service.IMemberService;
import library.domain.Book;
import library.domain.BorrowRecord;
import library.domain.Member;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class BorrowRecordScreen extends JFrame {
    private final IBorrowRecordService borrowRecordService;
    private final IBookService bookService;
    private final IMemberService memberService;

    private JPanel mainPanel;
    private JTable borrowsTable;
    private DefaultTableModel tableModel;

    private JComboBox<String> filterComboBox;
    private JTextField searchField;
    private JButton searchButton;

    private JButton borrowButton;
    private JButton returnButton;
    private JButton refreshButton;

    private JLabel borrowDateLabel;
    private JLabel dueDateLabel;
    private JTextField bookIdField;
    private JTextField memberIdField;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public BorrowRecordScreen(IBorrowRecordService borrowRecordService,
                              IBookService bookService,
                              IMemberService memberService) {
        this.borrowRecordService = borrowRecordService;
        this.bookService = bookService;
        this.memberService = memberService;

        initializeComponents();
        setupEventListeners();
        loadAllBorrows();
        setVisible(true);
    }

    private void initializeComponents() {
        setTitle("Borrow Management");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(800);

        JPanel leftPanel = createLeftPanel();
        splitPane.setLeftComponent(leftPanel);

        JPanel rightPanel = createRightPanel();
        splitPane.setRightComponent(rightPanel);

        mainPanel.add(splitPane);
        add(mainPanel);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Borrow Records");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterComboBox = new JComboBox<>(new String[]{"All", "Active", "Overdue", "By Member ID", "By Book ID"});
        searchField = new JTextField(15);
        searchButton = new JButton("Search");

        searchPanel.add(new JLabel("Filter:"));
        searchPanel.add(filterComboBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        String[] columns = {"ID", "Book", "Member", "Borrow Date", "Due Date", "Return Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        borrowsTable = new JTable(tableModel);
        borrowsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(borrowsTable);

        leftPanel.add(topPanel, BorderLayout.NORTH);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JLabel formTitle = new JLabel("Borrow Operations");
        formTitle.setFont(new Font("Arial", Font.BOLD, 16));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        bookIdField = new JTextField(10);
        memberIdField = new JTextField(10);

        gbc.gridy = 0;
        addFormField(formPanel, gbc, "Book ID:", bookIdField);

        gbc.gridy = 1;
        addFormField(formPanel, gbc, "Member ID:", memberIdField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        borrowButton = new JButton("Borrow Book");
        returnButton = new JButton("Return Book");
        refreshButton = new JButton("Refresh");

        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(refreshButton);

        rightPanel.add(formTitle);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(formPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(buttonPanel);

        return rightPanel;
    }
    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent component) {
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private void createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Borrow Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterComboBox = new JComboBox<>(new String[]{"All", "Active", "Overdue", "By Member", "By Book"});
        searchField = new JTextField(15);
        searchButton = new JButton("Search");

        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(filterComboBox);
        filterPanel.add(searchField);
        filterPanel.add(searchButton);

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(filterPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);
    }

    private void createCenterPanel() {
        String[] columns = {
                "ID", "Book", "Member", "Borrow Date", "Due Date", "Return Date", "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        borrowsTable = new JTable(tableModel);
        borrowsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(borrowsTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void setupEventListeners() {
        searchButton.addActionListener(e -> performSearch());
        borrowButton.addActionListener(e -> borrowBook());
        returnButton.addActionListener(e -> returnBook());
        refreshButton.addActionListener(e -> loadAllBorrows());

        filterComboBox.addActionListener(e -> {
            String selected = (String) filterComboBox.getSelectedItem();
            searchField.setEnabled(!"All".equals(selected) && !"Active".equals(selected) && !"Overdue".equals(selected));
        });
    }

    private void loadAllBorrows() {
        clearTable();
        Map<Integer, BorrowRecord> records = borrowRecordService.getAllBorrowRecords();
        records.values().forEach(this::addBorrowToTable);
    }

    private void clearTable() {
        tableModel.setRowCount(0);
    }

    private void addBorrowToTable(BorrowRecord record) {
        tableModel.addRow(new Object[]{
                record.getId(),
                record.getBook().getName(),
                record.getMember().getName(),
                record.getBorrowDate().format(dateFormatter),
                record.getDueDate().format(dateFormatter),
                record.getReturnDate() != null ? record.getReturnDate().format(dateFormatter) : "",
                record.getIsReturned() ? "Returned" : "Active"
        });
    }

    private void performSearch() {
        String filter = (String) filterComboBox.getSelectedItem();
        String searchText = searchField.getText().trim();

        clearTable();
        try {
            switch (filter) {
                case "All":
                    loadAllBorrows();
                    break;
                case "Active":
                    borrowRecordService.getActiveBorrows().values()
                            .forEach(this::addBorrowToTable);
                    break;
                case "Overdue":
                    borrowRecordService.getOverdueBorrows().values()
                            .forEach(this::addBorrowToTable);
                    break;
                case "By Member ID":
                    if (!searchText.isEmpty()) {
                        int memberId = Integer.parseInt(searchText);
                        borrowRecordService.getBorrowRecordsByMemberId(memberId)
                                .forEach(this::addBorrowToTable);
                    }
                    break;
                case "By Book ID":
                    if (!searchText.isEmpty()) {
                        int bookId = Integer.parseInt(searchText);
                        borrowRecordService.getBorrowsByBookId(bookId)
                                .forEach(this::addBorrowToTable);
                    }
                    break;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error performing search: " + ex.getMessage(),
                    "Search Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void borrowBook() {
        try {
            String bookIdText = bookIdField.getText().trim();
            String memberIdText = memberIdField.getText().trim();

            if (bookIdText.isEmpty() || memberIdText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter both Book ID and Member ID",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int bookId = Integer.parseInt(bookIdText);
            int memberId = Integer.parseInt(memberIdText);

            Book book = bookService.getBookById(bookId);
            Member member = memberService.getMemberById(memberId);

            if (book == null || member == null) {
                JOptionPane.showMessageDialog(this,
                        "Invalid Book ID or Member ID",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            BorrowRecord newBorrow = new BorrowRecord();
            newBorrow.setBook(book);
            newBorrow.setMember(member);
            newBorrow.setIsReturned(false);

            borrowRecordService.addBorrowRecord(newBorrow);

            JOptionPane.showMessageDialog(this,
                    "Book borrowed successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            clearForm();
            loadAllBorrows();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numeric IDs",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error borrowing book: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void returnBook() {
        int selectedRow = borrowsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a borrow record to return",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int borrowId = (int) borrowsTable.getValueAt(selectedRow, 0);
            BorrowRecord record = borrowRecordService.getBorrowRecordById(borrowId);

            if (record.getIsReturned()) {
                JOptionPane.showMessageDialog(this,
                        "This book has already been returned",
                        "Already Returned",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            record.setIsReturned(true);
            record.setReturnDate();
            borrowRecordService.updateBorrowRecord(record);

            JOptionPane.showMessageDialog(this,
                    "Book returned successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            loadAllBorrows();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error returning book: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    private void clearForm() {
        bookIdField.setText("");
        memberIdField.setText("");
    }
}