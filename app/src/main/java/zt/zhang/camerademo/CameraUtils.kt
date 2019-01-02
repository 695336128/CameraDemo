package zt.zhang.camerademo

import android.media.Image
import android.util.Size


/**
 * Created by zhang .
 * DATA: 2018/12/21 .
 * Description : 工具类
 */
object CameraUtils {

    /**
     * 获取最佳尺寸
     */
    public fun chooseOptimalSize(choices: Array<Size>,width: Int,height: Int,aspectRatio: Size): Size{
        // 收集摄像头支持的打过预览Surface的分辨率
        val bigEnough = ArrayList<Size>()
        val w = aspectRatio.width
        val h = aspectRatio.height
        for (option in choices){
//            if (option.height == option.width * h / w && option.width >= width && option.height >= height){
                bigEnough.add(option)
//            println(option.width.toString() + " * " + option.height.toString())
//            }
        }
        // 如果找到多个预览尺寸，获取其中面积最小的
        if (bigEnough.size > 0){
            return bigEnough[3]
//            return bigEnough.minWith(CompareSizesByArea())!!
        }else{
            println("找不到合适的预览尺寸")
            return choices[0]
        }
    }

    class CompareSizesByArea: Comparator<Size>{

        override fun compare(o1: Size?, o2: Size?): Int {
            // 强转为long保证不会发生溢出
//            return o1!!.width  * o1.height - o2!!.width  * o2.height
                if (o1!!.width  * o1.height - o2!!.width  * o2.height > 0){
                return 1
            }else if (o1.width  * o1.height - o2.width  * o2.height == 0){
                return 0
            }else{
                return  -1
            }
        }
    }

//    fun imageToByteArray(image: Image): ByteArray? {
//        var data: ByteArray? = null
//        if (image.getFormat() == ImageFormat.JPEG) {
//            val planes = image.getPlanes()
//            val buffer = planes[0].getBuffer()
//            data = ByteArray(buffer.capacity())
//            buffer.get(data)
//            return data
//        } else if (image.getFormat() == ImageFormat.YUV_420_888) {
//            data = NV21toJPEG(YUV_420_888toNV21(image), image.width, image.height)
//        }
//        return data
//    }

    public fun YUV_420_888toNV21(image: Image): ByteArray {
        val nv21: ByteArray
        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer
        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()
        nv21 = ByteArray(ySize + uSize + vSize)
        // U and V are swapped
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)
        return nv21
    }


}