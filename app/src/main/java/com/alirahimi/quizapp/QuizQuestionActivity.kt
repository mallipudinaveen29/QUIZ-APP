package com.alirahimi.quizapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.AdaptiveIconDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.alirahimi.quizapp.model.Question
import com.alirahimi.quizapp.utils.Constants

class QuizQuestionActivity : AppCompatActivity(), View.OnClickListener {

    private var progressBar: ProgressBar? = null
    private var tvProgress: TextView? = null
    private var tvQuestionTitle: TextView? = null
    private var ivQuestionImage: ImageView? = null
    private var tvOption1: TextView? = null
    private var tvOption2: TextView? = null
    private var tvOption3: TextView? = null
    private var tvOption4: TextView? = null
    private var btnSubmit: Button? = null

    private var currentPosition = 1
    private var mQuestionList: ArrayList<Question>? = null
    private var mSelectedOption: Int = 0
    private var mUserName: String? = null
    private var mCorrectAnswers: Int = 0


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)

        mQuestionList = Constants.getQuestions()

        progressBar = findViewById(R.id.progressBar)
        tvProgress = findViewById(R.id.tvProgress)
        tvQuestionTitle = findViewById(R.id.tvQuestionTitle)
        progressBar = findViewById(R.id.progressBar)
        ivQuestionImage = findViewById(R.id.ivFlag)
        tvOption1 = findViewById(R.id.tvOption1)
        tvOption2 = findViewById(R.id.tvOption2)
        tvOption3 = findViewById(R.id.tvOption3)
        tvOption4 = findViewById(R.id.tvOption4)
        btnSubmit = findViewById(R.id.btnSubmit)
        mUserName = intent.getStringExtra(Constants.USER_NAME)

        tvOption1?.setOnClickListener(this)
        tvOption2?.setOnClickListener(this)
        tvOption3?.setOnClickListener(this)
        tvOption4?.setOnClickListener(this)
        btnSubmit?.setOnClickListener(this)

        setQuestion()
        defaultOptionsView()
    }


    @SuppressLint("SetTextI18n")
    private fun setQuestion() {
        defaultOptionsView()

        val question = mQuestionList?.get(currentPosition - 1)
        progressBar?.progress = currentPosition
        tvProgress?.text = "$currentPosition / ${progressBar?.max}"
        tvQuestionTitle?.text = question?.title
        question?.image?.let { ivQuestionImage?.setImageResource(it) }
        tvOption1?.text = question?.option1
        tvOption2?.text = question?.option2
        tvOption3?.text = question?.option3
        tvOption4?.text = question?.option4

        if (currentPosition == mQuestionList!!.size) {
            btnSubmit?.text = "Finish"
        } else {
            btnSubmit?.text = "Submit"
        }
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        tvOption1?.let {
            options.add(0, it)
        }

        tvOption2?.let {
            options.add(1, it)
        }

        tvOption3?.let {
            options.add(2, it)
        }

        tvOption4?.let {
            options.add(3, it)
        }

        options.forEach {
            it.setTextColor(Color.parseColor("#7a8089"))
            it.typeface = Typeface.DEFAULT
            it.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNumber: Int) {
        defaultOptionsView()
        mSelectedOption = selectedOptionNumber
        tv.setTextColor(Color.parseColor("#363a43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvOption1 -> {
                tvOption1?.let {
                    selectedOptionView(it, 1)
                }
            }

            R.id.tvOption2 -> {
                tvOption2?.let {
                    selectedOptionView(it, 2)
                }
            }

            R.id.tvOption3 -> {
                tvOption3?.let {
                    selectedOptionView(it, 3)
                }
            }

            R.id.tvOption4 -> {
                tvOption4?.let {
                    selectedOptionView(it, 4)
                }
            }

            R.id.btnSubmit -> {
                if (mSelectedOption == 0) {
                    currentPosition++

                    when {
                        currentPosition <= mQuestionList!!.size -> {
                            setQuestion()
                        }

                        else -> {
                            Intent(this, ResultActivity::class.java).also {
                                it.putExtra(Constants.USER_NAME, mUserName)
                                it.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                                it.putExtra(Constants.TOTAL_QUESTIONS, mQuestionList?.size)
                                startActivity(it)
                                finish()
                            }
                        }
                    }
                } else {
                    val question = mQuestionList?.get(currentPosition - 1)

                    if (question!!.correctAnswer != mSelectedOption) {
                        answerView(mSelectedOption, R.drawable.wrong_option_border_bg)
                    } else {
                        mCorrectAnswers++
                    }
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    if (currentPosition == mQuestionList!!.size) {
                        btnSubmit?.text = "Finish"
                    } else {
                        btnSubmit?.text = "Go to next Question"
                    }
                    mSelectedOption = 0

                }
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                tvOption1?.background = ContextCompat.getDrawable(this, drawableView)
            }

            2 -> {
                tvOption2?.background = ContextCompat.getDrawable(this, drawableView)
            }

            3 -> {
                tvOption3?.background = ContextCompat.getDrawable(this, drawableView)
            }

            4 -> {
                tvOption4?.background = ContextCompat.getDrawable(this, drawableView)
            }
        }
    }
}