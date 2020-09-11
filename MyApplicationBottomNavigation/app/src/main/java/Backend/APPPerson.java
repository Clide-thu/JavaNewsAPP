package Backend;

import org.json.JSONException;
import org.json.JSONObject;

public class APPPerson {
    private String photoPath="";
    private String name="";
    private String name_zh="";
    private String affiliation="";
    private String bio="";
    private String position="";
    private Boolean is_passedaway=false;
    private String json="";
    private String work="";
    private String edu="";

    public APPPerson(JSONObject js){
        try {
            json = js.toString();
            photoPath = js.optString("avatar","");
            name = js.optString("name","");
            name_zh = js.optString("name_zh","");
            JSONObject profile = js.getJSONObject("profile");
            if(profile!=null){
                affiliation = profile.optString("affiliation","");
                bio = profile.optString("bio","");
                position = profile.optString("position","");
                work = profile.optString("work","");
                edu = profile.optString("edu","");
            }
            is_passedaway = js.getBoolean("is_passedaway");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Boolean getIs_passedaway() {
        return is_passedaway;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public String getBio() {
        return bio;
    }

    public String getJson() {
        return json;
    }

    public String getName() {
        return name;
    }

    public String getName_zh() {
        return name_zh;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public String getPosition() {
        return position;
    }

    public String getEdu() {
        return edu;
    }

    public String getWork() {
        return work;
    }
}

