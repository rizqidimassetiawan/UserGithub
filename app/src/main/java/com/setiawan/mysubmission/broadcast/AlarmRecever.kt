package com.setiawan.mysubmission.broadcast

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.setiawan.mysubmission.R
import com.setiawan.mysubmission.ui.MainActivity
import java.util.*

class AlarmRecever : BroadcastReceiver() {

    companion object{
        const val TYPE_REPATING = "repeat"
        const val EXTRA_MESSAGE = "extra_message"
        const val EXTRA_TYPE = "extra_type"
        private const val ID_REPEATING = 1234
    }

    override fun onReceive(context: Context, intent: Intent) {

        val type = intent.getStringExtra(EXTRA_TYPE)
        val notificationId =
            if (type.equals(TYPE_REPATING,ignoreCase = true)) ID_REPEATING
            else return
        showAlarmNotification(context,notificationId)

    }

    private fun showAlarmNotification(context: Context,notificationId : Int){
        val channelId = "Chanel01"
        val channelName = "Reminder"
        val title = context.resources.getString(R.string.message_title)
        val message = context.resources.getString(R.string.message)
        val intent = Intent(context,MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context,0,intent,0)
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val mBuilder = NotificationCompat.Builder(context,channelId)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentText(message)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVibrate(longArrayOf(1000,1000,1000,1000,1000))
            .setSound(ringtone)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000,1000,1000,1000,1000)
            mBuilder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notificationBuilder = mBuilder.build()
        notificationManagerCompat.notify(notificationId,notificationBuilder)
    }

    fun setDailyAlarm(context: Context,type:String,message : String){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context,AlarmRecever::class.java).apply {
            putExtra(EXTRA_MESSAGE,message)
            putExtra(EXTRA_TYPE,type)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING,intent,0)
        val calender = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY,9)
            set(Calendar.MINUTE,0)
            set(Calendar.SECOND,0)
        }
        val different = calender.timeInMillis - System.currentTimeMillis()
        if (different > 0){
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calender.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }else{
           val time = System.currentTimeMillis() + AlarmManager.INTERVAL_DAY - different
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                time,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
        Toast.makeText(context, "Daily Reminder Activated", Toast.LENGTH_SHORT).show()
    }

     fun canceld(context: Context,type: String){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context,AlarmRecever::class.java)
        val reqCode = if (type.equals(TYPE_REPATING,ignoreCase = true)) ID_REPEATING else 0
        val pendingIntent = PendingIntent.getBroadcast(context,reqCode,intent,0)

        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Daily Reminder Canceled", Toast.LENGTH_SHORT).show()
    }
}
