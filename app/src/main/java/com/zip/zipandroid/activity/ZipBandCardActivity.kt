package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.lxj.xpopup.XPopup
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.bean.ZipBankNameListBean
import com.zip.zipandroid.bean.ZipBankNameListBeanItem
import com.zip.zipandroid.bean.ZipQueryCardBeanItem
import com.zip.zipandroid.bean.ZipUserInfoBean
import com.zip.zipandroid.databinding.ActivityZipBandCardBinding
import com.zip.zipandroid.event.ZipFinishInfoEvent
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.pop.ZipBankNamePop
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.utils.EventBusUtils
import com.zip.zipandroid.utils.UserInfoUtils
import com.zip.zipandroid.view.SetInfoEditView
import com.zip.zipandroid.viewmodel.PersonInfoViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ZipBandCardActivity : ZipBaseBindingActivity<PersonInfoViewModel, ActivityZipBandCardBinding>() {

    companion object {
        @JvmStatic
        fun start(context: Context, fromMine: Boolean) {
            val starter = Intent(context, ZipBandCardActivity::class.java)
                .putExtra("fromMine", fromMine)
            context.startActivity(starter)
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ZipFinishInfoEvent) {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusUtils.unregister(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBusUtils.register(this)
    }

    var fromMine = false
    override fun initView(savedInstanceState: Bundle?) {
        fromMine = intent.getBooleanExtra("fromMine", false)
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Bank Info")
        mViewBind.zipBankName.infoViewClick = {
            if (!dataPrepare) {
                ToastUtils.showShort("Data preparation")
            } else {
                showBankListPop()
            }
        }
        mViewModel.zipQueryCard()
        mViewModel.getBankList()
        mViewModel.getUserInfo()
        focusChangeCheck(mViewBind.zipBankAccount)
        mViewBind.infoNextBtn.setOnDelayClickListener {

            showLoading()
            currentBandCardBean?.let {
                mViewModel.zipBandCard(it.id.toString(), it.bankName, mViewBind.zipBankAccount.getEditText(), it.payType.toString(),
                    userInfoBean?.firstName.toString(), userInfoBean?.realname.toString(), userInfoBean?.identity.toString(), userInfoBean?.lastName.toString(), UserInfoUtils.getUserPhone())
            }

        }
        mViewModel.userInfoLiveData.observe(this) {
            userInfoBean = it
        }
    }

    var userInfoBean: ZipUserInfoBean? = null

    fun focusChangeCheck(infoView: SetInfoEditView) {
        infoView.completeListener = {
            checkDone()
        }

    }


    fun checkDone() {
        val done = mViewBind.zipBankName.getEditIsComplete() && mViewBind.zipBankAccount.getEditIsComplete()
        mViewBind.infoNextBtn.setEnabledPlus(done)
    }

    var currentBandCardBean: ZipBankNameListBeanItem? = null
    private fun showBankListPop() {
        val pop = ZipBankNamePop(this, bankList)
        pop.selectBank = {
            currentBandCardBean = it
            mViewBind.zipBankName.setContentText(it.bankName)
            mViewBind.zipBankName.setTagComplete()
            checkDone()
        }
        XPopup.Builder(this).asCustom(pop).show()
    }

    //    {
//        "bankName": "FEDERALPOLY NASARAWAMFB",
//        "icon": "https://transsnet-android-upload-image-prod.s3.amazonaws.com/activity/169345215069216-FEDERAL%20POLY%20NASSARAWA.png",
//        "id": 2791,
//        "payType": 0
//    }
//
    override fun createObserver() {
//        mViewModel.bankListLiveData.observe(this) {
//            dismissLoading()
//        }
        mViewModel.bandCardLiveData.observe(this) { bandIt->
            UserInfoUtils.saveBankData(Gson().toJson(currentBandCardBean))
            currentBandCardBean?.let {
                mViewModel.zipChangeCard(it.id.toString(), it.bankName, mViewBind.zipBankAccount.getEditText(), it.payType.toString(),
                    userInfoBean?.firstName.toString(), userInfoBean?.realname.toString(), userInfoBean?.identity.toString(), userInfoBean?.lastName.toString(), UserInfoUtils.getUserPhone(),bandIt.tiedCardId)
            }

        }
        mViewModel.changeCardLiveData.observe(this) {
            mViewModel.saveMemberBehavior(Constants.TYPE_BANK)
        }
        mViewModel.saveMemberInfoLiveData.observe(this) {
            if (it == Constants.TYPE_BANK) {
                //下一个界面
                dismissLoading()
                if (fromMine) {
                    finish()
                } else {
//                    ToastUtils.showShort("finish5")
                    //进入额度计算页面
                    EventBusUtils.post(ZipFinishInfoEvent())
                    //选择放款银行卡
                    finish()
                    ZipOrderReviewActivity.start(this, "")
                }
//

            }
        }

        mViewModel.cardListLiveData.observe(this) {
            if (!it.isNullOrEmpty()) {
                val data = it.find {
                    it.status == 1
                }
                if (data != null) {
                    setCurrentBankData(data)
                }
            }
        }
        mViewModel.bankListLiveData.observe(this) {
//            dismissLoading()
            dataPrepare = true
            bankList = it
            bankList?.forEach {
                it.getNamePin()
            }
        }
    }

    private fun setCurrentBankData(data: ZipQueryCardBeanItem) {
        val currentData = ZipBankNameListBeanItem(data.bankName, "", data.bankId, data.cardType)
        currentBandCardBean = currentData
        mViewBind.zipBankName.setContentText(currentData.bankName)
        mViewBind.zipBankName.setTagComplete()
        mViewBind.zipBankAccount.setContentText(data.cardNo.toString())
        mViewBind.zipBankAccount.setTagComplete()
        checkDone()
    }

    var bankList: ZipBankNameListBean? = null

    var dataPrepare = false

    override fun getData() {
    }
}