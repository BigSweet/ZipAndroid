package com.zip.zipandroid.base

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.zip.zipandroid.utils.ZipLoadingUtils


abstract class ZipBaseModelFragment<VM : BaseViewModel> : Fragment() {

    private val handler = Handler()

    private var isFirst: Boolean = true

    lateinit var mViewModel: VM

//    lateinit var mActivity: AppCompatActivity

    abstract fun layoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId(), container, false)
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        mActivity = context as AppCompatActivity
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = createViewModel()
        initView(savedInstanceState)
        createObserver()
        initData()
    }

    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    abstract fun initView(savedInstanceState: Bundle?)

//    abstract fun lazyLoadData()

    abstract fun createObserver()
//
//    override fun onResume() {
//        super.onResume()
////        onVisible()
//    }

    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            // 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿
            handler.postDelayed({
                lazyLoadData()
                isFirst = false
            }, lazyLoadTime())
        }
    }

    open fun lazyLoadTime(): Long {
        return 100
    }

    open fun initData() {}

    abstract fun lazyLoadData()

    fun showLoading() {
        ZipLoadingUtils.show(requireActivity())
    }

    fun dismissLoading() {
        ZipLoadingUtils.dismiss()
    }

}