/*
 * Copyright (C) The Android Open Source Project
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
package ece.course.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.google.android.gms.vision.face.Face;

class FaceGraphic extends GraphicOverlay.Graphic {

  private static final String TAG = "FaceGraphic";

  private static final float DOT_RADIUS = 3.0f;
  private static final float TEXT_OFFSET_Y = -30.0f;

  private boolean mIsFrontFacing;

  // This variable may be written to by one of many threads. By declaring it as volatile,
  // we guarantee that when we read its contents, we're reading the most recent "write"
  // by any thread.
  private volatile Face mFace;

  private Paint mHintTextPaint;
  private Paint mHintOutlinePaint;
  private Paint mEyeWhitePaint;
  private Paint mIrisPaint;
  private Paint mEyeOutlinePaint;
  private Paint mEyelidPaint;

  private Drawable mPigNoseGraphic;
  private Drawable mMustacheGraphic;
  private Drawable mHappyStarGraphic;
  private Drawable mHatGraphic;


  FaceGraphic(GraphicOverlay overlay, Context context, boolean isFrontFacing) {
    super(overlay);
    mIsFrontFacing = isFrontFacing;
    Resources resources = context.getResources();
    initializePaints(resources);
    initializeGraphics(resources);
  }

  private void initializeGraphics(Resources resources) {
    mPigNoseGraphic = resources.getDrawable(R.drawable.switch_cameras);
    mMustacheGraphic = resources.getDrawable(R.drawable.switch_cameras);
    mHappyStarGraphic = resources.getDrawable(R.drawable.switch_cameras);
    mHatGraphic = resources.getDrawable(R.drawable.switch_cameras);
  }

  private void initializePaints(Resources resources) {
    mHintTextPaint = new Paint();
    mHintTextPaint.setColor(resources.getColor(R.color.overlayHint));
    mHintTextPaint.setTextSize(resources.getDimension(R.dimen.textSize));

    mHintOutlinePaint = new Paint();
    mHintOutlinePaint.setColor(resources.getColor(R.color.overlayHint));
    mHintOutlinePaint.setStyle(Paint.Style.STROKE);
    mHintOutlinePaint.setStrokeWidth(resources.getDimension(R.dimen.hintStroke));

    mEyeWhitePaint = new Paint();
    mEyeWhitePaint.setColor(resources.getColor(R.color.eyeWhite));
    mEyeWhitePaint.setStyle(Paint.Style.FILL);

    mIrisPaint = new Paint();
    mIrisPaint.setColor(resources.getColor(R.color.iris));
    mIrisPaint.setStyle(Paint.Style.FILL);

    mEyeOutlinePaint = new Paint();
    mEyeOutlinePaint.setColor(resources.getColor(R.color.eyeOutline));
    mEyeOutlinePaint.setStyle(Paint.Style.STROKE);
    mEyeOutlinePaint.setStrokeWidth(resources.getDimension(R.dimen.eyeOutlineStroke));

    mEyelidPaint = new Paint();
    mEyelidPaint.setColor(resources.getColor(R.color.eyelid));
    mEyelidPaint.setStyle(Paint.Style.FILL);
  }

  void update(Face face) {
    mFace = face;
    postInvalidate(); // Trigger a redraw of the graphic (i.e. cause draw() to be called).
  }

  @Override
  public void draw(Canvas canvas) {
// 2
    // Confirm that the face and its features are still visible
    // before drawing any graphics over it.
    Face face = mFace;
    if (face == null) {
      return;
    }

    // 3
    float centerX = translateX(face.getPosition().x + face.getWidth() / 2.0f);
    float centerY = translateY(face.getPosition().y + face.getHeight() / 2.0f);
    float offsetX = scaleX(face.getWidth() / 2.0f);
    float offsetY = scaleY(face.getHeight() / 2.0f);

    // 4
    // Draw a box around the face.
    float left = centerX - offsetX;
    float right = centerX + offsetX;
    float top = centerY - offsetY;
    float bottom = centerY + offsetY;

    // 5
    canvas.drawRect(left, top, right, bottom, mHintOutlinePaint);

    // 6
    // Draw the face's id.
    canvas.drawText(String.format("id: %d", face.getId()), centerX, centerY, mHintTextPaint);

  }

}
