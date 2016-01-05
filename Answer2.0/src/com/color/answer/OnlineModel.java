package com.color.answer;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OnlineModel extends Activity implements OnClickListener {
	private Button builder,joiner,start,cancel,join;
	private EditText name,psw;
	private TextView text1;
	private WifiManager wifiManager;
	private boolean flag=false;
	private String hotname,password;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online);
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		builder = (Button)findViewById(R.id.btn1);
		joiner = (Button)findViewById(R.id.btn2);
		start = (Button)findViewById(R.id.start);
		join = (Button)findViewById(R.id.join);
		cancel = (Button)findViewById(R.id.cancel);
		name = (EditText)findViewById(R.id.edit1);
		psw = (EditText)findViewById(R.id.edit2);
		text1 = (TextView)findViewById(R.id.text1);
		builder.setOnClickListener(this);
		joiner.setOnClickListener(this);
		start.setOnClickListener(this);
		join.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
	}

	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn1 :
			//enter the server activity
			// Intent intent = new Intent(OnlineModel.this,Server.class);
			//  startActivity(intent);
			name.setVisibility(View.VISIBLE);
			psw.setVisibility(View.VISIBLE);
			start.setVisibility(View.VISIBLE);
			cancel.setVisibility(View.VISIBLE);
			joiner.setVisibility(View.GONE);
			setWifiApEnabled(false);
			break;
		case R.id.btn2 :
			//enter the client activity
			builder.setVisibility(View.GONE);
			name.setVisibility(View.VISIBLE);
			psw.setVisibility(View.VISIBLE);
			join.setVisibility(View.VISIBLE);
			cancel.setVisibility(View.VISIBLE);
			text1.setVisibility(View.VISIBLE);
			text1.setText("make sure you have linked the hot_pot");
			
			break;
		case R.id.start :
			//hot_point
			//如果是打开状态就关闭，如果是关闭就打开
			hotname=name.getText().toString();
			password = psw.getText().toString();
			if((hotname.length()>1)&&(password.length()>7)){
			if(setWifiApEnabled(true))
			{
				String data ="热点名称："+hotname+"密码"+ password ;
			Toast.makeText(getApplication(), "open hotpot success", Toast.LENGTH_LONG).show();
			Intent intent3 = new Intent(OnlineModel.this,Client.class);
			intent3.putExtra("name", data);
			startActivity(intent3);
			}
			else{
			Toast.makeText(getApplication(), "Failed", Toast.LENGTH_LONG).show();	}}
			else{
				Toast.makeText(getApplication(), "Failed,make sure..", Toast.LENGTH_LONG).show();	
			}
			break;
		case R.id.join :
			String data = "joiner";
			Intent intent2 = new Intent(OnlineModel.this,Client.class);
			intent2.putExtra("name", data);
			startActivity(intent2);
			break;
		case R.id.cancel :
			name.setVisibility(View.GONE);
			psw.setVisibility(View.GONE);
			start.setVisibility(View.GONE);
			join.setVisibility(View.GONE);
			cancel.setVisibility(View.GONE);
			joiner.setVisibility(View.VISIBLE);
			builder.setVisibility(View.VISIBLE);
			text1.setText(null);
			setWifiApEnabled(false);
			Toast.makeText(getApplication(), "close hotpot", Toast.LENGTH_LONG).show();
			break;
		default: break;
		}

	};

	
	public boolean setWifiApEnabled(boolean enabled) {
		if (enabled) { // disable WiFi in any case
			//wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
			wifiManager.setWifiEnabled(false);
		}
		try {
			//热点的配置类
			WifiConfiguration apConfig = new WifiConfiguration();
			//配置热点的名称(可以在名字后面加点随机数什么的)
			apConfig.SSID = hotname;
			//配置热点的密码
			apConfig.preSharedKey=password;
			apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK); 
			//通过反射调用设置热点
			Method method = wifiManager.getClass().getMethod(
					"setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
			//返回热点打开状态
			return (Boolean) method.invoke(wifiManager, apConfig, enabled);
		} catch (Exception e) {
			return false;
		}
	}

}
