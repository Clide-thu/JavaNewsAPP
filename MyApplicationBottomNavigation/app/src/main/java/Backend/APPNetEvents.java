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
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        database.close();
    }

    public synchronized int deleteRecord(){
        return database.delete(APPSQLHelper.RECORD_TABLE,null,null);
    }

    public synchronized int deleteCached(String type){
        return database.delete(APPSQLHelper.CACHE_TABLE,"type=?",new String[]{type});
    }

    //please run in thread
    public ArrayList<APPEvent> getEventRecord(){
        try {
            Cursor cursor = database.query(APPSQLHelper.RECORD_TABLE,new String[]{"json"},null,null,null,null,"id desc");
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
        Cursor cursor = database.query(APPSQLHelper.RECORD_TABLE,new String[]{"json"},"_id=?",new String[]{_id},null,null,null);
        try {
            if(cursor.moveToNext()){
                JSONObject tmpJs = new JSONObject(cursor.getString(0));
                return APPEvent.GetRecordFromJson(tmpJs);
            }else{
                cursor = database.query(APPSQLHelper.CACHE_TABLE,new String[]{"json"},"_id=?",new String[]{_id},null,null,null);
                if(cursor.moveToNext()) {
                    JSONObject tmpJs = new JSONObject(cursor.getString(0));

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("_id",tmpJs.getString("_id"));
                    contentValues.put("json",tmpJs.toString());
                    contentValues.put("type",tmpJs.getString("type"));
                    synchronized (this) {
                        database.insertWithOnConflict(APPSQLHelper.RECORD_TABLE, null, contentValues,SQLiteDatabase.CONFLICT_IGNORE);
                    }

                    return APPEvent.GetRecordFromJson(tmpJs);
                }
                HttpURLConnection conn;
                URL tmpGet = new URL(eventsURLBy_id+"/"+_id);
                System.out.println(tmpGet.toString());
                conn = (HttpURLConnection)tmpGet.openConnection();
                conn.setConnectTimeout(5*1000);
                conn.setReadTimeout(10*1000);
                if(conn.getResponseCode()!=200){
                    return null;
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String jsonText = in.readLine();
                JSONObject tmpJs = new JSONObject(jsonText).getJSONObject("data");

                ContentValues contentValues = new ContentValues();
                contentValues.put("_id",tmpJs.getString("_id"));
                contentValues.put("json",tmpJs.toString());
                contentValues.put("type",tmpJs.getString("type"));
                synchronized (this) {
                    database.insertWithOnConflict(APPSQLHelper.RECORD_TABLE, null, contentValues,SQLiteDatabase.CONFLICT_IGNORE);
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
            Cursor cursor = database.query(APPSQLHelper.CACHE_TABLE,new String[]{"json"},"type=?",new String[]{type},null,null,"id asc");
            ArrayList<APPEvent> tmplist = new ArrayList<>();
            while(cursor.moveToNext()){
                APPEvent tmp = APPEvent.GetRecordFromJson(new JSONObject(cursor.getString(0)));
                if(database.query(APPSQLHelper.RECORD_TABLE,new String[]{"json"},"_id=?",new String[]{tmp.get_id()},null,null,null).moveToNext()){
                    tmp.setWatched();
                }
                tmplist.add(tmp);
            }
            return tmplist;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //please run in thread
    public synchronized ArrayList<APPEvent> getMore(String type) {
        if(type.equals("news")){
            return getEventsWithParameter(type, ++newsPage, 10);
        }else if(type.equals("paper")){
            return getEventsWithParameter(type, ++paperPage, 10);
        }
        return getEventsWithParameter(type, 1, 10);
    }

    //all 1 20
    private ArrayList<APPEvent> getEventsWithParameter(String type, int page, int size){
        HttpURLConnection conn;
        ArrayList<APPEvent> tmpList = new ArrayList<>();
        try {
            URL tmpGet = new URL(eventsURL+"?type="+type+"&page="+page+"&size="+size);
            System.out.println(eventsURL+"?type="+type+"&page="+page+"&size="+size);
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
                APPEvent tmpEvent = APPEvent.GetRecordFromJson(tmpJs);
                if(database.query(APPSQLHelper.RECORD_TABLE,new String[]{"json"},"_id=?",new String[]{tmpEvent.get_id()},null,null,null).moveToNext()){
                    tmpEvent.setWatched();
                }
                tmpList.add(tmpEvent);
            }
        }catch (IOException | JSONException e){
            System.out.println(e);
            return null;
        }

        return tmpList;
    }

    public synchronized void cacheEvents(ArrayList<APPEvent> json, String type){
        deleteCached(type);
        try {
            for(APPEvent tmp:json){
                JSONObject tmpJs = new JSONObject(tmp.getJson());
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id",tmpJs.getString("_id"));
                contentValues.put("json",tmpJs.toString());
                contentValues.put("type",tmpJs.getString("type"));
                database.insertWithOnConflict(APPSQLHelper.CACHE_TABLE, null, contentValues,SQLiteDatabase.CONFLICT_IGNORE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<APPEvent> search(String keyword){
        System.out.println(keyword);
        ContentValues contentValues = new ContentValues();
        contentValues.put("search",keyword);
        database.insertWithOnConflict(APPSQLHelper.SEARCH_TABLE, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        HttpURLConnection conn;
        ArrayList<APPEvent> tmpList = new ArrayList<>();
        try {
            URL tmpGet = new URL(eventsURL+"?size="+200);
            System.out.println(eventsURL+"?size="+200);
            conn = (HttpURLConnection)tmpGet.openConnection();
            conn.setConnectTimeout(5*1000);
            conn.setReadTimeout(10*1000);
            if(conn.getResponseCode()!=200){
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JSONObject Js = new JSONObject(in.readLine());
            System.out.println("read");
            JSONArray tmpJsList = Js.getJSONArray("data");

            int len = tmpJsList.length();
            for(int i = 0; i < len; i++){
                JSONObject tmpJs = tmpJsList.getJSONObject(i);
                APPEvent tmpEvent = APPEvent.GetRecordFromJson(tmpJs);
                if(tmpEvent.getTitle().contains(keyword)){
                    tmpList.add(tmpEvent);
                }
            }
        }catch (IOException | JSONException e){
            System.out.println(e);
            return null;
        }

        return tmpList;
    }

    public ArrayList<String> getSearchHistory(){
        ArrayList<String> tmp = new ArrayList<>();
        Cursor cursor = database.query(APPSQLHelper.SEARCH_TABLE,new String[]{"search"},null,null,null,null,"id desc");
        while(cursor.moveToNext()){
            tmp.add(cursor.getString(0));
        }
        return tmp;
    }
}
