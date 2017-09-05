package wancheng.com.servicetypegovernment.view;

/**
 * Created by john on 2017/8/16.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ChildLiistView extends ListView {

    public ChildLiistView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ChildLiistView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public ChildLiistView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
