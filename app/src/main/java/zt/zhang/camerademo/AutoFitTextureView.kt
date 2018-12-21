package zt.zhang.camerademo

import android.content.Context
import android.util.AttributeSet
import android.view.TextureView

/**
 * Created by zhang .
 * DATA: 2018/12/20 .
 * Description : 照相机预览界面
 */
class AutoFitTextureView(context: Context?, attrs: AttributeSet?) : TextureView(context, attrs) {

    private var mRatioWidth = 0
    private var mRatioHeight = 0

    public fun setAspectRatio(width: Int, height: Int) {
        mRatioWidth = width
        mRatioHeight = height
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height)
        } else {
            if (width < height * mRatioWidth / mRatioHeight){
                setMeasuredDimension(width,width * mRatioHeight / mRatioWidth)
            } else{
                setMeasuredDimension(height * mRatioWidth / mRatioHeight,height)
            }
        }
    }
}