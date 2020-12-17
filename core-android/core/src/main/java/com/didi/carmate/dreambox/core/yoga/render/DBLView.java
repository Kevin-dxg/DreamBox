package com.didi.carmate.dreambox.core.yoga.render;

import android.view.View;
import android.view.ViewGroup;

import com.didi.carmate.dreambox.core.action.DBActionAlias;
import com.didi.carmate.dreambox.core.action.DBActionAliasItem;
import com.didi.carmate.dreambox.core.base.DBAction;
import com.didi.carmate.dreambox.core.base.DBCallback;
import com.didi.carmate.dreambox.core.base.DBCallbacks;
import com.didi.carmate.dreambox.core.base.DBConstants;
import com.didi.carmate.dreambox.core.base.DBContext;
import com.didi.carmate.dreambox.core.base.DBNode;
import com.didi.carmate.dreambox.core.base.IDBCoreView;
import com.didi.carmate.dreambox.core.base.IDBNode;
import com.didi.carmate.dreambox.core.base.INodeCreator;
import com.didi.carmate.dreambox.core.bridge.DBOnEvent;
import com.didi.carmate.dreambox.core.data.DBData;
import com.didi.carmate.dreambox.core.utils.DBLogger;
import com.didi.carmate.dreambox.core.utils.DBUtils;
import com.didi.carmate.dreambox.core.yoga.render.view.DBCoreViewH;
import com.didi.carmate.dreambox.core.yoga.render.view.DBCoreViewV;
import com.didi.carmate.dreambox.core.yoga.render.view.DBYogaView;
import com.didi.carmate.dreambox.core.yoga.render.view.DreamBoxCoreView;
import com.facebook.yoga.YogaFlexDirection;
import com.facebook.yoga.YogaJustify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author: chenjing
 * date: 2020/4/30
 */
public class DBLView extends DBNode {
    private final List<DBCallback> mCallbacks = new ArrayList<>();
    private DBOnEvent mDBEvent;
    private DBRender mDBRender;
    private List<DBActionAliasItem> mActionAliasItems;

    // 根节点id默认0，其他视图节点id默认值-1
    private String changeOn;
    private String dismissOn;
    private String scroll;

    private IDBCoreView mDBCoreView;
    private DBYogaView mDBRootView;

    private DBLView(DBContext dbContext) {
        super(dbContext);
    }

    @Override
    public void onParserAttribute(Map<String, String> attrs) {
        super.onParserAttribute(attrs);

        changeOn = attrs.get("changeOn");
        dismissOn = attrs.get("dismissOn");
        scroll = attrs.get("scroll");
    }

    @Override
    public void onParserNode() {
        super.onParserNode();

        List<IDBNode> children = getChildren();
        for (IDBNode child : children) {
            if (child instanceof DBRender) {
                mDBRender = (DBRender) child;
            } else if (child instanceof DBActionAlias) {
                DBActionAlias actionAliasVNode = (DBActionAlias) child;
                mActionAliasItems = actionAliasVNode.getActionAliasItems();
            } else if (child instanceof DBCallbacks) { // callback 集合
                List<IDBNode> callbacks = child.getChildren();
                for (IDBNode callback : callbacks) {
                    mCallbacks.add((DBCallback) callback);
                }
            }
        }
    }

    @Override
    protected void onParserNodeFinished() {
        super.onParserNodeFinished();

        generateRootView();
        bindViewAttr();
        mDBRender.bindView(mDBRootView); // view结构的调用源头
        mDBRender.renderFinish();
    }

    /**
     * 重新刷新整个模板
     */
    public void invalidate() {
        mDBRender.parserAttribute(); // 视图节点部分属性放到此生命周期里解析，需要重新执行一遍
        mDBRender.bindView(mDBRootView);
        mDBRender.renderFinish();
    }

    @Override
    public void release() {
        // 子节点资源释放
        List<IDBNode> children = getChildren();
        if (children.size() > 0) {
            for (IDBNode child : children) {
                child.release();
            }
        }
    }

    public void onCreate() {

    }

    public void onResume() {
        for (final DBCallback callback : mCallbacks) {
            if ("onVisible".equals(callback.getTagName())) {
                List<DBAction> actions = callback.getActionNodes();
                for (DBAction action : actions) {
                    action.invoke();
                }
            }
        }
    }

    public void onPause() {
        for (final DBCallback callback : mCallbacks) {
            if ("onInvisible".equals(callback.getTagName())) {
                List<DBAction> actions = callback.getActionNodes();
                for (DBAction action : actions) {
                    action.invoke();
                }
            }
        }
    }

    public void onDestroy() {
        release();
    }

    public DBOnEvent getDBOnEvent() {
        if (null == mDBEvent) {
            List<IDBNode> children = getChildren();
            for (IDBNode child : children) {
                if (child instanceof DBCallbacks) {
                    List<IDBNode> callbacks = child.getChildren();
                    for (IDBNode callback : callbacks) {
                        if ("onEvent".equals(callback.getTagName())) {
                            mDBEvent = (DBOnEvent) callback;
                        }
                    }
                }
            }
        }
        return mDBEvent;
    }

    public IDBCoreView getDBCoreView() {
        return mDBCoreView;
    }

    public List<DBActionAliasItem> getActionAliasItems() {
        return mActionAliasItems;
    }

    private void generateRootView() {
        if (!DBUtils.isEmpty(scroll)) {
            if (null == mDBCoreView) {
                mDBRootView = new DBYogaView(mDBContext);
                if (DBConstants.STYLE_ORIENTATION_V.equals(scroll)) {
                    mDBCoreView = new DBCoreViewV(mDBContext, mDBRootView);
                } else if (DBConstants.STYLE_ORIENTATION_H.equals(scroll)) {
                    mDBCoreView = new DBCoreViewH(mDBContext, mDBRootView);
                }
            }
        } else if (null == mDBRootView) {
            DreamBoxCoreView coreView = new DreamBoxCoreView(mDBContext);
            mDBCoreView = coreView;
            mDBRootView = coreView;
        }
        mDBRootView.getYogaNode().setFlexDirection(YogaFlexDirection.ROW);
        mDBRootView.getYogaNode().setJustifyContent(YogaJustify.SPACE_BETWEEN);
    }

    private void bindViewAttr() {
        View view = mDBCoreView.getView();
        if (null == view.getLayoutParams()) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(lp);
        }

        // dismissOn
        if (null == dismissOn || getBoolean(dismissOn)) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }

        // observe meta [dismissOn]
        if (null != dismissOn) {
            mDBContext.observeDataPool(new DBData.IDataObserver() {
                @Override
                public void onDataChanged(String key) {
                    DBLogger.d(mDBContext, "key: " + key);
                    if (null != mDBCoreView) {
                        mDBCoreView.getView().setVisibility(getBoolean(dismissOn) ? View.GONE : View.VISIBLE);
                    }
                }

                @Override
                public String getKey() {
                    return dismissOn;
                }
            });
        }

        // observe meta [changeOn]
        if (null != changeOn) {
            String[] keys = changeOn.split("\\|");
            for (final String key : keys) {
                mDBContext.observeDataPool(new DBData.IDataObserver() {
                    @Override
                    public void onDataChanged(String key) {
                        DBLogger.d(mDBContext, "key: " + key);
                        if (null != mDBCoreView) {
                            mDBCoreView.requestRender();
                        }
                    }

                    @Override
                    public String getKey() {
                        return key;
                    }
                });
            }
        }
    }

    public static String getNodeTag() {
        return "dbl";
    }

    public static class NodeCreator implements INodeCreator {
        @Override
        public DBLView createNode(DBContext dbContext) {
            return new DBLView(dbContext);
        }
    }
}
