package com.didi.carmate.dreambox.core.render;

import android.view.View;
import android.widget.ImageView;

import com.didi.carmate.dreambox.core.base.DBBaseView;
import com.didi.carmate.dreambox.core.base.DBContext;
import com.didi.carmate.dreambox.core.base.INodeCreator;
import com.didi.carmate.dreambox.core.data.DBData;
import com.didi.carmate.dreambox.core.render.view.DBPatchDotNineView;
import com.didi.carmate.dreambox.core.utils.DBLogger;
import com.didi.carmate.dreambox.core.utils.DBScreenUtils;
import com.didi.carmate.dreambox.core.utils.DBUtils;
import com.didi.carmate.dreambox.wrapper.ImageLoader;
import com.didi.carmate.dreambox.wrapper.Wrapper;

import java.util.Map;

/**
 * author: chenjing
 * date: 2020/4/30
 */
public class DBImage extends DBBaseView<View> {
    private String srcType;
    private String scaleType;
    private int maxWidth;
    private int maxHeight;

    protected DBImage(DBContext dbContext) {
        super(dbContext);
    }

    @Override
    public void onParserAttribute(Map<String, String> attrs) {
        super.onParserAttribute(attrs);

        srcType = getString(attrs.get("srcType"));
    }

    @Override
    protected View onCreateView() {
        View view;
        if ("ninePatch".equals(srcType)) {
            view = new DBPatchDotNineView(mDBContext.getContext());
        } else {
            view = new ImageView(mDBContext.getContext());
        }
        return view;
    }

    @Override
    public void onAttributesBind(View selfView, Map<String, String> attrs) {
        super.onAttributesBind(selfView, attrs);

        String src = getString(attrs.get("src"));
        scaleType = getString(attrs.get("scaleType"));
        maxWidth = DBScreenUtils.processSize(mDBContext, attrs.get("maxWidth"), 0);
        maxHeight = DBScreenUtils.processSize(mDBContext, attrs.get("maxHeight"), 0);

        loadImage(selfView, src);
    }

    @Override
    protected void onDataChanged(final View selfView, final String key) {
        mDBContext.observeStringData(new DBData.IDataObserver<String>() {
            @Override
            public void onDataChanged(String key, String oldValue, String newValue) {
                DBLogger.d(mDBContext, "key: " + key + " oldValue: " + oldValue + " newValue: " + newValue);
                if (null != newValue) {
                    loadImage(selfView, getString(newValue));
                }
            }

            @Override
            public String getKey() {
                return key;
            }
        });
    }

    private void loadImage(View view, String src) {
        ImageLoader imageLoader = Wrapper.get(mDBContext.getAccessKey()).imageLoader();

        if ("ninePatch".equals(srcType) && view instanceof DBPatchDotNineView) {
            DBPatchDotNineView ninePatchView = (DBPatchDotNineView) view;
            // maxWidth
            if (maxWidth != 0) {
                ninePatchView.setMaxWidth(maxWidth);
            }
            // maxHeight
            if (maxHeight != 0) {
                ninePatchView.setMaxHeight(maxHeight);
            }
            if (!DBUtils.isEmpty(src)) {
                imageLoader.load(src, ninePatchView);
            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            if (maxWidth != 0 || maxHeight != 0) {
                imageView.setAdjustViewBounds(true);
            }
            // maxWidth
            if (maxWidth != 0) {
                imageView.setMaxWidth(maxWidth);
            }
            // maxHeight
            if (maxHeight != 0) {
                imageView.setMaxHeight(maxHeight);
            }

            // scaleType
            if ("crop".equals(scaleType)) {
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else if ("inside".equals(scaleType)) {
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else if ("fitXY".equals(scaleType)) {
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            // src
            if (!DBUtils.isEmpty(src)) {
                imageLoader.load(src, imageView);
            }
        }
    }

    public static String getNodeTag() {
        return "image";
    }

    public static class NodeCreator implements INodeCreator {
        @Override
        public DBImage createNode(DBContext dbContext) {
            return new DBImage(dbContext);
        }
    }
}
