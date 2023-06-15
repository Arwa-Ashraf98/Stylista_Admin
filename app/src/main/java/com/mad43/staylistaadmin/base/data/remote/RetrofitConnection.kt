package com.mad43.staylistaadmin.base.data.remote

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mad43.staylistaadmin.utils.Const
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitConnection {

    @Volatile
    private var requestInterceptor = Interceptor { chain ->
        val url = chain
            .request()
            .url
            .newBuilder()
            .build()

        val request = chain
            .request()
            .newBuilder()
            .url(url)
            .addHeader("X-Shopify-Access-Token" , Const.API_ADMIN_ACCESS_TOKEN)
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .build()

        return@Interceptor chain.proceed(request)
    }

    // okHttp Interceptor
    @Volatile
    private var client = OkHttpClient
        .Builder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(180, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .callTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .addInterceptor(requestInterceptor)
        .build()

    // retrofit object that take Interceptor client
    //    private var retrofit: Retrofit? = null
    @Volatile
    private var retrofit = Retrofit.Builder()
        .baseUrl(Const.BASE_URL)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .client(client)
        .build()

    // provide object from class implement Service and Retrofit is this class
    @Synchronized
    fun getServices(): RetrofitServices = retrofit.create(RetrofitServices::class.java)

}