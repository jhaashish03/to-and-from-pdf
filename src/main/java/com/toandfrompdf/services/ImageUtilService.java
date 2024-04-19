package com.toandfrompdf.services;

import com.toandfrompdf.exceptions.ImageFormatChangeException;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ImageUtilService {

    public byte[] changeImageFormat(MultipartFile multipartFile,String imageFormat ) throws IOException, ImageFormatChangeException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        BufferedImage bufferedImage= ImageIO.read(multipartFile.getInputStream());
          boolean result=  ImageIO.write(bufferedImage,imageFormat,byteArrayOutputStream);

        if(!result) throw new ImageFormatChangeException("Unexpected behaviour, something went wrong with the image provided and the format expected");

        return byteArrayOutputStream.toByteArray();
    }

    public byte[] compressImage(MultipartFile multipartFile,double imageQuality) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Thumbnails.of(multipartFile.getInputStream())
                .scale(1)
                .outputQuality(imageQuality)
                .toOutputStream(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
