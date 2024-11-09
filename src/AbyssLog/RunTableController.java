package AbyssLog;

import Models.AbyssRun;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class RunTableController implements Initializable {

    @FXML
    private TableView<AbyssRun> runTable;
    @FXML
    private TableColumn dateColumn;
    @FXML
    private TableColumn runTimeColumn;
    @FXML
    private TableColumn lootValueColumn;
    @FXML
    private TableColumn tierColumn;
    @FXML
    private TableColumn typeColumn;
    @FXML
    private TableColumn weatherColumn;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        runTimeColumn.setCellValueFactory(new PropertyValueFactory<>("elapsedTime"));
        lootValueColumn.setCellValueFactory(new PropertyValueFactory<>("lootValue"));
        lootValueColumn.setCellFactory(a -> new TableCell<String, Long>() {
            @Override
            protected void updateItem(Long price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(price / 1000000) + "M");
                }
            }
        });
        tierColumn.setCellValueFactory(new PropertyValueFactory<>("tier"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        weatherColumn.setCellValueFactory(new PropertyValueFactory<>("weather"));
    }

    public TableView<AbyssRun> getRunTable() {
        return runTable;
    }
}
