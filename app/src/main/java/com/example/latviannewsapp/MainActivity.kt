package com.example.latviannewsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.latviannewsapp.Model.Article
import com.example.latviannewsapp.Model.News
import com.example.latviannewsapp.Networking.ApiClient
import com.example.latviannewsapp.Networking.ApiInterface
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() , SwipeRefreshLayout.OnRefreshListener{

    private val apiKey: String = "426785172f6a46d18a6283d67806fde0"
    var articles: ArrayList<Article> = ArrayList()


    private lateinit var adapter: Adapter
    private lateinit var viewManger: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeRefresh.setOnRefreshListener(this)
        swipeRefresh.setColorSchemeResources(R.color.colorAccent)

        viewManger = LinearLayoutManager(this)
        recyclerView.layoutManager = viewManger
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.isNestedScrollingEnabled = false

        loadJson("")

        // onLoadRefresh("")
    }

    private fun loadJson(keyword: String) {

        swipeRefresh.isRefreshing = true

        val apiInterface: ApiInterface? = ApiClient.getApiClient?.create(ApiInterface::class.java)

        val utils = Utils()

        val country: String = utils.getCountry()
        val language: String = utils.getLanguage()

        val call: Call<News>?

        call = if (keyword.length < 0) {
            apiInterface?.getNewsSearch(keyword, language, "publishedAt", apiKey)
        } else {
            apiInterface?.getNews(country, apiKey)
        }


        call?.enqueue(object : Callback<News> {
            override fun onFailure(call: Call<News>?, t: Throwable?) {
                headlines.visibility = View.INVISIBLE
                swipeRefresh.isRefreshing = false
                Toast.makeText(this@MainActivity, "No Result", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<News>?, response: Response<News>?) {
                if (response!!.isSuccessful && response.body().article != null) {
                    if (articles.isNotEmpty()) {
                    }

                    articles = (response.body().article as ArrayList<Article>?)!!
                    adapter = Adapter(this@MainActivity, articles)
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()

                    headlines.visibility = View.VISIBLE
                    swipeRefresh.isRefreshing = false

                } else {
                    headlines.visibility = View.INVISIBLE
                    swipeRefresh.isRefreshing = false


                    val errorCode: String = when {
                        response.code() == 404 -> "404 not found"
                        response.code() == 500 -> "500 server broken"
                        else -> "unknown error"
                    }

                }
            }
        })
    }



    override fun onRefresh() {
        loadJson("")
    }

    private fun onLoadRefresh(keyword: String) {
        swipeRefresh.post {
            Runnable {
                loadJson(keyword)
            }
        }
    }


}