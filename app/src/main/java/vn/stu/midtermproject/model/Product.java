package vn.stu.midtermproject.model;

import android.graphics.Bitmap;

public class Product {
    private int id;
    private ProductLine productLine;
    private int price;
    private String name;
    private Bitmap image;
    private String netWeight;

    public Product() {
    }

    public Product(int id, String name, ProductLine productLine, Bitmap image, int price, String netWeight) {
        this.id = id;
        this.productLine = productLine;
        this.price = price;
        this.name = name;
        this.image = image;
        this.netWeight = netWeight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductLine getProductLine() {
        return productLine;
    }

    public void setProductLine(ProductLine productLine) {
        this.productLine = productLine;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
    }

    @Override
    public String toString() {
        return "Mã: " + id + '\n' +
                "Tên sản phẩm: " + name + '\n' +
                "Dòng sản phẩm: " + productLine + '\n' +
                "Giá: " + price + '\n' +
                "Hình ảnh: " + image + '\n' +
                "Khối lượng tịnh: " + netWeight;
    }
}
