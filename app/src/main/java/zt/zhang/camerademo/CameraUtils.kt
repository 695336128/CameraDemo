package zt.zhang.camerademo

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
            if (option.height == option.width * h / w && option.width >= width && option.height >= height){
                bigEnough.add(option)
            }
        }
        // 如果找到多个预览尺寸，获取其中面积最小的
        if (bigEnough.size > 0){
            return bigEnough.minWith(CompareSizesByArea())!!
        }else{
            println("找不到合适的预览尺寸")
            return choices[0]
        }
    }

    class CompareSizesByArea: Comparator<Size>{

        override fun compare(o1: Size?, o2: Size?): Int {
            // 强转为long保证不会发生溢出
            return java.lang.Long.signum(o1?.getWidth() as Long * o1.getHeight() - o2?.getWidth() as Long * o2.getHeight())
        }
    }
}