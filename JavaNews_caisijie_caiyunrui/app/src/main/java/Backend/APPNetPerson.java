package Backend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class APPNetPerson {
    public static String getURL = "https://innovaapi.aminer.cn/predictor/api/v1/valhalla/highlight/get_ncov_expers_list?v=2";

    public APPNetPerson(){}

    public ArrayList<APPPerson> getInfo(){
        HttpURLConnection conn;
        ArrayList<APPPerson> tmpList = new ArrayList<>();
        try {
            URL tmpGet = new URL(getURL);
            conn = (HttpURLConnection)tmpGet.openConnection();
            conn.setConnectTimeout(5*1000);
            conn.setReadTimeout(10*1000);
            if(conn.getResponseCode()!=200){
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JSONObject Js = new JSONObject(in.readLine());
            JSONArray tmpJsList = Js.getJSONArray("data");

            int len = tmpJsList.length();
            for(int i = 0; i < len; i++){

                JSONObject tmpJs = tmpJsList.getJSONObject(i);
                APPPerson tmpPerson = new APPPerson(tmpJs);
                tmpList.add(tmpPerson);
            }
        }catch (IOException | JSONException e){
            System.out.println(e);
            return null;
        }

        return tmpList;
    }
}
