package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import vos.Empleado;

public class DaoEmpleados 
{
	/**
	 * nombre de la tabla Empleados
	 */
	private static final String tablaEmpleado = "empleado";

	/**
	 * nombre de la columna titulo_original en la tabla videos.
	 */
	private static final String idEmpleado = "id";

	/**
	 * nombre de la columna anyo en la tabla videos.
	 */
	private static final String idEmpleado_Usuario = "id_usuario";

	/**
	 * nombre de la columna nombre en la tabla usuario.
	 */
	private static final String nombreUsuario = "nombre";

	/**
	 * nombre de la columna usuario en la tabla usuario.
	 */
	private static final String usernameUsuario = "usuario";

	/**
	 * nombre de la columna contrasenia en la tabla usuario.
	 */
	private static final String contraseniaUsuario = "contrasenia";

	/**
	 * nombre de la columna edad en la tabla usuario.
	 */
	private static final String edadUsuario = "edad";

	/**
	 * nombre de la columna genero en la tabla usuario.
	 */
	private static final String generoUsuario = "genero";

	/**
	 * nombre de la columna ciudad en la tabla usuario.
	 */
	private static final String ciudadUsuario = "ciudad";

	/**
	 * nombre de la columna direccion en la tabla usuario.
	 */
	private static final String direccionUsuario = "direccion";

	/**
	 * nombre de la columna tipo en la tabla usuario.
	 */
	private static final String tipoUsuario = "tipo";

	private static final String rolEmpleado= "rol";

	private static final String idOficina="id_oficina";

	//----------------------------------------------------
	//Consultas
	//----------------------------------------------------

	/**
	 * Consulta que devuelve isan, titulo, y año de los videos en orden alfabetico
	 */
	private static final String consultaEmpleadosDefault="SELECT * FROM "+tablaEmpleado+" e1 join usuario u1 on e1.ID_USUARIO=u1.CEDULA";

	private static final String maxIdEmpleado="SELECT MAX(id) AS maximo FROM "+tablaEmpleado;

	private static final String insertarEmpleado="INSERT INTO "+tablaEmpleado+" VALUES";
	// ---------------------------------------------------
	// Métodos asociados a los casos de uso: Consulta
	// ---------------------------------------------------

	/**
	 * Método que se encarga de realizar la consulta en la base de datos
	 * y retorna un ArrayList de elementos tipo VideosValue.
	 * @return ArrayList lista que contiene elementos tipo VideosValue.
	 * La lista contiene los videos ordenados alfabeticamente
	 * @throws Exception se lanza una excepción si ocurre un error en
	 * la conexión o en la consulta. 
	 */
	public ArrayList<Empleado> darEmpleadosDefault() throws Exception
	{
		PreparedStatement prepStmt = null;

		ArrayList<Empleado> Empleados = new ArrayList<Empleado>();
		Empleado EmpleadoValue = new Empleado();
		Connection conexion=null;

		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaEmpleadosDefault);

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				int idCli = rs.getInt(idEmpleado);
				int cedCli = rs.getInt(idEmpleado_Usuario);
				String nombre = rs.getString(nombreUsuario);
				int edad=rs.getInt(edadUsuario);
				String genero=rs.getString(generoUsuario);
				String ciudad=rs.getString(ciudadUsuario);
				String direccion=rs.getString(direccionUsuario);
				String tipo=rs.getString(tipoUsuario);
				String usuario= rs.getString(usernameUsuario);
				String contrasenia = rs.getString(contraseniaUsuario);
				String rol=rs.getString(rolEmpleado);
				int idOfic=rs.getInt(idOficina);

				EmpleadoValue.setIdEmpleado(idCli);
				EmpleadoValue.setIdUsuario(cedCli);
				EmpleadoValue.setCedula(cedCli);
				EmpleadoValue.setCiudad(ciudad);
				EmpleadoValue.setContrasenia(contrasenia);
				EmpleadoValue.setDireccion(direccion);
				EmpleadoValue.setEdad(edad);
				EmpleadoValue.setNombre(nombre);
				EmpleadoValue.setGenero(genero);
				EmpleadoValue.setTipo(tipo);
				EmpleadoValue.setUsuario(usuario);
				EmpleadoValue.setRol(rol);
				EmpleadoValue.setIdOficina(idOfic);
				Empleados.add(EmpleadoValue);
				EmpleadoValue = new Empleado();

			}
			conexion.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaEmpleadosDefault);
			conexion.rollback();
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}finally 
		{
			if (prepStmt != null) 
			{
				try {
					prepStmt.close();
				} catch (SQLException exception) {

					throw new Exception("ERROR: ConsultaDAO: loadRow() =  cerrando una conexión.");
				}
			}
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}		
		return Empleados;
	}

	public Empleado iniciarSesion(String usuario, String contrasenia) throws Exception
	{
		PreparedStatement prepStmt = null;
		Empleado usuarioValue = new Empleado();
		Connection conexion=null;

		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaEmpleadosDefault+" WHERE "+usernameUsuario+"='"+usuario+
					"' AND "+contraseniaUsuario+"='"+contrasenia+"'");

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				String rol=rs.getString(rolEmpleado);
				usuarioValue.setUsuario(usuario);
				usuarioValue.setContrasenia(contrasenia);
				usuarioValue.setRol(rol);
				conexion.commit();
				return usuarioValue;
			}
			conexion.commit();

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.out.println(consultaEmpleadosDefault);
			conexion.rollback();
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}
		finally 
		{
			if (prepStmt != null) 
			{
				try {
					prepStmt.close();
				} catch (SQLException exception) {

					throw new Exception("ERROR: ConsultaDAO: loadRow() =  cerrando una conexión.");
				}
			}
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}
		return null;
	}
	
	public Empleado darEmpleadoId(int idUsuario) throws Exception
	{
		PreparedStatement prepStmt = null;
		Empleado usuarioValue = new Empleado();
		Connection conexion=null;

		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaEmpleadosDefault+" WHERE "+idEmpleado_Usuario+"="+idUsuario);

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				int idEm=rs.getInt(idEmpleado);
				String nombre = rs.getString(nombreUsuario);
				int edad=rs.getInt(edadUsuario);
				String genero=rs.getString(generoUsuario);
				String ciudad=rs.getString(ciudadUsuario);
				String direccion=rs.getString(direccionUsuario);
				String tipo=rs.getString(tipoUsuario);
				String rol=rs.getString(rolEmpleado);
				int idOfi=rs.getInt(idOficina);
				String usuario=rs.getString(usernameUsuario);
				String contrasenia=rs.getString(contraseniaUsuario);

				usuarioValue.setNombre(nombre);
				usuarioValue.setCedula(idUsuario);
				usuarioValue.setUsuario(usuario);
				usuarioValue.setContrasenia(contrasenia);
				usuarioValue.setEdad(edad);
				usuarioValue.setGenero(genero);
				usuarioValue.setCiudad(ciudad);
				usuarioValue.setDireccion(direccion);
				usuarioValue.setTipo(tipo);
				usuarioValue.setIdEmpleado(idEm);
				usuarioValue.setIdUsuario(idUsuario);
				usuarioValue.setRol(rol);
				usuarioValue.setIdOficina(idOfi);
				conexion.commit();
				return usuarioValue;
			}
			conexion.commit();

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.out.println(consultaEmpleadosDefault);
			conexion.rollback();
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}
		finally 
		{
			if (prepStmt != null) 
			{
				try {
					prepStmt.close();
				} catch (SQLException exception) {

					throw new Exception("ERROR: ConsultaDAO: loadRow() =  cerrando una conexión.");
				}
			}
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}
		return null;
	}

	private int mayorId() throws Exception
	{
		PreparedStatement prepStmt = null;
		Connection conexion=null;
		int valor=0;
		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(maxIdEmpleado);

			ResultSet rs = prepStmt.executeQuery();
			while(rs.next())
				valor=rs.getInt("maximo");
			conexion.commit();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.out.println(maxIdEmpleado);
			conexion.rollback();
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}	
		return valor+1;
	}

	public ArrayList<Empleado> darGerentes() throws Exception
	{
		PreparedStatement prepStmt = null;

		ArrayList<Empleado> Empleados = new ArrayList<Empleado>();
		Empleado EmpleadoValue = new Empleado();
		Connection conexion=null;

		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaEmpleadosDefault+" WHERE e1.rol='gerente'");

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				int idCli = rs.getInt(idEmpleado);
				int cedCli = rs.getInt(idEmpleado_Usuario);
				String nombre = rs.getString(nombreUsuario);
				int edad=rs.getInt(edadUsuario);
				String genero=rs.getString(generoUsuario);
				String ciudad=rs.getString(ciudadUsuario);
				String direccion=rs.getString(direccionUsuario);
				String tipo=rs.getString(tipoUsuario);
				String usuario= rs.getString(usernameUsuario);
				String contrasenia = rs.getString(contraseniaUsuario);
				String rol=rs.getString(rolEmpleado);
				int idOfi=rs.getInt(idOficina);

				EmpleadoValue.setIdEmpleado(idCli);
				EmpleadoValue.setIdUsuario(cedCli);
				EmpleadoValue.setCedula(cedCli);
				EmpleadoValue.setCiudad(ciudad);
				EmpleadoValue.setContrasenia(contrasenia);
				EmpleadoValue.setDireccion(direccion);
				EmpleadoValue.setEdad(edad);
				EmpleadoValue.setNombre(nombre);
				EmpleadoValue.setGenero(genero);
				EmpleadoValue.setTipo(tipo);
				EmpleadoValue.setUsuario(usuario);
				EmpleadoValue.setRol(rol);
				EmpleadoValue.setIdOficina(idOfi);
				Empleados.add(EmpleadoValue);
				EmpleadoValue = new Empleado();

			}
			conexion.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaEmpleadosDefault);
			conexion.rollback();
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}finally 
		{
			if (prepStmt != null) 
			{
				try {
					prepStmt.close();
				} catch (SQLException exception) {

					throw new Exception("ERROR: ConsultaDAO: loadRow() =  cerrando una conexión.");
				}
			}
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}		
		return Empleados;
	} 

	public void registrarEmpleado(int cedula, String rol, int idOficin) throws Exception
	{
		Connection conexion=null;
		try
		{
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			Statement st=conexion.createStatement();
			if(rol.equals("admin"))
			{
				st.executeUpdate(insertarEmpleado+"("+mayorId()+","+cedula+",'"+rol+"',NULL)");
				conexion.commit();
			}
			else
			{
				st.executeUpdate(insertarEmpleado+"("+mayorId()+","+cedula+",'"+rol+"',"+idOficin+ ")");
				conexion.commit();
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.out.println(insertarEmpleado);
			conexion.rollback();
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}	
	}
}
