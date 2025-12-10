package com.iprism.ecmhealthadvisor.activities

import SelectedFileAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.ecmcorporatemarketing.utils.DateTimeUtils
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.ActivityMainBinding
import com.iprism.ecmhealthadvisor.databinding.ActivityReferDiagnosticBinding
import com.iprism.ecmhealthadvisor.modals.addleads.BloodGroup
import com.iprism.ecmhealthadvisor.modals.addleads.Gender
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.FileItem
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.FileType
import com.iprism.ecmhealthadvisor.modals.referadmissionanddiagnostic.ReferApiRequest
import com.iprism.ecmhealthadvisor.modals.toprequests.ReferDiagnosticApiRequest
import com.iprism.ecmhealthadvisor.repositoris.LeadsRepository
import com.iprism.ecmhealthadvisor.repositoris.ReferRepository
import com.iprism.ecmhealthadvisor.repositoris.TopRequestRepository
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.viewmodels.LeadsViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ReferViewModel
import com.iprism.ecmhealthadvisor.viewmodels.TopRequestViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory
import java.util.regex.Pattern
import kotlin.toString

class ReferDiagnosticActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReferDiagnosticBinding
    private lateinit var viewModel: TopRequestViewModel
    private lateinit var userDetails: HashMap<String, String?>
    private lateinit var user: User

    private val selectedFiles = mutableListOf<FileItem>()
    private lateinit var adapter: SelectedFileAdapter

    private val REQUEST_CODE_IMAGES = 100
    private val REQUEST_CODE_PDFS = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReferDiagnosticBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUser()
        initViewModel()
        initRecyclerView()
        setupClickListeners()
        observeReferDiagnosticResponse()
    }

    private fun initUser() {
        user = User(this)
        userDetails = user.getUserDetails()
    }

    private fun initViewModel() {
        val factory = ViewModelFactory { TopRequestViewModel(TopRequestRepository()) }
        viewModel = ViewModelProvider(this, factory)[TopRequestViewModel::class.java]
    }

    private fun initRecyclerView() {
        adapter = SelectedFileAdapter(selectedFiles)
        binding.selectedFilesRv.layoutManager = GridLayoutManager(this, 3)
        binding.selectedFilesRv.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.backImg.setOnClickListener { finish() }

        binding.galleryIv.setOnClickListener {
            pickFiles("image/*", REQUEST_CODE_IMAGES, "Select Images")
        }

        binding.fileIv.setOnClickListener {
            pickFiles("application/pdf", REQUEST_CODE_PDFS, "Select PDFs")
        }

        binding.referNowBtn.setOnClickListener { validateAndSubmit() }
    }

    private fun pickFiles(type: String, requestCode: Int, title: String) {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            this.type = type
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(Intent.createChooser(intent, title), requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK || data == null) return

        val fileType = if (requestCode == REQUEST_CODE_IMAGES) FileType.IMAGE else FileType.PDF
        val clipData = data.clipData

        if (selectedFiles.isNotEmpty() && selectedFiles[0].type != fileType) {
            selectedFiles.clear()
        }

        val currentCount = selectedFiles.size

        if (clipData != null) {
            if (currentCount + clipData.itemCount > 10) {
                ToastUtils.showErrorCustomToast(
                    this,
                    "You can select only 10 ${fileType.name.lowercase()}s."
                )
                return
            }

            for (i in 0 until clipData.itemCount) {
                selectedFiles.add(FileItem(clipData.getItemAt(i).uri, fileType))
            }

        } else {
            data.data?.let { uri ->
                if (currentCount + 1 > 10) {
                    ToastUtils.showErrorCustomToast(
                        this,
                        "You can select only 10 ${fileType.name.lowercase()}s."
                    )
                    return
                }
                selectedFiles.add(FileItem(uri, fileType))
            }
        }

        adapter.notifyDataSetChanged()
    }

    private fun validateAndSubmit() {
        when {
            getName().isEmpty() -> {
                ToastUtils.showErrorCustomToast(this, "Please Enter Patient Name..!")
            }

            getAge().isEmpty() -> {
                ToastUtils.showErrorCustomToast(this, "Please Enter Patient Age..!")
            }

            getAge().matches(Regex("0+")) -> {
                ToastUtils.showErrorCustomToast(this, "Patient Age Should not be Zero..!")
            }

            getMobile().isEmpty() -> {
                ToastUtils.showErrorCustomToast(this, "Please Enter Patient Mobile Number..!")
            }

            getMobile().length != 10 -> {
                ToastUtils.showErrorCustomToast(this, "Please Enter Valid Mobile Number..!")
            }

            Pattern.matches("[0-5].*", getMobile()) -> {
                ToastUtils.showErrorCustomToast(this, "Please Enter Valid Mobile Number..!")
            }

            getTestName().isEmpty() && selectedFiles.isEmpty() -> {
                ToastUtils.showErrorCustomToast(
                    this,
                    "Please Enter Test Name or Upload Prescription..!"
                )
            }

            else -> submitRequest()
        }
    }

    private fun submitRequest() {
        val base64List = selectedFiles.map { convertUriToBase64(it.uri) }
        val commaSeparatedString = base64List.joinToString(",")

        val request = ReferDiagnosticApiRequest(
            getAge(),
            userDetails[User.AUTH_TOKEN].toString(),
            commaSeparatedString,
            userDetails[User.MAIN_DATA_ID].toString(),
            getMobile(),
            getName(),
            getTestName(),
            userDetails[User.ID].toString()
        )
        viewModel.referDiagnostic(request)
        Log.d("DiagnosticRequest", request.toString())
    }

    private fun observeReferDiagnosticResponse() {
        viewModel.referDiagnosticResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.referNowBtn.isEnabled = false
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    startActivity(Intent(this, SuccessActivity::class.java).apply {
                        putExtra("tag", "Refer Diagnostic Request Sent")
                    })
                }

                is UiState.Error -> {
                    binding.referNowBtn.isEnabled = true
                    binding.progress.hideProgress()
                    ToastUtils.showErrorCustomToast(this, result.message)
                }
            }
        }
    }

    private fun getName() = binding.nameTxt.text.toString().trim()
    private fun getAge() = binding.ageTxt.text.toString().trim()
    private fun getTestName() = binding.testNameTxt.text.toString().trim()
    private fun getMobile() = binding.contactNumberTxt.text.toString().trim()

    private fun convertUriToBase64(uri: Uri): String {
        val bytes = contentResolver.openInputStream(uri)?.readBytes() ?: byteArrayOf()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

}