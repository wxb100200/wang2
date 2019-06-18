package com.base.wang.util;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 */
public class FileUtil {
    /**
     * 上传单个文件
     */
    public static File uploadSingle(CommonsMultipartFile partFile,String path) throws IOException {
        String filename = partFile.getOriginalFilename();
        File file = new File(path+"/"+filename);
        InputStream inputStream = partFile.getInputStream();
        FileUtils.copyInputStreamToFile(inputStream, file);
        inputStream.close();
        return file;
    }
    /**
     * 上传多个文件
     */
    public  static void uploadMany(CommonsMultipartFile[] partFiles,String path) throws IOException {
        InputStream inputStream =null;
        for (CommonsMultipartFile partFile : partFiles) {
            String filename = partFile.getOriginalFilename();
            if(filename==null || filename.equals("")) continue;
            File file = new File(path + "/" + filename);
            inputStream = partFile.getInputStream();
            FileUtils.copyInputStreamToFile(inputStream, file);
        }
        if(inputStream!=null){
            inputStream.close();
        }
    }

    /**
     * 文件单个下载
     */
    public  static void downSingle(HttpServletResponse response,String path) throws IOException {
        File file=new File(path);
        String name=file.getName();
        response.addHeader("content-disposition", "attachment;filename="+name);
        FileUtils.copyFile(file, response.getOutputStream());
    }
    /**
     * 下载多个文件
     */
    public  static void downMany(HttpServletResponse response,String path) throws IOException {
        File file = new File(path);
        File[] files = file.listFiles();
        File zipFile =new File("test.zip");
        if(!zipFile.exists()){
            zipFile.createNewFile();
        }
        String zipName = zipFile.getName();
        response.addHeader("Content-Disposition", "attachment;filename="+zipName);
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(zipFile));
        BufferedInputStream in  =null;
        for(File f:files){
            String name=f.getName();
            ZipEntry zipEntry = new ZipEntry(name);
            zip.putNextEntry(zipEntry);
            in = new BufferedInputStream(new FileInputStream(f));
            int len = 0;
            byte [] btyes = new byte[1024*4];
            while((len=in.read(btyes))!=-1){
                zip.write(btyes, 0, len);
            }
        }
        zip.flush();
        zip.close();
        if(in!=null) {
            in.close();
        }
        FileUtils.copyFile(zipFile, response.getOutputStream());
        if(zipFile.exists()){
            zipFile.delete();
        }

    }

    /**
     * 获得文件名的后缀
     */
    public static String getFileExtName(File file) {
        String fileName=file.getName();
        if(!fileName.contains(".")){
            return null;
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 下载文件
     * @param request
     * @param response
     * @param file 文件
     * @param name 下载文件的文件名称
     * @throws IOException
     */
    public static void copyFile(HttpServletRequest request, HttpServletResponse response,File file,String name) throws IOException {
        String userAgent = request.getHeader("user-agent").toLowerCase();
        String downloadFileName;
        if (userAgent.contains("msie") || userAgent.contains("like gecko") ) {
            // win10 ie edge 浏览器 和其他系统的ie
            downloadFileName = URLEncoder.encode(name, "UTF-8");
        } else {
            // fe
            downloadFileName = new String(name.getBytes("UTF-8"), "iso-8859-1");
        }
        response.addHeader("content-disposition", "attachment;filename="+downloadFileName);
        FileUtils.copyFile(file, response.getOutputStream());
    }
}
