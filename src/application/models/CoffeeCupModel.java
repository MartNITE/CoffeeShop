package application.models;

import application.enums.CoffeeCupSizeEnum;

public class CoffeeCupModel {
	private Integer cup_id;

	private CoffeeCupSizeEnum size;

	private Integer quantity;
	
	
	public CoffeeCupModel() {
		// Empty constructor
	}
	
	public CoffeeCupModel(Integer id, CoffeeCupSizeEnum size, Integer quantity) {
		this.cup_id = id;
		this.size = size;
		this.quantity = quantity;
	}
	
	public Integer getId() {
		return cup_id;
	}
	
	public void setId(Integer id) {
		cup_id = id;
	}
	
	
	public CoffeeCupSizeEnum getSize() {
		return size;
	}
	
	public void setSize(CoffeeCupSizeEnum size) {
		this.size = size;
	}
	
	public String getName() {
		return size.label + ": " + size.description;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Integer quantity ) {
		this.quantity = quantity;
	}
}
