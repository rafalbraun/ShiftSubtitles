package com.controllers;

import com.shifter.ParseSubtitlesCommand;
import com.shifter.PrintSubtitlesCommand;
import com.shifter.SanitizeSubtitlesCommand;
import com.shifter.ShiftSubtitlesCommand;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ResyncController {

    @PostMapping(value = "/resync", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("timeshift") String timeshift) throws IOException {
        // Check if file is empty
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload".getBytes());
        }

        // Check if the file extension is ".srt"
        if (!StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), ".srt")) {
            return ResponseEntity.badRequest().body("Please upload an SRT file".getBytes());
        }

        ParseSubtitlesCommand parseJob = new ParseSubtitlesCommand(file.getInputStream());
        parseJob.execute();
        ShiftSubtitlesCommand shiftJob = new ShiftSubtitlesCommand(parseJob.getSubtitleList(), timeshift);
        shiftJob.execute();
        SanitizeSubtitlesCommand sanitizeJob = new SanitizeSubtitlesCommand(shiftJob.getSubtitleList());
        sanitizeJob.execute();
        PrintSubtitlesCommand printJob = new PrintSubtitlesCommand(sanitizeJob.getSanitizedList());
        printJob.execute();

        byte[] fileContent = printJob.getFormattedSubtitles().getBytes();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + file.getOriginalFilename() + "\"")
                .body(fileContent);
    }

}
