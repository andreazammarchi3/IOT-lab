package assignment02.SmartCoffeeMachine;

/**
 * Product class, implemented Coffe, Tea and Chocolate
 */
public class Product {
    /**
     * Name of the product
     */
    private String label;

    /**
     * Quantity available for this product
     */
    private int availability;

    /**
     * Max availability for this product
     */
    private int maxAvailability;

    /**
     * Constructor
     * @param label Name of the product
     * @param availability Quantity available
     * @param maxAvailability Max availability
     */
    public Product(String label, int availability, int maxAvailability) {
        this.label = label;
        this.availability = availability;
        this.maxAvailability = maxAvailability;
    }

    /**
     * Constructor
     * @param label Name of the product
     */
    public Product(String label) {
        this.label = label;
        this.availability = 0;
        this.maxAvailability = 0;
    }

    /**
     * Set product name
     * @param label Name
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Set quantity left
     * @param availability Quantity left
     */
    public void setAvailability(int availability) {
        this.availability = availability;
    }

    /**
     * Set max availability
     * @param maxAvailability Max availability
     */
    public void setMaxAvailability(int maxAvailability) {
        this.maxAvailability = maxAvailability;
    }

    /**
     * Get product name
     * @return Product name
     */
    public String getLabel() {
        return label;
    }

    /**
     * Get product availability
     * @return Product availability
     */
    public int getAvailability() {
        return availability;
    }

    /**
     * Get product max availability
     * @return Product max availability
     */
    public int getMaxAvailability() {
        return maxAvailability;
    }
}
