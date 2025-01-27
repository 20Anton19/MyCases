plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlin)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-parcelize")
    id("kotlin-kapt")
    alias(libs.plugins.daggerHiltPlugin)
    alias(libs.plugins.gms)
}

android {
    namespace = "com.example.mycases"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mycases"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation (platform(libs.firebase.bom))

    implementation (libs.accompanist.systemuicontroller)
    implementation (libs.navigation.compose)
    implementation(libs.gson)
    implementation(libs.volley)
    implementation(libs.jsoup)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.compose.bom)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.compose.bom)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    //Coil
    implementation(libs.coil.compose)

    //Из MyRoomTry
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.test.core.ktx)
    implementation(libs.junit.ktx)
    kapt(libs.room.compiler)
    implementation (libs.lifecycle.viewmodel.compose)



    // Коррутины
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)

    // ViewModel
    implementation (libs.lifecycle.viewmodel.ktx)

    // Lifecycle
    //implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")

    implementation (libs.runtime.livedata)

    //Navigation
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    //Hilt
    implementation (libs.hilt.android)
    implementation (libs.hilt.navigation.compose)
    kapt (libs.hilt.compiler)
}