# Copyright (c) Meta Platforms, Inc. and affiliates.
#
# This source code is licensed under the MIT license found in the
# LICENSE file in the root directory of this source tree.

cmake_minimum_required(VERSION 3.13)
set(CMAKE_VERBOSE_MAKEFILE on)

add_library(jsc SHARED IMPORTED GLOBAL)
set_target_properties(jsc
        PROPERTIES
        IMPORTED_LOCATION
        ${CMAKE_CURRENT_SOURCE_DIR}/jni/${ANDROID_ABI}/libjsc.so)

target_include_directories(jsc INTERFACE .)
