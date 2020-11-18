package com.didi.carmate.dreambox.core.action;

import com.didi.carmate.dreambox.core.base.DBAction;
import com.didi.carmate.dreambox.core.base.DBContext;
import com.didi.carmate.dreambox.core.base.INodeCreator;
import com.didi.carmate.dreambox.core.utils.DBLogger;
import com.didi.carmate.dreambox.wrapper.Toast;
import com.didi.carmate.dreambox.wrapper.Wrapper;

import java.util.Map;

/**
 * author: chenjing
 * date: 2020/4/30
 */
public class DBToast extends DBAction {
    private DBToast(DBContext dbContext) {
        super(dbContext);
    }

    @Override
    protected void doInvoke(Map<String, String> attrs) {
        final String src = getString(attrs.get("src"));
        final boolean isLong = getBoolean(attrs.get("long"));

        if (null == src) {
            DBLogger.e(mDBContext, "[src] is null.");
            return;
        }

        Toast toast = Wrapper.get(mDBContext.getAccessKey()).toast();
        toast.show(mDBContext.getContext(), src, isLong);
    }

    public static class NodeCreator implements INodeCreator {
        @Override
        public DBToast createNode(DBContext dbContext) {
            return new DBToast(dbContext);
        }
    }

    public static String getNodeTag() {
        return "toast";
    }
}
