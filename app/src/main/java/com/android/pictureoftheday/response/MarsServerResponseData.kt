package com.android.pictureoftheday.response

import com.android.pictureoftheday.data.mars.Photo

data class MarsServerResponseData(
    val photos: List<Photo>
)