package com.micerlab.sparrow.utils;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class VideoUtil {

    @Value("/root/sparrow/temp")
    private String tempFilePath;

    public File getVideoFrame(File videoFile) throws IOException {
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
        ff.start();

        int i = 0;
        int length = ff.getLengthInFrames();
        Frame frame = null;
        while (i < length) {
            frame = ff.grabFrame();
            /** 过滤前5 帧，避免出现全黑的图片 */
            if ((i > 5) && (frame.image != null)) {
                break;
            }
            i++;
        }

        String imagePath = tempFilePath + File.separatorChar + UUID.randomUUID() + ".png";
        File frameImage = doExecuteFrame(frame, imagePath);

        ff.stop();

        return frameImage;
    }

    public File doExecuteFrame(Frame frame, String path) throws IOException {
        File frameImage = new File(path);
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);

        File fileParent = frameImage.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdir();
        }
        frameImage.createNewFile();
        /** 对图片进行缩放 */
//            int srcImageWidth = srcImage.getWidth();
//            int srcImageHeight = srcImage.getHeight();
//            int width = 480;
//            int height = (int) (((double) width / srcImageWidth) * srcImageHeight);
//            BufferedImage thumbnailImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
//            thumbnailImage.getGraphics().drawImage(srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

        try {
            ImageIO.write(bufferedImage, "png", frameImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return frameImage;
    }

    public File[] getVideoFramesByInterval(File videoFile) throws IOException {
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);

        String dirPath = tempFilePath + File.separatorChar + UUID.randomUUID() + File.separatorChar;

        ff.start();
        int ftp = ff.getLengthInFrames();
        int interval1 = ftp / 4;
        System.out.println(ftp);
        Frame frame = null;
        int flag = 0;
        int count = 0;
        while(flag < ftp){
            frame = ff.grabFrame();
            if(flag == count * interval1) {
                System.out.println(flag);
                while(frame.image == null && flag < ftp){
                    flag++;
                    frame = ff.grabFrame();
                }
                if(frame.image != null) {
                    String imagePath = dirPath + "keyFrame_" + count + ".png";
                    doExecuteFrame(frame, imagePath);
                    System.out.println(count);
                    count++;
                }
            }
            flag++;
        }
        ff.stop();

        return new File(dirPath).listFiles();
    }
}
