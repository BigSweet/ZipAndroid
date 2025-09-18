package com.zip.zipandroid.view

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.zip.zipandroid.R
import com.zip.zipandroid.ktx.hide
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.ktx.show
import com.zip.zipandroid.ktx.visible
import com.zip.zipandroid.ktx.visiblein
import com.zip.zipandroid.shape.ZipShapeConstraintLayout
import com.zip.zipandroid.shape.ZipShapeEditTextView
import com.zip.zipandroid.shape.ZipShapeTextView
import com.zip.zipandroid.utils.EmailValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SetEmailInfoEditView : RelativeLayout {
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
    private var set_email_place_bg: ZipShapeConstraintLayout? = null
    private var infoPlaceTv: ZipShapeTextView? = null
    private var infoX: TextView? = null
    private var infoTopName: TextView? = null
    private var infoMaxLength: TextView? = null
    private var set_info_left_number: TextView? = null

    companion object {
        const val TYPE_EMAIL = 3
    }

    var maxValue = 11
    var infoViewClick: (() -> Unit)? = null
    fun init(context: Context?, attrs: AttributeSet?) {
        context ?: return
        var view = LayoutInflater.from(context).inflate(R.layout.view_set_email_info_edit, this, true)
        infoEdit = view.findViewById(R.id.set_info_edit)
        set_email_place_bg = view.findViewById(R.id.set_email_place_bg)
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
        var inputInfoType = a.getInt(R.styleable.setInfoStyle_inputInfoType, 0)
        infoTopName?.setText(topName)
        var hintName = a.getString(R.styleable.setInfoStyle_infoHintName)
        maxValue = a.getInt(R.styleable.setInfoStyle_maxNumberValue, 11)
        infoEdit?.setHint(hintName)
        var showArrow = a.getBoolean(R.styleable.setInfoStyle_showInfoArrow, false)
        infoArrow?.visible = showArrow
        var showX = a.getBoolean(R.styleable.setInfoStyle_showX, false)
        infoX?.visiblein = showX
        infoMaxLength?.visible = false
        infoMaxLength?.setText("0/${maxValue}")
        set_info_left_number?.visible = false

        infoEdit?.inputType = InputType.TYPE_CLASS_TEXT
        infoEdit?.let {
            it.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (inputInfoType == TYPE_EMAIL) {
                        if (EmailValidator.isValid(it.text.toString())) {
                            it.tag = "completed"
                            set_email_place_bg?.setBackground2(
                                Color.parseColor("#F1F5FF")
                            )
                        } else {
                            ToastUtils.showShort("Please enter a valid email address")
                            it.tag = "error"
                            set_email_place_bg?.setBackground2(
                                Color.parseColor("#FFF1F1")
                            )
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
            set_email_place_bg?.setBackground2(Color.parseColor("#F7F7F7"))

            it.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    if (inputInfoType == TYPE_EMAIL) {
                        if (EmailValidator.isValid(it.text.toString())) {
                            it.tag = "completed"
                            set_email_place_bg?.setBackground2(
                                Color.parseColor("#F1F5FF")
                            )
                        } else {
                            ToastUtils.showShort("Please enter a valid email address")
                            it.tag = "error"
                            set_email_place_bg?.setBackground2(
                                Color.parseColor("#FFF1F1")
                            )
                        }
                        return@setOnFocusChangeListener
                    }
                }
                if (inputInfoType == SetInfoEditView.TYPE_EMAIL && hasFocus) {
                    scrollListener?.invoke()
                }



                set_email_place_bg?.setBackground2(
                    when {
                        infoEdit?.tag == "error" -> Color.parseColor("#FFF1F1")
                        hasFocus -> Color.parseColor("#FFFAEF")
                        infoEdit?.tag == "completed" -> Color.parseColor("#F1F5FF")
                        else -> Color.parseColor("#F7F7F7")
                    }
                )

            }
            it.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }


                override fun afterTextChanged(s: Editable?) {
                    if (inputInfoType == TYPE_EMAIL) {
                        if (EmailValidator.isValid(s.toString())) {
                            it.tag = "completed"
                            set_email_place_bg?.setBackground2(
                                Color.parseColor("#F1F5FF")
                            )
                        } else {
                            if (it.tag != "error") {
//                                ToastUtils.showShort("Please enter a valid email address")
                                it.tag = "error"
                                set_email_place_bg?.setBackground2(
                                    Color.parseColor("#FFF1F1")
                                )
                            }


                        }
                        textChangeListener?.invoke(s.toString())
                    }
                    debounceAndCheckCompletion()
                }
            })
        }


        a.recycle()
    }

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
        infoPlaceTv?.isSelected = true
    }


    fun getEditIsComplete(): Boolean {
        return infoEdit?.tag == "completed" && !infoEdit?.text.isNullOrEmpty()
    }


    fun getSmallEditText(): String {
        return infoEdit?.text.toString().lowercase()

    }

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
//        scrollListener?.invoke()

    }


}