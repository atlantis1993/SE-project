package com.color.answer;

import com.color.MusicTools.Mediaplayer;
import com.color.MusicTools.SoundPlayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener {
	private static String Tag = "color";
	private	  Button btn1,btn2,btn3,btn4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("Welcome!");
		SoundPlayer.init(MainActivity.this);
		Mediaplayer.init(MainActivity.this);
		if (Mediaplayer.getplayflag()) {
			Mediaplayer.PlayBackgroundMusic();
		}
		
        btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);
		btn4 = (Button) findViewById(R.id.btn4);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		
	}
	

 public void onClick(View v){
	 switch(v.getId()){
	 case R.id.btn1 :
	       //Log.v(Tag, "btn1");
	      // Toast.makeText(getApplication(), "btn1", Toast.LENGTH_SHORT).show();
	       SoundPlayer.playsound(R.raw.gang2);
			Intent intent1=new Intent(MainActivity.this,SingleModel.class);
			startActivity(intent1);    //进入游戏界面
			// 设置Activity之间的切换效果
			//overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			
	       break;
	 case R.id.btn2 :
	      // Log.v(Tag, "btn2");
	       //Toast.makeText(getApplication(), "btn2", Toast.LENGTH_SHORT).show();
	       SoundPlayer.playsound(R.raw.gang2);
	       Intent intent2 = new Intent(MainActivity.this,OnlineModel.class);
	       startActivity(intent2);
	       break;
	 case R.id.btn3 :
	       //Log.v(Tag, "btn3");
	       //Toast.makeText(getApplication(), "btn3", Toast.LENGTH_LONG).show();
	    // 播放音效
	    			SoundPlayer.playsound(R.raw.gang2);
	    			Intent intent3=new Intent(MainActivity.this,VolumControl.class);
	    			startActivity(intent3);    //进入游戏界面
	    			// 设置Activity之间的切换效果
	    			//overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	    			
	       break;
	 case R.id.btn4 :
	       //Log.v(Tag, "btn4");
	       //Toast.makeText(getApplication(), "btn4", Toast.LENGTH_LONG).show();
	     //用对话框的形式提示开发人员信息
			AlertDialog.Builder builder1=new AlertDialog.Builder(this);   
			builder1.setTitle("开发小组：");
			builder1.setMessage("孙宝锋，徐学华，王淑伟，李波");
           builder1.setNegativeButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					return;
				}
				
			});
           builder1.setCancelable(false);
			AlertDialog dialog1=builder1.create();
			dialog1.show();
		
	       break;
	       default: break;
	 }
	 
 }
 
 @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		SoundPlayer.Releasesoundplayer();// 退出Activity时释放soundpool中的资源
		Mediaplayer.ReleaseMediaplayer();// 退出Activity时释放mediaplayer中的资源
		finish();

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId,MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_settings1:
			onClick(btn1);
			break;
		case R.id.action_settings2:
			onClick(btn2);
			break;
		case R.id.action_settings3:
			onClick(btn3);
			break;
		case R.id.action_settings4:
			onClick(btn4);
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
