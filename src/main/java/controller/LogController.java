package controller;

import database.LogDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Log;
import model.LogginUser;
import model.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class LogController implements Initializable {

    @FXML
    private AnchorPane anchorPaneLog;

    @FXML
    private Button btnBack;

    @FXML
    private TableColumn<Log, String> columnCreated;

    @FXML
    private TableColumn<Log, String> columnId;

    @FXML
    private TableColumn<Log, String> columnLevel;

    @FXML
    private TableColumn<Log, String> columnMessage;

    @FXML
    private ComboBox<String> comboFilter;

    @FXML
    private TableView<Log> tabelViewLog;

    @FXML
    private Label labelInfo;

    @FXML
    private TextArea textAreaLog;

    LogDAOImpl db = null;

    @FXML
    void onBackButtonClicked(ActionEvent event) throws IOException {
        String dest = "/view/main-view.fxml";
        MainApplication.navigateTo(anchorPaneLog,dest);
    }

    @FXML
    void onReadLogClicked(ActionEvent event) throws IOException, ClassNotFoundException {

        FileInputStream fileIn = new FileInputStream("monitor.txt");
        ObjectInputStream objectIn = new ObjectInputStream(fileIn);
        Object obj = objectIn.readObject();
        textAreaLog.appendText(obj.toString());
        System.out.println("The Object has been read from the file");
        objectIn.close();
    }

    private ObservableList<Log> populateLogs() throws SQLException {
        List list = db.readAll();
        ObservableList<Log> l = FXCollections.observableArrayList(list);
        return l;
    }
    private ObservableList<Log> populateLogLevel(String condition) throws SQLException {
        List list = db.readTableColumnQuery("log","level", condition);
        ObservableList<Log> l = FXCollections.observableArrayList(list);
        return l;
    }
    void setUpTavelViewLog(){
        db = new LogDAOImpl();
        ObservableList<String> filter_list = FXCollections.observableArrayList();
        filter_list.add(LogDAOImpl.warn_level.INFO.toString());
        filter_list.add(LogDAOImpl.warn_level.WARN.toString());
        filter_list.add(LogDAOImpl.warn_level.DEBUG.toString());

        comboFilter.setItems(filter_list);
        Label selected = new Label("default item selected");

        EventHandler<ActionEvent> event =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        try {
                            tabelViewLog.setItems(populateLogLevel(comboFilter.getValue().toString()));
                        } catch (SQLException ex) {
                            MainApplication.logData(new Log(0,"WARN",ex.getMessage(),new Date().toString()));
                            throw new RuntimeException(ex);
                        }
                    }
                };
        comboFilter.setOnAction(event);
        tabelViewLog.setEditable(true);
        columnId.setCellValueFactory(new PropertyValueFactory<Log, String>("logId"));
        columnCreated.setCellValueFactory(new PropertyValueFactory<Log, String>("created"));
        columnLevel.setCellValueFactory(new PropertyValueFactory<Log, String>("level"));
        columnMessage.setCellValueFactory(new PropertyValueFactory<Log, String>("message"));
        try {
            tabelViewLog.setItems(populateLogs());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tabelViewLog.setEditable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpTavelViewLog();
        MainApplication.logData(new Log(0,"INFO","Logs accessed ",new Date().toString()));
        User currentUser = LogginUser.getUser();
        if(currentUser !=null){
            labelInfo.setText(currentUser.getName()+" is logged In");
        }

    }
}
