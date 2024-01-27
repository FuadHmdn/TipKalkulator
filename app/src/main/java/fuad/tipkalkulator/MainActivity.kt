package fuad.tipkalkulator

import android.animation.ArgbEvaluator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import fuad.tipkalkulator.ui.theme.TipKalkulatorTheme

private const val TAG = "FUAD"
private const val DEFAULT_PERSEN = 15

class MainActivity : ComponentActivity() {

    lateinit var hargaEditText: EditText
    lateinit var persenSeekBar: SeekBar
    lateinit var persenTextView: TextView
    lateinit var totalTipTextView: TextView
    lateinit var totalHargaKeseluruhanTextView: TextView
    lateinit var kepuasanTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        initComponent()

        initListener()
    }

    fun initComponent(){
        hargaEditText = findViewById(R.id.hargaEditText)
        persenSeekBar = findViewById(R.id.persenSeekBar)
        persenTextView = findViewById(R.id.persenTextView)
        totalTipTextView = findViewById(R.id.totalTipTextView)
        totalHargaKeseluruhanTextView = findViewById(R.id.totalHargaKeseluruhanTexView)
        kepuasanTextView = findViewById(R.id.kepuasanTextView)
    }

    fun initListener(){
        persenTextView.text = "$DEFAULT_PERSEN%"
        persenSeekBar.progress = DEFAULT_PERSEN
        kepuasan(DEFAULT_PERSEN)


        persenSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                persenTextView.setText("$progress%")
                kepuasan(progress)
                hitungTotal()
                Log.i(TAG, "Progres $progress")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        hargaEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "HARGA $s")
                hitungTotal()
            }

        })
    }

    fun hitungTotal(){

        if(hargaEditText.text.isEmpty()) {
            totalTipTextView.text = ""
            totalHargaKeseluruhanTextView.text = ""
            return
        }

        val harga = hargaEditText.text.toString().toDouble()
        val tip = persenSeekBar.progress

        val totalTip = harga * tip / 100
        val totalHarga = harga + totalTip

        totalTipTextView.text = "%.2f".format(totalTip)
        totalHargaKeseluruhanTextView.text = "%.2f".format(totalHarga)
    }

    fun kepuasan(proses: Int){
        when(proses){
            in 0..5 -> kepuasanTextView.text = "BURUK"
            in 6..15 -> kepuasanTextView.text = "BAIK"
            in 16..24 -> kepuasanTextView.text = "SANGAT BAIK"
            in 24..30 -> kepuasanTextView.text = "PERFECT"
        }

        val color = ArgbEvaluator().evaluate(
            proses.toFloat() / persenSeekBar.max,
            ContextCompat.getColor(this, R.color.worst),
            ContextCompat.getColor(this, R.color.best)
        )as Int

        kepuasanTextView.setTextColor(color)
        totalTipTextView.setTextColor(color)
    }
}