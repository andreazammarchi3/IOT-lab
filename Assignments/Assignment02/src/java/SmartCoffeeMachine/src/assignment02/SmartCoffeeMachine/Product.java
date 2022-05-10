package assignment02.SmartCoffeeMachine;

public class Product {
    private String label;
    private int availability;

    private int maxAvailability;

    public Product(String label, int availability, int maxAvailability) {
        this.label = label;
        this.availability = availability;
        this.maxAvailability = maxAvailability;
    }

    public Product(String label) {
        this.label = label;
        this.availability = 0;
        this.maxAvailability = 0;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public void setMaxAvailability(int maxAvailability) {
        this.maxAvailability = maxAvailability;
    }

    public String getLabel() {
        return label;
    }

    public int getAvailability() {
        return availability;
    }

    public int getMaxAvailability() {
        return maxAvailability;
    }
}
