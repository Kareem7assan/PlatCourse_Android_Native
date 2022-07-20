package com.rowaad.app.data.model.reviews

import com.rowaad.app.data.model.reviews.Review

data class ReviewsModel(
        var id: Int? = null,
        var page: Int? = null,
        var results: List<Review>? = null,
        var total_pages: Int? = null,
        var total_results: Int? = null
)