/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.react.utils

import com.android.build.api.variant.AndroidComponentsExtension
import com.facebook.react.ReactExtension
import com.facebook.react.utils.ProjectUtils.getReactNativeArchitectures
import com.facebook.react.utils.ProjectUtils.isNewArchEnabled
import java.io.File
import org.gradle.api.Project

internal object NdkConfiguratorUtils {
  @Suppress("UnstableApiUsage")
  fun configureReactNativeNdk(project: Project, extension: ReactExtension) {
    project.pluginManager.withPlugin("com.android.application") {
      project.extensions.getByType(AndroidComponentsExtension::class.java).finalizeDsl { ext ->
        if (!project.isNewArchEnabled) {
          // For Old Arch, we don't need to setup the NDK
          return@finalizeDsl
        }
        // We enable prefab so users can consume .so/headers from ReactAndroid and hermes-engine
        // .aar
        ext.buildFeatures.prefab = true

        // If the user has not provided a CmakeLists.txt path, let's provide
        // the default one from the framework
        if (ext.externalNativeBuild.cmake.path == null) {
          ext.externalNativeBuild.cmake.path =
              File(
                  extension.reactNativeDir.get().asFile,
                  "ReactAndroid/cmake-utils/default-app-setup/CMakeLists.txt")
        }

        // Parameters should be provided in an additive manner (do not override what
        // the user provided, but allow for sensible defaults).
        val cmakeArgs = ext.defaultConfig.externalNativeBuild.cmake.arguments
        if ("-DPROJECT_BUILD_DIR" !in cmakeArgs) {
          cmakeArgs.add("-DPROJECT_BUILD_DIR=${project.buildDir}")
        }
        if ("-DREACT_ANDROID_DIR" !in cmakeArgs) {
          cmakeArgs.add(
              "-DREACT_ANDROID_DIR=${extension.reactNativeDir.file("ReactAndroid").get().asFile}")
        }
        if ("-DANDROID_STL" !in cmakeArgs) {
          cmakeArgs.add("-DANDROID_STL=c++_shared")
        }

        val architectures = project.getReactNativeArchitectures()
        // abiFilters are split ABI are not compatible each other, so we set the abiFilters
        // only if the user hasn't enabled the split abi feature.
        if (architectures.isNotEmpty() && !ext.splits.abi.isEnable) {
          ext.defaultConfig.ndk.abiFilters.addAll(architectures)
        }
      }
    }
  }


}
