package app.javacode;

import java.util.ArrayList;
import java.util.List;

public class MyStringBuilder {
    private StringBuilder stringBuilder;
    private final List<String> snapshot;
    private int currentSnapshotIndex;

    public MyStringBuilder() {
        this.stringBuilder = new StringBuilder();
        this.snapshot = new ArrayList<>();
        this.currentSnapshotIndex = -1;
        saveSnapshot();
    }

    private void saveSnapshot() {
        while (currentSnapshotIndex < snapshot.size() - 1) {
            snapshot.remove(snapshot.size() - 1);
        }
        snapshot.add(stringBuilder.toString());
        currentSnapshotIndex++;
    }

    public void undo() {
        if (currentSnapshotIndex > 0) {
            currentSnapshotIndex--;
            restoreSnapshot();
        }
    }

    private void restoreSnapshot() {
        stringBuilder = new StringBuilder(snapshot.get(currentSnapshotIndex));
    }

    public MyStringBuilder append(String string){
        stringBuilder.append(string);
        saveSnapshot();
        return this;
    }

    public MyStringBuilder delete(int start, int end){
        stringBuilder.delete(start, end);
        saveSnapshot();
        return this;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}
