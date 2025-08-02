import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GarageUI {
    private static List<Service> selectedServices = new ArrayList<>();
    private static GarageService garageService = new GarageService();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "🔐 Admin Login Required");
            showAdminLogin();
        });
    }

    private static void showCustomerInputForm() {
        JFrame frame = new JFrame("Garage Billing System");
        frame.getContentPane().setBackground(new Color(232, 245, 233));

        JLabel welcomeLabel = new JLabel("🚗 Welcome to Abhishek Car Garage", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        welcomeLabel.setBounds(20, 10, 350, 30);

        JLabel nameLabel = new JLabel("Customer Name:");
        JLabel phoneLabel = new JLabel("Phone Number:");
        JLabel carModelLabel = new JLabel("Car Model:");
        JLabel carNumberLabel = new JLabel("Car Number:");

        JTextField nameField = new JTextField("Enter your name");
        nameField.setForeground(Color.GRAY);

        JTextField phoneField = new JTextField("Enter phone number");
        phoneField.setForeground(Color.GRAY);

        JTextField carModelField = new JTextField("Enter car model");
        carModelField.setForeground(Color.GRAY);

        JTextField carNumberField = new JTextField("XX00 XX000");
        carNumberField.setForeground(Color.GRAY);

        JButton nextButton = new JButton("Next");

        nameLabel.setBounds(30, 60, 120, 25);
        nameField.setBounds(160, 60, 150, 25);
        phoneLabel.setBounds(30, 100, 120, 25);
        phoneField.setBounds(160, 100, 150, 25);
        carModelLabel.setBounds(30, 140, 120, 25);
        carModelField.setBounds(160, 140, 150, 25);
        carNumberLabel.setBounds(30, 180, 120, 25);
        carNumberField.setBounds(160, 180, 150, 25);
        nextButton.setBounds(100, 230, 150, 30);

        // ----------- Placeholders and Field Behaviors -------------
        addPlaceholderBehavior(nameField, "Enter your name", true);
        addPlaceholderBehavior(phoneField, "Enter phone number", false);
        addPlaceholderBehavior(carModelField, "Enter car model", true);
        addPlaceholderBehavior(carNumberField, "XX00 XX000", true);

        // Input validations
        nameField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isAlphabetic(e.getKeyChar()) && e.getKeyChar() != ' ') {
                    e.consume();
                }
            }
        });

        phoneField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()) || phoneField.getText().length() >= 10) {
                    e.consume();
                }
            }
        });

        carNumberField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (carNumberField.getText().length() >= 10) {
                    e.consume();
                }
            }
        });

        // Move focus on Enter
        nameField.addActionListener(e -> phoneField.requestFocus());
        phoneField.addActionListener(e -> carModelField.requestFocus());
        carModelField.addActionListener(e -> carNumberField.requestFocus());
        carNumberField.addActionListener(e -> nextButton.doClick());

        nextButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String model = carModelField.getText().trim();
            String number = carNumberField.getText().trim();

            if (name.isEmpty() || name.equals("Enter your name") ||
                    phone.isEmpty() || phone.length() != 10 || phone.equals("Enter phone number") ||
                    model.isEmpty() || model.equals("Enter car model") ||
                    number.isEmpty() || number.equals("XX00 XX000")) {
                JOptionPane.showMessageDialog(frame, "❌ Please enter valid details in all fields.");
                return;
            }

            Car car = new Car(number, model);
            Customer customer = new Customer(name, phone, car);

            frame.dispose();
            showServiceSelectionUI(customer, car);
        });

        frame.add(welcomeLabel);
        frame.add(nameLabel); frame.add(nameField);
        frame.add(phoneLabel); frame.add(phoneField);
        frame.add(carModelLabel); frame.add(carModelField);
        frame.add(carNumberLabel); frame.add(carNumberField);
        frame.add(nextButton);

        frame.setSize(400, 320);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static void addPlaceholderBehavior(JTextField field, String placeholder, boolean clearOnlyOnFocus) {
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }

    private static void showServiceSelectionUI(Customer customer, Car car) {
        JFrame serviceFrame = new JFrame("Select Service");
        serviceFrame.getContentPane().setBackground(new Color(227, 242, 253));

        JLabel serviceLabel = new JLabel("Choose a Service:");
        JComboBox<String> serviceBox = new JComboBox<>(garageService.getServiceNames());
        JButton addButton = new JButton("Add Service");

        serviceLabel.setBounds(30, 30, 120, 25);
        serviceBox.setBounds(160, 30, 150, 25);
        addButton.setBounds(100, 80, 150, 30);

        addButton.addActionListener(e -> {
            String serviceName = (String) serviceBox.getSelectedItem();
            Service selected = garageService.getServiceByName(serviceName);
            if (selected != null) {
                selectedServices.add(selected);
                serviceFrame.dispose();
                showNextOptionsUI(customer, car);
            }
        });

        serviceFrame.add(serviceLabel);
        serviceFrame.add(serviceBox);
        serviceFrame.add(addButton);

        serviceFrame.setSize(400, 180);
        serviceFrame.setLayout(null);
        serviceFrame.setVisible(true);
        serviceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static void showNextOptionsUI(Customer customer, Car car) {
        JFrame optionFrame = new JFrame("Next Step");
        optionFrame.getContentPane().setBackground(new Color(255, 253, 231));

        JLabel doneLabel = new JLabel("✅ Service Added Successfully!");
        JButton moreButton = new JButton("Add More Services");
        JButton invoiceButton = new JButton("Generate Invoice");

        doneLabel.setBounds(80, 30, 250, 25);
        moreButton.setBounds(40, 70, 150, 30);
        invoiceButton.setBounds(200, 70, 150, 30);

        moreButton.addActionListener(e -> {
            optionFrame.dispose();
            showServiceSelectionUI(customer, car);
        });

        invoiceButton.addActionListener(e -> {
            optionFrame.dispose();
            Invoice invoice = new Invoice(customer);
            for (Service s : selectedServices) {
                invoice.addService(s);
            }
            selectedServices.clear();

            JOptionPane.showMessageDialog(null, invoice.toString(), "🧾 Invoice", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "🙏 Thank you for visiting Abhishek Car Garage!", "Goodbye", JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
        });

        optionFrame.add(doneLabel);
        optionFrame.add(moreButton);
        optionFrame.add(invoiceButton);

        optionFrame.setSize(420, 160);
        optionFrame.setLayout(null);
        optionFrame.setVisible(true);
        optionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static void showAdminLogin() {
        JFrame loginFrame = new JFrame("Admin Login");
        loginFrame.getContentPane().setBackground(new Color(255, 241, 224));

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        userLabel.setBounds(30, 30, 100, 25);
        userField.setBounds(140, 30, 150, 25);
        passLabel.setBounds(30, 70, 100, 25);
        passField.setBounds(140, 70, 150, 25);
        loginButton.setBounds(110, 120, 100, 30);

        loginFrame.add(userLabel); loginFrame.add(userField);
        loginFrame.add(passLabel); loginFrame.add(passField);
        loginFrame.add(loginButton);

        loginFrame.setLayout(null);
        loginFrame.setSize(350, 220);
        loginFrame.setVisible(true);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginButton.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword());

            if (user.equals("rudra") && pass.equals("rudra000")) {
                loginFrame.dispose();
                showCustomerInputForm();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "❌ Invalid Credentials!");
            }
        });
    }
}