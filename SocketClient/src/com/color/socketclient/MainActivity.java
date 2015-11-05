package com.color.socketclient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;  

public class MainActivity extends Activity {  

	String buffer = "";  
	TextView txt1;  
	Button send;  
	EditText ed1; 
	EditText ipaddr; 
	String geted1;
	String getip; 
	private static String Tag = "color";

	public Handler myHandler = new Handler() {  
		@Override  
		public void handleMessage(Message msg) {  
			if (msg.what == 0x11) {  
				Bundle bundle = msg.getData();  
				txt1.append("server:"+bundle.getString("msg")+"\n");  
			}  
		}  

	};  

	@Override  
	protected void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.activity_main);  
		txt1 = (TextView) findViewById(R.id.txt1);  
		send = (Button) findViewById(R.id.send);  
		ed1 = (EditText) findViewById(R.id.ed1); 
		ipaddr = (EditText) findViewById(R.id.ipaddr); 

		send.setOnClickListener(new OnClickListener() {  

			@Override  
			public void onClick(View v) {  
				getip = ipaddr.getText().toString();
				geted1 = ed1.getText().toString();  
				txt1.append("��:"+geted1+"\n");  
				//�����߳� ����������ͺͽ�����Ϣ  
				new MyThread(geted1).start();  
			}  
		});
	}

	class MyThread extends Thread {  

		public String txt1;  

		public MyThread(String str) {  
			txt1 = str;  
		}  

		@Override  
		public void run() {  
			Message msg = new Message(); 
			msg.what = 0x11;  
			Bundle bundle = new Bundle();  
			bundle.clear();  
			try {  
				//���ӷ����� ���������ӳ�ʱΪ5��  
				Socket	socket = new Socket();  
				socket.connect(new InetSocketAddress(getip, 30000), 5000);
				
				//���������������
				Writer writer = new OutputStreamWriter(socket.getOutputStream());  
				writer.write(txt1+"\n");  
			//	writer.write("eof\n");  
				writer.flush(); 			
				//��ȡ������������Ϣ  
				BufferedReader bff = new BufferedReader(new InputStreamReader(  
						socket.getInputStream()));  
			
				String line = bff.readLine();
				Log.v(Tag, "finishread");
				bundle.putString("msg", line.toString());  
				msg.setData(bundle);  
				//������Ϣ �޸�UI�߳��е����  
				myHandler.sendMessage(msg); 
				Log.v(Tag, line);
				//�رո������������
			//	bff.close();  
				//writer.close();
				//socket.close();  
			}  catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  
	} 



}