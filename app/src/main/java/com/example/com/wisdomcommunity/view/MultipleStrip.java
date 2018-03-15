package com.example.com.wisdomcommunity.view;

public interface MultipleStrip {
    public void showLoading(boolean preventTouch);
    public void hideLoading();
    public boolean isLoading();

    public void showEmpty();
    public void hideEmpty();
    public boolean isEmpty();

    public void showError();
    public void hideError();
    public boolean isError();

    public void showLoadingOnly(boolean preventTouch);
    public void showEmptyOnly();
    public void showErrorOnly();

    public void showContentOnly();
}
