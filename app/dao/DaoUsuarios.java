package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import vos.Usuario;

public class DaoUsuarios 
{
	/**
	 * nombre de la tabla usuario
	 */
	private static final String tablaUsuario = "usuario";

	/**
	 * nombre de la columna nombre en la tabla usuario.
	 */
	private static final String nombreUsuario = "nombre";

	/**
	 * nombre de la columna cedula en la tabla usuario.
	 */
	private static final String cedulaUsuario = "cedula";

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

	//----------------------------------------------------
	//Consultas
	//----------------------------------------------------

	/**
	 * Consulta que devuelve isan, titulo, y año de los videos en orden alfabetico
	 */
	private static final String consultaUsuariosDefault="SELECT * FROM "+tablaUsuario;

	private static final String insertarUsuario="INSERT INTO "+tablaUsuario+" VALUES";


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
	public ArrayList<Usuario> darUsuariosDefault() throws Exception
	{
		PreparedStatement prepStmt = null;

		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		Usuario usuarioValue = new Usuario();
		Connection conexion=null;

		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaUsuariosDefault);

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next()){
				String nombre = rs.getString(nombreUsuario);
				int cedula = rs.getInt(cedulaUsuario);
				String usuario= rs.getString(usernameUsuario);
				String contrasenia = rs.getString(contraseniaUsuario);
				int edad=rs.getInt(edadUsuario);
				String genero=rs.getString(generoUsuario);
				String ciudad=rs.getString(ciudadUsuario);
				String direccion=rs.getString(direccionUsuario);
				String tipo=rs.getString(tipoUsuario);

				usuarioValue.setNombre(nombre);
				usuarioValue.setCedula(cedula);
				usuarioValue.setUsuario(usuario);
				usuarioValue.setContrasenia(contrasenia);
				usuarioValue.setEdad(edad);
				usuarioValue.setGenero(genero);
				usuarioValue.setCiudad(ciudad);
				usuarioValue.setDireccion(direccion);
				usuarioValue.setTipo(tipo);
				usuarios.add(usuarioValue);
				usuarioValue = new Usuario();

			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaUsuariosDefault);
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
		return usuarios;
	}

	public void registrarUsuario(String nombre,int cedula, String usuario, String contrasenia, int edad, String genero, String ciudad, String direccion, String tipo) throws Exception
	{
		Connection conexion=null;
		try
		{
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			Statement st=conexion.createStatement();
			st.executeUpdate(insertarUsuario+"("+cedula+","
					+"'"+nombre+"',"+
					"'"+usuario+"',"+
					"'"+contrasenia+"',"+
					edad+","+
					"'"+genero+"',"+
					"'"+ciudad+"',"+
					"'"+direccion+"',"+
					"'"+tipo+"')");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.out.println(insertarUsuario);
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}	
	}

	public Usuario iniciarSesion(String usuario, String contrasenia) throws Exception
	{
		PreparedStatement prepStmt = null;
		Usuario usuarioValue = new Usuario();
		Connection conexion=null;

		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement("SELECT * FROM "+tablaUsuario+" WHERE "+usernameUsuario+"='"+usuario+
					"' AND "+contraseniaUsuario+"='"+contrasenia+"'");

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				String nombre = rs.getString(nombreUsuario);
				int cedula = rs.getInt(cedulaUsuario);
				int edad=rs.getInt(edadUsuario);
				String genero=rs.getString(generoUsuario);
				String ciudad=rs.getString(ciudadUsuario);
				String direccion=rs.getString(direccionUsuario);
				String tipo=rs.getString(tipoUsuario);

				usuarioValue.setNombre(nombre);
				usuarioValue.setCedula(cedula);
				usuarioValue.setUsuario(usuario);
				usuarioValue.setContrasenia(contrasenia);
				usuarioValue.setEdad(edad);
				usuarioValue.setGenero(genero);
				usuarioValue.setCiudad(ciudad);
				usuarioValue.setDireccion(direccion);
				usuarioValue.setTipo(tipo);
				return usuarioValue;
			}

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.out.println(consultaUsuariosDefault);
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
}
