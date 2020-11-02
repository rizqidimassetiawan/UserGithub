package com.setiawan.mysubmission.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.setiawan.mysubmission.R
import com.setiawan.mysubmission.broadcast.AlarmRecever
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        const val PREFS_NAME = "setting_preference"
        private const val DAILY = "daily"
    }

    private lateinit var alarmResever : AlarmRecever
    private lateinit var mSherdPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        alarmResever = AlarmRecever()
        mSherdPreferences = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.setting)

        btn_language.setOnClickListener(this)

        reminder.isChecked = mSherdPreferences.getBoolean(DAILY,false)
        reminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                alarmResever.setDailyAlarm(applicationContext,AlarmRecever.TYPE_REPATING,getString(R.string.reminder_title))
            }else{
                alarmResever.canceld(applicationContext,AlarmRecever.TYPE_REPATING)
            }
            setChange(isChecked)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_language -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
        }
    }

    private fun setChange(value : Boolean){
        val editor = mSherdPreferences.edit()
        editor.putBoolean(DAILY,value)
        editor.apply()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.item_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_setting -> {
                val move = Intent(this,SettingsActivity::class.java)
                startActivity(move)
                true
            }
            R.id.menu_fav -> {
                val move = Intent(this,FavoriteActivity::class.java)
                startActivity(move)
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> true
        }
    }
}