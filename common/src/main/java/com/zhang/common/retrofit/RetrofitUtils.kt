package com.zhang.common.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by zhang .
 * DATA: 2018/8/5 .
 * Description : RETROFIT
 */
class RetrofitUtils constructor(){

    private lateinit var mRetrofit: Retrofit

    init {
        configRetrofit()
    }

    companion object {
        var BASE_URL = ""
        var LEVEL = HttpLoggingInterceptor.Level.BODY
        var TIME_OUT = 20L
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            RetrofitUtils()
        }
    }

    /**
     * 初始化
     */
    private fun configRetrofit(){
        mRetrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(buildClient())
                .build()
    }

    /**
     * 构建OkHttpClient
     */
    private fun buildClient(): OkHttpClient{
        return OkHttpClient.Builder()
                .addInterceptor(initLoggingInterceptor())
                .addInterceptor(NetInterceptor())
                .connectTimeout(TIME_OUT,TimeUnit.SECONDS)
                .readTimeout(TIME_OUT,TimeUnit.SECONDS)
                .build()
    }


    private fun initLoggingInterceptor(): HttpLoggingInterceptor{
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = LEVEL
        return interceptor
    }

    class NetInterceptor: Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response? {
            val request = chain.request().newBuilder().addHeader("Connection", "close").build()
            return chain.proceed(request)
        }

    }

    fun <T> creat(cls: Class<T>): T {
        return mRetrofit.create(cls)
    }

}