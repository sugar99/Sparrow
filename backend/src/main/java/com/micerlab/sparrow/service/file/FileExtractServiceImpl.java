package com.micerlab.sparrow.service.file;

import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;
import com.micerlab.sparrow.utils.FileExtractUtil;
import com.micerlab.sparrow.utils.NlpUtil;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description TODO
 * @Author Honda
 * @Date 2019/7/12 16:14
 **/
public class FileExtractServiceImpl implements FileExtractService {

    private static final Logger logger = LoggerFactory.getLogger(FileExtractServiceImpl.class);

    /**
     * 项目选择保留的词性
     */
    private static Set<String> keep = new HashSet<>();

    private static Set<String> garbage = new HashSet<>();

    static {
        // 保留
        //  natures.add(Nature.nr.toString()); // 人名
        keep.add(Nature.nrf.toString());
        keep.add(Nature.ns.toString()); // 地名
        keep.add(Nature.nsf.toString());
        keep.add(Nature.nt.toString()); // 机构
        keep.add(Nature.ntc.toString()); // 公司名
        keep.add(Nature.ntcf.toString()); // 工厂名
        keep.add(Nature.nto.toString()); // 政府机构
        keep.add(Nature.ntu.toString()); // 大学
        keep.add(Nature.nts.toString()); // 中小学
        keep.add(Nature.nth.toString()); // 中小学
        keep.add(Nature.nis.toString()); // 机构后缀
        // natures.add(Nature.nz.toString()); // 其它专业名词

        // 丢弃
        garbage.add(Nature.v.toString());
    }

    @Override
    /**
     * 将一个文件（pdf, doc, ...）找出其中的关键字等便于进行内容的检索
     * @param path 文件存储位置
     */
    public List<Term> createFileIndex(String path) {
        List<Term> res = null;
        String str;
        // 转化为文本信息
        try {
            str = FileExtractUtil.extractString(path);
        } catch (Exception e) {
            throw new RuntimeException("提取文本信息失败");
        }
        if (str != null) {
            // 提取关键字
            List<String> keyword = NlpUtil.findKeyword(str);
            // 识别关键字并去掉一些词汇
            res = NlpUtil.NER(keyword);
            res = NlpUtil.removeNatures(res, garbage);
            // NER
            List<Term> ner = NlpUtil.NER(str);
            // 选取一些特定的词（人名 / 地点 / 机构 ...）
            ner = NlpUtil.keepNatures(ner, keep);
            // 融合
            res.addAll(ner);
        }
        return res;
    }
}
