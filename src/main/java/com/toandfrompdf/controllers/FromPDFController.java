package com.toandfrompdf.controllers;

import com.toandfrompdf.exceptions.IncorrectFileFormat;
import com.toandfrompdf.services.FromPDFService;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/from-pdf")
public class FromPDFController {

    private final FromPDFService fromPDFService;

    @Autowired
    public FromPDFController(FromPDFService fromPDFService) {
        this.fromPDFService = fromPDFService;
    }



    //PDF TO WORD CONVERSION

    @PostMapping(value = "/to-word", produces=MediaType.ALL_VALUE)
    public ResponseEntity<byte[]> fromPdfToWord(@RequestPart("pdfFile") MultipartFile multipartFile,@RequestParam(value = "formatMaintained",required = false) boolean formatMaintained ) throws IncorrectFileFormat, IOException {
        if(multipartFile.isEmpty()) return ResponseEntity.badRequest().build();

        if(!Objects.equals(multipartFile.getContentType(), "application/pdf")) throw new IncorrectFileFormat(STR."Exception File format .pdf:- Found \{multipartFile.getContentType()}");

        String fileName= Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(0, multipartFile.getOriginalFilename().lastIndexOf("."));



        return  ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, STR."attachment; filename=\"\{fileName}.docx\"").
                contentType(MediaType.parseMediaType("application/msword")).body(fromPDFService.fromPdfToWord(multipartFile,formatMaintained));
    }


    //PDF TO IMAGE CONVERSION WITH PAGE NO

    @PostMapping(value = "/to-images")
    public ResponseEntity<byte[]> fromPdfToImages(@RequestPart("pdfFile") MultipartFile multipartFile,@RequestPart("imageFormat") String imageFormat,@RequestPart("pageNo")
    @Positive(message = "page number should be greater than 1 and less than equal to total pages in pdf file") String pageNo)
            throws IncorrectFileFormat, IOException {

        if(multipartFile.isEmpty()) return ResponseEntity.badRequest().build();

        if(!Objects.equals(multipartFile.getContentType(), "application/pdf")) {
            throw new IncorrectFileFormat(STR."Exception File format .pdf:- Found \{multipartFile.getContentType()}");
        }

        imageFormat=imageFormat.toUpperCase();

       if(!(imageFormat.equals("JPEG") || imageFormat.equals("PNG"))) {
           throw new IncorrectFileFormat(STR."Expected Image Format filed value as JPEG,PNG but found \{imageFormat}");
       }
       String fileName= Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(0, multipartFile.getOriginalFilename().lastIndexOf("."));

     return   ResponseEntity.ok()
             .header(HttpHeaders.CONTENT_DISPOSITION,StringTemplate.STR."attachment; filename=\"image-\{fileName}pg\{pageNo}.\{imageFormat.toLowerCase()}\"")
             .contentType(MediaType.parseMediaType(STR."image/\{imageFormat.toLowerCase()}"))
             .body(fromPDFService.pdfToImages(multipartFile,imageFormat,Integer.parseInt(pageNo)));

    }





}
