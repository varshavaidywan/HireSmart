package com.avaloninfosys.hiresmart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class splashScreen extends AppCompatActivity {

    private int sleepTimer = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();

        logoLauncher launcher = new logoLauncher();
        launcher.start();

    }
    private class logoLauncher extends Thread{
        public void run(){
            try{
                sleep(1000 * sleepTimer);
            }
            catch(InterruptedException ie){
                ie.printStackTrace();
            }
            Intent intent = new Intent(splashScreen.this, MainActivity.class);
            startActivity(intent);
            splashScreen.this.finish();
        }
    }
}
