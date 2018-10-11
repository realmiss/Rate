package com.example.missy.rate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class BMIActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        Button btn1 = (Button) findViewById(R.id.button);
        btn1.setOnClickListener(this);
    }
    public void onClick(View view) {
        EditText height = (EditText) findViewById(R.id.height);
        EditText weight = (EditText) findViewById(R.id.weight);
        String heig = height.getText().toString();
        String weig = weight.getText().toString();
        if(null == heig || "".equals(heig) || null == weig || "".equals(weig)){
            Toast.makeText(this,"请输入完整信息", Toast.LENGTH_LONG).show();
        }else {
            double h = Double.parseDouble(heig);
            double w = Double.parseDouble(weig);
            if (h < 0.000001 || w < 0.000001) {
                Toast.makeText(this, "请输入有效数据", Toast.LENGTH_LONG).show();
            } else {
                double b = w / (h * h);
                b = (double) Math.round(b * 100) / 100;
                String ans = "BMI指数" + b;
                if (b >= 18.5 && b <= 23.9) {
                    ans += "\t体重理想，继续保持哦！";
                } else if (b < 18.5) {
                    ans += "\t体重偏轻，要多吃一点哦！";
                } else {
                    ans += "\t体重偏重，要控制饮食多运动哦！";
                }
                EditText bmi = (EditText) findViewById(R.id.result);
                bmi.setText(ans.toCharArray(), 0, ans.length());
            }
        }
    }
}
