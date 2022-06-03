/*
 * *
 *  * Created by Ali Ashkeyev on 19.05.2022, 9:51
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 19.05.2022, 9:51
 *
 */

package com.example.budka.utils

import android.util.Log
import com.pspdfkit.document.download.source.DownloadSource
import retrofit2.http.Url
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection

class WebDownloadSource(private val documentUrl: URL): DownloadSource {
    /**
     * The open method needs to return an [InputStream] that will provide the complete document.
     */
    override fun open(): InputStream {
        val connection = documentUrl.openConnection() as HttpURLConnection
        connection.connect()
        return connection.inputStream
    }

    /**
     * If the length is available it can be returned here. This is optional, and can improve the reported download progress, since it will then contain
     * a percentage of download.
     */
    override fun getLength(): Long {
        var length = DownloadSource.UNKNOWN_DOWNLOAD_SIZE

        // We try to estimate the download size using the content length header.
        var urlConnection: URLConnection? = null
        try {
            urlConnection = documentUrl.openConnection()
            val contentLength = urlConnection.contentLength
            if (contentLength != -1) {
                length = contentLength.toLong()
            }
        } catch (e: IOException) {
            Log.e("DocDownloadError", "Error while trying to parse the PDF Download URL.", e)
        } finally {
            (urlConnection as? HttpURLConnection)?.disconnect()
        }
        return length
    }
    override fun toString(): String {
        return "WebDownloadSource{documentURL=$documentUrl}"
    }
}
