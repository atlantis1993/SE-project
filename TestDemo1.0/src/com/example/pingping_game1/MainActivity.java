package com.example.pingping_game1;

import com.example.pingping_game1.MusicTools.Mediaplayer;
import com.example.pingping_game1.MusicTools.SoundPlayer;

import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

	private Button button;
	private Button button2;
	private Button button3;

	private CharSequence items[] = { "��ʼ��Ϸ", "��Ч����", "������Ϸ" };

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
		button = (Button) this.findViewById(R.id.button1);
		button.setOnClickListener(this);
		button2 = (Button) this.findViewById(R.id.button2);
		button2.setOnClickListener(this);
		button3 = (Button) this.findViewById(R.id.button3);
		button3.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		SoundPlayer.Releasesoundplayer();// �˳�Activityʱ�ͷ�soundpool�е���Դ
		Mediaplayer.ReleaseMediaplayer();// �˳�Activityʱ�ͷ�mediaplayer�е���Դ
		finish();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_settings1:
			onClick(button);
			break;
		case R.id.action_settings2:
			onClick(button2);
			break;
		case R.id.action_settings3:
			onClick(button3);
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			// ������Ч
			SoundPlayer.playsound(R.raw.gang2);
			Intent intent1=new Intent(MainActivity.this,GameActivity1.class);
			startActivity(intent1);    //������Ϸ����
			// ����Activity֮����л�Ч��
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			break;
		case R.id.button2:
			Intent intent2 = new Intent(MainActivity.this, VolumControl.class);
			startActivity(intent2);
			// ����Activity֮����л�Ч��
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			break;
		case R.id.button3:
			//�öԻ������ʽ��ʾȷ��Ҫ��Ҫ�˳���Ϸ
			AlertDialog.Builder builder=new AlertDialog.Builder(this);   
			builder.setTitle("��ʾ");
			builder.setMessage("��ȷ��Ҫ�˳���Ϸô��");
			builder.setPositiveButton("ȷ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					onBackPressed();// ���ð����ؼ�ʱ�������¼�����
				}
			});
			builder.setNegativeButton("ȡ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					return;
				}
			});
			builder.setCancelable(false);
			AlertDialog dialog=builder.create();
			dialog.show();
			break;

		}
	}

}
