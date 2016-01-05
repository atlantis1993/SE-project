package com.color.answer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.color.tools.JudgeAnswer;
import com.color.tools.MakeIntToString;
import com.color.getsqldatabase.getquestion;
import com.color.getsqldatabase.getsqldatabase;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.style.BulletSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SingleModel extends Activity implements OnClickListener {

	public SingleModel() {
		// TODO Auto-generated constructor stub
	}

	private TextView stateView, stateprogressView, questionView; // ����״̬��Ϣ
	private Button aswA, aswB, aswC, aswD; // 4����ѡ�ť
	private ProgressBar timeprogress; // ʱ�������
	private int wr = 0; // ��������
	private int tr = 0; // ��Ե�����
	private int qnumber = 1; // ��ǰ��Ŀ�����
	private int statenum = 1; // ��ǰ����
	private final static int sum = 8; // �ܹ���Ҫ��Ե�����
	private final static int wrsum = 3; // �ܹ��ɴ��Ĵ���
	private final static int LASTSTATE = 2; // ���չ���
	private final static int CHANGE_QUESTION = 1; // �任��Ϸ������Ŀ�ı�ʶ��
	private final static int SETPROGRESS = 2; // ��ʾ����ʱ��������ı�ʶ��
	private final static int RESTARTGAME = 3; // ���¿�ʼ��Ϸ�ı�ʶ��
	private static boolean OVERTIME = false; // �Ƿ��Ѿ���ʱ��ʶ��
	// ��mainMap���洢�����Ӧ����Ϣ
	private Map<String, String> mainMap = new HashMap<String, String>();
	private boolean flag = false; // �����Ƿ���
	private int progressBarValue = 0; // ��ʾʱ��������Ľ���
	private final static int TOTALPROGRESS = 30; // ����ʱ������������ֵ
	private Timer timer; // ����һ����ʱ��
	private Random random = new Random(); // ����һ��������������ȡ��Ŀ
	private int[] QuestionNum = new int[8]; // ÿһ����Ŀ�����к�

	// ���̺߳�handler��������Ϣ
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CHANGE_QUESTION:
				mainMap = (Map<String, String>) msg.obj;
				stateView.setText("��" + statenum + "�� "+"ÿ��10���⣬ÿ��10�֣��ﵽ60�ַ��ɹ��أ�");
				stateprogressView.setText("��ǰ�÷�: " + tr*10 + "\n" +"��������� "
						+ wr);
				questionView.setText(qnumber + ":" + mainMap.get("questions"));
				aswA.setText("A." + mainMap.get("a"));
				aswB.setText("B." + mainMap.get("b"));
				aswC.setText("C." + mainMap.get("c"));
				aswD.setText("D." + mainMap.get("d"));
				break;
			case SETPROGRESS:
				int progress = (Integer) msg.obj;
				timeprogress.setProgress(progress);
				break;
			case RESTARTGAME:
				timer.cancel();
				OVERTIME = true; // ����Ϊ��ʱ
				new ShowTimeOverDialog().showdialog();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single);
		//TextView tv = new TextView(this);
		//tv.setTextColor(0xff000000);
		stateView = (TextView) this.findViewById(R.id.statetext);
		
		stateprogressView = (TextView) this.findViewById(R.id.stateprogress);
		questionView = (TextView) this.findViewById(R.id.questiontext);
		questionView.setTextColor(Color.RED);
		aswA = (Button) this.findViewById(R.id.aswA);
		aswA.setAlpha((float) 0.5);
		aswA.setOnClickListener(this);
		aswB = (Button) this.findViewById(R.id.aswB);
		aswB.setAlpha((float) 0.5);
		aswB.setOnClickListener(this);
		aswC = (Button) this.findViewById(R.id.aswC);
		aswC.setAlpha((float) 0.5);
		aswC.setOnClickListener(this);
		aswD = (Button) this.findViewById(R.id.aswD);
		aswD.setAlpha((float) 0.5);
		aswD.setOnClickListener(this);
		timeprogress = (ProgressBar) this.findViewById(R.id.progressBar1);
		timeprogress.setMax(TOTALPROGRESS);
		InitialQNum(); // ��ʼ�������������
		new Thread(new StartGame()).start();
		timer = new Timer(true);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (progressBarValue == TOTALPROGRESS) {
					// ������Ϸʱ�䣬�����Ի�����ʾ���
					handler.sendEmptyMessage(RESTARTGAME);
				} else {
					// ����Ϣ���͸�handler�����½�����
					Message message = Message.obtain();
					message.obj = progressBarValue;
					message.what = SETPROGRESS;
					handler.sendMessage(message);
					// ʱ���������
					progressBarValue++;
				}
			}
		}, 0, 1000);
	}

	// ��ʼ��QuestionNum����,�����ȡ
	private void InitialQNum() {
		int count = 0;
		while (count < sum) {
			boolean flag1 = true; // ��־�Ƿ��ظ�
			int cur = Math.abs(random.nextInt() % sum) + 1;
			for (int i = 0; i < count; i++) {
				if (cur == QuestionNum[i]) {
					flag1 = false;
					break;
				}
			}
			if (flag1) {
				QuestionNum[count] = cur;
				count++;
			}
		}
	}

	public class StartGame implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			getquestion getq = new getquestion(SingleModel.this);
			Map<String, String> map = new HashMap<String, String>();
			// ��MakeIntToString��������ת���ַ�����ѡ���Ӧ��Ŀ
			String str = MakeIntToString.getString(QuestionNum[qnumber - 1]
					+ (statenum - 1) *sum);
			String str1 = String.valueOf(statenum);
			String[] strs = new String[] { str, str1 };
			map = getq.getquestionMap(strs);
			// ��message�������̴߳�����Ϣ������
			Message message = Message.obtain();
			message.obj = map; // ��map��Ϣ����message��
			message.what = CHANGE_QUESTION; // �趨message�ı�ʾ��
			handler.sendMessage(message); // �����߳��е�handler������Ϣ
		}

	}

	// ��Ϸ������һ��
	private void GoToNextState() {
		if (OVERTIME) {
			return;
		}
		timer.cancel(); // �ر�ʱ��
		statenum++; // ��������
		qnumber = 1; // �������Ϊ1
		wr = 0; // �������
		
		InitialQNum(); // ���³�ȡ�������Ϊ��Ŀ����
		progressBarValue = 0; // ��ʱ���������Ϊ0
		Toast.makeText(SingleModel.this, "���ĵ÷�Ϊ��"+tr*10+"\n"+"��ϲ������" + statenum + "�أ�", 0)
				.show();
		tr = 0; // �������
		new Thread(new StartGame()).start();
		timer = null;
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (progressBarValue == TOTALPROGRESS) {
					// ������Ϸʱ�䣬�����Ի�����ʾ���
					handler.sendEmptyMessage(RESTARTGAME);
				} else {
					// ����Ϣ���͸�handler�����½�����
					Message message = Message.obtain();
					message.obj = progressBarValue;
					message.what = SETPROGRESS;
					handler.sendMessage(message);
					// ʱ���������
					progressBarValue++;
				}
			}
		}, 0, 1000);
	}

	// ���¿�ʼ��Ϸ
	private class RestartGame {
		public RestartGame() {

		}

		public void restart() {
			statenum = 1;
			qnumber = 1; // �������Ϊ1
			wr = 0;
			tr = 0;
			progressBarValue = 0;
			InitialQNum();
			timer = null;
			timer = new Timer(true);
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (progressBarValue == TOTALPROGRESS) {
						     // ������Ϸʱ�䣬�����Ի�����ʾ���
						handler.sendEmptyMessage(RESTARTGAME);
					} else {
						// ����Ϣ���͸�handler�����½�����
						Message message = Message.obtain();
						message.obj = progressBarValue;
						message.what = SETPROGRESS;
						handler.sendMessage(message);
						              // ʱ���������
						progressBarValue++;
					}
				}
			}, 0, 1000);
			new Thread(new StartGame()).start();
		}
	}

	// ��Ϸ��ʱ�����Ի���
	public class ShowTimeOverDialog {
		public ShowTimeOverDialog() {

		}

		public void showdialog() {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					SingleModel.this);
			builder.setTitle("��ʾ");
			builder.setMessage("�Բ����������̫�ͣ�û���ڹ涨ʱ������ɴ��⣡");
			builder.setPositiveButton("���¿�ʼ",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							OVERTIME = false; // ����Ϊ����ʱ
							new RestartGame().restart();
						}
					});
			builder.setNegativeButton("������",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							SingleModel.this.finish();
						}
					});
			builder.setCancelable(false);
			Dialog dialog = builder.create();
			dialog.show();

		}
	}

	// ��Ϸʧ��ʱ�����ĶԻ���
	public class ShowGameOverDialog {

		public ShowGameOverDialog() {

		}

		public void showdialog() {
			timer.cancel();
			AlertDialog.Builder builder = new AlertDialog.Builder(
					SingleModel.this);
			builder.setTitle("��ʾ");
			builder.setMessage("�Բ����޴������࣬�㴳��ʧ���ˣ�");
			builder.setPositiveButton("���´���",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							new RestartGame().restart();
						}
					});
			builder.setNegativeButton("������",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							SingleModel.this.finish();
						}
					});
			builder.setCancelable(false);
			Dialog dialog = builder.create();
			dialog.show();
		}
	}

	private void GoOverGame() {
		if (OVERTIME) {
			return;
		}
		timer.cancel();
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SingleModel.this);
		builder.setTitle("��ʾ");
		builder.setMessage("��ϲ��ͨ�أ���~�����������Ǹ�!");
		builder.setPositiveButton("ǫ��ǫ��",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						SingleModel.this.finish();
					}
				});
		builder.setCancelable(false);
		Dialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public void onBackPressed() { // �����ؼ�ʱ�����¼�
		// TODO Auto-generated method stub
		super.onBackPressed();
		timer.cancel(); // ��ʱ��ȡ�����ÿ�
		timer = null;
		SingleModel.this.finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.aswA:
			// ���ص�ǰ�Ƿ���
			flag = new JudgeAnswer(SingleModel.this).judgeit("a", mainMap);
			if (flag) { // �����ԣ���Ӧ�������иı�
				tr++;
				qnumber++;
				if ((tr+wr) == sum) {
					if (statenum == LASTSTATE) {
						GoOverGame();
					} else {
						GoToNextState();
					}
				} else {
					new Thread(new StartGame()).start();
				}
			} else {
				wr++;
				qnumber++;
				if (wr == wrsum) { // �����������ﵽ���ޣ�������Ϸ�����Ի���
					new ShowGameOverDialog().showdialog();
				} else { // ���������Ŀ
					new Thread(new StartGame()).start();
				}
			}
			break;
		case R.id.aswB:
			flag = new JudgeAnswer(SingleModel.this).judgeit("b", mainMap);
			if (flag) {
				tr++;
				qnumber++;
				if ((tr+wr) == sum) {
					if (statenum == LASTSTATE) {
						GoOverGame();
					} else {
						GoToNextState();
					}
				} else {
					new Thread(new StartGame()).start();
				}
			} else {
				wr++;
				qnumber++;
				if (wr == wrsum) {
					new ShowGameOverDialog().showdialog();
				} else {
					new Thread(new StartGame()).start();
				}
			}
			break;
		case R.id.aswC:
			flag = new JudgeAnswer(SingleModel.this).judgeit("c", mainMap);
			if (flag) {
				tr++;
				qnumber++;
				if ((tr+wr) == sum) {
					if (statenum == LASTSTATE) {
						GoOverGame();
					} else {
						GoToNextState();
					}
				} else {
					new Thread(new StartGame()).start();
				}
			} else {
				wr++;
				qnumber++;
				if (wr == wrsum) {
					new ShowGameOverDialog().showdialog();
				} else {
					new Thread(new StartGame()).start();
				}
			}
			break;
		case R.id.aswD:
			flag = new JudgeAnswer(SingleModel.this).judgeit("d", mainMap);
			if (flag) {
				tr++;
				qnumber++;
				if ((tr+wr) == sum) {
					if (statenum == LASTSTATE) {
						GoOverGame();
					} else {
						GoToNextState();
					}
				} else {
					new Thread(new StartGame()).start();
				}
			} else {
				wr++;
				qnumber++;
				if (wr == wrsum) {
					new ShowGameOverDialog().showdialog();
				} else {
					new Thread(new StartGame()).start();
				}
			}
			break;
		}
	}
}
