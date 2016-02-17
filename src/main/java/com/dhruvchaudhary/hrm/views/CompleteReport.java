package com.dhruvchaudhary.hrm.views;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import com.dhruvchaudhary.hrm.model.CompensatoryOff;
import com.dhruvchaudhary.hrm.model.Employee;
import com.dhruvchaudhary.hrm.model.Leave;
import com.dhruvchaudhary.hrm.model.WorkFromHome;

public class CompleteReport extends AbstractExcelView {

	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<Employee, ArrayList<Leave>> leaveData = (Map<Employee, ArrayList<Leave>>) model.get("leaveData");
		Map<Employee, ArrayList<CompensatoryOff>> compOffData = (Map<Employee, ArrayList<CompensatoryOff>>) model.get("compOffData");
		Map<Employee, ArrayList<WorkFromHome>> wfhData = (Map<Employee, ArrayList<WorkFromHome>>) model.get("wfhData");
		
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
		leaveHeader.createCell(0).setCellValue("Emp ID");
		leaveHeader.createCell(1).setCellValue("Name");
		leaveHeader.createCell(2).setCellValue("Leave Date");
		leaveHeader.createCell(3).setCellValue("Leave Type");
		leaveHeader.createCell(4).setCellValue("Cancel Date");
		for(int i = 0; i<leaveHeader.getLastCellNum(); i++)
			leaveHeader.getCell(i).setCellStyle(headerStyle);
		
		int rowNum = 1;
		for(Entry<Employee, ArrayList<Leave>>e:leaveData.entrySet()) {
			for(Leave l:e.getValue()) {
				HSSFRow row = leaveSheet.createRow(rowNum++);
				row.createCell(0).setCellValue(e.getKey().getEmpCode());
				row.createCell(1).setCellValue(e.getKey().getName());
				row.createCell(2).setCellValue(formatter.format(l.getDate()));
				row.createCell(3).setCellValue(l.getLeaveType().getDescription());
				if(l.getCancelDate() != null)
					row.createCell(4).setCellValue(l.getCancelDate());
			}
		}
		
		HSSFRow compOffHeader = compOffSheet.createRow(0);
		compOffHeader.createCell(0).setCellValue("Emp ID");
		compOffHeader.createCell(1).setCellValue("Name");
		compOffHeader.createCell(2).setCellValue("Comp Off Date");
		compOffHeader.createCell(3).setCellValue("Consumption Info");
		compOffHeader.createCell(4).setCellValue("Leave Date");
		
		for(int i = 0; i<compOffHeader.getLastCellNum(); i++)
			compOffHeader.getCell(i).setCellStyle(headerStyle);
		
		rowNum = 1;
		for(Entry<Employee, ArrayList<CompensatoryOff>>e:compOffData.entrySet()) {
			for(CompensatoryOff c:e.getValue()) {
				HSSFRow row = compOffSheet.createRow(rowNum++);
				row.createCell(0).setCellValue(e.getKey().getEmpCode());
				row.createCell(1).setCellValue(e.getKey().getName());
				row.createCell(2).setCellValue(formatter.format(c.getAppliedAgainst()));
				if(c.getLeave() == null)
					row.createCell(3).setCellValue("Unconsumed");
				else {
					row.createCell(3).setCellValue("Consumed");
					row.createCell(4).setCellValue(formatter.format(c.getLeave().getDate()));
				}
			}
		}
		
		HSSFRow wfhHeader = wfhSheet.createRow(0);
		wfhHeader.createCell(0).setCellValue("Emp ID");
		wfhHeader.createCell(1).setCellValue("Name");
		wfhHeader.createCell(2).setCellValue("Work From Home Date");
		
		for(int i = 0; i<wfhHeader.getLastCellNum(); i++)
			wfhHeader.getCell(i).setCellStyle(headerStyle);

		rowNum = 1;
		for(Entry<Employee, ArrayList<WorkFromHome>>e:wfhData.entrySet()) {
			for(WorkFromHome w:e.getValue()) {
				HSSFRow row = wfhSheet.createRow(rowNum++);
				row.createCell(0).setCellValue(e.getKey().getEmpCode());
				row.createCell(1).setCellValue(e.getKey().getName());
				row.createCell(2).setCellValue(formatter.format(w.getDate()));
			}
		}
		
		String filename = "Report_Consolidated_" + new Date().toString();
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".xls\"");
	}


}
