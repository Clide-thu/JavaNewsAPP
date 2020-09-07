package Backend;

import android.content.ContentValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class APPNetEpidemicData {
    private static final String epidemicDataURL = "https://covid-dashboard.aminer.cn/api/dist/epidemic.json";

    private LinkedHashMap<String, APPEpidemicData> dataOfChina = new LinkedHashMap<>();
    private LinkedHashMap<String, APPEpidemicData> dataOfWorld = new LinkedHashMap<>();
    private boolean isCached = false;

    public APPNetEpidemicData(){}

    //please run in thread
    private void fetchData(){
        if(!isCached){
            try {
                HttpURLConnection conn;
                URL tmpGet = new URL(epidemicDataURL);
                conn = (HttpURLConnection)tmpGet.openConnection();
                conn.setConnectTimeout(5*1000);
                conn.setReadTimeout(3*1000);
                if(conn.getResponseCode()!=200){
                    return;
                }

                String regexChina = "^China[|]?[^|]*$";
                String regexWorld = "^[^|]*$";
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                JSONObject js = new JSONObject(in.readLine());
                JSONArray nameArray = js.names();
                int len = nameArray.length();
                for(int i = 0; i < len; i++){
                    String tmpName = nameArray.getString(i);
                    if(Pattern.matches(regexChina,tmpName)){
                        dataOfChina.put(tmpName, new APPEpidemicData(tmpName, js.getJSONObject(tmpName)));
                    }else if(Pattern.matches(regexWorld,tmpName)){
                        dataOfWorld.put(tmpName, new APPEpidemicData(tmpName, js.getJSONObject(tmpName)));
                    }
                }

                isCached = true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private LinkedHashMap<String, APPNetEpidemicData> getDataOfChina(){
        fetchData();
        return (LinkedHashMap<String, APPNetEpidemicData>) dataOfChina.clone();
    }

    private LinkedHashMap<String, APPNetEpidemicData> getDataOfWorld(){
        fetchData();
        return (LinkedHashMap<String, APPNetEpidemicData>) dataOfWorld.clone();
    }
}
