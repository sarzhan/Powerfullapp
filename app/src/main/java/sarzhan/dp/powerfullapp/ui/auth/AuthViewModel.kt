package sarzhan.dp.powerfullapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import sarzhan.dp.powerfullapp.api.auth.network_responses.LoginResponse
import sarzhan.dp.powerfullapp.api.auth.network_responses.RegistrationResponse
import sarzhan.dp.powerfullapp.models.AuthToken
import sarzhan.dp.powerfullapp.repository.auth.AuthRepository
import sarzhan.dp.powerfullapp.ui.BaseViewModel
import sarzhan.dp.powerfullapp.ui.DataState
import sarzhan.dp.powerfullapp.ui.auth.state.AuthStateEvent
import sarzhan.dp.powerfullapp.ui.auth.state.AuthViewState
import sarzhan.dp.powerfullapp.ui.auth.state.LoginFields
import sarzhan.dp.powerfullapp.ui.auth.state.RegistrationFields
import sarzhan.dp.powerfullapp.util.AbsentLiveData
import sarzhan.dp.powerfullapp.util.GenericApiResponse
import javax.inject.Inject

class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository
): BaseViewModel<AuthStateEvent, AuthViewState>()
{
    override fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<DataState<AuthViewState>> {
        when(stateEvent){

            is AuthStateEvent.LoginAttemptEvent -> {
                return authRepository.attemptLogin(
                    stateEvent.email,
                    stateEvent.password
                )
            }

            is AuthStateEvent.RegisterAttemptEvent -> {
                return authRepository.attemptRegistration(
                    stateEvent.email,
                    stateEvent.username,
                    stateEvent.password,
                    stateEvent.confirm_password
                )
            }

            is AuthStateEvent.CheckPreviousAuthEvent -> {
                return AbsentLiveData.create()
            }


        }
    }

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    fun setRegistrationFields(registrationFields: RegistrationFields){
        val update = getCurrentViewStateOrNew()
        if(update.registrationFields == registrationFields){
            return
        }
        update.registrationFields = registrationFields
        _viewState.value = update
    }

    fun setLoginFields(loginFields: LoginFields){
        val update = getCurrentViewStateOrNew()
        if(update.loginFields == loginFields){
            return
        }
        update.loginFields = loginFields
        _viewState.value = update
    }

    fun setAuthToken(authToken: AuthToken){
        val update = getCurrentViewStateOrNew()
        if(update.authToken == authToken){
            return
        }
        update.authToken = authToken
        _viewState.value = update
    }
}