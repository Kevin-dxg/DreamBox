package com.didi.carmate.dreambox.core.base;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;

import com.didi.carmate.dreambox.core.render.IDBContainer;
import com.didi.carmate.dreambox.core.render.IDBRender;
import com.didi.carmate.dreambox.core.utils.DBLogger;

import java.util.List;
import java.util.Map;

/**
 * author: chenjing
 * date: 2020/5/7
 */
public abstract class DBContainer<V extends ViewGroup> extends DBAbsView<V> implements IDBContainer<V> {
    private Map<String, String> parentAttrs;

    public DBContainer(DBContext dbContext) {
        super(dbContext);
    }

    /**
     * 容器节点自身的 mNativeView 对象有3种情况
     * 1. 根节点 此种情况 container 参数为null，需要调用 onCreateView 来创建自己的 mNativeView
     * 2. Adapter节点，在 onCreateViewHolder 创建了跟容器，container 为创建的跟容器对象，不需要创建
     * 3. 作为普通节点，container 为当前对象的父容器，需要调用 onCreateView 创建自己的 mNativeView
     */

    @Override
    public void bindView(NODE_TYPE nodeType) {
        bindView(null, nodeType);
    }

    @Override
    public void bindView(ViewGroup container, NODE_TYPE nodeType) {
        bindView(container, nodeType, false);
    }

    @Override
    public void bindView(ViewGroup container, NODE_TYPE nodeType, boolean bindAttrOnly) {
        if (null != mNativeView) {
            doBind(mNativeView, bindAttrOnly);
        } else if (nodeType == NODE_TYPE.NODE_TYPE_NORMAL) {
            mNativeView = onCreateView(); // 回调子类View实现
            doBind(mNativeView, bindAttrOnly);
            addToParent(mNativeView, container);
        } else if (nodeType == NODE_TYPE.NODE_TYPE_ADAPTER) {
            mNativeView = container;
            doBind(mNativeView, bindAttrOnly);
            addToParent(mNativeView, container);
        } else if (nodeType == NODE_TYPE.NODE_TYPE_ROOT) {
            mNativeView = onCreateView();
            doBind(mNativeView, bindAttrOnly);
        } else {
            DBLogger.e(mDBContext, "DBContainer::bindView, should not go here!");
        }

        // 递归子节点的bindView
        List<IDBNode> children = getChildren();
        for (IDBNode child : children) {
            if (child instanceof IDBRender) {
                ((IDBRender) child).bindView((ViewGroup) mNativeView, NODE_TYPE.NODE_TYPE_NORMAL, bindAttrOnly);
            }
        }
    }

    private void doBind(View nativeView, boolean bindAttrOnly) {
        if (null != nativeView && bindAttrOnly) {
            onAttributesBind(getAttrs());
        } else {
            // id
            String rawId = getAttrs().get("id");
            if (null != rawId) {
                id = Integer.parseInt(rawId);
                nativeView.setId(id);
            }
            // layout 相关属性
            onParseLayoutAttr(getAttrs());
            // 绑定视图属性
            onAttributesBind(getAttrs());
            // 添加到父容器
            if (null == nativeView.getLayoutParams()) {
                nativeView.setLayoutParams(new ViewGroup.LayoutParams(width, height));
            }
        }
    }

    private void addToParent(View nativeView, ViewGroup container) {
        // DBLView里根节点调用bindView时container传null
        // 适配List和Flow场景，native view 已经在adapter里创建好，且无需执行添加操作
        if (null != container) {
            container.addView(nativeView);
            onViewAdded(container);
        }
    }

    @CallSuper
    @Override
    public void renderFinish() {
        List<IDBNode> children = getChildren();
        for (IDBNode child : children) {
            if (child instanceof IDBContainer) {
                ((IDBContainer<?>) child).renderFinish();
            }
        }
    }

    public Map<String, String> getParentAttrs() {
        return parentAttrs;
    }

    public void setParentAttrs(Map<String, String> parentAttrs) {
        this.parentAttrs = parentAttrs;
    }
}
