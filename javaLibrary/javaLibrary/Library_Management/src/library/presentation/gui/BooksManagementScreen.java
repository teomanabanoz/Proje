package library.presentation.gui;

import library.buisness.service.IBookService;
import library.domain.Book;
import library.domain.enums.BookCategory;
import library.domain.enums.BookStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class BooksManagementScreen extends JFrame{
    private final IBookService bookService;
    private JPanel mainPanel;
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchCriteriaBox;

    private JButton searchButton;
    private JButton saveButton;
    private JButton clearButton;
    private JButton deleteButton;
    private JButton refreshButton;

    private JTextField nameField;
    private JTextField authorField;
    private JTextField isbnField;
    private JComboBox<BookCategory> categoryBox;
    private JTextField publisherField;
    private JTextField yearField;
    private Book selectedBook;


    public BooksManagementScreen(IBookService bookService) {
        this.bookService = bookService;

        initializeFrame();
        createMainPanel();
        setupEventListeners();
        loadAllBooks();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Books Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel titlePanel = createTitlePanel();
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(650);

        JPanel leftPanel = createTablePanel();
        splitPane.setLeftComponent(leftPanel);

        JPanel rightPanel = createFormPanel();
        splitPane.setRightComponent(rightPanel);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Books Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchField = new JTextField(20);
        searchCriteriaBox = new JComboBox<>(new String[]{ "Author", "Category"});
        searchButton = new JButton("Search");

        searchPanel.add(new JLabel("Search by:"));
        searchPanel.add(searchCriteriaBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        titlePanel.add(searchPanel, BorderLayout.EAST);
        return titlePanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));

        String[] columns = {"ID", "Name", "Author", "Category", "Publisher", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        booksTable = new JTable(tableModel);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(booksTable);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        refreshButton = new JButton("Refresh");
        deleteButton = new JButton("Delete Selected");

        JButton markLostButton = new JButton("Mark as Lost");
        JButton markDamagedButton = new JButton("Mark as Damaged");
        JButton markMaintenanceButton = new JButton("Mark for Maintenance");
        JButton markAvailableButton = new JButton("Mark as Available");

        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPanel.add(markLostButton);
        buttonPanel.add(markDamagedButton);
        buttonPanel.add(markMaintenanceButton);
        buttonPanel.add(markAvailableButton);

        markLostButton.addActionListener(e -> updateBookStatus(BookStatus.LOST));
        markDamagedButton.addActionListener(e -> updateBookStatus(BookStatus.DAMAGED));
        markMaintenanceButton.addActionListener(e -> updateBookStatus(BookStatus.UNDER_MAINTENANCE));
        markAvailableButton.addActionListener(e -> updateBookStatus(BookStatus.AVAILABLE));

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        return tablePanel;
    }
    private void updateBookStatus(BookStatus newStatus) {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow >= 0) {
            int bookId = (int) booksTable.getValueAt(selectedRow, 0);
            Book book = bookService.getBookById(bookId);

            if (book.getStatus() == BookStatus.BORROWED.getDisplayName() && newStatus != BookStatus.AVAILABLE) {
                JOptionPane.showMessageDialog(this,
                        "Cannot change status of borrowed book. Book must be returned first.",
                        "Status Update Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                book.setStatus(newStatus);
                bookService.updateBook(book);
                loadAllBooks();
                JOptionPane.showMessageDialog(this,
                        "Book status updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error updating book status: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a book to update its status",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JLabel formTitle = new JLabel("Add/Edit Book");
        formTitle.setFont(new Font("Arial", Font.BOLD, 16));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        nameField = new JTextField(20);
        authorField = new JTextField(20);
        categoryBox = new JComboBox<>(BookCategory.values());
        publisherField = new JTextField(20);

        gbc.gridy = 0;
        addFormField(fieldsPanel, gbc, "Name:", nameField);
        gbc.gridy++;
        addFormField(fieldsPanel, gbc, "Author:", authorField);
        gbc.gridy++;
        addFormField(fieldsPanel, gbc, "Category:", categoryBox);
        gbc.gridy++;
        addFormField(fieldsPanel, gbc, "Publisher:", publisherField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        saveButton = new JButton("Save");
        clearButton = new JButton("Clear");
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);

        formPanel.add(formTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(fieldsPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(buttonPanel);

        return formPanel;
    }


    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field) {
        gbc.gridx = 0;
        JLabel label = new JLabel(labelText);
        panel.add(label, gbc);

        gbc.gridx = 1;
        field.setPreferredSize(new Dimension(200, 25));
        panel.add(field, gbc);
    }

    private void loadAllBooks() {
        clearTable();
        List<Book> books = bookService.getAllBooks();
        for (Book book : books) {
            addBookToTable(book);
        }
    }

    private void clearTable() {
        tableModel.setRowCount(0);
    }

    private void addBookToTable(Book book) {
        tableModel.addRow(new Object[]{
                book.getId(),
                book.getName(),
                book.getAuthor(),
                book.getCategory(),
                book.getPublisher(),
                book.getStatus()
        });
    }

    private void setupEventListeners() {
        searchButton.addActionListener(e -> performSearch());

        booksTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = booksTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int bookId = (int) booksTable.getValueAt(selectedRow, 0);
                    loadBookForEditing(bookId);
                }
            }
        });

        saveButton.addActionListener(e -> saveBook());

        clearButton.addActionListener(e -> clearForm());

        deleteButton.addActionListener(e -> deleteSelectedBook());

        refreshButton.addActionListener(e -> loadAllBooks());
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        String criteria = (String) searchCriteriaBox.getSelectedItem();

        if (searchText.isEmpty()) {
            loadAllBooks();
            return;
        }

        clearTable();
        List<Book> results;

        switch (criteria) {
            case "Author":
                results = bookService.findBooksByAuthor(searchText);
                break;
            case "Category":
                results = bookService.findBooksByCategory(searchText);
                break;
            case "Publisher":
                results = bookService.findBooksByPublisher(searchText);
                break;
            default:
                results = bookService.getAllBooks();
        }

        for (Book book : results) {
            addBookToTable(book);
        }
    }

    private void loadBookForEditing(int bookId) {
        selectedBook = bookService.getBookById(bookId);
        if (selectedBook != null) {
            nameField.setText(selectedBook.getName());
            authorField.setText(selectedBook.getAuthor());
            categoryBox.setSelectedItem(selectedBook.getCategory());
            publisherField.setText(selectedBook.getPublisher());
        }
    }

    private void saveBook() {
        if (!validateForm()) {
            return;
        }

        try {
            Book book = selectedBook != null ? selectedBook : new Book();
            book.setName(nameField.getText().trim());
            book.setAuthor(authorField.getText().trim());
            book.setCategory((BookCategory) categoryBox.getSelectedItem());
            book.setPublisher(publisherField.getText().trim());

            if (selectedBook == null) {
                book.setStatus(BookStatus.AVAILABLE);
                bookService.addBook(book);
                JOptionPane.showMessageDialog(this, "Book added successfully!");
            } else {
                bookService.updateBook(book);
                JOptionPane.showMessageDialog(this, "Book updated successfully!");
            }

            clearForm();
            loadAllBooks();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving book: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Book name is required!",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return false;
        }
        if (authorField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Author name is required!",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            authorField.requestFocus();
            return false;
        }
        if (publisherField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Publisher name is required!",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            publisherField.requestFocus();
            return false;
        }
        return true;
    }

    private void deleteSelectedBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow >= 0) {
            int bookId = (int) booksTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this book?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    bookService.deleteBook(bookId);
                    loadAllBooks();
                    clearForm();
                    JOptionPane.showMessageDialog(this, "Book deleted successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error deleting book: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void clearForm() {
        selectedBook = null;
        nameField.setText("");
        authorField.setText("");
        categoryBox.setSelectedIndex(0);
        publisherField.setText("");
    }
}
