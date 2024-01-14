package com.example.listxml.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.listxml.Constants
import com.example.listxml.Constants.Companion.CAMERA_PERMISSION_CODE
import com.example.listxml.R
import com.example.listxml.data.firebase.user.UserFireViewModel
import com.example.listxml.data.room.UserViewModel
import com.example.listxml.data.room.user.UserEntity
import com.example.listxml.databinding.ActivityUserBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

@AndroidEntryPoint
class UserActivity : BaseActivity<ActivityUserBinding>(), View.OnClickListener {
    val userViewModel: UserViewModel by viewModels()
    val userFireViewModel: UserFireViewModel by viewModels()
    override fun getViewBinding() = ActivityUserBinding.inflate(layoutInflater)
    private lateinit var activityGalleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var activityCameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var requestGalleryPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var saveImageUri: Uri
    private lateinit var currentUser: UserEntity


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveImageUri = Uri.EMPTY
        activityGalleryLauncher()
        activityCameraLauncher()
        cameraLauncher()
        galleryLauncher()
        binding.textViewAddImage.setOnClickListener(this)
        binding.buttonSave.setOnClickListener(this)

        userViewModel.user.observe(this@UserActivity) { user ->
            currentUser = user ?: return@observe
            if (currentUser.image.isNotEmpty()) {
                val imagePath = currentUser.image
                // Set image
                if (File(imagePath).exists()) {
                    val imageUri = Uri.fromFile(File(imagePath))
                    lifecycleScope.launch {
                        val inputStream =
                            this@UserActivity.contentResolver.openInputStream(imageUri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        binding.imageView.setImageBitmap(bitmap)
                    }
                } else {
                    binding.imageView.setImageResource(R.drawable.ic_default_person)
                }

            }
        }
    }

    private fun galleryLauncher() {
        requestGalleryPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allPermissionsGranted = permissions.values.all { it }
            if (allPermissionsGranted) {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityGalleryLauncher.launch(galleryIntent)
            } else {
                showRationalDialogForPermissions()
            }
        }
    }

    private fun cameraLauncher() {
        requestCameraPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allPermissionsGranted = permissions.values.all { it }
            if (allPermissionsGranted) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                activityCameraLauncher.launch(cameraIntent)
            } else {
                showRationalDialogForPermissions()
            }
        }
    }

    private fun activityCameraLauncher() {
        activityCameraLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageBitmap: Bitmap = result.data?.extras?.get("data") as Bitmap
                saveImageToInternalStorage(imageBitmap)
                saveImageUri = saveImageToInternalStorage(imageBitmap)
                binding.imageView.setImageBitmap(imageBitmap)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun activityGalleryLauncher() {
        activityGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri = result.data!!.data
                val source: ImageDecoder.Source = imageUri?.let {
                    ImageDecoder.createSource(
                        contentResolver, it
                    )
                }!!
                val bitmap: Bitmap = ImageDecoder.decodeBitmap(source)
                saveImageUri = saveImageToInternalStorage(bitmap)
                Log.e("savedImg: ", "Path: $saveImageUri")
                binding.imageView.setImageBitmap(bitmap)
            }
        }
    }

    private fun pictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
            "Select photo from Gallery", "Capture photo from camera"
        )
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> requestGalleryPermissions()
                1 -> requestCameraPermissions()
            }
        }
        pictureDialog.show()
    }

    private fun requestCameraPermissions() {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
        )
        requestCameraPermissionLauncher.launch(permissions)
    }
    private fun requestGalleryPermissions() {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
        )
        requestGalleryPermissionLauncher.launch(permissions)
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this).setMessage(
            "It looks you have turned off permision required for this feature." + " It can ve enabled under the Application Settings"
        ).setPositiveButton("GO TO  SETTINGS") { _, _ ->
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }

        }.setNegativeButton("cancel") { dialog, which ->
            dialog.dismiss()
        }.show()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(application)
        var file = wrapper.getDir(Constants.IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val savedUri = Uri.parse(file.absolutePath)
        Log.d("ImageSaving", "$savedUri")
        return Uri.parse(file.absolutePath)
    }

    override fun onClick(view: View?) {
        val userName = binding.textViewUser.text.toString()

        when (view!!.id) {
            R.id.text_view_add_image -> {
                pictureDialog()
            }

            R.id.button_save -> {
                when {
                    userName.isEmpty() -> {
                        Toast.makeText(
                            this@UserActivity, "user name missing ", Toast.LENGTH_SHORT
                        ).show()
                    }

                    saveImageUri == Uri.EMPTY -> Toast.makeText(
                        this@UserActivity, "add image please", Toast.LENGTH_SHORT
                    ).show()

                    else -> {
                        lifecycleScope.launch {
                            userViewModel.updateUser(
                                userName = userName,
                                image = saveImageUri.toString()
                            )
                            val intent = Intent(this@UserActivity, ListActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}
