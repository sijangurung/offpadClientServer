package offpadClientServer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class RFCOMMServer {


	public  RFCOMMServer(){
		try {

	   StreamConnectionNotifier service = 
                    (StreamConnectionNotifier) Connector.open("btspp://localhost:" 
                            + new UUID("8ce255c0200a11e0ac640800200c9a66",
			false).toString() + ";name=Sample SPP Server");

	    StreamConnection conn = 		     // block for connect
                     (StreamConnection) service.acceptAndOpen();

	    System.out.println("Connected");


        //read string from spp client
        InputStream inStream=conn.openInputStream();
        BufferedReader bReader=new BufferedReader(new InputStreamReader(inStream));
        String lineRead=bReader.readLine();
        if(lineRead == null)
        	System.out.println("No Msg Recovered!!");
        else
        {       System.out.println(lineRead);
                //Now here comes the sending it to server part..
      
        		String desktop = System.getProperty ("user.home") + "/Desktop/";
        		File myFile = new File (desktop + "offpad.txt");
        		BufferedWriter offpad_txt = new BufferedWriter(new FileWriter(myFile)); 
        		offpad_txt.write(lineRead);
        		offpad_txt.close();
        
        }

	  //send response to spp client
        OutputStream outStream=conn.openOutputStream();
        PrintWriter pWriter=new PrintWriter(new OutputStreamWriter(outStream));
        pWriter.write("Response String from SPP Server\r\n");
        pWriter.flush();
  
        pWriter.close();

        
	    conn.close();
	    service.close();

	} catch (IOException e) {	System.err.print(e.toString());   }
//	return messages;
     }//end of 

	public static void main(String args[]) {	
		 new RFCOMMServer();
	}

}