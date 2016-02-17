package com.dhruvchaudhary.hrm.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class TeamReport extends AbstractExcelView {

	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<Integer, ArrayList<String>> data = (Map<Integer, ArrayList<String>>) model.get("data");
		int mgrCode = Integer.parseInt((String) model.get("mgrCode"));
		String mgrName = "";
		HSSFSheet sheet = workbook.createSheet("Report");

		//styling
		HSSFCellStyle headerStyle = workbook.createCellStyle();
		HSSFFont headerFont = workbook.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerStyle.setFont(headerFont);

		HSSFRow leaveHeader = sheet.createRow(0);
		leaveHeader.createCell(0).setCellValue("Emp ID");
		leaveHeader.createCell(1).setCellValue("Name");
		leaveHeader.createCell(2).setCellValue("Paid Leaves");
		leaveHeader.createCell(3).setCellValue("LWPs");
		leaveHeader.createCell(4).setCellValue("Leaves against Comp Off");
		leaveHeader.createCell(5).setCellValue("Comp Off Granted");
		leaveHeader.createCell(6).setCellValue("Work From Home");
		for(int i = 0; i<leaveHeader.getLastCellNum(); i++)
			leaveHeader.getCell(i).setCellStyle(headerStyle);

		int rowNum = 1;
		for(Entry<Integer, ArrayList<String>>e:data.entrySet()) {
			HSSFRow row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(e.getKey());
			if(e.getKey() == mgrCode)
				mgrName = e.getValue().get(0);
			int colIndex = 1;
			for(String s:e.getValue())				
				row.createCell(colIndex++).setCellValue(s);
		}
		
		mgrName = mgrName.replace(' ', '_');
		String filename = "Team_Report_" + mgrName + "_" + new Date().toString();
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".xls\"");
	}


}
