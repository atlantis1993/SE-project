package com.color.testmyserversocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class ChatSocket extends Thread {
	Socket socket;
       public ChatSocket(Socket s){
    	  this.socket = s; 
       }
       public void out(String out){
    	   try {
    		   
    		  
				PrintWriter outdata = new PrintWriter(
						new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
				outdata.println("you send--" + out);
		//	socket.getOutputStream().write(out.getBytes("UTF-8"));
			 
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       }
       @Override
    public void run() {
    	   try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							socket.getInputStream(), "UTF-8"));
			String line = null;
			  while((line = br.readLine()) != null){
				  ChatManage.getChatManage().publish(this, line);
			       
	           }
			  br.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
	
}
