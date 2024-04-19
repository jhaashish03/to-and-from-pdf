package com.toandfrompdf.services;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.graphics.PdfImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class ToPDFService {

    public byte[] convertImageToPDF(MultipartFile[] files) throws IOException {

        PdfDocument pdfDocument=new PdfDocument();
        pdfDocument.getPageSettings().setMargins(0);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        for(MultipartFile file:files){

        BufferedImage bufferedImage= ImageIO.read(file.getInputStream());

        PdfPageBase pageBase = pdfDocument.getPages().add(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));

        PdfImage pdfImage=PdfImage.fromImage(bufferedImage);

        pageBase.getCanvas().drawImage(pdfImage,0,0,pdfImage.getWidth(),pdfImage.getHeight());
        }

        pdfDocument.saveToStream(byteArrayOutputStream, FileFormat.PDF);

       return byteArrayOutputStream.toByteArray();


    }
}
