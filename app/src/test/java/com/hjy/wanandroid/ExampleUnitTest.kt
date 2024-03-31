package com.hjy.wanandroid

import com.google.gson.Gson
import com.hjy.wanandroid.mode.bean.ArticleListBean
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val data: String = "{\n" +
                "\t\t\t\"author\": \"xiaoyang\",\n" +
                "\t\t\t\"chapterId\": 269,\n" +
                "\t\t\t\"chapterName\": \"官方发布\",\n" +
                "\t\t\t\"courseId\": 13,\n" +
                "\t\t\t\"desc\": \"\",\n" +
                "\t\t\t\"envelopePic\": \"\",\n" +
                "\t\t\t\"id\": 327169,\n" +
                "\t\t\t\"link\": \"http://www.wanandroid.com/blog/show/2073\",\n" +
                "\t\t\t\"niceDate\": \"2天前\",\n" +
                "\t\t\t\"origin\": \"\",\n" +
                "\t\t\t\"originId\": 2462,\n" +
                "\t\t\t\"publishTime\": 1711546274000,\n" +
                "\t\t\t\"title\": \"[快讯] Android P 开发者预览版\",\n" +
                "\t\t\t\"userId\": 74097,\n" +
                "\t\t\t\"visible\": 0,\n" +
                "\t\t\t\"zan\": 0\n" +
                "\t\t}"
        val gson: Gson = Gson()
        val fromJson: ArticleListBean.Article =
            gson.fromJson(data, ArticleListBean.Article::class.java)
        println(fromJson.toString())
    }
}

