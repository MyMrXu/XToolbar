package example.site.xzwzz.xtoolbar

import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var count = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        xtoolbar.getHelper()
            .title("标题 标题  标题标题 标题  标题标题 标题  标题标题 标题  标题", marquee = true)
//            .openNaviButton {
//                finish()
//            }
            .subTitle("小标题")
            .titleOnClickListener {
                Toast.makeText(this@MainActivity, "吐丝", Toast.LENGTH_SHORT).show()
            }
            .commit()
        xtoolbar.getHelper().addToolbarButton(
            Gravity.RIGHT,
            resourceDrawable = R.drawable.ic_baseline_collections_24
        ) {

        }
        btn_change.setOnClickListener {
            xtoolbar.getHelper().title("标题${++count}").commit()
        }
    }
}