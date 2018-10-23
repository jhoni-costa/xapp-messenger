package br.com.jhonicosta.xapp_messenger.helper;

import android.util.Base64;

public class Base64Helper {

    public static String encode64(String txt) {
        return Base64.encodeToString(txt.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String decode64(String txt) {
        return new String(Base64.decode(txt, Base64.DEFAULT));
    }
}
