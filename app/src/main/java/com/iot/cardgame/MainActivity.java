package com.iot.cardgame;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private static final int REQUEST_CODE = 0;
    private int stage;
    private int score;
    EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFormat(Window.FEATURE_NO_TITLE);
        stage = 0;
        score = 0;
        etName = (EditText) findViewById(R.id.etName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onButtonClicked(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.btnStart:
                intent = new Intent(this, GameActivity.class);
                intent.putExtra("name", etName.getText().toString());
                intent.putExtra("stage", stage);
                intent.putExtra("score", score);
                startActivityForResult(intent, REQUEST_CODE);
                break;

            case R.id.btnScore:
                intent = new Intent(this, GameActivity.class);
                intent.putExtra("name", etName.getText().toString());
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            if (resultCode == RESULT_OK){
                if (data.getBooleanExtra("isClear", false)) {
                    stage = data.getIntExtra("stage", 0);
                    score = data.getIntExtra("score", 0);
                    if (stage < 2) {
                        ++stage;
                        Intent intent = new Intent(this, GameActivity.class);
                        intent.putExtra("name", etName.getText().toString());
                        intent.putExtra("stage", stage);
                        intent.putExtra("score", score);
                        startActivityForResult(intent, REQUEST_CODE);
                        Toast.makeText(getApplicationContext(), stage + " " + score, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Game Clear!! Your Score : " + score, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "시간초과로 게임 오", Toast.LENGTH_LONG).show();
                stage = 0;
            }
        }
    }
}
