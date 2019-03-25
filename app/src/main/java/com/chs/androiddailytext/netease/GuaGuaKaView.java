package com.chs.androiddailytext.netease;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.chs.androiddailytext.R;

/**
 * 作者：83734
 * 时间：2019/3/24
 * 描述：
 */
public class GuaGuaKaView extends View {
    private Paint mPaint;
    private Bitmap mBitmapRes,mDstBitmap,mSrcBitmap;
    private Path mPath = new Path();
    private float mEventX,mEventY;
    private Canvas mCanvas;
    public GuaGuaKaView(Context context) {
        this(context,null);
    }

    public GuaGuaKaView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GuaGuaKaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(30);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        //禁止硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE,null);

        mBitmapRes = BitmapFactory.decodeResource(getResources(), R.mipmap.guagua);
        mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.gua);
        mDstBitmap = Bitmap.createBitmap(mSrcBitmap.getWidth(),mSrcBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mDstBitmap);
//        mCanvas.drawBitmap(mSrcBitmap,0,0,null);
//        mDstBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.gua);
//        mSrcBitmap = Bitmap.createBitmap(mDstBitmap.getWidth(),mDstBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制结果图片
        canvas.drawBitmap(mBitmapRes,0,0,null);


//        //第一种 先绘制遮罩层在绘制path,path的画笔使用DST_OUT模式
//        mCanvas.drawBitmap(mSrcBitmap,0,0,null);
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
//        mCanvas.drawPath(mPath,mPaint);
//        //绘制目标图像
//        canvas.drawBitmap(mDstBitmap,0,0,null);

          //第二种//遮罩层绘制在path上面，遮罩层的画笔使用SRC_OUT模式
        //新建一个图层，不然会把原始图层也当成dst层了。上面的方法之所以不用起一个新图层，因为遮罩层和path都是绘制在新new的canvas中了。
        int layerId = canvas.saveLayer(0,0,getWidth(),getHeight(),mPaint,Canvas.ALL_SAVE_FLAG);
        //绘制路径
//        Canvas dstCanvas = new Canvas(mDstBitmap);
        mCanvas.drawPath(mPath,mPaint);
        //绘制目标图像
        canvas.drawBitmap(mDstBitmap,0,0,mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

        canvas.drawBitmap(mSrcBitmap,0,0,mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(layerId);




    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mEventX = event.getX();
                mEventY = event.getY();
                mPath.moveTo(mEventX,mEventY);
                Log.e("TAG","mEventX"+mEventX+"--mEventY"+mEventY);
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = (mEventX+event.getX())/2;
                float endY = (mEventY+event.getY())/2;
                mPath.quadTo(mEventX,mEventY,endX,endY);
//                mPath.lineTo(event.getX(),event.getY());
                mEventX = event.getX();
                mEventY = event.getY();
                Log.e("TAG------","mEventX"+mEventX+"--mEventY"+mEventY);
                invalidate();
                break;
        }
        return true;
    }
}
