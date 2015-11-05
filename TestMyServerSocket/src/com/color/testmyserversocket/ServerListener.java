package com.color.testmyserversocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ServerListener extends Thread {
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ServerSocket serverSocket = new ServerSocket(30000);
			while(true){
				//block
				Socket socket = serverSocket.accept();
				//connected
				JOptionPane.showMessageDialog(null, "something connected port 12345");	
				ChatSocket cs = new ChatSocket(socket);
				cs.start();	
				ChatManage.getChatManage().add(cs);
				}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
