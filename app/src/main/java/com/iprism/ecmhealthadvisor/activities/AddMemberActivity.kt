package com.iprism.ecmhealthadvisor.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.iprism.ecmcorporatemarketing.utils.DateTimeUtils
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.ActivityAddMemberBinding
import com.iprism.ecmhealthadvisor.databinding.ActivityMainBinding
import com.iprism.ecmhealthadvisor.modals.addleads.AddLeadApiRequest
import com.iprism.ecmhealthadvisor.modals.addleads.BloodGroup
import com.iprism.ecmhealthadvisor.modals.addleads.Gender
import com.iprism.ecmhealthadvisor.modals.addleads.LeadPaymentType
import com.iprism.ecmhealthadvisor.repositoris.LeadsRepository
import com.iprism.ecmhealthadvisor.utils.Constants
import com.iprism.ecmhealthadvisor.utils.ToastUtils
import com.iprism.ecmhealthadvisor.utils.UiState
import com.iprism.ecmhealthadvisor.utils.User
import com.iprism.ecmhealthadvisor.utils.hideProgress
import com.iprism.ecmhealthadvisor.utils.showProgress
import com.iprism.ecmhealthadvisor.utils.showToast
import com.iprism.ecmhealthadvisor.viewmodels.LeadsViewModel
import com.iprism.ecmhealthadvisor.viewmodels.ViewModelFactory
import com.yalantis.ucrop.UCrop
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.regex.Pattern
import kotlin.toString

class AddMemberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMemberBinding
    private var name = ""
    private var type = ""
    private var tag = ""
    private lateinit var leadsViewModel: LeadsViewModel
    private lateinit var offerLauncher: ActivityResultLauncher<Intent>
    private lateinit var locationPermissionLauncher: ActivityResultLauncher<Array<String>>
    private var lat = ""
    private var lon = ""
    private var address = ""
    private var genderId = "-1"
    private var bloodgroupId = "-1"
    private var paymentTypeId = ""
    private var insuranceType = ""
    private var paymentTypeName = ""
    private var profession = ""
    private lateinit var user: User
    private lateinit var userDetails: HashMap<String, String?>
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CAMERA_PERMISSION = 100
    private var profileUri: Uri? = null
    private var launchSomeActivity: ActivityResultLauncher<Intent>? = null
    val paymentTypes = listOf(
        LeadPaymentType("-1", "", "Select Payment Type"),
        LeadPaymentType("1", "cash", "Cash"),
        LeadPaymentType("2", "health_insurance", "Health Insurance"),
        LeadPaymentType("3", "others", "Others")
    )

    private val cropImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val croppedUri = result.data?.let { UCrop.getOutput(it) }
                croppedUri?.let { setProfileImage(it) }
            } else if (result.resultCode == UCrop.RESULT_ERROR) {
                val cropError = result.data?.let { UCrop.getError(it) }
                cropError?.printStackTrace()
            }
        }

    private fun setProfileImage(uri: Uri) {
        profileUri = uri
        binding.profileIv.visibility = View.VISIBLE
        Glide.with(this)
            .load(uri)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .circleCrop()
            .into(binding.profileIv)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = User(this)
        userDetails = user.getUserDetails()
        name = intent.getStringExtra("name").toString()
        type = intent.getStringExtra("type").toString()
        tag = intent.getStringExtra("tag").toString()
//        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        binding.titleTxt.text = name
        binding.advisorNameTxt.text = userDetails[User.NAME]
        binding.hospitalNameTxt.text = userDetails[User.HOSPITAL_NAME]
        if (userDetails[User.IMAGE].toString().isNotEmpty()) {
            Glide.with(this).load(Constants.IMAGES_URL + userDetails[User.IMAGE]).error(
                ContextCompat.getDrawable(this, R.drawable.customer_image)
            ).into(binding.advisorImg)
        } else {
            binding.advisorImg.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.customer_image
                )
            )
        }

        binding.profileIv.borderColor = ContextCompat.getColor(this, R.color.green)
        binding.profileIv.borderWidth = 4
        createLaunchSomeActivity()
        initViewModel()
        setupActivityResultLaunchers()
        handleBack()
        handleSubmitBtn()
        handleAddressLo()
        handleProfileLo()
        handleDobLo()
        setupPaymentTypesAdapter(paymentTypes)
        observeUserDropdownsResponse()
        leadsViewModel.fetchUserDropDowns()
        binding.professionRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.employee_rb -> {
                    profession = "Employee"
                }

                R.id.business_rb -> {
                    profession = "Business"
                }

                R.id.proffession_others_rb -> {
                    profession = "Others"
                }
            }
        }

        binding.insuranceCompaniesRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.personal_insurance_rb -> {
                    insuranceType = "personal_insurance"
                }

                R.id.company_insurance_rb -> {
                    insuranceType = "company_insurance"
                }
            }
        }

        binding.othersRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.state_govt_rb -> {
                    insuranceType = "state_govt"
                }

                R.id.central_govt_rb -> {
                    insuranceType = "central_govt"
                }

                R.id.arogyabhadratha_rb -> {
                    insuranceType = "arogyabhadratha"
                }

                R.id.arogyasree_rb -> {
                    insuranceType = "arogyasree"
                }

                R.id.ayushmanbhava_rb -> {
                    insuranceType = "ayushmanbhava"
                }

                R.id.others_rb -> {
                    insuranceType = "others"
                }
            }

        }
        observeAddLeadResponse()
    }

    private fun handleDobLo() {
        binding.dobLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getDate(binding.dateOfBirthTxt, true)
        })
    }

    private fun initViewModel() {
        val repository = LeadsRepository()
        val factory = ViewModelFactory { LeadsViewModel(repository) }
        leadsViewModel = ViewModelProvider(this, factory)[LeadsViewModel::class.java]
    }

    private fun setupPaymentTypesAdapter(paymentTypes: List<LeadPaymentType>) {
        var namesList = paymentTypes.map { it.formattedName }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, namesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.paymentsSp.adapter = adapter
        binding.paymentsSp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    paymentTypeId = paymentTypes[position].id.toString()
                    paymentTypeName = paymentTypes[position].name.toString()
                    if (paymentTypeName.isEmpty() || paymentTypeName.equals("cash", true)) {
                        binding.insuranceLo.visibility = View.GONE
                        binding.othersLo.visibility = View.GONE
                        insuranceType = "cash"
                    } else if (paymentTypeName.equals("health_insurance", true)) {
                        binding.insuranceLo.visibility = View.VISIBLE
                        binding.othersLo.visibility = View.GONE
                        insuranceType = ""
                    } else if (paymentTypeName.equals("others", true)) {
                        binding.insuranceLo.visibility = View.GONE
                        binding.othersLo.visibility = View.VISIBLE
                        insuranceType = ""
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun setupActivityResultLaunchers() {
        offerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.let { data ->
                        address = data.getStringExtra("address").orEmpty()
                        lat = data.getStringExtra("lat").orEmpty()
                        lon = data.getStringExtra("lon").orEmpty()
                        binding.addressTxt.text = address
                    }
                }
            }

        locationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
                val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

                if (fineGranted || coarseGranted) {
                    val intent = Intent(this, LocationActivity::class.java)
                    offerLauncher.launch(intent)
                } else {
                    ToastUtils.showErrorCustomToast(
                        this,
                        "Location permission is required to continue"
                    )
                }
            }
    }

    private fun handleAddressLo() {
        binding.addressLo.setOnClickListener {
            if (hasLocationPermission()) {
                val intent = Intent(this, LocationActivity::class.java)
                offerLauncher.launch(intent)
            } else {
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineLocationGranted || coarseLocationGranted
    }

    private fun setupGenderTypesAdapter(genderTypes: List<Gender>) {
        var namesList = genderTypes.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, namesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.gendersSp.adapter = adapter
        binding.gendersSp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    genderId = genderTypes[position].id.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun setupBloodGroupsAdapter(bloodGroups: List<BloodGroup>) {
        var namesList = bloodGroups.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, namesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.bloodGroupsSp.adapter = adapter
        binding.bloodGroupsSp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    bloodgroupId = bloodGroups[position].id.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun handleSubmitBtn() {
        binding.downloadAppBtn.setOnClickListener {
            if (profileUri == null) {
                ToastUtils.showErrorCustomToast(this, "Please Add " + tag + "Profile..!")
            } else if (getName().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter " + tag + "Name..!")
            } else if (getMobile().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter " + tag + "Mobile Number..!")
            } else if (getMobile().length != 10) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Valid Mobile Number..!")
            } else if (Pattern.matches("[0-5].*", getMobile())) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Valid Mobile Number..!")
            } else if (getEmail().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter " + tag + "Email ID..!")
            } else if (!isValidEmailAddress(getEmail())) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Valid Email ID..!")
            } else if (genderId.equals("-1", true)) {
                ToastUtils.showErrorCustomToast(this, "Please Select Gender..!")
            } else if (bloodgroupId.equals("-1", true)) {
                ToastUtils.showErrorCustomToast(this, "Please Select Blood Group..!")
            } else if (getDob().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Select Date of Birth..!")
            } else if (profession.isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Select Profession Type..!")
            } else if (getFamilyMembers().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Family Members Count..!")
            } else if (getFamilyMembers().matches(Regex("0+"))) {
                ToastUtils.showErrorCustomToast(this, "Family Members Count should not be Zero..!!")
            } else if (getAddress().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Select User Address..!")
            } else if (getTreatmentStatus().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Treatment Status..!")
            } else if (paymentTypeId.equals("-1", true)) {
                ToastUtils.showErrorCustomToast(this, "Please Select Payment Type..!")
            } else if (paymentTypeId.equals("2", true) && insuranceType.isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Select  Insurance Type..!")
            } else if (paymentTypeId.equals("2", true) && getInsuranceCompanyName().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Insurance Company Name..!")
            } else if (paymentTypeId.equals("2", true) && getTpaName().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter TPA Name..!")
            } else if (paymentTypeId.equals("2", true) && getNoOfPersonsCovered().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter No. of Persons Covered..!")
            } else if (paymentTypeId.equals(
                    "2",
                    true
                ) && getNoOfPersonsCovered().matches(Regex("0+"))
            ) {
                ToastUtils.showErrorCustomToast(
                    this,
                    "No. of Persons Covered Should not be Zero..!"
                )
            } else if (paymentTypeId.equals("3", true) && insuranceType.isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Select  Others Type!")
            } else {
                var addLeadApiRequest = AddLeadApiRequest(
                    getAddress(),
                    userDetails[User.AUTH_TOKEN].toString(),
                    getDob(),
                    getEmail(),
                    genderId,
                    bloodgroupId,
                    convertUriToBase64Image(profileUri),
                    getInsuranceCompanyName(),
                    lat,
                    type,
                    lon,
                    userDetails[User.MAIN_DATA_ID].toString(),
                    getMobile(),
                    getName(),
                    getNoOfPersonsCovered(),
                    paymentTypeName,
                    insuranceType,
                    profession,
                    getFamilyMembers(),
                    getTpaName(),
                    getTreatmentStatus(),
                    userDetails[User.ID].toString()
                )
                leadsViewModel.addLead(addLeadApiRequest)
                Log.d("AddLeadRequest", addLeadApiRequest.toString())
            }

        }
    }

    private fun observeUserDropdownsResponse() {
        leadsViewModel.userDropDownsResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.gender.isNotEmpty()) {
                        var updatedList = result.data.gender.toMutableList()
                        updatedList.add(0, Gender("-1", "Select Gender"))
                        setupGenderTypesAdapter(updatedList)
                    } else {
                        ToastUtils.showErrorCustomToast(this, "No Gender Found!")
                    }

                    if (result.data.blood_groups.isNotEmpty()) {
                        var updatedList = result.data.blood_groups.toMutableList()
                        updatedList.add(0, BloodGroup("-1", "Select Blood Group"))
                        setupBloodGroupsAdapter(updatedList)
                    } else {
                        ToastUtils.showErrorCustomToast(this, "No Blood Groups Found!")
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener {
            finish()
        }
    }

    private fun getName(): String {
        return binding.nameTxt.text.toString().trim()
    }

    private fun getMobile(): String {
        return binding.contactNumberTxt.text.toString().trim()
    }

    private fun getEmail(): String {
        return binding.emailTxt.text.toString().trim()
    }

    private fun getDob(): String {
        return binding.dateOfBirthTxt.text.toString().trim()
    }

    private fun getFamilyMembers(): String {
        return binding.familyMembersTxt.text.toString().trim()
    }

    private fun getAddress(): String {
        return binding.addressTxt.text.toString().trim()
    }

    private fun getTreatmentStatus(): String {
        return binding.treatmentStatusTxt.text.toString().trim()
    }

    private fun getInsuranceCompanyName(): String {
        return binding.insuranceCompanyNameTxt.text.toString().trim()
    }

    private fun getTpaName(): String {
        return binding.tpaNameTxt.text.toString().trim()
    }

    private fun getNoOfPersonsCovered(): String {
        return binding.noPersonsCoveredTxt.text.toString().trim()
    }

    private fun handleProfileLo() {
        binding.profileLo.setOnClickListener(View.OnClickListener {
            selectImage()
        })
    }

    private fun isValidEmailAddress(email: String): Boolean {
        val ePattern = "^[a-zA-Z0-9][a-zA-Z0-9._%+-]*@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        val pattern = Pattern.compile(ePattern)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun convertUriToBase64Image(imageUri: Uri?): String {
        if (imageUri == null) return ""

        return try {
            val inputStream = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (bitmap != null) {
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    byteArrayOutputStream
                ) // Use PNG if you prefer lossless
                val imageBytes = byteArrayOutputStream.toByteArray()
                Base64.encodeToString(imageBytes, Base64.DEFAULT)
            } else {
                ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    private fun createLaunchSomeActivity() {
        launchSomeActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val data = result.data
                val imageUri = result.data?.data
                imageUri?.let { startCrop(it) }
            }
        }
    }

    @SuppressLint("IntentReset")
    private fun selectImage() {
        var options = arrayOf<CharSequence>()
        options = arrayOf<CharSequence>("Choose From Gallery", "Camera", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Choose From Gallery") {
                val pickPhoto =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickPhoto.type = "image/*"
                launchSomeActivity!!.launch(pickPhoto)
            } else if (options[item] == "Camera") {
                if (checkPermissions()) {
                    launchCameraIntent()
                } else {
                    requestPermissions()
                }
            } else {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun startCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "cropped_image.jpg"))
        val uCrop = UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(512, 512)
            .withOptions(getUCropOptions())
        cropImageLauncher.launch(uCrop.getIntent(this))
    }

    private fun getUCropOptions(): UCrop.Options {
        val options = UCrop.Options()
        options.setCircleDimmedLayer(true)
        options.setShowCropGrid(false)
        options.setShowCropFrame(false)
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
        options.setCompressionQuality(90)
        options.setHideBottomControls(true)
        options.setFreeStyleCropEnabled(false)
        return options
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            val bitmap = data.extras!!["data"] as Bitmap?
            val baos = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            //  Base64.Encoder encoder = Base64.getEncoder();
            val uri: Uri = getImageUri(this@AddMemberActivity, bitmap)!!
            uri?.let { startCrop(it) }
            /*  profileUri = uri
              binding.profileImg.visibility = View.VISIBLE
              binding.profileImg.setImageURI(profileUri)*/
        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val file = File(inContext.cacheDir, "image.jpg")
        try {
            val out = FileOutputStream(file)
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.fromFile(file)
    }

    private fun checkPermissions(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val storagePermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return cameraPermission == PackageManager.PERMISSION_GRANTED &&
                storagePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CAMERA_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    launchCameraIntent()
                } else {
                    showToast("Permission Denied")
                }
                return
            }

            else -> {

            }
        }
    }

    private fun launchCameraIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    private fun observeAddLeadResponse() {
        leadsViewModel.addLeadResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.downloadAppBtn.isEnabled = false
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    var intent = Intent(this, SuccessActivity::class.java)
                    intent.putExtra("tag", tag + "Added ")
                    startActivity(intent)
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                    binding.downloadAppBtn.isEnabled = true
                }
            }
        }
    }

}