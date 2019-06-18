package com.base.wang.convert;




import java.util.List;

/**
 * Created by wxb on 2019/1/8.
 */
public class ExcelSheetData {
    //sheet名字
    private String sheetName;
    //表头名字列表
    private List<String> titleList;
    //字段名字列表
    private List<String> filedList;
    //需要转换的数据列表
    private List<ModelConvert> convertList;
    //数据列表
    private List objectList;
    //对关键字所在的行进行样式处理,一般允许为空
    private String styleKey;
    //是否有序号
    private Boolean orderNum=true;

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<String> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<String> titleList) {
        this.titleList = titleList;
    }

    public List<String> getFiledList() {
        return filedList;
    }

    public void setFiledList(List<String> filedList) {
        this.filedList = filedList;
    }

    public List<ModelConvert> getConvertList() {
        return convertList;
    }

    public void setConvertList(List<ModelConvert> convertList) {
        this.convertList = convertList;
    }

    public List getObjectList() {
        return objectList;
    }

    public void setObjectList(List objectList) {
        this.objectList = objectList;
    }

    public String getStyleKey() {
        return styleKey;
    }

    public void setStyleKey(String styleKey) {
        this.styleKey = styleKey;
    }

    public Boolean getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Boolean orderNum) {
        this.orderNum = orderNum;
    }
}
