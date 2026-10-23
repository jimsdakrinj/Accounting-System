import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.text.NumberFormat;

/*
 * Modern Accounting System with Material Design UI
 * Features:
 * - Gradient backgrounds
 * - Rounded panels with shadow effects
 * - Material design buttons
 * - Modern color scheme
 * - Card-based layouts
 * - Improved typography
 * - Emoji icons for better UX
 * - Smooth modern styling
 */

// ===================== UTILITY CLASSES =====================

class RoundedPanel extends JPanel {
    private Color backgroundColor;
    private int cornerRadius;
    private boolean hasShadow;

    public RoundedPanel(int radius, Color bgColor, boolean shadow) {
        super();
        this.cornerRadius = radius;
        this.backgroundColor = bgColor;
        this.hasShadow = shadow;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Shadow effect
        if (hasShadow) {
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, cornerRadius + 4, cornerRadius + 4);
        }
        
        // Main background
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        g2.dispose();
    }
}

class GradientPanel extends JPanel {
    private Color color1;
    private Color color2;
    private boolean vertical;

    public GradientPanel(Color color1, Color color2, boolean vertical) {
        this.color1 = color1;
        this.color2 = color2;
        this.vertical = vertical;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        GradientPaint gradient = new GradientPaint(
            0, 0, color1,
            vertical ? 0 : getWidth(), vertical ? getHeight() : 0, color2
        );
        g2.setPaint(gradient);
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        g2.dispose();
    }
}

class ModernButton extends JButton {
    private Color normalBg;
    private Color hoverBg;
    private Color pressedBg;
    private int radius;

    public ModernButton(String text, Color normalBg, Color hoverBg, Color pressedBg, int radius) {
        super(text);
        this.normalBg = normalBg;
        this.hoverBg = hoverBg;
        this.pressedBg = pressedBg;
        this.radius = radius;
        
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBg);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalBg);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedBg);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverBg);
            }
        });
        
        setBackground(normalBg);
        setForeground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        
        // Border
        g2.setColor(getBackground().darker());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // No additional border
    }
}

// ===================== MAIN ACCOUNTING APP =====================

// ===================== MAIN ACCOUNTING APP =====================

public class AccountingApp extends JFrame {
    // Core data
    private List<Account> accounts;
    private List<Transaction> transactions;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // Table models for UI
    private DefaultTableModel transactionsTableModel;
    private DefaultTableModel accountsTableModel;
    private DefaultTableModel journalTableModel;
    private DefaultTableModel ledgerTableModel;
    private DefaultTableModel assetsTableModel;
    private DefaultTableModel liabilitiesTableModel;

    // UI controls that need cross-method access
    private JComboBox<String> ledgerAccountCombo;
    private JComboBox<String> debitComboGlobal;
    private JComboBox<String> creditComboGlobal;

    // Balance sheet labels
    private JLabel totalAssetsLabel;
    private JLabel totalLiabLabel;

    // Modern color scheme
    private final Color PRIMARY = new Color(74, 20, 140);
    private final Color SECONDARY = new Color(0, 150, 136);
    private final Color ACCENT = new Color(255, 82, 82);
    private final Color ACCENT_GREEN = new Color(46, 125, 50);
    private final Color BACKGROUND = new Color(248, 249, 250);
    private final Color CARD_BG = new Color(255, 255, 255);
    private final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private final Color TEXT_SECONDARY = new Color(100, 100, 100);

    public AccountingApp() {
        sdf.setLenient(false);

        accounts = new ArrayList<>();
        transactions = new ArrayList<>();
        addPredefinedAccounts();

        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);

        add(createModernHeader(), BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BACKGROUND);
        tabbedPane.setForeground(PRIMARY);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setOpaque(false);
        
        // Style tabs
        UIManager.put("TabbedPane.selected", new Color(74, 20, 140));
        
        tabbedPane.addTab(" Add Transaction", createAddTransactionPanel());
        tabbedPane.addTab(" Transactions", createTransactionsPanel());
        tabbedPane.addTab(" Accounts", createAccountsPanel());
        tabbedPane.addTab(" General Journal", createGeneralJournalPanel());
        tabbedPane.addTab(" General Ledger", createGeneralLedgerPanel());
        tabbedPane.addTab(" Balance Sheet", createBalanceSheetPanel());

        add(tabbedPane, BorderLayout.CENTER);
        
        setTitle("Accounting System");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        refreshAllViews();
    }

    // ===================== MODERN HEADER =====================
    private JPanel createModernHeader() {
        RoundedPanel header = new RoundedPanel(0, PRIMARY, false);
        header.setLayout(new BorderLayout(20, 0));
        header.setPreferredSize(new Dimension(0, 80));

        // Left side
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));

        JLabel iconLabel = new JLabel("");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        iconLabel.setForeground(Color.WHITE);

        JLabel companyName = new JLabel(" Accounting System");
        companyName.setForeground(Color.WHITE);
        companyName.setFont(new Font("Segoe UI", Font.BOLD, 22));

        leftPanel.add(companyName);

        // Right side
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 25));

        ModernButton aboutBtn = new ModernButton("About", SECONDARY, SECONDARY.brighter(), SECONDARY.darker(), 10);
        ModernButton openFolderBtn = new ModernButton("Open Folder", SECONDARY, SECONDARY.brighter(), SECONDARY.darker(), 10);
        ModernButton saveBtn = new ModernButton("Save CSV", ACCENT_GREEN, ACCENT_GREEN.brighter(), ACCENT_GREEN.darker(), 10);

        JButton menuBtn = new JButton("");
        menuBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        menuBtn.setBackground(SECONDARY);
        menuBtn.setForeground(Color.WHITE);
        menuBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        menuBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuBtn.setFocusPainted(false);
        menuBtn.setContentAreaFilled(false);
        menuBtn.setOpaque(true);
        menuBtn.setText("");
        
        menuBtn.setVisible(false);

        aboutBtn.addActionListener(e -> showAbout());
        openFolderBtn.addActionListener(e -> openFolderAction());
        saveBtn.addActionListener(e -> saveCSVAction());

        rightPanel.add(aboutBtn);
        rightPanel.add(openFolderBtn);
        rightPanel.add(saveBtn);
        rightPanel.add(menuBtn);

        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private void openFolderAction() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDir = chooser.getSelectedFile();
            JOptionPane.showMessageDialog(this, "Opened folder: " + selectedDir.getAbsolutePath());
        }
    }

    private void saveCSVAction() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("accounts.csv"));
        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (PrintWriter pw = new PrintWriter(file)) {
                pw.println("Account Name,Type,Balance");
                for (Account a : accounts) {
                    pw.println(a.getName() + "," + a.getType() + "," + a.getBalance());
                }
                JOptionPane.showMessageDialog(this, "CSV saved: " + file.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving CSV: " + ex.getMessage());
            }
        }
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this,
            "Accounting System Pro v2.0\n\n" +
            "A modern accounting application with:\n" +
            " Transaction management\n" +
            " Account management\n" +
            " General Journal and Ledger\n" +
            " Balance Sheet\n" +
            " Financial Reports\n\n" +
            "Modern GUI with Material Design\n" +
            "Made with Java Swing\n",
            "About",
            JOptionPane.INFORMATION_MESSAGE);
    }

    // ===================== MODERN ADD TRANSACTION PANEL =====================
    private JPanel createAddTransactionPanel() {
        RoundedPanel panel = new RoundedPanel(0, CARD_BG, false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BorderLayout());

        RoundedPanel form = new RoundedPanel(20, new Color(255, 255, 255), true);
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        form.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel title = new JLabel("Add New Transaction");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(PRIMARY);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        form.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        form.add(createLabel("Date (YYYY-MM-DD):"), gbc);
        
        JTextField dateField = new JTextField(sdf.format(new Date()));
        styleTextField(dateField);
        gbc.gridx = 1;
        form.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        form.add(createLabel("Description:"), gbc);
        
        JTextField descField = new JTextField();
        styleTextField(descField);
        gbc.gridx = 1;
        form.add(descField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        form.add(createLabel("Debit Account:"), gbc);
        
        debitComboGlobal = new JComboBox<>();
        styleComboBox(debitComboGlobal);
        gbc.gridx = 1;
        form.add(debitComboGlobal, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        form.add(createLabel("Credit Account:"), gbc);
        
        creditComboGlobal = new JComboBox<>();
        styleComboBox(creditComboGlobal);
        gbc.gridx = 1;
        form.add(creditComboGlobal, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        form.add(createLabel("Amount:"), gbc);
        
        JTextField amountField = new JTextField();
        styleTextField(amountField);
        gbc.gridx = 1;
        form.add(amountField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        ModernButton postBtn = new ModernButton("Post Transaction", PRIMARY, PRIMARY.brighter(), PRIMARY.darker(), 15);
        ModernButton clearBtn = new ModernButton("Clear Fields", SECONDARY, SECONDARY.brighter(), SECONDARY.darker(), 15);
        
        postBtn.setPreferredSize(new Dimension(180, 45));
        clearBtn.setPreferredSize(new Dimension(150, 45));

        buttonPanel.add(postBtn);
        buttonPanel.add(clearBtn);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(buttonPanel, gbc);
        
        panel.add(form, BorderLayout.CENTER);

        refreshAccountCombos();

        postBtn.addActionListener(e -> {
            String dateStr = dateField.getText().trim();
            String desc = descField.getText().trim();
            String debitAccName = (String) debitComboGlobal.getSelectedItem();
            String creditAccName = (String) creditComboGlobal.getSelectedItem();
            String amtStr = amountField.getText().trim();

            Date date;
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amtStr);
                if (amount <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Amount must be a number greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (debitAccName == null || creditAccName == null) {
                JOptionPane.showMessageDialog(this, "Select both debit and credit accounts.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (debitAccName.equals(creditAccName)) {
                JOptionPane.showMessageDialog(this, "Debit and credit accounts cannot be the same.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Account debitAcc = getAccountByName(debitAccName);
            Account creditAcc = getAccountByName(creditAccName);
            if (debitAcc == null || creditAcc == null) {
                JOptionPane.showMessageDialog(this, "Selected account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            debitAcc.applyDebit(amount);
            creditAcc.applyCredit(amount);

            Transaction tx = new Transaction(date, desc, debitAccName, creditAccName, amount);
            transactions.add(tx);
            transactions.sort(Comparator.comparing(Transaction::getDate));

            refreshAllViews();

            JOptionPane.showMessageDialog(this, "Transaction posted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dateField.setText(sdf.format(new Date()));
            descField.setText("");
            amountField.setText("");
            if (debitComboGlobal.getItemCount() > 0) debitComboGlobal.setSelectedIndex(0);
            if (creditComboGlobal.getItemCount() > 0) creditComboGlobal.setSelectedIndex(0);
        });

        clearBtn.addActionListener(e -> {
            dateField.setText(sdf.format(new Date()));
            descField.setText("");
            amountField.setText("");
            if (debitComboGlobal.getItemCount() > 0) debitComboGlobal.setSelectedIndex(0);
            if (creditComboGlobal.getItemCount() > 0) creditComboGlobal.setSelectedIndex(0);
        });

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(TEXT_PRIMARY);
        return lbl;
    }

    private void styleTextField(JTextComponent field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setBackground(Color.WHITE);
        field.setPreferredSize(new Dimension(200, 35));
    }

    private void styleComboBox(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        combo.setBackground(Color.WHITE);
        combo.setPreferredSize(new Dimension(200, 35));
    }

    // ===================== TRANSACTIONS PANEL =====================
    private JPanel createTransactionsPanel() {
        RoundedPanel panel = new RoundedPanel(0, CARD_BG, false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BorderLayout());

        RoundedPanel topPanel = new RoundedPanel(15, new Color(255, 255, 255), false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JLabel title = new JLabel("All Transactions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(PRIMARY);
        topPanel.add(title);

        JTextField searchField = new JTextField(30);
        styleTextField(searchField);
        topPanel.add(searchField);

        ModernButton searchBtn = new ModernButton("Search", PRIMARY, PRIMARY.brighter(), PRIMARY.darker(), 10);
        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim().toLowerCase();
            filterTransactions(query);
        });
        searchBtn.setPreferredSize(new Dimension(100, 35));
        topPanel.add(searchBtn);

        searchField.addActionListener(e -> filterTransactions(searchField.getText().trim().toLowerCase()));

        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Date", "Description", "Debit Account", "Credit Account", "Amount"};
        transactionsTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = createModernTable(transactionsTableModel);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JTable createModernTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(230, 230, 230));
        
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(PRIMARY);
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        
        // Cell renderer
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                
                if (row % 2 == 0) {
                    c.setBackground(new Color(252, 252, 252));
                } else {
                    c.setBackground(Color.WHITE);
                }
                
                if (isSelected) {
                    c.setBackground(new Color(74, 20, 140, 20));
                    c.setForeground(PRIMARY);
                }
                
                if (value instanceof String) {
                    String strValue = (String)value;
                    if (strValue.startsWith("$") || 
                        strValue.matches("^-?\\d+\\.\\d{2}$") ||
                        (strValue.startsWith("(") && strValue.endsWith(")"))) {
                        ((JLabel)c).setHorizontalAlignment(SwingConstants.RIGHT);
                    }
                }
                
                return c;
            }
        });
        
        return table;
    }

    private void filterTransactions(String query) {
        transactionsTableModel.setRowCount(0);
        for (int i = transactions.size()-1; i >= 0; i--) {
            Transaction tx = transactions.get(i);
            String dateStr = sdf.format(tx.getDate());
            if (query.isEmpty()
                    || dateStr.contains(query)
                    || tx.getDescription().toLowerCase().contains(query)
                    || tx.getDebitAccount().toLowerCase().contains(query)
                    || tx.getCreditAccount().toLowerCase().contains(query)) {
                transactionsTableModel.addRow(new Object[]{
                        dateStr,
                        tx.getDescription(),
                        tx.getDebitAccount(),
                        tx.getCreditAccount(),
                        formatAccounting(tx.getAmount())
                });
            }
        }
    }

    // ===================== ACCOUNTS PANEL =====================
    private JPanel createAccountsPanel() {
        RoundedPanel panel = new RoundedPanel(0, CARD_BG, false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BorderLayout());

        RoundedPanel topPanel = new RoundedPanel(15, new Color(255, 255, 255), false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JLabel title = new JLabel("Account Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(PRIMARY);
        topPanel.add(title);

        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Account Name", "Type", "Current Balance"};
        accountsTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = createModernTable(accountsTableModel);

        // Add account form
        RoundedPanel addPanel = new RoundedPanel(15, new Color(255, 255, 255), false);
        addPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        addPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel addTitle = new JLabel("Add New Account");
        addTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addTitle.setForeground(PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        addPanel.add(addTitle, gbc);

        JTextField nameField = new JTextField();
        styleTextField(nameField);
        
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Asset", "Liability", "Owner's Equity", "Revenue", "Expense"});
        styleComboBox(typeCombo);
        
        JTextField balanceField = new JTextField();
        styleTextField(balanceField);
        
        ModernButton addBtn = new ModernButton("Add Account", ACCENT_GREEN, ACCENT_GREEN.brighter(), ACCENT_GREEN.darker(), 10);
        addBtn.setPreferredSize(new Dimension(130, 35));

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        addPanel.add(createLabel("Name:"), gbc);
        gbc.gridx = 1;
        addPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        addPanel.add(createLabel("Type:"), gbc);
        gbc.gridx = 1;
        addPanel.add(typeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        addPanel.add(createLabel("Initial Balance:"), gbc);
        gbc.gridx = 1;
        addPanel.add(balanceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnWrapper.setOpaque(false);
        btnWrapper.add(addBtn);
        addPanel.add(btnWrapper, gbc);

        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter account name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (getAccountByName(name) != null) {
                JOptionPane.showMessageDialog(this, "An account with this name already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String type = (String) typeCombo.getSelectedItem();
            double initBal = 0.0;
            if (!balanceField.getText().trim().isEmpty()) {
                try {
                    initBal = Double.parseDouble(balanceField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid initial balance. Use a number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            accounts.add(new Account(name, type, initBal));
            refreshAllViews();
            nameField.setText("");
            balanceField.setText("");
            typeCombo.setSelectedIndex(0);
            JOptionPane.showMessageDialog(this, "Account added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(addPanel, BorderLayout.SOUTH);
        return panel;
    }

    // ===================== JOURNAL PANEL =====================
    private JPanel createGeneralJournalPanel() {
        RoundedPanel panel = new RoundedPanel(0, CARD_BG, false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BorderLayout());

        RoundedPanel topPanel = new RoundedPanel(15, new Color(255, 255, 255), false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JLabel title = new JLabel("General Journal");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(PRIMARY);
        topPanel.add(title);

        panel.add(topPanel, BorderLayout.NORTH);

        String[] cols = {"Date", "Description", "Account", "Debit", "Credit"};
        journalTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = createModernTable(journalTableModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    // ===================== LEDGER PANEL =====================
    private JPanel createGeneralLedgerPanel() {
        RoundedPanel panel = new RoundedPanel(0, CARD_BG, false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BorderLayout());

        RoundedPanel topPanel = new RoundedPanel(15, new Color(255, 255, 255), false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JLabel title = new JLabel("General Ledger");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(PRIMARY);
        topPanel.add(title);

        panel.add(topPanel, BorderLayout.NORTH);

        ledgerAccountCombo = new JComboBox<>();
        refreshLedgerAccountCombo();
        
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        comboPanel.setOpaque(false);
        comboPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        comboPanel.setBackground(new Color(255, 255, 255));
        
        JLabel selectLabel = new JLabel("Select Account:");
        selectLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        selectLabel.setForeground(PRIMARY);
        
        styleComboBox(ledgerAccountCombo);
        
        comboPanel.add(selectLabel);
        comboPanel.add(ledgerAccountCombo);

        String[] cols = {"Date", "Description", "Debit Account", "Credit Account", "Amount", "Running Balance"};
        ledgerTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = createModernTable(ledgerTableModel);

        ledgerAccountCombo.addActionListener(e -> {
            String acc = (String) ledgerAccountCombo.getSelectedItem();
            if (acc != null) updateGeneralLedgerTable(acc);
        });

        panel.add(comboPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    // ===================== BALANCE SHEET PANEL =====================
    private JPanel createBalanceSheetPanel() {
        RoundedPanel panel = new RoundedPanel(0, CARD_BG, false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new GridLayout(1, 2, 20, 20));

        // ASSETS PANEL
        RoundedPanel assetsPanel = new RoundedPanel(15, new Color(255, 255, 255), false);
        assetsPanel.setLayout(new BorderLayout());
        
        RoundedPanel assetsHeader = new RoundedPanel(15, PRIMARY, false);
        assetsHeader.setPreferredSize(new Dimension(0, 50));
        assetsHeader.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        
        JLabel assetsTitle = new JLabel("Assets");
        assetsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        assetsTitle.setForeground(Color.WHITE);
        assetsHeader.add(assetsTitle);
        
        assetsPanel.add(assetsHeader, BorderLayout.NORTH);
        
        String[] assetCols = {"Account Name", "Amount"};
        assetsTableModel = new DefaultTableModel(assetCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable assetsTable = createModernTable(assetsTableModel);
        assetsPanel.add(new JScrollPane(assetsTable), BorderLayout.CENTER);
        
        totalAssetsLabel = new JLabel("", SwingConstants.RIGHT);
        totalAssetsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalAssetsLabel.setForeground(PRIMARY);
        totalAssetsLabel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        totalAssetsLabel.setBackground(new Color(248, 249, 250));
        totalAssetsLabel.setOpaque(true);
        assetsPanel.add(totalAssetsLabel, BorderLayout.SOUTH);

        // LIABILITIES AND EQUITY PANEL
        RoundedPanel liabilitiesPanel = new RoundedPanel(15, new Color(255, 255, 255), false);
        liabilitiesPanel.setLayout(new BorderLayout());
        
        RoundedPanel liabHeader = new RoundedPanel(15, SECONDARY, false);
        liabHeader.setPreferredSize(new Dimension(0, 50));
        liabHeader.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        
        JLabel liabTitle = new JLabel("Liabilities & Owner's Equity");
        liabTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        liabTitle.setForeground(Color.WHITE);
        liabHeader.add(liabTitle);
        
        liabilitiesPanel.add(liabHeader, BorderLayout.NORTH);
        
        String[] liabCols = {"Account Name", "Amount"};
        liabilitiesTableModel = new DefaultTableModel(liabCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable liabTable = createModernTable(liabilitiesTableModel);
        liabilitiesPanel.add(new JScrollPane(liabTable), BorderLayout.CENTER);
        
        totalLiabLabel = new JLabel("", SwingConstants.RIGHT);
        totalLiabLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLiabLabel.setForeground(SECONDARY);
        totalLiabLabel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        totalLiabLabel.setBackground(new Color(248, 249, 250));
        totalLiabLabel.setOpaque(true);
        liabilitiesPanel.add(totalLiabLabel, BorderLayout.SOUTH);

        panel.add(assetsPanel);
        panel.add(liabilitiesPanel);

        panel.putClientProperty("updateLabels", (Runnable) () -> {
            totalAssetsLabel.setText("Total Assets: " + formatAccounting(calculateTotalAssets()));
            totalLiabLabel.setText("Total Liabilities & Equity: " + formatAccounting(calculateTotalLiabilitiesAndEquity()));
        });

        return panel;
    }

    // ===================== DOMAIN CLASSES =====================
    private static class Account {
        private String name;
        private String type;
        private double balance;

        public Account(String name, String type, double initialBalance) {
            this.name = name;
            this.type = type;
            this.balance = initialBalance;
        }
        public String getName() { return name; }
        public String getType() { return type; }
        public double getBalance() { return balance; }

        public void applyDebit(double amount) {
            if (type.equals("Asset") || type.equals("Expense")) {
                balance += amount;
            } else {
                balance -= amount;
            }
        }
        public void applyCredit(double amount) {
            if (type.equals("Liability") || type.equals("Owner's Equity") || type.equals("Revenue")) {
                balance += amount;
            } else {
                balance -= amount;
            }
        }
    }

    private static class Transaction {
        private Date date;
        private String description;
        private String debitAccount;
        private String creditAccount;
        private double amount;

        public Transaction(Date date, String description, String debitAccount, String creditAccount, double amount) {
            this.date = date;
            this.description = description;
            this.debitAccount = debitAccount;
            this.creditAccount = creditAccount;
            this.amount = amount;
        }

        public Date getDate() { return date; }
        public String getDescription() { return description; }
        public String getDebitAccount() { return debitAccount; }
        public String getCreditAccount() { return creditAccount; }
        public double getAmount() { return amount; }
    }

    // ===================== PREDEFINED ACCOUNTS =====================
    private void addPredefinedAccounts() {
        String[][] predefined = {
            {"Cash", "Asset", "0"},
            {"Accounts Receivable", "Asset", "0"},
            {"Inventory", "Asset", "0"},
            {"Supplies", "Asset", "0"},
            {"Prepaid Expenses", "Asset", "0"},
            {"Equipment", "Asset", "0"},
            {"Furniture and Fixtures", "Asset", "0"},
            {"Accounts Payable", "Liability", "0"},
            {"Notes Payable", "Liability", "0"},
            {"Salaries Payable", "Liability", "0"},
            {"Owner's Capital", "Owner's Equity", "0"},
            {"Owner's Drawing", "Owner's Equity", "0"},
            {"Service Revenue", "Revenue", "0"},
            {"Sales Revenue", "Revenue", "0"},
            {"Salaries Expense", "Expense", "0"},
            {"Rent Expense", "Expense", "0"},
            {"Utilities Expense", "Expense", "0"}
        };

        for (String[] acc : predefined) {
            accounts.add(new Account(acc[0], acc[1], Double.parseDouble(acc[2])));
        }
    }

    // ===================== HELPERS =====================
    private String formatNumber(double value) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(value);
    }

    private String formatAccounting(double value) {
        if (value < 0) {
            return "(" + formatNumber(Math.abs(value)) + ")";
        }
        return formatNumber(value);
    }

    private Account getAccountByName(String name) {
        for (Account a : accounts) {
            if (a.getName().equals(name)) return a;
        }
        return null;
    }

    private List<String> getDebitAccountNames() {
        List<String> out = new ArrayList<>();
        for (Account a : accounts) out.add(a.getName());
        return out;
    }

    private List<String> getCreditAccountNames() {
        List<String> out = new ArrayList<>();
        for (Account a : accounts) out.add(a.getName());
        return out;
    }

    private List<String> getAllAccountNames() {
        List<String> out = new ArrayList<>();
        for (Account a : accounts) out.add(a.getName());
        return out;
    }

    private List<String> getAllAccountNamesSorted() {
        List<String> list = getAllAccountNames();
        Collections.sort(list);
        return list;
    }

    private void refreshAccountCombos() {
        List<String> debits = getDebitAccountNames();
        List<String> credits = getCreditAccountNames();

        if (debitComboGlobal != null) {
            debitComboGlobal.removeAllItems();
            for (String s : debits) debitComboGlobal.addItem(s);
        }
        if (creditComboGlobal != null) {
            creditComboGlobal.removeAllItems();
            for (String s : credits) creditComboGlobal.addItem(s);
        }
    }

    private void refreshLedgerAccountCombo() {
        if (ledgerAccountCombo == null) return;
        ledgerAccountCombo.removeAllItems();
        for (String s : getAllAccountNamesSorted()) ledgerAccountCombo.addItem(s);
        if (ledgerAccountCombo.getItemCount() > 0) ledgerAccountCombo.setSelectedIndex(0);
    }

    private void updateGeneralLedgerTable(String accountName) {
        ledgerTableModel.setRowCount(0);
        Account acc = getAccountByName(accountName);
        if (acc == null) return;

        double running = 0.0;
        for (Transaction tx : transactions) {
            double amount = tx.getAmount();
            String dateStr = sdf.format(tx.getDate());
            boolean added = false;
            if (tx.getDebitAccount().equals(accountName)) {
                if (acc.getType().equals("Asset") || acc.getType().equals("Expense")) {
                    running += amount;
                } else {
                    running -= amount;
                }
                ledgerTableModel.addRow(new Object[]{dateStr, tx.getDescription(), tx.getDebitAccount(), tx.getCreditAccount(),
                        formatAccounting(amount),
                        formatAccounting(running)});
                added = true;
            }
            if (tx.getCreditAccount().equals(accountName)) {
                if (acc.getType().equals("Liability") || acc.getType().equals("Owner's Equity") || acc.getType().equals("Revenue")) {
                    running += amount;
                } else {
                    running -= amount;
                }
                ledgerTableModel.addRow(new Object[]{dateStr, tx.getDescription(), tx.getDebitAccount(), tx.getCreditAccount(),
                        formatAccounting(amount),
                        formatAccounting(running)});
                added = true;
            }
        }
    }

    private double calculateTotalAssets() {
        double sum = 0;
        for (Account a : accounts) if (a.getType().equals("Asset")) sum += a.getBalance();
        return sum;
    }

    private double calculateTotalLiabilitiesAndEquity() {
        double total = 0;
        double capital = 0;
        double drawing = 0;
        double revenue = 0;
        double expense = 0;

        for (Account a : accounts) {
            switch (a.getType()) {
                case "Liability":
                    total += a.getBalance();
                    break;
                case "Owner's Equity":
                    if (a.getName().equals("Owner's Capital"))
                        capital += a.getBalance();
                    else if (a.getName().equals("Owner's Drawing"))
                        drawing += a.getBalance();
                    break;
                case "Revenue":
                    revenue += a.getBalance();
                    break;
                case "Expense":
                    expense += a.getBalance();
                    break;
            }
        }

        double netIncome = revenue - expense;
        double equity = capital - drawing + netIncome;

        return total + equity;
    }

    private void refreshAllViews() {
        if (transactionsTableModel != null) {
            filterTransactions("");
        }

        if (accountsTableModel != null) {
            accountsTableModel.setRowCount(0);
            for (Account a : accounts) {
                accountsTableModel.addRow(new Object[]{a.getName(), a.getType(), formatAccounting(a.getBalance())});
            }
        }

        if (journalTableModel != null) {
            journalTableModel.setRowCount(0);
            for (Transaction tx : transactions) {
                String dateStr = sdf.format(tx.getDate());
                journalTableModel.addRow(new Object[]{
                        dateStr, tx.getDescription(), tx.getDebitAccount(),
                        formatAccounting(tx.getAmount()), ""
                });
                journalTableModel.addRow(new Object[]{
                        dateStr, tx.getDescription(), tx.getCreditAccount(),
                        "", formatAccounting(tx.getAmount())
                });
            }
        }

        refreshLedgerAccountCombo();
        if (ledgerAccountCombo != null && ledgerAccountCombo.getItemCount() > 0) {
            String sel = (String) ledgerAccountCombo.getSelectedItem();
            if (sel == null) sel = ledgerAccountCombo.getItemAt(0);
            ledgerAccountCombo.setSelectedItem(sel);
            updateGeneralLedgerTable(sel);
        }

        if (assetsTableModel != null && liabilitiesTableModel != null) {
            assetsTableModel.setRowCount(0);
            liabilitiesTableModel.setRowCount(0);
            for (Account a : accounts) {
                if (a.getType().equals("Asset")) {
                    assetsTableModel.addRow(new Object[]{a.getName(), formatAccounting(a.getBalance())});
                } else if (a.getType().equals("Liability") || a.getType().equals("Owner's Equity")) {
                    liabilitiesTableModel.addRow(new Object[]{a.getName(), formatAccounting(a.getBalance())});
                }
            }
        }

        refreshAccountCombos();

        Component center = getContentPane().getComponent(1);
        if (center instanceof JTabbedPane) {
            JTabbedPane tp = (JTabbedPane) center;
            for (int i = 0; i < tp.getTabCount(); i++) {
                Component c = tp.getComponentAt(i);
                if (c instanceof JPanel) {
                    Object prop = ((JPanel) c).getClientProperty("updateLabels");
                    if (prop instanceof Runnable) ((Runnable) prop).run();
                }
            }
        }
    }

    // ===================== MAIN =====================
    
    public static void main(String[] args) {
        try {
            // Set system look and feel for better styling
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            // Use default look and feel
        }
        
        SwingUtilities.invokeLater(() -> new AccountingApp());
    }
}
