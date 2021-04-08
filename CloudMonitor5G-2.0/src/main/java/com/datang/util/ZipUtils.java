package com.datang.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtils {
	public static void main(String[] args) {
		try {
			unZip("E:/AMSUpload/machineUpload/设备资源巡查系统.zip","E:/AMSUpload/machineUpload");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("完成了");
	}
	
	/**
	 * 保存文件
	 * @author maxuancheng
	 * date:2020年6月23日 下午4:31:23
	 * @param file
	 * @param destUrl 保存路径
	 */
	public static void saveFile(File file,String destUrl){
		InputStream is;
		
		//如果文件夹不存在则创建
		File fileMk = new File(destUrl);
		if(!fileMk.exists()){
			fileMk.mkdirs();
		}
		
		File destFile=new File(destUrl,file.getName());

		OutputStream os;
		try {
			is=new FileInputStream(file);
			os = new FileOutputStream(destFile);
			byte[] buffer=new byte[400];

			int length=0;

			while((length=is.read(buffer))>0){
				os.write(buffer,0,length);
			}

			is.close();

			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 /**
     * 解压Zip文件
     * 
     * @param srcZipFile zip文件路径
     * @param destDir 解压后文件保存路径
     * @throws IOException
     */
	public static void unZip(String srcZipFile, String destDir) throws IOException
    {
        ZipFile zipFile = new ZipFile(srcZipFile,Charset.forName("GBK"));
        unZip(zipFile, destDir);
        zipFile.close();//删除文件之前必须停止zipfile
        new File(srcZipFile).delete();//删除文件夹
    }
	
	 /**
     * 解压Zip文件
     * 
     * @param zipFile zip文件
     * @param destDir 解压后文件保存路径
     * @throws IOException
     */
    public static void unZip(ZipFile zipFile, String destDir) throws IOException
    {
    	//如果文件夹不存在则创建
		File fileMk = new File(destDir);
		if(!fileMk.exists()){
			fileMk.mkdirs();
		}
    			
        Enumeration<? extends ZipEntry> entryEnum = zipFile.entries();
        ZipEntry entry = null;
        while (entryEnum.hasMoreElements()) {
            entry = entryEnum.nextElement();
            File destFile = new File(destDir +"/"+ entry.getName());
            if (entry.isDirectory()) {
                destFile.mkdirs();
            }
            else {
                destFile.getParentFile().mkdirs();
                InputStream eis = zipFile.getInputStream(entry);
                //System.out.println(eis.read());
                write(eis, destFile);
            }
        }
    }
    
    /**
     * 将输入流中的数据写到指定文件
     * 
     * @param inputStream
     * @param destFile
     */
    public static void write(InputStream inputStream, File destFile) throws IOException
    {
        BufferedInputStream bufIs = null;
        BufferedOutputStream bufOs = null;
        try {
            bufIs = new BufferedInputStream(inputStream);
            bufOs = new BufferedOutputStream(new FileOutputStream(destFile));
            byte[] buf = new byte[2048];
            int len = 0;
            while ((len = bufIs.read(buf, 0, buf.length)) > 0) {
                bufOs.write(buf, 0, len);
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            close(bufOs, bufIs);
        }
    }
    
    /**
     * 安全关闭多个流
     * 
     * @param streams
     */
    public static void close(Closeable... streams)
    {
        try {
            for (Closeable s : streams) {
                if (s != null)
                    s.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }
    }
}
