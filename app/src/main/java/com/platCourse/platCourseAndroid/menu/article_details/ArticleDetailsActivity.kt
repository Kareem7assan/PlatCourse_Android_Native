package com.platCourse.platCourseAndroid.menu.article_details

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.ActivityArticleDetailsBinding
import com.platCourse.platCourseAndroid.databinding.ActivityArticlesBinding
import com.platCourse.platCourseAndroid.menu.MenuViewModel
import com.rowaad.app.base.BaseActivity
import com.rowaad.app.data.model.articles.Article
import com.rowaad.app.data.model.articles.ArticlesModel
import com.rowaad.utils.extention.toast

class ArticleDetailsActivity : BaseActivity(R.layout.activity_article_details) {

    private var articleId: Int = 0
    private var binding: ActivityArticleDetailsBinding? = null
    private val viewModel: MenuViewModel by viewModels()


    override fun init() {
        binding = ActivityArticleDetailsBinding.bind(findViewById(R.id.rootArticle))
        articleId=intent.getIntExtra("article_id",0)
        setupToolbar()
        sendRequestArticles()
        setupArticles()
        setupActions()
    }


    private fun setupActions() {
        binding?.toolbar?.ivBack?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupArticles() {
        handleSharedFlow(viewModel.articleFlow,onSuccess = {it as Article
            handleArticle(it)
        })
    }

    private fun handleArticle(article: Article) {
        binding!!.tvBody.text=article.text
        binding!!.toolbar.tvTitle.text=article.title
    }

    private fun setupToolbar() {
        binding?.toolbar?.tvTitle?.text=""
    }


    private fun sendRequestArticles() {
        viewModel.getArticle(articleId)
    }
}