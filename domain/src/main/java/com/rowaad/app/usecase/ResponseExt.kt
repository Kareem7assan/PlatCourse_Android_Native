package com.rowaad.app.usecase

import android.os.RemoteException
import android.util.AndroidException
import android.util.Log
import com.rowaad.app.data.model.EndPointResponse
import com.rowaad.app.data.utils.Constants_Api
import com.rowaad.app.data.utils.handleError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.transform
import retrofit2.Response
import java.io.IOException
import java.net.*

inline fun < T, R> Flow<Response<EndPointResponse<T>>>.transformResponseData(
    crossinline onSuccess: suspend FlowCollector<R>.(T) -> Unit
): Flow<R> {
    return transform {
        Log.e("code..","...."+it.code().toString()+","+it.errorBody().toString())

        when {
            it.isSuccessful && it.body() != null -> onSuccess(it.body()?.data!!)
            it.code() == 400 && it.errorBody() == null -> throw Throwable(Constants_Api.ERROR_API.BAD_REQUEST)
            it.code() == 400 && it.errorBody() != null -> throw Throwable(it.errorBody()?.handleError())
            it.code() == 401 -> throw Throwable(Constants_Api.ERROR_API.UNAUTHRIZED)
            it.code() == 403 -> throw Throwable(Constants_Api.ERROR_API.UNAUTHRIZED)
            it.code() == 404 -> throw Throwable(Constants_Api.ERROR_API.NOT_FOUND)
            it.code() == 500 -> throw Throwable(Constants_Api.ERROR_API.SERVER_ERROR)
            it.code() == 503 -> throw Throwable(Constants_Api.ERROR_API.MAINTENANCE)
            else ->{
                Log.e("code",it.code().toString()+","+it.errorBody().toString())
                throw Throwable().handleException() }
        }
    }
}


inline fun < T, R> Flow<Response<T>>.transformResponse(
    crossinline onSuccess: suspend FlowCollector<R>.(T) -> Unit
): Flow<R> {
    return transform {
        when {

            it.isSuccessful && it.body() != null -> onSuccess(it.body()!!)
            it.code() == 400 && it.errorBody() == null -> throw Throwable(Constants_Api.ERROR_API.BAD_REQUEST)
            it.code() == 400 && it.errorBody() != null -> throw Throwable(it.errorBody()?.handleError())
            it.code() == 401 -> throw Throwable(Constants_Api.ERROR_API.UNAUTHRIZED)
            it.code() == 404 -> throw Throwable(Constants_Api.ERROR_API.NOT_FOUND)
            it.code() == 403 -> throw Throwable(Constants_Api.ERROR_API.UNAUTHRIZED)
            it.code() == 500 -> throw Throwable(Constants_Api.ERROR_API.SERVER_ERROR)
            it.code() == 503 -> throw Throwable(Constants_Api.ERROR_API.MAINTENANCE)
            else ->{ throw Throwable().handleException()
            }
        }
    }
}

fun Throwable.handleException():Throwable{
    Log.e("exception",this.javaClass.canonicalName+","+this.javaClass.name)
    return if(this is AndroidException || this is RemoteException || this is BindException ||this is PortUnreachableException ||this is SocketTimeoutException || this is UnknownServiceException ||this is UnknownHostException || this is IOException ||this is ConnectException || this is NoRouteToHostException){
        Throwable(Constants_Api.ERROR_API.CONNECTION_ERROR)
    }
    else {
        this
    }
}
