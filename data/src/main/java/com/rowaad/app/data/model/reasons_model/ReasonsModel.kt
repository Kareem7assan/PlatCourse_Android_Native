package com.rowaad.app.data.model.reasons_model

import com.rowaad.app.data.model.UserModel

data class ReasonsModel(
		val deliveryMen: UserModel? = null,
		val records: MutableList<ReasonsItem>? = null
)





data class ReasonsItem(
	val reason: String? = null,
	val id: Int? = null,
)
