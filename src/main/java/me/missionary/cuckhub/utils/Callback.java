/*
 * Copyright Missionary (c) 2017. Unauthorized use of this work can and will result in further action against the unauthorized user(s).
 */
package me.missionary.cuckhub.utils;

/**
 * Created by Missionary (missionarymc@gmail.com) on 5/28/2017.
 */
public interface Callback<T> {
    /**
     * Takes an object and runs it.
     * @param t
     */
    void run(T t);
}
