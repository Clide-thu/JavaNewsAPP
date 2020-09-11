package Backend;

import org.json.JSONException;
import org.json.JSONObject;

public class APPEntityRelation {
    private String relation;
    private String label;
    private boolean forward;

    public APPEntityRelation(JSONObject js){
        try {
            relation = js.getString("relation");
            label = js.getString("label");
            forward = js.getBoolean("forward");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getRelation() { return relation; }
    public String getLabel() { return label; }
    public boolean getForward(){ return forward; }
}
