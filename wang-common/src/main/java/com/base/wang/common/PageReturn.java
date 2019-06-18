package com.base.wang.common;


import java.io.Serializable;

/**
 * 请求返回的类
 */
public class PageReturn implements Serializable {
    /**
     * 返回结果
     */
    private boolean result;
    /**
     * 编码
     */
    private String messageCode;
    /**
     * 内容
     */
    private String messageText;
    /**
     * 数据
     */
    private Object data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static PageReturn success(){
        return multiple(true,"200","成功",null);
    }
    public static PageReturn success(String messageText){
        return multiple(true,"200",messageText,null);
    }
    public static PageReturn success(String messageCode, String messageText){
        return multiple(true,messageCode,messageText,null);
    }
    public static PageReturn successData(Object obj){
        return multiple(true,"200","成功",obj);
    }
    public static PageReturn fail(){
        return multiple(false,"500","失败",null);
    }
    public static PageReturn fail(String messageText){
        return multiple(false,"500",messageText,null);
    }
    public static PageReturn fail(String messageCode, String messageText){
        return multiple(false,messageCode,messageText,null);
    }
    private static PageReturn multiple(Boolean result, String messageCode, String messageText, Object data){
        PageReturn pageReturn=new PageReturn();
        pageReturn.result=result;
        pageReturn.messageCode=messageCode;
        pageReturn.messageText=messageText;
        pageReturn.data=data;
        return pageReturn;
    }
}
