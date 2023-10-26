/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.react

import com.facebook.react.utils.AgpConfiguratorUtils.configureBuildConfigFields
import com.facebook.react.utils.AgpConfiguratorUtils.configureDevPorts
import com.facebook.react.utils.BackwardCompatUtils.configureBackwardCompatibilityReactMap
import com.facebook.react.utils.DependencyUtils.configureDependencies
import com.facebook.react.utils.DependencyUtils.configureRepositories
import com.facebook.react.utils.DependencyUtils.readVersionAndGroupStrings
import com.facebook.react.utils.NdkConfiguratorUtils.configureReactNativeNdk
import java.io.File
import kotlin.system.exitProcess
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.jvm.Jvm

class ReactPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        checkJvmVersion(project)
        val extension = project.extensions.create("react", ReactExtension::class.java, project)
        // App Only Configuration
        project.pluginManager.withPlugin("com.android.application") {

            project.afterEvaluate {
                val reactNativeDir = extension.reactNativeDir.get().asFile
                val propertiesFile = File(reactNativeDir, "ReactAndroid/gradle.properties")
                val versionAndGroupStrings = readVersionAndGroupStrings(propertiesFile)
                val versionString = versionAndGroupStrings.first
                val groupString = versionAndGroupStrings.second
                configureDependencies(project, versionString, groupString)
                configureRepositories(project, reactNativeDir)
            }

            configureReactNativeNdk(project, extension)
            configureBuildConfigFields(project)
            configureDevPorts(project)
            configureBackwardCompatibilityReactMap(project)
        }
    }

    private fun checkJvmVersion(project: Project) {
        val jvmVersion = Jvm.current()?.javaVersion?.majorVersion
        if ((jvmVersion?.toIntOrNull() ?: 0) <= 8) {
            project.logger.error(
                    """

      ********************************************************************************

      ERROR: requires JDK11 or higher.
      Incompatible major version detected: '$jvmVersion'

      ********************************************************************************

      """
                            .trimIndent())
            exitProcess(1)
        }
    }

}
