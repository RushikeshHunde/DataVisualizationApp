package com.datavisualization;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@SuppressWarnings("serial")
@MultipartConfig
public class VisualizeServlet extends HttpServlet {
	
	 private String escapeJavaScript(String str) {
	        return str.replace("\\", "\\\\")
	                  .replace("\"", "\\\"")
	                  .replace("\n", "\\n")
	                  .replace("\r", "\\r")
	                  .replace("\t", "\\t");
	    }
	
	 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        Part filePart = request.getPart("file");
        String fileName = filePart.getSubmittedFileName();
        InputStream fileContent = filePart.getInputStream();

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        List<List<String>> data = new ArrayList<>();

        switch (extension) {
            case "csv":
                data = readCSV(fileContent);
                break;

            case "xlsx":
            case "xls":
                data = readExcel(fileContent);
                break;

            case "pdf":
                request.setAttribute("message", "PDF uploaded: " + fileName + ". (No preview available)");
                break;

            default:
                request.setAttribute("message", "Unsupported file type: " + fileName);
        }
        StringBuilder jsonBuilder = new StringBuilder("[");
        if (!data.isEmpty()) {
            // Process header row (always strings)
            jsonBuilder.append("[");
            for (String headerCell : data.get(0)) {
                jsonBuilder.append("\"").append(escapeJavaScript(headerCell)).append("\",");
            }
            jsonBuilder.setLength(jsonBuilder.length() - 1);
            jsonBuilder.append("],");

            // Process data rows with type checking
            for (int i = 1; i < data.size(); i++) {
                List<String> row = data.get(i);
                jsonBuilder.append("[");
                for (int j = 0; j < row.size(); j++) {
                    String cell = row.get(j);
                    if (j == 0) {
                        // First column always string (category)
                        jsonBuilder.append("\"").append(escapeJavaScript(cell)).append("\",");
                    } else {
                        // Subsequent columns: numeric or string
                        if (isNumeric(cell)) {
                            jsonBuilder.append(cell).append(",");
                        } else {
                            // Treat non-numeric as 0 to maintain number type
                            jsonBuilder.append("0,");
                        }
                    }
                }
                jsonBuilder.setLength(jsonBuilder.length() - 1);
                jsonBuilder.append("],");
            }
            jsonBuilder.setLength(jsonBuilder.length() - 1);
        }
        jsonBuilder.append("]");
        // ============== END OF MODIFICATIONS ==============

        request.setAttribute("fileName", fileName);
        request.setAttribute("chartData", jsonBuilder.toString());
        request.getRequestDispatcher("Visualize.jsp").forward(request, response);
    }

    // Enhanced numeric check
    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?([eE][+-]?\\d+)?"); // Handles scientific notation
    }
       

    private List<List<String>> readCSV(InputStream input) throws IOException {
        List<List<String>> rows = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] cols = line.split(",");
            List<String> row = new ArrayList<>();
            for (String col : cols) {
                row.add(col);
            }
            rows.add(row);
        }
        return rows;
    }

    private List<List<String>> readExcel(InputStream input) throws IOException {
        List<List<String>> rows = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(input);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            List<String> cols = new ArrayList<>();
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING: cols.add(cell.getStringCellValue()); break;
                    case NUMERIC: cols.add(String.valueOf(cell.getNumericCellValue())); break;
                    default: cols.add(""); break;
                }
            }
            rows.add(cols);
        }
        workbook.close();
        return rows;
    }
}
