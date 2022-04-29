package com.prud.customviews

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.prud.customviews.databinding.PartButtonsBinding

enum class BottomButtonAction {
    POSITIVE, NEGATIVE
}

typealias OnBottomButtonsActionListener = (BottomButtonAction) -> Unit

class BottomButtonsView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    desStyleRes: Int,
) : ConstraintLayout(context, attrs, defStyleAttr, desStyleRes) {

    private val binding: PartButtonsBinding
    private var listener: OnBottomButtonsActionListener? = null

    var isProgressMode: Boolean = false
        get() = field
        set(value) {
            field = value
            if (value) {
                binding.positiveBtn.visibility = INVISIBLE
                binding.negativeBtn.visibility = INVISIBLE
                binding.progress.visibility = VISIBLE
            } else {
                binding.positiveBtn.visibility = VISIBLE
                binding.negativeBtn.visibility = VISIBLE
                binding.progress.visibility = GONE
            }
        }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.part_buttons, this, true)
        binding = PartButtonsBinding.bind(this)
        initializeAttributes(attrs, defStyleAttr, desStyleRes)
        initListener()
    }

    private fun initializeAttributes(
        attrs: AttributeSet?,
        defStyleAttr: Int,
        desStyleRes: Int,
    ) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.BottomButtonsView,
            defStyleAttr,
            defStyleAttr
        )

        with(binding) {
            val positiveButtonText =
                typedArray.getString(R.styleable.BottomButtonsView_bottomPositiveButtonText)
            setPositiveButtonText(positiveButtonText ?: "Ok")

            val negativeButtonText =
                typedArray.getString(R.styleable.BottomButtonsView_bottomNegativeButtonText)
            setNegativeButtonText(negativeButtonText ?: "Cancel")

            val positiveButtonColor = typedArray.getInt(
                R.styleable.BottomButtonsView_bottomPositiveBackgroundButton,
                Color.BLACK
            )
            positiveBtn.backgroundTintList = ColorStateList.valueOf(positiveButtonColor)

            val negativeButtonColor = typedArray.getInt(
                R.styleable.BottomButtonsView_bottomNegativeBackgroundButton,
                Color.WHITE
            )
            negativeBtn.backgroundTintList = ColorStateList.valueOf(negativeButtonColor)

            this@BottomButtonsView.isProgressMode =
                typedArray.getBoolean(R.styleable.BottomButtonsView_bottomProgressMode, false)
        }

        typedArray.recycle()
    }

    private fun initListener() {
        binding.positiveBtn.setOnClickListener {
            this.listener?.invoke(BottomButtonAction.POSITIVE)
        }
        binding.negativeBtn.setOnClickListener {
            this.listener?.invoke(BottomButtonAction.NEGATIVE)
        }
    }

    fun setListener(listener: OnBottomButtonsActionListener?) {
        this.listener = listener
    }

    fun setPositiveButtonText(text: String?) {
        binding.positiveBtn.text = text
    }

    fun setNegativeButtonText(text: String?) {
        binding.negativeBtn.text = text
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()!!
        val savedState = SavedState(superState)
        savedState.positiveButtonText = binding.positiveBtn.text.toString()
        savedState.negativeButtonText = binding.negativeBtn.text.toString()
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        binding.positiveBtn.text = savedState.positiveButtonText
        binding.negativeBtn.text = savedState.negativeButtonText
    }

    class SavedState : BaseSavedState {

        var positiveButtonText: String? = null
        var negativeButtonText: String? = null

        constructor(superState: Parcelable) : super(superState)
        constructor(parcel: Parcel) : super(parcel) {
            positiveButtonText = parcel.readString()
            negativeButtonText = parcel.readString()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(positiveButtonText)
            out.writeString(negativeButtonText)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return Array(size) { null }
                }
            }
        }
    }
}