package androidx.leanback.widget;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

@SuppressLint({"ListenerInterface"})
public abstract class OnChildViewHolderSelectedListener
{
  public void onChildViewHolderSelected(@NonNull RecyclerView parent, @Nullable RecyclerView.ViewHolder child, int position, int subposition) {}
  
  public void onChildViewHolderSelectedAndPositioned(@NonNull RecyclerView parent, @Nullable RecyclerView.ViewHolder child, int position, int subposition) {}
}


/* Location:           D:\Android\Huan_Component\androidx_aar_leanback\repo\classes.jar
 * Qualified Name:     androidx.leanback.widget.OnChildViewHolderSelectedListener
 * JD-Core Version:    0.7.0.1
 */