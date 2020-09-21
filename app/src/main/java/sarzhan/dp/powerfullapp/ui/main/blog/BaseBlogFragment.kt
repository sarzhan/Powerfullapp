package sarzhan.dp.powerfullapp.ui.main.blog

import android.content.Context
import android.util.Log
import dagger.android.support.DaggerFragment
import sarzhan.dp.powerfullapp.ui.DataStateChangeListener

abstract class BaseBlogFragment : DaggerFragment(){

    val TAG: String = "AppDebug"

    lateinit var stateChangeListener: DataStateChangeListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            stateChangeListener = context as DataStateChangeListener
        }catch(e: ClassCastException){
            Log.e(TAG, "$context must implement DataStateChangeListener" )
        }
    }
}