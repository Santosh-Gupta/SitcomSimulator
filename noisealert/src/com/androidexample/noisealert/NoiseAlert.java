package com.androidexample.noisealert;

import java.util.Random;

import com.androidexample.noisealert.R;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class NoiseAlert extends Activity  {
        /* constants */
		//MediaPlayer mp;
		int ran;
	    MediaPlayer[] mp = new MediaPlayer[20];
	    Random r = new Random();
	
        private static final int POLL_INTERVAL = 200;
        boolean aaa = false;
       
        /** running state **/
        private boolean mRunning = false;
        
        /** config state **/
        private int mThreshold;
        
        private PowerManager.WakeLock mWakeLock;

        private Handler mHandler = new Handler();

        /* References to view elements */
        private TextView mStatusView;
        private SoundLevelView mDisplay;

        /* sound data source */
        private SoundMeter mSensor;
        
       /****************** Define runnable thread again and again detect noise *********/
        
        private Runnable mSleepTask = new Runnable() {
                public void run() {
                	//Log.i("Noise", "runnable mSleepTask");
                        
                	start();
                }
        };
        
        // Create runnable thread to Monitor Voice
        private Runnable mPollTask = new Runnable() {
                public void run() {
                	
                        double amp = mSensor.getAmplitude();
                        
                        
                        
                        //Log.i("Noise", "runnable mPollTask");
                        updateDisplay("For UCI Hackathon Videos check out our youtube channel https://www.youtube.com/channel/UCzVOOpeVLhc75DUzxdbDS0w or search for Awkward Pony", amp);

                        if ((amp > mThreshold)) {
                              callForHelp();
                              aaa=true;
                              ///mp[0].start();
                              //Log.i("Noise", "==== onCreate ===");
                        }
                        
                        if((amp<(mThreshold-4) && aaa==true)){
                        	mSensor.stop();
                        	ran = r.nextInt(20);
                        	mp[ran].start();
                        	aaa=false;
                        	mSensor.start();
                        }
                        
                        if((mp[ran].isPlaying())){
                        	mThreshold=80;
                        }
                        if(!(mp[ran].isPlaying())){
                        	mThreshold=6;
                        }
                        
                        // Runnable(mPollTask) will again execute after POLL_INTERVAL
                        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
                     
                }
        };
        
        
        
        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
        	
        	    mp[0]= MediaPlayer.create(this, R.raw.a518913);
        	    mp[1]= MediaPlayer.create(this, R.raw.aaah);
        	    mp[2]= MediaPlayer.create(this, R.raw.aaughndpplause);
        	    mp[3]= MediaPlayer.create(this, R.raw.acratching);
        	    mp[4]= MediaPlayer.create(this, R.raw.adramatic);
        	    mp[5]= MediaPlayer.create(this, R.raw.aheering);
        	    mp[6]= MediaPlayer.create(this, R.raw.ahockorror);
        	    mp[7]= MediaPlayer.create(this, R.raw.aidsheering);
        	    mp[8]= MediaPlayer.create(this, R.raw.aongpplause);
        	    mp[9]= MediaPlayer.create(this, R.raw.arowdghing);
        	    mp[10]= MediaPlayer.create(this, R.raw.arowdoo);
        	    mp[11]= MediaPlayer.create(this, R.raw.avoice017);
        	    mp[12]= MediaPlayer.create(this, R.raw.avoice018);
        	    mp[13]= MediaPlayer.create(this, R.raw.avoice020);
        	    mp[14]= MediaPlayer.create(this, R.raw.cheering);
        	    mp[15]= MediaPlayer.create(this, R.raw.ahockorror);
        	    mp[16]= MediaPlayer.create(this, R.raw.laughter);
        	    mp[17]= MediaPlayer.create(this, R.raw.oooh);
        	    mp[18]= MediaPlayer.create(this, R.raw.shocked);
        	    mp[19]= MediaPlayer.create(this, R.raw.avoice018);
        	    
        	    
        		
                super.onCreate(savedInstanceState);
                
                // Defined SoundLevelView in main.xml file
                setContentView(R.layout.main);
                mStatusView = (TextView) findViewById(R.id.status);
               
                // Used to record voice
                mSensor = new SoundMeter();
                mDisplay = (SoundLevelView) findViewById(R.id.volume);
                
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "NoiseAlert");
        }

        
        @Override
        public void onResume() {
                super.onResume();
                //Log.i("Noise", "==== onResume ===");
                
                initializeApplicationConstants();
                mDisplay.setLevel(0, mThreshold);
                
                if (!mRunning) {
                    mRunning = true;
                    start();
                }
        }

        @Override
        public void onStop() {
                super.onStop();
               // Log.i("Noise", "==== onStop ===");
               
                //Stop noise monitoring
                stop();
               
        }

        private void start() {
        	    //Log.i("Noise", "==== start ===");
        	
                mSensor.start(); //starts recording
                if (!mWakeLock.isHeld()) {
                        mWakeLock.acquire();
                }
                
                //Noise monitoring start
                // Runnable(mPollTask) will execute after POLL_INTERVAL
                mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        }

        private void stop() {
        	Log.i("Noise", "==== Stop Noise Monitoring===");
                if (mWakeLock.isHeld()) {
                        mWakeLock.release();
                }
                mHandler.removeCallbacks(mSleepTask);
                mHandler.removeCallbacks(mPollTask);
                mSensor.stop();
                mDisplay.setLevel(0,0);
                updateDisplay("stopped...", 0.0);
                mRunning = false;
               
        }

       
        private void initializeApplicationConstants() {
                // Set Noise Threshold
        	    mThreshold = 7;
                
        }

        private void updateDisplay(String status, double signalEMA) {
                mStatusView.setText(status);
                // 
                mDisplay.setLevel((int)signalEMA, mThreshold);
        }
        
        
        private void callForHelp() {
              
              //stop();
              
        	 // Show alert when noise thersold crossed
        	  //Toast.makeText(getApplicationContext(), "Noise Thersold Crossed, do here your stuff.", 
        			 // Toast.LENGTH_LONG).show();
        }

};
