package com.common.commonpop

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.common.cmnpop.XPopup

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_confirm_pop).setOnClickListener {
            XPopup.Builder(this)
                .setConfirmColor(ContextCompat.getColor(this, R.color.color_0C95FF))
                .setCancelColor(ContextCompat.getColor(this, R.color.color_0C95FF))
                .asConfirm(null, "亲～您要退出吗？", "否", "是", {

                }, {

                }, false).show()
        }
        findViewById<TextView>(R.id.tv_item_pop).setOnClickListener {
            XPopup.Builder(this)
                .setItemColor(ContextCompat.getColor(this, R.color.color_0C95FF))
                .asBottomList(
                    null, arrayOf("呼叫 ".plus("13651014919")),
                    null
                ) { _, _ ->
                    val intent = Intent(Intent.ACTION_DIAL)
                    val data = Uri.parse("tel:13651014919")
                    intent.data = data
                    startActivity(intent)
                }.show()
        }
    }
}