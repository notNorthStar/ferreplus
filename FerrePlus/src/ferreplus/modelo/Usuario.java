package ferreplus.modelo;

/**
 * Representa un usuario del sistema Ferre+ con permisos para entrar
 * al modulo de Inventario y al modulo de Ventas.
 *
 * <p>No contiene logica de negocio, solo los datos. La autenticacion
 * vive en {@code dao.UsuarioDAO} y en {@code controlador.LoginController}.</p>
 *
 * @author Sosa Fernandez Luis David
 * @author Gutierrez Colorado Oliver
 */
public class Usuario {

    /** Identificador autoincremental en la tabla usuarios. */
    private int id;

    /** Nombre de usuario que se teclea en el login. */
    private String username;

    /** Contrasena (en este proyecto se guarda en claro por simplicidad). */
    private String password;

    /** Nombre completo que se muestra en el dashboard ("Bienvenido, ..."). */
    private String nombreCompleto;

    /** True si el usuario puede iniciar sesion, false si fue deshabilitado. */
    private boolean activo;

    /** Constructor vacio para uso de los DAOs. */
    public Usuario() {
    }

    /**
     * Constructor con todos los campos.
     *
     * @param id              id autogenerado
     * @param username        nombre de usuario
     * @param password        contrasena
     * @param nombreCompleto  nombre y apellido para mostrar
     * @param activo          si el usuario esta habilitado
     */
    public Usuario(int id, String username, String password,
                   String nombreCompleto, boolean activo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", username='" + username
                + "', nombreCompleto='" + nombreCompleto + "'}";
    }
}
