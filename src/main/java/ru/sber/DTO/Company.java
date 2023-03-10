package ru.sber.DTO;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelRow;

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
        return "Company {" +
                ", rowIndex=" + rowIndex + "'" +
                " ogrn=" + ogrn +
                ", name=" + name + "'" +
                '}';
    }

//    public String getOgrn() {
//        return ogrn;
//    }
//
//    public void setOgrn(String ogrn) {
//        this.ogrn = ogrn;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
////    public Company(){
////    }
////
////    public Company(String ogrn, String name) {
////        this.ogrn = ogrn;
////        this.name = name;
////    }
//
//    public static List<Company> readFromExcel(String filepath) {
//
//        List<Company> companies = new ArrayList<>();
//
//        FileInputStream file = null;
//
//        try {
//            file = new FileInputStream(filepath);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        HSSFWorkbook workbook;
//
//        try {
//            assert file != null;
//
//            workbook = new HSSFWorkbook(file);
//
//            Iterator<Row> rowIterator = workbook.getSheetAt(0).iterator();
//
//            if (rowIterator.hasNext()) {
//                rowIterator.next();
//            }
//
//            while (rowIterator.hasNext()) {
//                Row row = rowIterator.next();
//                Iterator<Cell> cellIterator = row.cellIterator();
//
//                Company company = new Company();
//
//                if (cellIterator.hasNext()) {
//                    Cell cell = cellIterator.next();
//                    String ogrn = cell.getStringCellValue();
//
//                    if (ogrn != null && !ogrn.isEmpty()) {
//                        company.setOgrn(ogrn);
////                        System.out.println(ogrn);
//                    }
//                }
//
//                if (cellIterator.hasNext()) {
//                    Cell cell = cellIterator.next();
//                    String name = cell.getStringCellValue();
//
//                    if (name != null && !name.isEmpty()) {
//                        company.setName(name);
////                        System.out.println(name);
//                    }
//                }
//
//                companies.add(company);
//            }
//
//            workbook.close();
//            file.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return companies;
//    }
//
//    @Override
//    public String toString() {
//        StringBuffer buffer = new StringBuffer();
//
//        buffer.append(getOgrn());
//        buffer.append("\n");
//        buffer.append(getName());
//        buffer.append("\n");
//
//        return buffer.toString();
//    }
}
