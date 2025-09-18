package com.zip.zipandroid.activity

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.reflect.TypeToken
import com.zip.zipandroid.R
import com.zip.zipandroid.adapter.ZipContractAdapter
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.bean.OriginUploadContractBean
import com.zip.zipandroid.bean.PersonalInformationDictBean
import com.zip.zipandroid.bean.UploadContractBean
import com.zip.zipandroid.bean.ZipContractBean
import com.zip.zipandroid.databinding.ActivityZipContractBinding
import com.zip.zipandroid.event.ZipFinishInfoEvent
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.pop.SingleCommonSelectPop
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.utils.UserInfoUtils
import com.zip.zipandroid.utils.ZipEventBusUtils
import com.zip.zipandroid.utils.ZipTrackUtils
import com.zip.zipandroid.view.SetInfoEditView
import com.zip.zipandroid.viewmodel.PersonInfoViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ZipFinishInfoEvent) {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        ZipEventBusUtils.unregister(this)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        ZipTrackUtils.track("OutContactInfo")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZipTrackUtils.track("InContactInfo")
        ZipEventBusUtils.register(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            ZipTrackUtils.track("OutContactInfo")
            finish()
        }
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Contact Info")

        mViewBind.contractRv.layoutManager = LinearLayoutManager(this)
//        mViewBind.contractRv.adapter = adapter
        adapter.bindToRecyclerView(mViewBind.contractRv)

        adapter.popClick = object : ((Int, Int) -> Unit) {


            override fun invoke(position: Int, id: Int) {
                val setInfoView = adapter.getViewByPosition(position, (R.id.item_contract_relation_tv)) as SetInfoEditView?
                if (setInfoView != null) {
                    if (id == 1) {
                        showSelectPop(adapter.data.get(position).title, dicInfoBean?.familyRelation, SingleCommonSelectPop.relation_type, adapter.data.get(position).relation
                            ?: 0, setInfoView, position)
                    } else {
                        showSelectPop(adapter.data.get(position).title, dicInfoBean?.relation, SingleCommonSelectPop.relation_type, adapter.data.get(position).relation
                            ?: 0, setInfoView, position)
                    }
                }
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
            ZipTrackUtils.track("SubmitContactInfo")
        }
        mViewModel.saveInfoLiveData.observe(this) {
            //保存进件到第几部了
            mViewModel.saveMemberBehavior(Constants.TYPE_CONS)
        }
        mViewModel.saveMemberInfoLiveData.observe(this) {
            if (it == Constants.TYPE_CONS) {
                dismissLoading()
//                ToastUtils.showShort("finish3")
                startActivity(ZipQuestionActivity::class.java)
            }
        }
    }

    private fun convertData(): ArrayList<UploadContractBean> {
        val list = arrayListOf<UploadContractBean>()
        adapter.data.forEachIndexed { index, zipContractBean ->
            if ((zipContractBean.relation ?: -1) > -1) {
                val nameValue = zipContractBean.contactName
                val numberValue = zipContractBean.contactPhone
                val relationValue = zipContractBean.relationValue
                val bean = UploadContractBean(nameValue, numberValue, zipContractBean.relation
                    ?: -1, relationValue)
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

            val nameValue = zipContractBean.contactName
            val numberValue = zipContractBean.contactPhone
            val relationValue = zipContractBean.relationValue

            if (index == 0 && !numberValue.isNullOrEmpty()) {
                firstPhone = numberValue
            }

            if (!nameValue.isNullOrEmpty() && !numberValue.isNullOrEmpty() && !relationValue.isNullOrEmpty()) {
                twoDone++
            }
            if (index == 1 && !numberValue.isNullOrEmpty()) {
                secondPhone = numberValue
            }
        }
        if (!secondPhone.isNullOrEmpty() && !firstPhone.isNullOrEmpty() && secondPhone == firstPhone) {
            ToastUtils.showShort("This phone number has already been used. Please enter a different one")
        }
        if (twoDone == 2 && secondPhone != firstPhone) {
            //俩个都填好了而且电话不一样
            done = true
        }
        if (firstPhone == UserInfoUtils.getUserPhone() || secondPhone == UserInfoUtils.getUserPhone()) {
            ToastUtils.showShort("The contact’s mobile number matches your own. Please enter a different number")
            done = false
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
                item.relationValue = tv
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
        mViewModel.userInfoLiveData.observe(this) {
            if (!it.emergencyContactPerson.isNullOrEmpty()) {
                initList.clear()
                val list = GsonUtils.fromJson<List<OriginUploadContractBean>>(it.emergencyContactPerson, object : TypeToken<List<OriginUploadContractBean>>() {}.type)
                if (!list.isNullOrEmpty()) {
                    list.forEachIndexed { index, originUploadContractBean ->
                        if (index == 0) {
                            val firstBean = ZipContractBean(1, "Family member", true)
                            firstBean.contactPhone = originUploadContractBean.contactPhone
                            firstBean.contactName = originUploadContractBean.contactName
                            firstBean.relation = originUploadContractBean.relation
                            firstBean.relationValue = originUploadContractBean.relationValue
                            initList.add(firstBean)
                        } else if (index == 1) {
                            val firstBean = ZipContractBean(2, "Colleague/Friend", true)
                            firstBean.contactPhone = originUploadContractBean.contactPhone
                            firstBean.contactName = originUploadContractBean.contactName
                            firstBean.relation = originUploadContractBean.relation
                            firstBean.relationValue = originUploadContractBean.relationValue
                            initList.add(firstBean)
                        } else {
                            val otherContract = ZipContractBean(idx++, "Colleague/Friend", false)
                            otherContract.contactPhone = originUploadContractBean.contactPhone
                            otherContract.contactName = originUploadContractBean.contactName
                            otherContract.relation = originUploadContractBean.relation
                            otherContract.relationValue = originUploadContractBean.relationValue
                            initList.add(otherContract)
                        }

                    }
                    adapter.setNewData(initList)
                } else {
                    initList.add(ZipContractBean(1, "Family member", true))
                    initList.add(ZipContractBean(2, "Colleague/Friend", true))
                    adapter.setNewData(initList)
                }
            } else {
                initList.add(ZipContractBean(1, "Family member", true))
                initList.add(ZipContractBean(2, "Colleague/Friend", true))
                adapter.setNewData(initList)
            }
        }
    }

    override fun getData() {
    }
}