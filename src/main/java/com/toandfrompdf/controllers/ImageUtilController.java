package com.toandfrompdf.controllers;

import com.toandfrompdf.exceptions.ImageFormatChangeException;
import com.toandfrompdf.services.ImageUtilService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController()
@RequestMapping("/api/image")
public class ImageUtilController {

    private final ImageUtilService imageUtilService;

    @Autowired
    public ImageUtilController(ImageUtilService imageUtilService) {
        this.imageUtilService = imageUtilService;
    }

    @PostMapping("/format-change")
    public ResponseEntity<byte[]> changeImageFormat(@RequestPart("image") MultipartFile image, @RequestPart("newFormat") @NotNull String newFormat) throws ImageFormatChangeException, IOException {
        if(image.isEmpty())return ResponseEntity.badRequest().build();

        String fileName= Objects.requireNonNull(image.getOriginalFilename()).substring(0, image.getOriginalFilename().lastIndexOf("."));

        return   ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,STR."attachment; filename=\"convertedImage-\{fileName}.\{newFormat.toLowerCase()}\"")
                .contentType(MediaType.parseMediaType(STR."image/\{newFormat.toLowerCase()}"))
                .body(imageUtilService.changeImageFormat(image,newFormat.toUpperCase()));



    }

    @PostMapping("/compress-image")
    public ResponseEntity<byte[]> compressImage(@RequestPart("image") MultipartFile image, @RequestParam(value = "outputQuality",required = false) double outputQuality) throws ImageFormatChangeException, IOException {
        if(image.isEmpty())return ResponseEntity.badRequest().build();

        if(outputQuality==0) outputQuality=0.5;

        int index= Objects.requireNonNull(image.getOriginalFilename()).lastIndexOf(".");

        String fileName= Objects.requireNonNull(image.getOriginalFilename()).substring(0, index);
        String format=image.getOriginalFilename().substring(index+1);

        return   ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,STR."attachment; filename=\"compressedImage-\{fileName}.\{format}\"")
                .contentType(MediaType.parseMediaType(STR."image/\{format}"))
                .body(imageUtilService.compressImage(image,outputQuality));

    }

}
