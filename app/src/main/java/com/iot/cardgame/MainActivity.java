package com.iot.cardgame;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFormat(Window.FEATURE_NO_TITLE);

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
                startActivity(intent);
                break;

            case R.id.btnScore:
                intent = new Intent(this, GameActivity.class);
                intent.putExtra("name", etName.getText().toString());
                startActivity(intent);
                break;
        }
    }
}
