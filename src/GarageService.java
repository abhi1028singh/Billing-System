import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
public class GarageService {
    private Map<String, Customer> customersMap;
    private List<Service> availableServices;

    public GarageService() {
        this.customersMap = new HashMap<>();
        this.availableServices = new ArrayList<>();
        loadServices();
    }
    //DAta base
    // Inside GarageService.java




        // 👇 NEW METHOD to save customer in DB
        //public void addCustomerToDB(Customer customer) {
          //  try {
             //   Connection conn = DBConnect.getConnection();
               // String sql = "INSERT INTO customers(name, phone, car_model, car_number) VALUES (?, ?, ?, ?)";
             //   PreparedStatement ps = conn.prepareStatement(sql);
               // ps.setString(1, customer.getName());
                //ps.setString(2, customer.getPhone());
                //ps.setString(3, customer.getCar().getModel());
                //ps.setString(4, customer.getCar().getCarNumber());
                //ps.executeUpdate();
              //  System.out.println("✅ Customer saved to database.");
            //} catch (Exception e) {
              //  e.printStackTrace();
          //  }
        //}

        // existing methods like getServiceNames()...



    // Load predefined services
    public void loadServices() {
        availableServices.add(new Service("Car Washing ",  500));
        availableServices.add(new Service("Oil Change ", 300));
        availableServices.add(new Service("Wheel Alignment ", 400));
        availableServices.add(new Service("Tyre Replacement ", 3000));
        availableServices.add(new Service("Puncture " , 100));
        availableServices.add(new Service("Brake Repair ", 800));
        availableServices.add(new Service("Engine Repair ", 1500));
        availableServices.add(new Service("General Service ", 300));
    }

    // For JComboBox in UI
    public String[] getServiceNames() {
        return availableServices.stream()
                .map(Service::getName)
                .toArray(String[]::new);
    }

    // Get Service object by name
    public Service getServiceByName(String name) {
        for (Service s : availableServices) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    // Not needed anymore but kept for backward compatibility
    // You can remove this method if not used in UI
    public Invoice generateInvoice(Customer customer, Car car, String serviceType) {
        Invoice invoice = new Invoice(customer);
        for (Service s : availableServices) {
            if (s.getName().equalsIgnoreCase(serviceType)) {
                invoice.addService(s);
                break;
            }
        }
        return invoice;
    }

    // Add customer to internal storage
    public void addCustomer(String name, String phone, String carNumber, String model) {
        Car car = new Car(carNumber, model);
        Customer customer = new Customer(name, phone, car);
        customersMap.put(carNumber, customer);
        System.out.println("✅ Customer added successfully.");
    }

    // Console-based invoice generation (optional)
    public void createInvoice(String carNumber) {
        if (!customersMap.containsKey(carNumber)) {
            System.out.println("❌ No customer found with car number: " + carNumber);
            return;
        }

        Scanner sc = new Scanner(System.in);
        Customer customer = customersMap.get(carNumber);
        Invoice invoice = new Invoice(customer);

        boolean adding = true;

        while (adding) {
            System.out.println("\n📋 Available Services:");
            for (int i = 0; i < availableServices.size(); i++) {
                System.out.println((i + 1) + ". " + availableServices.get(i).getName()
                        + " - ₹" + availableServices.get(i).getPrice());
            }

            System.out.print("Enter service number to add: ");
            int choice = sc.nextInt();

            if (choice > 0 && choice <= availableServices.size()) {
                Service selectedService = availableServices.get(choice - 1);
                invoice.addService(selectedService);
                System.out.println("✅ " + selectedService.getName() + " added successfully.");

                System.out.print("Do you want to add more services? (y/n): ");
                sc.nextLine(); // clear newline
                String more = sc.nextLine().trim().toLowerCase();
                if (!more.equals("y")) {
                    adding = false;
                }
            } else {
                System.out.println("❌ Invalid choice.");
            }
        }

        System.out.println();
        invoice.printInvoice();
    }

}
