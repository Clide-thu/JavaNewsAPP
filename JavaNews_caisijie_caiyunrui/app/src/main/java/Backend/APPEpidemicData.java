package Backend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class APPEpidemicData {
    static int length = 15;
    private String region;
    private int[] confirmed = new int[length];
    private int[] cured = new int[length];
    private int[] dead = new int[length];

    public APPEpidemicData(String region, JSONObject js){
        this.region = region;
        try {
            JSONArray tmpArray = js.getJSONArray("data");
            int len = tmpArray.length();
            for(int i = 1; i <= len && i <= length; i++){
                JSONArray tmpData = tmpArray.getJSONArray(len-i);
                confirmed[length-i] = tmpData.getInt(0);
                cured[length-i] = tmpData.getInt(2);
                dead[length-i] = tmpData.getInt(3);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getRegion(){ return region; }
    public int[] getConfirmed(){ return confirmed.clone(); }
    public int[] getCured(){ return cured.clone(); }
    public int[] getDead(){ return dead.clone(); }
}
