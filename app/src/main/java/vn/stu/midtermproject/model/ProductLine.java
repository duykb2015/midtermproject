package vn.stu.midtermproject.model;

public class ProductLine {

    private int id;
    private String name;
    private String origin;
    private String manufacturer;

    public ProductLine() {
    }

    public ProductLine(int id, String name, String origin, String manufacturer) {
        this.id = id;
        this.name = name;
        this.origin = origin;
        this.manufacturer = manufacturer;
    }

    public int getId() {
        return this.id;
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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
