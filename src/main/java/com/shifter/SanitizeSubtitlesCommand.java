package com.shifter;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import static com.shifter.Constants.*;

public class SanitizeSubtitlesCommand implements CommandAbstract {

    private final List<Subtitle> subtitleList;
    private final List<Subtitle> sanitizedList;

    public SanitizeSubtitlesCommand(List<Subtitle> subtitleList) {
        this.subtitleList = subtitleList;
        this.sanitizedList = new LinkedList<>();
    }

    @Override
    public void execute() {
        // get last timestamp
        Subtitle last = subtitleList.get(subtitleList.size() - 1);

        // for each subtitle if timestamp is higher than last timestamp - remove
        subtitleList.removeIf((current) -> isValid(current, last));

        // recalculate numbers
        int counter = 1;
        for (Subtitle value : subtitleList) {
            String number = String.valueOf(counter++);
            sanitizedList.add(new Subtitle(number, value.timestamp, value.text));
        }
    }

    private boolean isValid(Subtitle current, Subtitle last) {
        LocalTime startTimeCurrent = LocalTime.parse(current.timestamp.split(" --> ")[0], formatter);
        LocalTime startTimeLast = LocalTime.parse(last.timestamp.split(" --> ")[0], formatter);
        return !startTimeCurrent.isBefore(startTimeLast);
    }

    public List<Subtitle> getSanitizedList() {
        return sanitizedList;
    }
}
