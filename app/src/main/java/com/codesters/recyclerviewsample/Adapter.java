package com.codesters.recyclerviewsample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Adapter extends SelectableAdapter<Adapter.ViewHolder> {

	private static final int ITEM_COUNT = 50;
	public static List<Item> items;

	private ViewHolder.ClickListener clickListener;

	public Adapter(ViewHolder.ClickListener clickListener) {
		super();
		this.clickListener = clickListener;

		// Create some items
		items = new ArrayList<>();
		for (int i = 0; i < ITEM_COUNT; ++i) {
			items.add(new Item("Item " + i, "This is the item number " + i));
		}
	}

	public void removeItem(int position) {
		items.remove(position);
		notifyItemRemoved(position);
	}

	public void removeItems(List<Integer> positions) {
		// Reverse-sort the list
		Collections.sort(positions, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return rhs - lhs;
            }
        });

		// Split the list in ranges
		while (!positions.isEmpty()) {
			if (positions.size() == 1) {
				removeItem(positions.get(0));
				positions.remove(0);
			} else {
				int count = 1;
				while (positions.size() > count && positions.get(count).equals(positions.get(count - 1) - 1)) {
					++count;
				}

				if (count == 1) {
					removeItem(positions.get(0));
				} else {
					removeRange(positions.get(count - 1), count);
				}

				for (int i = 0; i < count; ++i) {
					positions.remove(0);
				}
			}
		}
	}

	private void removeRange(int positionStart, int itemCount) {
		for (int i = 0; i < itemCount; ++i) {
			items.remove(positionStart);
		}
		notifyItemRangeRemoved(positionStart, itemCount);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
		return new ViewHolder(v, clickListener);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final Item item = items.get(position);

		holder.title.setText(item.getTitle());
		holder.subtitle.setText(item.getSubtitle());

		// Highlight the item if it's selected
		holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

    public static Item getItem(int i) {
        return items.get(i);
    }

	public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
			View.OnLongClickListener {

		TextView title;
		TextView subtitle;
		View selectedOverlay;

		private ClickListener listener;

		public ViewHolder(View itemView, ClickListener listener) {
			super(itemView);

			title = (TextView) itemView.findViewById(R.id.title);
			subtitle = (TextView) itemView.findViewById(R.id.subtitle);
			selectedOverlay = itemView.findViewById(R.id.selected_overlay);

			this.listener = listener;

			itemView.setOnClickListener(this);
			itemView.setOnLongClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (listener != null) {
				listener.onItemClicked(getPosition());
			}
		}

		@Override
		public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked(getPosition());
            }
            return false;
        }

		public interface ClickListener {
			public void onItemClicked(int position);
			public boolean onItemLongClicked(int position);
		}
	}
}