package AbyssLog;

import Models.AbyssRun;
import Models.Tier;
import Models.Type;
import Models.Weather;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.*;

public class MainWindowController implements Initializable {
    @FXML
    private Label runTimerLabel;
    @FXML
    private Button startRunButton;
    @FXML
    private TextArea startCargoTextArea;
    @FXML
    private TextArea endCargoTextArea;
    @FXML
    private Label statusLabel;
    @FXML
    private RunTableController runTableController;
    @FXML
    private ChoiceBox<Tier> tierChoiceBox;
    @FXML
    private ChoiceBox<Type> typeChoiceBox;
    @FXML
    private ChoiceBox<Weather> weatherChoiceBox;

    private AbyssRun currentRun;
    private Timer runTimer;
    private long startTime;
    private boolean running = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tierChoiceBox.getItems().setAll(Tier.values());
        typeChoiceBox.getItems().setAll(Type.values());
        weatherChoiceBox.getItems().setAll(Weather.values());
        endCargoTextArea.setDisable(true);
    }

    public void onStartRunButton(){
        if (!running) {
            startRun();
        } else {
            stopRun();
        }

    }

    private long getSec(long nanoSeconds) {
        return nanoSeconds / (1000*1000*1000);
    }

    // TODO might want to make this a static util method
    private String formatTime(long nanoTime) {
        long tempSec = nanoTime / (1000*1000*1000);
        long sec = tempSec % 60;
        long min = (tempSec / 60) % 60;

        String secLabel = String.valueOf(sec);
        String minLabel = String.valueOf(min);

        if (sec < 10) secLabel = "0" + sec;
        if (min < 10) minLabel = "0" + min;
        StringBuilder result = new StringBuilder();

        return result.append(minLabel).append(":").append(secLabel).toString();
    }

    private ArrayList<Map.Entry<String, Integer>> parseCargo(TextArea input){
        ArrayList<Map.Entry<String, Integer>> result = new ArrayList<>();

        for (String s : input.getText().split("\n")){
            String[] split = s.split("\\t");

            // Remove no-break spaces from qty
            if (split.length > 1){
                split[1] = split[1].replaceAll("\\h", "");
            }
            if (split.length > 1 && !split[1].isEmpty() && split[1].matches("^\\d*$")){
                // Cargo data contains quantity
                int qty = Integer.parseInt(split[1]);
                result.add(new AbstractMap.SimpleEntry<>(split[0], qty));
            } else {
                // Cargo data missing quantity data (single item)
                result.add(new AbstractMap.SimpleEntry<>(split[0], 1));
            }
        }
        return result;
    }

    private void startRun() {
        running = true;
        currentRun = new AbyssRun(tierChoiceBox.getValue(), typeChoiceBox.getValue(), weatherChoiceBox.getValue());
        if (!startCargoTextArea.getText().isEmpty()) {
            currentRun.updateCargo(parseCargo(startCargoTextArea), true);
        }

        startTime = System.nanoTime();
        currentRun.startRun(startTime);
        startRunButton.setText("End run");
        startCargoTextArea.setDisable(true);
        endCargoTextArea.setDisable(false);

        startTimer();
        updateStatusLabel("Run started...");
    }

    private void stopRun() {
        running = false;
        if (!endCargoTextArea.getText().isEmpty()) {
            currentRun.updateCargo(parseCargo(endCargoTextArea), false);
            updateStatusLabel("Run with loot recorded");
        } else {
            updateStatusLabel("Run recorded. No loot registered.");
        }
        currentRun.stopRun(System.nanoTime());
        runTableController.getRunTable().getItems().add(currentRun);
        startRunButton.setText("Start run");
        startCargoTextArea.setDisable(false);
        endCargoTextArea.setDisable(true);
        resetTimer();
    }

    private void resetTimer() {
        runTimer.cancel();
        runTimerLabel.setText("00:00");
        runTimerLabel.setTextFill(Color.color(0, 0, 0));
    }

    private void updateStatusLabel(String msg) {
        statusLabel.setText(msg);
    }

    private void startTimer() {
        runTimer = new Timer();
        runTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    long elapsed = System.nanoTime() - startTime;
                    String formattedTime = formatTime(elapsed);
                    runTimerLabel.setText(formattedTime);
                    if (getSec(elapsed) > (60*17) + 59) {
                        // Red 2 minute warning
                        runTimerLabel.setTextFill(Color.color(1, 0, 0));
                    } else if (getSec(elapsed) > (60*14) + 59) {
                        // Orange 5 minute warning
                        runTimerLabel.setTextFill(Color.color(1, 0.6, 0));
                    } else {
                        runTimerLabel.setTextFill(Color.color(0, 0, 0));
                    }
                });
            }
        }, 1000, 1000);
    }
}
