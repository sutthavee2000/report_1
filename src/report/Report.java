package report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;


import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Report {

    final static Logger logger = Logger.getLogger(Report.class);

    public void insertCard(List<card> list) throws Exception {

        logger.info("start insert card master");

        File file = new File("config.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("config");
        Node node = nodeList.item(0);
        Element eElement = (Element) node;
        String URL = (String) eElement.getElementsByTagName("url").item(0).getTextContent();
        String username = (String) eElement.getElementsByTagName("username").item(0).getTextContent();
        String password = (String) eElement.getElementsByTagName("password").item(0).getTextContent();
        /*
        FileInputStream fis = new FileInputStream("config.properties");
        Properties p = new Properties();
        p.load(fis);
        String URL = (String) p.get("URL");
        String username = (String) p.get("username");
        String password = (String) p.get("password");
         */
        String sql = "INSERT INTO card_master(card_no,issue_date,expire_date,card_status,cust_id) "
                + "VALUES(?,?,?,?,?)";
        Connection connection = DriverManager.getConnection(URL, username, password); //Connection conn = connect();

        logger.debug("create connection");

        try (PreparedStatement statement = connection.prepareStatement(sql);) {
            connection.setAutoCommit(false);
            int count = 0;

            for (card card : list) {
                statement.setString(1, card.getCard_no());
                statement.setString(2, card.getIssue_date());
                statement.setString(3, card.getExpire_date());
                statement.setString(4, card.getCard_status());
                statement.setString(5, card.getCust_id());

                statement.addBatch();
                count++;
                // execute every 100 rows or less
                if (count % 100 == 0 || count == list.size()) {
                    statement.executeBatch();
                }
            }
            connection.commit();
            statement.close();

        } catch (SQLException ex) {
            connection.rollback();
            logger.error("Datababse error:");
            logger.error(ex.getMessage());
            logger.warn("rollback insertCard");
            ex.printStackTrace();
            //System.out.println(ex.getMessage());
        } finally {
            connection.close();
            logger.debug("close connection");
            logger.info("insert card master finish");
        }
    }

    public void updateCard(int cust_id, String card_status) throws Exception {

        logger.info("start update card master");

        File file = new File("config.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("config");
        Node node = nodeList.item(0);
        Element eElement = (Element) node;
        String URL = (String) eElement.getElementsByTagName("url").item(0).getTextContent();
        String username = (String) eElement.getElementsByTagName("username").item(0).getTextContent();
        String password = (String) eElement.getElementsByTagName("password").item(0).getTextContent();
        /*
        FileInputStream fis = new FileInputStream("config.properties");
        Properties p = new Properties();
        p.load(fis);
        String URL = (String) p.get("URL");
        String username = (String) p.get("username");
        String password = (String) p.get("password");
         */
        //String sql = "INSERT INTO mydb.card_master(card_no,issue_date,expire_date,card_status,cust_id) " + "VALUES(?,?,?,?,?)";
        String sql = "UPDATE card_master SET card_status = ? WHERE cust_id = ?";

        Connection connection = DriverManager.getConnection(URL, username, password); //Connection conn = connect();

        logger.debug("create connection");
        logger.info("Prepared Statemen to update data: " + "cust_id =" + cust_id);

        try (PreparedStatement statement = connection.prepareStatement(sql);) {
            connection.setAutoCommit(false);

            statement.setString(1, card_status);
            statement.setInt(2, cust_id);
            statement.execute();

            connection.commit();
            statement.close();

        } catch (SQLException ex) {
            connection.rollback();
            logger.error("Datababse error:");
            logger.warn(ex.getMessage());
            logger.warn("rollback updateCard");
            //System.out.println(ex.getMessage());
        } finally {
            connection.close();
            logger.debug("close connection");
            logger.info("update card master finish");
        }
    }

    public void deleteCard(int cust_id) throws Exception {

        logger.info("start delete card master");

        File file = new File("config.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("config");
        Node node = nodeList.item(0);
        Element eElement = (Element) node;
        String URL = (String) eElement.getElementsByTagName("url").item(0).getTextContent();
        String username = (String) eElement.getElementsByTagName("username").item(0).getTextContent();
        String password = (String) eElement.getElementsByTagName("password").item(0).getTextContent();
        /*
        FileInputStream fis = new FileInputStream("config.properties");
        Properties p = new Properties();
        p.load(fis);
        String URL = (String) p.get("URL");
        String username = (String) p.get("username");
        String password = (String) p.get("password");
         */
        //String sql = "INSERT INTO mydb.card_master(card_no,issue_date,expire_date,card_status,cust_id) " + "VALUES(?,?,?,?,?)";
        String sql = "DELETE FROM card_master WHERE cust_id = ?";

        Connection connection = DriverManager.getConnection(URL, username, password); //Connection conn = connect();

        logger.debug("create connection");
        logger.info("Prepared Statemen to delete data: " + "cust_id =" + cust_id);

        try (PreparedStatement statement = connection.prepareStatement(sql);) {
            connection.setAutoCommit(false);

            statement.setInt(1, cust_id);
            statement.execute();

            connection.commit();
            statement.close();

        } catch (SQLException ex) {
            connection.rollback();
            logger.error("Datababse error:");
            logger.warn(ex.getMessage());
            logger.warn("rollback deleteCard");
            //System.out.println(ex.getMessage());
        } finally {
            connection.close();
            logger.debug("close connection");
            logger.info("delete card master finish");
        }
    }

    public void export() throws Exception {

        logger.info("start export custumer");

        File file = new File("config.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("config");
        Node node = nodeList.item(0);
        Element eElement = (Element) node;
        String URL = (String) eElement.getElementsByTagName("url").item(0).getTextContent();
        String username = (String) eElement.getElementsByTagName("username").item(0).getTextContent();
        String password = (String) eElement.getElementsByTagName("password").item(0).getTextContent();
        /*
        FileInputStream fis = new FileInputStream("config.properties");
        Properties p = new Properties();
        p.load(fis);
        String URL = (String) p.get("URL");
        String username = (String) p.get("username");
        String password = (String) p.get("password");
         */
        String excelFilePath = "Report_1.xlsx";
        String csv_path = "report_csv.csv";
        String txt_path = "report_txt.txt";

        try (Connection connection = DriverManager.getConnection(URL, username, password)) {

            //logger.debug("create connection");
            String sql = "SELECT custumer.* , card_master.*\n" + "FROM mydb.custumer\n" + "INNER JOIN mydb.card_master ON mydb.custumer.cust_id = mydb.card_master.cust_id\n" + "WHERE card_status ='active';";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Report_1");
            XSSFCellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.LIME.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFCellStyle style2 = workbook.createCellStyle();
            style2.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            ArrayList<custumer> re = getData(result);

            writeHeaderLine(style, sheet);
            writeDataLines(style, style2, workbook, sheet, re);

            FileOutputStream outputStream = new FileOutputStream(excelFilePath);
            workbook.write(outputStream);

            File file_csv = new File(csv_path);
            //FileWriter writer_csv = new FileWriter(csv_path);
            //FileWriter writer_csv = new FileWriter(file_csv, true);       //True = Append to file, false = Overwrite
            BufferedWriter bufferwriter_csv = new BufferedWriter(new FileWriter(csv_path));
            writeCSV(bufferwriter_csv, re);

            File file_txt = new File(txt_path);
            //FileWriter writer_txt = new FileWriter(txt_path);
            //FileWriter writer_txt = new FileWriter(txt_path, true);       //True = Append to file, false = Overwrite
            BufferedWriter bufferwriter_txt = new BufferedWriter(new FileWriter(txt_path));
            writeTXT(bufferwriter_txt, re);

            JasperDesign jasperDesign = JRXmlLoader.load("CustomerReport.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            //Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, getReportPatameters(), connection);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "CustomerReport.pdf");

            workbook.close();
            //writer_csv.close();
            bufferwriter_csv.close();
            //writer_txt.close();
            bufferwriter_txt.close();
            statement.close();
            result.close();

            connection.close();
            logger.debug("close connection");

        } catch (SQLException e) {
            logger.error("Datababse error:");
            //logger.error(e.getMessage());
            //System.out.println("Datababse error:");
            e.printStackTrace();
        }
        /*catch (IOException e) {
            logger.error("File IO error:");
            logger.error(e.getMessage());
            //System.out.println("File IO error:");
            e.printStackTrace();
        }*/

        logger.info("export custumer finish");
    }

    private static Map<String, Object> getReportPatameters() {
        Map<String, Object> parameters = new HashMap<>();
        return parameters;
    }

    public void writeHeaderLine(XSSFCellStyle style, XSSFSheet sheet) {

        for (int i = 0; i < 10; i++) {
            sheet.setColumnWidth(i, 14 * 256);
            //sheet.setTabColor(color);
        }
        sheet.setColumnWidth(6, 17 * 256);

        Row headerRow = sheet.createRow(0);
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("cust_id");
        headerCell.setCellStyle(style);

        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("cust_name");
        headerCell.setCellStyle(style);

        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("cust_lname");
        headerCell.setCellStyle(style);

        headerCell = headerRow.createCell(3);
        headerCell.setCellValue("cust_citizenid");
        headerCell.setCellStyle(style);

        headerCell = headerRow.createCell(4);
        headerCell.setCellValue("cust_address");
        headerCell.setCellStyle(style);

        headerCell = headerRow.createCell(5);
        headerCell.setCellValue("cust_dob");
        headerCell.setCellStyle(style);

        headerCell = headerRow.createCell(6);
        headerCell.setCellValue("card_no");
        headerCell.setCellStyle(style);

        headerCell = headerRow.createCell(7);
        headerCell.setCellValue("issue_date");
        headerCell.setCellStyle(style);

        headerCell = headerRow.createCell(8);
        headerCell.setCellValue("expire_date");
        headerCell.setCellStyle(style);

        headerCell = headerRow.createCell(9);
        headerCell.setCellValue("card_status");
        headerCell.setCellStyle(style);
        //headerCell = headerRow.createCell(10);
        //headerCell.setCellValue("cust_id");

        logger.info("write Header Line in Excel");

    }

    public void writeDataLines(XSSFCellStyle style, XSSFCellStyle style2, XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<custumer> arr) throws SQLException {

        for (int i = 0; i < arr.size(); i++) {

            if (i % 2 == 0) {
                Row row = sheet.createRow(i + 1);
                Cell cell = row.createCell(0);
                cell.setCellValue(arr.get(i).getId());
                cell.setCellStyle(style2);

                Cell cell_name = row.createCell(1);
                cell_name.setCellValue(arr.get(i).getName());
                cell_name.setCellStyle(style2);

                Cell cell_lname = row.createCell(2);
                cell_lname.setCellValue(arr.get(i).getLname());
                cell_lname.setCellStyle(style2);

                Cell cell_citizenid = row.createCell(3);
                cell_citizenid.setCellValue(arr.get(i).getCitizenid());
                cell_citizenid.setCellStyle(style2);

                Cell cell_address = row.createCell(4);
                cell_address.setCellValue(arr.get(i).getAddress());
                cell_address.setCellStyle(style2);

                Cell cell_dob = row.createCell(5);
                cell_dob.setCellValue(arr.get(i).getDob());
                cell_dob.setCellStyle(style2);

                Cell cell_card_no = row.createCell(6);
                cell_card_no.setCellValue(arr.get(i).getCard_no());
                cell_card_no.setCellStyle(style2);

                Cell cell_issue_date = row.createCell(7);
                cell_issue_date.setCellValue(arr.get(i).getIssue_date());
                cell_issue_date.setCellStyle(style2);

                Cell cell_expire_date = row.createCell(8);
                cell_expire_date.setCellValue(arr.get(i).getExpire_date());
                cell_expire_date.setCellStyle(style2);

                Cell cell_card_status = row.createCell(9);
                cell_card_status.setCellValue(arr.get(i).getCard_status());
                cell_card_status.setCellStyle(style2);
            } else {
                Row row = sheet.createRow(i + 1);
                Cell cell = row.createCell(0);
                cell.setCellValue(arr.get(i).getId());
                cell.setCellStyle(style);

                Cell cell_name = row.createCell(1);
                cell_name.setCellValue(arr.get(i).getName());
                cell_name.setCellStyle(style);

                Cell cell_lname = row.createCell(2);
                cell_lname.setCellValue(arr.get(i).getLname());
                cell_lname.setCellStyle(style);

                Cell cell_citizenid = row.createCell(3);
                cell_citizenid.setCellValue(arr.get(i).getCitizenid());
                cell_citizenid.setCellStyle(style);

                Cell cell_address = row.createCell(4);
                cell_address.setCellValue(arr.get(i).getAddress());
                cell_address.setCellStyle(style);

                Cell cell_dob = row.createCell(5);
                cell_dob.setCellValue(arr.get(i).getDob());
                cell_dob.setCellStyle(style);

                Cell cell_card_no = row.createCell(6);
                cell_card_no.setCellValue(arr.get(i).getCard_no());
                cell_card_no.setCellStyle(style);

                Cell cell_issue_date = row.createCell(7);
                cell_issue_date.setCellValue(arr.get(i).getIssue_date());
                cell_issue_date.setCellStyle(style);

                Cell cell_expire_date = row.createCell(8);
                cell_expire_date.setCellValue(arr.get(i).getExpire_date());
                cell_expire_date.setCellStyle(style);

                Cell cell_card_status = row.createCell(9);
                cell_card_status.setCellValue(arr.get(i).getCard_status());
                cell_card_status.setCellStyle(style);
            }

            //Cell cell_cust_id = row.createCell(10);
            //cell_cust_id.setCellValue(arr.get(i).getCust_id());
        }

        logger.info("write Data Line in Excel");

    }

    public void writeCSV(BufferedWriter bufferwriter_csv, ArrayList<custumer> arr) throws IOException {

        final String comma = ",";
        //StringBuffer sb_csv = new StringBuffer("cust_id,cust_name,cust_lname,cust_citizenid,cust_address,cust_dob,card_no,issue_date,expire_date,card_status\r\n");
        StringBuilder sb_csv = new StringBuilder("cust_id,cust_name,cust_lname,cust_citizenid,cust_address,cust_dob,card_no,issue_date,expire_date,card_status\r\n");
        for (int i = 0; i < arr.size(); i++) {
            sb_csv.append(arr.get(i).getId());
            sb_csv.append(comma);
            sb_csv.append(arr.get(i).getName());
            sb_csv.append(comma);
            sb_csv.append(arr.get(i).getLname());
            sb_csv.append(comma);
            sb_csv.append(arr.get(i).getCitizenid());
            sb_csv.append(comma);
            sb_csv.append(arr.get(i).getAddress());
            sb_csv.append(comma);
            sb_csv.append(arr.get(i).getDob());
            sb_csv.append(comma);
            sb_csv.append(arr.get(i).getCard_no());
            sb_csv.append(comma);
            sb_csv.append(arr.get(i).getIssue_date());
            sb_csv.append(comma);
            sb_csv.append(arr.get(i).getExpire_date());
            sb_csv.append(comma);
            sb_csv.append(arr.get(i).getCard_status());
            sb_csv.append("\r\n");
        }
        //logger.debug(sb_csv.toString());
        bufferwriter_csv.write(sb_csv.toString());
        logger.info("write CSV file");
    }

    public void writeTXT(BufferedWriter bufferwriter_txt, ArrayList<custumer> arr) throws IOException {

        final String vertical_bar = "|";
        //StringBuffer sb_t = new StringBuffer("cust_id|cust_name|cust_lname|cust_citizenid|cust_address|cust_dob|card_no|issue_date|expire_date|card_status\r\n");
        StringBuilder sb_t = new StringBuilder("cust_id|cust_name|cust_lname|cust_citizenid|cust_address|cust_dob|card_no|issue_date|expire_date|card_status\r\n");
        for (int i = 0; i < arr.size(); i++) {
            sb_t.append(arr.get(i).getId());
            sb_t.append(vertical_bar);
            sb_t.append(arr.get(i).getName());
            sb_t.append(vertical_bar);
            sb_t.append(arr.get(i).getLname());
            sb_t.append(vertical_bar);
            sb_t.append(arr.get(i).getCitizenid());
            sb_t.append(vertical_bar);
            sb_t.append(arr.get(i).getAddress());
            sb_t.append(vertical_bar);
            sb_t.append(arr.get(i).getDob());
            sb_t.append(vertical_bar);
            sb_t.append(arr.get(i).getCard_no());
            sb_t.append(vertical_bar);
            sb_t.append(arr.get(i).getIssue_date());
            sb_t.append(vertical_bar);
            sb_t.append(arr.get(i).getExpire_date());
            sb_t.append(vertical_bar);
            sb_t.append(arr.get(i).getCard_status());
            sb_t.append("\r\n");
        }
        //logger.debug(sb_t.toString());
        bufferwriter_txt.write(sb_t.toString());
        logger.info("write TXT file");
    }

    public ArrayList getData(ResultSet result) throws SQLException {

        ArrayList<custumer> arr = new ArrayList<custumer>();

        while (result.next()) {
            String id = result.getString("cust_id");
            String name = result.getString("cust_name");
            String lname = result.getString("cust_lname");
            String citizenid = result.getString("cust_citizenid");
            String address = result.getString("cust_address");
            String dob = result.getString("cust_dob");

            String card_no = result.getString("card_no");
            String issue_date = result.getString("issue_date");
            String expire_date = result.getString("expire_date");
            String card_status = result.getString("card_status");
            //String cust_id = result.getString("cust_id");

            custumer myObj = new custumer();
            myObj.setId(id);
            myObj.setName(name);
            myObj.setLname(lname);
            myObj.setCitizenid(citizenid);
            myObj.setAddress(address);
            myObj.setDob(dob);
            myObj.setCard_no(card_no);
            myObj.setIssue_date(issue_date);
            myObj.setExpire_date(expire_date);
            myObj.setCard_status(card_status);
            //myObj.setCust_id(cust_id);
            //logger.debug("myObj.toString() : "+myObj.toString());
            arr.add(myObj);
        }
        //Collections.sort(arr, new Sort());
        //logger.debug("arr : "+arr);
        return arr;
    }

    public static void main(String[] args) throws Exception {

        PropertyConfigurator.configure("log4j.properties");

        //Report report = new Report();
        /*
        ArrayList<card> cardlist = new ArrayList();

        cardlist.add(new card("0000000000000001", "2022-04-18", "2024-04-18", "active", "1"));
        cardlist.add(new card("0000000000000002", "2022-05-18", "2024-05-18", "active", "2"));
        cardlist.add(new card("0000000000000003", "2022-06-18", "2024-06-18", "active", "3"));
        cardlist.add(new card("0000000000000004", "2022-02-18", "2024-02-18", "active", "4"));
        cardlist.add(new card("0000000000000005", "2022-03-18", "2024-03-18", "active", "5"));
        cardlist.add(new card("0000000000000006", "2022-04-18", "2022-05-18", "close", "6"));
        cardlist.add(new card("0000000000000007", "2022-05-18", "2022-06-18", "close", "7"));
        cardlist.add(new card("0000000000000008", "2022-06-18", "2022-07-18", "close", "8"));
        cardlist.add(new card("0000000000000009", "2022-02-18", "2022-03-18", "close", "9"));
        cardlist.add(new card("0000000000000010", "2022-03-18", "2022-04-18", "close", "10"));
        cardlist.add(new card("0000000000000011", "2022-03-18", "2022-04-18", "close", "11"));
         */
        //report.insertCard(cardlist);
        //report.updateCard(2, "close");
        //report.updateCard(6, "active");
        //report.deleteCard(3);
        //report.deleteCard(7);
        //report.export();
        /*
        File file = new File("config.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("config");
        Node node = nodeList.item(0);
        Element eElement = (Element) node;
        String URL = (String) eElement.getElementsByTagName("url").item(0).getTextContent();
        String username = (String) eElement.getElementsByTagName("username").item(0).getTextContent();
        String password = (String) eElement.getElementsByTagName("password").item(0).getTextContent();
        */
        String URL = "jdbc:mysql://localhost:3306/mydb";
        String username = "root";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(URL, username, password)) {

            //JasperDesign jasperDesign = JRXmlLoader.load("CustomerReport.jrxml");
            //JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            JasperDesign jasperDesign = JRXmlLoader.load("newreport.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "newreport.pdf");
            
            
            //Map<String, Object> parameters = new HashMap<>();
            
            // Load the report design from a JRXML file
            //JasperReport jasperReport = JasperCompileManager.compileReport("CustomerReport.jrxml");

            // Create a JasperPrint object from the compiled report
            //JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            // Export the report to PDF format
            //JasperExportManager.exportReportToPdfFile(jasperPrint, "CustomerReport.pdf");
        }

    }

}
