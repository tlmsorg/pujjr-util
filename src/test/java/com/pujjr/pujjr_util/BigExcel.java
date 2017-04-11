package com.pujjr.pujjr_util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.pujjr.utils.Utils;

public class BigExcel {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		 // 创建基于stream的工作薄对象的
        SXSSFWorkbook wb = new SXSSFWorkbook(1000); // keep 100 rows in memory,
                                                    // exceeding rows will be
                                                    // flushed to disk

        // SXSSFWorkbook wb = new SXSSFWorkbook();
        // wb.setCompressTempFiles(true); // temp files will be gzipped
        long startTime = System.currentTimeMillis();
        Sheet sh = wb.createSheet();
        // 使用createRow将信息写在内存中。
        for (int rownum = 0; rownum < 200; rownum++) {
            Row row = sh.createRow(rownum);
            System.out.println("rownum:"+rownum);
            for (int cellnum = 0; cellnum < 6; cellnum++) {
                Cell cell = row.createCell(cellnum);
                String address = new CellReference(cell).formatAsString();
                //写入日期
                Short dateFormat = wb.createDataFormat().getFormat("yyyy-MM-dd hh:mm:ss");
                CellStyle cellStyle = wb.createCellStyle();
                cellStyle.setDataFormat(dateFormat);
                cell.setCellValue(Utils.formateString2Date("2015-01-15 12:24:44", "yyyy-MM-dd hh:mm:ss"));
                cell.setCellStyle(cellStyle);
               /* 
                DateFormat format;
                SimpleDateFormat datetemp = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                SimpleDateFormat datetemp_test=new SimpleDateFormat("yyyy-MM-dd");
                Date temp1 = datetemp.parse("1900-01-01 00:00");	
                Date temp2=datetemp.parse("1994-01-01 12:00");
                double numOfDateTime=0;
                numOfDateTime=temp2.getTime()-temp1.getTime();
                numOfDateTime=numOfDateTime/24/60/60/1000+1;
                cell = row.createCell((short)0);
                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue(numOfDateTime); 
                cell.setCellStyle(cellStyle);
                cell.setCellType(CellType.NUMERIC);*/
            }

        }
        
        // Rows with rownum < 900 are flushed and not accessible
        // 当使用getRow方法访问的时候，将内存中的信息刷新到硬盘中去。
        for (int rownum = 0; rownum < 900; rownum++) {
            System.out.println(sh.getRow(rownum));
        }

        // ther last 100 rows are still in memory
        for (int rownum = 900; rownum < 1000; rownum++) {
            System.out.println(sh.getRow(rownum));
        }
        // 写入文件中
        FileOutputStream out = new FileOutputStream("D://sxssf.xlsx");
        wb.write(out);
        // 关闭文件流对象
        out.close();
        System.out.println("基于流写入执行完毕!");
        long endTime = System.currentTimeMillis();
        long dueTime = (endTime - startTime)/1000;
        System.out.println("耗时："+dueTime);
	}

}
