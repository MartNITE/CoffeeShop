package application.enums;

public enum DbTableNameEnum {
	SANDWICHES("sandwiches"),
	DESSERTS("desserts"),
	COFFEE_CUPS("coffee_cups");
	
	private final String tableName;
	
	private DbTableNameEnum(String tableName) {
		this.tableName = tableName;
	}
	
	public String getTableName() {
		return tableName;
	}
}
