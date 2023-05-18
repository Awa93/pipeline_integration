package com.example.githubactiondemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {
    private var info: String = ""

    var isEnable =  false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkBioMatricsAvailable()
        val btn = findViewById<TextView>(R.id.btnBiometric)
        if(isEnable){
            btn.visibility = View.VISIBLE
            btn.setOnClickListener {
                val executor = ContextCompat.getMainExecutor(this)
                val biometricManager = BiometricManager.from(this)
                btn.visibility = View.GONE
                setAuthUser(executor)
            }
        }


    }

    fun addTwoNumbers(a: Int, b: Int): Int {
        return a + b
    }

    fun checkBioMatricsAvailable() {
        Log.d("MY_APP_TAG", "checkBioMatricsAvailable called")
        val biometricManager = BiometricManager.from(this)

        when (biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
                    or BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )) {

            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                info = "App can authenticate using biometrics."
                isEnable = true
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.d("MY_APP_TAG", "No biometric features available on this device.")
                info = "No biometric features available on this device."

            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.d("MY_APP_TAG", "Biometric features are currently unavailable.")
                info = "Biometric features are currently unavailable."


            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                    )
                }
                startActivityForResult(enrollIntent, 100)
            }
        }

    }

    private fun setAuthUser(executor: Executor) {
        Log.d("MY_APP_TAG", "setAuthUser called.")

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            // 2
            .setTitle("Authentication Required!")
            // 3
            .setSubtitle("Important!")
            // 4
            .setDescription("please Authenticate to view the details")
            // 5
            .setDeviceCredentialAllowed(true)
            // 6
            .build()

        val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                // 2
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Log.d("MY_APP_TAG", "Authenticated Successfully!")

                    val sum = addTwoNumbers(2, 3)
                    val totaltext =  findViewById<TextView>(R.id.total)
                    totaltext.text = sum.toString()
                    totaltext.visibility = View.VISIBLE

                    val helloWorldLabel = findViewById<TextView>(R.id.helloWorldTest)

                    helloWorldLabel.text = "User Authenticated successfully"
                    helloWorldLabel.visibility = View.VISIBLE
                }
                // 3
                override fun onAuthenticationError(
                    errorCode: Int, errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext, "Athentication Failed", Toast.LENGTH_SHORT).show()
                }
                // 4
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Athentication Failed", Toast.LENGTH_SHORT).show()

                }
            })


        biometricPrompt.authenticate(promptInfo)
    }


}