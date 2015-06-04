package com.iot.cardgame;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;


public class GameActivity extends ActionBarActivity implements GridView.OnItemClickListener{

    GridView gridView;
    TextView tvTime;
    TextView tvScore;
    DisplayMetrics metrics;
    ImageAdapter imageAdapter;
    private CountDownTimer timer;
    public int time = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);

        this.gridView = (GridView) findViewById(R.id.gridView);
        this.tvTime = (TextView) findViewById(R.id.tvTime);
        this.tvScore = (TextView) findViewById(R.id.tvScore);
        this.metrics = new DisplayMetrics();
        initStage(0);

        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(this);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        timer.start();
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

        timer = new CountDownTimer(5 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                --time;
                tvTime.setText("Time : " + time);
            }

            @Override
            public void onFinish() {
                --time;
                tvTime.setText("Time : " + time);
                Toast.makeText(getApplicationContext(), "Time Over", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this, parent.getAdapter().getItem(position).toString(), Toast.LENGTH_SHORT).show();

        imageAdapter.itemClicked(position);
        tvScore.setText(imageAdapter.getScore()+"");

        //imageAdapter.notifyDataSetChanged();
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
