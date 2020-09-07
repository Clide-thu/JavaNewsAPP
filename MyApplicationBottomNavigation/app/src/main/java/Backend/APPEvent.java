package Backend;

import org.json.JSONException;
import org.json.JSONObject;

public class APPEvent {
    private final String _id;
    private final String date;
    private final String title;
    private final String type;
    private boolean watched = false;

    public static APPEvent GetRecordFromJson(JSONObject js) throws JSONException{
        String type = js.getString("type");
        if(type.equals("news")){
            return new APPNews(js);
        }else if(type.equals("paper")){
            return new APPPaper(js);
        }else if(type.equals("event")){
            return new APPEvent(js);
        }
        System.out.println("Js type no match!");
        return null;
    }

    public APPEvent(JSONObject js) throws JSONException {
        _id = js.getString("_id");
        date = js.getString("date");
        title = js.getString("title");
        type = js.getString("type");
    }

    public String get_id(){ return _id; }
    public String getDate(){ return date; }
    public String getTitle(){ return title; }

    public String getType() {
        return type;
    }
    public boolean getWatched(){ return watched; }

    public void setWatched(){ watched = true; }

}
