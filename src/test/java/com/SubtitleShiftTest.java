package com;

import com.shifter.Subtitle;
import org.junit.jupiter.api.Test;

import com.shifter.ParseSubtitlesCommand;
import com.shifter.PrintSubtitlesCommand;
import com.shifter.ShiftSubtitlesCommand;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SubtitleShiftTest {

    private static final String INPUT_FILE = "src/test/resources/input.srt";
    private static final String TEMP_FILE = "src/test/resources/temp.srt";
    private static final String SHIFTED_FILE = "src/test/resources/shifted.srt";
    private static final String TIMESHIFT = "+10.0";

    @Test
    public void subtitleParseTest() throws Exception {
        File inputFile = new File(INPUT_FILE);
        InputStream inputStream = Files.newInputStream(inputFile.toPath());

        ParseSubtitlesCommand parseJob = new ParseSubtitlesCommand(inputStream);
        parseJob.execute();

        assertEquals(1964, parseJob.getSubtitleList().size());
    }

    @Test
    public void subtitlePrintTest() throws IOException {
        Path inputFile = Paths.get(INPUT_FILE);
        Path outputFile = Paths.get(TEMP_FILE);
        if (Files.exists(outputFile)) Files.delete(outputFile);
        assertFalse(Files.exists(outputFile), "File should not exist after removal: " + TEMP_FILE);

        try (
                PrintWriter writer = new PrintWriter(new FileWriter(outputFile.toFile()));
                InputStream inputStream = Files.newInputStream(inputFile)
        ) {
            ParseSubtitlesCommand parseJob = new ParseSubtitlesCommand(inputStream);
            parseJob.execute();
            PrintSubtitlesCommand printJob = new PrintSubtitlesCommand(parseJob.getSubtitleList());
            printJob.execute();

            // print parsed srt input file into the output srt file without changing contents
            writer.println(printJob.getFormattedSubtitles());

        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(Files.exists(outputFile), "File should exist after printing: " + TEMP_FILE);
        assertEquals(Files.lines(inputFile).count(), Files.lines(outputFile).count());
    }

    @Test
    public void subtitleShiftTest() {
        Path inputFile = Paths.get(INPUT_FILE);
        try (
                InputStream inputStream = Files.newInputStream(inputFile)
        ) {
            ParseSubtitlesCommand parseJob = new ParseSubtitlesCommand(inputStream);
            parseJob.execute();
            ShiftSubtitlesCommand shiftJob = new ShiftSubtitlesCommand(parseJob.getSubtitleList(), TIMESHIFT);
            shiftJob.execute();

            List<Subtitle> subtitleList = parseJob.getSubtitleList();
            List<Subtitle> shiftedSubtitleList = shiftJob.getSubtitleList();

            int timeshift = ShiftSubtitlesCommand.parseTimeshift(TIMESHIFT);
            int size = subtitleList.size();
            for (int i = 0; i < size; i++) {
                Subtitle element1 = subtitleList.get(i);
                Subtitle element2 = shiftedSubtitleList.get(i);
                assertEquals(timeshift, element1.compareTo(element2));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO
    // test for negative shift
    // test for renumerating shifted subtitles


}