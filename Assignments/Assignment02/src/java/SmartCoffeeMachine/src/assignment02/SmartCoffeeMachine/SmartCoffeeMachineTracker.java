package assignment02.SmartCoffeeMachine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SmartCoffeeMachineTracker {
    private final SerialCommChannel channel;
    private String modality;
    private List<Product> products;
    private int selfTests;

    public SmartCoffeeMachineTracker() throws Exception {
        channel = new SerialCommChannel("/dev/cu.usbmodem14101", 9600);
        System.out.println("Waiting Arduino for rebooting...");
        Thread.sleep(4000);
        initialize();
        System.out.println("Ready.");
    }

    public void initialize() throws Exception {
        modality = getSerialData("modality");

        products = new ArrayList<>();
        initializeProduct(new Product("Coffee"));
        initializeProduct(new Product("Tea"));
        initializeProduct(new Product("Chocolate"));

        selfTests = Integer.parseInt(getSerialData("selfTests"));
    }

    public void initializeProduct(Product product) throws Exception {
        int maxAvailability = Integer.parseInt(getSerialData(product.getLabel()));
        product.setMaxAvailability(maxAvailability);
        product.setAvailability(maxAvailability);
        products.add(product);
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setSelfTests(int selfTests) {
        this.selfTests = selfTests;
    }

    public String getModality() {
        return modality;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Product getProduct(String label) {
        for (Product product:
             products) {
            if (Objects.equals(product.getLabel(), label)) {
                return product;
            }
        }
        return null;
    }

    public int getSelfTests() {
        return selfTests;
    }

    public String getSerialData(String inputMsg) throws Exception {
        channel.sendMsg(inputMsg);
        return channel.receiveMsg();
    }

    public void update() throws Exception {
        modality = getSerialData("modality");
        for (Product product:
             products) {
            int availability = Integer.parseInt(getSerialData(product.getLabel()));
            product.setAvailability(availability);
        }
    }
}