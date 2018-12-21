package com.zhang.common.commonutils

import android.content.Context
import java.io.File


/**
 * Created by zhang .
 * DATA: 2018/8/10 .
 * Description : 清除相关
 */
object CleanUtil {
    /**
     * 清除内部缓存
     *
     * directory: /data/data/package/cache
     */
    fun cleanInternalCache(context: Context): Boolean {
        return deleteFilesInDir(context.applicationContext.cacheDir)
    }

    /**
     * 清除内部文件
     *
     * directory: /data/data/package/files
     */
    fun cleanInternalFiles(context: Context): Boolean {
        return deleteFilesInDir(context.applicationContext.filesDir)
    }

    /**
     * 清除内部数据库
     *
     * directory: /data/data/package/databases
     */
    fun cleanInternalDbs(context: Context): Boolean {
        return deleteFilesInDir(File(context.applicationContext.filesDir.parent, "databases"))
    }


    /**
     * 清除内部 SP
     *
     * directory: /data/data/package/shared_prefs
     */
    fun cleanInternalSp(context: Context): Boolean {
        return deleteFilesInDir(File(context.applicationContext.filesDir.parent, "shared_prefs"))
    }



    ///////////////////////////////////////////////////////////////////////////
    // other utils methods
    ///////////////////////////////////////////////////////////////////////////

    private fun deleteFilesInDir(dir: File?): Boolean {
        if (dir == null) return false
        // dir doesn't exist then return true
        if (!dir!!.exists()) return true
        // dir isn't a directory then return false
        if (!dir!!.isDirectory()) return false
        val files = dir!!.listFiles()
        if (files != null && files!!.size != 0) {
            for (file in files!!) {
                if (file.isFile()) {
                    if (!file.delete()) return false
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false
                }
            }
        }
        return true
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir == null) return false
        // dir doesn't exist then return true
        if (!dir!!.exists()) return true
        // dir isn't a directory then return false
        if (!dir!!.isDirectory()) return false
        val files = dir!!.listFiles()
        if (files != null && files!!.size != 0) {
            for (file in files!!) {
                if (file.isFile()) {
                    if (!file.delete()) return false
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false
                }
            }
        }
        return dir!!.delete()
    }

    private fun getFileByPath(filePath: String): File? {
        return if (isSpace(filePath)) null else File(filePath)
    }

    private fun isSpace(s: String?): Boolean {
        if (s == null) return true
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return true
    }
}