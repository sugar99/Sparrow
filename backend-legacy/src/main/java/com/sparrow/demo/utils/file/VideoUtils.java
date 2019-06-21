package com.sparrow.demo.utils.file;

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

/**
 * @author allen
 */
@Component
public class VideoUtils {

    @Value("${file.temp.path}")
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
        File frameImage = doExecuteFrame(frame,imagePath);

        ff.stop();

        return frameImage;
    }

    public File doExecuteFrame(Frame frame, String path) throws IOException {
        File frameImage = new File(path);
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);

        File fileParent = frameImage.getParentFile();
        if (! fileParent.exists()){
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
            ImageIO.write(bufferedImage,"png",frameImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return frameImage;
    }

    public File getVideoScreenShot(File videoFile, int time) throws IOException{
        FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(videoFile);
        ff.start();
        double frameRate = ff.getFrameRate();
        int frameIndex = (int) frameRate * time;

        Frame frame;
        ff.setFrameNumber(frameIndex);
        frame = ff.grabFrame();

        String imagePath = tempFilePath + File.separatorChar + UUID.randomUUID() + ".png";
        File frameImage = doExecuteFrame(frame,imagePath);

        ff.stop();

        return frameImage;
    }

    public File[] getVideoFramesByInterval(File videoFile) throws IOException{
        FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(videoFile);

        String dirPath = tempFilePath + File.separatorChar + UUID.randomUUID() + File.separatorChar;

        ff.start();
        double frameRate = ff.getFrameRate();
        /** 获取到的时间长度单位是微秒 */
        Long videoTimeLength =  ff.getLengthInTime();
        /** 将时间转换成秒，然后计算中间取帧的时间 */
        int videoTotalSecond = (int) (videoTimeLength / 1000 / 1000);
        int interval = videoTotalSecond / 6;

        Frame frame;
        for (int i = 1; i <= 5; i++){
            ff.setFrameNumber((int) (i * frameRate * interval));
            frame = ff.grabFrame();
            String imagePath = dirPath + "keyFrame_"+ i + ".png";
            doExecuteFrame(frame,imagePath);
        }

        ff.stop();

        return new File(dirPath).listFiles();
    }

}
