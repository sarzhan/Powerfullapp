package sarzhan.dp.powerfullapp

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import sarzhan.dp.powerfullapp.di.DaggerAppComponent

class BaseApplication: DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }


}