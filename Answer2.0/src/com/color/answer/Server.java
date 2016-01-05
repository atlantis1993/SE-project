package com.color.answer;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Server  extends Activity{
	private Button btn1,btn2,btn3,btn4,btn5;
	private TextView txt1;
	private static String Tag = "color";
	private ServerListener server;
	private Handler handlerRec;
	private String[] message;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server);
		btn1 = (Button)findViewById(R.id.btn1);
		btn2 = (Button)findViewById(R.id.btn2);
		btn3 = (Button)findViewById(R.id.btn3);
		btn4 = (Button)findViewById(R.id.btn4);
		btn5 = (Button)findViewById(R.id.btn5);
		txt1 = (TextView)findViewById(R.id.txt1);
		txt1.setText(getLocalIpAddress()+"and30000\n");
  message = new String[]{"这是题目","这是A选项里面的内容","这是B选项里面的内容","这是C选项里面的内容","这是D选项里面的内容" };
  //  message= new String[]{"this is question","This is answer A","This is answer B","This is answer C","This is answer D" };
		handlerRec = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if (msg.obj != null) {
					String s = msg.obj.toString();
					txt1.append("server:" + s);
				}
			}
		};


		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.v(Tag, "thread begin");
				server =  new ServerListener(handlerRec);
				server.start();
				Log.v(Tag, "thread2");
				Toast.makeText(getApplication(), "thread start!", Toast.LENGTH_LONG).show();
			}
		});

		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				server.endThread();
				ManageClient.getChatManage().closeClient();
				Toast.makeText(getApplication(), "thread stop!", Toast.LENGTH_LONG).show();
			}
		});
		btn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			//	ManageClient.getChatManage().publish("你好！我是服务器！1234abf\n");
				ManageClient.getChatManage().serverpublish(message);

				Toast.makeText(getApplication(), "send message!", Toast.LENGTH_LONG).show();
			}
		});
		btn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		btn5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				txt1.setText(null);
				txt1.setText(getLocalIpAddress()+"and30000\n");
			}
		});
	}

	public String getLocalIpAddress() {
		try {  

			for (Enumeration<NetworkInterface> en = NetworkInterface  

					.getNetworkInterfaces(); en.hasMoreElements();) {  

				NetworkInterface intf = en.nextElement();  

				for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr  

						.hasMoreElements();) {  

					InetAddress inetAddress = ipAddr.nextElement();  
					// ipv4地址  
					if (!inetAddress.isLoopbackAddress()  
							&& InetAddressUtils.isIPv4Address(inetAddress  
									.getHostAddress())) {  

						return inetAddress.getHostAddress();  

					}  

				}  

			}  

		} catch (Exception ex) {  

		}  

		return null;  

	}







	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
