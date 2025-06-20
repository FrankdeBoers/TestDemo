package com.frank.newapplication.startup;

import android.content.Context;

import java.util.List;

public interface StartUp<T> {
    T create();

    List<Class<? extends StartUp<?>>> dependencies();

    int getDependenciesCount();
}
