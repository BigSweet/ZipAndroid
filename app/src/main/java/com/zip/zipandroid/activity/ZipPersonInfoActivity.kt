package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.contrarywind.view.WheelView
import com.lxj.xpopup.XPopup
import com.zip.zipandroid.R
import com.zip.zipandroid.adapter.ZipPersonInfoEmailAdapter
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.bean.AddressInfoBean
import com.zip.zipandroid.bean.PersonalInformationDictBean
import com.zip.zipandroid.bean.PhotoPathBean
import com.zip.zipandroid.bean.ZipIndImgBean
import com.zip.zipandroid.databinding.ActivityZipPersonInfoBinding
import com.zip.zipandroid.ktx.hide
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.ktx.show
import com.zip.zipandroid.pop.SingleCommonSelectPop
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.utils.UserInfoUtils
import com.zip.zipandroid.utils.ZipStringUtils
import com.zip.zipandroid.view.SetInfoEditView
import com.zip.zipandroid.viewmodel.PersonInfoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ZipPersonInfoActivity : ZipBaseBindingActivity<PersonInfoViewModel, ActivityZipPersonInfoBinding>() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ZipPersonInfoActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Personal Info")
        mViewBind.firstNameInfoView.showBoard()

        mViewBind.recommendEmailRv.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        mViewBind.recommendEmailRv.adapter = emailAdapter
        emailAdapter.setOnItemClickListener { baseQuickAdapter, view, i ->
            val item = baseQuickAdapter.getItem(i) as String
            mViewBind.emailInfoView.appendText("@" + item)
        }

        val systemLocale = Locale.getDefault()
        language = systemLocale.language
        if (!language?.isNullOrEmpty()) {
            mViewBind.langInfoView.hide()
        } else {
            mViewBind.langInfoView.show()
        }
//        mViewBind.bvnInfoView.setContentText("22298656042")
//        mViewModel.checkBvn("22298656042")
        mViewBind.eduInfoView.infoViewClick = {
            showSelectPop("Education", dicInfoBean?.degree, SingleCommonSelectPop.edu_type, mViewBind.eduInfoView)
        }
        mViewBind.maInfoView.infoViewClick = {
            showSelectPop("Marital Status", dicInfoBean?.marry, SingleCommonSelectPop.ma_type, mViewBind.maInfoView)
        }
        mViewBind.numberInfoView.infoViewClick = {
            showSelectPop("Number of Children", dicInfoBean?.childrens, SingleCommonSelectPop.child_type, mViewBind.numberInfoView)
        }
        mViewBind.langInfoView.infoViewClick = {
            showSelectPop("Languages", dicInfoBean?.language, SingleCommonSelectPop.la_type, mViewBind.langInfoView)
        }
        mViewBind.birthdayInfoView.infoViewClick = {
            //showbrith
            KeyboardUtils.hideSoftInput(this)
            showBirthDayPickView("Date of Birth")
        }
        mViewBind.addressInfoView.infoViewClick = {
            if (addressPrepare) {
                KeyboardUtils.hideSoftInput(this)
                showAddressPickerView()
            } else {
                ToastUtils.showShort("Data preparationp")
            }

        }

        mViewModel.getUserInfo()
        mViewModel.getPersonInfoDic()
        mViewModel.getAllAddressInfo()
        mViewBind.infoFemaleTv.setOnDelayClickListener {
            clickFeMale()
        }
        mViewBind.infoMaleTv.setOnDelayClickListener {
            clickMale()
        }
        focusChangeCheck(mViewBind.firstNameInfoView)
        focusChangeCheck(mViewBind.lastNameInfoView)
        focusChangeCheck(mViewBind.bvnInfoView)
        focusChangeCheck(mViewBind.emailInfoView)
        focusChangeCheck(mViewBind.detailAddressInfoView)

        mViewBind.infoNextBtn.setOnDelayClickListener {
            showLoading()
            mViewModel.checkBvn(mViewBind.bvnInfoView.getEditText())
        }
    }

    val emailAdapter = ZipPersonInfoEmailAdapter()

    var dicInfoBean: PersonalInformationDictBean? = null
    var currentIdeImg = ""
    var servicePath = ""

    sealed class ProcessResult {
        object Success : ProcessResult()
        data class Error(val message: String) : ProcessResult()
    }

    fun processDataAsync(data: List<AddressInfoBean>, callback: (ProcessResult) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            // 标记处理开始
            val result = withContext(Dispatchers.IO) { // IO 线程处理数据
                try {
                    // 模拟耗时操作（如解析、计算、存储等）
                    processData(data)
                    ProcessResult.Success
                } catch (e: Exception) {
                    ProcessResult.Error(e.message ?: "Unknown error")
                }
            }
            // 回调主线程
            callback(result)
        }
    }

    private fun processData(data: List<AddressInfoBean>) {
        options1Items.clear()
        options1Items.addAll(data)
        data.forEach { mainData ->
            //遍历省份
            val cityList = arrayListOf<AddressInfoBean>() //该省的城市列表（第二级）
            val province_AreaList = arrayListOf<ArrayList<AddressInfoBean>>() //该省的所有地区列表（第三极）
            mainData.child?.forEach {
                cityList.add(AddressInfoBean(it.name, null))
                val city_AreaList = arrayListOf<AddressInfoBean>() //该城市的所有地区列表
                it.child?.let { it1 -> city_AreaList.addAll(it1) }
                province_AreaList.add(city_AreaList)
            }
            options2Items.add(cityList)
            options3Items.add(province_AreaList)
        }

    }

    private fun showAddressPickerView() { // 弹出选择器
        val pvOptions = OptionsPickerBuilder(this, object : OnOptionsSelectListener {
            override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
                val opt1tx = if (options1Items.size > 0) options1Items[options1].pickerViewText else ""
                val opt2tx: String = if (options2Items.size > 0
                    && options2Items[options1].isNotEmpty()
                ) options2Items[options1][options2].name else ""
                val opt3tx: String = if (options2Items.size > 0 && options3Items[options1].isNotEmpty() && options3Items[options1][options2].isNotEmpty()) options3Items[options1][options2][options3].name else ""
                val tx = "$opt1tx $opt2tx $opt3tx"
                mViewBind.addressInfoView.setContentText(tx)
//                Log.d("选择的区域是", tx)
            }

        })
        pvOptions.setTitleText("Home Address")
        pvOptions.setSubmitColor(Color.WHITE)
        pvOptions.setDividerColor(Color.TRANSPARENT)
        pvOptions.setOutSideCancelable(true)
        pvOptions.setItemVisibleCount(12)
        pvOptions.setDividerType(WheelView.DividerType.FILL)
//        pvOptions.setBgColor(Color.parseColor("#FFE8EEFF"))
        pvOptions.setTextColorCenter(Color.BLACK) //设置选中项文字颜色
        pvOptions.setContentTextSize(14)
        val realView = pvOptions.build<AddressInfoBean>()

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        realView.setPicker(options1Items, options2Items, options3Items) //三级选择器
        realView.show()
    }

    private var options1Items = arrayListOf<AddressInfoBean>()
    private val options2Items = arrayListOf<List<AddressInfoBean>>()
    private val options3Items = arrayListOf<List<List<AddressInfoBean>>>()

    var addressPrepare = false
    override fun createObserver() {
        mViewModel.allAddressInfo.observe(this) {
            processDataAsync(it) { result ->
                when (result) {
                    is ProcessResult.Success -> addressPrepare = true
                    is ProcessResult.Error -> mViewModel.getAllAddressInfo()
                }
            }
        }
        mViewModel.realNameInfoLiveData.observe(this) {
            //更新用户信息
            val imgBean = ZipIndImgBean(PhotoPathBean(currentIdeImg), PhotoPathBean(servicePath))
            mViewModel.saveUserInfo(age, brithDay, brithDayStr, mViewBind.eduInfoView.getEditText(), degree, mViewBind.bvnInfoView.getEditText(),
                imgBean, mViewBind.emailInfoView.getEditText(), UserInfoUtils.getUserPhone(), "1", mViewBind.addressInfoView.getEditText(), mViewBind.detailAddressInfoView.getEditText(), sex, marry, childrens, language, it.custId,
                mViewBind.firstNameInfoView.getEditText(), mViewBind.middleNameInfoView.getEditText(), mViewBind.lastNameInfoView.getEditText())
        }
        mViewModel.saveInfoLiveData.observe(this) {
            //保存进件到第几部了
            mViewModel.saveMemberBehavior(Constants.TYPE_REAL)
        }
        mViewModel.saveMemberInfoLiveData.observe(this) {
            if (it == Constants.TYPE_REAL) {
                mViewModel.saveMemberBehavior(Constants.TYPE_ADDRESS)
            }
            if (it == Constants.TYPE_ADDRESS) {
                mViewModel.saveMemberBehavior(Constants.TYPE_REAL)
            }
            if (it == Constants.TYPE_REAL) {
                //下一个界面
                dismissLoading()
                ToastUtils.showShort("finish")

            }
        }
        mViewModel.uploadImgLiveData.observe(this) {
            Log.d("获取bvn图片成功", it)
            currentIdeImg = it
            //实名
            mViewModel.realName(mViewBind.bvnInfoView.getEditText(), brithDay, brithDayStr, mViewBind.firstNameInfoView.getEditText(), mViewBind.middleNameInfoView.getEditText(), mViewBind.lastNameInfoView.getEditText(), sex)

        }

        mViewModel.servicePathLiveData.observe(this) {
            servicePath = it
        }
        mViewModel.personDicLiveData.observe(this) {
            dicInfoBean = it
            if (!it.emailSuffix.isNullOrEmpty()) {
                //email
                emailAdapter.setNewData(it.emailSuffix)
            }
        }
        mViewModel.bvnInfoLiveData.observe(this) {
            if (!it.photo.isNullOrEmpty()) {
                //去换成图片地址
                mViewModel.getPhotoUrlByBase(it.photo)
            }
        }
        mViewModel.failLiveData.observe(this) {
            dismissLoading()
        }
        mViewModel.userInfoLiveData.observe(this) {
            val span = SpannableStringBuilder()
            if (!it.firstName.isNullOrEmpty()) {
                mViewBind.firstNameInfoView.setContentText(it.firstName)
                span.append(it.firstName)
            }
            if (!it.midName.isNullOrEmpty()) {
                mViewBind.middleNameInfoView.setContentText(it.midName)
                span.append(" " + it.midName)
            }
            if (!it.lastName.isNullOrEmpty()) {
                mViewBind.lastNameInfoView.setContentText(it.lastName)
                span.append(" " + it.lastName)
            }
            if (span.length > 0) {
                realName = span.toString()
            }
            //0和1
            if (!it.sex.isNullOrEmpty()) {
                //0 女 1 男
                if (it.sex == "1") {
                    clickMale()
                } else if (it.sex == "0") {
                    clickFeMale()
                }
            }
            if (!it.birthDateStr.isNullOrEmpty()) {
                brithDayStr = it.birthDateStr
                brithDay = it.birthDate
                mViewBind.birthdayInfoView.setContentText(it.birthDateStr)
            }
            if (!it.identity.isNullOrEmpty()) {
                mViewBind.bvnInfoView.setContentText(it.identity)
            }
            if (it.degree > 0) {
                //学历
                dicInfoBean?.degree?.get(it.degree - 1)?.let { it1 -> mViewBind.eduInfoView.setContentText(it1) }
                degree = it.degree
            }
            if (it.marry > 0) {
                dicInfoBean?.marry?.get(it.marry - 1)?.let { it1 -> mViewBind.maInfoView.setContentText(it1) }
                marry = it.marry
            }
//            if(it.identity.isNullOrEmpty()){
//
//            }
            if (it.childrens > 0) {
                childrens = it.childrens
                dicInfoBean?.childrens?.get(it.childrens - 1)?.let { it1 -> mViewBind.numberInfoView.setContentText(it1) }
            }
            if (!it.mbEmail.isNullOrEmpty()) {
                mbEmail = it.mbEmail
                mViewBind.emailInfoView.setContentText(it.mbEmail)
            }
            if (!it.idAddress.isNullOrEmpty()) {
                mViewBind.addressInfoView.setContentText(it.idAddress)
            }
            if (!it.nowAddress.isNullOrEmpty()) {
                mViewBind.detailAddressInfoView.setContentText(it.nowAddress)
            }
            if (it.language > 0) {
                languageIndex = it.language
                dicInfoBean?.language?.get(it.language - 1)?.let { it1 -> mViewBind.langInfoView.setContentText(it1) }
            }

            if (!it.mbPhone.isNullOrEmpty()) {
                UserInfoUtils.setUserPhone(it.mbPhone)
            }
        }


    }


    fun calculateAge(birthday: Calendar): Int {
        val today = Calendar.getInstance()

        var age = today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR)

        // 检查是否已过生日
        if (today.get(Calendar.MONTH) < birthday.get(Calendar.MONTH) ||
            (today.get(Calendar.MONTH) == birthday.get(Calendar.MONTH) &&
                    today.get(Calendar.DAY_OF_MONTH) < birthday.get(Calendar.DAY_OF_MONTH))
        ) {
            age--  // 如果今年还没过生日，年龄减1
        }
        return age
    }

    var show: Calendar? = null
    fun showBirthDayPickView(title: String) {
        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        start[Calendar.YEAR] = end[Calendar.YEAR] - 75
        end[Calendar.YEAR] = end[Calendar.YEAR]
        if (show == null) {
            show = Calendar.getInstance()
            show!![Calendar.YEAR] = end[Calendar.YEAR] - 30
        }

        var pickerView = TimePickerBuilder(getContext(), object : OnTimeSelectListener {
            override fun onTimeSelect(date: Date?, v: View?) {
                val result = Calendar.getInstance()
                result.time = date
                show = result
                val calendar = Calendar.getInstance()
                calendar.time = date
                age = calculateAge(calendar)
                val day = calendar[Calendar.DATE]
                val realDay = ZipStringUtils.addZero(day)
                val month = ZipStringUtils.addZero(calendar[Calendar.MONTH] + 1)
                val year = calendar.get(Calendar.YEAR)
                brithDayStr = "$year-$month-$realDay"
                brithDay = calendar.time.time

                Log.d("选的日期", "$year-$month-$realDay" + "数字时间" + brithDay)
                mViewBind.birthdayInfoView.setContentText(brithDayStr)
            }

        })
            .setType(booleanArrayOf(true, true, true, false, false, false)) // 默认全部显示
            .setCancelText("Cancel") //取消按钮文字
            .setSubmitText("Confirm") //确认按钮文字
            //                .setContentSize(18)//滚轮文字大小
            .setTitleText(title) //标题文字
            .setOutSideCancelable(true) //点击屏幕，点在控件外部范围时，是否取消显示
            .isCyclic(false) //是否循环滚动
            .setDividerColor(Color.TRANSPARENT)
            .setTitleColor(getResources().getColor(R.color.black)) //标题文字颜色
            .setSubmitColor(getResources().getColor(R.color.white)) //确定按钮文字颜色
            .setCancelColor(getResources().getColor(R.color.cFF3667F0)) //取消按钮文字颜色
            .setTitleBgColor(Color.WHITE) //标题背景颜色 Night mode
            .setBgColor(Color.WHITE) //滚轮背景颜色 Night mode
            .setDate(show) // 如果不设置的话，默认是系统时间*/
            .setRangDate(start, end) //起始终止年月日设定
            .setLabel("", "", "", "", "", "") //默认设置为年月日时分秒
            .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
            .isDialog(false) //是否显示为对话框样式
            .build()
        pickerView.show()
    }

    fun focusChangeCheck(infoView: SetInfoEditView) {
        infoView.completeListener = {
            checkAllDone()
        }

    }

    fun showSelectPop(title: String, data: List<String>?, type: Int, infoView: SetInfoEditView) {
        //选择完成后检测
        KeyboardUtils.hideSoftInput(this)
        data ?: return
        val pop = SingleCommonSelectPop(this, title, data, type)
        pop.sureClick = object : ((String, Int, Int) -> Unit) {
            override fun invoke(tv: String, position: Int, type: Int) {
                if (type == SingleCommonSelectPop.edu_type) {
                    degree = position
                }
                if (type == SingleCommonSelectPop.ma_type) {
                    marry = position
                }
                if (type == SingleCommonSelectPop.child_type) {
                    childrens = position
                }
                if (type == SingleCommonSelectPop.la_type) {
                    languageIndex = position
                }
                infoView.setContentText(tv)
                checkAllDone()

            }

        }
        XPopup.Builder(this).asCustom(pop).show()
    }

    fun clickFeMale() {
        checkAllDone()
        mViewBind.infoFemaleTv.setBackground(Color.parseColor("#F1F5FF"))
        mViewBind.infoFemaleTv.setTextColor(Color.parseColor("#3667F0"))
        mViewBind.infoMaleTv.setBackground(Color.parseColor("#F7F7F7"))
        mViewBind.infoMaleTv.setTextColor(Color.parseColor("#000000"))
        sex = 0

    }

    fun checkAllDone() {

        val done = mViewBind.firstNameInfoView.getEditIsComplete() &&
                mViewBind.lastNameInfoView.getEditIsComplete() &&
                sex != -1 &&
                mViewBind.birthdayInfoView.isTextNotEmpty() &&
                mViewBind.bvnInfoView.getEditIsComplete() &&
                mViewBind.eduInfoView.isTextNotEmpty() &&
                mViewBind.maInfoView.isTextNotEmpty() &&
                mViewBind.numberInfoView.isTextNotEmpty() &&
                mViewBind.emailInfoView.getEditIsComplete() &&
                mViewBind.addressInfoView.isTextNotEmpty() &&
                mViewBind.detailAddressInfoView.isTextNotEmpty() && !language.isNullOrEmpty()
        mViewBind.infoNextBtn.setEnabledPlus(done)
    }

    fun clickMale() {
        checkAllDone()
        mViewBind.infoMaleTv.setBackground(Color.parseColor("#F1F5FF"))
        mViewBind.infoMaleTv.setTextColor(Color.parseColor("#3667F0"))
        mViewBind.infoFemaleTv.setBackground(Color.parseColor("#F7F7F7"))
        mViewBind.infoFemaleTv.setTextColor(Color.parseColor("#000000"))
        sex = 1
    }

    var brithDayStr = ""
    var mbEmail = ""
    var realName = ""
    var brithDay = 0L
    var sex = -1
    var degree = 0
    var age = 0
    var marry = 0
    var childrens = 0
    var language = ""
    var languageIndex = 0

    override fun getData() {
    }
}