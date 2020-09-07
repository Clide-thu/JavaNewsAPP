package Backend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class APPEntity {
    private String label;
    private String introduce;
    private LinkedHashMap<String, String> properties = new LinkedHashMap<>();
    private ArrayList<APPEntityRelation> relations = new ArrayList<>();
    private String imgPath;

    public APPEntity(JSONObject js){
        try {
            label = js.getString("label");
            JSONObject abstractInfo = js.getJSONObject("abstractInfo");
            for(String source:new String[]{"enwiki","baidu","zhwiki"}){
                String tmpIntro = js.getString("source");
                if(!tmpIntro.equals("")){
                    introduce = tmpIntro;
                    break;
                }
            }

            JSONObject COVID = js.getJSONObject("COVID");
            JSONObject propertiesJs = js.getJSONObject("properties");
            JSONArray propertyList = propertiesJs.names();
            int tmpLen = propertyList.length();
            for(int i = 0; i < tmpLen; i++){
                String tmpString = propertyList.getString(i);
                properties.put(tmpString,propertiesJs.getString(tmpString));
            }

            JSONArray relationsJs = COVID.getJSONArray("relations");
            tmpLen = relationsJs.length();
            for(int i = 0; i < tmpLen; i++){
                relations.add(new APPEntityRelation(relationsJs.getJSONObject(i)));
            }

            Object tmp = js.get("img");
            if(tmp == JSONObject.NULL){
                imgPath = null;
            }else{
                imgPath = (String)tmp;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getLabel() {
        return label;
    }

    public ArrayList<APPEntityRelation> getRelations() {
        return (ArrayList<APPEntityRelation>)relations.clone();
    }

    public LinkedHashMap<String, String> getProperties() {
        return (LinkedHashMap<String, String>)properties.clone();
    }
}
