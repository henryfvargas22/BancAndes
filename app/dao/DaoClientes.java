package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import vos.Cliente;


public class DaoClientes 
{
	/**
	 * nombre de la tabla clientes
	 */
	private static final String tablaCliente = "cliente";
	
	/**
	 * nombre de la columna titulo_original en la tabla videos.
	 */
	private static final String idCliente = "id";
	
	/**
	 * nombre de la columna anyo en la tabla videos.
	 */
	private static final String idCliente_Usuario = "id_usuario";
	
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

	//----------------------------------------------------
	//Consultas
	//----------------------------------------------------
	
	/**
	 * Consulta que devuelve isan, titulo, y año de los videos en orden alfabetico
	 */
	private static final String consultaClientesDefault="SELECT * FROM "+tablaCliente+ " ORDER BY "+idCliente_Usuario;
	
	private static final String maxIdCliente="SELECT MAX(id) AS maximo FROM "+tablaCliente;
	
	private static final String insertarCliente="INSERT INTO "+tablaCliente+" VALUES";
	
	private static final String consultaClienteUsuario="SELECT * FROM "+tablaCliente+" JOIN usuario ON cliente.id_usuario=usuario.cedula ";
	
	private static final String eliminarCliente="DELETE FROM "+tablaCliente+" WHERE "+idCliente_Usuario+"=";
	
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
    public ArrayList<Cliente> darClientesDefault() throws Exception
    {
    	PreparedStatement prepStmt = null;
    	
    	ArrayList<Cliente> clientes = new ArrayList<Cliente>();
		Cliente clienteValue = new Cliente();
    	Connection conexion=null;
		
		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaClienteUsuario);
			
			ResultSet rs = prepStmt.executeQuery();
			
			while(rs.next())
			{
				int idCli = rs.getInt(idCliente);
				int cedCli = rs.getInt(idCliente_Usuario);
				String nombre = rs.getString(nombreUsuario);
				int edad=rs.getInt(edadUsuario);
				String genero=rs.getString(generoUsuario);
				String ciudad=rs.getString(ciudadUsuario);
				String direccion=rs.getString(direccionUsuario);
				String tipo=rs.getString(tipoUsuario);
				String usuario= rs.getString(usernameUsuario);
				String contrasenia = rs.getString(contraseniaUsuario);
				
				clienteValue.setIdCliente(idCli);
				clienteValue.setIdUsuario(cedCli);
				clienteValue.setCedula(cedCli);
				clienteValue.setContrasenia(contrasenia);
				clienteValue.setCiudad(ciudad);
				clienteValue.setDireccion(direccion);
				clienteValue.setEdad(edad);
				clienteValue.setGenero(genero);
				clienteValue.setNombre(nombre);
				clienteValue.setTipo(tipo);
				clienteValue.setUsuario(usuario);
				clientes.add(clienteValue);
				clienteValue = new Cliente();
							
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaClientesDefault);
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
		return clientes;
    }
    
    public Cliente iniciarSesion(String usuario, String contrasenia) throws Exception
    {
    	PreparedStatement prepStmt = null;
    	
		Cliente clienteValue = new Cliente();
    	Connection conexion=null;
		
		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaClienteUsuario+"WHERE usuario='"+usuario+"' AND "+
			"contrasenia='"+contrasenia+"'");
			
			ResultSet rs = prepStmt.executeQuery();
			
			while(rs.next())
			{
				clienteValue.setUsuario(usuario);
				clienteValue.setContrasenia(contrasenia);
				clienteValue = new Cliente();
				
				return clienteValue;
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaClienteUsuario);
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
		return null;
    }
    
    private int mayorId() throws Exception
	{
		PreparedStatement prepStmt = null;
		Connection conexion=null;
		int valor=0;
		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(maxIdCliente);

			ResultSet rs = prepStmt.executeQuery();
			while(rs.next())
			valor=rs.getInt("maximo");
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.out.println(maxIdCliente);
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}	
		return valor+1;
	}
    
    public void registrarCliente(int cedula) throws Exception
	{
		Connection conexion=null;
		try
		{
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			Statement st=conexion.createStatement();
			st.executeUpdate(insertarCliente+"("+mayorId()+","
					+cedula+")");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.out.println(insertarCliente);
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}	
	}
    
    public void eliminarCliente(int cedula) throws Exception
    {
    	Connection conexion=null;
		try
		{
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			Statement st=conexion.createStatement();
			st.executeUpdate(eliminarCliente+cedula);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.out.println(eliminarCliente);
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}
    }
}
