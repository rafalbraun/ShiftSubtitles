package com.shifter;

import java.util.List;
import java.util.stream.Collectors;

import static com.shifter.Constants.NUMBER_OF_MILISECONDS_IN_SECOND;

public class ShiftSubtitlesCommand extends CommandAbstract {
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

    public static Integer parseTimeshift(String timeshift) {
        String numericStr = timeshift.replaceAll("[^0-9.]", "");
        double resultDouble = Double.parseDouble(numericStr) * NUMBER_OF_MILISECONDS_IN_SECOND;
        return (int) resultDouble;
    }

    public List<Subtitle> getSubtitleList() {
        return outputList;
    }

}
