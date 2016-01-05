package com.color.answer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SocketClient extends Thread {
	private static String Tag = "Socket";
	Socket socket;
	private Handler outhandler;
	public SocketClient(Socket s,Handler handler){
		this.socket = s; 
		outhandler = handler;
	}
	public void out(String out){
		try {
		      
			Writer outdata = new OutputStreamWriter(socket.getOutputStream(),"GBK");  
			outdata.write(out+"\n");
			outdata.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void close(){
		try {

			socket.close();
			Log.v(Tag, "socket close");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		try {
			Log.v(Tag, "socket built");	
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							socket.getInputStream(), "GBK"));
			String line = null;
			while((line = br.readLine()) != null){
				Log.v(Tag, "client:"+line);	
				Message msg = outhandler.obtainMessage();
				msg.obj = line;
				msg.what = 1;
				outhandler.sendMessage(msg);
				ManageClient.getChatManage().clientpublish(this,line);
			}
			Log.v(Tag, "you");
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