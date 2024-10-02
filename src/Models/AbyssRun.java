package Models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AbyssRun {
    private LocalDateTime startTimeStamp;
    private LocalDateTime stopTimeStamp;
    private long startNanos;
    private long stopNanos;
    private long elapsedTime;

    private HashMap<String, Integer> cargoItems = new HashMap<>();
    private HashMap<String, Integer> startingCargoItems = new HashMap<>();
    private HashMap<String, Integer> consumedItems = new HashMap<>();
    // Also need "newItems" for easy loot access? That ignores thing such as ammo, cap boosters etc.

    public void updateCargo(ArrayList<Map.Entry<String, Integer>> items, boolean isStart) {
        cargoItems.clear();
        for (Map.Entry item : items){
            int newQty = (Integer) item.getValue();
            if(!cargoItems.containsKey(item.getKey().toString())) {
                // New item
                cargoItems.put(item.getKey().toString(), newQty);
            } else {
                // more of existing item
                cargoItems.replace(item.getKey().toString(), newQty);
            }
        }
        if (isStart) {
            startingCargoItems.putAll(cargoItems);
        } else {
            // If it's not the start of a run it's assumed to be the end of one
            // Find partially consumed items
            for (Map.Entry item : cargoItems.entrySet()) {
                if (startingCargoItems.containsKey(item.getKey().toString())) {
                    int remainingQty = (int) item.getValue();
                    int startingQty = startingCargoItems.get(item.getKey().toString());
                    consumedItems.put(item.getKey().toString(), startingQty - remainingQty);
                }
            }
            // Find fully consumed items
            for (Map.Entry item : startingCargoItems.entrySet()) {
                if (!cargoItems.containsKey(item.getKey().toString())) consumedItems.put(item.getKey().toString(), (int) item.getValue());
            }
        }
    }

    public HashMap<String, Integer> getConsumedItems() {
        return consumedItems;
    }

    public void startRun(long startNanos) {
        startTimeStamp = LocalDateTime.now();
        this.startNanos = startNanos;
    }

    public void stopRun(long stopNanos) {
        stopTimeStamp = LocalDateTime.now();
        this.elapsedTime = stopNanos - startNanos;
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

    public LocalDateTime getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(LocalDateTime startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public LocalDateTime getStopTimeStamp() {
        return stopTimeStamp;
    }

    public void setStopTimeStamp(LocalDateTime stopTimeStamp) {
        this.stopTimeStamp = stopTimeStamp;
    }

    public HashMap<String, Integer> getCargoItems() {
        return cargoItems;
    }

    public void setCargoItems(HashMap<String, Integer> cargoItems) {
        this.cargoItems = cargoItems;
    }

    public String getElapsedTime() {
        return formatTime(elapsedTime);
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public LocalDate getDate() {
        return stopTimeStamp.toLocalDate();
    }
}
