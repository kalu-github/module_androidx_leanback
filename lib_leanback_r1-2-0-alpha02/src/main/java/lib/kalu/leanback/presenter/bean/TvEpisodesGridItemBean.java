package lib.kalu.leanback.presenter.bean;

import android.widget.LinearLayout;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class TvEpisodesGridItemBean extends TvPresenterRowBeanImpl implements Serializable {

    public TvEpisodesGridItemBean() {
    }

    private boolean checked = false; // 是否正在选中
    private boolean playing = false; // 是否正在播放
    private boolean focus = false; // 是否有焦点

    private int episodeIndex = -1;
    private int episodeMax = -1;

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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }
}
