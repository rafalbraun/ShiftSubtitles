package com.shifter;

import java.util.List;
import java.util.stream.Collectors;

import static com.shifter.Constants.NUMBER_OF_MILISECONDS_IN_SECOND;

public class ShiftSubtitlesCommand implements CommandAbstract {
    private final Integer timeshift;
    private final List<Subtitle> subtitleList;

    private List<Subtitle> outputList;

    public ShiftSubtitlesCommand(List<Subtitle> subtitleList, String timeshift) {
        this.subtitleList = subtitleList;
        this.timeshift = parseTimeshift(timeshift);
    }

    @Override
    public void execute() {
        outputList = subtitleList.stream()
                .map(item -> item.shift(timeshift))
                .collect(Collectors.toList());
    }

    /*
        String numericStr = timeshift.replaceAll("[^0-9.]", "");
        double resultDouble = Double.parseDouble(numericStr) * NUMBER_OF_MILISECONDS_IN_SECOND;
        return (int) resultDouble;
    * */
    public static Integer parseTimeshift(String timeshift) {

        if (timeshift == null || timeshift.isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null or empty");
        }

        char sign = timeshift.charAt(0);
        if (sign != '+' && sign != '-') {
            throw new IllegalArgumentException("Input string must start with '+' or '-'");
        }

        double doubleValue = Double.parseDouble(timeshift.substring(1));

        if (sign == '-') {
            doubleValue *= -1;
        }

        return (int)doubleValue * NUMBER_OF_MILISECONDS_IN_SECOND;
    }

    public List<Subtitle> getSubtitleList() {
        return outputList;
    }

}
