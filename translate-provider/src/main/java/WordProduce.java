import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;

public class WordProduce {
    public static void main(String[] args) throws IOException {
        XWPFDocument document = new XWPFDocument();

        XWPFParagraph titlePara = document.createParagraph();
        titlePara.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun titleRun = titlePara.createRun();
        titleRun.setColor("000000");
        titleRun.setFontSize(20);
        titleRun.setFontFamily("宋体");
        titleRun.setBold(true);
        titleRun.setText("标题");
        titleRun.addBreak();//换行

        XWPFParagraph firstPara = document.createParagraph();
        firstPara.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun firstRun = firstPara.createRun();
        firstRun.setFontSize(13);
        firstRun.setFontFamily("黑体");
        firstRun.addTab();
        firstRun.setText("有人曾经有人曾经有人曾经有人曾经有人曾经有人曾经有人曾经有人曾经有人曾经有人曾经有人曾经有人曾经有人曾经有人曾经有人曾经有人曾经");
        firstRun.addBreak();
        firstRun.addBreak();
        firstRun.addTab();
        firstRun.setText("虽然现在2虽然现在2虽然现在2虽然现在2虽然现在2虽然现在2虽然现在2虽然现在2虽然现在2虽然现在2虽然现在2虽然现在2虽然现在2");

        XWPFParagraph secondPara = document.createParagraph();
        secondPara.setAlignment(ParagraphAlignment.RIGHT);
        XWPFRun secondRun = secondPara.createRun();
        secondRun.setText("时间:" + new Date());

        XWPFTable table = document.createTable(1, 4);
        CTTbl ttbl = table.getCTTbl();
        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();
        CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();

        // 大表宽度
        tblWidth.setW(new BigInteger("10000"));
        tblWidth.setType(STTblWidth.DXA);

        // 创建表头数据
        int i = 0;
        table.getRow(i).setHeight(500);
//        setCellText(table.getRow(i).getCell(0), "序号", "FFFFFF", 1000);
//        setCellText(table.getRow(i).getCell(1), "类别", "FFFFFF", 2000);
//        setCellText(table.getRow(i).getCell(2), "证据名称", "FFFFFF", 2000);
//        setCellText(table.getRow(i).getCell(3), "备注", "FFFFFF", 2000);
//        setCellText(table.getRow(i).getCell(4), "测试添加测试添加测试添加", "FFFFFF", 2000);

        writeDocToFile(document, "/Users/xingchuan/Desktop/test_" + System.currentTimeMillis() + ".docx");
    }

    private static void setCellText(XWPFTableCell cell, String text, String bgcolor, int width) {
        CTTc cttc = cell.getCTTc();
        CTTcPr cellPr = cttc.addNewTcPr();
        cellPr.addNewTcW().setW(BigInteger.valueOf(width));
        cell.setColor(bgcolor);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        CTTcPr ctPr = cttc.addNewTcPr();
        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
        cell.setText(text);
    }

    private static void writeDocToFile(XWPFDocument document, String filePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        document.write(fos);
        document.close();
    }
}