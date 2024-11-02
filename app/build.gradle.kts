plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.climatic"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.climatic"
        minSdk = 24
        targetSdk = 34
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.places)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Gson library for converting Java objects to JSON and vice versa
    implementation("com.google.code.gson:gson:2.11.0")

// Retrofit library for handling HTTP requests
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

// Gson converter for Retrofit to handle JSON responses using Gson
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

// Glide library for loading and caching images
    implementation("com.github.bumptech.glide:glide:4.16.0")

// Glide compiler for annotation processing (required for Glide)
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

// Room library for database persistence
    implementation("androidx.room:room-runtime:2.6.1")

// Room annotation processor for generating database code
    annotationProcessor("androidx.room:room-compiler:2.6.1")

// Kotlin Annotation Processing Tool (KAPT) for Room's annotation processor in Kotlin
    kapt("androidx.room:room-compiler:2.6.1")

// Room KTX extensions for Kotlin-friendly APIs
    implementation("androidx.room:room-ktx:2.6.1")

// Lifecycle extensions for managing Android lifecycle states (deprecated but still used in some projects)
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

// ViewModel KTX extensions for Kotlin-friendly ViewModel APIs
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")

// LiveData KTX extensions for Kotlin-friendly LiveData APIs
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")

// Coroutines library for handling asynchronous tasks
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.0")

// WorkManager KTX for managing background tasks in Android
    implementation("androidx.work:work-runtime-ktx:2.9.1")

// Picasso library for loading and caching images
    implementation("com.squareup.picasso:picasso:2.8")

// Google Play Services Location API for accessing device location
    implementation("com.google.android.gms:play-services-location:21.1.0")
//Lotie
    implementation("com.airbnb.android:lottie:6.5.2")
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.22")

//navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.8.1")
    implementation ("androidx.navigation:navigation-ui-ktx:2.8.1")

//settings
    implementation("androidx.preference:preference-ktx:1.2.0")

//Maps
    implementation ("org.osmdroid:osmdroid-android:6.1.10")
    implementation ("org.osmdroid:osmdroid-wms:6.1.10")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")
}