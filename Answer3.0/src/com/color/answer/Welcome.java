package com.color.answer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class Welcome extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);  
		 final View view = View.inflate(this, R.layout.welcome, null);
	        setContentView(view);
	                                                                      
	        //����չʾ������
	        AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
	        aa.setDuration(2300);
	        view.startAnimation(aa);
	        aa.setAnimationListener(new AnimationListener()
	        {
	            @Override
	            public void onAnimationEnd(Animation arg0) {
	                redirectTo();
	            }
	            @Override
	            public void onAnimationRepeat(Animation animation) {}
	            @Override
	            public void onAnimationStart(Animation animation) {}
	                                                                          
	        });
	                                                                      
	                                                          
	    }
	                                                                  
	    /**
	     * ��ת��...
	     */
	    private void redirectTo(){  
	        Intent intent = new Intent(this, MainActivity.class);
	        startActivity(intent);
	        finish();
	    }
}