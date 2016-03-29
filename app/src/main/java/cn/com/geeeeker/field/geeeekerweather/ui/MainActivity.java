package cn.com.geeeeker.field.geeeekerweather.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import cn.com.geeeeker.field.geeeekerweather.R;



public class MainActivity extends Activity {
    String[] result= new String[23];
    ImageView maininfoLayer,moreInfo;
    Handler handler;
//    static String cityName;
    MainInfoListener clickMainInfo =  new MainInfoListener();
    TextView city,date,week,shishiqiwen,quantianqiwen,chuanyi,xiche,lvyou,ganmao,yundong,ziwaixian,pm,tianqi,mingtiantianqi,mingtianqiwen,mingtianxingqi,houtiantianqi,houtianqiwen,houtianxingqi,dahoutiantianqi,dahoutianqiwen,dahoutianxingqi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        maininfoLayer = (ImageView)findViewById(R.id.maininfo);
        maininfoLayer.setOnClickListener(clickMainInfo);
        moreInfo = (ImageView)findViewById(R.id.menu);
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast=Toast.makeText(getApplicationContext(),"浮芸编写", Toast.LENGTH_LONG);
                toast.show();
            }
        });


        getInfo();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                   if (msg.what== 0x2333){

                       if (result[22].equals("succeed")){
                           UpdateInfo();
                       }else{
                           Toast toast=Toast.makeText(getApplicationContext(),"天气数据获取失败，请检查网络连接", Toast.LENGTH_LONG);
                           toast.show();
                       }

                   }

            }
        };
    }

    private void getInfo(){
        new Thread(){
            @Override
            public void run(){

                result[22]="succeed";
        /*
        0 城市
        1 日期

        2 今天实时气温
        3 今天是星期几
        4 今天天气
        5 今天气温

        6 明天周几
        7 明天天气
        8 明天气温

        9 后天周几
        10 后天天气
        11 后天气温

        12 大后天周几
        13 大后天天气
        14 大后天气温

        15 穿衣指数
        16 洗车指数
        17 旅游指数
        18 感冒指数
        19 运动指数
        20 紫外线强度
        21 PM2.5
        22 故障判断位
        */
                System.setProperty("http.keepAlive","false");
                try{
                    String sn =getSN("南京");
                    HttpGet httpGet = new HttpGet("http://api.map.baidu.com/telematics/v3/weather?location=南京&output=json&ak=zVKQ9b0qwcZTiDjhHkB7y34x&sn="+sn);
                    HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode()== HttpStatus.SC_OK){

                        String resultJson = EntityUtils.toString(httpResponse.getEntity());
                        resultJson.replaceAll("\r","");

                        JSONObject json = new JSONObject(resultJson);
                        //获取日期
                        result[1] = convertDate(json.getString("date"));
                        //json现在是result里面的东西
                        json=json.getJSONArray("results").getJSONObject(0);


                        result[0] = json.getString("currentCity");
                        result[21] = json.getString("pm25");


                        JSONArray array  =  json.getJSONArray("weather_data");
                        for (int i=0;i<4;i++){
                            json = array.getJSONObject(i);
                            result[i*3+3]=json.getString("date");
                            result[i*3+5]=json.getString("temperature");
                            result[i*3+4]=json.getString("weather");
                        }

                        if (result[3].split(" ").length==3) {
                            result[2] = result[3].split(" ")[2].split("：")[1].replaceAll("\\)", "");
                            result[3] = result[3].split(" ")[0];
                        }else {
                            result[3] = result[3].split(" ")[0];
                            result[2]="暂无数据";
                        }

                        json = new JSONObject(resultJson);
                        json=json.getJSONArray("results").getJSONObject(0);
                        array  =  json.getJSONArray("index");
                        for (int i=0;i<6;i++){
                            json = array.getJSONObject(i);
                            result[15+i]=json.getString("zs");
                        }
                    }
                    Message msg = new Message();
                    msg.what = 0x2333;
                    handler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                    result[22]="error";
                }
            }
        }.start();


    }



    private int UpdateInfo(){

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
            for (int i = 1; i < 15; i++) {
               textViews[i] = (TextView) findViewById(IDs[i]);
               textViews[i].setText(result[i]);
            }
            return 0;
        }else return 1;
    }

    class MainInfoListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,Detail.class);
            intent.putExtra("detailInfo",result);
            startActivity(intent);

        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
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

    //把获取到的2015-5-30这样的日期变成2015年5月30日
    private static String convertDate(String date){
        String[] tmp = date.split("-");
        return tmp[0]+"年"+tmp[1]+"月"+tmp[2]+"日";
    }

    private static String getSN(String city) throws Exception{
        Map paramsMap = new LinkedHashMap<String, String>();
        paramsMap.put("location", city);
        paramsMap.put("output", "json");
        paramsMap.put("ak", "zVKQ9b0qwcZTiDjhHkB7y34x");

        // 调用下面的toQueryString方法，对LinkedHashMap内所有value作utf8编码，拼接返回结果address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourak
        String paramsStr = toQueryString(paramsMap);

        // 对paramsStr前面拼接上/geocoder/v2/?，后面直接拼接yoursk得到/geocoder/v2/?address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourakyoursk
        String wholeStr = new String("/telematics/v3/weather?" + paramsStr + "hBoVs9NSOBGFED2VK02U2pWoidhzWPrG");

        // 对上面wholeStr再作utf8编码
        String tempStr = URLEncoder.encode(wholeStr, "UTF-8");
        return MD5(tempStr);
    }


    private static String toQueryString(Map<?, ?> data) throws UnsupportedEncodingException
    {
        StringBuffer queryString = new StringBuffer();
        for (Map.Entry<?, ?> pair : data.entrySet())
        {
            queryString.append(pair.getKey() + "=");
            queryString.append(URLEncoder.encode((String) pair.getValue(), "UTF-8") + "&");
        }
        if (queryString.length() > 0)
        {
            queryString.deleteCharAt(queryString.length() - 1);
        }
        return queryString.toString();
    }

    // 来自stackoverflow的MD5计算方法，调用了MessageDigest库函数，并把byte数组结果转换成16进制
    private static String MD5(String md5)
    {
        try
        {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i)
            {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e)
        {
        }
        return null;
    }

}
