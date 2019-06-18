package com.base.wang.util;

import com.base.wang.convert.DefaultConvert;
import com.base.wang.convert.ExcelSheetData;
import com.base.wang.convert.ModelConvert;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel工具类
 */
public class ExcelUtil {
    //导出只有一个sheet的Excel
    public static File export(String sheetName, List<String> titleList, List<String> filedList,
                              List<ModelConvert> convertList, List objectList){
        return export(sheetName, titleList, filedList, convertList, objectList,null,false);
    }
    public static File export(String sheetName, List<String> titleList, List<String> filedList,
                              List<ModelConvert> convertList, List objectList,String styleKey,Boolean orderNum){
        ExcelSheetData data=new ExcelSheetData();
        data.setSheetName(sheetName);
        data.setTitleList(titleList);
        data.setFiledList(filedList);
        data.setConvertList(convertList);
        data.setObjectList(objectList);
        data.setStyleKey(styleKey);
        data.setOrderNum(orderNum);
        List<ExcelSheetData> sheetList=new ArrayList<>();
        sheetList.add(data);
        return exportManySheet(sheetList);
    }
    //导出有多个sheet的Excel
    //注意：多个sheet的时候，sheetName不能相同否则会报错
    public static File exportManySheet(List<ExcelSheetData> sheetList){
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        for(ExcelSheetData data:sheetList){
            String sheetName=data.getSheetName();
            List<String> titleList=data.getTitleList();
            List<String> filedList=data.getFiledList();
            List<ModelConvert> convertList=data.getConvertList();
            List objectList=data.getObjectList();
            String styleKey=data.getStyleKey();
            Boolean orderNum=data.getOrderNum();
            try {
                sheetDataHandle(workbook, sheetName,titleList,filedList,convertList,objectList,styleKey,orderNum);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            File tmpFile=File.createTempFile("temp",".xls");
            tmpFile.deleteOnExit();//程序退出时删除临时文件
            FileOutputStream fos=new FileOutputStream(tmpFile);
            workbook.write(fos);
            workbook.close();
            fos.close();
            return tmpFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * sheet数据处理
     * @param workbook 工作薄
     * @param sheetName sheet的名字
     * @param titleList 表头名称列表
     * @param filedList 字段名称列表
     * @param convertList 需要转换的数据列表
     * @param objectList 数据列表
     */
    public static void sheetDataHandle(HSSFWorkbook workbook, String sheetName,
                                       List<String> titleList,
                                       List<String> filedList,
                                       List<ModelConvert> convertList,
                                       List objectList, String styleKey, Boolean orderNumber) throws Exception {
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(sheetName);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);
        try {
            if(orderNumber){
                //产生序号生成表格
                sheetOrderNum( workbook, sheet,titleList,filedList, convertList, objectList);
            }else {
                //不用产生序号生成表格
                sheetNotOrderNum( workbook, sheet,titleList,filedList, convertList, objectList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        keyRowHandle(workbook,sheet,styleKey);//对关键字所在的行进行样式处理
    }
    //不用产生序号生成表格
    private static void sheetNotOrderNum(HSSFWorkbook workbook, HSSFSheet sheet, List<String> titleList,
                                         List<String> filedList, List<ModelConvert> convertList, List objectList) throws Exception {
        // 生成一个样式
        HSSFCellStyle styleHead = generateStyle(workbook, HSSFColor.SKY_BLUE.index, HSSFColor.VIOLET.index,(short) 12, HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle styleData1= generateStyle(workbook, HSSFColor.WHITE.index,(short) 0,(short) 0, HSSFFont.BOLDWEIGHT_NORMAL);
        HSSFCellStyle styleData2= generateStyle(workbook, HSSFColor.LIGHT_TURQUOISE.index, HSSFColor.BLACK.index,(short) 0, HSSFFont.BOLDWEIGHT_NORMAL);
        //产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for(int i=0;i<titleList.size();i++){
            cellHandle(row,i,styleHead,titleList.get(i));
        }
        //把每个单元格赋值
        for(int i=0;i<objectList.size();i++){
            Object obj=objectList.get(i);
            HSSFRow rowData = sheet.createRow(i+1);
            HSSFCellStyle styleData;
            if(i%2==1){
                styleData=styleData1;
            }else {
                styleData=styleData2;
            }
            for(int j=0;j<filedList.size();j++){
                String fileName=filedList.get(j);
                Object convertObj = convertList.get(j);
                Object data = dataHandle(obj,fileName,convertObj);
                cellHandle(rowData,j,styleData,data);

            }
        }
    }
    //产生序号生成表格
    private static void sheetOrderNum(HSSFWorkbook workbook, HSSFSheet sheet, List<String> titleList,
                                      List<String> filedList, List<ModelConvert> convertList, List objectList) throws Exception {
        // 生成一个样式
        HSSFCellStyle styleHead = generateStyle(workbook, HSSFColor.SKY_BLUE.index, HSSFColor.VIOLET.index,(short) 12, HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle styleData1= generateStyle(workbook, HSSFColor.WHITE.index,(short) 0,(short) 0, HSSFFont.BOLDWEIGHT_NORMAL);
        HSSFCellStyle styleData2= generateStyle(workbook, HSSFColor.LIGHT_TURQUOISE.index, HSSFColor.BLACK.index,(short) 0, HSSFFont.BOLDWEIGHT_NORMAL);
        //产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for(int i=0;i<titleList.size()+1;i++){
            if(i==0){
                cellHandle(row,i,styleHead,"序号");
                continue;
            }
            cellHandle(row,i,styleHead,titleList.get(i-1));
        }
        //把每个单元格赋值
        for(int i=0;i<objectList.size();i++){
            Object obj=objectList.get(i);
            HSSFRow rowData = sheet.createRow(i+1);
            HSSFCellStyle styleData;
            if(i%2==1){
                styleData=styleData1;
            }else {
                styleData=styleData2;
            }
            for(int j=0;j<filedList.size()+1;j++){
                if(j==0){
                    cellHandle(rowData,j,styleData,i+1);
                    continue;
                }
                String fileName=filedList.get(j-1);
                Object convertObj = convertList.get(j-1);
                Object data = dataHandle(obj,fileName,convertObj);
                cellHandle(rowData,j,styleData,data);

            }
        }
    }
    /**
     * 对关键字所在的行进行样式处理
     */
    private  static void keyRowHandle(HSSFWorkbook workbook, HSSFSheet sheet, String styleKey){
        if(styleKey==null || styleKey.trim().equals(""))return;
        HSSFCellStyle style = generateStyle(workbook, HSSFColor.LIGHT_GREEN.index, HSSFColor.VIOLET.index,(short) 12, HSSFFont.BOLDWEIGHT_BOLD);
        for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
            HSSFRow xssfRow = sheet.getRow(rowNum);
            if(xssfRow==null)continue;
            for(int no=0;no<xssfRow.getLastCellNum();no++){
                HSSFCell cell=xssfRow.getCell(no);
                if(cell==null)continue;
                String str=getValue(cell);
                if(str==null || str.trim().equals(""))continue;
                if(!str.contains(styleKey))continue;
                for(int o=0;o<xssfRow.getLastCellNum();o++){
                    HSSFCell c=xssfRow.getCell(o);
                    c.setCellStyle(style);
                }
            }
        }
    }

    /**
     * data数据处理，有可能需要对数据进行转换
   * @param obj
     * @param name
     * @param convertObj
     * @return
     * @throws Exception
     */
    private static Object dataHandle(Object obj,String name,Object convertObj)throws Exception{
        String methodName="get"+name.substring(0, 1).toUpperCase() + name.substring(1);
        Method getMethod = obj.getClass().getMethod(methodName);//获取get方法
        Object val = getMethod.invoke(obj);

        if (convertObj == null) convertObj = new DefaultConvert();
        ModelConvert c = (ModelConvert) convertObj;
        Object convertVal = c.convert(val);
        return convertVal;
    }
    //单元格处理
    private static HSSFCell cellHandle(HSSFRow row, int column, HSSFCellStyle style, Object obj){
        HSSFCell cell= row.createCell(column);
        cell.setCellStyle(style);
        if(obj==null)return cell;
       if(obj instanceof BigDecimal){
            cell.setCellValue(((BigDecimal) obj).doubleValue());
        }else if(obj instanceof Integer){
            cell.setCellValue(((Integer) obj).doubleValue());
        }else {
           HSSFRichTextString text = new HSSFRichTextString(obj.toString());
           cell.setCellValue(text);
        }
        return cell;
    }
    //生成一个样式
    private static HSSFCellStyle generateStyle(HSSFWorkbook workbook, short foregroundColor, short color, short heightInPoints, short boldweight){
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(foregroundColor);//HSSFColor.SKY_BLUE.index,HSSFColor.WHITE.index,HSSFColor.LIGHT_TURQUOISE.index
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        if(color>0){
            font.setColor(color);//HSSFColor.VIOLET.index,
        }
        if(heightInPoints>0){
            font.setFontHeightInPoints(heightInPoints);//12
        }
        font.setBoldweight(boldweight);//HSSFFont.BOLDWEIGHT_BOLD,HSSFFont.BOLDWEIGHT_NORMAL
        // 把字体应用到当前的样式
        style.setFont(font);
        return style;
    }
    //获取单元格的值
    private static String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            DecimalFormat df = new DecimalFormat("0");
            return df.format(hssfCell.getNumericCellValue());
        } else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }
    //获取单元格的值
    private static String getValue(XSSFCell xssfRow) {
        if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfRow.getBooleanCellValue());
        } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
            DecimalFormat df = new DecimalFormat("0");
            return df.format(xssfRow.getNumericCellValue());
        } else {
            return String.valueOf(xssfRow.getStringCellValue());
        }
    }

    public static List<List> readExcel(InputStream is, String path, Integer cellNumber) {
        List<List> list = new ArrayList<>();
        try {
            if (path.endsWith(".xlsx")) {
                list = readXlsx(is, cellNumber);
            } else if (path.endsWith(".xls")) {
                list = readXls(is, cellNumber);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    //读取excel表格内容(excel2007以上)
    public static List<List> readXlsx(InputStream is, Integer cellNumber) throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        List<List> list = new ArrayList<>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    List<String> rowList = new ArrayList<>();
                    for (int no = 0; no < cellNumber; no++) {
                        XSSFCell cell = xssfRow.getCell(no);
                        rowList.add(getValue(cell));
                    }
                    list.add(rowList);
                }
            }
        }
        return list;
    }

    //读取excel表格内容(excel2003以下)
    public static List readXls(InputStream is, Integer cellNumber) throws IOException {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        List<List> list = new ArrayList<>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    List<String> rowList = new ArrayList<>();
                    for (int no = 0; no < cellNumber; no++) {
                        HSSFCell cell = hssfRow.getCell(0);
                        rowList.add(getValue(cell));
                    }
                }
            }
        }
        return list;
    }
}





















