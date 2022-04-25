package com.tokyonth.weather.utils

import android.content.Context
import android.util.Log
import com.tokyonth.weather.App
import kotlin.Throws

import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

object AssetsZipUtils {

    @Throws(Exception::class)
    fun unZipAssetsFolder(context: Context, zipFileString: String, outPathString: String) {
        val inPutZip = ZipInputStream(
            context.assets.open(
                zipFileString
            )
        )
        var zipEntry: ZipEntry?
        var szName: String?
        while (inPutZip.nextEntry.also { zipEntry = it } != null) {
            szName = zipEntry!!.name
            if (zipEntry!!.isDirectory) {
                szName = szName.substring(0, szName.length - 1)
                val folder = File(outPathString + File.separator + szName)
                if (!folder.exists()) {
                    folder.mkdirs()
                } else {
                    return
                }
            } else {
                val file = File(outPathString + File.separator + szName)
                if (!file.exists()) {
                    file.parentFile?.mkdirs()
                    file.createNewFile()
                }
                val out = FileOutputStream(file)
                var len: Int
                val buffer = ByteArray(1024)
                while (inPutZip.read(buffer).also { len = it } != -1) {
                    out.write(buffer, 0, len)
                    out.flush()
                }
                out.close()
            }
        }
        inPutZip.close()
    }

    fun getFilePathByName(name: String, isFillStyle: Boolean = false): String {
        val end = if (isFillStyle) {
            "-fill.svg"
        } else {
            ".svg"
        }
        return App.context.cacheDir.absolutePath + "/icons/" + name + end
    }

    fun getFilesAllName(path: String): List<String>? {
        val files = File(path).listFiles()
        if (files == null) {
            Log.e("error", "null folder")
            return null
        }

        return files.map {
            it.absolutePath
        }
    }

}
