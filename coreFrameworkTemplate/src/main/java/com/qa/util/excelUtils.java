package com.qa.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

import com.qa.base.TestBase;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;


public class excelUtils extends TestBase {

    public String path;
    private FileInputStream fis = null;
    private FileOutputStream fileOut = null;
    private Workbook workbook = null;
    private Sheet sheet = null;
    private Row row = null;
    private Cell cell = null;


    /*
    * Constructor that initializes workbook and worksheet object
    * @param path of the excel workbook
    * */
    public excelUtils(String path) {

        this.path = path;
        try {
            fis = new FileInputStream(path);
            workbook = WorkbookFactory.create(fis);
            sheet = workbook.getSheetAt(0);
            fis.close();
        } catch (Exception e) {
            System.out.println("Workbook Object not initialized. Error Message: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /*
    * Returns row count of the excel sheet
    * @param sheetname name of the worksheet
    * @return number of rows in the excel sheet
    * */
    public int getRowCount(String sheetName) {

        int index = workbook.getSheetIndex(sheetName);

        if (index == -1)
            return 0;
        else {
            sheet = workbook.getSheetAt(index);
            int number = sheet.getLastRowNum() + 1;
            return number;
        }
    }


    /*
    * fetches cell data from excel worksheet
    * @param sheetname name of the sheet from where to fetch data
    * @param rowNum row number from where to fetch data
    * @param colName name of the column from where to fetch data
    * @return cell data from excel worksheet
    * */
    public String getCellData(String sheetName, int rowNum, String colName) {
        String returnValue = "";

        try {
            int col_Num = -1;

            if (rowNum <= 0)
                returnValue = "";

            int index = workbook.getSheetIndex(sheetName);
            row = sheet.getRow(0);
            for(int i = 0; i < this.row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equals((colName.trim())))
                    col_Num = i;
            }

            if (col_Num == -1)
                returnValue = "";


            row = sheet.getRow(rowNum - 1);
            if (row == null)
                returnValue = "";

            cell = row.getCell(col_Num);
            if (cell == null)
                returnValue = "";


            if (cell.getCellType() == Cell.CELL_TYPE_STRING)
                returnValue = cell.getStringCellValue();
            else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

                String cellText = String.valueOf(cell.getNumericCellValue());

                if (HSSFDateUtil.isCellDateFormatted(cell)) {

                    double d = cell.getNumericCellValue();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(HSSFDateUtil.getJavaDate(d));
                    cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
                    cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + 1 + "/" + cellText;
                }

                returnValue = cellText;
            } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
                returnValue = "";
            else
                returnValue = String.valueOf(cell.getBooleanCellValue());

        } catch (Exception e) {

            System.out.println("row " + rowNum + " or column " + colName + " does not exist in sheet " + sheetName);
            e.printStackTrace();
        }

        return returnValue;
    }


    /*
     * fetches cell data from excel worksheet
     * @param sheetname name of the sheet from where to fetch data
     * @param rowNum row number from where to fetch data
     * @param colNum column number from where to fetch data
     * @return cell data from excel worksheet
     * */
    public String getCellData(String sheetName, int rowNum, int colNum) {
        String returnValue = "";

        try {
            if (rowNum <= 0)
                returnValue = "";

            int index = workbook.getSheetIndex(sheetName);
            if (index == -1)
                returnValue = "";

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(rowNum - 1);
            if (row == null)
                returnValue = "";

            cell = row.getCell(colNum);
            if (cell == null)
                returnValue = "";


            if (cell.getCellType() == Cell.CELL_TYPE_STRING)
                returnValue = cell.getStringCellValue();
            else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

                String cellText = String.valueOf(cell.getNumericCellValue());
                if (HSSFDateUtil.isCellDateFormatted(cell)) {

                    double d = cell.getNumericCellValue();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(HSSFDateUtil.getJavaDate(d));
                    cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
                    cellText = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cellText;
                }

                returnValue = cellText;
            } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
                returnValue = "";
            else
                returnValue = String.valueOf(cell.getBooleanCellValue());
        } catch (Exception e) {

            System.out.println("row " + rowNum + " or column " + colNum + " does not exist in sheet " + sheetName);
            e.printStackTrace();
        }

        return returnValue;
    }


    /*
     * write cell data in excel worksheet
     * @param sheetName name of the sheet where to write data
     * @param rowNum row number where to write data
     * @param colName name of the column where to write data
     * @param data cell data which is to be written to the excel sheet
     * @return true if data is written successfully otherwise returns false
     * */
    public boolean setCellData(String sheetName, int rowNum, String colName, String data) {
        boolean returnValue = false;

        try {
            int colNum = -1;
            if (rowNum <= 0)
                returnValue = false;

            int index = workbook.getSheetIndex(sheetName);
            if (index == -1)
                returnValue = false;

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equals(colName))
                    colNum = i;
            }
            if (colNum == -1)
                returnValue = false;

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum - 1);
            if (row == null)
                row = sheet.createRow(rowNum - 1);

            cell = row.getCell(colNum);
            if (cell == null)
                cell = row.createCell(colNum);

            cell.setCellValue(data);

            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
            returnValue = true;

        } catch (Exception e) {
            System.out.println("Some Error Occurred. Error Message: " + e.getMessage());
            e.printStackTrace();
        }

        return returnValue;
    }


    /*
     * write cell data in excel worksheet
     * @param sheetName name of the sheet where to write data
     * @param rowNum row number where to write data
     * @param colName name of the column where to write data
     * @param data cell data which is to be written to the excel sheet
     * @param url url with which data is to be hyperlinked
     * @return true if data is written successfully otherwise returns false
     * */
    public boolean setCellData(String sheetName, int rowNum, String colName, String data, String url) {
        boolean returnValue = false;

        try {
            int colNum = -1;
            if (rowNum <= 0)
                returnValue = false;

            int index = workbook.getSheetIndex(sheetName);
            if (index == -1)
                returnValue = false;

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equals(colName))
                    colNum = i;
            }
            if (colNum == -1)
                returnValue = false;

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum - 1);
            if (row == null)
                row = sheet.createRow(rowNum - 1);

            cell = row.getCell(colNum);
            if (cell == null)
                cell = row.createCell(colNum);

            cell.setCellValue(data);

            CreationHelper createHelper = workbook.getCreationHelper();
            CellStyle hlink_style = workbook.createCellStyle();

            Font hlink_font = workbook.createFont();
            hlink_font.setUnderline(XSSFFont.U_SINGLE);
            hlink_font.setColor(IndexedColors.BLUE.getIndex());
            hlink_style.setFont(hlink_font);

            Hyperlink link = createHelper.createHyperlink(XSSFHyperlink.LINK_FILE);
            link.setAddress(url);
            cell.setHyperlink(link);
            cell.setCellStyle(hlink_style);

            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
            returnValue = true;

        } catch (Exception e) {
            System.out.println("Some Error Occurred. Error Message: " + e.getMessage());
            e.printStackTrace();
        }
        return returnValue;
    }

    /*
    * adds sheet to the existing workbook
    * @param sheetName name of the sheet which needs to be added
    * @return true if sheet is added successfully otherwise returns false
    * */
    public boolean addSheet(String sheetName) {

        FileOutputStream fileOut;
        try {
            workbook.createSheet(sheetName);
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /*
    * removes sheet from existing workbook
    * @param SheetName name of the sheet which is to be removed
    * @return true if sheet is removed successfully otherwise false
    * */
    public boolean removeSheet(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1)
            return false;

        FileOutputStream fileOut;
        try {
            workbook.removeSheetAt(index);
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /*
    * add column in the existing workbook
    * @param sheetName name of the sheet in the existing workbook
    * @param colName name of the column to be added
    * @return true if column is added successfully otherwise false
    * */
    public boolean addColumn(String sheetName, String colName) {

        try {
            int index = workbook.getSheetIndex(sheetName);
            if (index == -1)
                return false;

            CellStyle style = this.workbook.createCellStyle();
            style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            sheet = workbook.getSheetAt(index);

            row = sheet.getRow(0);
            if (row == null)
                row = sheet.createRow(0);

            if (row.getLastCellNum() == -1)
                cell = row.createCell(0);
            else
                cell = row.createCell(this.row.getLastCellNum());

            cell.setCellValue(colName);
            cell.setCellStyle(style);

            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    /*
    * delete column from the existing workbook
    * @param sheetName name of the sheet in the existing workbook
    * @param colNum number of the column to be deleted
    * @return true if column is deleted successfully otherwise false
    * */
    public boolean removeColumn(String sheetName, int colNum) {
        try {
            if (!isSheetExist(sheetName))
                return false;

            sheet = workbook.getSheet(sheetName);

            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
            style.setFillPattern(HSSFCellStyle.NO_FILL);

            for (int i = 0; i < getRowCount(sheetName); i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    cell = row.getCell(colNum);
                    if (cell != null) {
                        cell.setCellStyle(style);
                        row.removeCell(this.cell);
                    }
                }
            }

            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }


    /*
    * verify if sheet exists
    * @param sheetName name of the sheet which needs to be checked
    * @return true if sheet exists otherwise false
    * */
    public boolean isSheetExist(String sheetName) {

        int index = workbook.getSheetIndex(sheetName);
        if (index == -1) {
            index = workbook.getSheetIndex(sheetName.toUpperCase());
            if (index == -1)
                return false;
            else
                return true;
        } else
            return true;
    }


    /*
    * returns number of columns in a sheet
    * @param sheetName name of the sheet
    * */
    public int getColumnCount(String sheetName) {

        if (!isSheetExist(sheetName))
            return -1;

        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(0);

        if (row == null)
            return -1;

        return row.getLastCellNum();

    }


    /*
    * find the row number where data is present
    * @param sheetName name of the sheet
    * @param colName name of column where to search data
    * @param cellValue value which needs to be searched
    * @return row number where data is found
    * */
    public int getCellRowNum(String sheetName, String colName, String cellValue) {

        for (int i = 1; i <= getRowCount(sheetName); i++) {
            if (getCellData(sheetName, i, colName).equalsIgnoreCase(cellValue)) {
                return i;
            }
        }
        return -1;

    }


    /*
    * returns data from the sheet in the form of two dimensional array
    * @param sheetName name of the sheet
    * */
    public Object[][] getTestData(String sheetName) {

        sheet = workbook.getSheet(sheetName);
        Object[][] data = new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];

        for(int i = 0; i < sheet.getLastRowNum(); i++) {
            for(int k = 0; k < sheet.getRow(0).getLastCellNum(); k++) {
                data[i][k] = sheet.getRow(i+1).getCell(k).toString();
            }
        }
        return data;
    }


    /*
     * returns data from the sheet in the form of two dimensional array
     * @param sheetName name of the sheet
     * */
    public Iterator<Object[]> getTestDatas(String sheetName) {

        sheet = workbook.getSheet(sheetName);
        List<Object[]> data = new ArrayList<>();
        Object[][] dataArray = new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];

        for(int i = 0; i < sheet.getLastRowNum(); i++) {
            for(int k = 0; k < sheet.getRow(0).getLastCellNum(); k++) {
                dataArray[i][k] = sheet.getRow(i+1).getCell(k).toString();
            }
        }

        data = Arrays.asList(dataArray);
        return data.iterator();
    }

}
