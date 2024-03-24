package com;

import com.shifter.ParseSubtitlesCommand;
import com.shifter.PrintSubtitlesCommand;
import com.shifter.SanitizeSubtitlesCommand;
import com.shifter.ShiftSubtitlesCommand;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * this test performs full process of shifting subtitles
 */
public class MainTest {

    private static final String INPUT_FILE = "src/test/resources/input.srt";
    private static final String SHIFTED_FILE = "src/test/resources/shifted.srt";
    private static final String TIMESHIFT = "-66.0";

    @Test
    public void subtitlePrintTest() throws IOException {
        Path inputFile = Paths.get(INPUT_FILE);
        Path outputFile = Paths.get(SHIFTED_FILE);

        try (
                PrintWriter writer = new PrintWriter(new FileWriter(outputFile.toFile()));
                InputStream inputStream = Files.newInputStream(inputFile)
        ) {
            ParseSubtitlesCommand parseJob = new ParseSubtitlesCommand(inputStream);
            parseJob.execute();
            ShiftSubtitlesCommand shiftJob = new ShiftSubtitlesCommand(parseJob.getSubtitleList(), TIMESHIFT);
            shiftJob.execute();
            SanitizeSubtitlesCommand sanitizeJob = new SanitizeSubtitlesCommand(shiftJob.getSubtitleList());
            sanitizeJob.execute();
            PrintSubtitlesCommand printJob = new PrintSubtitlesCommand(sanitizeJob.getSanitizedList());
            printJob.execute();

            writer.println(printJob.getFormattedSubtitles());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
