package com.toandfrompdf.controllers;

import com.toandfrompdf.exceptions.IncorrectFileFormat;
import com.toandfrompdf.services.ToPDFService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/to-pdf")
public class ToPDFController {

    private final ToPDFService toPDFService;

    @Autowired
    public ToPDFController(ToPDFService toPDFService) {
        this.toPDFService = toPDFService;
    }

    @PostMapping(value = "/from-image",produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> convertImageToPDF(@RequestParam("images") MultipartFile[] multipartFile) throws IncorrectFileFormat, IOException {
        if(multipartFile.length==0) return ResponseEntity.noContent().build();

        String imageFormat=multipartFile[0].getContentType();

        assert imageFormat != null;
        if(!(imageFormat.equals("image/jpeg") || imageFormat.equals("image/png"))) {
            throw new IncorrectFileFormat(STR."Expected Image Format filed value as JPEG,PNG but found \{imageFormat}");
        }
        String fileName= STR."images-to-pdf-\{RandomStringUtils.random(7, true, true)}";
        return   ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,STR."attachment; filename=\"\{fileName}.pdf\"")
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(toPDFService.convertImageToPDF(multipartFile));

    }
}
