package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer
    lateinit var startButton:Button
    lateinit var countdown30:Button
    lateinit var countdown60:Button
    lateinit var countdown90:Button
    lateinit var countdown120:Button
    lateinit var coutdownDisplay:TextView

    var timeToCountDownInMs = 5000L
    val timeTicks = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       startButton = findViewById<Button>(R.id.startCountdownButton)
        countdown30 = findViewById<Button>(R.id.countDown_30)
        countdown60 = findViewById<Button>(R.id.countDown_60)
        countdown90 = findViewById<Button>(R.id.countDown_90)
        countdown120 = findViewById<Button>(R.id.countDown_120)

        countdown30.setOnClickListener(){
            timeToCountDownInMs = 1800000L
        }
        countdown60.setOnClickListener(){
            timeToCountDownInMs = 3600000L
        }
        countdown90.setOnClickListener(){
            timeToCountDownInMs = 6400000L
        }
        countdown120.setOnClickListener(){
            timeToCountDownInMs = 7200000L
        }
        startButton.setOnClickListener(){
            if (coutdownDisplay.text == millisecondsToDescriptiveTime(0)){
                startCountDown(it)
            }
       }
       coutdownDisplay = findViewById<TextView>(R.id.countDownView)

    }

    fun startCountDown(v: View){
        timer = object : CountDownTimer(timeToCountDownInMs,timeTicks) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity,"Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
            }
            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
            }
        }

        timer.start()

    }

    fun updateCountDownDisplay(timeInMs:Long){
        coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

}