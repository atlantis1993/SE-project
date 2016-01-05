package com.color.answer;

import java.util.Vector;

public class ManageClient {
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
			 csChatSocket.out(out[j]);
		 }
	
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


}
