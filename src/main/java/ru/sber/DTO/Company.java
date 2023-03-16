package ru.sber.dto;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelRow;

/**
 * DTO Company.
 */
public class Company {
    @ExcelRow
    private int rowIndex;
    @ExcelCell(value = 0)
    private String ogrn;
    @ExcelCell(value = 1)
    private String name;

    public int getRowIndex() {
        return rowIndex;
    }

    public String getOgrn() {
        return ogrn;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Company {"
                + ", rowIndex=" + rowIndex + "'"
                + " ogrn=" + ogrn
                + ", name=" + name + "'"
                + '}';
    }
}
