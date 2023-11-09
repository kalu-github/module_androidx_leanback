package lib.kalu.leanback.presenter.bean;

import java.io.Serializable;

public class TvRadioGroupItemBean implements TvPresenterRowBean, Serializable {

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String getRowTitle() {
        return null;
    }
}
