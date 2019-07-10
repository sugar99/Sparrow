package com.micerlab.sparrow.service.fileStore;

import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.file.SpaFile;
import com.micerlab.sparrow.service.base.BaseService;
import com.micerlab.sparrow.utils.BusinessException;
import com.micerlab.sparrow.utils.FileUtil;
import com.micerlab.sparrow.utils.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("minioService")
public class FileStoreMinioServiceImpl implements FileStoreService {

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private FileUtil fileUtil;

    @Value("/root/sparrow/temp")
    private String tempFilePath;

    @Override
    public Result getPolicy(Map<String, Object> params, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        throw new BusinessException(ErrorCode.SERVER_ERR_OSS);
    }

    @Override
    public Result getPresignedUrl(Map<String, Object> params, HttpServletRequest httpServletRequest){
        // 文件扩展名
        String suffix = (String) params.get("ext");
        // OSS存储路径
        String path = fileUtil.getFileStorePath(suffix);
        // 文件ID
        String file_id = fileUtil.generateFileId();

        String objectName = path + "/" + file_id + "." + suffix;
        String url = minioUtil.getPutUrl(objectName);
        Map<String, String> respMap = new HashMap<>();
        respMap.put("url", url);
        respMap.put("creator", BaseService.getUser_Id(httpServletRequest));
        return Result.OK().data(respMap).build();
    }

    @Override
    public Result deleteFile(List<String> objectNames){
        for (String objectName: objectNames) {
            minioUtil.deleteFile(objectName);
        }
        return Result.OK().build();
    }

    @Override
    public void downloadFile(String title, String key, HttpServletResponse httpServletResponse){
        minioUtil.downloadFile(title, key, httpServletResponse);
    }

    @Override
    public Map<String, Object> uploadThumbnail(File file){
        return minioUtil.uploadThumbnail(file);
    }

    @Override
    public File getFile(SpaFile fileMeta){
        String path = tempFilePath + fileMeta.getId() + "." + fileMeta.getExt();
        return minioUtil.getFileFromMinio(fileMeta.getStore_key(), path);
    }
}
