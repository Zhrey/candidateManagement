package com.ray.core.api.utils;

import com.ray.cloud.framework.base.dto.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class ImportExecl {


    /**
     * 总行数
     */
    private int totalRows = 0;
    /**
     * 总列数
     */
    private int totalCells = 0;
    /**
     * 错误信息
     */
    private String errorInfo;

    /**
     * 构造方法
     */
    public ImportExecl() {
    }


    public int getTotalRows() {
        return totalRows;
    }

    public int getTotalCells() {
        return totalCells;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public boolean validateExcel(File file, String filePath) {
        /** 检查文件名是否为空或者是否是Excel格式的文件 */
        if (filePath == null
                || !(WDWUtil.isExcel2003(filePath) || WDWUtil
                .isExcel2007(filePath))) {
            errorInfo = "文件名不是excel格式";
            return false;
        }
        /** 检查文件是否存在 */
        if (file == null || !file.exists()) {
            errorInfo = "文件不存在";
            return false;
        }
        return true;
    }

    public List<List<String>> read(File file, String filePath) {
        List<List<String>> dataLst = new ArrayList<List<String>>();
        InputStream is = null;
        try {
            /** 验证文件是否合法 */
            if (!validateExcel(file, filePath)) {
                System.out.println(errorInfo);
                return null;
            }
            /** 判断文件的类型，是2003还是2007 */
            boolean isExcel2003 = true;
            if (WDWUtil.isExcel2007(filePath)) {
                isExcel2003 = false;
            }
            /** 调用本类提供的根据流读取的方法 */
            is = new FileInputStream(file);
            dataLst = read(is, isExcel2003);
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }
        /** 返回最后读取的结果 */
        return dataLst;
    }


    public List<List<String>> read(InputStream inputStream, boolean isExcel2003) {
        List<List<String>> dataLst = null;
        try {
            /** 根据版本选择创建Workbook的方式 */
            Workbook wb = null;
            if (isExcel2003) {
                wb = new HSSFWorkbook(inputStream);
            } else {
                wb = new XSSFWorkbook(inputStream);
            }
            dataLst = read(wb,0);
            dataLst.addAll(read(wb,1));
        } catch (IOException e) {

            e.printStackTrace();
        }
        return dataLst;
    }


    private List<List<String>> read(Workbook wb,int index) {
        List<List<String>> dataLst = new ArrayList<List<String>>();
        /** 得到第一个shell */
        Sheet sheet = wb.getSheetAt(index);
        /** 得到Excel的行数 */
        this.totalRows = sheet.getPhysicalNumberOfRows();
        /** 得到Excel的列数 */
        if (this.totalRows >= 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        /** 循环Excel的行 */
        for (int r = 0; r < this.totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            List<String> rowLst = new ArrayList<String>();
            /** 循环Excel的列 */
            for (int c = 0; c < this.getTotalCells(); c++) {
                Cell cell = row.getCell(c);
                String cellValue = "";
                if (null != cell) {
                    switch (cell.getCellType()) {
                        case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                            // 以下是判断数据的类型
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                Date d = cell.getDateCellValue();
                                DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                                cellValue = formater.format(d);
                            } else {
                                DecimalFormat df = new DecimalFormat("0");
                                cellValue = df.format(cell.getNumericCellValue());
                            }
                            break;
                        case HSSFCell.CELL_TYPE_STRING: // 字符串
                            cellValue = cell.getStringCellValue();
                            break;
                        case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                            cellValue = cell.getBooleanCellValue() + "";
                            break;
                        case HSSFCell.CELL_TYPE_FORMULA: // 公式
                            cellValue = cell.getCellFormula() + "";
                            break;
                        case HSSFCell.CELL_TYPE_BLANK: // 空值
                            cellValue = "";
                            break;
                        case HSSFCell.CELL_TYPE_ERROR: // 故障
                            cellValue = "非法字符";
                            break;
                        default:
                            cellValue = "未知类型";
                            break;
                    }
                }
                rowLst.add(cellValue);
            }
            /** 保存第r行的第c列 */
            dataLst.add(rowLst);
        }
        return dataLst;
    }

    /**
     * 读取Excel返回客商信息
     *
     * @param companyInfoStringList
     * @return
     */
    public ResultDTO readModelToCompany(List<List<String>> companyInfoStringList) {

        ResultDTO resultDTO = ResultDTO.success();

        if (companyInfoStringList != null) {
            Field[] fields = resultDTO.getClass().getDeclaredFields();
            //"第" + (i) + "行"
            int i = 0;
            //跳过第一行
            if (companyInfoStringList.size() > 0) {
                companyInfoStringList.remove(0);
            } else {
                log.error("读取excel时没有读取到数据！");
                return null;
            }
            for (List<String> cellList : companyInfoStringList) {

                //"第" + 2 + "列"
                if (StringUtils.isNotEmpty(cellList.get(2))) {
                    try {
                        fields[i].setAccessible(true);
                        if (fields[i].getType() == Integer.class) {
//                            fields[i].set(resultDTO, bizmap.get(WDWUtil.ran2fuc(cellList.get(2))));
                        } else if (fields[i].getType() == BigDecimal.class) {
//                            fields[i].set(resultDTO, new BigDecimal(cellList.get(2)));
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("赋值错误：" + e.getMessage(), e);
                        return null;
                    }
                }
                i++;
            }
        } else {
            log.error("companyInfoStringList：null");
        }
        return null;
    }

}