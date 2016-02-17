package com.dhruvchaudhary.hrm.views;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.dhruvchaudhary.hrm.model.CompensatoryOff;
import com.dhruvchaudhary.hrm.model.Leave;
import com.dhruvchaudhary.hrm.model.WorkFromHome;

public class EmployeeReport extends AbstractExcelView {

	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<Leave> leaveData = (ArrayList<Leave>) model.get("leaveData");
		ArrayList<CompensatoryOff> compOffData = (ArrayList<CompensatoryOff>) model.get("compOffData");
		ArrayList<WorkFromHome> wfhData = (ArrayList<WorkFromHome>) model.get("wfhData");
		String empName = (String) model.get("empName");
		
		HSSFSheet leaveSheet = workbook.createSheet("Leaves");
		HSSFSheet compOffSheet = workbook.createSheet("Comp Offs");
		HSSFSheet wfhSheet = workbook.createSheet("Work From Home");
		
		//styling
		HSSFCellStyle headerStyle = workbook.createCellStyle();
		HSSFFont headerFont = workbook.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerStyle.setFont(headerFont);
		
		//Date Format
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		HSSFRow leaveHeader = leaveSheet.createRow(0);
		leaveHeader.createCell(0).setCellValue("Leave Date");
		leaveHeader.createCell(1).setCellValue("Leave Type");
		leaveHeader.createCell(2).setCellValue("Cancel Date");
		for(int i = 0; i<leaveHeader.getLastCellNum(); i++)
			leaveHeader.getCell(i).setCellStyle(headerStyle);
		
		int rowNum = 1;
		
			for(Leave l:leaveData) {
				HSSFRow row = leaveSheet.createRow(rowNum++);
				row.createCell(0).setCellValue(formatter.format(l.getDate()));
				row.createCell(1).setCellValue(l.getLeaveType().getDescription());
				if(l.getCancelDate() != null)
					row.createCell(2).setCellValue(l.getCancelDate().toString());
			}
		
		HSSFRow compOffHeader = compOffSheet.createRow(0);
		compOffHeader.createCell(0).setCellValue("Comp Off Date");
		compOffHeader.createCell(1).setCellValue("Consumption Info");
		compOffHeader.createCell(2).setCellValue("Leave Date");
		
		for(int i = 0; i<compOffHeader.getLastCellNum(); i++)
			compOffHeader.getCell(i).setCellStyle(headerStyle);
		
		rowNum = 1;
			for(CompensatoryOff c:compOffData) {
				HSSFRow row = compOffSheet.createRow(rowNum++);
				row.createCell(0).setCellValue(formatter.format(c.getAppliedAgainst()));
				if(c.getLeave() == null)
					row.createCell(1).setCellValue("Unconsumed");
				else {
					row.createCell(1).setCellValue("Consumed");
					row.createCell(2).setCellValue(formatter.format(c.getLeave().getDate()));
				}
			}
		
		HSSFRow wfhHeader = wfhSheet.createRow(0);
		wfhHeader.createCell(0).setCellValue("Work From Home Date");
		
		for(int i = 0; i<wfhHeader.getLastCellNum(); i++)
			wfhHeader.getCell(i).setCellStyle(headerStyle);

		rowNum = 1;

			for(WorkFromHome w:wfhData) {
				HSSFRow row = wfhSheet.createRow(rowNum++);
				row.createCell(0).setCellValue(formatter.format(w.getDate()));
			}
		
		String filename = "Employee_Report_" + empName + "_" + new Date().toString();
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".xls\"");
	}


}
