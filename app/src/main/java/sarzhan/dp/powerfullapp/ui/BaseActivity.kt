package sarzhan.dp.powerfullapp.ui

import dagger.android.support.DaggerAppCompatActivity
import sarzhan.dp.powerfullapp.session.SessionManager
import javax.inject.Inject


abstract class BaseActivity: DaggerAppCompatActivity(){

    val TAG: String = "AppDebug"

    @Inject
    lateinit var sessionManager: SessionManager

}