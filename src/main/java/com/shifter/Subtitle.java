package com.shifter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

class Subtitle {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");

    String number;
    String timestamp;
    String text;

    public Subtitle(String number, String timestamp, String text) {
        this.number = number;
        this.timestamp = timestamp;
        this.text = text;
    }

    public Subtitle shift(int millisecondsDelay) {
        String[] times = timestamp.split(" --> ");
        String startTimeString = times[0];
        String endTimeString = times[1];

        LocalTime startTime = LocalTime.parse(startTimeString, formatter);
        LocalTime endTime = LocalTime.parse(endTimeString, formatter);

        String formattedStartTime = startTime.plus(millisecondsDelay, ChronoUnit.MILLIS).format(formatter);
        String formattedEndTime = endTime.plus(millisecondsDelay, ChronoUnit.MILLIS).format(formatter);
        String delayedTimestamp = String.join(" ", formattedStartTime, "-->", formattedEndTime);

        return new Subtitle(number, delayedTimestamp, text);
    }

    public String toString() {
        return String.join("\n", number, timestamp, text) + "\n\n";
    }

}
