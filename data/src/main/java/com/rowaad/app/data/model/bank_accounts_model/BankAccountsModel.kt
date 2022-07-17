package com.rowaad.app.data.model.bank_accounts_model

import com.rowaad.app.data.model.tweets_model.PaginationInfo

data class BankAccountsModel(
		val records: List<RecordsItem>? = null,
		val paginationInfo: PaginationInfo? = null
)

data class RecordsItem(
	val createdAt: CreatedAt? = null,
	val ibenNumber: String? = null,
	val photo: String? = null,
	val bankName: String? = null,
	val id: Int? = null,
	val published: Boolean? = null,
	val accountNumber: String? = null,
	val accountHolderName: String? = null,
	val updatedAt: UpdatedAt? = null
)

data class UpdatedAt(
	val humanTime: String? = null,
	val format: String? = null,
	val text: String? = null,
	val timestamp: Int? = null
)



data class CreatedAt(
	val humanTime: String? = null,
	val format: String? = null,
	val text: String? = null,
	val timestamp: Int? = null
)

