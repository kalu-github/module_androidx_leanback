package lib.kalu.leanback.presenter.bean;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class TvEpisodesPlusItemBean extends TvEpisodesGridItemBean implements Serializable {

    public TvEpisodesPlusItemBean() {
    }

    private int rangeStart;
    private int rangeEnd;
    private int rangeIndex;
    private int rangeMax;
    private int episodeIndex;
    private int episodeMax;

    public int getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(int rangeStart) {
        this.rangeStart = rangeStart;
    }

    public int getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(int rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public int getRangeIndex() {
        return rangeIndex;
    }

    public void setRangeIndex(int rangeIndex) {
        this.rangeIndex = rangeIndex;
    }

    public int getRangeMax() {
        return rangeMax;
    }

    public void setRangeMax(int rangeMax) {
        this.rangeMax = rangeMax;
    }

    public int getEpisodeIndex() {
        return episodeIndex;
    }

    public void setEpisodeIndex(int episodeIndex) {
        this.episodeIndex = episodeIndex;
    }

    public int getEpisodeMax() {
        return episodeMax;
    }

    public void setEpisodeMax(int episodeMax) {
        this.episodeMax = episodeMax;
    }
}
