package com.ray.core.api.service.impl;

import com.ray.cloud.framework.base.dto.ResultDTO;
import com.ray.cloud.framework.file.sdk.service.FileSdkService;
import com.ray.core.api.service.ResumeService;
import com.ray.core.api.utils.CommandExecute;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by ZhangRui on 2018/8/5.
 */
@Service
@Slf4j
public class ResumeServiceImpl implements ResumeService {

    private final static String LIBRE_COM_WIN = "soffice --convert-to pdf -outdir";
    private final static String LIBRE_COM_LUX = "libreoffice --convert-to pdf -outdir";

    @Autowired
    private FileSdkService fileSdkService;

    public static void main(String[] ar) {

        File input = new File("F:\\resume\\智联招聘_尹胜男_中文_20180807_1533623614974.pdf");
        PDDocument pd = null;
        try {
            pd = PDDocument.load(input);
            PDFTextStripper stripper = new PDFTextStripper();
            System.out.println(stripper.getText(pd));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResultDTO downloadFile(String filePath, String fileName){

        return fileSdkService.downloadFile(filePath,fileName);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResultDTO uploadFile(File file) {

        FileInputStream fileInputStream = null;
        try {

//            if (!file.exists()) {
//                throw new RuntimeException("目标文件不存在！");
//            }
            fileInputStream = new FileInputStream(file);

            String osName = System.getProperty("os.name");
            String command = "";
            if (osName.contains("Windows")) {
                command = LIBRE_COM_WIN;
            } else {
                command = LIBRE_COM_LUX;
            }
            boolean convertStatus = CommandExecute.wordConverterToPdf(command,
                    "F:\\resume\\temp.doc" , file.getAbsolutePath());
            System.out.println(convertStatus);
//            WordExtractor ex = new WordExtractor(fileInputStream);
//            HWPFDocument doc = new HWPFDocument(fileInputStream);

            //通过 Doc对象直接获取Text
//            StringBuilder sb = doc.getText();

            //通过Range对象获取Text
//            Range range = doc.getRange();
//            String text = range.text();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
//        ResultDTO resultDTO = fileSdkService.uploadFile(file);
        return ResultDTO.success();
    }
}
