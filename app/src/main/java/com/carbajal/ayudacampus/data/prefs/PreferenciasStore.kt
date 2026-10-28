package com.carbajal.ayudacampus.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferenciasStore(private val context: Context) {

    companion object {
        val PERFIL_ACTIVO = stringPreferencesKey("perfil_activo") // "CLIENTE" | "ADMIN" | ""
        val USUARIO_ID = intPreferencesKey("usuario_id")
        val MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
        val NOTIFICACIONES = booleanPreferencesKey("notificaciones")
        val RECIBIR_TICKETS = booleanPreferencesKey("recibir_tickets")
    }

    val perfilActivo: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PERFIL_ACTIVO] ?: ""
    }
    val usuarioId: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[USUARIO_ID] ?: 0
    }
    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[MODO_OSCURO] ?: false
    }
    val notificaciones: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[claveNotificaciones(prefs)] ?: prefs[NOTIFICACIONES] ?: true
    }
    val recibirTickets: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[RECIBIR_TICKETS] ?: true // Por defecto true
    }

    private fun claveNotificaciones(prefs: Preferences) = booleanPreferencesKey(
        "notificaciones_${prefs[PERFIL_ACTIVO].orEmpty()}_${prefs[USUARIO_ID] ?: 0}"
    )

    suspend fun guardarSesion(rol: String, id: Int) {
        context.dataStore.edit { prefs ->
            prefs[PERFIL_ACTIVO] = rol
            prefs[USUARIO_ID] = id
        }
    }

    suspend fun guardarModoOscuro(activado: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[MODO_OSCURO] = activado
        }
    }

    suspend fun guardarNotificaciones(activadas: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[claveNotificaciones(prefs)] = activadas
        }
    }

    suspend fun guardarRecibirTickets(recibir: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[RECIBIR_TICKETS] = recibir
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { prefs ->
            prefs[PERFIL_ACTIVO] = ""
            prefs[USUARIO_ID] = 0
        }
    }
}