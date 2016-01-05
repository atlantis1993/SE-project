package com.color.answer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.color.answer.SingleModel.ShowGameOverDialog;
import com.color.answer.SingleModel.ShowTimeOverDialog;
import com.color.answer.SingleModel.StartGame;
import com.color.getsqldatabase.getquestion;
import com.color.tools.JudgeAnswer;
import com.color.tools.MakeIntToString;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Client extends Activity implements OnClickListener {
	private boolean flag_room ; //房主和房客的标识
	private ServerListener server;
	private String title;
	private int mes_from;
	private Timer timer; // 设置一个定时器
	private ProgressBar timeprogress; // 时间进度条
	private int progressBarValue = 0; // 表示时间进度条的进度
	private final static int SETPROGRESS = 4; // 表示设置时间进度条的标识符
	private final static int TOTALPROGRESS = 10; // 设置时间进度条的最大值
	private Joiner joiner;
	private TextView text10,text2,tip,model1_tip,model2_tip;    //text10-->title_tip ;text2-->question                                                //
	private RadioGroup radioGroupTutorials;         
	private RadioButton radioBtn1;                                                    //
	private EditText edit0,edit1,edit2,edit3,edit4;                                //
	private Button start,ansA,ansB,ansC,ansD,exit,send,exitroom,model1,model2,aswA, aswB, aswC, aswD,check,lucky;
	String name ;
	private static String Tag = "client";
	private Handler handlerRec;
	private String[] message,mes;         //mes is the receiver buffer,  message is send buffer
	int i=0;
	private int model = 1;
	private boolean flag = false; // 此题是否答对
	private int statenum = 1; // 当前关数
	private int[] QuestionNum = new int[8]; // 每一关题目的序列号
	private Random random = new Random(); // 设置一个随机数来随机抽取题目
	private final static int sum = 8; // 总共需要答对的题数
	private int qnumber = 1; // 当前题目的题号
	private int wr = 0; // 答错的题数
	private int tr = 0; // 答对的题数
	private String tihao = "11." ;
	private final static int CHANGE_QUESTION = 1; // 变换游戏界面题目的标识符
	private Map<String, String> mainMap = new HashMap<String, String>();
	// start from here
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.client);
		initializeLog();
		messageHandle();
		serverorclient();      // just choose you are server or client
	}
	//end of void onCreate(Bundle savedInstanceState);


	//fuction messageHandle() is just handle the message receiver from thread
	public void messageHandle(){
		handlerRec = new Handler(){
			public void handleMessage(Message msg) {
				switch(msg.what){
				case 1:       
					//receive the message from socket ,this is the most important code ,
					if (msg.obj != null) {
						mes[i] = msg.obj.toString();
						String  numberti = mes[i];
						String result = numberti.substring(0, 2);
						mes_from = Integer.valueOf(result).intValue();
						Log.v(Tag, result);
					switch(mes_from){
						case 10:                           //model1  right answer 
							i=0;
							String result0 = numberti.substring(3, 4);
							String name_joint = numberti.substring(4, numberti.length());
							Toast.makeText(getApplication(),name_joint+"got right anser"+result0 , Toast.LENGTH_SHORT).show();
							tip.setVisibility(View.VISIBLE);
							tip.append(name_joint+ "答对了"+result0+"\r\n");
							break;
						case 11:              //tihao
							i=0;
							String result1 = numberti.substring(3, 11);
							tihao = result1;
							Log.v(Tag, "r2="+result1);
							get_tihao();
							break;
						case 20:     //model2
							mes[i] = numberti.substring(3, numberti.length());
							Log.v(Tag, mes[i]);
							receive();
							break;
						default:
							break;
							
						}
					}
					break;
				case 2:          
					//receiver the number of joiner from serverlistener,and just update the number of joiner include yourself
					Toast.makeText(getApplication(), (CharSequence) msg.obj, Toast.LENGTH_SHORT).show();
					text10.setText(name +"人数:"+msg.arg1);
					break; 
				case 3:   
					// receiver  message from 。。。。   ,I have not use it yet,
					Toast.makeText(getApplication(), "some join", Toast.LENGTH_SHORT).show();
					text2.setVisibility(View.VISIBLE);
					text2.append( (CharSequence) msg.obj);
					break;
				case 4:
					int progress = (Integer) msg.obj;
					timeprogress.setProgress(progress);
					//Toast.makeText(getApplication(), "timer++", Toast.LENGTH_SHORT).show();  
					break;
				case 5:

					Toast.makeText(getApplication(), "时间到，你不能再答题了！！！！", Toast.LENGTH_SHORT).show();  
					showalert("时间到", "你总共答对"+tr,2);
					Log.v(Tag, "timerout success");
					break;
				default: break;
				}
			}
		};
	}






	public void get_tihao(){
		QuestionNum[7] = Integer.valueOf(tihao).intValue();
		QuestionNum[0] = QuestionNum[7]/10000000;
		QuestionNum[1] = (QuestionNum[7]%10000000)/1000000;
		QuestionNum[2] = (QuestionNum[7]%1000000)/100000;
		QuestionNum[3] = (QuestionNum[7]%100000)/10000;
		QuestionNum[4] = (QuestionNum[7]%10000)/1000;
		QuestionNum[5] = (QuestionNum[7]%1000)/100;
		QuestionNum[6] = (QuestionNum[7]%100)/10;
		QuestionNum[7] = (QuestionNum[7]%10);
		Log.v(Tag, QuestionNum[0]+"ok");
		Log.v(Tag, QuestionNum[1]+"ok");
		Log.v(Tag, QuestionNum[2]+"ok");
		Log.v(Tag, QuestionNum[3]+"ok");
		Log.v(Tag, QuestionNum[4]+"ok");
		Log.v(Tag, QuestionNum[5]+"ok");
		Log.v(Tag, QuestionNum[6]+"ok");
		Log.v(Tag, QuestionNum[7]+"ok");
		model1_init();
		progressBarValue=0;
		timer = null; 
		tr=0;wr=0;
		model_play();
		model1_btn_click();
		timer_begin();
		hidetext();                    
		hideedit();
	}
	private void timer_begin(){
		timer = new Timer(true);
		start.setText("游戏中...");
		model1.setClickable(false);
		model2.setClickable(false);
		start.setClickable(false);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (progressBarValue == TOTALPROGRESS) {
					Log.v(Tag, "timer up");
					progressBarValue = 0;
					timer.cancel();
					timer = null;
					timeprogress.setProgress(TOTALPROGRESS);
					handlerRec.sendEmptyMessage(5);


				} else {
					Message message = Message.obtain();
					message.obj = progressBarValue;
					message.what = SETPROGRESS;
					handlerRec.sendMessage(message);
					progressBarValue++;

				}
			}
		}, 0, 1000);

	}

	// receive  the message 
	public void receive(){
		i++;
		if(i==8){
			i=0;
			showtext();                  //show the textview
			settext();                   //set the text on the textview
			checkAnswerReceived();       //just check the mes[6] =? right/wrong ,represent he is right or wrong
			if(mes[6].equals("none")){
				setBtnClick();
			}
		}
	}


	private void model1_init(){
		model = 1;
		start.setText("开始游戏");
		model1.setTextColor(0xFFFF0000);
		model2.setTextColor(0x7f000000);
		hidetext(); 
		hide_model_btn();
		hideedit();
		model1_tip.setVisibility(View.VISIBLE);
		model2_tip.setVisibility(View.GONE);
	}


	//  btn click 
	public void onClick(View v){
		switch(v.getId()){
		case R.id.aswA:
			model1_check();
			break;
		case R.id.aswB:
			model1_check();
			break;
		case R.id.aswC:
			model1_check();
			break;
		case R.id.aswD:
			model1_check();
			break;
		case R.id.model1 : 
			model1_init();
			break;
		case R.id.model2 : 
			model = 2;
			model1.setClickable(true);
			start.setText("我出题");
			model1.setTextColor(0x7f000000);
			model2.setTextColor(0xFFFF0000);
			hidetext();                    
			hideedit();
			hide_model_btn();
			model1_tip.setVisibility(View.GONE);
			model2_tip.setVisibility(View.VISIBLE);
			break;
		case R.id.start :                 //  begin to edit the question  you want ask
			if(model == 1){
				if(flag_room){
					tr=0;wr=0;
					qnumber = 1; // 重置题号为1
					InitialQNum();
					model_play();
					model1_btn_click();
					timer_begin();
					sendmessage(tihao);      //11  tihao	
				}else{
					showalert("提示", "请等待房主点击开始！！！", 4);
				}
		
			}else{
				hidetext();                    
				showedit();
				hide_model_btn();
			}	
			tip.setText(null);
			break;
		case R.id.btn3 :                                   //ansA\\
			checkself("A");     
			break;
		case R.id.btn4 :                                 //  ansB  \\
			checkself("B");
			break; 
		case R.id.btn5 :                               //    ansC     \\   
			checkself("C");
			break;
		case R.id.btn6 :                            //       ansD       \\
			checkself("D");
			break;
		case R.id.btn7 :                        //exit ,just hide the editView and textView
			start.setVisibility(View.VISIBLE); //just make the btn "answer" visiable  \\
			hidetext();        
			hideedit();
			break;

		case R.id.send :                 //send                                           \\    
			gettext();                  //get the message that you will send 
			message[6]="none";         //  means this is question    message[6]=right/wrong  means this is answer
			tip.setText(null);        //clear the tip(textView)
			sendmessage(message);    //send the message you edit to others
			break;
		case R.id.exitroom :      //exitRoom--> finish  activity
			Toast.makeText(getApplication(), "you click finish", Toast.LENGTH_SHORT).show();  
			finish();          // to close ServerSocket or SocketClient
			break;
		case R.id.lucky : 
			  //showalert("等待编写中", "敬请期待....", 4);
			  Intent intent11=new Intent(Client.this,Raffle.class);
			  startActivity(intent11);
			  //Raffle();
			break;
		case R.id.check :  
			
			  showalert("等待编写中", "敬请期待....", 4);
			break;
		default:
			break;
		}

	};

	private void model1_check(){
		if(tr+wr<8){
			
			flag = new JudgeAnswer(Client.this).judgeit("a", mainMap);

			if (flag) { // 如果答对，对应参数进行改变
				tr++;
			} else {
				wr++;
			}
			Log.v(Tag, tr+"+"+wr);
			Toast.makeText(getApplication(), "答对"+tr+"答错"+wr, Toast.LENGTH_SHORT).show(); 
		}
		if(tr+wr>=8){
			timer.cancel();
			timer=null;
			progressBarValue = 0;
			showalert("答题结束", "你总共答对"+tr+"用时"+progressBarValue,3);
		}else{
			qnumber++;
			Log.v(Tag,"num:"+qnumber);
			model_play(); 
		}
	}

	private void model1_btn_click(){
		aswA.setClickable(true);
		aswB.setClickable(true);
		aswC.setClickable(true);
		aswD.setClickable(true);
	}
	private void model1_btn_unclick(){
		aswA.setClickable(false);
		aswB.setClickable(false);
		aswC.setClickable(false);
		aswD.setClickable(false);
	}

	private void checkAnswerReceived(){
		if(mes[6].equals("right")){
			tip.setVisibility(View.VISIBLE);
			exit.setVisibility(View.VISIBLE);
			tip.append( mes[7]+ "   got the right answer"+"\r\n");
			//	Toast.makeText(getApplication(), "  the right answer!", Toast.LENGTH_SHORT).show(); 
		}else if(mes[6].equals("wrong")){
			tip.setVisibility(View.VISIBLE);
			tip.append( mes[7]+ "   got the wrong answer"+"\r\n");
			exit.setVisibility(View.VISIBLE);
			//Toast.makeText(getApplication(), " the wrong answer!", Toast.LENGTH_SHORT).show(); 
		}else{
			tip.setText(null);
		}
	}


  private void sendmessage(String msg){
		if(name.equals("joiner")){
			joiner.out(msg);
			Log.v(Tag, "joiner_senf_msg:"+msg);
			Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show(); 
		}else{
			ManageClient.getChatManage().server_send_msg(msg);   //sever
			Log.v(Tag, "server_send_msg:"+msg);
			Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show(); 
		} 
  }

	private void sendmessage(String[] msg){

		//get the text from the editview;
		if(name.equals("joiner")){             
			//  send the message to server
			joiner.sendmess(msg);
			Toast.makeText(getApplication(), msg[0]+msg[1]+msg[2], Toast.LENGTH_SHORT).show(); 
			Toast.makeText(getApplication(), msg[3]+msg[4]+msg[5]+msg[6], Toast.LENGTH_SHORT).show();
			//Log.v(Tag, "joiner_senf_msg:"+msg);
		}else{
			//server send the message to client
			ManageClient.getChatManage().serverpublish(msg);
			Toast.makeText(getApplication(), "server send message!", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplication(), msg[0]+msg[1]+msg[2], Toast.LENGTH_SHORT).show(); 
			Toast.makeText(getApplication(), msg[3]+msg[4]+msg[5]+msg[6], Toast.LENGTH_SHORT).show();
		}
	}




	private void checkself(String answer){
		//mes is the message received from the person who ask the question,we just send it back with the right/wrong
		if(mes[5].equals(answer))
		{
			Toast.makeText(getApplication(), "you are right"+answer, Toast.LENGTH_SHORT).show();
			mes[6]="right";
			showalert("your answer is","right",1);
			sendmessage(mes);                         //  tell the other i'm right
			tip.append("答题结束，the right answer is "+ mes[5] +",    and  you  choose  "+ answer+"\r\n");
		}
		else{
			tip.append("the right answer is "+ mes[5] +" ,    and  you choose  "+ answer+"\r\n");
			Toast.makeText(getApplication(), "答题结束，you are wrong"+answer+mes[5], Toast.LENGTH_SHORT).show();
			mes[6]="wrong";
			showalert("your answer is","wrong",1);  
			sendmessage(mes);   //   tell the other i'm wrong
		}
	}




	private void initializeLog() {
		mes = new String[]{null,null,null,null,null,null,null,null};
		message = new String[]{null,null,null,null,null,null,null,"myself"};
		text10 = (TextView)findViewById(R.id.txt1);
		model1_tip = (TextView)findViewById(R.id.model1_tip);
		model2_tip = (TextView)findViewById(R.id.model2_tip);
		tip = (TextView)findViewById(R.id.tip);
		text2 = (TextView)findViewById(R.id.txt2);
		start = (Button)findViewById(R.id.start);
		lucky = (Button)findViewById(R.id.lucky);
		check = (Button)findViewById(R.id.check);
		ansA = (Button)findViewById(R.id.btn3);
		ansB = (Button)findViewById(R.id.btn4);
		ansC = (Button)findViewById(R.id.btn5);
		ansD = (Button)findViewById(R.id.btn6);
		exit = (Button)findViewById(R.id.btn7);
		aswA = (Button)findViewById(R.id.aswA);
		aswB = (Button)findViewById(R.id.aswB);
		aswC = (Button)findViewById(R.id.aswC);
		aswD = (Button)findViewById(R.id.aswD);
		model1 = (Button)findViewById(R.id.model1);
		model2 = (Button)findViewById(R.id.model2);
		exitroom = (Button)findViewById(R.id.exitroom);
		send = (Button)findViewById(R.id.send);
		edit0 = (EditText)findViewById(R.id.edit0);
		edit1 = (EditText)findViewById(R.id.edit1);
		edit2 = (EditText)findViewById(R.id.edit2);
		edit3 = (EditText)findViewById(R.id.edit3);
		edit4 = (EditText)findViewById(R.id.edit4);	
		timeprogress = (ProgressBar) this.findViewById(R.id.progressBar1);
		timeprogress.setMax(TOTALPROGRESS);
		radioGroupTutorials = (RadioGroup) findViewById(R.id.radioGroup1);
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		start.setOnClickListener(this);
		exit.setOnClickListener(this);	
		exitroom.setOnClickListener(this);	
		send.setOnClickListener(this);	
		ansA.setOnClickListener(this);
		ansB.setOnClickListener(this);	
		ansC.setOnClickListener(this);	
		ansD.setOnClickListener(this);	
		model1.setOnClickListener(this);	
		model2.setOnClickListener(this);	
		aswA.setOnClickListener(this);	
		aswB.setOnClickListener(this);	
		aswC.setOnClickListener(this);	
		aswD.setOnClickListener(this);	
		check.setOnClickListener(this);	
		lucky.setOnClickListener(this);	
		hide_model_btn();
	}	
	//end of initializeLog();




	private void serverorclient(){
            //
		String result = name.substring(0, name.indexOf("."));        //from 0  to '.' present  eg:  joint.name  -->result = joint
		Log.v(Tag,"from the previous activity  的前缀   "+ result);

		if(result.equals("joiner")){
			flag_room = false;
			String result1 = name.substring((result.length()+1));  //  get  the rest  from  above activity  eg:joint.name-->result1 = name
			Log.v(Tag, "name="+result1);
			joiner = new Joiner(handlerRec,result1);
			message[7] = result1;
			name = result1;
			text10.setText(name +"人数:1");
			joiner.start();
		}else{
			flag_room = true;
			String result1 = name.substring((result.length()+1));
			Log.v(Tag, "name="+result1);
			title = name;
			name = result1;
			Log.v(Tag, "name="+name);
			server =  new ServerListener(handlerRec);
			message[7] = result1;
			server.start();
			Log.v(Tag, "thread2");
			text10.setText(title +"加入人数:");
		}
	}
	//end of serverorClient();




	@Override
	public void finish() {

		if(name.equals("joiner")){
			joiner.joinerclose();
			joiner.out("exit-/");
		}else{
			server.endServerThread();      //  stop server socket
		}
		Toast.makeText(getApplication(), " 退出房间！", Toast.LENGTH_SHORT).show();
		Log.v(Tag, "finish");
		super.finish();

	}
	//end of onClick();



	//  start   showAlert();
	private void showalert(String title,String true_answer,int model){
		AlertDialog.Builder dialog = new AlertDialog.Builder(Client.this);
		int model_type = model;
		dialog.setTitle(title);
		dialog.setMessage(true_answer);
		dialog.setCancelable(false);
		dialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//clear the message
			}
		});
		dialog.show();

		switch (model_type){
		case 1:
			setBtnUnclick();
			mes[6]="none";
			message[6]="none";
			mes[5]=null;
			message[5]=null;
			break;
		case 2:      //  时间到
			model1_btn_unclick();                           // 1.设置按键不可按  2.设置成开始游戏，而不是游戏中，并且是按钮是可按的  3.模式二按钮 也是可以按得
			start.setText("开始游戏");
			// start.setBackgroundColor(color);
			tihao="11.";
			model2.setClickable(true);
			start.setClickable(true);
			sendmessage("10"+tr+name);
			tip.setVisibility(View.VISIBLE);
			tip.setText(name+"答对了"+tr);
			break;
		case 3:     //  答题数目到了
			// 1.设置按键不可按  2.设置成开始游戏，而不是游戏中，并且是按钮是可按的  3.模式二按钮 也是可以按得
			model1_btn_unclick(); 
			tihao="11.";       // 1.设置按键不可按  2.设置成开始游戏，而不是游戏中，并且是按钮是可按的  3.模式二按钮 也是可以按得
			start.setText("开始游戏");
			model2.setClickable(true);
			start.setClickable(true);
			sendmessage("10"+tr+name);
			tip.setVisibility(View.VISIBLE);
			tip.setText(name+"答对了"+tr);
			break;
		default:
			break;
		}
	}




	private void setBtnClick(){
		Log.v(Tag, "ques is false click");
		ansA.setClickable(true);
		ansB.setClickable(true);
		ansC.setClickable(true);
		ansD.setClickable(true);
		start.setClickable(false);
		Log.v(Tag, "ques is false click");
	}


	private void setBtnUnclick(){
		ansA.setClickable(false);
		ansB.setClickable(false);
		ansC.setClickable(false);
		ansD.setClickable(false);
		start.setClickable(true);
	}

	private void gettext(){
		message[0] = edit0.getText().toString();
		message[1] = edit1.getText().toString();
		message[2] = edit2.getText().toString();
		message[3] = edit3.getText().toString();
		message[4] = edit4.getText().toString();
		int selected = radioGroupTutorials.getCheckedRadioButtonId();
		Log.v(Tag, Integer.toString(selected));
		radioBtn1 = (RadioButton) findViewById(selected);
		Toast.makeText(getApplication(),radioBtn1.getText()+Integer.toString(selected), Toast.LENGTH_SHORT).show();
		switch(selected){
		case 2131230735:
			message[5] = "A";
			break;
		case 2131230736:
			message[5] = "B";
			break;
		case 2131230737:
			message[5] = "C";
			break;
		case 2131230738:
			message[5] = "D";
			break;
		default:
			break;
		}	
	}





	private void showtext(){
		hideedit();
		model1_tip.setVisibility(View.GONE);
		model2_tip.setVisibility(View.GONE);
		text2.setVisibility(View.VISIBLE);
		ansA.setVisibility(View.VISIBLE);
		ansB.setVisibility(View.VISIBLE);
		ansC.setVisibility(View.VISIBLE);
		ansD.setVisibility(View.VISIBLE);
		tip.setVisibility(View.VISIBLE);
		exit.setVisibility(View.VISIBLE);

	}
	private void show_model_btn(){
		timeprogress.setVisibility(View.VISIBLE);
		text2.setVisibility(View.VISIBLE);
		aswA.setVisibility(View.VISIBLE);
		aswB.setVisibility(View.VISIBLE);
		aswC.setVisibility(View.VISIBLE);
		aswD.setVisibility(View.VISIBLE);
	}
	private void hide_model_btn(){
		timeprogress.setVisibility(View.GONE);
		text2.setVisibility(View.GONE);
		aswA.setVisibility(View.GONE);
		aswB.setVisibility(View.GONE);
		aswC.setVisibility(View.GONE);
		aswD.setVisibility(View.GONE);
	}
	private void hidetext(){
		text2.setVisibility(View.GONE);
		ansA.setVisibility(View.GONE);
		ansB.setVisibility(View.GONE);
		ansC.setVisibility(View.GONE);
		ansD.setVisibility(View.GONE);

		tip.setVisibility(View.GONE);
		exit.setVisibility(View.GONE);	
	}
	private void showedit(){
		hidetext();
		model1_tip.setVisibility(View.GONE);
		model2_tip.setVisibility(View.GONE);
		edit0.setVisibility(View.VISIBLE);
		edit1.setVisibility(View.VISIBLE);
		edit2.setVisibility(View.VISIBLE);
		edit3.setVisibility(View.VISIBLE);
		edit4.setVisibility(View.VISIBLE);
		radioGroupTutorials.setVisibility(View.VISIBLE);
		exit.setVisibility(View.VISIBLE);
		send.setVisibility(View.VISIBLE);
	} 
	private void hideedit(){
		edit0.setVisibility(View.GONE);
		edit1.setVisibility(View.GONE);
		edit2.setVisibility(View.GONE);
		edit3.setVisibility(View.GONE);
		edit4.setVisibility(View.GONE);
		send.setVisibility(View.GONE);
		radioGroupTutorials.setVisibility(View.GONE);
	}




	private void settext(){
		text2.setText("题目:" +mes[0]);
		ansA.setText("A:"+mes[1]);
		ansB.setText("B:"+mes[2]);
		ansC.setText("C:"+mes[3]);
		ansD.setText("D:"+mes[4]);

	}

	private void model_play() {
		// TODO Auto-generated method stub
		tip.setText(null);
		Log.v(Tag, "get the radom number");
		getquestion getq = new getquestion(Client.this);
		Map<String, String> map = new HashMap<String, String>();
		// 用MakeIntToString工具类来转换字符，并选择对应题目
		String str = MakeIntToString.getString(QuestionNum[qnumber - 1]
				+ (statenum - 1) *sum);
		Log.v(Tag, "get the radom number"+str);
		String str1 = String.valueOf(statenum);
		String[] strs = new String[] { str, str1 };
		map = getq.getquestionMap(strs);
		mainMap = map;
		text2.setText(qnumber + ":" + map.get("questions"));
		aswA.setText("A." + map.get("a"));
		aswB.setText("B." + map.get("b"));
		aswC.setText("C." + map.get("c"));
		aswD.setText("D." + map.get("d"));
		show_model_btn();
	}
	private void InitialQNum() {
		int count = 0;
		while (count < sum) {
			boolean flag1 = true; // 标志是否重复
			int cur = Math.abs(random.nextInt() % sum) + 1;
			for (int i = 0; i < count; i++) {
				if (cur == QuestionNum[i]) {
					flag1 = false;
					break;
				}
			}
			if (flag1) {
				QuestionNum[count] = cur;
				tihao = tihao+cur;
				Log.v(Tag, "题号："+cur);
				count++;
			}
		}	
		Log.v(Tag, "题号："+tihao);
	}

}


