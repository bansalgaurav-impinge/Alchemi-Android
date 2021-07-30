package com.app.alchemi.retrofit

import androidx.multidex.BuildConfig
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private val retrofitClient: Retrofit.Builder by lazy {

        val levelType: HttpLoggingInterceptor.Level = if (BuildConfig.BUILD_TYPE.contentEquals("debug"))
            HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BODY
        val logging = HttpLoggingInterceptor()
        logging.setLevel(levelType)
        val gson= GsonBuilder().setLenient().create()
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(logging)
        okHttpClientBuilder.connectTimeout(120, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(120, TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(120, TimeUnit.SECONDS)
        okHttpClientBuilder.retryOnConnectionFailure(true)
        Retrofit.Builder()
            .baseUrl(Constants.WS_BASE_URL)
            .client(okHttpClientBuilder.build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    val apiInterface: ApiInterface by lazy {
        retrofitClient
            .build()
            .create(ApiInterface::class.java)
    }

    val token
        get() = apiInterface.getLinkToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.link_token }
}