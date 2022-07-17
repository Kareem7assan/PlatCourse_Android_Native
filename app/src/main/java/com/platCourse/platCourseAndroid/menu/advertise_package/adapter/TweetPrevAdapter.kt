package com.platCourse.platCourseAndroid.menu.advertise_package.adapter

import android.graphics.Color
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rowaad.app.data.model.tweets_model.Tweets
import com.rowaad.app.data.utils.Constants_Api
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemTweetBinding
import com.platCourse.platCourseAndroid.databinding.ItemTweetPrevBinding
import com.platCourse.platCourseAndroid.home.time_line.adapter.TweetImgsAdapter
import com.platCourse.platCourseAndroid.listener.TweetActions
import com.rowaad.utils.extention.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*
import java.util.regex.Pattern


class TweetPrevAdapter(val actions: TweetActions) : RecyclerView.Adapter<TweetPrevAdapter.TweetVH>() {

    private var data: MutableList<Tweets> = ArrayList()

    var onClickItem: ((Tweets, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetVH {
        return TweetVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_tweet_prev, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TweetVH, position: Int) {
        holder.bind(data[position])
    }

    fun swapData(data: List<Tweets>) {
        this.data = data as MutableList<Tweets>
        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }
    fun removeWithItem(tweets: Tweets) {
        val index=data.indexOf(tweets)
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    inner class TweetVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tweetAdapter : TweetImgsAdapter? = null
        fun bind(item: Tweets) = with(ItemTweetPrevBinding.bind(itemView)) {
            ivOwner.loadImage(item.customer?.image)
            tvName.text=item.customer?.name
            tvLocation.text=item.city?.title+"-"+item.region?.title
            tvSince.text=item.createdAt?.humanTime
            tvCat.text=item.category?.title
            if (item.price ?: 0f > 0f) {
                priceGroup.show().also { tvMoney.formatPrice(
                    item.price ?: 0f, itemView.context.getString(
                        R.string.sar
                    )
                ) }
            }
            else{
                priceGroup.hide()
            }
            tvId.text="#"+item.tweetId.toString()
            tvCommentNum.text=item.totalComments.toString()
            tvHeart.text=item.totalLikes.toString()
            tvRetweet.text=item.totalRetweets.toString()
            rvImgs.layoutManager=LinearLayoutManager(itemView.context)

            if (item.title?.contains("#")==true){
                val hashText =  SpannableString(item.title!!)
                val matcher = Pattern.compile("#([A-Za-z/\u0600-\u06FF0-9_-]+)").matcher(hashText)
                while (matcher.find()) {
                    hashText.setSpan(
                        ForegroundColorSpan(Color.parseColor("#169ac5")),
                        matcher.start(),
                        matcher.end(),
                        0
                    )

                }
                tvDesc.text=hashText
                tvDesc.movementMethod = LinkMovementMethod.getInstance()

            }
            else{
                tvDesc.text=item.title
            }
            tvDesc.onClickWord {
                actions.onClickHashTag(it ?: "",item)
            }


            ivMsg.hide()
            if (item.isPremium==true)
                premLay.show().also { upView.show() }.also { bottomView.show() }
            else
                premLay.hide().also { upView.invisible() }.also { bottomView.invisible() }

            if (item.isLiked==true)
                ivHeart.tintImage("#fd0000")
            else
                ivHeart.tintImage("#939393")

            if (item.isRetweeted==true)
                ivRetweet.tintImage("#5fc098")
            else
                ivRetweet.imageTintList=null

            when {
                item.photos?.isEmpty()==true -> {
                    imgLay.hide()
                    rvImgs.hide()
                }
                item.photos?.size==1 -> {
                    imgLay.show()
                    rvImgs.hide()
                    ivFirstImg.loadImageWithCorner(item.photos?.first(), 15, itemView.context)
                }
                item.photos?.size!! > 4 -> {
                    imgLay.show()
                    rvImgs.show()
                    ivFirstImg.loadImageWithCorner(item.photos?.first(), 15, itemView.context)
                    item.photos?.size
                    tweetAdapter = TweetImgsAdapter((item.photos!!.size - 1))
                    tweetAdapter!!.swapData(item.photos!!.slice(IntRange(1, 3)))
                    rvImgs.adapter=tweetAdapter
                }
                else -> {
                    imgLay.show()
                    rvImgs.show()
                    ivFirstImg?.loadImageWithCorner(item.photos?.first(),15,itemView.context)
                    item.photos?.size
                    tweetAdapter = TweetImgsAdapter()
                    tweetAdapter!!.swapData(
                        item.photos!!.slice(
                            IntRange(
                                1,
                                item.photos!!.lastIndex
                            )
                        )
                    )
                    rvImgs.adapter=tweetAdapter
                }

            }
            if (item.status == Constants_Api.TWEET.SOLD){
                ivPhone.hide()
                soldLay.show()
            }
            else{
                if (item.whatsappNumber.isNullOrBlank())
                    ivPhone.hide()
                else
                    ivPhone.show()
                    soldLay.hide()
            }

            ivPhone.onClick {
                actions.onClickWhats(item.customer?.phoneNumber)
            }


            ivCopy.onClick {
                actions.onClickCopy(item.tweetId.toString())
            }

            itemView.onClick {
                actions.onClickAction(item)
            }

            tvName.onClick {
                actions.onClickProfile(item)
            }

            ivOwner.onClick {
                actions.onClickProfile(item)
            }




        }



    }
}