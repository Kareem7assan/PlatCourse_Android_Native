package com.rowaad.utils.extention

import android.Manifest
import android.app.usage.NetworkStats
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.rowaad.app.data.remote.NetWorkState
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

//import com.esafirm.imagepicker.features.ReturnMode
//import com.rowaad.app.data.R
//import com.tbruyelle.rxpermissions3.RxPermissions

//fun Fragment.uploadImageAPI30(resultLauncher: ActivityResultLauncher<Intent>) {
//    com.github.dhaval2404.imagepicker.ImagePicker.with(this)
//        .compress(2048)
//        .createIntent { intent ->
//            resultLauncher.launch(intent)
//        }
//}
//
//fun Fragment.uploadImage() {
//    com.esafirm.imagepicker.features.ImagePicker.create(this)
//        .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
//        .folderMode(true) // folder mode (false by default)
//        .toolbarFolderTitle(getString(R.string.images)) // folder selection title
//        .toolbarImageTitle(getString(R.string.choose_image)) // image selection title
//        .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
//        .single() // single mode
//        .showCamera(true) // show camera or not (true by default)
//        .enableLog(true)
//        .start()
//}
//
//fun Fragment.uploadImages() {
//    com.esafirm.imagepicker.features.ImagePicker.create(this)
//        .folderMode(true) // folder mode (false by default)
//        .toolbarFolderTitle(getString(R.string.images)) // folder selection title
//        .toolbarImageTitle(getString(R.string.choose_image)) // image selection title
//        .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
//        .multi()
//        .limit(10)
//        .showCamera(true) // show camera or not (true by default)
//        .enableLog(true)
//        .start()
//}
//




/**
 * get string result from fragment
 */
fun Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(key)

/**
 * set string result for fragment
 */
fun Fragment.setNavigationResult(result: String, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}


fun Fragment.handleCustomBack(action: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            action.invoke()
        }

    })
}

fun Fragment.closeMe(){
    requireActivity().supportFragmentManager.popBackStack()
}

fun Fragment.checkPermissionLocation(onLocationGranted: (granted: Boolean) -> Unit) {
    val rxPermissions = RxPermissions(this)
    rxPermissions.request(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
        .subscribe { granted ->
            onLocationGranted(granted)
        }
}
fun Fragment.handlePermissionFile(requestPermissionLauncher: ActivityResultLauncher<Array<String>>,onSuccess:()->Unit,onFail:()->Unit,showInContextUI:(()->Unit)?=null){

    when {
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
            onSuccess.invoke()
        }
        shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) && shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)-> {
            showInContextUI?.invoke()

        }
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED -> {
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
        }
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED -> {
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        }
        else -> {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE))

        }
    }
}