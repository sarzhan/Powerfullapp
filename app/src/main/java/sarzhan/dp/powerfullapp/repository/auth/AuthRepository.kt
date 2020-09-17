package sarzhan.dp.powerfullapp.repository.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import kotlinx.coroutines.Job
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
import sarzhan.dp.powerfullapp.ui.auth.state.LoginFields
import sarzhan.dp.powerfullapp.ui.auth.state.RegistrationFields
import sarzhan.dp.powerfullapp.util.ApiEmptyResponse
import sarzhan.dp.powerfullapp.util.ApiErrorResponse
import sarzhan.dp.powerfullapp.util.ApiSuccessResponse
import sarzhan.dp.powerfullapp.util.ErrorHandling.Companion.ERROR_UNKNOWN
import sarzhan.dp.powerfullapp.util.ErrorHandling.Companion.GENERIC_AUTH_ERROR
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

    private val TAG: String = "AppDebug"

    private var repositoryJob: Job? = null


    fun attemptLogin(email: String, password: String): LiveData<DataState<AuthViewState>>{

        val loginFieldErrors = LoginFields(email, password).isValidForLogin()
        if(!loginFieldErrors.equals(LoginFields.LoginError.none())){
            return returnErrorResponse(loginFieldErrors, ResponseType.Dialog())
        }

        return object: NetworkBoundResource<LoginResponse, AuthViewState>(
            sessionManager.isConnectedToTheInternet()
        ){
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<LoginResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: ${response}")

                // Incorrect login credentials counts as a 200 response from server, so need to handle that
                if(response.body.response.equals(GENERIC_AUTH_ERROR)){
                    return onErrorReturn(response.body.errorMessage, true, false)
                }

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = AuthToken(response.body.pk, response.body.token)
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<LoginResponse>> {
                return openApiAuthService.login(email, password)
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    fun attemptRegistration(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): LiveData<DataState<AuthViewState>>{

        val registrationFieldErrors = RegistrationFields(email, username, password, confirmPassword).isValidForRegistration()
        if(!registrationFieldErrors.equals(RegistrationFields.RegistrationError.none())){
            return returnErrorResponse(registrationFieldErrors, ResponseType.Dialog())
        }

        return object: NetworkBoundResource<RegistrationResponse, AuthViewState>(
            sessionManager.isConnectedToTheInternet()
        ){
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<RegistrationResponse>) {

                Log.d(TAG, "handleApiSuccessResponse: ${response}")

                if(response.body.response.equals(GENERIC_AUTH_ERROR)){
                    return onErrorReturn(response.body.errorMessage, true, false)
                }

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = AuthToken(response.body.pk, response.body.token)
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<RegistrationResponse>> {
                return openApiAuthService.register(email, username, password, confirmPassword)
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }


    private fun returnErrorResponse(errorMessage: String, responseType: ResponseType): LiveData<DataState<AuthViewState>>{
        Log.d(TAG, "returnErrorResponse: ${errorMessage}")

        return object: LiveData<DataState<AuthViewState>>(){
            override fun onActive() {
                super.onActive()
                value = DataState.error(
                    Response(
                        errorMessage,
                        responseType
                    )
                )
            }
        }
    }

    fun cancelActiveJobs(){
        Log.d(TAG, "AuthRepository: Cancelling on-going jobs...")
        repositoryJob?.cancel()
    }

}