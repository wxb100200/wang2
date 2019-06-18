package com.base.wang.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * IO工具类。用于处理IO相关的操作。<br/>
 * 特别包含了加载配置文件的操作。
 */
public final class IOUtil {

    public static final String UTF8 = "utf-8";

    public static enum FileType {
        Image(512 * 1024, "512K"), // 512k
        NormalFile(10 * 1024 * 1024, "10M"), // 10M
        LargeFile(20 * 1024 * 1024,"20M")   // 20M
        ;

        public long size;
        public String text;

        private FileType(long size, String text){
            this.size = size;
            this.text = text;
        }
    }

    /**
     * 检查文件大小
     * @param file 文件
     * @param type 文件类型
     * @return true - 大小合适； false - 超过大小限制.
     */
    public static boolean checkFileSize(File file, FileType type){
        return file.length() <= type.size ;
    }

    /**
     * config 设置为默认访问权限，以允许同一个包下面的其它类来修改这个config,方便测试。
     */
    static Properties config = null;
    private static boolean configLoaded = false;
    private final static Integer ConfigLock = new Integer(0);

    private static File tempFolder = null;

    private static Integer LOCK = 0;

     /**
     * 取得系统默认临时目录。
     * @return
     */
    public static File getTempFolder(){
        if(tempFolder==null){
            synchronized(LOCK){
                if(tempFolder==null){
                    try {
                        File tempFile = File.createTempFile("IOUtil", "tmp");
                        tempFolder = tempFile.getParentFile();
                        tempFile.delete();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return tempFolder;
    }

    public static File createTempFile(String fileName){
        return createTempFile("IOUtil", fileName);
    }

    public static File createTempFile(String prefix, String suffix){
        try {
            File f = File.createTempFile(prefix, suffix);
            f.deleteOnExit();
            return f;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 获取配置文件。<br/>
     * 应该在classpath中包含一个config.xml的配置文件。
     */
    public static Properties getConfig() {
        if (config == null && !configLoaded) {
            synchronized (ConfigLock) {
                if (!configLoaded) {
                    try {
                        config = loadPropertiesFromXML("/config.xml");
                    } finally {
                        configLoaded = true;
                    }
                }
            }
        }
        return config;
    }

    /**
     * 读取一个文件。
     */
    public static InputStream openFile(File file) {
        try {
            InputStream is = new FileInputStream(file);
            return is;
        } catch (Exception exp) {
            throw new RuntimeException("openFile error, file: " + file, exp);
        }
    }

    /**
     * 读取一个文件。
     */
    public static InputStream openFile(String name) {
        try {
            return openFile(new File(name));
        } catch (Exception exp) {
            throw new RuntimeException("openFile error, file: " + name, exp);
        }
    }

    /**
     * 读取一个文本文件。返回字符串文本。可以指定字符集。
     */
    public static String readAsText(String fileName, String charsetName) {
        InputStream is = null;
        try {
            is = openFile(fileName);
            return readAsText(is, charsetName);
        } catch (Exception exp) {
            throw new RuntimeException("readTextFile error, file: " + fileName, exp);
        } finally {
            close(is);
        }
    }

    /**
     * 写文本文件。
     * @param fileName 文件名称。
     * @param content  内容。
     */
    public static void writeAsText(String fileName, String content) {
        writeAsText(new File(fileName),content);
    }

    /**
     * 写文本文件。
     * @param file 文件。
     * @param content  内容。
     */
    public static void writeAsText(File file, String content) {
        OutputStreamWriter osw = null;
        try {
            osw =new OutputStreamWriter( new FileOutputStream(file), IOUtil.UTF8 );
            osw.write(content, 0, content.length());
        } catch (Exception exp) {
            throw new RuntimeException("writeTextFile error, file: " + file, exp);
        } finally {
            close(osw);
        }
    }

    /**
     * 写文件.默认Buffer大小为100k。
     * @param file 文件
     * @param is   内容
     */
    public static void write(File file, InputStream is) {
        write(file,is,1024*100);
    }

    /**
     * 写文件。
     * @param file 文件
     * @param b Buffer 大小
     */
    public static void write(File file, byte []b) {
        BufferedOutputStream os = null;
        try {
            os =new BufferedOutputStream( new FileOutputStream(file) );
            os.write(b);
        } catch (Exception exp) {
            throw new RuntimeException("writeFile error, file: " + file, exp);
        } finally {
            close(os);
        }
    }

    /**
     * 写文件。
     * @param file 文件
     * @param is   内容
     * @param bufferSize Buffer 大小
     */
    public static void write(File file, InputStream is, int bufferSize) {
        BufferedOutputStream os = null;
        try {
            os =new BufferedOutputStream( new FileOutputStream(file) );
            byte[] buffer = new byte[bufferSize];
            int len = -1;
            while( (len=is.read(buffer))!=-1){
                os.write(buffer, 0, len);
            }

        } catch (Exception exp) {
            throw new RuntimeException("writeFile error, file: " + file, exp);
        } finally {
            close(os);
        }
    }

    /**
     * 输入输出。将is流中的内容输出到os中去。
     * @param os 输出流。
     * @param is 输入流。
     * @param bufferSize Buffer大小。
     */
    public static void write(OutputStream os, InputStream is, int bufferSize) {
        BufferedOutputStream bos = null;
        try {
            bos =new BufferedOutputStream( os);
            byte[] buffer = new byte[bufferSize];
            int len = -1;
            while( (len=is.read(buffer))!=-1){
                bos.write(buffer, 0, len);
            }
            bos.flush();
        } catch (Exception exp) {
            throw new RuntimeException("write error" , exp);
        } finally {
            close(bos);
        }
    }

    /**
     * 输入输出。将is流中的内容输到os中去。
     * copy 函数，不会自动关闭os，以备后续继续copy，要求调用者自行关闭
     * 采用1M的buffer
     * @param os 输出流。
     * @param is 输入流。
     */
    public static void copy(OutputStream os, InputStream is) {
        copy(os,is,1024*1024);
    }

    /**
     * 输入输出。将is流中的内容输到os中去。
     * copy 函数，不会自动关闭os，以备后续继续copy，要求调用者自行关闭
     * @param os 输出流。
     * @param is 输入流。
     * @param bufferSize Buffer大小。
     */
    public static void copy(OutputStream os, InputStream is, int bufferSize) {
        try {
            byte[] buffer = new byte[bufferSize];
            int len = -1;
            while( (len=is.read(buffer))!=-1){
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (Exception exp) {
            throw new RuntimeException("write error" , exp);
        } finally {
        }
    }

    public static PrintWriter getPrintWriter(OutputStream os, String charset) {
        try {
            return new PrintWriter(new BufferedWriter(new OutputStreamWriter(os, charset)));
        } catch (UnsupportedEncodingException ex) {
            return new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));
        }
    }

    public static PrintWriter getPrintWriter(OutputStream os) {
        return getPrintWriter(os,UTF8);
    }

    /**
     * 读取一个字符串流。返回字符串文本。可以指定字符集。
     * @param charsetName 字符集，可以为null.
     */
    public static String readAsText(InputStream is, String charsetName) {
        try {
            StringBuffer sb = new StringBuffer();
            char[] buffer = new char[10000];
            InputStreamReader isr = null;
            if (charsetName != null) {
                isr = new InputStreamReader(is, charsetName);
            } else {
                isr = new InputStreamReader(is);
            }
            int length;
            while ((length = isr.read(buffer)) != -1) {
                sb.append(buffer, 0, length);
            }
            return sb.toString();
        } catch (Exception exp) {
            throw new RuntimeException("readTextInputStream error!", exp);
        }
    }

    public static byte[] read(File file) {
        return read(openFile(file));
    }

    /*public static String readAsBase64String(InputStream is){
    	byte[] data = read(is);
    	byte[] base64Content = BASE64Codec.encode(data);
    	try {
			return new String(base64Content,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }*/

    /**
     * 读取一个流。返回包含全部内容的byte[]。
     * 因为本方法会读取全部内容到内存中，故此方法只能在已知文件不是很大的情况下使用。
     */
    public static byte[] read(InputStream is) {
        List<byte[]> bufferList = new ArrayList<byte[]>();
        List<Integer> lengthList = new ArrayList<Integer>();
        int total = 0;
        try {
            do {
                byte[] buffer = new byte[10000];
                int length;
                length = is.read(buffer);
                if (length == -1) {
                    break;
                }
                total += length;
                bufferList.add(buffer);
                lengthList.add(length);
            } while (true);
            byte[] result = new byte[total];
            int offset = 0;
            for (int i = 0; i < bufferList.size(); i++) {
                byte[] buf = (byte[]) bufferList.get(i);
                int len = (Integer) lengthList.get(i);
                System.arraycopy(buf, 0, result, offset, len);
                offset += len;
            }
            return result;
        } catch (Exception exp) {
            throw new RuntimeException("readInputStream error!", exp);
        }
    }

    /**
     * 读取某个网页的内容
     * @param url 地址，可以是http/https
     * @param charsetName 字符集，可以为null，使用系统默认字符集
     */
    public static String readURL(String url, String charsetName) {
        try {
            java.net.URL u = new java.net.URL(url);
            return readAsText(u.openStream(), charsetName);
        } catch (Exception exp) {
            throw new RuntimeException("readURL error!", exp);
        }
    }

    /**
     * 从classpath中读取一个name的资源文件。
     */
    public static InputStream getResourceAsStream(String name) {
        try {
            InputStream is = IOUtil.class.getResourceAsStream(name);
            return is;
        } catch (Exception exp) {
            exp.printStackTrace();
            throw new RuntimeException("getResouce error, file: " + name, exp);
        }
    }

    /**
     * 从classpath中读取一个name的属性文件。
     */
    public static Properties loadProperties(String name) {
        Properties p = new Properties();
        try {
            InputStream is = IOUtil.class.getResourceAsStream(name);
            p.load(is);
            return p;
        } catch (Exception exp) {
            exp.printStackTrace();
            throw new RuntimeException("Load Properties file error: " + name, exp);
        }
    }

    /**
     * 从classpath中读取一个name的XML格式的属性文件。
     */
    public static Properties loadPropertiesFromXML(String name) {
        Properties p = new Properties();
        try {
            InputStream is = IOUtil.class.getResourceAsStream(name);
            p.loadFromXML(is);
            return p;
        } catch (Exception exp) {
            exp.printStackTrace();
            throw new RuntimeException("Load Properties file error: " + name, exp);
        }
    }

    public static void close(Closeable resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            // System.out.println(readURL(args[0], null));
        }
    }
    /**
     * 把字节数组保存为一个文件
     * @EditTime 2007-8-13 上午11:45:56
     */
    public static File writeAsBytes(byte[] b, String outputFile){
        BufferedOutputStream stream = null;
        File file = null;
        try{
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e){
            e.printStackTrace();
        } finally{
            if (stream != null){
                try{
                    stream.close();
                } catch (IOException e1){
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    public static String getErrorStackTrace(Throwable th){
        StringWriter sw = new StringWriter();
        th.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public static InputStream zipFile(String[] filePath,String[] fileName) throws Exception {
        if(null == filePath || null == fileName || filePath.length != fileName.length){
            throw new RuntimeException("filePath and fileName not be null,filePath.length unequ");
        }
        int bufferLength = 1024 * 1024;
        byte[] buffer = new byte[bufferLength];
        int length;

        File zipFile = File.createTempFile("zip",".zip");
        System.out.println(zipFile.length());

        FileOutputStream out = new FileOutputStream(zipFile);
        ZipOutputStream zipOut = new ZipOutputStream(out, Charset.forName("gb2312"));
        File file;
        InputStream in;
        for(int i = 0; i< fileName.length; i++){
            zipOut.putNextEntry(new ZipEntry(fileName[i]));
            file = new File(filePath[i]);
            if(!file.exists()){
                throw new RuntimeException("file "+ filePath[i] +"not exists");
            }
            in = new FileInputStream(file);
            while((length = in.read(buffer,0,bufferLength)) > -1){
                zipOut.write(buffer,0,length);
            }
            zipOut.flush();
        }
        zipOut.close();
        System.out.println(zipFile.length());
        return new FileInputStream(zipFile);
    }

}
