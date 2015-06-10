package com.iot.cardgame;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;
import java.util.Timer;


public class GameActivity extends ActionBarActivity implements GridView.OnItemClickListener{
    private static final String TAG = "GameActivity";
    GridView gridView;
    TextView tvTime;
    TextView tvScore;
    DisplayMetrics metrics;
    ImageAdapter imageAdapter;

    private int stage;
    private CountDownTimer timer;
    public int time = 60;
    public int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();

        stage = intent.getIntExtra("stage", 0);
        score = intent.getIntExtra("score", 0);

        this.gridView = (GridView) findViewById(R.id.gridView);
        this.tvTime = (TextView) findViewById(R.id.tvTime);
        this.tvScore = (TextView) findViewById(R.id.tvScore);
        this.metrics = new DisplayMetrics();
        initStage(stage);

        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(this);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //timer.start();

        timeSet();
        scoreSet();
        imageAdapter.isStart = true;
        handler.sendEmptyMessageDelayed(1, 10000);
    }
    private void initStage(int num){
        int col = 0;
        switch (num){
            case 0:
                col = 3;
                break;
            case 1:
                col = 4;
                break;
            case 2:
                col = 6;
                break;
        }
        this.gridView.setNumColumns(col);
        this.imageAdapter = new ImageAdapter(getApplicationContext(), metrics, (int)Math.pow(2, num) * 6);

        /*timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                --time;
                tvTime.setText("Time : " + time);
                //tvScore.setText("Score : " + imageAdapter.getScore());
            }

            @Override
            public void onFinish() {
                --time;
                tvTime.setText("Time : " + time);
                Toast.makeText(getApplicationContext(), "Time Over", Toast.LENGTH_LONG).show();

                Intent intent = new Intent();
                intent.putExtra("isClear", false);
                intent.putExtra("stage", stage);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        };*/
    }

    public void timeSet(){
        tvTime.setText("Time : " + time);
    }

    public void scoreSet(){
        tvScore.setText("Score : " + imageAdapter.getScore());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        imageAdapter.itemClicked(position);
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                score = imageAdapter.getScore();
                scoreSet();
                if (imageAdapter.isGameClear()) {
                    Intent intent = new Intent();
                    intent.putExtra("isClear", true);
                    intent.putExtra("stage", stage);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                if (time > 0) {
                    --time;
                    timeSet();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("isClear", false);
                    intent.putExtra("stage", stage);
                    intent.putExtra("score", score);
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
                handler.sendEmptyMessageDelayed(0, 1000);
            } else if (msg.what == 1) {
                imageAdapter.isStart = false;
                imageAdapter.notifyDataSetChanged();
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
