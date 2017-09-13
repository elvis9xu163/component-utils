package com.xjd.utils.component.office.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

/**
 * @author elvis.xu
 * @since 2017-09-12 17:33
 */
public class SimpleExcel {
	protected HSSFWorkbook workbook;
	protected HSSFSheet sheet;
	protected HSSFRow row;

	protected List<String> headerNames;
	protected HSSFRow headerRow;

	public SimpleExcel() {
		workbook = new HSSFWorkbook();
	}

	public HSSFWorkbook getWorkbook() {
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
		headerRow = null;
		if (showHeader) {
			headerRow = sheet.createRow(0);
		}
		return this;
	}

	public SimpleExcel addHeaders(String... headers) {
		if (headers != null && headers.length > 0) {
			for (String header : headers) {
				if (headerNames.indexOf(header) > -1) {
					continue;
				}

				headerNames.add(header);
				if (headerRow != null) {
					headerRow.createCell(headerNames.size() - 1).setCellValue(header);
				}
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

	public SimpleExcel setData(String header, Object data) {
		Cell cell = getCell(header);

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

	public SimpleExcel setStyle(String header, CellStyle cellStyle) {
		Cell cell = getCell(header);
		cell.setCellStyle(cellStyle);
		return this;
	}

	/**
	 * set the data format (must be a valid format). Built in formats are defined at {@link BuiltinFormats}.
	 * @see DataFormat
	 */
	public SimpleExcel setFormat(String header, int format) {
		Cell cell = getCell(header);
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.cloneStyleFrom(cell.getCellStyle());
		cellStyle.setDataFormat((short)format);
		cell.setCellStyle(cellStyle);
		return this;
	}

	protected Cell getCell(String header) {
		int index = headerNames.indexOf(header);
		if (index == -1) {
			addHeaders(header);
			index = headerNames.indexOf(header);
		}

		int lastCellNum = row.getLastCellNum();
		if (lastCellNum < 0) lastCellNum = 0;

		for (int i = lastCellNum; i <= index; i++) {
			row.createCell(i);
		}

		HSSFCell cell = row.getCell(index);
		return cell;
	}

	public SimpleExcel setWidth(String header, int width) {
		int index = headerNames.indexOf(header);
		if (index == -1) {
			addHeaders(header);
			index = headerNames.indexOf(header);
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
