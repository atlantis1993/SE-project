package com.color.tools;

import java.util.Map;

import android.R.integer;
import android.content.Context;
import android.util.Log;

public class JudgeAnswer {
	private static String Tag = "check";
	private Context context;
	public JudgeAnswer(Context context) {
		// TODO Auto-generated constructor stub
	}


	public boolean judgeit(String answer,Map<String, String> map){
		boolean flag=false;
		String Trueanswer=map.get("answer");
		Log.v(Tag, "answer:"+Trueanswer);
		if(answer.equals(Trueanswer)){
			flag=true;
		}
		return flag;
	}
}
