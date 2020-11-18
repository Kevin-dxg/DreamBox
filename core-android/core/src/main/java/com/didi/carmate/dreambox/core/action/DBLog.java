package com.didi.carmate.dreambox.core.action;

import com.didi.carmate.dreambox.core.base.DBAction;
import com.didi.carmate.dreambox.core.base.DBConstants;
import com.didi.carmate.dreambox.core.base.DBContext;
import com.didi.carmate.dreambox.core.base.INodeCreator;
import com.didi.carmate.dreambox.core.utils.DBLogger;
import com.didi.carmate.dreambox.core.utils.DBUtils;
import com.didi.carmate.dreambox.wrapper.Log;
import com.didi.carmate.dreambox.wrapper.Wrapper;

import java.util.Map;

/**
 * author: chenjing
 * date: 2020/4/30
 */
public class DBLog extends DBAction {
    private DBLog(DBContext dbContext) {
        super(dbContext);
    }

    @Override
    protected void doInvoke(Map<String, String> attrs) {
        String level = attrs.get("level");
        String tag = attrs.get("tag");
        String msg = getString(attrs.get("msg"));

        if (DBUtils.isEmpty(level)) {
            DBLogger.e(mDBContext, "[level] is null");
            return;
        }
        if (DBUtils.isEmpty(msg)) {
            DBLogger.e(mDBContext, "[msg] is null");
            return;
        }

        Log dbLog = Wrapper.get(mDBContext.getAccessKey()).log();
        switch (level) {
            case DBConstants.LOG_LEVEL_E:
                dbLog.e("tag: " + tag + " msg: " + msg);
                break;
            case DBConstants.LOG_LEVEL_W:
                dbLog.w("tag: " + tag + " msg: " + msg);
                break;
            case DBConstants.LOG_LEVEL_I:
                dbLog.i("tag: " + tag + " msg: " + msg);
                break;
            case DBConstants.LOG_LEVEL_D:
                dbLog.d("tag: " + tag + " msg: " + msg);
                break;
            case DBConstants.LOG_LEVEL_V:
                dbLog.v("tag: " + tag + " msg: " + msg);
                break;
            default:
                break;
        }
    }

    public static class NodeCreator implements INodeCreator {
        @Override
        public DBLog createNode(DBContext dbContext) {
            return new DBLog(dbContext);
        }
    }

    public static String getNodeTag() {
        return "log";
    }
}