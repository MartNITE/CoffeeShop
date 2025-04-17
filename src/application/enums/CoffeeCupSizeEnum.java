package application.enums;

public enum CoffeeCupSizeEnum {
	SMALL("Small", "small", "Еспресо"),
    MEDIUM("Medium", "medium", "Капучино"),
    LARGE("Large", "large", "Лате");

    public final String label;
    public final String size;
    public final String description;

    private CoffeeCupSizeEnum(String label, String size, String description) {
        this.label = label;
        this.size = size;
        this.description = description;
    }
    
    public String getSize() {
        return size;
    }
}
