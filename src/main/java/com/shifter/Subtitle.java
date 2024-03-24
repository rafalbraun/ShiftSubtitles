package com.shifter;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static com.shifter.Constants.*;

public class Subtitle implements Comparable<Subtitle> {

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

    @Override
    public String toString() {
        return String.join(NEWLINE, number, timestamp, text) + DOUBLE_NEWLINE;
    }

    @Override
    public int compareTo(Subtitle other) {
        String[] timesThis = this.timestamp.split(SRT_ARROW);
        String startTimeThisString = timesThis[0];
        String endTimeThisString = timesThis[1];

        String[] timesOther = other.timestamp.split(SRT_ARROW);
        String startTimeOtherString = timesOther[0];
        String endTimeOtherString = timesOther[1];

        Duration durationThis = Duration.between(LocalTime.parse(startTimeThisString, formatter), LocalTime.parse(endTimeThisString, formatter));
        Duration durationOther = Duration.between(LocalTime.parse(startTimeOtherString, formatter), LocalTime.parse(endTimeOtherString, formatter));
        if (!durationThis.equals(durationOther)) {
            throw new IllegalStateException("Comparison not possible, there is different delay between start and end of timeframe.");
        }

        Duration timeshiftBetweenTimestamps = Duration.between(LocalTime.parse(startTimeThisString, formatter), LocalTime.parse(startTimeOtherString, formatter));
        return (int) timeshiftBetweenTimestamps.toMillis();
    }

}
