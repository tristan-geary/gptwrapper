package ca.bishops.cs330.gptwrapper.s002344234.di

import ca.bishops.cs330.gptwrapper.s002344234.BuildConfig
import ca.bishops.cs330.gptwrapper.s002344234.network.OpenAIService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

///dependency injection
@Module
///dependencies live as long as the app
@InstallIn(SingletonComponent::class)
object NetworkModule {

    ///provides singleton okhttpclient instance
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        ///logging http request/response info
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient.Builder()
            ///interceptor adding headers to each request
            .addInterceptor { chain ->
                val request: Request = chain.request().newBuilder()
                    .addHeader(
                        "Authorization",
                        "Bearer ${BuildConfig.OPENAI_API_KEY}"
                    )
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logging)
            .build()
    }

    ///provide singleton moshi instance for json parsing
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()


    ///singleton retrofit instance w/ okhttpclient and moshi
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    ///provides singleton OpenAIService for API calls
    @Provides
    @Singleton
    fun provideOpenAIService(retrofit: Retrofit): OpenAIService =
        retrofit.create(OpenAIService::class.java)
}
