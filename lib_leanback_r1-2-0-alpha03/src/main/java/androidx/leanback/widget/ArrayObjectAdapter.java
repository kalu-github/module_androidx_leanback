/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package androidx.leanback.widget;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * An {@link ObjectAdapter} implemented with an {@link ArrayList}.
 */
public class ArrayObjectAdapter extends ObjectAdapter {

    private static final Boolean DEBUG = false;
    private static final String TAG = "ArrayObjectAdapter";
    // To compute the payload correctly, we should use a temporary list to hold all the old items.
    @SuppressWarnings("WeakerAccess") /* synthetic access */
    final List<Object> mOldItems = new ArrayList<>();
    private final List<Object> mItems = new ArrayList<>();
    ListUpdateCallback mListUpdateCallback;
    // Un modifiable version of mItems;
    private List<?> mUnmodifiableItems;

    /**
     * Constructs an adapter with the given {@link PresenterSelector}.
     */
    public ArrayObjectAdapter(@NonNull PresenterSelector presenterSelector) {
        super(presenterSelector);
    }

    /**
     * Constructs an adapter that uses the given {@link Presenter} for all items.
     */
    public ArrayObjectAdapter(@NonNull Presenter presenter) {
        super(presenter);
    }

    /**
     * Constructs an adapter.
     */
    public ArrayObjectAdapter() {
        super();
    }

    @Override
    public int size() {
        return mItems.size();
    }

    @Nullable
    @Override
    public Object get(int index) {
        return mItems.get(index);
    }

    /**
     * Returns the index for the first occurrence of item in the adapter, or -1 if
     * not found.
     *
     * @param item The item to find in the list.
     * @return Index of the first occurrence of the item in the adapter, or -1
     * if not found.
     */
    public int indexOf(@NonNull Object item) {
        return mItems.indexOf(item);
    }

    /**
     * Notify that the content of a range of items changed. Note that this is
     * not same as items being added or removed.
     *
     * @param positionStart The position of first item that has changed.
     * @param itemCount     The count of how many items have changed.
     */
    public void notifyArrayItemRangeChanged(int positionStart, int itemCount) {
        notifyItemRangeChanged(positionStart, itemCount);
    }

    /**
     * Adds an item to the end of the adapter.
     *
     * @param item The item to add to the end of the adapter.
     */
    public void add(@NonNull Object item) {
        add(mItems.size(), item, false);
    }

    public void add(@NonNull Object item, boolean notifyUI) {
        add(mItems.size(), item, notifyUI);
    }

    public void add(int index, @NonNull Object item, @NonNull boolean notifyUI) {
        mItems.add(index, item);
        if (!notifyUI)
            return;
        notifyItemRangeInserted(index, 1);
    }

    public void addAll(int index, @NonNull Collection<?> items) {
        addAll(index, items, false);
    }

    public void addAll(int index, @NonNull Collection<?> items, @NonNull boolean notifyUI) {
        int itemsCount = items.size();
        if (itemsCount == 0) {
            return;
        }
        mItems.addAll(index, items);
        if (!notifyUI)
            return;
        notifyItemRangeInserted(index, itemsCount);
    }

    public boolean remove(@NonNull Object item) {
        return remove(item, false);
    }

    public boolean remove(@NonNull Object item, @NonNull boolean notifyUI) {
        int index = mItems.indexOf(item);
        if (index >= 0) {
            mItems.remove(index);
            if (notifyUI) {
                notifyItemRangeRemoved(index, 1);
            }
        }
        return index >= 0;
    }

    public void move(int fromPosition, int toPosition) {
        move(fromPosition, toPosition, false);
    }

    public void move(int fromPosition, int toPosition, @NonNull boolean notifyUI) {
        if (fromPosition == toPosition) {
            // no-op
            return;
        }
        Object item = mItems.remove(fromPosition);
        mItems.add(toPosition, item);
        if (!notifyUI)
            return;
        notifyItemMoved(fromPosition, toPosition);
    }

    public void replace(int position, @NonNull Object item) {
        replace(position, item, false);
    }

    public void replace(int position, @NonNull Object item, @NonNull boolean notifyUI) {
        mItems.set(position, item);
        if (!notifyUI)
            return;
        notifyItemRangeChanged(position, 1);
    }

    public int removeItems(int position, int count) {
        return removeItems(position, count, false);
    }

    public int removeItems(int position, int count, @NonNull boolean notifyUI) {
        int itemsToRemove = Math.min(count, mItems.size() - position);
        if (itemsToRemove <= 0) {
            return 0;
        }

        for (int i = 0; i < itemsToRemove; i++) {
            mItems.remove(position);
        }
        if (notifyUI) {
            notifyItemRangeRemoved(position, itemsToRemove);
        }
        return itemsToRemove;
    }

    public void clear() {
        clear(false);
    }

    public void clear(@NonNull boolean notifyUI) {
        int itemCount = mItems.size();
        if (itemCount == 0) {
            return;
        }
        mItems.clear();
        if (!notifyUI)
            return;
        notifyItemRangeRemoved(0, itemCount);
    }

    /**
     * Gets a read-only view of the list of object of this ArrayObjectAdapter.
     */
    @NonNull
    @SuppressWarnings("unchecked")
    public <E> List<E> unmodifiableList() {

        // The mUnmodifiableItems will only be created once as long as the content of mItems has not
        // been changed.
        if (mUnmodifiableItems == null) {
            mUnmodifiableItems = Collections.unmodifiableList(mItems);
        }
        return (List<E>) mUnmodifiableItems;
    }

    @Override
    public boolean isImmediateNotifySupported() {
        return true;
    }

    /**
     * Set a new item list to adapter. The DiffUtil will compute the difference and dispatch it to
     * specified position.
     *
     * @param itemList List of new Items
     * @param callback Optional DiffCallback Object to compute the difference between the old data
     *                 set and new data set. When null, {@link #notifyChanged()} will be fired.
     */
    @SuppressWarnings("unchecked")
    public void setItems(final @NonNull List itemList, final @Nullable DiffCallback callback) {
        if (callback == null) {
            // shortcut when DiffCallback is not provided
            mItems.clear();
            mItems.addAll(itemList);
            notifyChanged();
            return;
        }
        mOldItems.clear();
        mOldItems.addAll(mItems);

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return mOldItems.size();
            }

            @Override
            public int getNewListSize() {
                return itemList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return callback.areItemsTheSame(mOldItems.get(oldItemPosition),
                        itemList.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return callback.areContentsTheSame(mOldItems.get(oldItemPosition),
                        itemList.get(newItemPosition));
            }

            @Nullable
            @Override
            public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                return callback.getChangePayload(mOldItems.get(oldItemPosition),
                        itemList.get(newItemPosition));
            }
        });

        // update items.
        mItems.clear();
        mItems.addAll(itemList);

        // dispatch diff result
        if (mListUpdateCallback == null) {
            mListUpdateCallback = new ListUpdateCallback() {

                @Override
                public void onInserted(int position, int count) {
                    if (DEBUG) {
                        Log.d(TAG, "onInserted");
                    }
                    notifyItemRangeInserted(position, count);
                }

                @Override
                public void onRemoved(int position, int count) {
                    if (DEBUG) {
                        Log.d(TAG, "onRemoved");
                    }
                    notifyItemRangeRemoved(position, count);
                }

                @Override
                public void onMoved(int fromPosition, int toPosition) {
                    if (DEBUG) {
                        Log.d(TAG, "onMoved");
                    }
                    notifyItemMoved(fromPosition, toPosition);
                }

                @Override
                public void onChanged(int position, int count, Object payload) {
                    if (DEBUG) {
                        Log.d(TAG, "onChanged");
                    }
                    notifyItemRangeChanged(position, count, payload);
                }
            };
        }
        diffResult.dispatchUpdatesTo(mListUpdateCallback);
        mOldItems.clear();
    }
}
