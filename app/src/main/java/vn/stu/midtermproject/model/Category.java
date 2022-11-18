package vn.stu.midtermproject.model;

public class Category {

    private int id;
    private String name;
    private String origin; //Xuất sứ

    public Category() {
    }

    public Category(int id, String name, String origin) {
        this.id = id;
        this.name = name;
        this.origin = origin;
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

    @Override
    public String toString() {
        return name;
    }
}
