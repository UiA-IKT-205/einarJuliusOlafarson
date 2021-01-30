package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity() {

    lateinit var timer: CountDownTimer
    lateinit var startButton: Button
    lateinit var coutdownDisplay: TextView
    lateinit var timeSlider: SeekBar
    lateinit var pauseSlider: SeekBar
    lateinit var countDown: TextView
    lateinit var pausetimerText: TextView
    lateinit var repeatAmountText: EditText

    // sets the default to 15 minutes for the countdown and pause timer
    var minimumTimer = 15*60000L
    var timeToCountDownInMs = minimumTimer
    var pauseTimer = minimumTimer
    val timeTicks = 1000L

    // default repetition amount
    var repAmount: Int = 0
    // Values to check if either timer is on
    var isTimerOn: Boolean = false
    var isPauseOn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Update our variables to be the elements on the screen
        startButton = findViewById<Button>(R.id.startCountdownButton)
        countDown = findViewById<TextView>(R.id.countdownText)
        pausetimerText = findViewById<TextView>(R.id.pausetimerText)
        timeSlider = findViewById<SeekBar>(R.id.timeSlider)
        pauseSlider = findViewById<SeekBar>(R.id.pauseSlider)
        repeatAmountText = findViewById<EditText>(R.id.repNum)


        timeSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, i: Int, b: Boolean) {
                // Make it so if any changes are made to the timer variable we
                // stop all timers from continuing and reset to a neutral state
                if (isTimerOn or isPauseOn) {
                    isPauseOn = false
                    isTimerOn = false
                    timer.cancel()
                }
                // Display what the first timer will count down from
                timeToCountDownInMs = i * minimumTimer
                countDown.setText(millisecondsToDescriptiveTime(timeToCountDownInMs))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Do something
            }
        })

        pauseSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, i: Int, b: Boolean) {
                // Make it so if any changes are made to the timer variable we
                // stop all timers from continuing and reset to a neutral state
                if (isTimerOn or isPauseOn) {
                    isPauseOn = false
                    isTimerOn = false
                    timer.cancel()
                }
                // Display what the pause timer will count down from
                pauseTimer = i * minimumTimer
                pausetimerText.setText(millisecondsToDescriptiveTime(pauseTimer))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Do something
            }

        })

        startButton.setOnClickListener {
            // if the repeat amount edit text is empty we default to a 0 repeat value
            if (repeatAmountText.text.toString().isEmpty()){
                repAmount = 0
            } else {
                // Otherwise we get the value in the edit text
                repAmount = repeatAmountText.text.toString().toInt()
            }
            startCountDown(it)
        }
        coutdownDisplay = findViewById<TextView>(R.id.countDownView)
    }

    fun startCountDown(v: View) {

        // If we try and start a timer we check if either timer is already on
        // If either the normal countdown or pause timer are on we restart first
        // and then the user can start their timer

        if (isTimerOn or isPauseOn) {
            timer.cancel()
            isTimerOn = false
            isPauseOn = false

        } else {
            // If no timers are on we set the countdown variable to true
            isTimerOn = !isTimerOn

            timer = object : CountDownTimer(timeToCountDownInMs, timeTicks) {
                override fun onFinish() {
                    // when it is finished we set our countdown variable to false
                    isTimerOn = false
                    Toast.makeText(this@MainActivity, "Arbeidsøkt er ferdig", Toast.LENGTH_SHORT)
                        .show()
                    if (!isPauseOn) {
                        // we then start the pause timer
                        Toast.makeText(this@MainActivity, "Time for a break", Toast.LENGTH_SHORT).show()
                        startPauseTimer(v)

                    }
                }

                override fun onTick(millisUntilFinished: Long) {
                    updateCountDownDisplay(millisUntilFinished)
                }
            }
            timer.start()
        }

    }

    fun startPauseTimer(v: View) {
        // We now flip isPauseOn to true, as the pause timer only starts after a work we don't have to worry
        // about conflicts with pressing start again

        isPauseOn = !isPauseOn
        timer = object : CountDownTimer(pauseTimer, timeTicks) {
            override fun onFinish() {
                // If our repetition amounts are higher than 1 we start the repetition cycle
                // We decrement our repAmount by 1 and start the timer all over again
                // Once the repAmount is set to 0 it no longer repeats
                if (repAmount > 0){
                    Toast.makeText(this@MainActivity, "Time to start working again", Toast.LENGTH_SHORT).show()
                    isPauseOn = false
                    repAmount--
                    repeatAmountText.setText(repAmount.toString())
                    startCountDown(v)
                } else {
                    isPauseOn = false
                    Toast.makeText(this@MainActivity, "Timer is done", Toast.LENGTH_SHORT).show()

                }
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