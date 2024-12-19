package library.presentation.gui;

import library.buisness.service.IBookService;
import library.buisness.service.IBorrowRecordService;
import library.buisness.service.IMemberService;
import library.domain.BorrowRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ReportsScreen extends JFrame {
    private final IBookService bookService;
    private final IMemberService memberService;
    private final IBorrowRecordService borrowRecordService;

    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ReportsScreen(IBookService bookService,
                         IMemberService memberService,
                         IBorrowRecordService borrowRecordService) {
        this.bookService = bookService;
        this.memberService = memberService;
        this.borrowRecordService = borrowRecordService;

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        setTitle("Library Reports");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("General Statistics", createGeneralStatsPanel());
        tabbedPane.addTab("Borrowing Statistics", createBorrowingStatsPanel());
        tabbedPane.addTab("Overdue Books", createOverdueBooksPanel());
        tabbedPane.addTab("Member Reports", createMemberReportsPanel());
        tabbedPane.addTab("Book Reports", createBookReportsPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createGeneralStatsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addStatCard(panel, gbc, 0, 0, "Total Books", String.valueOf(bookService.getAllBooks().size()));
        addStatCard(panel, gbc, 1, 0, "Total Members", String.valueOf(memberService.getAllMembers().size()));
        addStatCard(panel, gbc, 0, 1, "Active Borrows", String.valueOf(borrowRecordService.getActiveBorrowsCount()));
        addStatCard(panel, gbc, 1, 1, "Overdue Books", String.valueOf(borrowRecordService.getOverdueBorrowsCount()));

        return panel;
    }

    private JPanel createBorrowingStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Book", "Member", "Borrow Date", "Due Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        Map<Integer, BorrowRecord> activeRecords = borrowRecordService.getActiveBorrows();
        for (BorrowRecord record : activeRecords.values()) {
            model.addRow(new Object[]{
                    record.getBook().getName(),
                    record.getMember().getName(),
                    record.getBorrowDate().format(dateFormatter),
                    record.getDueDate().format(dateFormatter),
                    record.getIsReturned() ? "Returned" : "Active"
            });
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createOverdueBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Book", "Member", "Borrow Date", "Due Date", "Days Overdue"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        Map<Integer, BorrowRecord> overdueRecords = borrowRecordService.getOverdueBorrows();
        LocalDate today = LocalDate.now();

        for (BorrowRecord record : overdueRecords.values()) {
            long daysOverdue = record.getDueDate().until(today).getDays();
            model.addRow(new Object[]{
                    record.getBook().getName(),
                    record.getMember().getName(),
                    record.getBorrowDate().format(dateFormatter),
                    record.getDueDate().format(dateFormatter),
                    daysOverdue
            });
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMemberReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField memberIdField = new JTextField(10);
        JButton searchButton = new JButton("Search Member");
        searchPanel.add(new JLabel("Member ID:"));
        searchPanel.add(memberIdField);
        searchPanel.add(searchButton);

        String[] columns = {"Book", "Borrow Date", "Due Date", "Return Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        searchButton.addActionListener(e -> {
            try {
                int memberId = Integer.parseInt(memberIdField.getText());
                List<BorrowRecord> memberRecords = borrowRecordService.getBorrowRecordsByMemberId(memberId);

                model.setRowCount(0);
                for (BorrowRecord record : memberRecords) {
                    model.addRow(new Object[]{
                            record.getBook().getName(),
                            record.getBorrowDate().format(dateFormatter),
                            record.getDueDate().format(dateFormatter),
                            record.getReturnDate() != null ? record.getReturnDate().format(dateFormatter) : "",
                            record.getIsReturned() ? "Returned" : "Active"
                    });
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel,
                        "Please enter a valid Member ID",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBookReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField bookIdField = new JTextField(10);
        JButton searchButton = new JButton("Search Book");
        searchPanel.add(new JLabel("Book ID:"));
        searchPanel.add(bookIdField);
        searchPanel.add(searchButton);

        String[] columns = {"Member", "Borrow Date", "Due Date", "Return Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        searchButton.addActionListener(e -> {
            try {
                int bookId = Integer.parseInt(bookIdField.getText());
                List<BorrowRecord> bookRecords = borrowRecordService.getBorrowsByBookId(bookId);

                model.setRowCount(0);
                for (BorrowRecord record : bookRecords) {
                    model.addRow(new Object[]{
                            record.getMember().getName(),
                            record.getBorrowDate().format(dateFormatter),
                            record.getDueDate().format(dateFormatter),
                            record.getReturnDate() != null ? record.getReturnDate().format(dateFormatter) : "",
                            record.getIsReturned() ? "Returned" : "Active"
                    });
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel,
                        "Please enter a valid Book ID",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void addStatCard(JPanel panel, GridBagConstraints gbc, int x, int y, String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(valueLabel);

        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(card, gbc);
    }
}