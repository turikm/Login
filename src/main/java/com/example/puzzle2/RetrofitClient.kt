package com.example.puzzle2

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient
{
    private var ourInstances:Retrofit?=null
    val instance:Retrofit
    get()
    {
        if(ourInstances == null)
            ourInstances = Retrofit.Builder()
                    .baseUrl("http://127.0.0.1:3000")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
        return ourInstances!!
    }
}