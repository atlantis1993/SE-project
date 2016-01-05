package com.color.answer;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Client extends Activity implements OnClickListener {
	private ServerListener server;
	private Joiner joiner;
	private boolean flag;
	private TextView text10,text2,tip;
	private RadioGroup radioGroupTutorials;
	private RadioButton radioBtn1;
	private RadioButton radioBtn2;
	private EditText edit0,edit1,edit2,edit3,edit4;
	private Button ques,ans,ansA,ansB,ansC,ansD,exit,send;
	String name ;
	private static String Tag = "color";
	private Handler handlerRec;
	private String[] message,mes;
	int i=0;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client);
		initializeLog();
	
		handlerRec = new Handler(){
			public void handleMessage(Message msg) {
				switch(msg.what){
				case 1:
					if (msg.obj != null) {
						mes[i] = msg.obj.toString();
						i++;
						if(i==7){
							i=0;
							showtext();  //show the textview
							settext();    //set the text on the textview
							gettheanswer();
							
							ansA.setClickable(true);
							ansB.setClickable(true);
							ansC.setClickable(true);
							ansD.setClickable(true);
						}
					}
					break;
				case 2:
					Toast.makeText(getApplication(), (CharSequence) msg.obj, Toast.LENGTH_LONG).show();
					text10.setText(name +"加入人数:"+ msg.obj+msg.arg1);

					break;
				case 3:
					Toast.makeText(getApplication(), "from joiner", Toast.LENGTH_LONG).show();
					text2.setVisibility(View.VISIBLE);
					text2.append( (CharSequence) msg.obj);
					break;
				default: break;
				}
			}
		};
		serverorclient();
	}
	//end of void onCreate(Bundle savedInstanceState);



	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn1 :     //question
			ans.setVisibility(View.GONE);
			hidetext();
			showedit();
			break;
		case R.id.btn2 :       //answer
			ques.setVisibility(View.GONE);
			hideedit();
			showtext();
			settext();
			break;
		case R.id.btn3 :       //ansA
			check("A");
			break;
		case R.id.btn4 :      //ansB
			check("B");
			break;
		case R.id.btn5 :     //ansC
			check("C");
			break;
		case R.id.btn6 :     //ansD
			check("D");
			break;

		case R.id.btn7 :    //exit
			ans.setVisibility(View.VISIBLE);
			ques.setVisibility(View.VISIBLE);
			hidetext();
			hideedit();
			break;
		
		case R.id.send :    //send
			gettext();
			sendmessage(message);
			break;
		default: break;
		}

	};
	private void gettheanswer(){
		if(mes[6].equals("right")){
			Toast.makeText(getApplication(), "he got the right answer!", Toast.LENGTH_LONG).show(); 
		}else if(mes[6].equals("wrong")){
			Toast.makeText(getApplication(), "he got the wrong answer!", Toast.LENGTH_LONG).show(); 
		}
	}
	private void sendmessage(String[] msg){

		      //get the text from the editview;
		if(name.equals("joiner")){
			//  send the message to server
			joiner.sendmess(msg);
			Toast.makeText(getApplication(), msg[0]+msg[1]+msg[2], Toast.LENGTH_LONG).show(); 
			Toast.makeText(getApplication(), msg[3]+msg[4]+msg[5]+msg[6], Toast.LENGTH_LONG).show(); 
		}else{
			//server send the message to client
			ManageClient.getChatManage().serverpublish(msg);
			Toast.makeText(getApplication(), "server send message!", Toast.LENGTH_LONG).show();
		}
	}
	private void showalert(String trueans){
		AlertDialog.Builder dialog = new AlertDialog.Builder(Client.this);
		dialog.setTitle("your answer is");
		dialog.setMessage(trueans);
		dialog.setCancelable(false);
		dialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//clear the message
				ansA.setClickable(false);
				ansB.setClickable(false);
				ansC.setClickable(false);
				ansD.setClickable(false);
				mes[6]="none";
				message[6]="none";
			}
		});
			
	
		dialog.show();
	}
	private void check(String answer){
	      
		 if(mes[5].equals(answer))
		 {
			 Toast.makeText(getApplication(), "you are right"+answer, Toast.LENGTH_LONG).show();
			 mes[6]="right";
			 showalert("right");
				sendmessage(mes);
				tip.setText("答题结束，the right answer is "+ mes[5] +",    and  you  choose  "+ answer);
		       }
         else{
        	 tip.setText("the right answer is "+ mes[5] +" ,    and  you choose  "+ answer);
      	   Toast.makeText(getApplication(), "答题结束，you are wrong"+answer+mes[5], Toast.LENGTH_LONG).show();
      	 mes[6]="wrong";
      	 showalert("wrong");
      	sendmessage(mes);
		 }
	}
	
	//end of onClick();
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
	case 2131230734:
		 message[5] = "A";
		break;
	case 2131230735:
		 message[5] = "B";
		break;
	case 2131230736:
		 message[5] = "C";
		break;
	case 2131230737:
		 message[5] = "D";
		break;
		default:
			break;
	}	
}
	private void settext(){
		text2.setText("题目:" +mes[0]);
		ansA.setText("A:"+mes[1]);
		ansB.setText("B:"+mes[2]);
		ansC.setText("C:"+mes[3]);
		ansD.setText("D:"+mes[4]);
		
	}
	private void showtext(){
		if(mes[6].equals("none")){
			hideedit();
			text2.setVisibility(View.VISIBLE);
			ansA.setVisibility(View.VISIBLE);
			ansB.setVisibility(View.VISIBLE);
			ansC.setVisibility(View.VISIBLE);
			ansD.setVisibility(View.VISIBLE);
			tip.setVisibility(View.VISIBLE);
			exit.setVisibility(View.VISIBLE);
			
		}else{
		
		}
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
	private void initializeLog() {
		mes = new String[]{null,null,null,null,null,null,"none"};
		message = new String[]{null,null,null,null,null,null,"none"};
		text10 = (TextView)findViewById(R.id.txt1);
		tip = (TextView)findViewById(R.id.tip);
		text2 = (TextView)findViewById(R.id.txt2);
		ques = (Button)findViewById(R.id.btn1);
		ans = (Button)findViewById(R.id.btn2);
		ansA = (Button)findViewById(R.id.btn3);
		ansB = (Button)findViewById(R.id.btn4);
		ansC = (Button)findViewById(R.id.btn5);
		ansD = (Button)findViewById(R.id.btn6);
		exit = (Button)findViewById(R.id.btn7);
		send = (Button)findViewById(R.id.send);
		edit0 = (EditText)findViewById(R.id.edit0);
		edit1 = (EditText)findViewById(R.id.edit1);
		edit2 = (EditText)findViewById(R.id.edit2);
		edit3 = (EditText)findViewById(R.id.edit3);
		edit4 = (EditText)findViewById(R.id.edit4);	
		radioGroupTutorials = (RadioGroup) findViewById(R.id.radioGroup1);

		Intent intent = getIntent();
		name = intent.getStringExtra("name");

		ques.setOnClickListener(this);
		ans.setOnClickListener(this);	
		exit.setOnClickListener(this);	
		send.setOnClickListener(this);	
		ansA.setOnClickListener(this);
		ansB.setOnClickListener(this);	
		ansC.setOnClickListener(this);	
		ansD.setOnClickListener(this);	
		 flag = false;
		 ansA.setClickable(false);
			ansB.setClickable(false);
			ansC.setClickable(false);
			ansD.setClickable(false);

	}	
	//end of initializeLog();
	private void serverorclient(){
		if(name.equals("joiner")){
			//  joiner 
			Toast.makeText(getApplication(), name, Toast.LENGTH_LONG).show();
			joiner = new Joiner(handlerRec);
			joiner.start();
			Toast.makeText(getApplication(), " joiner start!", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(getApplication(), "builder", Toast.LENGTH_LONG).show();
			server =  new ServerListener(handlerRec);
			server.start();
			Log.v(Tag, "thread2");
			Toast.makeText(getApplication(), "thread start!", Toast.LENGTH_LONG).show();
			text10.setText(name +"加入人数:");
		}
	}
	//end of serverorclient();

}   //end of class
