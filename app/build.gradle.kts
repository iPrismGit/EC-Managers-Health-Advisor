plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.onesignal.androidsdk.onesignal-gradle-plugin")
}

android {
    namespace = "com.iprism.ecmhealthadvisor"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.iprism.ecmhealthadvisor"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.google.play.base)
    implementation(libs.google.play.location)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.analytics)

    implementation(libs.circleimageview)
    implementation(libs.pinview)
    implementation(libs.avloading)
    implementation(libs.android.image.slider)
    implementation(libs.swipe.refresh.layout)
    implementation(libs.photoview)
    implementation(libs.ucrop)
    implementation(libs.facebook.shimmer)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.app.update)
    implementation(libs.bumptech.glide)
    implementation(libs.exo.player)
    implementation(libs.exo.player.ui)

    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.19.0")
    implementation("com.google.android.gms:play-services-auth-api-phone:18.0.1")
    implementation("com.google.android.libraries.places:places:2.6.0")
    implementation("com.onesignal:OneSignal:[4.0.0, 4.99.99]")

}