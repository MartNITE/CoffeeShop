package application;

import java.sql.Date;
import java.sql.SQLException;

import application.models.CoffeeCupModel;
import application.models.DessertModel;
import application.models.SandwichModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class tableController {
	
	@FXML
	private TableView<SandwichModel> Stable;
	@FXML
	private TableView<DessertModel> Dtable;
	@FXML
	private TableView<CoffeeCupModel> Ctable;
	
	@FXML
	private TableColumn<SandwichModel, Integer> Sid;
	@FXML
	private TableColumn<SandwichModel, String> Sname;
	@FXML
	private TableColumn<SandwichModel, Integer> Sqt;
	@FXML
	private TableColumn<SandwichModel, Date> Sdate;
	@FXML
	private TableColumn<SandwichModel, Integer> Did;
	@FXML
	private TableColumn<SandwichModel, String> Dname;
	@FXML
	private TableColumn<SandwichModel, Integer>Dqt;
	@FXML
	private TableColumn<SandwichModel, Date> Ddate;
	@FXML
	private TableColumn<SandwichModel, Integer>Cid;
	@FXML
	private TableColumn<SandwichModel, String>Cname;
	@FXML
	private TableColumn<SandwichModel, Integer>Cqt;
	
	private JdbcDao jdbcDao;
	
	private ObservableList<SandwichModel> sandwichesList = null;
	private ObservableList<DessertModel> dessertsList = null;
	private ObservableList<CoffeeCupModel> coffeeCupsList = null;
	
	@FXML
    public void initialize() {
    	jdbcDao = new JdbcDao();
    	
    	try {
			sandwichesList = jdbcDao.getSandwiches();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    	
    	if (sandwichesList != null) {
    		if (LOG) sandwichesList.forEach((item) -> { System.out.println(item.toString()); });
    		
    		Stable.setItems(sandwichesList);
    		
    		Sid.setCellValueFactory(new PropertyValueFactory<>("id"));
    		Sname.setCellValueFactory(new PropertyValueFactory<>("name"));
    		Sqt.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    		Sdate.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
    	}
    	
    	try {
			dessertsList = jdbcDao.getDesserts();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    	
    	if (dessertsList != null) {
    		if (LOG) dessertsList.forEach((item) -> { System.out.println(item.toString()); });
    		
    		Dtable.setItems(dessertsList);
    		
    		Did.setCellValueFactory(new PropertyValueFactory<>("id"));
    		Dname.setCellValueFactory(new PropertyValueFactory<>("name"));
    		Dqt.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    		Ddate.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
    	}
    	
    	try {
			coffeeCupsList = jdbcDao.getCoffeeCups();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    	
    	if (coffeeCupsList != null) {    		
    		Ctable.setItems(coffeeCupsList);
    		
    		Cid.setCellValueFactory(new PropertyValueFactory<>("id"));
    		Cname.setCellValueFactory(new PropertyValueFactory<>("name"));
    		Cqt.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    	}
    }
	
	private static final boolean LOG = false;
}
