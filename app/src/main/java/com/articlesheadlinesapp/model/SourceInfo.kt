package com.articlesheadlinesapp.model

import androidx.room.Embedded

data class SourceInfo(
    @Embedded
    val id: String?,
    @Embedded
    val name: String?
) {
}