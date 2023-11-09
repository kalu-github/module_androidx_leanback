package lib.kalu.leanback.presenter.bean;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class TvPresenterRowBeanImpl  extends TvRadioGroupItemBean implements Serializable {

    private String rowTitle;

    @Override
    public String getRowTitle() {
        return rowTitle;
    }

    public void setRowTitle(String rowTitle) {
        this.rowTitle = rowTitle;
    }
}
