package Backend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class APPPaper extends APPEvent {
    private final String content;
    private final String[] authors;

    public APPPaper(JSONObject js) throws JSONException {
        super(js);
        content = js.getString("content");

        JSONArray authorsJs = js.getJSONArray("authors");
        int authorNum = authorsJs.length();
        authors = new String[authorNum];

        for(int i = 0; i < authorNum; i++){
            authors[i] = authorsJs.getJSONObject(i).getString("name");
        }
    }
    @Override
    public String getContent(){ return content; }
    public String[] getAuthor(){ return authors.clone(); }

    @Override
    public String getSourceAuthor() {
        String a = "";
        for(String tmp:getAuthor()){
            a = a + tmp +"\n";
        }
        return a;
    }
}
