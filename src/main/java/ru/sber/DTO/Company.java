package ru.sber.dto;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelRow;

/**
 * Company from Excel.
 */
public class Company {
    @ExcelRow
    private int rowIndex;
    @ExcelCell(value = 0)
    private String psrn;
    @ExcelCell(value = 1)
    private String name;

    public int getRowIndex() {
        return rowIndex;
    }

    public String getPsrn() {
        return psrn;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Company {"
                + ", rowIndex =" + rowIndex + "'"
                + " psrn =" + psrn
                + ", name =" + name + "'"
                + '}';
    }
}
