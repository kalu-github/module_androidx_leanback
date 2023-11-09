//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package androidx.leanback.widget;

final class WindowAlignment {
    private int mOrientation = 0;
    public final Axis vertical = new Axis("vertical");
    public final Axis horizontal = new Axis("horizontal");
    private Axis mMainAxis;
    private Axis mSecondAxis;

    WindowAlignment() {
        this.mMainAxis = this.horizontal;
        this.mSecondAxis = this.vertical;
    }

    public Axis mainAxis() {
        return this.mMainAxis;
    }

    public Axis secondAxis() {
        return this.mSecondAxis;
    }

    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
        if (this.mOrientation == 0) {
            this.mMainAxis = this.horizontal;
            this.mSecondAxis = this.vertical;
        } else {
            this.mMainAxis = this.vertical;
            this.mSecondAxis = this.horizontal;
        }

    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public void reset() {
        this.mainAxis().reset();
    }

    public String toString() {
        return "horizontal=" + this.horizontal + "; vertical=" + this.vertical;
    }

    static final class Axis {
        private static final int PF_KEYLINE_OVER_LOW_EDGE = 1;
        private static final int PF_KEYLINE_OVER_HIGH_EDGE = 2;
        private int mMaxEdge;
        private int mMinEdge;
        private int mMaxScroll;
        private int mMinScroll;
        private int mPreferredKeyLine = 2;
        private int mWindowAlignment = 3;
        private int mWindowAlignmentOffset = 0;
        private float mWindowAlignmentOffsetPercent = 50.0F;
        private int mSize;
        private int mPaddingMin;
        private int mPaddingMax;
        private boolean mReversedFlow;

        Axis(String name) {
            this.reset();
        }

        public int getWindowAlignment() {
            return this.mWindowAlignment;
        }

        public void setWindowAlignment(int windowAlignment) {
            this.mWindowAlignment = windowAlignment;
        }

        void setPreferKeylineOverLowEdge(boolean keylineOverLowEdge) {
            this.mPreferredKeyLine = keylineOverLowEdge ? this.mPreferredKeyLine | 1 : this.mPreferredKeyLine & -2;
        }

        void setPreferKeylineOverHighEdge(boolean keylineOverHighEdge) {
            this.mPreferredKeyLine = keylineOverHighEdge ? this.mPreferredKeyLine | 2 : this.mPreferredKeyLine & -3;
        }

        boolean isPreferKeylineOverHighEdge() {
            return (this.mPreferredKeyLine & 2) != 0;
        }

        boolean isPreferKeylineOverLowEdge() {
            return (this.mPreferredKeyLine & 1) != 0;
        }

        public int getWindowAlignmentOffset() {
            return this.mWindowAlignmentOffset;
        }

        public void setWindowAlignmentOffset(int offset) {
            this.mWindowAlignmentOffset = offset;
        }

        public void setWindowAlignmentOffsetPercent(float percent) {
            if ((percent < 0.0F || percent > 100.0F) && percent != -1.0F) {
                throw new IllegalArgumentException();
            } else {
                this.mWindowAlignmentOffsetPercent = percent;
            }
        }

        public float getWindowAlignmentOffsetPercent() {
            return this.mWindowAlignmentOffsetPercent;
        }

        public int getMinScroll() {
            return this.mMinScroll;
        }

        public void invalidateScrollMin() {
            this.mMinEdge = Integer.MIN_VALUE;
            this.mMinScroll = Integer.MIN_VALUE;
        }

        public int getMaxScroll() {
            return this.mMaxScroll;
        }

        public void invalidateScrollMax() {
            this.mMaxEdge = Integer.MAX_VALUE;
            this.mMaxScroll = Integer.MAX_VALUE;
        }

        void reset() {
            this.mMinEdge = Integer.MIN_VALUE;
            this.mMaxEdge = Integer.MAX_VALUE;
        }

        public boolean isMinUnknown() {
            return this.mMinEdge == Integer.MIN_VALUE;
        }

        public boolean isMaxUnknown() {
            return this.mMaxEdge == Integer.MAX_VALUE;
        }

        public void setSize(int size) {
            this.mSize = size;
        }

        public int getSize() {
            return this.mSize;
        }

        public void setPadding(int paddingMin, int paddingMax) {
            this.mPaddingMin = paddingMin;
            this.mPaddingMax = paddingMax;
        }

        public int getPaddingMin() {
            return this.mPaddingMin;
        }

        public int getPaddingMax() {
            return this.mPaddingMax;
        }

        public int getClientSize() {
            return this.mSize - this.mPaddingMin - this.mPaddingMax;
        }

        int calculateKeyline() {
            int keyLine;
            if (!this.mReversedFlow) {
                if (this.mWindowAlignmentOffset >= 0) {
                    keyLine = this.mWindowAlignmentOffset;
                } else {
                    keyLine = this.mSize + this.mWindowAlignmentOffset;
                }

                if (this.mWindowAlignmentOffsetPercent != -1.0F) {
                    keyLine += (int)((float)this.mSize * this.mWindowAlignmentOffsetPercent / 100.0F);
                }
            } else {
                if (this.mWindowAlignmentOffset >= 0) {
                    keyLine = this.mSize - this.mWindowAlignmentOffset;
                } else {
                    keyLine = -this.mWindowAlignmentOffset;
                }

                if (this.mWindowAlignmentOffsetPercent != -1.0F) {
                    keyLine -= (int)((float)this.mSize * this.mWindowAlignmentOffsetPercent / 100.0F);
                }
            }

            return keyLine;
        }

        int calculateScrollToKeyLine(int viewCenterPosition, int keyLine) {
            return viewCenterPosition - keyLine;
        }

        public void updateMinMax(int minEdge, int maxEdge, int minChildViewCenter, int maxChildViewCenter) {
            this.mMinEdge = minEdge;
            this.mMaxEdge = maxEdge;
            int clientSize = this.getClientSize();
            int keyLine = this.calculateKeyline();
            boolean isMinUnknown = this.isMinUnknown();
            boolean isMaxUnknown = this.isMaxUnknown();
            if (!isMinUnknown) {
                label69: {
                    label68: {
                        if (!this.mReversedFlow) {
                            if ((this.mWindowAlignment & 1) != 0) {
                                break label68;
                            }
                        } else if ((this.mWindowAlignment & 2) != 0) {
                            break label68;
                        }

                        this.mMinScroll = this.calculateScrollToKeyLine(minChildViewCenter, keyLine);
                        break label69;
                    }

                    this.mMinScroll = this.mMinEdge - this.mPaddingMin;
                }
            }

            if (!isMaxUnknown) {
                label60: {
                    label59: {
                        if (!this.mReversedFlow) {
                            if ((this.mWindowAlignment & 2) != 0) {
                                break label59;
                            }
                        } else if ((this.mWindowAlignment & 1) != 0) {
                            break label59;
                        }

                        this.mMaxScroll = this.calculateScrollToKeyLine(maxChildViewCenter, keyLine);
                        break label60;
                    }

                    this.mMaxScroll = this.mMaxEdge - this.mPaddingMin - clientSize;
                }
            }

            if (!isMaxUnknown && !isMinUnknown) {
                if (!this.mReversedFlow) {
                    if ((this.mWindowAlignment & 1) != 0) {
                        if (this.isPreferKeylineOverLowEdge()) {
                            this.mMinScroll = Math.min(this.mMinScroll, this.calculateScrollToKeyLine(maxChildViewCenter, keyLine));
                        }

                        this.mMaxScroll = Math.max(this.mMinScroll, this.mMaxScroll);
                    } else if ((this.mWindowAlignment & 2) != 0) {
                        if (this.isPreferKeylineOverHighEdge()) {
                            this.mMaxScroll = Math.max(this.mMaxScroll, this.calculateScrollToKeyLine(minChildViewCenter, keyLine));
                        }

                        this.mMinScroll = Math.min(this.mMinScroll, this.mMaxScroll);
                    }
                } else if ((this.mWindowAlignment & 1) != 0) {
                    if (this.isPreferKeylineOverLowEdge()) {
                        this.mMaxScroll = Math.max(this.mMaxScroll, this.calculateScrollToKeyLine(minChildViewCenter, keyLine));
                    }

                    this.mMinScroll = Math.min(this.mMinScroll, this.mMaxScroll);
                } else if ((this.mWindowAlignment & 2) != 0) {
                    if (this.isPreferKeylineOverHighEdge()) {
                        this.mMinScroll = Math.min(this.mMinScroll, this.calculateScrollToKeyLine(maxChildViewCenter, keyLine));
                    }

                    this.mMaxScroll = Math.max(this.mMinScroll, this.mMaxScroll);
                }
            }

        }

        public int getScroll(int viewCenter) {
            int size = this.getSize();
            int keyLine = this.calculateKeyline();
            boolean isMinUnknown = this.isMinUnknown();
            boolean isMaxUnknown = this.isMaxUnknown();
            int keyLineToMaxEdge;
            int alignToMax;
            if (!isMinUnknown) {
                label44: {
                    keyLineToMaxEdge = keyLine - this.mPaddingMin;
                    if (!this.mReversedFlow) {
                        if ((this.mWindowAlignment & 1) == 0) {
                            break label44;
                        }
                    } else if ((this.mWindowAlignment & 2) == 0) {
                        break label44;
                    }

                    if (viewCenter - this.mMinEdge <= keyLineToMaxEdge) {
                        alignToMax = this.mMinEdge - this.mPaddingMin;
                        if (!isMaxUnknown && alignToMax > this.mMaxScroll) {
                            alignToMax = this.mMaxScroll;
                        }

                        return alignToMax;
                    }
                }
            }

            if (!isMaxUnknown) {
                keyLineToMaxEdge = size - keyLine - this.mPaddingMax;
                if (!this.mReversedFlow) {
                    if ((this.mWindowAlignment & 2) == 0) {
                        return this.calculateScrollToKeyLine(viewCenter, keyLine);
                    }
                } else if ((this.mWindowAlignment & 1) == 0) {
                    return this.calculateScrollToKeyLine(viewCenter, keyLine);
                }

                if (this.mMaxEdge - viewCenter <= keyLineToMaxEdge) {
                    alignToMax = this.mMaxEdge - (size - this.mPaddingMax);
                    if (!isMinUnknown && alignToMax < this.mMinScroll) {
                        alignToMax = this.mMinScroll;
                    }

                    return alignToMax;
                }
            }

            return this.calculateScrollToKeyLine(viewCenter, keyLine);
        }

        public void setReversedFlow(boolean reversedFlow) {
            this.mReversedFlow = reversedFlow;
        }

        public String toString() {
            return " min:" + this.mMinEdge + " " + this.mMinScroll + " max:" + this.mMaxEdge + " " + this.mMaxScroll;
        }
    }
}
