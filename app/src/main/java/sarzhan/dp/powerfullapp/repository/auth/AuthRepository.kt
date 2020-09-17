package sarzhan.dp.powerfullapp.repository.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import sarzhan.dp.powerfullapp.api.auth.OpenApiAuthService
import sarzhan.dp.powerfullapp.api.auth.network_responses.LoginResponse
import sarzhan.dp.powerfullapp.api.auth.network_responses.RegistrationResponse
import sarzhan.dp.powerfullapp.models.AuthToken
import sarzhan.dp.powerfullapp.persitence.AccountPropertiesDao
import sarzhan.dp.powerfullapp.persitence.AuthTokenDao
import sarzhan.dp.powerfullapp.session.SessionManager
import sarzhan.dp.powerfullapp.ui.DataState
import sarzhan.dp.powerfullapp.ui.Response
import sarzhan.dp.powerfullapp.ui.ResponseType
import sarzhan.dp.powerfullapp.ui.auth.state.AuthViewState
import sarzhan.dp.powerfullapp.util.ApiEmptyResponse
import sarzhan.dp.powerfullapp.util.ApiErrorResponse
import sarzhan.dp.powerfullapp.util.ApiSuccessResponse
import sarzhan.dp.powerfullapp.util.ErrorHandling.Companion.ERROR_UNKNOWN
import sarzhan.dp.powerfullapp.util.GenericApiResponse
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager
)
{

    fun attemptLogin(email: String, password: String): LiveData<DataState<AuthViewState>>{
        return openApiAuthService.login(email, password)
            .switchMap { response ->
                object: LiveData<DataState<AuthViewState>>(){
                    override fun onActive() {
                        super.onActive()
                        when(response){
                            is ApiSuccessResponse ->{
                                value = DataState.data(
                                    AuthViewState(
                                        authToken = AuthToken(response.body.pk, response.body.token)
                                    ),
                                    response = null
                                )
                            }
                            is ApiErrorResponse ->{
                                value = DataState.error(
                                    Response(
                                        message = response.errorMessage,
                                        responseType = ResponseType.Dialog()
                                    )
                                )
                            }
                            is ApiEmptyResponse ->{
                                value = DataState.error(
                                    Response(
                                        message = ERROR_UNKNOWN,
                                        responseType = ResponseType.Dialog()
                                    )
                                )
                            }
                        }
                    }
                }
            }
    }


    fun attemptRegistration(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): LiveData<DataState<AuthViewState>>{
        return openApiAuthService.register(email, username, password, confirmPassword)
            .switchMap { response ->
                object: LiveData<DataState<AuthViewState>>(){
                    override fun onActive() {
                        super.onActive()
                        when(response){
                            is ApiSuccessResponse ->{
                                value = DataState.data(
                                    AuthViewState(
                                        authToken = AuthToken(response.body.pk, response.body.token)
                                    ),
                                    response = null
                                )
                            }
                            is ApiErrorResponse ->{
                                value = DataState.error(
                                    Response(
                                        message = response.errorMessage,
                                        responseType = ResponseType.Dialog()
                                    )
                                )
                            }
                            is ApiEmptyResponse ->{
                                value = DataState.error(
                                    Response(
                                        message = ERROR_UNKNOWN,
                                        responseType = ResponseType.Dialog()
                                    )
                                )
                            }
                        }
                    }
                }
            }
    }

}