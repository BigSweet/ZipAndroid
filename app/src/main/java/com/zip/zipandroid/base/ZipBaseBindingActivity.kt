package com.zip.zipandroid.base

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.viewbinding.ViewBinding
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.PermissionUtils
import com.contrarywind.view.WheelView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lxj.xpopup.XPopup
import com.tencent.mmkv.MMKV
import com.zip.zipandroid.R
import com.zip.zipandroid.ZipApplication
import com.zip.zipandroid.bean.AddressInfoBean
import com.zip.zipandroid.pop.SingleCommonSelectPop
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.utils.ZipDesUtil
import com.zip.zipandroid.utils.ZipProjectUtil.getPhotoLocation
import com.zip.zipandroid.utils.ZipLoadingUtils
import com.zip.zipandroid.utils.phonedate.ZipPhoneDateProvider
import com.zip.zipandroid.utils.phonedate.applist.ZipInstalledApp
import com.zip.zipandroid.utils.phonedate.applist.ZipInstalledAppListener
import com.zip.zipandroid.utils.phonedate.calendar.ZipCalendarInfos
import com.zip.zipandroid.utils.phonedate.calendar.ZipCalendarListener
import com.zip.zipandroid.utils.phonedate.calllog.ZipCallLog
import com.zip.zipandroid.utils.phonedate.calllog.ZipCallLogListener
import com.zip.zipandroid.utils.phonedate.sms.ZipSMSMessage
import com.zip.zipandroid.utils.phonedate.sms.ZipSMSMessageListener
import com.zip.zipandroid.view.SetInfoEditView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

abstract class ZipBaseBindingActivity<VM : ZipBaseViewModel, VB : ViewBinding> : ZipBaseModelActivity<VM>() {

    override fun layoutId(): Int = 0

    lateinit var mViewBind: VB

    fun showLoading() {
        ZipLoadingUtils.show(this)
    }


    open fun getContext(): Context {
        return this
    }

    fun dismissLoading() {
        ZipLoadingUtils.dismiss()
    }

    fun getPerList(): ArrayList<String> {
        val list = arrayListOf<String>()
        list.add(Manifest.permission.READ_SMS)
        list.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        return list
    }


    /**
     * 创建DataBinding
     */
    override fun initDataBind(): View? {
        mViewBind = inflateBindingWithGeneric(layoutInflater)
        return mViewBind.root

    }

    fun updateToolbarTopMargin(v: View?) {
        if (v == null) {
            return
        }
        val lp: Any? = v.layoutParams
        if (lp != null && lp is ViewGroup.MarginLayoutParams) {
            lp.topMargin = BarUtils.getStatusBarHeight()
        }
    }


    private var options1Items = arrayListOf<AddressInfoBean>()
    private val options2Items = arrayListOf<List<AddressInfoBean>>()
    private val options3Items = arrayListOf<List<List<AddressInfoBean>>>()


    fun processData(data: List<AddressInfoBean>) {
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

    fun showAddressPickerView(selectListener: ((String,String,String) -> Unit)?) { // 弹出选择器
        KeyboardUtils.hideSoftInput(this)
        val pvOptions = OptionsPickerBuilder(this, object : OnOptionsSelectListener {
            override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
                val opt1tx = if (options1Items.size > 0) options1Items[options1].pickerViewText else ""
                val opt2tx: String = if (options2Items.size > 0
                    && options2Items[options1].isNotEmpty()
                ) options2Items[options1][options2].name else ""
                val opt3tx: String = if (options2Items.size > 0 && options3Items[options1].isNotEmpty() && options3Items[options1][options2].isNotEmpty()) options3Items[options1][options2][options3].name else ""

                selectListener?.invoke(opt1tx,opt2tx,opt3tx)
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

    var show: Calendar? = null


    fun showBirthDayPickView(title: String, selectListener: ((Calendar) -> Unit)? = null) {
        KeyboardUtils.hideSoftInput(this)
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
                selectListener?.invoke(calendar)
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

    open fun startActivity(clz: Class<*>?) {
        startActivity(Intent(this, clz))
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


    fun formatTimestamp(timestamp: Long): String {
        // 创建 SimpleDateFormat 实例，目标格式为 yyyy-MM-dd
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        // 设置时区（可选，默认是系统时区）
        sdf.timeZone = TimeZone.getDefault()
        // 将时间戳转为 Date 对象，再格式化为字符串
        return sdf.format(Date(timestamp))
    }


    fun showSelectPop(title: String, data: List<String>?, type: Int, selectPosition: Int,infoView: SetInfoEditView,selectListener:((String,Int,Int)->Unit)?=null) {
        //选择完成后检测
        KeyboardUtils.hideSoftInput(this)
        data ?: return
        val pop = SingleCommonSelectPop(this, title, data, type,selectPosition)
        pop.sureClick = object : ((String, Int, Int) -> Unit) {
            override fun invoke(tv: String, position: Int, type: Int) {
                infoView.setContentText(tv)
                selectListener?.invoke(tv,position,type)
            }

        }
        XPopup.Builder(this).asCustom(pop).show()
    }

    sealed class ProcessResult {
        object Success : ProcessResult()
        data class Error(val message: String) : ProcessResult()
    }


//    设备信息、短信、日历、蓝牙、Advertising ID、WIFI这些都要吗

    fun getAllPerData() {
        getCalendar()
        getSms()
//        getPhotoData("VIDEO")
//        getPhotoData("IMAGE")
//        getCallLogs()
        getInstallApp()
    }

    fun getCalendar() {
        if (PermissionUtils.isGranted(Manifest.permission.READ_CALENDAR) && PermissionUtils.isGranted(Manifest.permission.WRITE_CALENDAR)) {
            ZipPhoneDateProvider.sharedInstance(ZipApplication.instance).getCalendarInfo(object : ZipCalendarListener {
                override fun onCalendarFetched(calendar: Array<ZipCalendarInfos>) {
                    val calendar = ZipDesUtil.Base64Encode(Gson().toJson(calendar))
                    MMKV.defaultMMKV()?.putString("calendar", calendar)
                }

                override fun onError(s: String) {}
            })
        }

    }

    fun getSms() {
        if (PermissionUtils.isGranted(Manifest.permission.READ_SMS)) {
            ZipPhoneDateProvider.sharedInstance(ZipApplication.instance)
                .getSMSMessages(object : ZipSMSMessageListener {
                    override fun onSMSMessagesFetched(zipSmsMessages: Array<ZipSMSMessage?>?) {
                        val messages = ZipDesUtil.Base64Encode(Gson().toJson(zipSmsMessages))
                        MMKV.defaultMMKV()?.putString("smsMessage", messages)
                    }

                    override fun onError(s: String?) {}
                })
        }

    }

    fun getPhotoData(type: String?) {
        if (PermissionUtils.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Thread { getPhotoLocation(ZipApplication.instance?.applicationContext, type) }.start()
        }
    }


    fun getInstallApp() {
        ZipPhoneDateProvider.sharedInstance(ZipApplication.instance!!)
            .getInstalledApps(object : ZipInstalledAppListener {
                override fun onInstalledAppsFetched(zipInstalledApps: Array<ZipInstalledApp?>?) {
                    val apps = ZipDesUtil.Base64Encode(Gson().toJson(zipInstalledApps))
                    MMKV.defaultMMKV()?.putString("installedApp", apps)
                }
            })
    }

    fun getCallLogs() {
        if (ActivityCompat.checkSelfPermission(ZipApplication.instance!!, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            ZipPhoneDateProvider.sharedInstance(ZipApplication.instance)
                .getCallLogs(object : ZipCallLogListener {
                    override fun onCallLogsFetched(zipCallLogs: Array<ZipCallLog?>?) {
                        val logs = ZipDesUtil.Base64Encode(Gson().toJson(zipCallLogs))
                        MMKV.defaultMMKV()?.putString("callLog", logs)
                    }

                    override fun onError(s: String?) {}
                })
        }
    }


    val installAppInfo: Array<ZipInstalledApp?>?
        get() {
            var installAppDates: Array<ZipInstalledApp?>? = null
            if (!Constants.loadInstall) {
                val installedApp = ZipDesUtil.Base64Decode(MMKV.defaultMMKV()?.getString("installedApp",""))
                if (!TextUtils.isEmpty(installedApp)) {
                    installAppDates = Gson().fromJson(installedApp, object : TypeToken<Array<ZipInstalledApp?>?>() {}.type)
                }
            }
            return installAppDates
        }

    val callInfo: Array<ZipCallLog?>?
        get() {
            var zipCallLogDates: Array<ZipCallLog?>? = null
            if (!Constants.lodaCallInfo) {
                val callLog = ZipDesUtil.Base64Decode(MMKV.defaultMMKV()?.getString("callLog",""))
                if (!TextUtils.isEmpty(callLog)) {
                    zipCallLogDates = Gson().fromJson(callLog, object : TypeToken<Array<ZipCallLog?>?>() {}.type)
                }
            }
            return zipCallLogDates
        }

    val zipSmsMessageInfos: Array<ZipSMSMessage?>?
        get() {
            var zipSmsMessageDates: Array<ZipSMSMessage?>? = null
            if (!Constants.loadSms) {
                val smsMessage = ZipDesUtil.Base64Decode(MMKV.defaultMMKV()?.getString("smsMessage",""))
                if (!TextUtils.isEmpty(smsMessage)) {
                    zipSmsMessageDates = Gson().fromJson(smsMessage, object : TypeToken<Array<ZipSMSMessage?>?>() {}.type)
                }
            }
            return zipSmsMessageDates
        }

    val calendarInfo: Array<ZipCalendarInfos?>?
        get() {
            var calendarData: Array<ZipCalendarInfos?>? = null
            if (!Constants.loadCal) {
                val calendar = ZipDesUtil.Base64Decode(MMKV.defaultMMKV()?.getString("calendar",""))
                if (!TextUtils.isEmpty(calendar)) {
                    calendarData = Gson().fromJson(calendar, object : TypeToken<Array<ZipCalendarInfos?>?>() {}.type)
                }
            }
            return calendarData
        }


}