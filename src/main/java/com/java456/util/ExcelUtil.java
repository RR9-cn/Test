package com.java456.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class ExcelUtil {
	
	public static void main(String[] args) throws IOException {
	}

	
	/**
	 * 格式化单元格返回其内容 格式化成string返回。
	 * 
	 * @param cell
	 * @return
	 */
	public static String formatCell(XSSFCell cell) {
		if (cell == null) {
			return "";
		} else {
			if (cell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
				return String.valueOf(cell.getBooleanCellValue());
			} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
				//将科学计数法格式化
				DecimalFormat df = new DecimalFormat("#");
				String value = df.format(cell.getNumericCellValue());
				return value;
			} else {
				return String.valueOf(cell.getStringCellValue());
			}
		}
	}
	
	public static String formatCell(HSSFCell cell) {
		if (cell == null) {
			return "";
		} else {
			if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
				return String.valueOf(cell.getBooleanCellValue());
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				//将科学计数法格式化
				DecimalFormat df = new DecimalFormat("#");
				String value = df.format(cell.getNumericCellValue());
				return value;
			} else {
				return String.valueOf(cell.getStringCellValue());
			}
		}
	}
	
	
	/**
	 * 
	 * 返回 日期  date  2018/3/28 14:18:00  这种类型的可以。
	 */
	
	public static Date formatDate(XSSFCell cell) throws ParseException{
		
		
		String str=null;
		
		Date date = null;
		LocalDateTime localTime=LocalDateTime.now();
		if(cell.toString().isEmpty()) {
			str=localTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			date  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
			
		}		
		else if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
			date  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			date = cell.getDateCellValue();
		}
		return date;
	}
	
	public static Date formatDate(HSSFCell cell) throws ParseException{
		
		String str = cell.toString();
		
		Date date = null;
		LocalDateTime localTime=LocalDateTime.now();
		if(str.isEmpty()) {
			str=localTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			date  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
			
		}		
		else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			date  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			date = cell.getDateCellValue();
		}
		return date;
	}
	
	
	/**
	 * 返回 BigDecimal 数据
	 */
	public static BigDecimal formatBigDecimal(HSSFCell cell) throws ParseException{
		String str = ExcelUtil.formatCell(cell);
		BigDecimal num = new BigDecimal(str.trim());
		return num;
	}
}


