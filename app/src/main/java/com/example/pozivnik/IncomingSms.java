package com.example.pozivnik;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.pozivnik.Database.DatabaseHelper;
import com.example.pozivnik.Database.NumberNote;

import java.util.List;

import static java.lang.Thread.sleep;

public class IncomingSms extends BroadcastReceiver {

    public static DatabaseHelper db;

    @Override
    public void onReceive(Context context, Intent intent) {
        String phoneNumber;
        db = new DatabaseHelper(context);

        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String message = currentMessage.getDisplayMessageBody();

                    List<String> numbers = db.getAllNumbers();
                    NumberNote note = db.getNumberFilters(phoneNumber);
                    String filterIncludes = note.getFilterIncludes();
                    String filterExcludes = note.getFilterExcludes();

                    boolean includes = true;
                    boolean excludes = true;
                    String[] wordsIncludes = null;
                    String[] wordsExcludes = null;
                    if(!filterIncludes.isEmpty()){
                        wordsIncludes = filterIncludes.split(";",10);
                    }
                    if(!filterExcludes.isEmpty()){
                        wordsExcludes = filterExcludes.split(";",10);
                    }

                    if(wordsIncludes != null)
                    for(int k = 0; k< wordsIncludes.length; k++){
                        if(message.contains(wordsIncludes[k])){
                            includes = true;
                            break;
                        }else{
                            includes = false;
                        }
                    }
                    if(wordsExcludes != null)
                    for(int k = 0; k< wordsExcludes.length; k++){
                        if(!message.contains(wordsExcludes[k])){
                            excludes = true;
                            break;
                        }else{
                            excludes = false;
                        }
                    }

                    if (numbers.contains(phoneNumber) && includes && excludes) {
                        Intent service = new Intent(context, MyService.class);
                        service.putExtra("number", phoneNumber);
                        service.putExtra("message", message);
                        //Toast.makeText(context, "phone: " + phoneNumber + "message" + message, Toast.LENGTH_SHORT).show();
                        context.startService(service);
                    }
                }// end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }
}
