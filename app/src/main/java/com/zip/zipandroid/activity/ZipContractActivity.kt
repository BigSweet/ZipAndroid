package com.zip.zipandroid.activity

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.zip.zipandroid.R
import com.zip.zipandroid.adapter.ZipContractAdapter
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.bean.PersonalInformationDictBean
import com.zip.zipandroid.bean.UploadContractBean
import com.zip.zipandroid.bean.ZipContractBean
import com.zip.zipandroid.databinding.ActivityZipContractBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.pop.SingleCommonSelectPop
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.view.SetInfoEditView
import com.zip.zipandroid.viewmodel.PersonInfoViewModel

//
//emergentContacts
//array[object]
//true
//紧急联系人
//contactName
//string
//true
//姓名（中间要有空，区分姓和名）
//contactPhone
//string
//true
//手机号（有号段校验，建议用示例的）
//contactRamarkName
//string
//true
//联系人别名
//relation
//string
//true
//关系，枚举值
//relationValue

class ZipContractActivity : ZipBaseBindingActivity<PersonInfoViewModel, ActivityZipContractBinding>() {


    override fun initView(savedInstanceState: Bundle?) {
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Contact Info")

        mViewBind.contractRv.layoutManager = LinearLayoutManager(this)
//        mViewBind.contractRv.adapter = adapter
        adapter.bindToRecyclerView(mViewBind.contractRv)
        initList.add(ZipContractBean(1, "Family member", true))
        initList.add(ZipContractBean(2, "Colleague/Friend", true))
        adapter.setNewData(initList)
        adapter.popClick = {
            val setInfoView = adapter.getViewByPosition(it, (R.id.item_contract_relation_tv)) as SetInfoEditView?
            if (setInfoView != null) {
                showSelectPop(adapter.data.get(it).title, dicInfoBean?.relation, SingleCommonSelectPop.relation_type, adapter.data.get(it).relation
                    ?: 0, setInfoView, it)
            }
        }
        adapter.checkCompleteListener = {
            checkAdapterPreTwoDone()
        }
        val foot = LayoutInflater.from(this).inflate(R.layout.zip_contract_foot, null)
        footAddIcon = foot.findViewById(R.id.zip_add_icon)
        footDesTv = foot.findViewById(R.id.zip_add_des_tv)
        adapter.addFooterView(foot)
        foot.setOnDelayClickListener {
            //增加数据
            KeyboardUtils.hideSoftInput(this)
            if (done) {
//                initList.add(ZipContractBean(idx++, "Colleague/Friend", false))
//                adapter.setNewData(initList)
                adapter.addData(ZipContractBean(idx++, "Colleague/Friend", false))

                mViewBind.contractRv.post {
                    mViewBind.contractRv.scrollToPosition(adapter.data.size - 1)
                }

            }
        }
        mViewModel.getPersonInfoDic()
        mViewModel.getUserInfo()

        mViewBind.infoNextBtn.setOnDelayClickListener {
            showLoading()
            val list = convertData()
            mViewModel.saveContractInfo(list)
        }
        mViewModel.saveInfoLiveData.observe(this) {
            //保存进件到第几部了
            mViewModel.saveMemberBehavior(Constants.TYPE_CONS)
        }
        mViewModel.saveMemberInfoLiveData.observe(this) {
            if (it == Constants.TYPE_CONS) {
                dismissLoading()
                ToastUtils.showShort("finish3")
            }
        }
    }

    private fun convertData(): ArrayList<UploadContractBean> {
        val list = arrayListOf<UploadContractBean>()
        adapter.data.forEachIndexed { index, zipContractBean ->
            if ((zipContractBean.relation ?: -1) > -1) {
                val nameValue = adapter.getViewByPosition(index, R.id.item_contract_name_tv) as SetInfoEditView
                val numberValue = adapter.getViewByPosition(index, R.id.item_contract_number_tv) as SetInfoEditView
                val relationValue = adapter.getViewByPosition(index, R.id.item_contract_relation_tv) as SetInfoEditView
                val bean = UploadContractBean(nameValue.getEditText(), numberValue.getRealPhone(), zipContractBean.relation
                    ?: -1, relationValue.getEditText())
                list.add(bean)
            }
        }
        return list

    }

    var idx = 3

    var done = false

    var footAddIcon: ImageView? = null
    var footDesTv: TextView? = null
    fun checkAdapterPreTwoDone() {
        val twoList = adapter.data.filter {
            it.id == 1 || it.id == 2
        }
        var twoDone = 0
        var firstPhone = ""
        var secondPhone = ""
        twoList.forEachIndexed { index, zipContractBean ->

            val nameValue = adapter.getViewByPosition(index, R.id.item_contract_name_tv) as SetInfoEditView
            val numberValue = adapter.getViewByPosition(index, R.id.item_contract_number_tv) as SetInfoEditView
            val relationValue = adapter.getViewByPosition(index, R.id.item_contract_relation_tv) as SetInfoEditView

            if (index == 0 && !numberValue.getEditText().isNullOrEmpty()) {
                firstPhone = numberValue.getEditText()
            }

            if (!nameValue.getEditText().isNullOrEmpty() && !numberValue.getEditText().isNullOrEmpty() && !relationValue.getEditText().isNullOrEmpty()) {
                twoDone++
            }
            if (index == 1 && !numberValue.getEditText().isNullOrEmpty()) {
                secondPhone = numberValue.getEditText()
            }
        }
        if (!secondPhone.isNullOrEmpty() && !firstPhone.isNullOrEmpty() && secondPhone == firstPhone) {
            ToastUtils.showShort("Do not enter the same number")
        }
        if (twoDone == 2 && secondPhone != firstPhone) {
            //俩个都填好了而且电话不一样
            done = true
        }
        if (done) {
            footAddIcon?.setImageResource(R.drawable.done_foot_add_icon)
            footDesTv?.setTextColor(Color.parseColor("#FF3667F0"))
        } else {
            footAddIcon?.setImageResource(R.drawable.zip_contract_add_icon)
            footDesTv?.setTextColor(Color.parseColor("#ffa5b0ce"))
        }
        mViewBind.infoNextBtn.setEnabledPlus(done)

    }

    fun showSelectPop(title: String, data: List<String>?, type: Int, selectPosition: Int, infoView: SetInfoEditView, adapterPosition: Int) {
        //选择完成后检测
        KeyboardUtils.hideSoftInput(this)
        data ?: return
        showSelectPop(title, data, type, selectPosition, infoView, object : ((String, Int, Int) -> Unit) {
            override fun invoke(tv: String, position: Int, type: Int) {
                val item = adapter.data.get(adapterPosition)
                item.relation = position
//                item.relationValue = tv
                infoView.setContentText(tv)
//                adapter.notifyItemChanged(adapterPosition)
                checkAdapterPreTwoDone()
            }
        })
    }

    var dicInfoBean: PersonalInformationDictBean? = null

    var initList = arrayListOf<ZipContractBean>()

    val adapter = ZipContractAdapter()

    override fun createObserver() {
        mViewModel.personDicLiveData.observe(this) {
            dicInfoBean = it
        }
    }

    override fun getData() {
    }
}