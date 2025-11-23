package com.example.madereira.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "MadereiraSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_TIPO_PERFIL = "userTipoPerfil";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    /**
     * Criar sessão de login
     */
    public void createLoginSession(int userId, String userName, String userEmail, String tipoPerfil) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_EMAIL, userEmail);
        editor.putString(KEY_USER_TIPO_PERFIL, tipoPerfil);
        editor.commit();
    }

    /**
     * Verificar se está logado
     */
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Obter ID do usuário logado
     */
    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    /**
     * Obter nome do usuário logado
     */
    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "");
    }

    /**
     * Obter email do usuário logado
     */
    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, "");
    }

    /**
     * Obter tipo de perfil do usuário
     */
    public String getUserTipoPerfil() {
        return prefs.getString(KEY_USER_TIPO_PERFIL, "Cliente");
    }

    /**
     * Fazer logout (limpar sessão)
     */
    public void logout() {
        editor.clear();
        editor.commit();
    }
}

