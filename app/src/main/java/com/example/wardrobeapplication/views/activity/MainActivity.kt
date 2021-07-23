package com.example.wardrobeapplication.views.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.wardrobeapplication.R
import com.example.wardrobeapplication.Utils.Constants
import com.example.wardrobeapplication.model.PairDataModel
import com.example.wardrobeapplication.viewmodel.MainViewModel
import com.example.wardrobeapplication.views.adapter.MainViewPagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.pair_single_row_view.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList


const val CAMERA_REQUEST_CODE = 1000
const val STORAGE_REQUEST_CODE = 2000

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBottomSheetDialog: BottomSheetDialog
    private lateinit var mTopsPagerAdapter: MainViewPagerAdapter
    private lateinit var mBottomPagerAdapter: MainViewPagerAdapter
    private lateinit var mMainViewModel: MainViewModel
    private lateinit var source: String
    private var topWearsList : MutableList<PairDataModel> = mutableListOf()
    private var bottomWearsList : MutableList<PairDataModel> = mutableListOf()


    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                val data: Intent? = result.data
                Log.d("SOURCE_RESULT", "HELLO $source")

                var bitmap = data?.extras?.get("data") as Bitmap?

                bitmap?.let {
                    var encodedString = bitMapToString(it)
                    encodedString?.let {
                        mMainViewModel.insertPair(PairDataModel(source, it))
                    }
                }

                if (bitmap == null){
                    var uri = data?.data
                    val galleryImage = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    galleryImage?.let {
                        var encodedString = bitMapToString(it)
                        encodedString?.let {
                            mMainViewModel.insertPair(PairDataModel(source, it))
                        }

                    }
                }

            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mMainViewModel = getViewModel()

        select_tops.setOnClickListener(this)
        select_bottom.setOnClickListener(this)
        mTopsPagerAdapter = MainViewPagerAdapter(this)
        mBottomPagerAdapter = MainViewPagerAdapter(this)

        tops_viewpager.adapter = mTopsPagerAdapter
        bottom_viewpager.adapter = mBottomPagerAdapter

        mMainViewModel.getBottomWear(Constants.SOURCE_BOTTOM_WEARS).observe(this, Observer { bottomWears ->
            bottomWearsList.clear()
            bottomWearsList.addAll(bottomWears)
            mBottomPagerAdapter.addAllItems(bottomWears)
        })


        mMainViewModel.getTopWear(Constants.SOURCE_TOP_WEARS).observe(this, Observer { topWears->
            topWearsList.clear()
            topWearsList.addAll(topWears)
            mTopsPagerAdapter.addAllItems(topWears)
        })

        pair_shuffle_icon.setOnClickListener {
            tops_viewpager.currentItem = getRandomWears(topWearsList)
            bottom_viewpager.currentItem = getRandomWears(bottomWearsList)
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.select_tops -> {
                initializeChoiceBottomSheet(Constants.SOURCE_TOP_WEARS)
            }

            R.id.select_bottom -> {
                initializeChoiceBottomSheet(Constants.SOURCE_BOTTOM_WEARS)
            }

            else -> {

            }
        }
    }

    private fun initializeChoiceBottomSheet(inputSource: String) {
        mBottomSheetDialog = BottomSheetDialog(this)
        source = inputSource
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        val closeText = view.findViewById<AppCompatTextView>(R.id.bottom_sheet_dialog_close)
        val openCameraText = view.findViewById<AppCompatTextView>(R.id.bottom_sheet_dialog_camera)
        val openGalleryText = view.findViewById<AppCompatTextView>(R.id.bottom_sheet_dialog_gallery)


        closeText.setOnClickListener {
            mBottomSheetDialog.dismiss()
        }

        openCameraText.setOnClickListener {

            mBottomSheetDialog.dismiss()
            if (checkRuntimePermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera()

            } else {
                requestPermissions(Manifest.permission.CAMERA, CAMERA_REQUEST_CODE)
            }
        }

        openGalleryText.setOnClickListener {
            mBottomSheetDialog.dismiss()
            if (checkRuntimePermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_REQUEST_CODE)
            }

        }

        mBottomSheetDialog.setContentView(view)
        mBottomSheetDialog.setCancelable(true)
        mBottomSheetDialog.show()
    }


    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, source)
        resultLauncher.launch(cameraIntent)
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, source)
        galleryIntent.type = "image/*"
        resultLauncher.launch(galleryIntent)
//        result.launch("image/*")
    }

    private fun checkRuntimePermission(permission: String): Int {
        return ContextCompat.checkSelfPermission(this, permission)
    }

    private fun requestPermissions(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }

            STORAGE_REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            }

        }
    }


    fun bitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        val temp: String = Base64.encodeToString(b, Base64.DEFAULT)
        return if (temp == null) {
            null
        } else temp
    }


    fun getRandomWears(list: List<PairDataModel>): Int {
        val rand = Random()
        return rand.nextInt(list.size)
    }
}