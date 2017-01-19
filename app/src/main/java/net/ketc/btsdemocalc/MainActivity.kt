package net.ketc.btsdemocalc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.TableRow
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var leftNumber: Double = 0.0
    private var displayNumber: Double = 0.0
    private var currentOperator: Operator? = null
    private var pressDot = false
    private var pressOpr = false
    private var pressEqual = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
    }

    private fun initialize() {
        val numButtons = arrayListOf(num0_button, num1_button, num2_button, num3_button,
                num4_button, num5_button, num6_button,
                num7_button, num8_button, num9_button)
        numButtons.forEachIndexed { i, button ->
            button.setOnClickListener { onClickNum(i) }
        }
        add_button.setOnClickListener { onClickOperate(Operator.Add()) }
        sub_button.setOnClickListener { onClickOperate(Operator.Sub()) }
        multi_button.setOnClickListener { onClickOperate(Operator.Multi()) }
        div_button.setOnClickListener { onClickOperate(Operator.Div()) }
        equal_button.setOnClickListener {
            currentOperator?.run {
                val result = run(leftNumber, displayNumber)
                currentOperator = null
                setResultText(result)
                leftNumber = result
                pressDot = false
                pressOpr = false
                pressEqual = true
            }
        }
        ac_button.setOnClickListener { clear() }
        sqrt_button.setOnClickListener { onClickFunc(Func.Sqrt()) }
        tax_button.setOnClickListener { onClickFunc(Func.Tax()) }
        del_button.setOnClickListener {
            val text = result_text.text.toString()
            if (text == "0")
                return@setOnClickListener
            val num = if (text.length > 1) {
                text.subSequence(0, text.length - 1).toString().toDouble()
            } else {
                0.0
            }
            setResultText(num)
        }
        dot_button.setOnClickListener {
            val number = result_text.text.toString().replace(".", "")
            result_text.text = StringBuilder(number).append(".").toString()
            pressDot = true
        }

        enableRippleEffect()

    }

    private fun enableRippleEffect() {
        val outValue = TypedValue()
        theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true)
        (0..(button_table.childCount - 1))
                .map { (button_table.getChildAt(it) as TableRow) }
                .flatMap { row -> (0..(row.childCount - 1)).map { row.getChildAt(it) } }
                .forEach {
                    it.setBackgroundResource(outValue.resourceId)
                }
    }


    private fun clear() {
        leftNumber = 0.0
        currentOperator = null
        pressOpr = false
        pressDot = false
        setResultText(0.0)
    }

    private fun onClickNum(num: Int) {
        val builder = StringBuilder(if (displayNumber == 0.0 || pressEqual) "" else convertToString(displayNumber))
        pressEqual = false
        if (pressDot) {
            pressDot = false
            builder.append(".")
        }
        pressOpr = false
        val numStr = builder.append(num).toString()
        setResultText(numStr.toDouble())
    }

    private fun onClickOperate(operator: Operator) {
        if (!pressOpr) {
            pressOpr = true
            pressEqual = false
            if (currentOperator != null) {
                val result = currentOperator!!.run(leftNumber, displayNumber)
                result_text.text = convertToString(result)
                leftNumber = result
            } else {
                leftNumber = displayNumber
            }
            displayNumber = 0.0
        }
        currentOperator = operator
    }

    private fun onClickFunc(func: Func) {
        if (!pressOpr)
            setResultText(func.run(displayNumber))
        pressEqual = false
    }

    private fun convertToString(x: Double): String {
        var resultStr = x.toString()
        val splitResult = resultStr.split(".")
        if (splitResult[splitResult.size - 1].matches("0*".toRegex())) {
            resultStr = splitResult[0]
        }
        return resultStr
    }

    private fun setResultText(result: Double) {
        result_text.text = convertToString(result)
        displayNumber = result
    }
}
