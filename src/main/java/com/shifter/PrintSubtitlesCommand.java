package com.shifter;

import java.util.List;
import java.util.stream.Collectors;

public class PrintSubtitlesCommand implements CommandAbstract {

    private final List<Subtitle> shiftedList;
    private String formattedSubtitles;

    public PrintSubtitlesCommand(List<Subtitle> shiftedList) {
        this.shiftedList = shiftedList;
    }

    @Override
    public void execute() {
        this.formattedSubtitles = shiftedList.stream()
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    public String getFormattedSubtitles() {
        return formattedSubtitles;
    }

}
