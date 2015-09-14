package offpadClientServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class helloform
 */
@WebServlet("/helloform")
public class helloform extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public helloform() {
		super();
		// TODO Auto-generated constructor stub
	}

	//This is for generation of the hash......
	public static String md5(String input) {
		String md5 = null;
		if(null == input) return null;
		try {
			//Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");
			//Update input string in message digest
			digest.update(input.getBytes(), 0, input.length());
			//Converts message digest value in base 16 (hex) 
			md5 = new BigInteger(1, digest.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		return md5;
	}//end of the function.....

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// Set response content type


		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "Server Side Data Handling .....";
		String destination=request.getParameter("destination").toString();
		String amount=request.getParameter("amount").toString();
		String hash=request.getParameter("hash");
		String received_hash="something";
		String docType =
				"<!doctype html public \"-//w3c//dtd html 4.0 " +
						"transitional//en\">\n";
		out.println(docType +
				"<html>\n"
				+ "<link rel='stylesheet' type='text/css' href='" + request.getContextPath() + "/styles/style.css' /> "
				+ "<head><title>" + title + "</title></head>\n" +
				"<body bgcolor=\"#f0f0f0\" align='center'>\n" +
				"<h1 align=\"center\">" + title + "</h1>\n" 
				+ "<center><img width='200px' src='images\\logo_offpad.jpg'/></center>"
				+ "<ul class='unstyled'>\n" +
				"  <li><b>Destination Account</b>: "
				+ destination + "\n" +
				"  <li><b>Amount</b>: "
				+ amount + "\n" +
				"</ul>\n");
		//Here is the decryption part..Sijan Gurung
		int p=0,lo=0;
		for(int i=0;i< hash.length() ;i++){
			if(hash.charAt(i)=='+'){
				p++;
				lo=i;
			}
			if(p == 2){
				received_hash= hash.substring(lo+1,hash.length());
				System.out.println("Received Hash = " + received_hash);
				break;
			}
		}
		String generatedHash = md5(destination+amount);

		System.out.println("Generated Hash from client Value = "+ generatedHash);
		out.println(docType+""
				+ "Generated Hash: "+generatedHash + ""
				+ "<br/> Expected Hash:"+received_hash+"<br/><br/>");
		if(received_hash.equals( generatedHash )){
			out.println(docType+""
					+ "<font size=\"5\" color=\"green\">Hash Match</font>");
					//+ "<br/><img width='50px' height='auto' src='images/tick.png' />");
		}
		else{

			out.println(docType+""
					+ "<font size=\"5\" color=\"red\">Hash MisMatch</font>");
					//+ "<br/><img width='50px' height='auto' src='images/cross.png' />");
		}
		//THis Part is for the image...i suppose....

		out.println(docType+""
				+ "</body></html>");
	}
	//THIS FUNCTION IS FOR SHOWING IMAGES.....
	public void show_image(String path_to_Show,HttpServletResponse resp){
		ServletContext cntx= getServletContext();
		// Get the absolute path of the image
		String filename = cntx.getRealPath(path_to_Show);
		// retrieve mimeType dynamically
		String mime = cntx.getMimeType(filename);
		if (mime == null) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		resp.setContentType(mime);
		File file = new File(filename);
		resp.setContentLength((int)file.length());

		FileInputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(file);
			out = resp.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Copy the contents of the file to the output stream
		byte[] buf = new byte[1024];
		int count = 0;
		try {
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
				out.close();
				in.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}//end of the function.....
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
