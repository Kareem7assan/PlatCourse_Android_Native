package com.rowaad.utils.extention

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rowaad.utils.R
import com.squareup.picasso.Picasso

/**
 * Extension method to load imageView from url.
 */
fun ImageView?.loadFromUrl(imageUrl: String?, @DrawableRes placeholderId: Int? = R.drawable.ic_place_holder2) {
/*
    if (imageUrl == null || this.context==null) return
    if (placeholderId != null) Glide.with(this).load(imageUrl).placeholder(placeholderId).into(this)
    else Glide.with(this).load(imageUrl).into(this)
*/

    this?.context?.let {
        Glide.with(it)
            .load(imageUrl)
            .placeholder(R.drawable.ic_place_holder2)
            .into(this)
    }
}

fun ImageView.loadAsMap(lat: String?, lng:String,key:String,corner: Int?=14) {
    val mapUrl = "http://maps.google.com/maps/api/staticmap?/center=$lat,$lng&zoom=12&size=312x166&sensor=false&markers=color:red%7Clabel:C%7C$lat,$lng&key=$key"
    Glide.with(context).load(mapUrl)
        .apply {
            corner?.let {
                this.transform(CenterCrop(), RoundedCorners(it))
            } ?: this.transform(CenterCrop(), CircleCrop())
        }
        .into(this)
}


fun ImageView?.loadImage(url: String?) {
    this?.context?.let {
        show()
        Glide.with(it)
            .load(url)
            //.placeholder(R.drawable.ic_place_holder2)
            .into(this)
    }
}
fun ImageView?.loadImagePicasso(url: String?) {
    this?.context?.let {
        Picasso.get()
                .load(url)
                .into(this)
        /*Glide.with(it)
            .load(url)
            //.placeholder(R.drawable.ic_place_holder2)
            .into(this)*/
    }
}

fun ImageView?.loadImage(url: Int?) {
    this?.context?.let {
        Glide.with(it)
            .load(url)
            .placeholder(R.drawable.ic_place_holder2)
            .into(this)
    }
}

fun ImageView?.loadImageWithCornerFit(src: Any?, corner: Int? = null, context: Context) {
    this?.let {
        Glide.with(context)
            .load(src)

            .apply {
                corner?.let {
                    this.transform(CenterCrop(), RoundedCorners(corner))
                } ?: this.transform(FitCenter(), CircleCrop())
            }
            .into(it)
    }

}

fun ImageView?.loadImageWithCorner(url: String?, corner: Int? = null, context: Context) {
    this?.let {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.ic_place_holder2)
            .apply {
                corner.let {

                }
                corner?.let {
                    this.transform( RoundedCorners(corner))
                }
            }
            .into(it)
    }
}

fun EditText?.ItsTextTrim(): String {
    return this?.text.toString().trim()
}
fun TextView?.ItsTextTrim(): String {
    return this?.text.toString().trim()
}

fun EditText?.ItsText(): String {
    return this?.text.toString()
}


fun ImageView?.loadImageWithCorner(src: Any?, corner: Int? = null, context: Context) {
    this?.let {
        Glide.with(context)
            .load(src)
            .apply {
                corner?.let {
                    this.transform(CenterCrop(), RoundedCorners(corner))
                } ?: this.transform(CenterCrop(), CircleCrop())
            }
            .into(it)
    }
}

fun ImageView.setDrawableVector(@DrawableRes icon: Int, @AttrRes color: Int) {
    context?.colorVectorIconWithAttrColor(icon, color)?.let {
        setImageDrawable(it)
    }
}

fun Context.colorVectorIconWithAttrColor(@DrawableRes icon: Int, @AttrRes color: Int): Drawable? {
    val drawable: Drawable? = VectorDrawableCompat.create(resources, icon, null)
    return drawable?.let {
        val colorRes = getColorFromAttr(color)
        DrawableCompat.wrap(it)
        DrawableCompat.setTint(it, colorRes)
        it
    }
}


fun Context.getColorFromAttr(@AttrRes colorAttr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(colorAttr, typedValue, true)
    return typedValue.data
}


