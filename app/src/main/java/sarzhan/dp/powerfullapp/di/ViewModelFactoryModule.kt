package sarzhan.dp.powerfullapp.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import sarzhan.dp.powerfullapp.viewmodels.ViewModelProviderFactory

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory
}