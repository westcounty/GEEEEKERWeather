package cn.com.geeeeker.field.geeeekerweather.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.geeeeker.field.geeeekerweather.R;


public class Detail extends Activity {

    ImageView maininfoLayer,moreInfo,detailInfo;
    MainInfoListener clickMainInfo =  new MainInfoListener();
    TextView city,date,week,shishiqiwen,quantianqiwen,chuanyi,xiche,lvyou,ganmao,yundong,ziwaixian,pm,tianqi,mingtiantianqi,mingtianqiwen,mingtianxingqi,houtiantianqi,houtianqiwen,houtianxingqi,dahoutiantianqi,dahoutianqiwen,dahoutianxingqi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        maininfoLayer = (ImageView)findViewById(R.id.back);
        maininfoLayer.setOnClickListener(clickMainInfo);
        moreInfo = (ImageView)findViewById(R.id.more);
        detailInfo= (ImageView)findViewById(R.id.detailinfo);
        detailInfo.setOnClickListener(clickMainInfo);
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast=Toast.makeText(getApplicationContext(),"浮芸编写", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        Intent intent = getIntent();
        String[] result = intent.getStringArrayExtra("detailInfo");

        if (result[22].equals("succeed")) {

            TextView[] textViews = {city, date, shishiqiwen, week, tianqi, quantianqiwen, mingtianxingqi, mingtiantianqi, mingtianqiwen, houtianxingqi, houtiantianqi, houtianqiwen, dahoutianxingqi, dahoutiantianqi, dahoutianqiwen, chuanyi, xiche, lvyou, ganmao, yundong, ziwaixian, pm};
            int[] IDs = {0, R.id.date, R.id.shishiqiwen, R.id.week, R.id.tianqi, R.id.quantianqiwen, R.id.mingtianxingqi, R.id.mingtiantianqi, R.id.mingtianqiwen, R.id.houtianxingqi,
                    R.id.houtiantianqi, R.id.houtianqiwen, R.id.dahoutianxingqi, R.id.dahoutiantianqi, R.id.dahoutianqiwen, R.id.chuanyi, R.id.xiche, R.id.lvyou, R.id.ganmao, R.id.yundong, R.id.ziwaixian, R.id.pm};

//        0 城市
//        1 日期
//        2 今天实时气温
//        3 今天是星期几
//        4 今天天气
//        5 今天气温
//        6 明天周几
//        7 明天天气
//        8 明天气温
//        9 后天周几
//        10 后天天气
//        11 后天气温
//        12 大后天周几
//        13 大后天天气
//        14 大后天气温
//        15 穿衣指数
//        16 洗车指数
//        17 旅游指数
//        18 感冒指数
//        19 运动指数
//        20 紫外线强度
//        21 PM2.5
//        22 故障判断位
            for (int i = 15; i < 22; i++) {
                textViews[i] = (TextView) findViewById(IDs[i]);
                textViews[i].setText(result[i]);
            }
        }else {
            Toast toast=Toast.makeText(getApplicationContext(),"天气数据获取失败，请检查网络连接", Toast.LENGTH_LONG);
            toast.show();
        }
    }
    class MainInfoListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(Detail.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
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
