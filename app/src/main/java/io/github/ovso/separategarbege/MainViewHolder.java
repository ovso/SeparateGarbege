package io.github.ovso.separategarbege;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jaeho on 2017. 6. 25
 */

public class MainViewHolder extends RecyclerView.ViewHolder {
  @BindView(R.id.imageview) ImageView imageView;
  int position;
  OnAdapterItemClickListener onAdapterItemClickListener;
  public MainViewHolder(final View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  @OnClick(R.id.imageview) void onImageViewClick() {
    if (onAdapterItemClickListener != null) {
      onAdapterItemClickListener.onItemClick(position);
    }
  }

}