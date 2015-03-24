package com.codesters.recyclerviewsample;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
	@SuppressWarnings("unused")
	private static final String TAG = SelectableAdapter.class.getSimpleName();

	private SparseBooleanArray selectedItems;

	public SelectableAdapter() {
		selectedItems = new SparseBooleanArray();
	}

	public boolean isSelected(int position) {
		return getSelectedItems().contains(position);
	}

	public void toggleSelection(int position) {
		if (selectedItems.get(position, false)) {
			selectedItems.delete(position);
		} else {
			selectedItems.put(position, true);
		}
		notifyItemChanged(position);
	}

	public void clearSelection() {
		List<Integer> selection = getSelectedItems();
		selectedItems.clear();
		for (Integer i : selection) {
			notifyItemChanged(i);
		}
	}

	public int getSelectedItemCount() {
		return selectedItems.size();
	}

	public List<Integer> getSelectedItems() {
		List<Integer> items = new ArrayList<>(selectedItems.size());
		for (int i = 0; i < selectedItems.size(); ++i) {
			items.add(selectedItems.keyAt(i));
		}
		return items;
	}
}