package com.zhang.common.commonutils

import java.io.File
import java.io.FileFilter
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

//getFileByPath             : 根据文件路径获取文件
//isFileExists              : 判断文件是否存在
//rename                    : 重命名文件
//isDir                     : 判断是否是目录
//isFile                    : 判断是否是文件
//createOrExistsDir         : 判断目录是否存在，不存在则判断是否创建成功
//createOrExistsFile        : 判断文件是否存在，不存在则判断是否创建成功
//createFileByDeleteOldFile : 判断文件是否存在，存在则在创建之前删除
//deleteDir                 : 删除目录
//deleteFile                : 删除文件
//listFilesInDir            : 获取目录下所有文件
//listFilesInDirWithFilter  : 获取目录下所有过滤的文件
//getFileSize               : 获取文件大小



/**
 * Created by zhang .
 * DATA: 2018/8/10 .
 * Description : 文件相关
 */
object FileUtil {

    /**
     * 根据路径获取文件
     * @param filePath 文件路径
     * @return File 文件
     */
    fun getFileByPath(filePath: String?): File? {
        return if (isSpace(filePath)) null else File(filePath)
    }

    /**
     * 判断文件是否存在
     * @param filePath 文件路径
     */
    fun isFileExists(filePath: String?): Boolean {
        return getFileByPath(filePath) == null
    }

    /**
     * 重命名
     */
    fun rename(filePath: String?, newName: String): Boolean {
        return rename(getFileByPath(filePath), newName)
    }

    /**
     * 重命名
     */
    fun rename(file: File?, newName: String): Boolean {
        if (file == null) return false
        if (!file.exists()) return false
        if (isSpace(newName)) return false
        if (file.name == newName) return true
        val newFile = File(file.parent + File.separator + newName)
        return !newFile.exists() && file.renameTo(newFile)
    }

    /**
     * 是否是文件夹
     */
    fun isDir(filePath: String): Boolean {
        return isDir(getFileByPath(filePath))
    }

    /**
     * 是否是文件夹
     */
    fun isDir(file: File?): Boolean {
        return file != null && file.exists() && file.isDirectory
    }

    /**
     * 是否是文件
     */
    fun isFile(filePath: String): Boolean {
        return isFile(getFileByPath(filePath))
    }

    /**
     * 是否是文件
     */
    fun isFile(file: File?): Boolean {
        return file != null && file.exists() && file.isFile
    }

    /**
     * 新建文件夹，如果文件夹已存在则不做操作
     */
    fun createOrExistsDir(filePath: String?): Boolean {
        return createOrExistsDir(getFileByPath(filePath))
    }

    /**
     * 新建文件夹，如果文件夹已存在则不做操作
     */
    fun createOrExistsDir(file: File?): Boolean {
        return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
    }

    /**
     * 创建文件
     */
    fun createOrExistsFile(filePath: String?): Boolean {
        return createOrExistsFile(getFileByPath(filePath))
    }


    /**
     * 删除旧文件后创建文件
     */
    fun createFileByDeleteOldFile(filePath: String): Boolean {
        return createFileByDeleteOldFile(getFileByPath(filePath))
    }

    /**
     * 删除旧文件后创建文件
     */
    fun createFileByDeleteOldFile(file: File?): Boolean {
        if (file == null) return false
        // file exists and unsuccessfully delete then return false
        if (file.exists() && !file.delete()) return false
        if (!createOrExistsDir(file.parentFile)) return false
        return try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }

    }

    /**
     * 创建文件
     */
    fun createOrExistsFile(file: File?): Boolean {
        if (file == null) return false
        if (file.exists()) return file.isFile
        if (!createOrExistsDir(file.parentFile)) return false
        return try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 删除文件夹
     */
    fun deleteDir(dirPath: String): Boolean {
        return deleteDir(getFileByPath(dirPath))
    }

    /**
     * 删除文件夹
     */
    fun deleteDir(dir: File?): Boolean {
        if (dir == null) return false
        // dir doesn't exist then return true
        if (!dir.exists()) return true
        // dir isn't a directory then return false
        if (!dir.isDirectory) return false
        val files = dir.listFiles()
        if (files != null && files.isNotEmpty()) {
            for (file in files) {
                if (file.isFile) {
                    if (!file.delete()) return false
                } else if (file.isDirectory) {
                    if (!deleteDir(file)) return false
                }
            }
        }
        return dir.delete()
    }

    /**
     * 删除文件
     */
    fun deleteFile(srcFilePath: String): Boolean {
        return deleteFile(getFileByPath(srcFilePath))
    }

    /**
     * 删除文件
     */
    fun deleteFile(file: File?): Boolean {
        return file != null && (!file.exists() || file.isFile && file.delete())
    }

    /**
     * 获取目录下所有文件
     */
    fun listFilesInDir(dirPath: String): List<File>? {
        return listFilesInDir(getFileByPath(dirPath), false)
    }

    /**
     * 获取目录下所有文件
     */
    fun listFilesInDir(dir: File): List<File>? {
        return listFilesInDir(dir, false)
    }


    /**
     * Return the files in directory.
     *
     * @param dir         The directory.
     * @param isRecursive True to traverse subdirectories, false otherwise.
     * @return the files in directory
     */
    fun listFilesInDir(dir: File?, isRecursive: Boolean): List<File>? {
        return listFilesInDirWithFilter(dir, FileFilter { true }, isRecursive)
    }

    /**
     * Return the files that satisfy the filter in directory.
     *
     * Doesn't traverse subdirectories
     *
     * @param dirPath The path of directory.
     * @param filter  The filter.
     * @return the files that satisfy the filter in directory
     */
    fun listFilesInDirWithFilter(dirPath: String,
                                 filter: FileFilter): List<File>? {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter, false)
    }

    /**
     * Return the files that satisfy the filter in directory.
     *
     * Doesn't traverse subdirectories
     *
     * @param dir    The directory.
     * @param filter The filter.
     * @return the files that satisfy the filter in directory
     */
    fun listFilesInDirWithFilter(dir: File,
                                 filter: FileFilter): List<File> ?{
        return listFilesInDirWithFilter(dir, filter, false)
    }

    /**
     * Return the files that satisfy the filter in directory.
     *
     * @param dirPath     The path of directory.
     * @param filter      The filter.
     * @param isRecursive True to traverse subdirectories, false otherwise.
     * @return the files that satisfy the filter in directory
     */
    fun listFilesInDirWithFilter(dirPath: String,
                                 filter: FileFilter,
                                 isRecursive: Boolean): List<File>? {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter, isRecursive)
    }


    /**
     * Return the files that satisfy the filter in directory.
     *
     * @param dir         The directory.
     * @param filter      The filter.
     * @param isRecursive True to traverse subdirectories, false otherwise.
     * @return the files that satisfy the filter in directory
     */
    fun listFilesInDirWithFilter(dir: File?,
                                 filter: FileFilter,
                                 isRecursive: Boolean): List<File>? {
        if (!isDir(dir)) return null
        val list = ArrayList<File>()
        val files = dir?.listFiles()
        if (files != null && files.isNotEmpty()) {
            for (file in files) {
                if (filter.accept(file)) {
                    list.add(file)
                }
                if (isRecursive && file.isDirectory) {

                    list.addAll(listFilesInDirWithFilter(file, filter, true)!!)
                }
            }
        }
        return list
    }

    /**
     * Return the length of file.
     *
     * @param filePath The path of file.
     * @return the length of file
     */
    fun getFileSize(filePath: String): String {
        val len = getFileLength(filePath)
        return if (len == -1L) "" else byte2FitMemorySize(len?:-1L)
    }

    /**
     * Return the length of file.
     *
     * @param file The file.
     * @return the length of file
     */
    fun getFileSize(file: File): String {
        val len = getFileLength(file)
        return if (len == -1L) "" else byte2FitMemorySize(len?:-1L)
    }


    /**
     * Return the length of directory.
     */
    fun getDirLength(dirPath: String): Long {
        return getDirLength(getFileByPath(dirPath))
    }

    /**
     * Return the length of directory.
     *
     * @param dir The directory.
     * @return the length of directory
     */
    fun getDirLength(dir: File?): Long {
        if (!isDir(dir)) return -1
        var len: Long = 0
        val files = dir?.listFiles()
        if (files != null && files.isNotEmpty()) {
            for (file in files) {
                if (file.isDirectory) {
                    len += getDirLength(file)
                } else {
                    len += file.length()
                }
            }
        }
        return len
    }

    /**
     * Return the length of file.
     *
     * @param filePath The path of file.
     * @return the length of file
     */
    fun getFileLength(filePath: String): Long? {
        val isURL = filePath.matches("[a-zA-z]+://[^\\s]*".toRegex())
        if (isURL) {
            try {
                val conn = URL(filePath).openConnection() as HttpURLConnection
                conn.setRequestProperty("Accept-Encoding", "identity")
                conn.connect()
                return if (conn.responseCode == 200) {
                    conn.contentLength.toLong()
                } else -1
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return getFileLength(getFileByPath(filePath))
    }

    /**
     * Return the length of file.
     *
     * @param file The file.
     * @return the length of file
     */
    fun getFileLength(file: File?): Long? {
        return if (!isFile(file)) -1 else file?.length()
    }


    private fun byte2FitMemorySize(byteNum: Long): String {
        return if (byteNum < 0) {
            "shouldn't be less than zero!"
        } else if (byteNum < 1024) {
            String.format(Locale.getDefault(), "%.3fB", byteNum.toDouble())
        } else if (byteNum < 1048576) {
            String.format(Locale.getDefault(), "%.3fKB", byteNum.toDouble() / 1024)
        } else if (byteNum < 1073741824) {
            String.format(Locale.getDefault(), "%.3fMB", byteNum.toDouble() / 1048576)
        } else {
            String.format(Locale.getDefault(), "%.3fGB", byteNum.toDouble() / 1073741824)
        }
    }


    /**
     * 字符串判空
     */
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
