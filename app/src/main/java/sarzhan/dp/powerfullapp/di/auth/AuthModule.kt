package sarzhan.dp.powerfullapp.di.auth

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import sarzhan.dp.powerfullapp.api.auth.OpenApiAuthService
import sarzhan.dp.powerfullapp.persitence.AccountPropertiesDao
import sarzhan.dp.powerfullapp.persitence.AuthTokenDao
import sarzhan.dp.powerfullapp.repository.auth.AuthRepository
import sarzhan.dp.powerfullapp.session.SessionManager

@Module
class AuthModule{

    @AuthScope
    @Provides
    fun provideOpenApiAuthService(retrofitBuilder: Retrofit.Builder): OpenApiAuthService {
        return retrofitBuilder
            .build()
            .create(OpenApiAuthService::class.java)
    }

    @AuthScope
    @Provides
    fun provideAuthRepository(
        sessionManager: SessionManager,
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao,
        openApiAuthService: OpenApiAuthService,
        preferences: SharedPreferences,
        editor: SharedPreferences.Editor
    ): AuthRepository {
        return AuthRepository(
            authTokenDao,
            accountPropertiesDao,
            openApiAuthService,
            sessionManager,
            preferences,
            editor
        )
    }

}