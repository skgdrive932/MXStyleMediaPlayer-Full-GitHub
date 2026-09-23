package com.skkaushal.mxstyleplayer.model

data class CategoryItem(
    val name: String,
    val count: Int,
    val type: CategoryType
)

enum class CategoryType {
    ALBUM, ARTIST, FOLDER
}
