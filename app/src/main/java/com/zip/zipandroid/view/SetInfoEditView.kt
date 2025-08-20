package com.zip.zipandroid.view

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.zip.zipandroid.R
import com.zip.zipandroid.ktx.visible
import com.zip.zipandroid.ktx.visiblein
import com.zip.zipandroid.shape.ShapeEditTextView
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
    }

    fun init(context: Context?, attrs: AttributeSet?) {
        context ?: return
        var view = LayoutInflater.from(context).inflate(R.layout.view_set_info_edit, this, true)
        infoEdit = view.findViewById(R.id.set_info_edit)
        infoX = view.findViewById(R.id.set_info_xx)
        infoArrow = view.findViewById(R.id.set_info_arrow)
        infoTopName = view.findViewById(R.id.set_info_top_name)
        infoMaxLength = view.findViewById(R.id.set_info_max_length)


        val a = context.obtainStyledAttributes(attrs, R.styleable.setInfoStyle)

        var topName = a.getString(R.styleable.setInfoStyle_infoTopName)
        var inputInfoType = a.getInt(R.styleable.setInfoStyle_inputInfoType, 1)
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
                it.isClickable = false
                it.isLongClickable = false
                it.keyListener = null
                it.background = null
                it.setOnTouchListener { _, _ -> true }
                it.setOnKeyListener { _, _, _ -> true }
            }

        }
        if (inputInfoType == TYPE_NAME) {
            infoEdit?.inputType = InputType.TYPE_CLASS_TEXT
        } else if (inputInfoType == TYPE_BVN) {
            infoEdit?.inputType = InputType.TYPE_CLASS_NUMBER
//            infoEdit?.maxEms = 11
            infoEdit?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))
        } else {
            infoEdit?.inputType = InputType.TYPE_CLASS_TEXT
        }

        infoEdit?.let {

            it.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
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
                    }

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
                            it.setBackgroundColor(
                                Color.parseColor("#F1F5FF")
                            )
                        }
                        infoMaxLength?.setText(s.toString().length.toString() + "/11")

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
                        it.setBackgroundColor(
                            Color.parseColor("#F1F5FF")
                        )
                        isFormatting = false
                    }
                }
            })
        }


        a.recycle()
    }

    fun showBoard() {
        infoEdit?.let { KeyboardUtils.showSoftInput(it) }
    }


}