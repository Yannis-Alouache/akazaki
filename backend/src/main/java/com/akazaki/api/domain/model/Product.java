package com.akazaki.api.domain.model;

import java.util.List;

public class Product {
    private Long id;

    private String name;

    private String description;

    private double price;

    private int stock;

    private String imageUrl;

    private List<Category> categories;

    // private List<Review> reviews;
    
    // private Promotion promotions;

    // Add constructors
    private Product(Long id, String name, String description, double price, int stock, String imageUrl, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.categories = categories;
        // this.reviews = new ArrayList<>();
    }

    private Product(String name, String description, double price, int stock, String imageUrl, List<Category> categories) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.categories = categories;
        // this.reviews = new ArrayList<>();
    }

    private Product(String name, String description, double price, int stock, List<Category> categories) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.categories = categories;
        // this.reviews = new ArrayList<>();
    }

    public static Product create(Long id, String name, String description, double price, int stock, String imageUrl, List<Category> categories) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        
        return new Product(id, name, description, price, stock, imageUrl, categories);
    }

    public static Product create(String name, String description, double price, int stock, String imageUrl, List<Category> categories) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        return new Product(name, description, price, stock, imageUrl, categories);
    }

    public static Product create(String name, String description, double price, int stock, List<Category> categories) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        return new Product(name, description, price, stock, categories);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    // public List<Review> getReviews() {
    //     return reviews;
    // }

    // public void setReviews(List<Review> reviews) {
    //     this.reviews = reviews;
    // }

    // public Promotion getPromotions() {
    //     return promotions;
    // }

    // public void setPromotions(Promotion promotions) {
    //     this.promotions = promotions;
    // }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (Double.compare(product.price, price) != 0) return false;
        if (stock != product.stock) return false;
        if (!id.equals(product.id)) return false;
        if (!name.equals(product.name)) return false;
        if (!description.equals(product.description)) return false;
        if (!imageUrl.equals(product.imageUrl)) return false;
        return categories.equals(product.categories);
    }
}
