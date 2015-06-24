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
    TextView tvStage;
    TextView tvReady;
    TextView tvName;
    TextView tvTime;
    TextView tvScore;
    DisplayMetrics metrics;
    ImageAdapter imageAdapter;

    UserData userInfo;
    private String name;
    private int stage;
    private boolean timeFlag;
    public int time = 70;
    public int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);

        Bundle bundle = getIntent().getExtras();
        userInfo = bundle.getParcelable("userinfo");
        name = userInfo.getName();
        stage = userInfo.getStage();
        score = userInfo.getScore();

        Log.d(TAG, "stage : " + stage + " score : " + score);

        this.gridView = (GridView) findViewById(R.id.gridView);
        this.tvStage = (TextView) findViewById(R.id.tvStage);
        this.tvReady = (TextView) findViewById(R.id.tvReady);
        this.tvName = (TextView) findViewById(R.id.tvName);
        this.tvTime = (TextView) findViewById(R.id.tvTime);
        this.tvScore = (TextView) findViewById(R.id.tvScore);
        this.metrics = new DisplayMetrics();
        initStage(stage);

        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(this);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        timeFlag = true;
        scoreSet();
        imageAdapter.isStart = true;
        handler.sendEmptyMessage(0);
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
        this.tvStage.setText((stage + 1) + " 단계");
        this.tvName.setText(name);
        this.imageAdapter = new ImageAdapter(getApplicationContext(), metrics, (int)Math.pow(2, num) * 6);

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

    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (time < 60) {
                score = imageAdapter.getScore();
                scoreSet();
                timeSet();
                if (imageAdapter.isGameClear()) {
                    score += time;
                    userInfo.setStage(stage);
                    userInfo.setScore(score + userInfo.getScore());
                    Intent intent = new Intent();
                    intent.putExtra("isClear", true);
                    intent.putExtra("userinfo", userInfo);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                if (time == 0) {
                    userInfo.setStage(stage);
                    userInfo.setScore(score + userInfo.getScore());
                    Intent intent = new Intent();
                    intent.putExtra("isClear", false);
                    intent.putExtra("userinfo", userInfo);
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }

            } else if (time == 60){
                tvReady.setText("Start!!!");
                imageAdapter.isStart = false;
                imageAdapter.notifyDataSetChanged();
                //handler.sendEmptyMessageDelayed(0, 1000);
            } else {
                tvReady.setText("Ready!(" + (time-60) + ")");
            }

            --time;


            if (timeFlag == true)
                handler.sendEmptyMessageDelayed(0, 1000);
            return false;
        }
    });

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
        timeFlag = false;
        imageAdapter.destroy();
        imageAdapter = null;
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
