package com.melegy.retrofitcoroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.melegy.retrofitcoroutines.remote.NetworkResponse
import com.melegy.retrofitcoroutines.remote.NetworkResponseAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val okHttpClient = OkHttpClient.Builder().build()
        val retrofit = createRetrofit(okHttpClient)
        val service = retrofit.create(ApiService::class.java)

        val retrofitWoAdapter = createRetrofitWoAdapter(okHttpClient)
        val serviceWoAdapter = retrofitWoAdapter.create(ApiServiceWoAdapter ::class.java)
        val serviceWoAdapterSuspend = retrofitWoAdapter.create(ApiServiceWoAdapterSuspend ::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            val response1 = service.getFact()
            logResult(response1)

        }
        lifecycleScope.launch(Dispatchers.IO) {
            try {

                val response = serviceWoAdapter.getFact().awaitResponse()

                Log.d("Test_tag", "Response body - ${response.body()}")

            } catch (e: Exception) {
                Log.d("Test_tag", e.stackTraceToString())
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {

                val body = serviceWoAdapterSuspend.getFact().body()

                Log.d("Test_tag", "Call body - " + body.toString())

            } catch (e: Exception) {
                Log.d("Test_tag", e.stackTraceToString())
            }
        }
    }

    fun logResult(response: NetworkResponse<Fact, Error>){
        when (response) {
            is NetworkResponse.Success ->
                if (response.body.error == null) {
                    Log.d(TAG, "Success ${response.body.fact}")
                }
                else {
                    Log.d(TAG, "Error ${response.body.error}")
                }
            is NetworkResponse.ApiError -> Log.d(TAG, "ApiError ${response.body.error}")
            is NetworkResponse.NetworkError -> Log.d(TAG, "NetworkError ${response.error}")
            is NetworkResponse.UnknownError -> Log.d(TAG, "UnknownError ${response.error}")
        }
    }

    interface ApiService {
        @GET("fact")
        suspend fun getFact(): NetworkResponse<Fact, Error>

        @GET("t")
        suspend fun getError(): NetworkResponse<Fact, Error>
    }

    interface ApiServiceWoAdapter {
        @GET("fact")
        fun getFact(): Call<Fact>

        @GET("t")
        fun getError(): Call<Error>
    }

    interface ApiServiceWoAdapterSuspend {
        @GET("fact")
        suspend fun getFact(): Response<Fact>

        @GET("t")
        suspend fun getError(): Response<Error>
    }

    private fun createRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun createRetrofitWoAdapter( client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun createRetrofitWoAdapterSuspend( client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}