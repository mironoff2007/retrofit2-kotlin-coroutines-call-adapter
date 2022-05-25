package com.melegy.retrofitcoroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.melegy.retrofitcoroutines.remote.NetworkResponse
import com.melegy.retrofitcoroutines.remote.NetworkResponseAdapterFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.create
import retrofit2.http.GET
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val okHttpClient = OkHttpClient.Builder().build()
        val retrofit = createRetrofit(okHttpClient)
        val service = retrofit.create<ApiService>()

        GlobalScope.launch {
            val response1 = service.getSuccess()
            logResult(response1)

            val response2 = service.getError()
            logResult(response2)
        }
    }

    fun logResult(response: NetworkResponse<Success, Error>){
        when (response) {
            is NetworkResponse.Success ->
                if (response.body.error == null) {
                    Log.d(TAG, "Success ${response.body.activity}")
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
        @GET("activity")
        suspend fun getSuccess(): NetworkResponse<Success, Error>

        @GET("error")
        suspend fun getError(): NetworkResponse<Success, Error>
    }

    private fun createRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.boredapi.com/api/")
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(JacksonConverterFactory.create())
            .client(client)
            .build()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}