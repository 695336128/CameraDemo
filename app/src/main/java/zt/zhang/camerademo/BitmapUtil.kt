package zt.zhang.camerademo

import android.graphics.Bitmap
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * Created by zhang .
 * DATA: 2019/1/2 .
 * Description :
 */
object BitmapUtil {
    public fun saveImage2File(bitmap: Bitmap): String? {
        val path = Environment.getExternalStorageDirectory().path + "/1/"
        var fileDir: File? = null
        var filePic: File? = null
        val fileName = Date().time
        var fos: FileOutputStream? = null
        try {
            fileDir = File(path)
            if (!fileDir.exists()){
                fileDir.mkdirs()
            }
            println("$path$fileName.jpg")
            filePic = File("$path$fileName.jpg")
            if (!filePic.exists()) {
                filePic.parentFile.mkdirs()
                filePic.createNewFile()
            }
            fos = FileOutputStream(filePic)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            fos?.flush()
            fos?.close()
        }
        return filePic?.absolutePath
    }
}