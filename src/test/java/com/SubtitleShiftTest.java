package com;

import com.shifter.ParseSubtitlesCommand;
import com.shifter.PrintSubtitlesCommand;
import com.shifter.ShiftSubtitlesCommand;

import java.io.*;
import java.nio.file.Files;

public class SubtitleShiftTest {

    public static void main(String[] args) {

        File inputFile = new File("src/test/resources/The.Flash.2023.1080p.WEB-DL.H.264-RiGHTNOW.srt");
        File outputFile = new File("src/test/resources/output.srt");
        String timeshift = "+4.000";

        try (
            PrintWriter writer = new PrintWriter(new FileWriter(outputFile));
            InputStream inputStream = Files.newInputStream(inputFile.toPath())
        ) {

            ParseSubtitlesCommand parseJob = new ParseSubtitlesCommand(inputStream);
            parseJob.execute();
            ShiftSubtitlesCommand shiftJob = new ShiftSubtitlesCommand(parseJob.getSubtitleList(), timeshift);
            shiftJob.execute();
            PrintSubtitlesCommand printJob = new PrintSubtitlesCommand(shiftJob.getSubtitleList());
            printJob.execute();

            writer.println(printJob.getFormattedSubtitles());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}