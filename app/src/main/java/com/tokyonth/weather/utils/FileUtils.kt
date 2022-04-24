package com.tokyonth.weather.utils

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ByteArrayOutputStream
import java.lang.Exception

object FileUtils {

    /**
     * 字符串保存到手机内存设备中
     */
    fun write(content: String, filePath: String) {
        try {
            val file = File(filePath)
            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
            val outStream = FileOutputStream(file)
            outStream.write(content.toByteArray())
            outStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 读取文件里面的内容
     */
    fun read(filePath: String): String? {
        val file = File(filePath)
        if (!file.exists()) {
            return null
        }
        return try {
            val fis = FileInputStream(file)
            val b = ByteArray(1024)
            var len: Int
            val bos = ByteArrayOutputStream()
            while (fis.read(b).also { len = it } != -1) {
                bos.write(b, 0, len)
            }
            val data = bos.toByteArray()
            bos.close()
            fis.close()
            String(data)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 删除已存储的文件
     */
    fun deleteFile(filePath: String) {
        try {
            val file = File(filePath)
            file.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 文件是否存在
     */
    fun isFileExists(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists()
    }

}
