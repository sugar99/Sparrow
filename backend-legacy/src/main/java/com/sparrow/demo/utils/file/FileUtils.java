package com.sparrow.demo.utils.file;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.sparrow.demo.utils.OssUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * @author allen
 */
@Component
public class FileUtils {
    @Value("${file.temp.path}")
    private String tempFilePath;

    @Value("${thumbnail.default.url}")
    private String defaultUrl;

    @Autowired
    private OssUtils ossUtils;

    @Autowired
    private VideoUtils videoUtils;

    public String getFileLogicPath(File file) throws FileNotFoundException {
        String path = "";

//        /** 使用读取文件头方式获取文件类型*/
//        String fileType = JudgeFileTypeByHead.getFileType(JudgeFileTypeByHead.judgeType(new FileInputStream(file)));

        //使用读取文件后缀名获取文件类型
        String fileType = getFileType(file);

        //TODO: 构建逻辑路径 — 缺少根据用户群组与用户构建路径
        path = "admin/" + "allen/" + fileType + "/";
        return path;
    }


    public String getFileExtension(String fileName){
        return fileName.split("\\.")[fileName.split("\\.").length - 1];
    }

    public String getFileType(File inputFile){
        String type = "others";

        String suffix = getFileExtension(inputFile.getName());

        FileSuffixType[] pics = { FileSuffixType.BMP, FileSuffixType.JPG, FileSuffixType.JPEG, FileSuffixType.PNG, FileSuffixType.GIF, FileSuffixType.TIFF, FileSuffixType.ICO
                /*, FileSuffixType.WEBP, FileSuffixType.PCX, FileSuffixType.TGA, FileSuffixType.EXIF, FileSuffixType.FPX, FileSuffixType.SVG, FileSuffixType.PSD, FileSuffixType.CDR,
                FileSuffixType.PCD, FileSuffixType.DXF, FileSuffixType.UFO, FileSuffixType.EPS, FileSuffixType.AI, FileSuffixType.HDRI, FileSuffixType.RAW, FileSuffixType.WNF,
                FileSuffixType.FLIC, FileSuffixType.EMF*/
        };

        FileSuffixType[] videos = { FileSuffixType.MP4, FileSuffixType.AVI, FileSuffixType.FLV, FileSuffixType.MKV, FileSuffixType.WMV, FileSuffixType.MOV
                /*, FileSuffixType.ASF, FileSuffixType.ASX, FileSuffixType.RM, FileSuffixType.RA, FileSuffixType.RMVB, FileSuffixType.QT, FileSuffixType.DAT, FileSuffixType.MPEG, FileSuffixType.MPG */
        };

        FileSuffixType[] audios = { FileSuffixType.MP3, FileSuffixType.WAV };

        FileSuffixType[] docs = { FileSuffixType.DOC, FileSuffixType.DOCX, FileSuffixType.TXT, FileSuffixType.XLS, FileSuffixType.XLSX, FileSuffixType.PPT, FileSuffixType.PPTX,
                FileSuffixType.PDF
                /*, FileSuffixType.VSDX*/
        };

        for (FileSuffixType fileSuffixType : audios){
            if (fileSuffixType.getValue().equals(suffix)){
                type = "audio";
                return type;
            }
        }

        for (FileSuffixType fileSuffixType : docs){
            if (fileSuffixType.getValue().equals(suffix)){
                type = "document";
                return type;
            }
        }

        for (FileSuffixType fileSuffixType : videos){
            if (fileSuffixType.getValue().equals(suffix)){
                type = "video";
                return type;
            }
        }

        for (FileSuffixType fileSuffixType : pics) {
            if (fileSuffixType.getValue().equals(suffix)) {
                type = "picture";
                return type;
            }
        }

        return type;
    }

    public String getFileType(String fileName){
        String type = "others";

        String suffix = getFileExtension(fileName);

        FileSuffixType[] pics = { FileSuffixType.BMP, FileSuffixType.JPG, FileSuffixType.JPEG, FileSuffixType.PNG, FileSuffixType.GIF, FileSuffixType.TIFF, FileSuffixType.ICO
                /*, FileSuffixType.WEBP, FileSuffixType.PCX, FileSuffixType.TGA, FileSuffixType.EXIF, FileSuffixType.FPX, FileSuffixType.SVG, FileSuffixType.PSD, FileSuffixType.CDR,
                FileSuffixType.PCD, FileSuffixType.DXF, FileSuffixType.UFO, FileSuffixType.EPS, FileSuffixType.AI, FileSuffixType.HDRI, FileSuffixType.RAW, FileSuffixType.WNF,
                FileSuffixType.FLIC, FileSuffixType.EMF*/
        };

        FileSuffixType[] videos = { FileSuffixType.MP4, FileSuffixType.AVI, FileSuffixType.FLV, FileSuffixType.MKV, FileSuffixType.WMV, FileSuffixType.MOV
                /*, FileSuffixType.ASF, FileSuffixType.ASX, FileSuffixType.RM, FileSuffixType.RA, FileSuffixType.RMVB, FileSuffixType.QT, FileSuffixType.DAT, FileSuffixType.MPEG, FileSuffixType.MPG */
        };

        FileSuffixType[] audios = { FileSuffixType.MP3, FileSuffixType.WAV };

        FileSuffixType[] docs = { FileSuffixType.DOC, FileSuffixType.DOCX, FileSuffixType.TXT, FileSuffixType.XLS, FileSuffixType.XLSX, FileSuffixType.PPT, FileSuffixType.PPTX,
                FileSuffixType.PDF
                /*, FileSuffixType.VSDX*/
        };

        for (FileSuffixType fileSuffixType : audios){
            if (fileSuffixType.getValue().equals(suffix)){
                type = "audio";
                return type;
            }
        }

        for (FileSuffixType fileSuffixType : docs){
            if (fileSuffixType.getValue().equals(suffix)){
                type = "document";
                return type;
            }
        }

        for (FileSuffixType fileSuffixType : videos){
            if (fileSuffixType.getValue().equals(suffix)){
                type = "video";
                return type;
            }
        }

        for (FileSuffixType fileSuffixType : pics) {
            if (fileSuffixType.getValue().equals(suffix)) {
                type = "picture";
                return type;
            }
        }
        return type;
    }

    public Map<String, Object> getThumbnailInfo(File inputFile) throws IOException {
        Map<String, Object> thumbnailInfo = new HashMap<>(2);
        FileSuffixType[] officeDoc = { FileSuffixType.DOC, FileSuffixType.DOCX, FileSuffixType.PPT, FileSuffixType.PPTX, FileSuffixType.TXT, FileSuffixType.XLS, FileSuffixType.XLSX };
        FileSuffixType[] imageTypes = { FileSuffixType.JPG, FileSuffixType.JPEG, FileSuffixType.PNG, FileSuffixType.BMP, FileSuffixType.ICO, FileSuffixType.GIF, FileSuffixType.TIFF };
        FileSuffixType[] videoTypes = { FileSuffixType.MP4, FileSuffixType.FLV, FileSuffixType.AVI, FileSuffixType.MKV, FileSuffixType.WMV, FileSuffixType.MOV };
        String fileType = getFileExtension(inputFile.getName());
        int flag = 0;

        if (FileSuffixType.PDF.getValue().equals(fileType)){
            thumbnailInfo = ossUtils.uploadThumbnailToOss(generateThumbnail(inputFile));
            flag = 1;
        }

        for (FileSuffixType fileSuffix : officeDoc) {
            if (fileSuffix.getValue().equals(fileType)) {
                File pdfFile = transferDocToPdf(inputFile);
                thumbnailInfo = ossUtils.uploadThumbnailToOss(generateThumbnail(pdfFile));
                flag = 1;
                break;
            }
        }

        for (FileSuffixType imageType : imageTypes) {
            if (imageType.getValue().equals(fileType)) {
                thumbnailInfo = ossUtils.uploadThumbnailToOss(inputFile);
                flag = 1;
                break;
            }
        }

        for (FileSuffixType videoType : videoTypes) {
            if (videoType.getValue().equals(fileType)) {
                /**获取视频第一帧作为缩略图*/
                File thumbnail = videoUtils.getVideoFrame(inputFile);
                thumbnailInfo = ossUtils.uploadThumbnailToOss(thumbnail);
                flag = 1;
                break;
            }
        }

        if (flag == 0){
            thumbnailInfo.put("thumbnail_path","default_thumbnail.jpg");
            thumbnailInfo.put("thumbnail_url",defaultUrl);
        }

        return thumbnailInfo;
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

    /**
     * 生成pdf首页图片(png)
     * @param inputFile
     * @return
     * @throws IOException
     */
    public File generateThumbnail(File inputFile) throws IOException {
        PDDocument pdDocument = PDDocument.load(inputFile);
        PDFRenderer renderer = new PDFRenderer(pdDocument);

        BufferedImage tempImage = renderer.renderImageWithDPI(0,100,ImageType.RGB);
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


    /** 将图片转换成base64 字符串，考虑到转换后字符串长度过大，从ES 中获取时的效率问题，初步弃用*/
    private String imageToBase64Str(File file){
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(file);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    public File base64StrToImage(String base64Str, String path){
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(base64Str);
            for (int i = 0; i < b.length; ++i){
                if (b[i] > 0){
                    b[i] += 256;
                }
            }
            File file = new File(path);
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(b);
            outputStream.flush();
            outputStream.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getThumbnailStr(File file) throws IOException {
        String thumbnailStr = "";

        FileSuffixType[] officeDoc = { FileSuffixType.DOC, FileSuffixType.DOCX, FileSuffixType.PPT, FileSuffixType.PPTX, FileSuffixType.TXT, FileSuffixType.XLS, FileSuffixType.XLSX };
        FileSuffixType[] imageTypes = { FileSuffixType.JPG, FileSuffixType.JPEG, FileSuffixType.PNG, FileSuffixType.BMP, FileSuffixType.ICO, FileSuffixType.GIF, FileSuffixType.TIFF };
        FileSuffixType[] videoTypes = { FileSuffixType.MP4, FileSuffixType.FLV, FileSuffixType.AVI, FileSuffixType.MKV, FileSuffixType.WMV, FileSuffixType.MOV };
        String fileType = getFileExtension(file.getName());
        int flag = 0;
        if (FileSuffixType.PDF.getValue().equals(fileType)){
            thumbnailStr = imageToBase64Str(generateThumbnail(file));
            flag = 1;
        }

        for (FileSuffixType imageType : imageTypes) {
            if (imageType.getValue().equals(fileType)) {
                thumbnailStr = imageToBase64Str(file);
                flag = 1;
                break;
            }
        }

        for (FileSuffixType fileSuffix : officeDoc) {
            if (fileSuffix.getValue().equals(fileType)) {
                File pdfFile = transferDocToPdf(file);
                File thumbnailImage = generateThumbnail(pdfFile);
                thumbnailStr = imageToBase64Str(thumbnailImage);
                flag = 1;
                break;
            }
        }

        for (FileSuffixType videoType : videoTypes) {
            if (videoType.getValue().equals(fileType)) {
                /**获取视频第一帧作为缩略图*/
                File thumbnail = videoUtils.getVideoFrame(file);
                thumbnailStr = imageToBase64Str(thumbnail);
                flag = 1;
                break;
            }
        }

        if (flag == 0){
            String filePath = "image/default_thumbnail.jpg";
            File thumbImage = new File(filePath);
            thumbnailStr = imageToBase64Str(thumbImage);
        }

        return thumbnailStr;
    }
}
