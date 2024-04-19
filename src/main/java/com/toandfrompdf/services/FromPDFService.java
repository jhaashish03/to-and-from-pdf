package com.toandfrompdf.services;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.graphics.PdfImageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class FromPDFService {



    public byte[] fromPdfToWord(MultipartFile multipartFile,boolean formatMaintained) throws IOException {

        PdfDocument pdfDocument=new PdfDocument();
        pdfDocument.loadFromStream(multipartFile.getInputStream());
         if(formatMaintained)  pdfDocument.getConvertOptions().setConvertToWordUsingFlow(true);

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        pdfDocument.saveToStream(byteArrayOutputStream,FileFormat.DOCX);
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] pdfToImages(MultipartFile multipartFile,String imageFormat,int pageNo) throws IOException {

        PdfDocument pdfDocument=new PdfDocument();
        pdfDocument.loadFromStream(multipartFile.getInputStream());

        if(pdfDocument.getPages().getCount()<pageNo) throw new IllegalArgumentException("page number should be greater than 1 and less than equal to total pages in pdf file");

             pdfDocument.getConvertOptions().setPdfToImageOptions(0);
            BufferedImage bufferedImage=pdfDocument.saveAsImage(pageNo-1, PdfImageType.Bitmap,300,300);
            BufferedImage newImage=new BufferedImage(bufferedImage.getWidth(),bufferedImage.getHeight(),BufferedImage.TYPE_INT_RGB);
            newImage.getGraphics().drawImage(bufferedImage,0,0,null);
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            ImageIO.write(newImage,imageFormat,byteArrayOutputStream);
            byteArrayOutputStream.close();
        pdfDocument.close();
        return byteArrayOutputStream.toByteArray();
    }
}
