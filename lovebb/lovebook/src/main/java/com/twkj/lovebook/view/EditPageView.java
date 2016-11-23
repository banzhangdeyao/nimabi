package com.twkj.lovebook.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.icu.text.LocaleDisplayNames;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.twkj.lovebook.R;
import com.twkj.lovebook.bean.DraftBookContent;
import com.twkj.lovebook.bean.EditPageBean;
import com.twkj.lovebook.utils.BookcontentBeanUtils;

import java.lang.reflect.Field;

import static android.R.attr.angle;

/**
 * Created by wht on 2016/10/10.
 */

public class EditPageView extends ViewGroup {
    public EditPageView(Context context) {
        super(context);
    }

    public EditPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(582,437);
//        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //用来限定textview的长宽的方法
        int childCount=getChildCount();
        for(int i=3;i<childCount;i++){
            final View childView = getChildAt(i);
            DraftBookContent bean= (DraftBookContent) childView.getTag(R.string.app_name);
           if (bean.getIsTextOrImage().equals("text")){
               LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(dp2px(BookcontentBeanUtils.changeToPxOfInt(bean.getTextOrImageWidth())/2),0);
               childView.setLayoutParams(params);
           }
        }

        setMeasuredDimension(dp2px(777/2),dp2px(583/2));
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childViewCount=getChildCount();
        DraftBookContent bean=null;
        //model背景 左背景 有背景 这么多背景做宝搞
        if (childViewCount!=0) {
            View vmodel = getChildAt(0);
            vmodel.layout(0, 0, dp2px(777 / 2), dp2px(583 / 2));
            View vleft = getChildAt(1);
            vleft.layout(0, 0, dp2px(777 / 4), dp2px(583 / 2));
            View vright = getChildAt(2);
            vright.layout(dp2px(777 / 4), 0, dp2px(777 / 2), dp2px(583 / 2));

        for (int j = 3; j < childViewCount; j++){
            final View childView = getChildAt(j);
            bean= (DraftBookContent) childView.getTag(R.string.app_name);
            int cl = 0, ct = 0, cr = 0, cb = 0;
                cl= BookcontentBeanUtils.changeToPxOfInt(bean.getTextOrImageX());
                ct=BookcontentBeanUtils.changeToPxOfInt(bean.getTextOrImageY());
                cr=cl+BookcontentBeanUtils.changeToPxOfInt(bean.getTextOrImageWidth());
                cb=ct+BookcontentBeanUtils.changeToPxOfInt(bean.getTextOrImageHeight());
            Log.d("ttt","l:"+cl+" r:"+cr+" t:"+ct+" b:"+cb);

            cl=dp2px(cl/2);
            ct=dp2px(ct/2) ;
            cr=dp2px(cr/2);
            cb=dp2px(cb/2);

            childView.layout(cl, ct, cr, cb);
//            Log.d("aaa","旋转的角度："+bean.getRotationAngle());
            if (!TextUtils.isEmpty(bean.getRotationAngle())){
                //图片的旋转
            rotate( (ImageView)childView,Float.parseFloat(bean.getRotationAngle()));
            }
            childView.setBackgroundColor(getResources().getColor(R.color.pageViewBg));

        }
        }
    }
    //DP转像素的方法
    protected  int dp2px(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getResources().getDisplayMetrics());
    }


    //图片旋转的方法
    protected void rotate(ImageView img,float angel){
        Animation rotateAnimation  = new RotateAnimation(angel, angel, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        img.startAnimation(rotateAnimation);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
    }





}
