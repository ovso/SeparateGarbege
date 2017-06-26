package io.github.ovso.separategarbege;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.pixplicity.easyprefs.library.Prefs;

/**
 * Created by jaeho on 2017. 6. 26
 */

public class PhotoViewActivity extends AppCompatActivity {
  @BindView(R.id.photoview) PhotoView mPhotoView;
  @BindView(R.id.pinch_zoom_imageview) ImageView mPinchZoomImageview;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_photoview);
    ButterKnife.bind(this);
    if (getIntent().hasExtra("url")) {
      String url = getIntent().getStringExtra("url");
      Glide.with(this).load(url).into(mPhotoView);
    }
    boolean isVisible = Prefs.getBoolean("GuideVisible", true);
    mPinchZoomImageview.setVisibility(isVisible?View.VISIBLE:View.GONE);
  }

  @OnClick(R.id.pinch_zoom_imageview) void onPinchZoomClick() {
    Prefs.putBoolean("GuideVisible", false);
    mPinchZoomImageview.setVisibility(View.GONE);
  }
}
