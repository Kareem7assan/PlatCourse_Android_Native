package com.platCourse.platCourseAndroid.menu.articles

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ItemArticleBinding
import com.rowaad.app.data.model.articles.Article
import com.rowaad.app.data.model.articles.ArticlesModel
import com.rowaad.utils.extention.convertDate
import java.util.*

class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ArticleVH>() {

    private var data: MutableList<Article> = ArrayList()

    var onClickItem: ((Article, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleVH {
        return ArticleVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ArticleVH, position: Int) = holder.bind(data[position])

    fun swapData(data: List<Article>) {
        this.data = data as MutableList<Article>
        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    inner class ArticleVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:Article) = with(ItemArticleBinding.bind(itemView)) {
            tvTitle.text=item.title
            tvCreated.text=item.created_at?.convertDate()
            itemView.setOnClickListener {
                onClickItem?.invoke(item,adapterPosition)
            }
        }
    }
}