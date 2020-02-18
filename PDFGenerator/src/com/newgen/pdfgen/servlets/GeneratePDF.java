package com.newgen.pdfgen.servlets;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import sun.misc.BASE64Decoder;

@WebServlet("/GeneratePDF")
public class GeneratePDF extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String PMMDY_NO_PREFIX= "7000000";
	private static final String TAG_NAME = "subscriber_doc";
	private static final String FILE_PATH = "C:\\Users\\HP-LIC\\Desktop\\Sample Xml Files";
	private static final String PNG_FILE_PATH = "C:\\Users\\HP-LIC\\Desktop\\png";
	private static final String PDF_FILE_PATH = "C:\\Users\\HP-LIC\\Desktop\\pdf";
	private static String responseMsg = "";
	private static String pdfDisplayPath = "";
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pmmdyNoSuffix = request.getParameter("pmmdyNumber");
		String pmmdyNo = PMMDY_NO_PREFIX+""+pmmdyNoSuffix;
		try {
			processFile(FILE_PATH,pmmdyNo);
			
			request.setAttribute("responseMsg", responseMsg);
			request.setAttribute("pdfDisplayPath", pdfDisplayPath);
	        request.getRequestDispatcher("/PDFGen.jsp").forward(request, response);  
	        
		} catch (Throwable e) {
			e.printStackTrace();
		} 	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public static void processFile(String path,String pmmdyNo) throws Throwable{
		File folder = new File(path);
		String[] files = folder.list();	
		File xmlFilePath =  null;
		for (String file : files)
		{
			System.out.println("file : "+file);
			String filename = file.substring(0,12);
			if(pmmdyNo.equalsIgnoreCase(filename))
				xmlFilePath = new File(path+"\\"+file);
		}
		
		if(xmlFilePath != null) {
				//Get Base64 String from XML File
			String base64String = getPDFString(xmlFilePath);
				//Generate PDF 
			generatePDF(base64String,pmmdyNo);
		} else { 
			System.out.println("Invalid File Name");
			responseMsg = "Invalid File Name";
		}
	}
	
	public static String getPDFString(File xmlFilePath) throws Throwable{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
		DocumentBuilder db = dbf.newDocumentBuilder();  
		org.w3c.dom.Document xmlDoc = db.parse(xmlFilePath);  
		String string = xmlDoc.getElementsByTagName(TAG_NAME).item(0).getTextContent();
		return string;
	}

	public static void generatePDF(String base64String, String fileName) throws Throwable{
		
		//Base64 String to PNG Conversion
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] imgBytes = decoder.decodeBuffer(base64String);        
        BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(imgBytes));
		File imgOutFile = new File(PNG_FILE_PATH+"\\"+fileName+".png");
		ImageIO.write(bufImg, "png", imgOutFile);
		System.out.println("PNG file created!");
      
		//PNG to PDF Conversion
		String outputPath = PDF_FILE_PATH+"\\"+fileName+".pdf";
		String inputPath = imgOutFile.getAbsolutePath();
		Document document = new Document();
		FileOutputStream fos = new FileOutputStream(outputPath);
		PdfWriter writer = PdfWriter.getInstance(document, fos);
		writer.open();
		document.open();
		
		//Create image to add in document
		Image img = getImage(inputPath,document);
		
		document.add(img);
		document.close();
		writer.close();
		System.out.println("PDF file created!");
		responseMsg = "PDF Generated Successfully!";
		pdfDisplayPath = outputPath;
	}
	
	public static Image getImage(String input, Document document) throws Throwable{
		Image img = Image.getInstance(input);
		int indentation = 0;
		float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
	               - document.rightMargin() - indentation) / img.getWidth()) * 100;
		img.scalePercent(scaler);
		return img;
	}
	
}
