/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.react

import javax.inject.Inject
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty

abstract class ReactExtension @Inject constructor(project: Project) {

  private val objects = project.objects

  /**
   * The path to the root of your project. This is the path to where the `package.json` lives. All
   * the CLI commands will be invoked from this folder as working directory.
   *
   * Default: ${rootProject.dir}/../
   */
  val root: DirectoryProperty =
      objects.directoryProperty().convention(project.rootProject.layout.projectDirectory.dir("./"))

  /**
   * The path to the react-native NPM package folder.
   *
   * Default: ${rootProject.dir}/../node_modules/react-native-codegen
   */
  val reactNativeDir: DirectoryProperty =
      objects.directoryProperty().convention(root.dir("react-native"))

}
