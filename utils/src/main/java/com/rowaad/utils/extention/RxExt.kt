package com.rowaad.utils.extention

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber

fun <T> Observable<Response<T>>.subscribeWithLoading(
    onSuccess: (T) -> Unit,
    onErrorResponse: (throwable: ResponseBody) -> Unit,
    onGeneralError: (throwable: Throwable) -> Unit = { throw  it }
): Disposable {
    return subscribe({
        if (it.isSuccessful && it.body() != null) {
            onSuccess(it.body()!!)
        } else {
            Timber.tag("error").e(it.errorBody().toString())
            onErrorResponse(it.errorBody()!!)
        }
    }, {
        Timber.tag("error_general").e(it.message.toString())
        onGeneralError(it)
    })
}