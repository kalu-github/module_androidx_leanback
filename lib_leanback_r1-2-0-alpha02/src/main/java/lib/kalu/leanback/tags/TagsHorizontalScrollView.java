package lib.kalu.leanback.tags;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.List;

import lib.kalu.leanback.tags.model.TagBean;

@SuppressLint("NewApi")
@Keep
class TagsHorizontalScrollView extends HorizontalScrollView {
    public TagsHorizontalScrollView(Context context) {
        super(context);
        init();
    }

    public TagsHorizontalScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TagsHorizontalScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TagsHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private final void init() {
        setFillViewport(true);
        setFocusable(false);
        setFocusableInTouchMode(false);
    }

    /********************/

    protected final void update(@NonNull String key, @NonNull List<TagBean> list, @NonNull int textSize, @NonNull int paddingLeft, @NonNull int paddingRight) {

        if (null == key || key.length() == 0 || null == list || list.size() == 0)
            return;
        TagsLinearLayoutChild layout = new TagsLinearLayoutChild(getContext());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);

        layout.update(key, list, textSize, paddingLeft, paddingRight);
        addView(layout);
    }

    protected final void callback(@NonNull int column) {
        try {
            ViewGroup viewGroup = (ViewGroup) getParent();
            int row = viewGroup.indexOfChild(this);
            ((TagsLayout) getParent()).callback(row, column);
        } catch (Exception e) {
        }
    }

    protected final String[] searchTags() {
        int select = -1;
        TagsLinearLayoutChild layout = (TagsLinearLayoutChild) getChildAt(0);
        int count = layout.getChildCount();
        for (int i = 0; i < count; i++) {
            TagsTextView child = (TagsTextView) layout.getChildAt(i);
            if (child.isHightlight()) {
                select = i;
                break;
            }
        }
        if (select == -1) {
            select = 0;
        }
        TagsTextView child = (TagsTextView) layout.getChildAt(select);
        CharSequence hint = child.getHint();
        CharSequence text = child.getText();
        return new String[]{String.valueOf(hint), String.valueOf(text)};
    }
}
