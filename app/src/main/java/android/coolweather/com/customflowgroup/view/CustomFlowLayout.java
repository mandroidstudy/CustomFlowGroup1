package android.coolweather.com.customflowgroup.view;

import android.content.Context;
import android.coolweather.com.customflowgroup.utils.LogUtil;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mao on 2018/2/20.
 */

public class CustomFlowLayout extends ViewGroup {
    Context mContext;
    /*
    * 构造
    * */
    public CustomFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
    }

    public CustomFlowLayout(Context context) {
        this(context,null);
    }

    /*
    * 使ViewGroup支持Margin
    * */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(mContext,attrs);
    }
    /*
    * 测量
    * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*
        * 父容器推荐的尺寸规格
        */
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
       /*
        * 根据子View确定尺寸
        */
        int width=0;
        int height=0;
        /*
         * 行高，行宽
         */
        int lineWidth=0;
        int lineHeight=0;
        //子View的个数
        int count=getChildCount();
        for (int i=0;i<count;i++){
            View child=getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            MarginLayoutParams mlp=(MarginLayoutParams)child.getLayoutParams();
            int childWidth=child.getMeasuredWidth()+mlp.leftMargin+mlp.rightMargin;
            int childHeight=child.getMeasuredHeight()+mlp.topMargin+mlp.bottomMargin;
            LogUtil.d("childWidth:"+childWidth);
            if ((lineWidth+childWidth)>widthSize){
                //换行
                height+=lineHeight;
                width=Math.max(width,lineWidth);
                //置0
                lineWidth=0;
                lineHeight=0;
            }
            lineWidth+=childWidth;
            lineHeight=Math.max(lineHeight,childHeight);
            if (i==count-1){
                 width=Math.max(width,lineWidth);
                 height+=lineHeight;
            }
        }
        /*
        * 若CustomFlowLayout在布局文件中的宽高是MeasureSpec.EXACTLY
        * 则直接采用父容器推荐的尺寸，反之，若是MeasureSpec.AT_MOST则根据
        * CustomFlowLayout中的子view设置相应的尺寸
        * */
        LogUtil.d("width:"+((widthMode==MeasureSpec.EXACTLY)?widthSize:width));
        LogUtil.d("height:"+((heightMode==MeasureSpec.EXACTLY)?heightSize:height));
        setMeasuredDimension((widthMode==MeasureSpec.EXACTLY)?widthSize:width,
                (heightMode==MeasureSpec.EXACTLY)?heightSize:height);
    }

    private ArrayList<ArrayList<View>> lines=new ArrayList<ArrayList<View>>();
    private ArrayList<Integer> mlineHeight=new ArrayList<Integer>();
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        lines.clear();
        mlineHeight.clear();

        int width=getWidth();
        LogUtil.d("width："+width);
        int count=getChildCount();
        int lineWidth=0;
        int lineHeight=0;

        ArrayList<View> line=new ArrayList<View>();
        for (int i=0;i<count;i++){
            View child=getChildAt(i);
            MarginLayoutParams mlp=(MarginLayoutParams)child.getLayoutParams();
            int childWidth=child.getMeasuredWidth()+mlp.leftMargin+mlp.rightMargin;
            int childHeight=child.getMeasuredHeight()+mlp.topMargin+mlp.bottomMargin;

            if ((lineWidth+childWidth)>width){
                //换行
                mlineHeight.add(lineHeight);
                lines.add(line);
                //置0
                lineWidth=0;
                lineHeight=0;
                line=new ArrayList<View>();
            }
            lineWidth+=childWidth;
            lineHeight=Math.max(lineHeight,childHeight);
            line.add(child);
            if (i==count-1){
                mlineHeight.add(lineHeight);
                lines.add(line);
            }
        }

        int left=0;
        int top=0;
        LogUtil.d("行数:"+lines.size());
        for (int i=0;i<lines.size();i++){
            ArrayList<View> mline=(ArrayList<View>)lines.get(i);
            LogUtil.d("第"+i+"行View数:"+mline.size());
            for (int j=0;j<mline.size();j++){
                View child=mline.get(j);
                MarginLayoutParams mlp=(MarginLayoutParams) child.getLayoutParams();

                int ml=left+mlp.leftMargin;
                int mt=top+mlp.topMargin;
                int mr=ml+child.getMeasuredWidth();
                int mb=mt+child.getMeasuredHeight();
                child.layout(ml,mt,mr,mb);
                LogUtil.d("ml:"+ml+"||mt:"+mt+"||mr:"+mr+"||mb"+mb);
                left+=mlp.leftMargin+child.getMeasuredWidth()+mlp.rightMargin;
            }
            left=0;
            top+=mlineHeight.get(i);
            LogUtil.d("top:"+top);
        }

    }
}
