package com.color.testmyserversocket;

import java.util.Vector;

import org.omg.CORBA.PRIVATE_MEMBER;

public class ChatManage {
     private ChatManage(){}
    	 private static final ChatManage cm = new ChatManage();
    	 public static ChatManage getChatManage(){
    		 return cm;
    	 }
     Vector<ChatSocket> vector =  new Vector<ChatSocket>();
     public void add(ChatSocket cs) {
    	 vector.add(cs);
		
	}
     public void  publish(ChatSocket cs,String out) {
    	 for (int i = 0; i <vector.size(); i++) {
			ChatSocket csChatSocket = vector.get(i);
			if (!cs.equals(csChatSocket)) {
				csChatSocket.out(out);
				
			}
		}
		
	}
}
