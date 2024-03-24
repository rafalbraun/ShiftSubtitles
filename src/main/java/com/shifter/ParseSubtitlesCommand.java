package com.shifter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.shifter.Constants.DOUBLE_NEWLINE;
import static com.shifter.Constants.NEWLINE;

public class ParseSubtitlesCommand implements CommandAbstract {

    private final InputStream inputStream;
    private final List<Subtitle> subtitleList = new LinkedList<>();

    public ParseSubtitlesCommand(InputStream inputFile) {
        this.inputStream = inputFile;
    }

    @Override
    public void execute() {
        List<String> segments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(this.inputStream))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(NEWLINE);
            }
            /* replace all occurrences of more than two newlines with exactly two newlines */
            String result = sb.toString().replaceAll("\n{3,}", DOUBLE_NEWLINE);
            String[] splitSegments = result.split(DOUBLE_NEWLINE);
            segments.addAll(Arrays.asList(splitSegments));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String segment : segments) {
            String[] splitted = segment.split(NEWLINE);
            String number = splitted[0];
            String timestamp = splitted[1];
            String text = String.join(NEWLINE,  Arrays.copyOfRange(splitted, 2, splitted.length));
            subtitleList.add(new Subtitle(number, timestamp, text));
        }
    }

    public List<Subtitle> getSubtitleList() {
        return subtitleList;
    }
}
