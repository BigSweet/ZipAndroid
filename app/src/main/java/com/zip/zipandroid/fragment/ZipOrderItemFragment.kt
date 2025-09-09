package com.zip.zipandroid.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
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

        mViewBind.itemOrderListRv.layoutManager = LinearLayoutManager(requireActivity())
        mViewBind.itemOrderListRv.adapter = adapter
        adapter.setOnItemChildClickListener { baseQuickAdapter, view, i ->
            val item = baseQuickAdapter.getItem(i) as ZipOrderListBeanItem
            if (view.id == R.id.zip_order_item_finish_detail_tv || view.id == R.id.zip_order_item_show_detail_tv) {
                //订单详情页面
                ZipOrderDetailActivity.start(requireActivity(), item.bizId,queryType)
            }
            if (view.id == R.id.zip_order_item_repay_btn) {
                PayOrderDetailActivity.start(requireActivity(), item.bizId, item?.lid.toString(), item?.amountDue.toString())
            }
        }


    }

    override fun onResume() {
        super.onResume()
        mViewModel.getOrderListInfo(queryType)
    }

    val adapter = OrderItemListAdapter()

    override fun createObserver() {
        mViewModel.orderListLiveData.observe(this) {

            if (it.isNullOrEmpty()) {
                adapter.setEmptyView(getEmptyView(requireActivity()))
            } else {
                if (queryType == 0) {
                    //查待还款和逾期的
                    val type0List = it.filter {
                        it.status == "NOTREPAID" || it.status == "OVERDUE" || it.status == "PARTIAL" || it.status == "LENDING" || it.status == "PASSED"
                    }
                    if (type0List.isNullOrEmpty()) {
                        adapter.setEmptyView(getEmptyView(requireActivity()))
                    } else {
                        adapter.setNewData(type0List)
                    }


                }
                if (queryType == 1) {
//                    查审核中和放款中的
                    val type1List = it.filter {
                        it.status == "TRANSACTION" || it.status == "TRANSACTION" || it.status == "EXECUTING"
                    }

                    if (type1List.isNullOrEmpty()) {
                        adapter.setEmptyView(getEmptyView(requireActivity()))
                    } else {
                        adapter.setNewData(type1List)
                    }
                }
                if (queryType == 2) {
//                   拒绝和取消的
                    val type2List = it.filter {
                        it.status == "REFUSED" || it.status == "CANCELED" || it.status == "CANCEL" || it.status == "FINISH"
                    }
                    if (type2List.isNullOrEmpty()) {
                        adapter.setEmptyView(getEmptyView(requireActivity()))
                    } else {
                        adapter.setNewData(type2List)

                    }
                }
            }
        }
    }

    fun getEmptyView(context: Context): View {
        val inflate: View = LayoutInflater.from(context).inflate(R.layout.item_order_empty, null)
        return inflate
    }

    override fun lazyLoadData() {
    }
}