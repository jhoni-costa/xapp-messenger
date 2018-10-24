package br.com.jhonicosta.xapp_messenger.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissions {

    public static boolean validarPermissions(String[] permissoes, Activity activity, int requestCode) {

        if (Build.VERSION.SDK_INT >= 23) {
            List<String> lista = new ArrayList<>();

            for (String permissao : permissoes) {
                boolean temPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if (!temPermissao) {
                    lista.add(permissao);
                }
            }
            if (lista.isEmpty()) {
                return true;
            }
            String[] array = new String[lista.size()];
            lista.toArray(array);
            ActivityCompat.requestPermissions(activity, array, requestCode);
        }

        return true;
    }
}
