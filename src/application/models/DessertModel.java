package application.models;

import java.sql.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class DessertModel {
	private Integer dessert_id;
	private String name;
	private Integer quantity;
	private SimpleObjectProperty<Date> expirationDate;
	
	
	public DessertModel() {
		// Empty constructor
	}
	
	public DessertModel(Integer id, String name, Integer quantity, Date expDate) {
		this.dessert_id = id;
		this.name = name;
		this.quantity = quantity;
		expirationDate = new SimpleObjectProperty<>(expDate);
	}
	
	public Integer getId() {
		return dessert_id;
	}
	
	public void setId(Integer id) {
		this.dessert_id = id;
	}
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public Integer getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Integer quantity ) {
		this.quantity = quantity;
	}
	
	public Date getExpDate() {
		return expirationDate.get();
	}
	
	public void setExpDate(Date expirationDate) {
		this.expirationDate = new SimpleObjectProperty<>(expirationDate);
	}
	
	public ObjectProperty<Date> expirationDateProperty() { return expirationDate; }
}

