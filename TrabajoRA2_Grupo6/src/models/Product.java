package models;

import services.ProductServices;

public class Product {
	private int id,stock,id_provider;
	private String name,description,category,image;
	private float price;
	private int available;
	
	public Product() {
		super();
	}

	public Product(String name, String description, float price, String category, String image, int id_provider) {
		super();
		this.id = ProductServices.getNextId();
		this.name = name;
		this.description = description;
		this.price = price;
		this.category = category;
		this.image = image;
		this.stock = 0;
		this.id_provider = id_provider;
		this.available = 1;
	}
	
	public Product(int id, String name, String description, float price, String category, String image, int stock,
			int id_provider, int available) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.category = category;
		this.image = image;
		this.stock = stock;
		this.id_provider = id_provider;
		this.available = available;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getId_provider() {
		return id_provider;
	}

	public void setId_provider(int id_provider) {
		this.id_provider = id_provider;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", stock=" + stock + ", id_provider=" + id_provider + ", name=" + name
				+ ", description=" + description + ", category=" + category + ", image=" + image + ", price=" + price
				+ ", available=" + available + "]";
	}

}
