package com.carbajal.ayudacampus.data.local

import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.carbajal.ayudacampus.data.local.dao.SoporteDao
import com.carbajal.ayudacampus.data.local.entity.CitaAtencion
import com.carbajal.ayudacampus.data.local.entity.ActividadTicket
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.carbajal.ayudacampus.data.local.dao.CategoriaDao
import com.carbajal.ayudacampus.data.local.dao.TicketDao
import com.carbajal.ayudacampus.data.local.dao.UsuarioDao
import com.carbajal.ayudacampus.data.local.entity.Categoria
import com.carbajal.ayudacampus.data.local.entity.Comentario
import com.carbajal.ayudacampus.data.local.entity.Ticket
import com.carbajal.ayudacampus.data.local.entity.Usuario

@Database(
    entities = [
        Usuario::class,
        Categoria::class,
        Ticket::class,
        Comentario::class,
        CitaAtencion::class,
        ActividadTicket::class,
        com.carbajal.ayudacampus.data.local.entity.Notificacion::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AyudaDatabase: RoomDatabase(){
    abstract fun categoriaDao(): CategoriaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun ticketDao(): TicketDao
    abstract fun soporteDao(): SoporteDao

    abstract fun notificacionDao(): com.carbajal.ayudacampus.data.local.dao.NotificacionDao

    companion object {
        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""CREATE TABLE IF NOT EXISTS notificaciones (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    ticketId INTEGER NOT NULL, usuarioId INTEGER NOT NULL, rol TEXT NOT NULL,
                    tituloTicket TEXT NOT NULL, estado TEXT NOT NULL, mensaje TEXT NOT NULL,
                    fecha INTEGER NOT NULL, leida INTEGER NOT NULL,
                    FOREIGN KEY(ticketId) REFERENCES tickets(id) ON UPDATE NO ACTION ON DELETE CASCADE,
                    FOREIGN KEY(usuarioId) REFERENCES usuarios(id) ON UPDATE NO ACTION ON DELETE CASCADE)""")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_notificaciones_ticketId ON notificaciones(ticketId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_notificaciones_usuarioId ON notificaciones(usuarioId)")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""CREATE TABLE IF NOT EXISTS citas_atencion (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    ticketId INTEGER NOT NULL, tituloTicket TEXT NOT NULL, estudiante TEXT NOT NULL,
                    soporteId INTEGER NOT NULL, inicio INTEGER NOT NULL, fin INTEGER NOT NULL,
                    lugar TEXT NOT NULL, notas TEXT NOT NULL, estado TEXT NOT NULL,
                    FOREIGN KEY(ticketId) REFERENCES tickets(id) ON UPDATE NO ACTION ON DELETE CASCADE)""")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_citas_atencion_ticketId ON citas_atencion(ticketId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_citas_atencion_inicio ON citas_atencion(inicio)")
                db.execSQL("""CREATE TABLE IF NOT EXISTS actividad_tickets (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    ticketId INTEGER NOT NULL, tituloTicket TEXT NOT NULL, autorNombre TEXT NOT NULL,
                    tipo TEXT NOT NULL, detalle TEXT NOT NULL, fecha INTEGER NOT NULL,
                    FOREIGN KEY(ticketId) REFERENCES tickets(id) ON UPDATE NO ACTION ON DELETE CASCADE)""")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_actividad_tickets_ticketId ON actividad_tickets(ticketId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_actividad_tickets_fecha ON actividad_tickets(fecha)")
                db.execSQL("""INSERT INTO actividad_tickets (ticketId, tituloTicket, autorNombre, tipo, detalle, fecha)
                    SELECT t.id, t.titulo, u.nombre, 'CREACION', 'Incidencia creada', t.fecha
                    FROM tickets t JOIN usuarios u ON u.id = t.clienteId""")
                db.execSQL("""INSERT INTO actividad_tickets (ticketId, tituloTicket, autorNombre, tipo, detalle, fecha)
                    SELECT c.ticketId, t.titulo, c.autorNombre, 'COMENTARIO',
                    CASE WHEN c.esInterno = 1 THEN 'Nota interna: ' ELSE 'Comentario: ' END || c.texto, c.fecha
                    FROM comentarios c JOIN tickets t ON t.id = c.ticketId""")
                db.execSQL("""INSERT INTO actividad_tickets (ticketId, tituloTicket, autorNombre, tipo, detalle, fecha)
                    SELECT t.id, t.titulo, u.nombre, 'CALIFICACION', 'Calificó con ' || t.calificacion || '/5 y cerró la incidencia', t.fechaCierre
                    FROM tickets t JOIN usuarios u ON u.id = t.clienteId
                    WHERE t.calificacion IS NOT NULL AND t.fechaCierre IS NOT NULL""")
            }
        }

        @Volatile
        private var INSTANCE: AyudaDatabase? = null

        fun obtener(context: Context): AyudaDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AyudaDatabase::class.java,
                    "ayudacampus.db"
                )
                .addMigrations(MIGRATION_3_4, MIGRATION_4_5)
                .fallbackToDestructiveMigration(true)
                .build().also { INSTANCE = it }
            }
    }
}
