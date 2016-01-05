package com.color.answer;

import java.util.Vector;

import android.util.Log;

public class ManageClient {
	private String Tag = "mana";
	 private ManageClient(){}
	 private static final ManageClient cm = new ManageClient();
	 public static ManageClient getChatManage(){
		 return cm;
	 }
 Vector<SocketClient> vector =  new Vector<SocketClient>();
 public void add(SocketClient cs) {
	 vector.add(cs);
	
}
 public void  serverpublish(String[] out) {
	 for (int i = 0; i <vector.size(); i++) {
		 SocketClient csChatSocket = vector.get(i);
		 for(int j=0; j<out.length; j++){
			 csChatSocket.out("20."+out[j]);
		 }
		
	}
 }
 public void  server_send_msg(String out) {
	 for (int i = 0; i <vector.size(); i++) {
		 SocketClient csChatSocket = vector.get(i);
			 csChatSocket.out(out);
			Log.v(Tag, out); 
	}
 }
 public void  clientpublish(SocketClient cs,String out) {
	 for (int i = 0; i <vector.size(); i++) {
		 SocketClient csChatSocket = vector.get(i);
			if (!cs.equals(csChatSocket)) {
					 csChatSocket.out(out);
			}
	}
 }
 public int getSize(){
		if(vector.size()>0){
		return vector.size()+1;
		}
		else
			return 1;
	 }
public void closeClient(){
	
	 for (int i = 0; i <vector.size(); i++) {
		 SocketClient csChatSocket = vector.get(i);
			csChatSocket.close();
			 csChatSocket.out("close");
	}
}

public void clientClose(SocketClient cs){
	
	 for (int i = 0; i <vector.size(); i++) {
		 SocketClient csChatSocket = vector.get(i);
			if (cs.equals(csChatSocket)) {
					 csChatSocket.close();
					 vector.remove(i);
			}
		}
}

}
