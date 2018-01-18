package com.xjd.utils.component.office.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.xjd.utils.basic.StringUtils;

/**
 * @author elvis.xu
 * @since 2017-09-12 17:33
 */
public class SimpleExcel {
	public static final int HSSF_EXCEL_2003 = 1;
	public static final int XSSF_EXCEL_2007 = 2;
	public static final int SXSSF_EXCEL_2007 = 3;

	protected Workbook workbook;
	protected Sheet sheet;
	protected Row row;

	protected List<String> headerNames;
	protected List<String> headerTitles;
	protected Row headerRow;

	public SimpleExcel() {
		this(XSSF_EXCEL_2007);
	}

	public SimpleExcel(int mode) {
		if (HSSF_EXCEL_2003 == mode) {
			workbook = new HSSFWorkbook();
		} else if (XSSF_EXCEL_2007 == mode) {
			workbook = new XSSFWorkbook();
		} else if (SXSSF_EXCEL_2007 == mode) {
			workbook = new SXSSFWorkbook();
		} else {
			throw new RuntimeException("unresolved mode: " + mode);
		}
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public SimpleExcel newSheet() {
		return newSheet(null, true);
	}

	public SimpleExcel newSheet(String name) {
		return newSheet(name, true);
	}

	public SimpleExcel newSheet(boolean showHeader) {
		return newSheet(null, showHeader);
	}

	public SimpleExcel newSheet(String name, boolean showHeader) {
		sheet = name == null ? workbook.createSheet() : workbook.createSheet(name);
		row = null;
		headerNames = new ArrayList<>();
		headerTitles = new ArrayList<>();
		headerRow = null;
		if (showHeader) {
			headerRow = sheet.createRow(0);
		}
		return this;
	}

	public SimpleExcel addHeaders(String... names) {
		if (names != null && names.length > 0) {
			for (String name : names) {
				name = StringUtils.trimToEmpty(name);
				addHeader(name, name);
			}
		}
		return this;
	}

	public SimpleExcel addHeader(String name, String title) {
		name = StringUtils.trimToEmpty(name);
		title = StringUtils.trimToEmpty(title);
		int index = headerNames.indexOf(name);
		if (index > -1) {
			if (!title.equals(headerTitles.get(index))) {
				headerTitles.set(index, title);
				if (headerRow != null) {
					headerRow.getCell(index).setCellValue(title);
				}
			}
		} else {
			headerNames.add(name);
			headerTitles.add(title);
			if (headerRow != null) {
				headerRow.createCell(headerNames.size() - 1).setCellValue(title);
			}
		}
		return this;
	}

	public SimpleExcel newRow() {
		int lastIndex = sheet.getLastRowNum();
		if (lastIndex == 0 && sheet.getRow(0) == null) {
			row = sheet.createRow(0);
		} else {
			row = sheet.createRow(lastIndex + 1);
		}
		return this;
	}

	public SimpleExcel setData(String headerName, Object data) {
		Cell cell = getCell(headerName);

		if (data == null) {
			cell.setCellValue("");

		} else if (data instanceof String) {
			cell.setCellValue((String) data);

		} else if (data instanceof Number) {
			cell.setCellValue(((Number) data).doubleValue());

		} else if (data instanceof Boolean) {
			cell.setCellValue((Boolean) data);

		} else if (data instanceof Calendar) {
			cell.setCellValue((Calendar) data);

		} else if (data instanceof Date) {
			cell.setCellValue((Date) data);

		} else if (data instanceof RichTextString) {
			cell.setCellValue((RichTextString) data);

		}

		return this;
	}

	public SimpleExcel setStyle(String headerName, CellStyle cellStyle) {
		Cell cell = getCell(headerName);
		cell.setCellStyle(cellStyle);
		return this;
	}

	/**
	 * set the data format (must be a valid format). Built in formats are defined at {@link BuiltinFormats}.
	 * @see DataFormat
	 */
	public SimpleExcel setFormat(String headerName, int format) {
		Cell cell = getCell(headerName);
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.cloneStyleFrom(cell.getCellStyle());
		cellStyle.setDataFormat((short)format);
		cell.setCellStyle(cellStyle);
		return this;
	}

	protected Cell getCell(String headerName) {
		int index = headerNames.indexOf(headerName);
		if (index == -1) {
			addHeaders(headerName);
			index = headerNames.indexOf(headerName);
		}

		int lastCellNum = row.getLastCellNum();
		if (lastCellNum < 0) lastCellNum = 0;

		for (int i = lastCellNum; i <= index; i++) {
			row.createCell(i);
		}

		Cell cell = row.getCell(index);
		return cell;
	}

	public SimpleExcel setWidth(String headerName, int width) {
		int index = headerNames.indexOf(headerName);
		if (index == -1) {
			addHeaders(headerName);
			index = headerNames.indexOf(headerName);
		}
		sheet.setColumnWidth(index, width);
		return this;
	}

	public SimpleExcel write(OutputStream out) {
		try {
			workbook.write(out);
			out.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public void close() {
		try {
			workbook.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
