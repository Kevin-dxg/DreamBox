package com.airk.dreamhouse;

import android.view.View;

import com.airk.dreamhouse.demo.R;
import com.didi.carmate.dreambox.core.v4.base.DBAction;
import com.didi.carmate.dreambox.core.v4.base.DBConstants;
import com.didi.carmate.dreambox.core.v4.base.DBContext;
import com.didi.carmate.dreambox.core.v4.base.INodeCreator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

public class CXAddCartAction extends DBAction {
    private JsonElement jsonElement;

    private CXAddCartAction(DBContext dbContext) {
        super(dbContext);
    }

    @Override
    protected void onParserAttribute(Map<String, String> attrs) {
        super.onParserAttribute(attrs);

        jsonElement = getJsonArray(attrs.get("data"));
    }

    @Override
    protected void doInvoke(Map<String, String> attrs, View view, JsonObject data) {
        final JsonArray jsonArray;
        if (null == data) {
            jsonElement.getAsJsonArray();
        } else {
            jsonArray = getJsonArray(attrs.get("data"), data);
        }
    }

    public static class NodeCreator implements INodeCreator {
        @Override
        public CXAddCartAction createNode(DBContext dbContext) {
            return new CXAddCartAction(dbContext);
        }
    }

    public static String getNodeTag() {
        return "addCartAction";
    }
}
