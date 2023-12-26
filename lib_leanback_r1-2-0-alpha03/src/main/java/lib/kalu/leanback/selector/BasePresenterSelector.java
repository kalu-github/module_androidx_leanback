package lib.kalu.leanback.selector;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;

import java.util.ArrayList;

public abstract class BasePresenterSelector extends PresenterSelector {

    private ArrayList<Presenter> mPresenters = new ArrayList<>();
    private ArrayMap<Class<?>, ArrayMap<Class<?>, Presenter>> mClassMap = new ArrayMap<>();
    private ArrayMap<Class<?>, Presenter> mClassSingleMap = new ArrayMap<>();

    /*******/

    /*******/


    public BasePresenterSelector() {
        init();
    }

    /**
     * 定制Presenter
     *
     * @param cls
     * @param presenter
     */
    protected void addPresenterCustom(@NonNull Class<?> cls, @NonNull Presenter presenter) {
        mClassSingleMap.put(cls, presenter);
        if (!mPresenters.contains(presenter)) {
            mPresenters.add(presenter);
        }
    }

    protected abstract void init();

    @Override
    public Presenter getPresenter(Object item) {
        Class<?> cls = item.getClass();
        Presenter presenter;
        presenter = mClassSingleMap.get(cls);
        if (presenter != null) {
            return presenter;
        }
        ArrayMap<Class<?>, Presenter> presenters = mClassMap.get(cls);
        if (presenters != null) {
            if (presenters.size() == 1) {
                return presenters.valueAt(0);
            } else if (presenters.size() > 1) {
                throw new NullPointerException("presenter == null, please add presenter to PresenterSelector");
            }
        }
        if (presenter == null) {
            throw new NullPointerException("presenter == null, please add presenter to PresenterSelector");
        }
        return presenter;
    }

    @Override
    public Presenter[] getPresenters() {
        return mPresenters.toArray(new Presenter[0]);
    }
}