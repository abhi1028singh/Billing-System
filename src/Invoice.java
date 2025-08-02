import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private Customer customer;
    private List<Service> serviceList;
    private double totalamount;

    // 🆕 Added Fields
    private String invoiceId;
    private String dateTime;
    private static int counter = 1;

    // ✅ Constructor for multi-service invoice
    public Invoice(Customer customer) {
        this.customer = customer;
        this.serviceList = new ArrayList<>();
        this.totalamount = 0;

        // Generate invoice ID and timestamp
        this.invoiceId = "INV" + String.format("%03d", counter++);
        this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a"));
    }

    // ✅ Add selected service to invoice
    public void addService(Service service) {
        serviceList.add(service);
        totalamount += service.getPrice();
    }

    // ✅ Print nicely formatted invoice in console
    public void printInvoice() {
        System.out.println("==========================================");
        System.out.println("               GARAGE INVOICE             ");
        System.out.println("==========================================");
        System.out.println("Invoice No: " + invoiceId);
        System.out.println("Date/Time : " + dateTime);
        System.out.println("------------------------------------------");
        System.out.println("Customer  : " + customer.getName());
        System.out.println("Phone     : " + customer.getPhone());
        System.out.println("Car       : " + customer.getCar().getModel() +
                " (" + customer.getCar().getCarNumber() + ")");
        System.out.println("------------------------------------------");
        System.out.println("Services:");
        for (int i = 0; i < serviceList.size(); i++) {
            Service s = serviceList.get(i);
            System.out.printf("%2d. %-20s ₹%.2f\n", (i + 1), s.getName(), s.getPrice());
        }
        System.out.println("------------------------------------------");
        System.out.printf("Total Amount:           ₹%.2f\n", totalamount);
        System.out.println("==========================================");
        System.out.println("🙏 Thank you for visiting Abhishek Garage!");
        System.out.println("==========================================");
    }

    // ✅ Return invoice as a string for UI popup
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== GARAGE INVOICE ==========\n");
        sb.append("Invoice No: ").append(invoiceId).append("\n");
        sb.append("Date/Time : ").append(dateTime).append("\n");
        sb.append("-----------------------------------\n");
        sb.append("Customer  : ").append(customer.getName()).append("\n");
        sb.append("Phone     : ").append(customer.getPhone()).append("\n");
        sb.append("Car       : ").append(customer.getCar().getModel())
                .append(" (").append(customer.getCar().getCarNumber()).append(")\n");
        sb.append("-----------------------------------\n");
        sb.append("Services:\n");
        for (int i = 0; i < serviceList.size(); i++) {
            Service s = serviceList.get(i);
            sb.append(String.format("%2d. %-20s ₹%.2f\n", (i + 1), s.getName(), s.getPrice()));
        }
        sb.append("-----------------------------------\n");
        sb.append(String.format("Total Amount:        ₹%.2f\n", totalamount));
        sb.append("===================================\n");
        sb.append("🙏 Thank you for visiting!\n");

        return sb.toString();
    }
}
