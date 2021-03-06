package com.stl.wristNotes;

import android.app.*;
import android.content.*;
import android.os.*;
import android.text.method.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import java.io.*;
import java.util.*;

import org.json.*;

import com.stl.wristNotes.method.*;


public class inputAct extends Activity
{
    Context ctx;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    TextView inputtitle;
    EditText inputedit;
    Button inputbutton;

    int po;
    Intent i;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input);

        ctx = this;
        sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        po = getIntent().getIntExtra("po", -1);
        
        i = new Intent();
        i.putExtra("info", "");
        setResult(po, i);
        
        inputtitle = (TextView) findViewById(R.id.inputText);
        inputtitle.setText(MainActivity.inputtitle);
        inputedit = (EditText) findViewById(R.id.inputEdit);
        inputedit.setHint(MainActivity.inputhite);
        inputedit.setText(MainActivity.inputset);
        if(!MainActivity.inputkeys.equals(""))
            inputedit.setKeyListener(DigitsKeyListener.getInstance(MainActivity.inputkeys));
        inputbutton = (Button) findViewById(R.id.inputButton);
        inputbutton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View p1)
            {
                if(MainActivity.inputact == 1)//密码伪装
                {
                    if(!inputedit.getText().toString().equals(""))
                    {
                        editor.putString("passtext", inputedit.getText().toString());
                        editor.commit();
                        Toast.makeText(ctx, "设置成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        editor.putString("passtext", "输入密码");
                        editor.commit();
                        Toast.makeText(ctx, "恢复默认：“输入密码”", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else if(MainActivity.inputact == 2)
                {
                    try
                    {
                        if(inputedit.getText().toString() != "")
                        {
                            JSONObject novellist = new JSONObject(sharedPreferences.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
                            List<String> novelpage = new ArrayList<String>(Arrays.asList(novellist.getString("page").split("▒")));
                            novelpage.set(MainActivity.p - 1, (Integer.valueOf(inputedit.getText().toString()).intValue() - 1) + "");
                            novellist.put("page", MainActivity.join(novelpage.toArray(new String[novelpage.size()]), "▒"));

                            MainActivity.novelReader = new BufferedReader(new InputStreamReader(new FileInputStream(MainActivity.filepath + MainActivity.filename), MainActivity.code));
                            if(Integer.valueOf(inputedit.getText().toString()).intValue() != 1)
                            {
                                MainActivity.novelReader.skip((Integer.valueOf(inputedit.getText().toString()).intValue() - 1) * 500);
                            }
                            MainActivity.textView.setText(fileOpen.novelReader(0));
                            MainActivity.mainScrollView.fullScroll(View.FOCUS_UP);
                            editor.putString("novelList", novellist.toString());
                            editor.commit();
                            MainActivity.mainHint.setText(MainActivity.getHintText(sharedPreferences));
                            //batteryLevel();
                            Toast.makeText(ctx, "已跳转(^-^)", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(ctx, "你好像什么也没有输入→_→", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e)
                    {
                        if(e.toString().contains("charCount"))
                            Toast.makeText(ctx, "已是第一页！", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(ctx, "发生未知错误！", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    if(!inputedit.getText().toString().equals(""))
                    {
                        i.putExtra("info", inputedit.getText().toString());
                        setResult(po, i);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(ctx, "请输入" + MainActivity.inputset + "名称！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
