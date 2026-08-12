package model;

public class Resource {
    private int id;
    private String name;
    private String type;
    private double cost;
    private int quantity;
    private int value;

    public Resource(
            int id,
            String name,
            String type,
            double cost,
            int quantity,
            int value
    ) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.cost = cost;
        this.quantity = quantity;
        this.value = value;
    }

    public double totalCost() {
        return cost * quantity;
    }

    public int totalValue() {
        return value * quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
