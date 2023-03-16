package lib.kalu.leanback.presenter.bean;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class TvEpisodesPlusItemBean extends TvEpisodesGridItemBean implements Serializable {

    public TvEpisodesPlusItemBean() {
    }

    private int start;
    private int end;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
