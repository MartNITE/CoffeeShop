package application.models;

import java.sql.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SandwichModel {
	private Integer sandwich_id;
	private String name;
	private Integer quantity;
	private SimpleObjectProperty<Date> expirationDate;
	
	public SandwichModel() {
		// Empty constructor
	}
	
	public SandwichModel(Integer id, String name, Integer quantity, Date expDate) {
		this.sandwich_id = id;
		this.name = name;
		this.quantity = quantity;
		expirationDate = new SimpleObjectProperty<>(expDate);
	}
	
	public Integer getId() {
		return sandwich_id;
	}
	
	public void setId(Integer id) {
		sandwich_id = id;
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
	
	public void setQuantity(Integer quantity) {
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

