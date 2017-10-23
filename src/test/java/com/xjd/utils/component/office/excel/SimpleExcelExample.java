package com.xjd.utils.component.office.excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * @author elvis.xu
 * @since 2017-09-12 19:13
 */
public class SimpleExcelExample {

	public static void main(String[] args) throws FileNotFoundException {
		SimpleExcel simpleExcel = new SimpleExcel();

		simpleExcel.newSheet()
				.addHeaders("A", "B", "C")
				.setWidth("A", 4000)
				.newRow()
				.setData("D", 100)
				.setFormat("D", 2)
				.setData("A", new Date())
				.setFormat("A", 22)
				.setData("C", "哈哈")
				.setData("B", null)
				.newRow()
				.setData("D", 100)
				.addHeader("A", "AAAAA")
				.setData("A", new Date())
				.setFormat("A", 22)
				.setData("C", "哈哈")
				.setData("B", null)
				.newRow()
				.setData("D", 100)
				.setData("A", new Date())
				.setFormat("A", 22)
				.setData("C", "哈哈")
				.setData("B", null)
				.newSheet("我是sheet2", false)
				.addHeaders("A", "B", "C")
				.setWidth("A", 4000)
				.newRow()
				.setData("D", 100)
				.setData("A", new Date())
				.setFormat("A", 22)
				.setData("C", "哈哈")
				.setData("B", null)
				.newRow()
				.setData("D", 100)
				.setData("A", new Date())
				.setFormat("A", 22)
				.setData("C", "哈哈")
				.setData("B", null)
				.newRow()
				.setData("D", 100)
				.setData("A", new Date())
				.setFormat("A", 22)
				.setData("C", "哈哈")
				.setData("B", null)
				.setData("E", true)
				.setData("F", Calendar.getInstance())
				.setFormat("F", 22)
				.setWidth("F", 4000)
				.write(new FileOutputStream("/data/tmp/tmpExcel.xls"))
				.close();

	}
}