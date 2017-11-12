package com.bi.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Project: oneData
 * Description:${DESCRIPTION}
 * Created at: 2017/7/20
 * Created by xianggu.
 *
 * @author xianggu
 */
public class CSVUtils {

    /**
     * 生成为CVS文件
     *
     * @param exportData 源数据List
     * @param map        csv文件的列表头map
     * @param outPutPath 文件路径
     * @param fileName   文件名称
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static File createCSVFile(List exportData, LinkedHashMap map, String outPutPath,
                                     String fileName) throws Exception {
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            File file = new File(outPutPath);
            if (!file.exists()) {
                file.mkdir();
            }
            //定义文件名格式并创建
            csvFile = File.createTempFile(fileName, ".csv", new File(outPutPath));
            System.out.println("csvFile：" + csvFile);
            // UTF-8使正确读取分隔符"," 
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    csvFile), "UTF-8"), 1024);
            System.out.println("csvFileOutputStream：" + csvFileOutputStream);
            // 写入文件头部 
            for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext(); ) {
                Map.Entry propertyEntry = (Map.Entry) propertyIterator.next();
                csvFileOutputStream.write('"' + (String) propertyEntry.getValue() != null ? (String) propertyEntry.getValue() : "" + '"');
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.newLine();
            // 写入文件内容 
            for (Iterator iterator = exportData.iterator(); iterator.hasNext(); ) {
                Object row = (Object) iterator.next();
                for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext(); ) {
                    Map.Entry propertyEntry = (Map.Entry) propertyIterator.next();
                    csvFileOutputStream.write((String) BeanUtils.getProperty(row, (String) propertyEntry.getKey()));
                    if (propertyIterator.hasNext()) {
                        csvFileOutputStream.write(",");
                    }
                }
                if (iterator.hasNext()) {
                    csvFileOutputStream.newLine();
                }
            }
            csvFileOutputStream.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return csvFile;
    }

    /**
     * 下载文件
     *
     * @param response
     * @param csvFilePath 文件路径
     * @param fileName    文件名称
     * @throws IOException
     */
    public static void exportFile(HttpServletResponse response, String csvFilePath, String fileName)
            throws Exception {
        response.setContentType("application/csv;charset=UTF-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

        InputStream in = null;
        try {
            in = new FileInputStream(csvFilePath);
            int len = 0;
            byte[] buffer = new byte[1024];
            response.setCharacterEncoding("UTF-8");
            OutputStream out = response.getOutputStream();
            while ((len = in.read(buffer)) > 0) {
                out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
                out.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 导出文件
     *
     * @param response
     * @param exportData 导出数据
     * @param map        csv文件的列表头map
     * @param fileName   导出文件名
     * @throws Exception
     */
    public static void exportFile(HttpServletResponse response,
                                  List exportData, Map map, String fileName) throws Exception {

        BufferedWriter outputStream = null;
        try {
            OutputStream out = response.getOutputStream();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/csv;charset=UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}); //解决execl打开乱码，需要输出的内容加上BOM标识
            OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
            outputStream = new BufferedWriter(osw);
            for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext(); ) {
                Map.Entry propertyEntry = (Map.Entry) propertyIterator.next();
                outputStream.write('"' + (String) propertyEntry.getValue() != null ? (String) propertyEntry.getValue() : "" + '"');
                if (propertyIterator.hasNext()) {
                    outputStream.write(",");
                }
            }
            outputStream.newLine();

            for (Iterator iterator = exportData.iterator(); iterator.hasNext(); ) {
                Object row = (Object) iterator.next();
                for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext(); ) {
                    Map.Entry propertyEntry = (Map.Entry) propertyIterator.next();
                    String value = BeanUtils.getProperty(row, (String) propertyEntry.getKey());
                    if (value != null) {
                        outputStream.write(value);
                    } else {
                        outputStream.write("");
                    }
                    if (propertyIterator.hasNext()) {
                        outputStream.write(",");
                    }
                }
                if (iterator.hasNext()) {
                    outputStream.newLine();
                }
            }
            outputStream.flush();
        } catch (Exception e) {
            new RRException("导出csv异常", e);
        }
    }

    public static void exportFile(HttpServletResponse response, String fileName,
                                  List<String> header, List<List<Object>> body) {
        BufferedWriter outputStream = null;
        try {
            OutputStream out = response.getOutputStream();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/csv;charset=UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}); //解决execl打开乱码，需要输出的内容加上BOM标识
            OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
            outputStream = new BufferedWriter(osw);

            int i = 0;
            for (String s : header) {
                outputStream.write('"' + s != null ? s : "" + '"');
                if (++i < header.size()) {
                    outputStream.write(",");
                }
            }
            outputStream.newLine();

            for (int i1 = 0; i1 < body.size(); i1++) {
                List<Object> row = body.get(i1);
                for (int i2 = 0; i2 < row.size(); i2++) {
                    String field = String.valueOf(row.get(i2));
                    outputStream.write('"' + field != null ? field : "" + '"');
                    if ((i2 + 1) < row.size()) {
                        outputStream.write(",");
                    }
                }
                if ((i1 + 1) < body.size()) {
                    outputStream.newLine();
                }
            }

            outputStream.flush();
        } catch (Exception e) {
            new RRException("导出csv异常", e);
        }
    }

    /**
     * 删除该目录filePath下的所有文件
     *
     * @param filePath 文件目录路径
     */
    public static void deleteFiles(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    files[i].delete();
                }
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param filePath 文件目录路径
     * @param fileName 文件名称
     */
    public static void deleteFile(String filePath, String fileName) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    if (files[i].getName().equals(fileName)) {
                        files[i].delete();
                        return;
                    }
                }
            }
        }
    }
}
