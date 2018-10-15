package com.example.missy.rate;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyList2Activity extends ListActivity implements Runnable{
    Handler handler;
    private ArrayList<HashMap<String, String>> listItems; // 存放文字、
    MyAdapter myAdapter;
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
        myAdapter = new MyAdapter(this,R.layout.list_item,listItems);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListView();
        this.setListAdapter(myAdapter);
        Thread t = new Thread(this); // 创建新线程
        t.start(); // 开启线程
        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == msgWhat) {
                    List<HashMap<String, String>> retList = (List<HashMap<String, String>>) msg.obj;
                   /* MyAdapter adapter = new MyAdapter(MyListActivity2.this,
                            R.layout.list_item,retList);*/// ListItem的XML布局实现
                    SimpleAdapter adapter = new SimpleAdapter(MyList2Activity.this, retList,
                            R.layout.list_item, // ListItem的XML布局实现
                            new String[]{"ItemTitle", "ItemDetail"},
                            new int[]{R.id.itemTitle, R.id.itemDetail});
                    setListAdapter(adapter);
                    Log.i("handler", "reset list...");
                }
                super.handleMessage(msg);
            }
        };
    }


    @Override
    public void run() {
        Log.i("thread", "run.....");
        boolean marker = false;
        List<HashMap<String, String>> rateList = new ArrayList<HashMap<String, String>>();
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
}
