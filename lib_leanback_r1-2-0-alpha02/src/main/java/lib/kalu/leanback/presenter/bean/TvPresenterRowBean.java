package lib.kalu.leanback.presenter.bean;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public interface TvPresenterRowBean extends Serializable {
    String getRowTitle();
}
