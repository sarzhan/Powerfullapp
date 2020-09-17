package sarzhan.dp.powerfullapp.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_register.*
import sarzhan.dp.powerfullapp.R
import sarzhan.dp.powerfullapp.ui.auth.state.AuthStateEvent
import sarzhan.dp.powerfullapp.ui.auth.state.RegistrationFields
import sarzhan.dp.powerfullapp.util.ApiEmptyResponse
import sarzhan.dp.powerfullapp.util.ApiErrorResponse
import sarzhan.dp.powerfullapp.util.ApiSuccessResponse


class RegisterFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "RegisterFragment: ${viewModel}")
        subscribeObservers()

        register_button.setOnClickListener {
            register()
        }
    }

    fun register(){
        viewModel.setStateEvent(
            AuthStateEvent.RegisterAttemptEvent(
                input_email.text.toString(),
                input_username.text.toString(),
                input_password.text.toString(),
                input_password_confirm.text.toString()
            )
        )
    }

    fun subscribeObservers(){
        viewModel.viewState.observe(viewLifecycleOwner, Observer{viewState ->
            viewState.registrationFields?.let {
                it.registration_email?.let{input_email.setText(it)}
                it.registration_username?.let{input_username.setText(it)}
                it.registration_password?.let{input_password.setText(it)}
                it.registration_confirm_password?.let{input_password_confirm.setText(it)}
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRegistrationFields(
            RegistrationFields(
                input_email.text.toString(),
                input_username.text.toString(),
                input_password.text.toString(),
                input_password_confirm.text.toString()
            )
        )
    }
}
