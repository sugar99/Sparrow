package com.micerlab.sparrow.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import com.micerlab.sparrow.domain.file.FileType;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.micerlab.sparrow.service.fileStore.FileStoreService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ConnectException;
import java.util.*;

@Component
public class FileUtil {

    @Value("${file.temp.path}")
    private String tempFilePath;

    @Autowired
    @Qualifier("minioService")
    private FileStoreService fileStoreService;

    @Autowired
    private VideoUtil videoUtil;

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private static final String exts_dir = "classpath:static/type_exts/";

    /**
     * 加载拓展名配置文件，获取特定类型下的拓展名
     * @param type 文档类型
     * @return 该类型下的拓展名
     */
    public static List<String> loadTypeExtsConfig(String type)
    {
        String filePath = null;
        try
        {
            filePath = exts_dir + type + "_exts.txt";
            File type_exts_file = ResourceUtils.getFile(filePath);
            Scanner scanner = new Scanner(type_exts_file);
            List<String> exts = new ArrayList<>();
            while (scanner.hasNext())
                exts.add(scanner.next());
            return exts;
        } catch (FileNotFoundException e)
        {
            String errorMsg = "无法加载拓展名配置文件:" + filePath;
            logger.error(errorMsg);
        }
        return Collections.emptyList();
    }

    public String getFileType(String suffix) {
        return FileType.fromExt(suffix).toString();
    }

    public String getFileStorePath(String suffix){
        return getFileType(suffix) + "/";
    }

    public String generateFileId(){
        return UUID.randomUUID().toString();
    }

    public String getFileExtension(String fileName){
        return fileName.split("\\.")[fileName.split("\\.").length - 1];
    }

    public Map<String, Object> getThumbnailInfo(File inputFile) throws IOException {
        Map<String, Object> thumbnailInfo = new HashMap<>(2);
        String fileType = FileType.fromExt(getFileExtension(inputFile.getName())).toString();

        if(getFileExtension(inputFile.getName()).equals("pdf")){
            thumbnailInfo = fileStoreService.uploadThumbnail(generateThumbnail(inputFile));
        }else if(fileType.equals("doc")){
            File pdfFile = transferDocToPdf(inputFile);
            thumbnailInfo = fileStoreService.uploadThumbnail(generateThumbnail(pdfFile));
        }else if(fileType.equals("image")){
            thumbnailInfo = fileStoreService.uploadThumbnail(inputFile);
        }else if(fileType.equals("video")){
            /**获取视频第一帧作为缩略图*/
            File thumbnail = videoUtil.getVideoFrame(inputFile);
            thumbnailInfo = fileStoreService.uploadThumbnail(thumbnail);
        }else{
            thumbnailInfo.put("thumbnail_path","default_thumbnail.jpg");
            thumbnailInfo.put("thumbnail_url","");
        }

        return thumbnailInfo;
    }

    /**
     * 生成pdf首页图片(png)
     * @param inputFile
     * @return
     * @throws IOException
     */
    public File generateThumbnail(File inputFile) throws IOException {
        PDDocument pdDocument = PDDocument.load(inputFile);
        PDFRenderer renderer = new PDFRenderer(pdDocument);

        BufferedImage tempImage = renderer.renderImageWithDPI(0,100, ImageType.RGB);
        Iterator<ImageWriter> it = ImageIO.getImageWritersBySuffix("png");

        String thumbnailName = tempFilePath + File.separatorChar + UUID.randomUUID() + ".png";

        File outputFile = new File(thumbnailName);

        ImageWriter writer = (ImageWriter) it.next();
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(new FileOutputStream(outputFile));
        writer.setOutput(imageOutputStream);
        writer.write(new IIOImage(tempImage,null,null));
        tempImage.flush();
        imageOutputStream.flush();
        imageOutputStream.close();
        pdDocument.close();

        return outputFile;
    }

    public File transferDocToPdf(File inputFile) throws IOException {
        String pdfFilePath = tempFilePath + File.separatorChar + UUID.randomUUID() + ".pdf";
        String docFilePath = tempFilePath + File.separatorChar + UUID.randomUUID() + "." + getFileExtension(inputFile.getName());

        InputStream inputStream = new FileInputStream(inputFile);

        File docFile = new File(docFilePath);
        File pdfFile = new File(pdfFilePath);

        try{
            OutputStream outputStream = new FileOutputStream(docFile);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,bytesRead);
            }
            outputStream.close();
            inputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
        try {
            connection.connect();
        }catch (ConnectException e){
            System.err.println("文件转换出错，请检查OpenOffice服务是否启动。");
        }

        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
        converter.convert(docFile,pdfFile);
        connection.disconnect();

        /**删除产生的临时文件*/
        docFile.delete();

        return pdfFile;
    }
}
