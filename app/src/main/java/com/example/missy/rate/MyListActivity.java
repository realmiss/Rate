package com.example.missy.rate;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyListActivity extends ListActivity implements Runnable,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    private final String TAG = "MyListActivity";
    Handler handler;
    private ArrayList<HashMap<String, String>> listItems; // 存放文字、
    private ArrayList<HashMap<String, String>> rateList;
    private SimpleAdapter listItemAdapter; // 适配器
    private SimpleAdapter adapter;
    private int msgWhat = 7;

    private void initListView() {
        listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "Rate： " + i); // 标题文字
            map.put("ItemDetail", "detail" + i); // 详情描述
            listItems.add(map);
        }
        // 生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this, listItems, // listItems
                R.layout.list_item, // ListItem的XML布局实现
                new String[] { "ItemTitle", "ItemDetail" },
                new int[] { R.id.itemTitle, R.id.itemDetail }
        );
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListView();
        this.setListAdapter(listItemAdapter);
        Thread t = new Thread(this); // 创建新线程
        t.start(); // 开启线程
        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == msgWhat) {
                    List<HashMap<String, String>> retList = (List<HashMap<String, String>>) msg.obj;
                    adapter = new SimpleAdapter(MyListActivity.this, retList,
                            R.layout.list_item, // ListItem的XML布局实现
                            new String[]{"ItemTitle", "ItemDetail"},
                            new int[]{R.id.itemTitle, R.id.itemDetail});
                    setListAdapter(adapter);
                    Log.i("handler", "reset list...");
                }
                super.handleMessage(msg);
            }
        };
        getListView().setOnItemLongClickListener(this);
        getListView().setOnItemClickListener(this);
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Object itemAtPosition = getListView().getItemAtPosition(position);
        HashMap<String,String> map = (HashMap<String,String>) itemAtPosition;
        String titleStr = map.get("ItemTitle");
        String detailStr = map.get("ItemDetail");
        Log.i(TAG, "onItemClick:titleStr=" + titleStr);
        Log.i(TAG, "onItemClick:detailStr=" + detailStr);

        TextView title = (TextView)view.findViewById(R.id.itemTitle);
        TextView detail = (TextView)view.findViewById(R.id.itemDetail);
        String title2 = String.valueOf(title.getText());
        String detail2 = String.valueOf(detail.getText());
        Log.i(TAG, "onItemClick:title2=" + title2);
        Log.i(TAG, "onItemClick:detail2=" + detail2);
        Intent rateCalc = new Intent(this,RateCalcActivity.class);
        rateCalc.putExtra("title",titleStr);
        rateCalc.putExtra("rate",Float.parseFloat(detailStr));
        startActivity(rateCalc);
    }

    @Override
    public void run() {
        Log.i("thread", "run.....");
        boolean marker = false;
        rateList = new ArrayList<HashMap<String, String>>();
        try {
            Document doc = Jsoup.connect("http://www.usd-cny.com/icbc.htm").get();
            Elements tbs = doc.getElementsByClass("tableDataTable");
            Element table = tbs.get(0);
            Elements tds = table.getElementsByTag("td");
            for (int i = 0; i < tds.size(); i += 5) {
                Element td = tds.get(i);
                Element td2 = tds.get(i + 3);
                String tdStr = td.text();
                String pStr = td2.text();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemTitle", tdStr);
                map.put("ItemDetail", pStr);
                rateList.add(map);
                Log.i("td", tdStr + "=>" + pStr);
            }
            marker = true;
        } catch (MalformedURLException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage();
        msg.what = msgWhat;
        if (marker) {
            msg.arg1 = 1;
        } else {
            msg.arg1 = 0;
        }
        msg.obj = rateList;
        handler.sendMessage(msg);
        Log.i("thread", "sendMessage.....");

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        /*if(rateList.remove(position)!=null){//更新适配器
            Log.i(TAG, "onItemClick:delete success");
        }else{
            Log.i(TAG, "onItemClick:delete fail");
        }
        adapter.notifyDataSetChanged();
        return true;*/
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("确定删除?");
        builder.setTitle("提示");

        //添加AlertDialog.Builder对象的setPositiveButton()方法
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(rateList.remove(position)!=null){//更新适配器
                    Log.i(TAG, "onItemClick:delete success");
                }else{
                    Log.i(TAG, "onItemClick:delete fail");
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(), "删除列表项", Toast.LENGTH_SHORT).show();
            }
        });

        //添加AlertDialog.Builder对象的setNegativeButton()方法
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
        return true;
    }


}
