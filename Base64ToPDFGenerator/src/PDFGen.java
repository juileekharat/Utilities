import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import javax.imageio.ImageIO;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import sun.misc.BASE64Decoder;

import javax.xml.parsers.*;

class PDFGen {
	
	public static final String TAG_NAME = "subscriber_doc";
	public static final String FILE_PATH = "C:\\Users\\HP-LIC\\Desktop\\Xml Files\\xml";
	public static final String PNG_FILE_PATH = "C:\\Users\\HP-LIC\\Desktop\\Xml Files\\png";
	public static final String PDF_FILE_PATH = "C:\\Users\\HP-LIC\\Desktop\\Xml Files\\pdf";

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
		File imgOutFile = new File(PNG_FILE_PATH+"\\"+fileName.replace(".xml","")+".png");
		ImageIO.write(bufImg, "png", imgOutFile);
		System.out.println("PNG file created!");
      
		//PNG to PDF Conversion
		String output = PDF_FILE_PATH+"\\"+fileName.replace(".xml","")+".pdf";
		String input = imgOutFile.getAbsolutePath();
		Document document = new Document();
		FileOutputStream fos = new FileOutputStream(output);
		PdfWriter writer = PdfWriter.getInstance(document, fos);
		writer.open();
		document.open();
		Image img = Image.getInstance(input);
		
		int indentation = 0;
		float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
	               - document.rightMargin() - indentation) / img.getWidth()) * 100;
		img.scalePercent(scaler);
		
		document.add(img);
		document.close();
		writer.close();
		System.out.println("PDF file created!");
	}
	
	public static void processFile(String path) throws Throwable{
		File folder = new File(path);
		String[] files = folder.list();		
		for (String file : files)
		{
			File xmlFilePath = new File(path+"\\"+file);
			
			//Get Encoded String from XML File
			String base64String = getPDFString(xmlFilePath);
		
			//Generate PDF 
			generatePDF(base64String,file);
		}	 		
	}
	
	public static void main(String[] args) throws Throwable {
		processFile(FILE_PATH); 
   }
}