package com.color.answer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
public class ServerListener extends Thread {
	private static String Tag = "severlistener";
	ServerSocket serverSocket;
	private Boolean flag = true;
	 Handler outHandler;
	public ServerListener(Handler handler){
		outHandler = handler;
	}
	public void endServerThread(){	
		flag = false;	
	}

	@Override
	public void run(){
	
		flag = true;
		try {
			ServerSocket serverSocket = new ServerSocket(30000);

			while(flag){	
				Log.v(Tag, "serversocket build");
				Socket socket = serverSocket.accept();
				if(flag){
				SocketClient cs = new SocketClient(socket,outHandler);
				cs.start();	
				ManageClient.getChatManage().add(cs);
				int number = ManageClient.getChatManage().getSize();
				Message msg = outHandler.obtainMessage();
				msg.obj = "client connected";
				msg.arg1 = number;
				msg.what = 2;
				Log.v(Tag, Integer.toString(number));
				outHandler.sendMessage(msg);
				}
			}
			serverSocket.close();
			if(serverSocket.isClosed()){
				Log.v(Tag, "server close!!");	
				ManageClient.getChatManage().closeClient();//关闭所有的socket
			}
		
	      }
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

