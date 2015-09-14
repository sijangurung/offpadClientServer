package offpadClientServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class clientBrowser
 */
@WebServlet("/clientBrowser")
public class clientBrowser extends HttpServlet {
	// This would be filled from the another class which would be running in the
	// background....

	private static final long serialVersionUID = 1L;
	public static String hashReceived = "nothing", rDest = "Not_Received_Yet",
			rAmt = "000000";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public clientBrowser() {
		super();
		
		// TODO Auto-generated constructor stub
		try {
			String desktop = System.getProperty("user.home") + "/Desktop/";
			Reader file = new FileReader(desktop + "offpad.txt");
			System.out.println("File reading successful!");
			BufferedReader buff = new BufferedReader(file);
			hashReceived = buff.readLine();
			buff.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		// now decrypt it to fill in the forms auto...
		// Here is the decryption part..Sijan Gurung
		int p = 0, lo = 0;
		for (int i = 0; i < hashReceived.length(); i++) {
			if (hashReceived.charAt(i) == '+') {
				p++;
				if(p == 1){
					lo = i;
				rDest = hashReceived.substring(0, i);
				}else if (p == 2) {
					rAmt = hashReceived.substring(lo + 1, i);
					break;
				}
			}
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	/*
		// we do not set content type, headers, cookies etc.
        // resp.setContentType("text/html"); // while redirecting as
        // it would most likely result in an IllegalStateException

        // "/" is relative to the context root (your web-app name)
        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/html/index.html");
        // don't add your web-app name to the path

        view.forward(request, response); 
        */
		// Set response content type
		performTask(request, response); //This method reloads this page once in 15 seconds....
		
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		// String show = rf.retMsg();
		String pathVar = request.getContextPath() + "/WebContent/WEB-INF/css/style.css";
		System.out.println("pathVar: "+pathVar);
		
		
		String title = "Secure Banking Services";
		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 "
				+ "transitional//en\">\n";
		out.println(docType
				+ "<html>\n"
				+ "<head> "
				+ "<link rel='stylesheet' type='text/css' href='" + request.getContextPath() + "/styles/style.css' /> "
				+ "<title class='title'>"
				+ title
				+ "</title></head>\n"
				+ "<body>\n"
				+ "<h1 align=\"center\">"
				+ title
				+ "</h1>\n"
				+ "<center><img width='200px' src='images\\logo_offpad.jpg'/></center>"
				+ "<div class='login' ><form action=\"helloform\" method=\"GET\">"
				+ "Destination Account Number: <input type=\"text\" name=\"destination\" value="
				+ rDest + ">" + "<br />"
				+ "<p>Amount:<input type=\"integer\" name=\"amount\" value="
				+ rAmt + " /><br />"
				+ "<input type=\"hidden\" name=\"hash\" value=" + hashReceived
				+ " />" + "<input type=\"submit\" value=\"Transfer Amount\" />"
				+ "</form>" + "</div>\n" + "</body></html>");
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//After this page is done... remove the values
		//This is for writing dummy values...after wards...
		String desktop = System.getProperty("user.home") + "/Desktop/";
		BufferedWriter writer = null;
		Writer write_file = new FileWriter(desktop+"offpad.txt");
        writer = new BufferedWriter(write_file);
        writer.write("Not_Received_Yet+000000+testing...");
        writer.close();
	}
	private void performTask(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	IOException {
		response.setContentType("text/html");
		response.addHeader("Refresh", "15");
		new clientBrowser();
	}

}
