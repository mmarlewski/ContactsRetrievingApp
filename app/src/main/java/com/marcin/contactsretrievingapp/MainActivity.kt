package com.marcin.contactsretrievingapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permission=Manifest.permission.READ_CONTACTS
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 1)
        }
    }

    override fun onStart()
    {
        super.onStart()

        readAndSendContacts()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode==1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show()
        }
    }

    fun readAndSendContacts()
    {
        var contactsText=""

        val contacts= contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null) ?: return

        while(contacts.moveToNext())
        {
            val nameIndex=contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val name=contacts.getString(nameIndex)

            val numberIndex=contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val number=contacts.getString(numberIndex)

            contactsText+="$name : $number \n"
        }

        contacts.close()

        val intent=Intent()
        intent.action = "com.marcin.CONTACTS_ACTION"
        intent.`package` = "com.marcin.emailsendingapp"
        intent.putExtra("com.marcin.CONTACTS_TEXT",contactsText)
        sendBroadcast(intent)
    }
}