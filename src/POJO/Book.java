package POJO;

import javax.swing.*;

public class Book {

    private String name;
    private String authorName;
    private int price;
    private ImageIcon cover;
    private String pathToCover;

    public Book(String name, String authorName, int price, ImageIcon cover) {
        this.name = name;
        this.authorName = authorName;
        this.price = price;
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public int getPrice() {
        return price;
    }

    public ImageIcon getCover() {
        return cover;
    }

    public void setPathToCover(String pathToCover) {
        this.pathToCover = pathToCover;
    }

    public String getPathToCover() {
        return pathToCover;
    }
}