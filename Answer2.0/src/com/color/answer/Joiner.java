package com.color.answer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Joiner extends Thread{
	private static String Tag = "joiner";
	private Socket socket;
	String buffer = ""; 
	 Handler outHandler;
	public Joiner(Handler handler){
		outHandler = handler;
	}
	
	public void sendmess(String[] out){
		 for(int j=0; j<out.length; j++){
			this.out(out[j]);
		 }
		Log.v(Tag, "clinet send success!!");
	}
	public void out(String data){
		try {
		      
			Writer outdata = new OutputStreamWriter(socket.getOutputStream(),"GBK");  
			outdata.write(data+"\n");
			outdata.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
   public void run() {
	    
		 socket = new Socket();  
		 Log.v(Tag, "connecting");
	     try {
			socket.connect(new InetSocketAddress("192.168.43.1", 30000), 5000);
			//socket.connect(new InetSocketAddress("192.168.43.196", 30000), 5000);
			 Log.v(Tag, "connecting success");
		     //获取输入输出流  
			 Writer writer = new OutputStreamWriter(socket.getOutputStream());  
		      BufferedReader br = new BufferedReader(
						new InputStreamReader(
								socket.getInputStream(), "GBK"));
				String line = null;
				while((line = br.readLine()) != null){
					Log.v(Tag, "client:"+line);	
					Message msg = outHandler.obtainMessage();
					msg.obj = line;
					msg.what = 1;
					outHandler.sendMessage(msg);
				}
				Log.v(Tag, "you");
				br.close();
				//关闭各种输入输出流
              writer.close();


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

   }
   }

	
