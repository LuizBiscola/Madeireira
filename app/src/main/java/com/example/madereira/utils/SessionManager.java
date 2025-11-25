package com.example.madereira.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "MadereiraSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_NAME = "userName";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Criar sessão de login
    public void createLoginSession(String userName) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_NAME, userName);
        editor.commit();
    }

    // Verificar se está logado
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Obter nome do usuário logado
    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "");
    }

     // Fazer logout (limpar sessão)
    public void logout() {
        editor.clear();
        editor.commit();
    }
}
