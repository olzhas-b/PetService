/*
 * *
 *  * Created by Ali Ashkeyev on 13.01.22 20:29
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 13.01.22 20:29
 *
 */

package com.example.budka.data.model

import android.net.Uri

data class  UploadImage (
    var imageUri: Uri?,
    var isFirstElement: Boolean = false
)
