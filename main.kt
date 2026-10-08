enum class Rol { ADMIN, EDITOR, VISITANTE }

interface Autenticable {
    fun autenticar(clave: String): Boolean
}

data class Usuario(
    val id: Int,
    val nombre: String,
    val rol: Rol,
    private val clave: String
) : Autenticable {
    override fun autenticar(clave: String): Boolean = this.clave == clave
}

object GestorUsuarios {
    private val acciones = listOf("ver", "editar", "eliminar")

    fun iniciarSesion(cuenta: Autenticable, clave: String): Boolean {
        val ok = cuenta.autenticar(clave)
        println(if (ok) "Acceso concedido" else "Acceso denegado: clave incorrecta")
        return ok
    }

    fun puedeRealizar(usuario: Usuario, accion: String): Boolean =
        when (usuario.rol) {
            Rol.ADMIN -> true
            Rol.EDITOR -> accion != "eliminar"
            Rol.VISITANTE -> accion == "ver"
        }

    fun mostrarPermisos(usuario: Usuario, clave: String) {
        if (!iniciarSesion(usuario, clave)) return

        println("Permisos de ${usuario.nombre} (${usuario.rol}):")
        for (accion in acciones) {
            println("  $accion: ${puedeRealizar(usuario, accion)}")
        }
    }
}

fun main() {
    val ana = Usuario(1, "Ana", Rol.ADMIN, "1234")
    val luis = Usuario(2, "Luis", Rol.EDITOR, "abcd")
    val eva = Usuario(3, "Eva", Rol.VISITANTE, "pass")

    GestorUsuarios.mostrarPermisos(ana, "1234")
    GestorUsuarios.mostrarPermisos(luis, "clave-mal")
    GestorUsuarios.mostrarPermisos(eva, "pass")
}
