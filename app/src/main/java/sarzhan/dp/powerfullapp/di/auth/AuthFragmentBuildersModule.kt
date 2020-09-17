package sarzhan.dp.powerfullapp.di.auth

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sarzhan.dp.powerfullapp.ui.auth.ForgotPasswordFragment
import sarzhan.dp.powerfullapp.ui.auth.LauncherFragment
import sarzhan.dp.powerfullapp.ui.auth.LoginFragment
import sarzhan.dp.powerfullapp.ui.auth.RegisterFragment

@Module
abstract class AuthFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeLauncherFragment(): LauncherFragment

    @ContributesAndroidInjector()
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector()
    abstract fun contributeRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector()
    abstract fun contributeForgotPasswordFragment(): ForgotPasswordFragment

}