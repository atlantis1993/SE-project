package com.color.answer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
	private String userName;
	String buffer = ""; 
	 Handler outHandler;
	public Joiner(Handler handler,String joiner){
		outHandler = handler;
		userName = joiner;
	}
	public void joinerclose(){
		try {
			Log.v(Tag, "start close"); 
			socket.close();
			if(socket.isClosed())  
				Log.v(Tag, userName+"close"); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			// writer.write(userName+"\n");
			// writer.flush();
		      BufferedReader br = new BufferedReader(
						new InputStreamReader(
								socket.getInputStream(), "GBK"));
				String line = null;
				while((line = br.readLine()) != null){
					if(line.equals("exit-/"))
					{
						joinerclose();
						break;
					}
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
             if(!(socket.isConnected())){
            	 joinerclose();
             }

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

   }
   }

	
