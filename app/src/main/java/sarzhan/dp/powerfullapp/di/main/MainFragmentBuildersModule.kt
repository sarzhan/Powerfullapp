package sarzhan.dp.powerfullapp.di.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sarzhan.dp.powerfullapp.ui.main.account.AccountFragment
import sarzhan.dp.powerfullapp.ui.main.account.ChangePasswordFragment
import sarzhan.dp.powerfullapp.ui.main.account.UpdateAccountFragment
import sarzhan.dp.powerfullapp.ui.main.blog.BlogFragment
import sarzhan.dp.powerfullapp.ui.main.blog.UpdateBlogFragment
import sarzhan.dp.powerfullapp.ui.main.blog.ViewBlogFragment
import sarzhan.dp.powerfullapp.ui.main.create_blog.CreateBlogFragment

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeBlogFragment(): BlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeAccountFragment(): AccountFragment

    @ContributesAndroidInjector()
    abstract fun contributeChangePasswordFragment(): ChangePasswordFragment

    @ContributesAndroidInjector()
    abstract fun contributeCreateBlogFragment(): CreateBlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeUpdateBlogFragment(): UpdateBlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeViewBlogFragment(): ViewBlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeUpdateAccountFragment(): UpdateAccountFragment
}