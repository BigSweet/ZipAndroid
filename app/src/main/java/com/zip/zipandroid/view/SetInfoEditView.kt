package com.zip.zipandroid.view

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.zip.zipandroid.R
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.ktx.visible
import com.zip.zipandroid.ktx.visiblein
import com.zip.zipandroid.shape.ShapeEditTextView
import com.zip.zipandroid.utils.EmailValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.Locale

class SetInfoEditView : RelativeLayout {
    constructor(context: Context?) : super(context) {
        init(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private var infoArrow: ImageView? = null
    private var infoEdit: ShapeEditTextView? = null
    private var infoX: TextView? = null
    private var infoTopName: TextView? = null
    private var infoMaxLength: TextView? = null

    companion object {
        const val TYPE_NAME = 1
        const val TYPE_BVN = 2
        const val TYPE_EMAIL = 3
        const val TYPE_ADDRESS = 4
        const val TYPE_PAY_DAY = 5
        const val TYPE_INCOME = 6
    }

    var infoViewClick: (() -> Unit)? = null
    fun init(context: Context?, attrs: AttributeSet?) {
        context ?: return
        var view = LayoutInflater.from(context).inflate(R.layout.view_set_info_edit, this, true)
        infoEdit = view.findViewById(R.id.set_info_edit)
        val infoViewCl = view.findViewById<View>(R.id.info_view_cl)
        infoX = view.findViewById(R.id.set_info_xx)
        infoArrow = view.findViewById(R.id.set_info_arrow)
        infoTopName = view.findViewById(R.id.set_info_top_name)
        infoMaxLength = view.findViewById(R.id.set_info_max_length)


        val a = context.obtainStyledAttributes(attrs, R.styleable.setInfoStyle)

        var topName = a.getString(R.styleable.setInfoStyle_infoTopName)
        var inputInfoType = a.getInt(R.styleable.setInfoStyle_inputInfoType, 0)
        infoTopName?.setText(topName)
        var hintName = a.getString(R.styleable.setInfoStyle_infoHintName)
        infoEdit?.setHint(hintName)
        var showArrow = a.getBoolean(R.styleable.setInfoStyle_showInfoArrow, false)
        infoArrow?.visible = showArrow
        var showX = a.getBoolean(R.styleable.setInfoStyle_showX, false)
        infoX?.visiblein = showX
        var showNumber = a.getBoolean(R.styleable.setInfoStyle_showBottomNumber, false)
        infoMaxLength?.visible = showNumber

        if (showArrow) {
            infoEdit?.let {
                it.isEnabled = false
                it.isFocusable = false
                it.isFocusableInTouchMode = false
                it.isClickable = true
                it.isLongClickable = false
                it.background = null
            }
            infoViewCl.setOnDelayClickListener {
                infoViewClick?.invoke()
            }
        }
        if (inputInfoType == TYPE_NAME) {
            infoEdit?.inputType = InputType.TYPE_CLASS_TEXT
        } else if (inputInfoType == TYPE_INCOME) {
            // 设置输入类型为数字
            infoEdit?.inputType = android.text.InputType.TYPE_CLASS_NUMBER
            // 初始显示 ₦ 符号
//            infoEdit?.setText("₦")
//            infoEdit?.setSelection(1) // 光标在 ₦ 后

//             添加 TextWatcher
//            infoEdit?.addTextChangedListener(CurrencyIncomeTextWatcher(infoEdit!!))

//            // 可选：监听焦点变化，确保始终显示 ₦
//            editText.setOnFocusChangeListener { _, hasFocus ->
//                if (hasFocus && editText.text.toString() == "₦") {
//                    editText.setSelection(1)
//                }
//            }
        } else if (inputInfoType == TYPE_BVN) {
            infoEdit?.inputType = InputType.TYPE_CLASS_NUMBER
//            infoEdit?.maxEms = 11
            infoEdit?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))
        } else if (inputInfoType == TYPE_PAY_DAY) {
            infoEdit?.inputType = InputType.TYPE_CLASS_NUMBER
            infoEdit?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2), InputFilter { source, start, end, dest, dstart, dend ->
                // 只允许输入数字
                if (source.toString().matches(Regex("\\d+"))) {
                    val newText = dest.replaceRange(dstart, dend, source.substring(start, end))
                    if (newText.isNotEmpty()) {
                        val value = newText.toString().toInt()
                        if (value !in 1..31) return@InputFilter "" // 拒绝无效输入
                    }
                    null // 接受输入
                } else {
                    "" // 拒绝非数字输入
                }
            })
        } else {
            infoEdit?.inputType = InputType.TYPE_CLASS_TEXT
        }

        infoEdit?.let {

            it.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (inputInfoType == TYPE_PAY_DAY) {
                        if ((it.text?.length ?: 0) < 1) {
                            it.tag = "error"
                            ToastUtils.showShort("Please enter a valid day")
                            it.background.setTint(Color.parseColor("#FFF1F1")) // 错误状态
                        } else {
                            it.tag = "completed"
                            it.background.setTint(Color.parseColor("#F1F5FF")) // 完成状态
                        }
                        it.clearFocus()
                        false
                    }

                    if (inputInfoType == TYPE_NAME) {
                        if ((it.text?.length ?: 0) < 2) {
                            it.tag = "error"
                            ToastUtils.showShort("Please enter a valid name")
                            it.background.setTint(Color.parseColor("#FFF1F1")) // 错误状态
                        } else {
                            it.tag = "completed"
                            it.background.setTint(Color.parseColor("#F1F5FF")) // 完成状态
                        }
                        it.clearFocus()
                        false
                    }
                    if (inputInfoType == TYPE_ADDRESS) {
                        if ((it.text?.length ?: 0) < 2) {
                            it.tag = "error"
                            ToastUtils.showShort("Minimum input length of two digits")
                            it.background.setTint(Color.parseColor("#FFF1F1")) // 错误状态
                        } else {
                            it.tag = "completed"
                            it.background.setTint(Color.parseColor("#F1F5FF")) // 完成状态
                        }
                        false
                    }
                    it.tag = "completed"
                    handleInputComplete()
                    true
                }
                false
            }

            // 默认状态
            it.setBackgroundColor(Color.parseColor("#F7F7F7"))

            it.setOnFocusChangeListener { _, hasFocus ->
                it.setBackgroundColor(
                    when {
                        it.tag == "error" -> Color.parseColor("#FFF1F1")
                        hasFocus -> Color.parseColor("#FFFAEF")
                        it.tag == "completed" -> Color.parseColor("#F1F5FF")
                        else -> Color.parseColor("#F7F7F7")
                    }
                )
            }
            it.addTextChangedListener(object : TextWatcher {
                private var isFormatting = false
                private val forbiddenRegex = """[0-9\p{P}\p{S}]""".toRegex()
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if (inputInfoType == TYPE_BVN) {
                        if (s.toString().length == 1) {
                            if (!s.toString().startsWith("2")) {
                                ToastUtils.showShort("Must start with 22")
                                it.setBackgroundColor(
                                    Color.parseColor("#FFF1F1")
                                )
                                s?.replace(0, 1, "")
                            }
                        } else if (s.toString().length == 2) {
                            if (!s.toString().startsWith("22")) {
                                ToastUtils.showShort("Must start with 22")
                                it.setBackgroundColor(
                                    Color.parseColor("#FFF1F1")
                                )
                                s?.replace(1, 2, "")
                            }
                        } else {
                            it.tag = "completed"
                            it.setBackgroundColor(
                                Color.parseColor("#F1F5FF")
                            )
                        }
                        infoMaxLength?.setText(s.toString().length.toString() + "/11")

                    }

                    if (inputInfoType == TYPE_EMAIL) {
                        if (EmailValidator.isValid(s.toString())) {
                            it.tag = "completed"
                            it.setBackgroundColor(
                                Color.parseColor("#F1F5FF")
                            )
                        } else {
                            it.setBackgroundColor(
                                Color.parseColor("#FFF1F1")
                            )
                        }
                    }
                    if (inputInfoType == TYPE_PAY_DAY) {

                    }
                    if (inputInfoType == TYPE_ADDRESS) {
                        if (isFormatting || s.isNullOrEmpty()) return
                        // 1. 移除所有空格 + 过滤非法字符
                        isFormatting = true
                        val filtered = s.toString()
                            .replace("\\s+".toRegex(), "")
                            .replace(forbiddenRegex, "")

                        // 2. 自动截断超长内容（避免粘贴绕过）
                        val truncated = if (filtered.length > 100) filtered.substring(0, 100) else filtered
                        // 4. 回写处理后的文本
                        if (s.toString() != truncated) {
                            s.replace(0, s.length, truncated)
                        }
                        it.tag = "completed"
                        it.setBackgroundColor(
                            Color.parseColor("#F1F5FF")
                        )
                        isFormatting = false
                    }
                    if (inputInfoType == TYPE_NAME) {
                        //
                        if (isFormatting || s.isNullOrEmpty()) return

                        isFormatting = true

                        // 1. 移除所有空格 + 过滤非法字符
                        val filtered = s.toString()
                            .replace("\\s+".toRegex(), "")
                            .replace(forbiddenRegex, "")

                        // 2. 自动截断超长内容（避免粘贴绕过）
                        val truncated = if (filtered.length > 50) filtered.substring(0, 50) else filtered

                        // 3. 首字母大写（保留其他字母原样）
                        val formatted = truncated.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                        }

                        // 4. 回写处理后的文本
                        if (s.toString() != formatted) {
                            s.replace(0, s.length, formatted)
                        }
                        it.tag = "completed"
                        it.setBackgroundColor(
                            Color.parseColor("#F1F5FF")
                        )
                        isFormatting = false
                    }
                    if (inputInfoType == TYPE_INCOME) {
                        if (isFormatting) return
                        isFormatting = true

                        try {
                            // 1. 移除所有非数字字符（包括可能的逗号和 ₦）
                            val originalString = s.toString().replace(Regex("[^\\d]"), "")

                            if (originalString.isNotEmpty()) {
                                // 2. 限制长度 1-10 位
                                val trimmedString = if (originalString.length > 10) {
                                    originalString.substring(0, 10)
                                } else {
                                    originalString
                                }

                                // 3. 格式化数字（添加千分位逗号和 ₦）
                                val number = trimmedString.toLong()
                                val formatted = "₦${decimalFormat.format(number)}"

                                // 4. 更新 EditText 显示
                                it.setText(formatted)
                                it.setSelection(formatted.length) // 光标移到末尾
                            } else {
                                // 5. 如果用户删除了所有数字，移除 ₦ 符号
                                it.setText("")
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
                            it.tag = "completed"
                            it.setBackgroundColor(
                                Color.parseColor("#F1F5FF")
                            )
                            isFormatting = false
                        }
                    }
                    debounceAndCheckCompletion()
                }
            })
        }


        a.recycle()
    }

    private val decimalFormat = DecimalFormat("#,###")

    var completeListener: (() -> Unit)? = null

    private fun handleInputComplete() {
        completeListener?.invoke()
    }


    private fun debounceAndCheckCompletion() {
        debounceJob?.cancel()
        debounceJob = CoroutineScope(Dispatchers.Main).launch {
            delay(800)
            handleInputComplete()
        }
    }

    private var debounceJob: Job? = null

    fun setContentText(text: String) {
        infoEdit?.setText(text)
    }

    fun getEditIsComplete(): Boolean {
        return infoEdit?.tag == "completed" && !infoEdit?.text.isNullOrEmpty()
    }

    fun showBoard() {
        infoEdit?.let { KeyboardUtils.showSoftInput(it) }
    }

    fun getEditText(): String {
        return infoEdit?.text.toString()
    }

    fun appendText(text: String) {
        infoEdit?.append(text)
    }

    fun warSetX(show: Boolean) {
        infoX?.visiblein = show
    }

    fun setTagComplete() {
        infoEdit?.tag = "completed"
        infoEdit?.background?.setTint(Color.parseColor("#F1F5FF")) // 完成状态
    }

    fun getRawNumericValue(): String {
        return infoEdit?.text.toString().replace(Regex("[^\\d]"), "")
    }

    fun getEditTextView(): ShapeEditTextView? {
        return infoEdit
    }

    fun isTextNotEmpty(): Boolean {
        return !infoEdit?.text.isNullOrEmpty()
    }


}