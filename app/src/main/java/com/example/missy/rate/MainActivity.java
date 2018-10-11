package com.example.missy.rate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = "Counter";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = (Button) findViewById(R.id.button);
        btn1.setOnClickListener(this);

        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        Button btn4 = (Button) findViewById(R.id.button4);
        btn4.setOnClickListener(this);

        Button btn5 = (Button) findViewById(R.id.button5);
        btn5.setOnClickListener(this);

        Button btn6 = (Button) findViewById(R.id.button6);
        btn6.setOnClickListener(this);

        Button btn7 = (Button) findViewById(R.id.button7);
        btn7.setOnClickListener(this);
    }

    public void onClick(View view) {
        TextView textA = (TextView) findViewById(R.id.textA);
        TextView textB = (TextView) findViewById(R.id.textB);
        switch (view.getId()){
            case R.id.button:
                textA.setText(String.valueOf(Integer.parseInt(textA.getText().toString())+3));
                break;
            case R.id.button2:
                textA.setText(String.valueOf(Integer.parseInt(textA.getText().toString())+2));
                break;
            case R.id.button3:
                textA.setText(String.valueOf(Integer.parseInt(textA.getText().toString())+1));
                break;
            case R.id.button4:
                textA.setText(String.valueOf(0));
                textB.setText(String.valueOf(0));
                break;
            case R.id.button5:
                textB.setText(String.valueOf(Integer.parseInt(textB.getText().toString())+3));
                break;
            case R.id.button6:
                textB.setText(String.valueOf(Integer.parseInt(textB.getText().toString())+2));
                break;
            case R.id.button7:
                textB.setText(String.valueOf(Integer.parseInt(textB.getText().toString())+1));
                break;
            default:
                break;
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String scorea = ((TextView) findViewById(R.id.textA)).getText().toString();
        String scoreb = ((TextView) findViewById(R.id.textB)).getText().toString();
        Log.i(TAG, "onSaveInstanceState: ");
        outState.putString("teama_score", scorea);
        outState.putString("teamb_score", scoreb);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String scorea = savedInstanceState.getString("teama_score");
        String scoreb = savedInstanceState.getString("teamb_score");
        Log.i(TAG, "onRestoreInstanceState: ");
        ((TextView) findViewById(R.id.textA)).setText(scorea);
        ((TextView) findViewById(R.id.textB)).setText(scoreb);
    }
}



