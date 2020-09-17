package sarzhan.dp.powerfullapp.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sarzhan.dp.powerfullapp.di.auth.AuthFragmentBuildersModule
import sarzhan.dp.powerfullapp.di.auth.AuthModule
import sarzhan.dp.powerfullapp.di.auth.AuthScope
import sarzhan.dp.powerfullapp.di.auth.AuthViewModelModule
import sarzhan.dp.powerfullapp.ui.auth.AuthActivity
import sarzhan.dp.powerfullapp.ui.main.MainActivity

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [AuthModule::class, AuthFragmentBuildersModule::class, AuthViewModelModule::class]
    )
    abstract fun contributeAuthActivity(): AuthActivity

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

}