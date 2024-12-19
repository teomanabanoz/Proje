package library.presentation.gui;

import library.buisness.service.IMemberService;
import library.domain.Member;
import library.domain.enums.MemberType;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Set;

public class MembersManagementScreen extends JFrame {
    private final IMemberService memberService;
    private JPanel mainPanel;
    private JTable membersTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchCriteriaBox;

    private JButton searchButton;
    private JButton saveButton;
    private JButton clearButton;
    private JButton deleteButton;
    private JButton refreshButton;

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<MemberType> typeComboBox;
    private JComboBox<String> genderComboBox;
    private Member selectedMember;

    public MembersManagementScreen(IMemberService memberService) {
        this.memberService = memberService;
        initializeComponents();
        setupEventListeners();
        loadAllMembers();
        setVisible(true);
    }

    private void initializeComponents() {
        initializeFrame();
        createMainPanel();
    }

    private void initializeFrame() {
        setTitle("Members Management");
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
        JLabel titleLabel = new JLabel("Members Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchField = new JTextField(20);
        searchCriteriaBox = new JComboBox<>(new String[]{"Name", "Email", "Phone", "Type", "Gender"});
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

        String[] columns = {"ID", "Name", "Email", "Phone", "Type", "Gender"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        membersTable = new JTable(tableModel);
        membersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(membersTable);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        refreshButton = new JButton("Refresh");
        deleteButton = new JButton("Delete Selected");

        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        return tablePanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JLabel formTitle = new JLabel("Add/Edit Member");
        formTitle.setFont(new Font("Arial", Font.BOLD, 16));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        typeComboBox = new JComboBox<>(MemberType.values());
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});

        gbc.gridy = 0;
        addFormField(fieldsPanel, gbc, "Name:", nameField);
        gbc.gridy++;
        addFormField(fieldsPanel, gbc, "Email:", emailField);
        gbc.gridy++;
        addFormField(fieldsPanel, gbc, "Phone:", phoneField);
        gbc.gridy++;
        addFormField(fieldsPanel, gbc, "Type:", typeComboBox);
        gbc.gridy++;
        addFormField(fieldsPanel, gbc, "Gender:", genderComboBox);

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

    private void setupEventListeners() {
        if (searchButton != null) {
            searchButton.addActionListener(e -> performSearch());
        }

        if (saveButton != null) {
            saveButton.addActionListener(e -> saveMember());
        }

        if (clearButton != null) {
            clearButton.addActionListener(e -> clearForm());
        }

        if (deleteButton != null) {
            deleteButton.addActionListener(e -> deleteSelectedMember());
        }

        if (refreshButton != null) {
            refreshButton.addActionListener(e -> loadAllMembers());
        }

        if (membersTable != null) {
            membersTable.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = membersTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        int memberId = (int) membersTable.getValueAt(selectedRow, 0);
                        loadMemberForEditing(memberId);
                    }
                }
            });
        }
    }

    private void loadAllMembers() {
        clearTable();
        Set<Member> members = memberService.getAllMembers();
        for (Member member : members) {
            addMemberToTable(member);
        }
    }

    private void clearTable() {
        tableModel.setRowCount(0);
    }

    private void addMemberToTable(Member member) {
        tableModel.addRow(new Object[]{
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.getType(),
                member.getGender()
        });
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        String criteria = (String) searchCriteriaBox.getSelectedItem();

        if (searchText.isEmpty()) {
            loadAllMembers();
            return;
        }

        clearTable();
        Set<Member> results;

        try {
            switch (criteria) {
                case "Name":
                    results = memberService.findMembersByName(searchText);
                    break;
                case "Email":
                    results = Set.of(memberService.findMemberByEmail(searchText));
                    break;
                case "Phone":
                    results = Set.of(memberService.findMemberByPhone(searchText));
                    break;
                case "Type":
                    results = memberService.findMembersByType(MemberType.valueOf(searchText.toUpperCase()));
                    break;
                case "Gender":
                    results = memberService.findMembersByGender(searchText);
                    break;
                default:
                    results = memberService.getAllMembers();
            }

            for (Member member : results) {
                addMemberToTable(member);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error searching members: " + ex.getMessage(),
                    "Search Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveMember() {
        try {
            if (!validateForm()) {
                return;
            }

            Member member = selectedMember != null ? selectedMember : new Member();
            member.setName(nameField.getText());
            member.setEmail(emailField.getText());
            member.setPhone(phoneField.getText());
            member.setType((MemberType) typeComboBox.getSelectedItem());
            member.setGender((String) genderComboBox.getSelectedItem());

            if (selectedMember == null) {
                memberService.addMember(member);
                JOptionPane.showMessageDialog(this, "Member added successfully!");
            } else {
                memberService.updateMember(member);
                JOptionPane.showMessageDialog(this, "Member updated successfully!");
            }

            clearForm();
            loadAllMembers();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving member: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadMemberForEditing(int memberId) {
        selectedMember = memberService.getMemberById(memberId);
        if (selectedMember != null) {
            nameField.setText(selectedMember.getName());
            emailField.setText(selectedMember.getEmail());
            phoneField.setText(selectedMember.getPhone());
            typeComboBox.setSelectedItem(selectedMember.getType());
            genderComboBox.setSelectedItem(selectedMember.getGender());
        }
    }

    private void deleteSelectedMember() {
        int selectedRow = membersTable.getSelectedRow();
        if (selectedRow >= 0) {
            int memberId = (int) membersTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this member?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    memberService.deleteMember(memberId);
                    loadAllMembers();
                    clearForm();
                    JOptionPane.showMessageDialog(this, "Member deleted successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error deleting member: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void clearForm() {
        selectedMember = null;
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        typeComboBox.setSelectedIndex(0);
        genderComboBox.setSelectedIndex(0);
    }

    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required!");
            return false;
        }
        if (emailField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email is required!");
            return false;
        }
        if (phoneField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone is required!");
            return false;
        }
        return true;
    }
}
