package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import vos.Oficina;


public class DaoOficinas 
{
	/**
	 * nombre de la tabla Empleados
	 */
	private static final String tablaOficina = "oficina";

	/**
	 * nombre de la columna titulo_original en la tabla videos.
	 */
	private static final String idOficina = "id";

	/**
	 * nombre de la columna anyo en la tabla videos.
	 */
	private static final String idOficina_Gerente = "id_gerente";

	private static final String nombreOficina="nombre";

	private static final String direccionOficina="direccion";

	private static final String telefonoOficina="telefono";

	//----------------------------------------------------
	//Consultas
	//----------------------------------------------------

	/**
	 * Consulta que devuelve isan, titulo, y año de los videos en orden alfabetico
	 */
	private static final String consultaOficinasDefault="SELECT * FROM "+tablaOficina;

	private static final String maxIdOficina="SELECT MAX(id) AS maximo FROM "+tablaOficina;

	private static final String insertarOficina="INSERT INTO "+tablaOficina+" VALUES";

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
	public ArrayList<Oficina> darOficinasDefault() throws Exception
	{
		PreparedStatement prepStmt = null;

		ArrayList<Oficina> Oficinas = new ArrayList<Oficina>();
		Oficina OficinaValue = new Oficina();
		Connection conexion=null;

		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaOficinasDefault);

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				int id = rs.getInt(idOficina);
				int idGerente = rs.getInt(idOficina_Gerente);
				String nombre=rs.getString(nombreOficina);
				String direccion=rs.getString(direccionOficina);
				String telefono = rs.getString(telefonoOficina);

				OficinaValue.setId(id);
				OficinaValue.setId_gerente(idGerente);
				OficinaValue.setDireccion(direccion);
				OficinaValue.setNombre(nombre);
				OficinaValue.setTelefono(telefono);
				Oficinas.add(OficinaValue);
				OficinaValue = new Oficina();

			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaOficinasDefault);
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
		return Oficinas;
	}

	private int mayorId() throws Exception
	{
		PreparedStatement prepStmt = null;
		Connection conexion=null;
		int valor=0;
		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(maxIdOficina);

			ResultSet rs = prepStmt.executeQuery();
			while(rs.next())
			valor=rs.getInt("maximo");
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.out.println(maxIdOficina);
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}	
		return valor+1;
	}

	public void registrarOficina(String nombre, String direccion, String telefono, int idGerente) throws Exception
	{
		Connection conexion=null;
		try
		{
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			Statement st=conexion.createStatement();
			st.executeUpdate(insertarOficina+"("+mayorId()+","
					+"'"+nombre+"',"+
					"'"+direccion+"',"+
					"'"+telefono+"',"+
					idGerente+")");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.out.println(insertarOficina);
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}	
	}
	
	
}
