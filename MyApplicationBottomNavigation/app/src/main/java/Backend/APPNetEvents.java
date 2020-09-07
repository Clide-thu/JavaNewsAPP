package Backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class APPNetEvents {
    private static final String eventsURL = "https://covid-dashboard.aminer.cn/api/events/list";
    private static final String eventsURLBy_id = "https://covid-dashboard-api.aminer.cn/event";

    private int paperPage = 0;
    private int newsPage = 0;
    private SQLiteDatabase database;

    //refresh via new APPNetEvents
    public APPNetEvents(Context context){
        database = new APPSQLHelper(context).getWritableDatabase();
        synchronized (APPSQLHelper.class){
            database.delete(APPSQLHelper.EVENTS_TABLE,"watched=?",new String[]{"0"});
        }
    }

    public int deleteRecord(){
        synchronized (APPSQLHelper.class){
            return database.delete(APPSQLHelper.EVENTS_TABLE,"watched=?",new String[]{"1"});
        }
    }

    //please run in thread
    public ArrayList<APPEvent> getEventRecord(){
        try {
            Cursor cursor = database.query(APPSQLHelper.EVENTS_TABLE,new String[]{"json"},"watched=?",new String[]{"1"},null,null,"id desc");
            ArrayList<APPEvent> tmplist = new ArrayList<>();
            while(cursor.moveToNext()){
                tmplist.add(APPEvent.GetRecordFromJson(new JSONObject(cursor.getString(0))));
            }
            return tmplist;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //please run in thread
    public APPEvent getEventBy_id(String _id){
        Cursor cursor = database.query(APPSQLHelper.EVENTS_TABLE,new String[]{"json"},"_id=?",new String[]{_id},null,null,null);
        try {
            if(cursor.moveToNext()){
                JSONObject tmpJs = new JSONObject(cursor.getString(0));
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id",tmpJs.getString("_id"));
                contentValues.put("json",tmpJs.toString());
                contentValues.put("watched",1);
                synchronized (APPSQLHelper.class) {
                    database.insertWithOnConflict(APPSQLHelper.EVENTS_TABLE, null, contentValues,SQLiteDatabase.CONFLICT_IGNORE);
                }
                return APPEvent.GetRecordFromJson(tmpJs);
            }else{
                HttpURLConnection conn;
                URL tmpGet = new URL(eventsURLBy_id+"/"+_id);
                conn = (HttpURLConnection)tmpGet.openConnection();
                conn.setConnectTimeout(5*1000);
                conn.setReadTimeout(3*1000);
                if(conn.getResponseCode()!=200){
                    return null;
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String jsonText = in.readLine();
                JSONObject tmpJs = new JSONObject(jsonText).getJSONObject("data");

                ContentValues contentValues = new ContentValues();
                contentValues.put("_id",tmpJs.getString("_id"));
                contentValues.put("json",tmpJs.toString());
                contentValues.put("watched",1);
                synchronized (APPSQLHelper.class) {
                    database.insertWithOnConflict(APPSQLHelper.EVENTS_TABLE, null, contentValues,SQLiteDatabase.CONFLICT_IGNORE);
                }
                return APPEvent.GetRecordFromJson(tmpJs);
            }
        }catch (IOException | JSONException e){
            System.out.println(e);
            return null;
        }
    }

    public ArrayList<APPEvent> getOriginEvents(String type){
        try {
            Cursor cursor = database.query(APPSQLHelper.EVENTS_TABLE,new String[]{"json","count(*)","sum(watched)"},null,null,null,null,null);
            ArrayList<APPEvent> tmplist = new ArrayList<>();
            while(cursor.moveToNext()){
                APPEvent tmp = APPEvent.GetRecordFromJson(new JSONObject(cursor.getString(0)));
                if(cursor.getInt(2) == 1){
                    tmp.setWatched();
                }
                if(cursor.getInt(1) == 2 || cursor.getInt(2) == 0){
                    if(tmp.getType().equals(type)){
                        tmplist.add(tmp);
                    }
                }
            }
            return tmplist;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //please run in thread
    public ArrayList<APPEvent> getPaperMore(){
        return getEventsWithParameter("paper", ++paperPage, 10);
    }
    //please run in thread
    public ArrayList<APPEvent> getNewsMore(){
        return getEventsWithParameter("news", ++newsPage, 10);
    }
    //all 1 20
    private ArrayList<APPEvent> getEventsWithParameter(String type, int page, int size){
        HttpURLConnection conn;
        ArrayList<APPEvent> tmpList = new ArrayList<>();
        try {
            URL tmpGet = new URL(eventsURL+"?type="+type+"&page="+page+"&size="+size);
            conn = (HttpURLConnection)tmpGet.openConnection();
            conn.setConnectTimeout(5*1000);
            conn.setReadTimeout(3*1000);
            if(conn.getResponseCode()!=200){
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JSONObject Js = new JSONObject(in.readLine());
            JSONArray tmpJsList = Js.getJSONArray("data");

            int len = tmpJsList.length();
            for(int i = 0; i < len; i++){
                JSONObject tmpJs = tmpJsList.getJSONObject(i);
                tmpList.add(APPEvent.GetRecordFromJson(tmpJs));
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id",tmpJs.getString("_id"));
                contentValues.put("json",tmpJs.toString());
                contentValues.put("watched",0);
                synchronized (APPSQLHelper.class) {
                    database.insertWithOnConflict(APPSQLHelper.EVENTS_TABLE, null, contentValues,SQLiteDatabase.CONFLICT_IGNORE);
                }
            }
        }catch (IOException | JSONException e){
            System.out.println(e);
            return null;
        }

        return tmpList;
    }

}
