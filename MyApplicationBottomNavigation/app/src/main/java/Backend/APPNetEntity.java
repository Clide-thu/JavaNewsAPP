package Backend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class APPNetEntity {
    private static final String entitiesQueryURL= "https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery";
    private String query;

    public APPNetEntity(String query){
        this.query = query;
    }

    //please run in thread
    public ArrayList<APPEntity> getResult(){
        ArrayList<APPEntity> entityList = new ArrayList<>();
        try {
            HttpURLConnection conn;
            URL tmpGet = new URL(entitiesQueryURL+"?entity="+query);
            conn = (HttpURLConnection)tmpGet.openConnection();
            conn.setConnectTimeout(5*1000);
            conn.setReadTimeout(5*1000);
            if(conn.getResponseCode()!=200){
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JSONArray js = new JSONObject(in.readLine()).getJSONArray("data");
            int len = js.length();
            for(int i = 0; i < len; i++){
                entityList.add(new APPEntity(js.getJSONObject(i)));
            }
            return entityList;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
