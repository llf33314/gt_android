package com.gt.gtapp.http.store;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public interface CookieStore {

    void add(String uri, List<Cookie> cookie);

    List<Cookie> get(String uri);

    List<Cookie> getCookies();

    boolean remove(String uri, Cookie cookie);

    boolean removeAll();

}
