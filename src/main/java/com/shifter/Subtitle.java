package com.shifter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static com.shifter.Constants.*;

class Subtitle {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");

    private final String number;
    private final String timestamp;
    private final String text;

    public Subtitle(String number, String timestamp, String text) {
        this.number = number;
        this.timestamp = timestamp;
        this.text = text;
    }

    public Subtitle shift(int millisecondsDelay) {
        String[] times = timestamp.split(SRT_ARROW);
        String startTimeString = times[0];
        String endTimeString = times[1];

        LocalTime startTime = LocalTime.parse(startTimeString, formatter);
        LocalTime endTime = LocalTime.parse(endTimeString, formatter);

        String formattedStartTime = startTime.plus(millisecondsDelay, ChronoUnit.MILLIS).format(formatter);
        String formattedEndTime = endTime.plus(millisecondsDelay, ChronoUnit.MILLIS).format(formatter);
        String delayedTimestamp = String.join("", formattedStartTime, SRT_ARROW, formattedEndTime);

        return new Subtitle(number, delayedTimestamp, text);
    }

    public String toString() {
        return String.join(NEWLINE, number, timestamp, text) + DOUBLE_NEWLINE;
    }

}
