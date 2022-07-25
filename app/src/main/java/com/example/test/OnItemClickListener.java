package com.example.test;

import android.view.View;

public interface OnItemClickListener<T> {
    void onItemClicked(View view, T data);
}