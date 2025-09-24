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
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.zip.zipandroid.R
import com.zip.zipandroid.ktx.hide
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.ktx.show
import com.zip.zipandroid.ktx.visible
import com.zip.zipandroid.ktx.visiblein
import com.zip.zipandroid.shape.ZipShapeEditTextView
import com.zip.zipandroid.shape.ZipShapeTextView
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
    private var infoEdit: ZipShapeEditTextView? = null
    private var infoPlaceTv: ZipShapeTextView? = null
    private var infoX: TextView? = null
    private var infoTopName: TextView? = null
    private var infoMaxLength: TextView? = null
    private var set_info_left_number: TextView? = null

    companion object {
        const val TYPE_NAME = 1
        const val TYPE_COMPANY_OR_SCHOOLD_NAME = 10
        const val TYPE_BVN = 2
        const val TYPE_EMAIL = 3
        const val TYPE_ADDRESS = 4
        const val TYPE_PAY_DAY = 5
        const val TYPE_INCOME = 6
        const val TYPE_UME_LENGTH = 7
        const val TYPE_PHONE = 8
        const val TYPE_BANK = 9
    }

    var maxValue = 11
    var infoViewClick: (() -> Unit)? = null
    fun init(context: Context?, attrs: AttributeSet?) {
        context ?: return
        var view = LayoutInflater.from(context).inflate(R.layout.view_set_info_edit, this, true)
        infoEdit = view.findViewById(R.id.set_info_edit)
        infoPlaceTv = view.findViewById(R.id.set_info_place_tv)
        val infoViewCl = view.findViewById<View>(R.id.info_view_cl)
        infoX = view.findViewById(R.id.set_info_xx)
        infoArrow = view.findViewById(R.id.set_info_arrow)
        infoTopName = view.findViewById(R.id.set_info_top_name)
        infoMaxLength = view.findViewById(R.id.set_info_max_length)
        set_info_left_number = view.findViewById(R.id.set_info_left_number)
//        infoEdit?.movementMethod = LinkMovementMethod.getInstance()

        val a = context.obtainStyledAttributes(attrs, R.styleable.setInfoStyle)

        var topName = a.getString(R.styleable.setInfoStyle_infoTopName)
        toastHintName = a.getString(R.styleable.setInfoStyle_toastHintName) ?: ""
        var inputInfoType = a.getInt(R.styleable.setInfoStyle_inputInfoType, 0)
        infoTopName?.setText(topName)
        var hintName = a.getString(R.styleable.setInfoStyle_infoHintName)
        maxValue = a.getInt(R.styleable.setInfoStyle_maxNumberValue, 11)
        infoEdit?.setHint(hintName)
        var showArrow = a.getBoolean(R.styleable.setInfoStyle_showInfoArrow, false)
        infoArrow?.visible = showArrow
        var showX = a.getBoolean(R.styleable.setInfoStyle_showX, false)
        infoX?.visiblein = showX
        var showNumber = a.getBoolean(R.styleable.setInfoStyle_showBottomNumber, false)
        var showLeftNumber = a.getBoolean(R.styleable.setInfoStyle_showLeftNumber, false)
        infoMaxLength?.visible = showNumber
        infoMaxLength?.setText("0/${maxValue}")
        set_info_left_number?.visible = showLeftNumber

        if (showLeftNumber) {
            infoEdit?.setPadding(ConvertUtils.dp2px(65f), 0, 0, ConvertUtils.dp2px(10f))
        } else {
            infoEdit?.setPadding(ConvertUtils.dp2px(15f), 0, 0, ConvertUtils.dp2px(10f))
        }
        if (showArrow) {
            infoEdit?.let {
                it.isEnabled = false
                it.isFocusable = false
                it.isFocusableInTouchMode = false
                it.isClickable = true
                it.isLongClickable = false
                it.background = null
            }
            infoPlaceTv?.show()
            infoPlaceTv?.setOnDelayClickListener {
                infoViewClick?.invoke()
            }
            infoEdit?.hide(false)
        }
        if (inputInfoType == TYPE_NAME) {
            infoEdit?.inputType = InputType.TYPE_CLASS_TEXT
        } else if (inputInfoType == TYPE_COMPANY_OR_SCHOOLD_NAME) {
            infoEdit?.inputType = InputType.TYPE_CLASS_TEXT
        } else if (inputInfoType == TYPE_UME_LENGTH) {
            infoEdit?.inputType = InputType.TYPE_CLASS_NUMBER
            infoEdit?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(5))
        } else if (inputInfoType == TYPE_BANK) {
            infoEdit?.inputType = InputType.TYPE_CLASS_NUMBER
            infoEdit?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))
        } else if (inputInfoType == TYPE_PHONE) {
            infoEdit?.inputType = InputType.TYPE_CLASS_NUMBER
            infoEdit?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))
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
                            it.setBackgroundColor(Color.parseColor("#FFF1F1")) // 错误状态
                        } else {
                            it.tag = "completed"
                            it.setBackgroundColor(Color.parseColor("#F1F5FF")) // 完成状态
                        }
                        it.clearFocus()
                        false
                    }

                    if (inputInfoType == TYPE_NAME) {
                        val newText = it.text?.trim()
                        if ((newText?.length ?: 0) < 2) {
                            it.tag = "error"
                            ToastUtils.showShort(toastHintName)
                            it.setBackgroundColor(Color.parseColor("#FFF1F1")) // 错误状态
                        } else {
                            it.tag = "completed"
                            it.setBackgroundColor(Color.parseColor("#F1F5FF")) // 完成状态
                        }
                        it.clearFocus()
                        false
                    }
                    if (inputInfoType == TYPE_COMPANY_OR_SCHOOLD_NAME) {
                        val newText = it.text?.trim()
                        if ((newText?.length ?: 0) < 2) {
                            it.tag = "error"
                            ToastUtils.showShort("Please enter a valid company name")
                            it.setBackgroundColor(Color.parseColor("#FFF1F1")) // 错误状态
                        } else {
                            it.tag = "completed"
                            it.setBackgroundColor(Color.parseColor("#F1F5FF")) // 完成状态
                        }
                        it.clearFocus()
                        false
                    }
                    if (inputInfoType == TYPE_BANK) {
                        if ((it.text?.length ?: 0) < 10) {
                            it.tag = "error"
                            ToastUtils.showShort("Bank account error. Please check")
                            it.setBackgroundColor(Color.parseColor("#FFF1F1")) // 错误状态
                        } else {
                            it.tag = "completed"
                            it.setBackgroundColor(Color.parseColor("#F1F5FF")) // 完成状态
                        }
                        it.clearFocus()
                        false
                    }
                    if (inputInfoType == TYPE_PHONE) {
                        if ((it.text?.length ?: 0) !in 10..11) {
                            ToastUtils.showShort("Please enter 10 or 11 digits")
                            it.tag = "error"
                            it.setBackgroundColor(Color.parseColor("#FFF1F1")) // 错误状态
                        } else {
                            if ((it.text?.length ?: 0) == 10 && (it.text?.get(0) ?: "") == "0") {
                                ToastUtils.showShort("The first digit cannot be 0")
                                it.tag = "error"
                                it.setBackgroundColor(Color.parseColor("#FFF1F1")) // 错误状态
                            } else {
                                it.tag = "completed"
                                it.setBackgroundColor(Color.parseColor("#F1F5FF")) // 完成状态
                            }


                        }
                        it.clearFocus()
                        false
                    }
                    if (inputInfoType == TYPE_ADDRESS) {
                        val newText = it.text?.trim()
                        if ((newText?.length ?: 0) < 2) {
                            it.tag = "error"
                            ToastUtils.showShort(toastHintName)
                            it.setBackgroundColor(Color.parseColor("#FFF1F1")) // 错误状态
                        } else {
                            it.tag = "completed"
                            it.setBackgroundColor(Color.parseColor("#F1F5FF")) // 完成状态
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
                if (!hasFocus) {
                    if (inputInfoType == TYPE_NAME) {
                        val newText = it.text?.trim()
                        if ((newText?.length ?: 0) < 2) {
                            it.tag = "error"
                            ToastUtils.showShort(toastHintName)
                            it.setBackgroundColor(Color.parseColor("#FFF1F1")) // 错误状态
                        } else {
                            it.tag = "completed"
                            it.setBackgroundColor(Color.parseColor("#F1F5FF")) // 完成状态
                        }
                        return@setOnFocusChangeListener
                    }
                    if (inputInfoType == TYPE_BANK) {
                        if ((it.text?.length ?: 0) < 10) {
                            it.tag = "error"
                            ToastUtils.showShort("Bank account error. Please check")
                            it.setBackgroundColor(Color.parseColor("#FFF1F1")) // 错误状态
                        } else {
                            it.tag = "completed"
                            it.setBackgroundColor(Color.parseColor("#F1F5FF")) // 完成状态
                        }
                        return@setOnFocusChangeListener
                    }

                    if (inputInfoType == TYPE_COMPANY_OR_SCHOOLD_NAME) {
                        val newText = it.text?.trim()
                        if ((newText?.length ?: 0) < 2) {
                            it.tag = "error"
                            ToastUtils.showShort("Please enter a valid company name")
                            it.setBackgroundColor(Color.parseColor("#FFF1F1")) // 错误状态
                        } else {
                            it.tag = "completed"
                            it.setBackgroundColor(Color.parseColor("#F1F5FF")) // 完成状态
                        }
                        return@setOnFocusChangeListener
                    }
                    if (inputInfoType == TYPE_ADDRESS) {
                        val newText = it.text?.trim()
                        if ((newText?.length ?: 0) < 2) {
                            it.tag = "error"
                            ToastUtils.showShort(toastHintName)
                            it.setBackgroundColor(Color.parseColor("#FFF1F1")) // 错误状态
                        } else {
                            it.tag = "completed"
                            it.setBackgroundColor(Color.parseColor("#F1F5FF")) // 完成状态
                        }
                        return@setOnFocusChangeListener
                    }
                    if (inputInfoType == TYPE_BVN) {
                        if ((it.text?.length ?: 0) < 11) {
                            it.tag = "error"
                            ToastUtils.showShort("The BVN format is invalid, please check")
                            it.setBackgroundColor(Color.parseColor("#FFF1F1")) // 错误状态
                        } else {
                            it.tag = "completed"
                            it.setBackgroundColor(Color.parseColor("#F1F5FF")) // 完成状态
                        }
                    }
                }

                it.setBackgroundColor(
                    when {
                        it.tag == "error" -> Color.parseColor("#FFF1F1")
                        hasFocus -> Color.parseColor("#FFFAEF")
                        it.tag == "completed" -> Color.parseColor("#F1F5FF")
                        else -> Color.parseColor("#F7F7F7")
                    }
                )
                if (inputInfoType == TYPE_EMAIL && hasFocus) {
                    scrollListener?.invoke()
                }
            }
            it.addTextChangedListener(object : TextWatcher {
                private var isFormatting = false
                private var lastValidText = ""
                private val forbiddenRegex = """[\p{P}\p{S}]""".toRegex()
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }


                override fun afterTextChanged(s: Editable?) {
                    if (inputInfoType == TYPE_UME_LENGTH) {
                        val input = s?.toString() ?: ""

                        if (input.any { !it.isDigit() }) {
                            val filtered = input.filter { it.isDigit() }.take(5) // 只保留数字，最多5位
                            it.removeTextChangedListener(this)
                            it.setText(filtered)
                            it.setSelection(filtered.length)
                            it.addTextChangedListener(this)
                            it.tag = "completed"
                            it.setBackgroundColor(
                                Color.parseColor("#F1F5FF")
                            )
                            return
                        }

                        if (input.isEmpty()) {
                            it.tag = "error"
//                            ToastUtils.showShort("Minimum input length of one digits")
                            it.setBackgroundColor(Color.parseColor("#FFF1F1")) // 错误状态
                        } else {
                            it.tag = "completed"
                            it.setBackgroundColor(
                                Color.parseColor("#F1F5FF")
                            )
                        }
                    }
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
                        infoMaxLength?.setText(s.toString().length.toString() + "/${maxValue}")

                    }
                    if (inputInfoType == TYPE_BANK) {
                        infoMaxLength?.setText(s.toString().length.toString() + "/${maxValue}")
                        if (s?.length == 10) {
                            it.tag = "completed"
                            it.setBackgroundColor(
                                Color.parseColor("#F1F5FF")
                            )
                        } else {
                            it.tag = "error"
                            it.setBackgroundColor(
                                Color.parseColor("#FFF1F1")
                            )
                        }
                    }

                    if (inputInfoType == TYPE_EMAIL) {
                        if (EmailValidator.isValid(s.toString())) {
                            it.tag = "completed"
                            it.setBackgroundColor(
                                Color.parseColor("#F1F5FF")
                            )
                        } else {
                            ToastUtils.showShort("Please enter a valid email address")
                            it.tag = "error"
                            it.setBackgroundColor(
                                Color.parseColor("#FFF1F1")
                            )
                        }
                    }
                    if (inputInfoType == TYPE_PAY_DAY) {
                        it.tag = "completed"
                        it.setBackgroundColor(
                            Color.parseColor("#F1F5FF")
                        )
                    }
                    if (inputInfoType == TYPE_ADDRESS) {
                        if (isFormatting || s.isNullOrEmpty()) return
                        // 1. 移除所有空格 + 过滤非法字符
                        isFormatting = true
                        val filtered = s.toString()
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
                    if (inputInfoType == TYPE_COMPANY_OR_SCHOOLD_NAME) {
                        //
                        if (isFormatting || s.isNullOrEmpty()) return

                        isFormatting = true

                        // 1. 移除所有空格 + 过滤非法字符
                        val filtered = s.toString()
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

                    if (inputInfoType == TYPE_NAME) {
                        //
                        if (isFormatting || s.isNullOrEmpty()) return

                        isFormatting = true

                        // 1. 移除所有空格 + 过滤非法字符
                        val filtered = s.toString()
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
                        textChangeListener?.invoke(s.toString())
                    }
                    if (inputInfoType == TYPE_PHONE) {
                        val input = s?.toString() ?: ""

                        // 实时显示错误提示（校验通过时清除错误）
//                        it.error = when {
//                            !input.matches(Regex("\\d+")) -> "Only numbers can be entered"
//                            else -> validatePhoneFormat(input) // 具体格式校验
//                        }
//                        if (!it.error.isNullOrEmpty()) {
//                            ToastUtils.showShort(it.error)
//                            it.setBackgroundColor(Color.parseColor("#FFF1F1"))
//                        } else {
//                            it.tag = "completed"
//                            it.setBackgroundColor(
//                                Color.parseColor("#F1F5FF")
//                            )
//                        }

                        if (isFormatting || s == null) {
                            return
                        }
                        isFormatting = true
                        val currentText = s.toString().replace(" ", "") // 移除所有空格进行纯数字处理
                        val length = currentText.length
                        // 规则1：如果当前文本为空，清空lastValidText并直接返回
                        if (currentText.isEmpty()) {
                            lastValidText = ""
                            isFormatting = false
                            return
                        }
                        // 规则2：检查首位，决定应用哪套规则
                        val firstChar = currentText[0]
                        // --- 10位模式 (首位为1-9) ---
                        if (firstChar in '1'..'9') {
                            // 子规则2a：检查是否所有数字都相同（低效但清晰的写法，适用于10位）
                            val allDigitsSame = currentText.length == 10 && currentText.all { it == firstChar }
                            if (allDigitsSame) {
                                // 违反了“不能全部相同”的规则，回退到上一个有效文本
                                s.replace(0, s.length, lastValidText)
                                it.error = "The number cannot have all the same digits"
                                isFormatting = false
                                it.setBackgroundColor(Color.parseColor("#FFF1F1"))
                                return
                            }
                            // 子规则2b：长度不能超过10
                            if (length > 10) {
                                s.replace(0, s.length, lastValidText)
                                it.error = "enter a maximum of 10 digits."
                                isFormatting = false
                                return
                            }


                            val formattedText = currentText
                            // 清除错误提示（如果之前有）
                            it.error = null
                            lastValidText = formattedText

                        } else if (firstChar == '0') {
                            // 子规则3a：检查第二位不能是0
                            if (length >= 2 && currentText[1] == '0') {
                                s.replace(0, s.length, lastValidText)
                                it.error = "The second digit cannot be 0"
                                it.setBackgroundColor(Color.parseColor("#FFF1F1"))
                                isFormatting = false
                                return
                            }
                            val formattedText = currentText
                            if (s.toString() != formattedText) {
                                s.replace(0, s.length, formattedText)
                                it.setSelection(s.length)
                            }
                            it.error = null
                            lastValidText = formattedText

                        }
                        // --- 无效模式 (首位既不是1-9也不是0，理论上被InputFilter阻止，此为安全备份) ---
                        else {
                            s.clear()
                            it.error = "The first digit must be 1-9 or 0"
                            it.setBackgroundColor(Color.parseColor("#FFF1F1"))
                            lastValidText = ""
                        }
                        isFormatting = false
                        textChangeListener?.invoke(s.toString())

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

    private fun validatePhoneFormat(number: String): String? {
        return when (number.length) {
            10 -> {
                when {
//                    number[0] == '0' -> "The first digit cannot be 0"
                    number.toSet().size == 1 -> "Cannot all be the same number" // 如1111111111
                    else -> null // 校验通过
                }
            }

            11 -> {
                when {
                    number[0] != '0' -> "The first digit must be 0"
                    number[1] == '0' -> "The second digit cannot be 0"
                    else -> null // 校验通过
                }
            }

            else -> null
        }
    }

    private val decimalFormat = DecimalFormat("#,###")

    var completeListener: (() -> Unit)? = null
    var scrollListener: (() -> Unit)? = null
    var textChangeListener: ((String) -> Unit)? = null

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
        infoPlaceTv?.setText(text)
//        infoPlaceTv?.isSelected = true
    }

    fun setTopName(topName: String) {
        infoTopName?.setText(topName)
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

    fun clearText() {
        infoEdit?.setText("")
        infoPlaceTv?.setText("")
        infoEdit?.setBackgroundColor(Color.parseColor("#f7f7f7"))
    }

    fun getSmallEditText(): String {
        return infoEdit?.text.toString().lowercase()

    }

    var preEmail = ""
    fun appendText(text: String) {
        val originText = infoEdit?.text?.toString() ?: ""
        val atIndex = originText.indexOf("@")
        if (atIndex > -1) {
            val newStr = originText.substring(0, atIndex)
            infoEdit?.setText(newStr)
            infoEdit?.append(text)
        } else {
            infoEdit?.append(text)
        }
        infoEdit?.text?.length?.let { infoEdit?.setSelection(it) }
        scrollListener?.invoke()
//        if (!preEmail.isNullOrEmpty()) {
//            if (infoEdit?.text?.contains(preEmail) == true) {
//                val newStr = infoEdit?.text?.toString()?.replace(preEmail, text)
//                infoEdit?.setText(newStr)
//            } else {
//                infoEdit?.append(text)
//                preEmail = text
//            }
//        } else {
//            infoEdit?.append(text)
//            preEmail = text
//        }

    }

    var toastHintName = ""
    fun warSetX(show: Boolean) {
        infoX?.visiblein = show
    }

    fun setTagComplete() {
        infoEdit?.tag = "completed"
        infoEdit?.setBackgroundColor(Color.parseColor("#F1F5FF")) // 完成状态
    }

    fun getRawNumericValue(): String {
        return infoEdit?.text.toString().replace(Regex("[^\\d]"), "")
    }

    fun getEditTextView(): ZipShapeEditTextView? {
        return infoEdit
    }


    fun getRealPhone(): String {
        val tv = infoEdit?.text.toString()
        val formatTv = formatPhoneNumber(tv)
//        return "234$formatTv"
        return "$formatTv"
    }

    fun formatPhoneNumber(input: String): String {
        // 1. 移除所有非数字字符（如空格、-等）
        val cleaned = input.replace(Regex("[^0-9]"), "")

        // 2. 如果是11位且以0开头，去掉首个0
        return if (cleaned.length == 11 && cleaned.startsWith("0")) {
            cleaned.substring(1)
        } else {
            cleaned // 其他情况原样返回（可根据需求调整）
        }
    }

    fun isTextNotEmpty(): Boolean {
        return !infoEdit?.text.isNullOrEmpty()
    }


}