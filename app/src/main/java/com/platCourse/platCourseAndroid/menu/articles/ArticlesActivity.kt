package com.platCourse.platCourseAndroid.menu.articles

import android.content.Intent
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ActivityArticlesBinding
import com.platCourse.platCourseAndroid.menu.MenuViewModel
import com.platCourse.platCourseAndroid.menu.article_details.ArticleDetailsActivity
import com.rowaad.app.base.BaseActivity
import com.rowaad.app.data.model.articles.Article
import com.rowaad.app.data.model.articles.ArticlesModel
import com.rowaad.utils.extention.toast

class ArticlesActivity : BaseActivity(R.layout.activity_articles) {

    private var binding: ActivityArticlesBinding? = null
    private val viewModel: MenuViewModel by viewModels()
    private val adapter by lazy { ArticleAdapter() }

    override fun init() {
        binding = ActivityArticlesBinding.bind(findViewById(R.id.nestedArticles))
        setupToolbar()
        setupRec()
        sendRequestArticles()
        setupArticles()
        setupActions()
    }

    private fun setupActions() {
        binding?.toolbar?.ivBack?.setOnClickListener {
            onBackPressed()
        }
        adapter.onClickItem=::onClickItem
    }

    private fun onClickItem(article: Article, pos: Int) {
        startActivity(Intent(this,ArticleDetailsActivity::class.java).also { it.putExtra("article_id",article.id) })
    }

    private fun setupRec() {
        binding?.rvArticles?.layoutManager= LinearLayoutManager(this)
        binding?.rvArticles?.adapter=adapter
    }


    private fun setupArticles() {
        handleSharedFlow(viewModel.articlesFlow,onSuccess = {it as List<Article>
            it.let { it1 -> handleArticles(it1) }
        })
    }

    private fun handleArticles(articles: List<Article>) {
        adapter.swapData(articles)
    }

    private fun setupToolbar() {
        binding?.toolbar?.tvTitle?.text=getString(R.string.articles)
    }


    private fun sendRequestArticles() {
        viewModel.getArticles()
    }
}