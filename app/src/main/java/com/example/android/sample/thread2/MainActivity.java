package com.example.android.sample.thread2;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ProgressBar myBar;
    TextView IbITopCaption;
    EditText txtBox1;
    Button btnDoSomething;
    int accum = 0;
    long startingMills = System.currentTimeMillis();//get time
    String PATIENCE = "Some important data is been collected now ¥n Please be patient";
    Handler myHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IbITopCaption=(TextView)findViewById(R.id.IBITopCaption);
        myBar = (ProgressBar)findViewById(R.id.myBar);
        myBar.setMax(100);
        txtBox1 = (EditText)findViewById(R.id.txtBox1);
        txtBox1.setHint("Foreground distraction . Enter some data");
        btnDoSomething = (Button)findViewById(R.id.btnDoSomething);
        btnDoSomething.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable txt = txtBox1.getText();
                Toast.makeText(getBaseContext(),"You said >> "+txt,Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onStart(){
        super.onStart();
        //create background thread were the busy work will be done
        Thread myThread1 = new Thread(backgroundTask,"backAlias1");
        myThread1.start();
        myBar.incrementProgressBy(0);
    }
    //this is the foreground "Runnable" object responsible for GUI updates
    private Runnable foregroundTask = new Runnable(){
        @Override
        public void run(){
            try {
                int progressStep = 5;
                IbITopCaption.setText(PATIENCE + "Totalseec.so far : " +
                        (System.currentTimeMillis() - startingMills) / 1000);
                myBar.incrementProgressBy(progressStep);
                accum += progressStep;
                if (accum >= myBar.getMax()) {
                    IbITopCaption.setText("Background work is over !!");
                    myBar.setVisibility(View.INVISIBLE);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private Runnable backgroundTask = new Runnable() {
        @Override
        public void run() {
            //busy work goes here
            try{
                for(int n = 0;n < 20; n++){
                    //This simulates 1sec of busy activity
                    Thread.sleep(1000);
                    //now talk to the ain thread
                    myHandler.post(foregroundTask);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    };
}
