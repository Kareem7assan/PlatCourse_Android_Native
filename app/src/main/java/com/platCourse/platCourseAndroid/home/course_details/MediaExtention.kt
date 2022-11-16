package com.platCourse.platCourseAndroid.home.course_details


import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector.ParametersBuilder
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector.SelectionOverride
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo
import com.google.android.exoplayer2.trackselection.TrackSelectionOverride
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters
import com.google.android.exoplayer2.ui.TrackSelectionView
import com.google.android.exoplayer2.ui.TrackSelectionView.TrackSelectionListener
import com.google.android.exoplayer2.util.Assertions
import com.google.android.material.tabs.TabLayout
import com.platCourse.platCourseAndroid.R


fun DefaultTrackSelector.generateQualityList(): ArrayList<Pair<String, TrackSelectionOverride>> {
    //Render Track -> TRACK GROUPS (Track Array)(Video,Audio,Text)->Track
    val trackOverrideList = ArrayList<Pair<String, TrackSelectionOverride>>()

    val renderTrack = this.currentMappedTrackInfo
    val renderCount = renderTrack?.rendererCount ?: 0
    Log.e("qualities", renderTrack.toString())
    for (rendererIndex in 0 until renderCount) {

            val trackGroupType = renderTrack?.getRendererType(rendererIndex)
            val trackGroups = renderTrack?.getTrackGroups(rendererIndex)
            val trackGroupsCount = trackGroups?.length!!
        //Log.e("qualities_group", trackGroupType.toString() + "," + trackGroups[1].length)

                for (groupIndex in 0 until trackGroupsCount) {
                    val videoQualityTrackCount = trackGroups[groupIndex].length
                    for (trackIndex in 0 until videoQualityTrackCount) {
                        val isTrackSupported = renderTrack.getTrackSupport(
                            rendererIndex,
                            groupIndex,
                            trackIndex
                        ) != C.FORMAT_EXCEEDS_CAPABILITIES
                        if (1==1) {
                            val track = trackGroups[groupIndex]
                            val trackName =
                                    "${track.getFormat(trackIndex).width} x ${track.getFormat(
                                        trackIndex
                                    ).height}"
                            if (track.getFormat(trackIndex).selectionFlags==C.SELECTION_FLAG_AUTOSELECT){
                                trackName.plus(" (Default)")
                            }
                            val parms=TrackSelectionParameters.Builder(context!!)
                                    .clearOverridesOfType(C.TRACK_TYPE_VIDEO)
                                    .addOverride(TrackSelectionOverride(track, listOf(trackIndex)))



                            val trackBuilder = TrackSelectionOverride(track, trackIndex)

                                  /*  TrackSelectionOverrides.Builder()
                                            .clearOverridesOfType(C.TRACK_TYPE_VIDEO)
                                            .addOverride(TrackSelectionOverrides.TrackSelectionOverride(track,
                                                    listOf(trackIndex)))*/
                            trackOverrideList.add(Pair(trackName, trackBuilder))
                        }

                }
            }

    }
    Log.e("qualities2", trackOverrideList.toString())
    return trackOverrideList
}

fun isSupportedFormat(mappedTrackInfo: MappedTrackInfo?, rendererIndex: Int): Boolean {
    val trackGroupArray = mappedTrackInfo?.getTrackGroups(rendererIndex)
    return if (trackGroupArray?.length == 0) {
        false
    } else mappedTrackInfo?.getRendererType(rendererIndex) == C.TRACK_TYPE_VIDEO || mappedTrackInfo?.getRendererType(
        rendererIndex
    ) == C.TRACK_TYPE_AUDIO || mappedTrackInfo?.getRendererType(rendererIndex) == C.TRACK_TYPE_TEXT
}

/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


