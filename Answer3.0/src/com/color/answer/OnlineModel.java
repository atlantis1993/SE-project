package com.color.answer;

import java.lang.reflect.Method;
import java.util.List;

import com.color.server.WifiAdmin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OnlineModel extends Activity implements OnClickListener {
	private Button builder,joiner,start,cancel,join;
	private EditText hotpot,psw,username;
	private TextView text1;
	private WifiManager wifiManager;
	private static String Tag = "wificon";
	private String hotname,password,name;
	private  WifiAdmin wifiAdmin ;
	private List<ScanResult> list;    
	private ScanResult mScanResult;    
	private StringBuffer sb=new StringBuffer();
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
		hotpot = (EditText)findViewById(R.id.edit1);
		psw = (EditText)findViewById(R.id.edit2);
		username = (EditText)findViewById(R.id.username);
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
			hotpot.setVisibility(View.VISIBLE);
		//	psw.setVisibility(View.VISIBLE);
			username.setVisibility(View.VISIBLE);
			start.setVisibility(View.VISIBLE);
			cancel.setVisibility(View.VISIBLE);
			joiner.setVisibility(View.GONE);
			setWifiApEnabled(false);
			break;
		case R.id.btn2 :
			//enter the client activity
			builder.setVisibility(View.GONE);
			//hotpot.setVisibility(View.VISIBLE);
			//psw.setVisibility(View.VISIBLE);
			username.setVisibility(View.VISIBLE);
			join.setVisibility(View.VISIBLE);
			cancel.setVisibility(View.VISIBLE);
			text1.setVisibility(View.VISIBLE);
			text1.setText("make sure you have linked the hot_pot");

			break;
		case R.id.start :
			//hot_point
			//如果是打开状态就关闭，如果是关闭就打开
			hotname =  hotpot.getText().toString();
			password = psw.getText().toString();
			name = username.getText().toString();
			if((hotname.length()>1)&&(password.length()>7)&&(name.length()>1)){
				if(setWifiApEnabled(true))
				{
					String data =hotname+"密码."+name ;
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
			    name=username.getText().toString();
			    if((name.length()>1)){
			     String data = "joiner." + name;
				Intent intent2 = new Intent(OnlineModel.this,Client.class);
				intent2.putExtra("name", data);
				startActivity(intent2);
				}else{
					Toast.makeText(getApplication(), "Failed,make sure..", Toast.LENGTH_LONG).show();	
				}
			break;
		case R.id.cancel :
			hotpot.setVisibility(View.GONE);
			psw.setVisibility(View.GONE);
			start.setVisibility(View.GONE);
			join.setVisibility(View.GONE);
			username.setVisibility(View.GONE);
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

	public void connectWifi(){
		wifiAdmin = new WifiAdmin(this);  
	//	wifiAdmin.openWifi(); 
		Log.v(Tag, "wifi open");
		
		//wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo(hotname, password, 3)); 
	}

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
			//apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK); //  WPA_PSK格式 ,默认额情况为不验证
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
