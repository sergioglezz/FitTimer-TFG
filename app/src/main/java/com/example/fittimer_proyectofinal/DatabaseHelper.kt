package com.example.fittimer_proyectofinal

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 4 // Incrementa la versión de la base de datos
        private const val DATABASE_NAME = "FitTimer"
        private const val TABLE_USUARIOS = "usuarios"
        private const val TABLE_PESOS = "pesos"
        private const val TABLE_ICONOS = "iconos"
        private const val KEY_ID = "id"
        private const val KEY_NOMBRE = "nombre"
        private const val KEY_CONTRASEÑA = "contraseña"
        private const val KEY_USUARIO_ID = "usuario_id"
        private const val KEY_PESO = "peso"
        private const val KEY_IMC = "imc"
        private const val KEY_DIA = "dia"
        private const val KEY_ICONO = "icono"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsuariosTable = ("CREATE TABLE $TABLE_USUARIOS("
                + "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$KEY_NOMBRE TEXT,"
                + "$KEY_CONTRASEÑA TEXT)")
        db.execSQL(createUsuariosTable)

        val createPesosTable = ("CREATE TABLE $TABLE_PESOS("
                + "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$KEY_USUARIO_ID INTEGER,"
                + "$KEY_PESO REAL,"
                + "$KEY_IMC REAL,"
                + "FOREIGN KEY($KEY_USUARIO_ID) REFERENCES $TABLE_USUARIOS($KEY_ID))")
        db.execSQL(createPesosTable)

        val createIconosTable = ("CREATE TABLE $TABLE_ICONOS("
                + "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$KEY_USUARIO_ID INTEGER,"
                + "$KEY_DIA TEXT,"
                + "$KEY_ICONO TEXT,"
                + "FOREIGN KEY($KEY_USUARIO_ID) REFERENCES $TABLE_USUARIOS($KEY_ID))")
        db.execSQL(createIconosTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PESOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ICONOS")
        onCreate(db)
    }

    fun addUsuario(nombre: String, contraseña: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NOMBRE, nombre)
            put(KEY_CONTRASEÑA, contraseña)
        }
        db.insert(TABLE_USUARIOS, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getUsuarioId(nombre: String): Int? {
        val db = this.readableDatabase
        val selectQuery = "SELECT $KEY_ID FROM $TABLE_USUARIOS WHERE $KEY_NOMBRE = ?"
        db.rawQuery(selectQuery, arrayOf(nombre)).use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex(KEY_ID))
            }
        }
        return null
    }

    fun addPeso(nombre: String, peso: Double, imc: Double) {
        val usuarioId = getUsuarioId(nombre)
        if (usuarioId != null) {
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(KEY_USUARIO_ID, usuarioId)
                put(KEY_PESO, peso)
                put(KEY_IMC, imc)
            }
            db.insert(TABLE_PESOS, null, values)
            db.close()
        }
    }

    @SuppressLint("Range")
    fun getUltimoPeso(nombre: String): Double? {
        val usuarioId = getUsuarioId(nombre)
        if (usuarioId != null) {
            val db = this.readableDatabase
            val selectQuery = "SELECT $KEY_PESO FROM $TABLE_PESOS WHERE $KEY_USUARIO_ID = ? ORDER BY $KEY_ID DESC LIMIT 1"
            db.rawQuery(selectQuery, arrayOf(usuarioId.toString())).use { cursor ->
                if (cursor.moveToFirst()) {
                    return cursor.getDouble(cursor.getColumnIndex(KEY_PESO))
                }
            }
        }
        return null
    }

    @SuppressLint("Range")
    fun getUltimoIMC(nombre: String): Double? {
        val usuarioId = getUsuarioId(nombre)
        if (usuarioId != null) {
            val db = this.readableDatabase
            val selectQuery = "SELECT $KEY_IMC FROM $TABLE_PESOS WHERE $KEY_USUARIO_ID = ? ORDER BY $KEY_ID DESC LIMIT 1"
            db.rawQuery(selectQuery, arrayOf(usuarioId.toString())).use { cursor ->
                if (cursor.moveToFirst()) {
                    return cursor.getDouble(cursor.getColumnIndex(KEY_IMC))
                }
            }
        }
        return null
    }

    @SuppressLint("Range")
    fun getPesos(nombre: String): List<Double>? {
        val usuarioId = getUsuarioId(nombre)
        val pesos = mutableListOf<Double>()
        if (usuarioId != null) {
            val db = this.readableDatabase
            val selectQuery = "SELECT $KEY_PESO FROM $TABLE_PESOS WHERE $KEY_USUARIO_ID = ? ORDER BY $KEY_ID ASC"
            db.rawQuery(selectQuery, arrayOf(usuarioId.toString())).use { cursor ->
                while (cursor.moveToNext()) {
                    pesos.add(cursor.getDouble(cursor.getColumnIndex(KEY_PESO)))
                }
            }
        }
        return if (pesos.isNotEmpty()) pesos else null
    }

    fun addIcono(nombre: String, dia: String, icono: String) {
        val usuarioId = getUsuarioId(nombre)
        if (usuarioId != null) {
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(KEY_USUARIO_ID, usuarioId)
                put(KEY_DIA, dia)
                put(KEY_ICONO, icono)
            }
            db.insert(TABLE_ICONOS, null, values)
            db.close()
        }
    }

    fun clearIconos(nombre: String) {
        val usuarioId = getUsuarioId(nombre)
        if (usuarioId != null) {
            val db = this.writableDatabase
            db.delete(TABLE_ICONOS, "$KEY_USUARIO_ID = ?", arrayOf(usuarioId.toString()))
            db.close()
        }
    }

    @SuppressLint("Range")
    fun getContrasenaHasheadaPorNombre(nombre: String): String? {
        val db = this.readableDatabase
        val selectQuery = "SELECT $KEY_CONTRASEÑA FROM $TABLE_USUARIOS WHERE $KEY_NOMBRE = ?"
        db.rawQuery(selectQuery, arrayOf(nombre)).use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(KEY_CONTRASEÑA))
            }
        }
        return null
    }

    @SuppressLint("Range")
    fun getAllUsuarios(): MutableList<String> {
        val list: MutableList<String> = ArrayList()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_USUARIOS"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOMBRE))
                val contraseña = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CONTRASEÑA))

                val usuarios = "$nombre - $contraseña"
                list.add(usuarios)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    @SuppressLint("Range")
    fun getIconos(nombre: String): Map<String, String>? {
        val usuarioId = getUsuarioId(nombre)
        val iconos = mutableMapOf<String, String>()
        if (usuarioId != null) {
            val db = this.readableDatabase
            val selectQuery = "SELECT $KEY_DIA, $KEY_ICONO FROM $TABLE_ICONOS WHERE $KEY_USUARIO_ID = ?"
            db.rawQuery(selectQuery, arrayOf(usuarioId.toString())).use { cursor ->
                while (cursor.moveToNext()) {
                    val dia = cursor.getString(cursor.getColumnIndex(KEY_DIA))
                    val icono = cursor.getString(cursor.getColumnIndex(KEY_ICONO))
                    iconos[dia] = icono
                }
            }
        }
        return if (iconos.isNotEmpty()) iconos else null
    }

    fun deleteAllUsuarios() {
        val db = this.writableDatabase
        db.delete(TABLE_USUARIOS, null, null)
        db.close()
    }
}
