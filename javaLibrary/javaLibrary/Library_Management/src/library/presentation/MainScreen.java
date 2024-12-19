package library.presentation;

import library.buisness.service.IBookService;
import library.buisness.service.IBorrowRecordService;
import library.buisness.service.IMemberService;
import library.presentation.gui.BooksManagementScreen;
import library.presentation.gui.BorrowRecordScreen;
import library.presentation.gui.MembersManagementScreen;
import library.presentation.gui.ReportsScreen;

import javax.swing.*;
import java.awt.*;

public class MainScreen extends JFrame {
    private JPanel mainPanel;
    private final IBookService bookService;
    private final IMemberService memberService;
    private final IBorrowRecordService borrowRecordService;

    public MainScreen(IBookService bookService, IMemberService memberService, IBorrowRecordService borrowRecordService) {
        this.borrowRecordService = borrowRecordService;
        this.memberService = memberService;
        this.bookService = bookService;
        initializeFrame();
        createMainPanel();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Library Management System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel welcomePanel = new JPanel();
        JLabel welcomeLabel = new JLabel("Welcome to Library Management System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel);
        mainPanel.add(welcomePanel, BorderLayout.NORTH);

        // Main Content Panel - Sadece butonlar için
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0)); // Üstten boşluk

        // Buttons Panel
        JPanel buttonsPanel = createButtonsPanel();
        contentPanel.add(buttonsPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 20, 20));

        // Books Management Button
        JButton booksButton = createStyledButton("Books Management", new Color(70, 130, 180));
        booksButton.addActionListener(e -> openBooksManagement());

        // Members Management Button
        JButton membersButton = createStyledButton("Members Management", new Color(60, 179, 113));
        membersButton.addActionListener(e -> openMembersManagement());

        // Borrowing Operations Button
        JButton borrowingButton = createStyledButton("Borrowing Operations", new Color(205, 133, 63));
        borrowingButton.addActionListener(e -> openBorrowingOperations());

        // Reports Button
        JButton reportsButton = createStyledButton("Reports", new Color(148, 0, 211));
        reportsButton.addActionListener(e -> openReports());

        // Add buttons to panel
        buttonsPanel.add(booksButton);
        buttonsPanel.add(membersButton);
        buttonsPanel.add(borrowingButton);
        buttonsPanel.add(reportsButton);

        return buttonsPanel;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 100));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }

    private void openBooksManagement() {
        SwingUtilities.invokeLater(() -> {
            BooksManagementScreen booksScreen =
                    new BooksManagementScreen(bookService);
            booksScreen.setVisible(true);
        });
    }

    private void openMembersManagement() {
        SwingUtilities.invokeLater(() -> {
            MembersManagementScreen membersScreen =
                    new MembersManagementScreen(memberService);
            membersScreen.setVisible(true);
        });
    }
//
    private void openBorrowingOperations() {
        SwingUtilities.invokeLater(() -> {
            BorrowRecordScreen borrowingScreen =
                    new BorrowRecordScreen(borrowRecordService, bookService, memberService);
            borrowingScreen.setVisible(true);
        });
    }

    private void openReports() {
        SwingUtilities.invokeLater(() -> {
            ReportsScreen reportsScreen = new ReportsScreen(
                    bookService,
                    memberService,
                    borrowRecordService
            );
            reportsScreen.setVisible(true);
        });
    }
}
