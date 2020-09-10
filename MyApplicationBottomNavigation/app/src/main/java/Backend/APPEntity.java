package Backend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class APPEntity {
    private String label;
    private String introduce="";
    private LinkedHashMap<String, String> properties = new LinkedHashMap<>();
    private ArrayList<APPEntityRelation> relations = new ArrayList<>();
    private String imgPath;
    private String json;

    public APPEntity(JSONObject js){
        json = js.toString();
        try {
            label = js.getString("label");
            System.out.println(label);
            JSONObject abstractInfo = js.getJSONObject("abstractInfo");
            for(String source:new String[]{"enwiki","baidu","zhwiki"}){
                String tmpIntro = abstractInfo.optString(source,"");
                if(!tmpIntro.equals("")){
                    introduce = tmpIntro;
                    break;
                }
            }
            System.out.println(introduce);

            JSONObject COVID = abstractInfo.getJSONObject("COVID");
            JSONObject propertiesJs = COVID.getJSONObject("properties");
            JSONArray propertyList = propertiesJs.names();
            if(propertyList!=null){
                int tmpLen = propertyList.length();
                for(int i = 0; i < tmpLen; i++){
                    String tmpString = propertyList.getString(i);
                    properties.put(tmpString,propertiesJs.getString(tmpString));
                }
            }


            JSONArray relationsJs = COVID.getJSONArray("relations");
            if(relationsJs != null){
                int tmpLen = relationsJs.length();
                for(int i = 0; i < tmpLen; i++){
                    relations.add(new APPEntityRelation(relationsJs.getJSONObject(i)));
                }
            }

            Object tmp = js.get("img");
            if(tmp == JSONObject.NULL){
                imgPath = null;
            }else{
                imgPath = (String)tmp;
            }
        } catch (JSONException e) {
            System.out.println(e);
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
        return relations;
    }

    public LinkedHashMap<String, String> getProperties() {
        return properties;
    }

    public String getJson(){ return json; }
}
