package Backend;

import org.json.JSONException;
import org.json.JSONObject;

public class APPNews extends APPEvent {
    private final String content;
    private final String source;

    public APPNews(JSONObject js) throws JSONException {
        super(js);
        content = js.getString("content");
        source = js.getString("source");
    }

    public String getContent(){ return content; }
    public String getSource(){ return source; }
}
