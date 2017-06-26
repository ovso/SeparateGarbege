package io.github.ovso.separategarbege;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnAdapterItemClickListener {
  private final static String TAG = "MainActivity";
  private Unbinder mUnbinder;
  @BindView(R.id.scroll_button) Button mScrollButton;
  @BindView(R.id.scale_button) Button mScaleButton;
  private DiscreteScrollView mScrollView;
  private MainItem mCurrentMainItem;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mUnbinder = ButterKnife.bind(this);
    showProgress();
    mScrollView = (DiscreteScrollView) findViewById(R.id.scrollview);
    mScrollView.setItemTransformer(new ScaleTransformer.Builder().setMinScale(0.8f).build());
    mScrollView.setAdapter(new MainAdapter());
    mScrollView.addOnItemChangedListener(
        new DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>() {
          @Override public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder,
              int adapterPosition) {
            MainAdapter adapter = (MainAdapter) mScrollView.getAdapter();
            mCurrentMainItem = adapter.getItem(adapterPosition);
          }
        });
    DatabaseReference reference =
        FirebaseDatabase.getInstance("https://separategarbege.firebaseio.com/")
            .getReference("separate");
    reference.addValueEventListener(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        final ArrayList<MainItem> mainItems = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          mainItems.add(snapshot.getValue(MainItem.class));
        }
        runOnUiThread(new Runnable() {
          @Override public void run() {
            setAdapterItems(mainItems);
            hideProgress();
          }
        });
      }

      @Override public void onCancelled(DatabaseError databaseError) {
        Log.d(TAG, databaseError.getMessage());
        hideProgress();
      }
    });
  }

  @OnClick(R.id.scroll_button) void onScrollClick(View view) {
    final PopupMenu popupMenu = new PopupMenu(this, view);
    for (int i = 0; i < mTableOfContentsList.size(); i++) {
      MainItem mainItem = mTableOfContentsList.get(i);
      popupMenu.getMenu().add(0, i, 0, mainItem.getSubject());
    }
    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();

        int contentPosition = mTableOfContentsList.get(itemId).getPosition();
        mScrollView.smoothScrollToPosition(contentPosition);
        return true;
      }
    });
    popupMenu.show();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    mUnbinder.unbind();
  }

  public void setAdapterItems(ArrayList<MainItem> adapterItems) {
    MainAdapter adapter = (MainAdapter) mScrollView.getAdapter();
    adapter.setOnAdapterItemClickListener(this);
    adapter.setItems(adapterItems);
    adapter.refresh();
    mTableOfContentsList = getTableOfContentsList(adapterItems);
  }

  private List<MainItem> mTableOfContentsList;

  private List<MainItem> getTableOfContentsList(ArrayList<MainItem> mainItems) {
    ArrayList<MainItem> items = new ArrayList<>();
    for (MainItem mainItem : mainItems) {
      if (mainItem.getPosition() > -1) {
        items.add(mainItem);
      }
    }
    return items;
  }

  @BindView(R.id.loading_progressbar) ProgressBar mProgressBar;

  private void showProgress() {
    mProgressBar.setVisibility(View.VISIBLE);
    disableButton();
  }

  private void hideProgress() {
    mProgressBar.setVisibility(View.GONE);
    enableButton();
  }

  private void enableButton() {
    mScrollButton.setEnabled(true);
    mScaleButton.setEnabled(true);
  }

  private void disableButton() {
    mScrollButton.setEnabled(false);
    mScaleButton.setEnabled(false);
  }

  @OnClick({ R.id.scale_button, R.id.scrollview }) void onScaleClick() {
    Intent intent = new Intent(getApplicationContext(), PhotoViewActivity.class);
    intent.putExtra("url", mCurrentMainItem.getUrl());
    startActivity(intent);
  }

  @Override public void onItemClick(int position) {
    onScaleClick();
  }
}
