package io.github.ovso.separategarbege;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;

/**
 * Created by jaeho on 2017. 6. 25
 */

public class MainAdapter extends RecyclerView.Adapter<MainViewHolder> {

  private List<MainItem> items;

  public MainAdapter() {
    items = new ArrayList<>();
  }

  public void setItems(List<MainItem> items) {
    this.items = items;
  }

  public void refresh() {
    notifyDataSetChanged();
  }

  @Override public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new MainViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, null));
  }

  @Override public void onBindViewHolder(MainViewHolder holder, int position) {
    holder.position = position;
    holder.onAdapterItemClickListener = this.onAdapterItemClickListener;
    String url = items.get(position).getUrl();
    Glide.with(holder.imageView.getContext()).load(url).into(holder.imageView);
  }

  @Override public int getItemCount() {
    return items.size();
  }

  public MainItem getItem(int position) {
    return items.get(position);
  }

  @Setter private OnAdapterItemClickListener onAdapterItemClickListener;
}
