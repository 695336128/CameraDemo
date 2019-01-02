package zt.zhang.camerademo

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.*

/**
 * Created by zhang .
 * DATA: 2018/12/28 .
 * Description :
 */
class NV21ToBitmap(context: Context) {
    var rs: RenderScript? = null
    var yuvToRgbIntrinsic: ScriptIntrinsicYuvToRGB? = null
    var yuvType: Type.Builder? = null
    var rgbaType: Type.Builder? = null
    var inAllocation : Allocation? = null
    var outAllocation : Allocation? = null

    init {
        rs = RenderScript.create(context)
        yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs))
    }

    public fun nv21ToBitmap(nv21: ByteArray, width: Int, height: Int): Bitmap? {
        if (yuvType == null) {
            yuvType = Type.Builder(rs, Element.U8(rs)).setX(nv21.size)
            inAllocation = Allocation.createTyped(rs,yuvType!!.create(),Allocation.USAGE_SCRIPT)

            rgbaType = Type.Builder(rs,Element.RGBA_8888(rs)).setX(nv21.size)
            outAllocation = Allocation.createTyped(rs,rgbaType!!.create())
        }
        inAllocation?.copyFrom(nv21)

        yuvToRgbIntrinsic?.setInput(inAllocation)
        yuvToRgbIntrinsic?.forEach(outAllocation)

        val bitmapOut = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        outAllocation?.copyTo(bitmapOut)

        println(bitmapOut.byteCount)

        return bitmapOut
    }

}