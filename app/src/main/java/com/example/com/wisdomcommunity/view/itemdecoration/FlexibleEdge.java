package com.example.com.wisdomcommunity.view.itemdecoration;

public class FlexibleEdge {

    public boolean isHeaderRank;
    public boolean isHeaderRow;
    public boolean isFooterRank;
    public boolean isFooterRow;

    public FlexibleEdge(boolean isHeaderRank, boolean isHeaderRow, boolean isFooterRank, boolean isFooterRow) {
        this.isHeaderRank = isHeaderRank;
        this.isHeaderRow = isHeaderRow;
        this.isFooterRank = isFooterRank;
        this.isFooterRow = isFooterRow;
    }
}
