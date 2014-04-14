/*
 * Copyright (C) 2014 The Android Open Source Project
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

package com.google.android.glass.sample.charades;

import com.google.android.glass.touchpad.Gesture;

import android.os.Bundle;
import android.view.View;

import java.util.Arrays;
import java.util.List;

/**
 * An implementation of the game that acts as a tutorial, restricting certain gestures to match
 * the instruction phrases on the screen.
 */
public class TutorialActivity extends BaseGameActivity {

    /** The index of the "swipe to pass" card in the tutorial model. */
    private static final int SWIPE_TO_PASS_CARD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the status bar in tutorial mode.
        findViewById(R.id.status_bar).setVisibility(View.GONE);
    }

    /** Overridden to load the fixed tutorial phrases from the application's resources. */
    @Override
    protected CharadesModel createCharadesModel() {
        List<String> tutorialPhrases = Arrays.asList(getResources().getStringArray(
                R.array.tutorial_phrases));
        return new CharadesModel(tutorialPhrases);
    }

    /**
     * Overridden to only allow the tap gesture on the "Tap to score" screen and to only allow the
     * swipe gesture on the "Swipe to pass" screen. The game is also automatically ended when the
     * final card is either tapped or swiped.
     */
    @Override
    protected void handleGameGesture(Gesture gesture) {
        int phraseIndex = getCharadesModel().getCurrentPhraseIndex();
        switch (gesture) {
            case TAP:
                if (phraseIndex != SWIPE_TO_PASS_CARD) {
                    score();
                }
                break;
            case SWIPE_RIGHT:
                if (phraseIndex == SWIPE_TO_PASS_CARD) {
                    pass();
                }
                break;
        }

        // Finish the tutorial if we transitioned away from the final card.
        if (phraseIndex == getCharadesModel().getPhraseCount() - 1) {
            finish();
        }
    }
}
