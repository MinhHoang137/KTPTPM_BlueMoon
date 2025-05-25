package entity.vehicle;

import java.util.Date;

public class Vehicle {
    private int id;
    private int ownerPersonId;
    private String brand;
    private String model;
    private String licensePlate;
    private String color;
    private String imagePath;
    private Date createdAt;
    private Date updatedAt;

    public Vehicle() {}

    public Vehicle(int id, int ownerPersonId, String brand, String model, String licensePlate, String color, String imagePath, Date createdAt, Date updatedAt) {
        this.id = id;
        this.ownerPersonId = ownerPersonId;
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
        this.color = color;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOwnerPersonId() { return ownerPersonId; }
    public void setOwnerPersonId(int ownerPersonId) { this.ownerPersonId = ownerPersonId; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}