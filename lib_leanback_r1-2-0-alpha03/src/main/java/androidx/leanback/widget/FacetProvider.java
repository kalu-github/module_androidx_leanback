package androidx.leanback.widget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract interface FacetProvider
{
  @Nullable
  public abstract Object getFacet(@NonNull Class<?> paramClass);
}


/* Location:           D:\Android\Huan_Component\androidx_aar_leanback\repo\classes.jar
 * Qualified Name:     androidx.leanback.widget.FacetProvider
 * JD-Core Version:    0.7.0.1
 */