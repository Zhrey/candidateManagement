package com.ray.core.api.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 转换和打开PDF文件
 */
@Slf4j
public class CommandExecute {
    /**
     * word转换PDF文件
     *
     * @param docxPath
     * @param targetPath
     * @return
     * @throws IOException
     */
    public static boolean wordConverterToPdf(String command, String docxPath, String targetPath) throws Exception {
        File file = new File(docxPath);
        try {
            String result = CommandExecute.executeCommand(command + " " + targetPath + " " + docxPath);
            log.info("==执行转换命令:" + command + " " + targetPath + " " + docxPath);
            log.info("执行转换命令返回值result:{}", result);
            if (result.equals("") || result.contains("writer_pdf_Export")) {
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        return false;
    }

    /**
     * 执行转换功能
     *
     * @param command
     * @return
     */
    public static String executeCommand(String command) {
        StringBuffer output = new StringBuffer();
        Process p;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;

        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            inputStreamReader = new InputStreamReader(p.getInputStream(), "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(inputStreamReader);
        }

        System.out.println(output.toString());
        return output.toString();

    }

    /**
     * 打开PDF文件
     *
     * @param response
     * @param targetFile
     * @throws IOException
     */
    public static void findPdf(HttpServletResponse response, String targetFile) throws IOException {
        try {
            response.setContentType("application/pdf");
            response.setCharacterEncoding("utf-8");
            FileInputStream in = new FileInputStream(new File(targetFile));
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[512];
            while ((in.read(b)) != -1) {
                out.write(b);
            }
            out.flush();
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
