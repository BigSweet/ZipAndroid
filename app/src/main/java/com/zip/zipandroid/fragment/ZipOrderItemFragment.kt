package com.zip.zipandroid.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView
import com.zip.zipandroid.R
import com.zip.zipandroid.activity.PayOrderDetailActivity
import com.zip.zipandroid.activity.ZipOrderDetailActivity
import com.zip.zipandroid.adapter.OrderItemListAdapter
import com.zip.zipandroid.base.ZipBaseBindingFragment
import com.zip.zipandroid.bean.ZipOrderListBeanItem
import com.zip.zipandroid.databinding.FragmentZipItemOrderBinding
import com.zip.zipandroid.viewmodel.OrderItemViewModel

class ZipOrderItemFragment : ZipBaseBindingFragment<OrderItemViewModel, FragmentZipItemOrderBinding>() {
    companion object {
        fun newInstance(queryType: Int): ZipOrderItemFragment {
            val args = Bundle()
            val fragment = ZipOrderItemFragment()
            args.putInt("queryType", queryType)
            fragment.arguments = args
            return fragment
        }
    }

    var queryType = 0 //0是全部订单
    override fun initView(savedInstanceState: Bundle?) {
        queryType = arguments?.getInt("queryType", 0) ?: 0

//        mViewBind.orderListRefresh.setOnRefreshListener {
//            index = 0
//            mViewModel.getOrderListInfo(queryType)
//        }
        mViewBind.itemOrderListRv.layoutManager = LinearLayoutManager(requireActivity())
//        mViewBind.itemOrderListRv.adapter = adapter
        adapter.bindToRecyclerView(mViewBind.itemOrderListRv)
        adapter.setOnItemChildClickListener { baseQuickAdapter, view, i ->
            val item = baseQuickAdapter.getItem(i) as ZipOrderListBeanItem
            if (view.id == R.id.zip_order_item_finish_detail_tv || view.id == R.id.zip_order_item_show_detail_tv) {
                //订单详情页面
                ZipOrderDetailActivity.start(requireActivity(), item.bizId, queryType)
            }
            if (view.id == R.id.zip_order_item_repay_btn) {
                PayOrderDetailActivity.start(requireActivity(), item.bizId, item?.lid.toString(), item?.amountDue.toString())
            }
        }


    }

    override fun onResume() {
        super.onResume()
        index = 0
        showLoading()
        mViewModel.getOrderListInfo(queryType)
    }

    val adapter = OrderItemListAdapter()

    override fun createObserver() {
        adapter.setEnableLoadMore(true)
        adapter.disableLoadMoreIfNotFullPage(mViewBind.itemOrderListRv)
        adapter.setPreLoadNumber(2)
        adapter.setLoadMoreView(SimpleLoadMoreView())
        adapter.setOnLoadMoreListener({
            if (index + 1 >= (allList?.size ?: 0)) {
                adapter.loadMoreEnd()
                return@setOnLoadMoreListener
            }
            index++
            adapter.addData(allList?.get(index))
            adapter.loadMoreComplete()
        }, mViewBind.itemOrderListRv)
        mViewModel.orderListLiveData.observe(this) {

            dismissLoading()
//            if (mViewBind.orderListRefresh.isRefreshing) {
//                mViewBind.orderListRefresh.isRefreshing = false
//            }
            if (it.isNullOrEmpty()) {
                adapter.setNewData(arrayListOf())
                adapter.setEmptyView(getEmptyView(requireActivity()))
            } else {
                if (queryType == 0) {
                    //查待还款和逾期的
                    val type0List = it.filter {
                        it.status == "NOTREPAID" || it.status == "OVERDUE" || it.status == "PARTIAL" || it.status == "LENDING" || it.status == "PASSED"
                    }
                    if (type0List.isNullOrEmpty()) {
                        adapter.setNewData(arrayListOf())
                        adapter.setEmptyView(getEmptyView(requireActivity()))
                    } else {
                        allList = splitList(type0List)
                        adapter.setNewData(allList?.get(index))
                    }


                }
                if (queryType == 1) {
//                    查审核中和放款中的
                    val type1List = it.filter {
                        it.status == "TRANSACTION" || it.status == "EXECUTING"
                    }

                    if (type1List.isNullOrEmpty()) {
                        adapter.setNewData(arrayListOf())
                        adapter.setEmptyView(getEmptyView(requireActivity()))
                    } else {
                        allList = splitList(type1List)
                        adapter.setNewData(allList?.get(index))
                    }
                }
                if (queryType == 2) {
//                   拒绝和取消的
                    val type2List = it.filter {
                        it.status == "REFUSED" || it.status == "CANCELED" || it.status == "CANCEL" || it.status == "FINISH" || it.status == "OVERDUEREPAYMENT"
                    }
                    if (type2List.isNullOrEmpty()) {
                        adapter.setNewData(arrayListOf())
                        adapter.setEmptyView(getEmptyView(requireActivity()))
                    } else {
//                        val testList = arrayListOf<ZipOrderListBeanItem>()
//                        testList.addAll(type2List)
//                        testList.addAll(type2List)
//                        testList.addAll(type2List)
//                        testList.addAll(type2List)
//                        testList.addAll(type2List)
//                        testList.addAll(type2List)
//                        testList.addAll(type2List)
//                        testList.addAll(type2List)
//                        testList.addAll(type2List)
//                        testList.addAll(type2List)
//                        testList.addAll(type2List)
//                        testList.addAll(type2List)
//                        testList.addAll(type2List)
//                        testList.addAll(type2List)
//                        testList.addAll(type2List)
//                        testList.addAll(type2List)
//                        allList = splitList(testList)
                        allList = splitList(type2List)
                        adapter.setNewData(allList?.get(index))

                    }
                }
            }
        }
    }

    var index = 0

    var allList: List<List<ZipOrderListBeanItem>> = arrayListOf()

    fun splitList(list: List<ZipOrderListBeanItem>): List<List<ZipOrderListBeanItem>> {
        val list = list.chunked(10)
        return list
    }

    fun getEmptyView(context: Context): View {
        val inflate: View = LayoutInflater.from(context).inflate(R.layout.item_order_empty, null)
        return inflate
    }

    override fun lazyLoadData() {
    }
}