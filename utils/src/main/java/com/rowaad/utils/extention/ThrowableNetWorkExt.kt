package com.rowaad.utils.extention

import android.os.RemoteException
import android.util.AndroidException
import android.util.Log
import com.rowaad.app.data.utils.Constants_Api
import java.io.IOException
import java.net.*


//check if timeout api
fun Throwable.handleException():Throwable{
    return if(this is AndroidException || this is RemoteException || this is BindException ||this is PortUnreachableException ||this is SocketTimeoutException || this is UnknownServiceException ||this is UnknownHostException || this is IOException ||this is ConnectException || this is NoRouteToHostException){
        Throwable(Constants_Api.ERROR_API.CONNECTION_ERROR)
    }
    else {
        this
    }
}