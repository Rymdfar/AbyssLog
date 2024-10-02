package Models;

import java.time.LocalDateTime;
import java.util.*;

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

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}
