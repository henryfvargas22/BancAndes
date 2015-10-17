package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import vos.Trabaja;

public class DaoTrabaja 
{
	/**
	 * nombre de la tabla Empleados
	 */
	private static final String tablaTrabaja = "trabaja";

	/**
	 * nombre de la columna titulo_original en la tabla videos.
	 */
	private static final String idOficina = "id_punto";

	/**
	 * nombre de la columna anyo en la tabla videos.
	 */
	private static final String idEmpleado = "id_empleado";

	//----------------------------------------------------
	//Consultas
	//----------------------------------------------------

	/**
	 * Consulta que devuelve isan, titulo, y año de los videos en orden alfabetico
	 */
	private static final String consultaTrabajaDefault="SELECT * FROM "+tablaTrabaja;

	private static final String ingresarTrabaja="INSERT INTO "+tablaTrabaja+" VALUES";

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
	public ArrayList<Trabaja> darTrabajaDefault() throws Exception
	{
		PreparedStatement prepStmt = null;

		ArrayList<Trabaja> Trabajas = new ArrayList<Trabaja>();
		Trabaja TrabajaValue = new Trabaja();
		Connection conexion=null;

		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaTrabajaDefault);

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				int idEmp = rs.getInt(idEmpleado);
				int idOfic = rs.getInt(idOficina);

				TrabajaValue.setIdEmpleado(idEmp);
				TrabajaValue.setIdOficina(idOfic);
				Trabajas.add(TrabajaValue);
				TrabajaValue = new Trabaja();

			}
			conexion.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaTrabajaDefault);
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
		return Trabajas;
	}

	public void registrarTrabaja(int idEmple,int idOfic) throws Exception
	{
		Connection conexion=null;
		try
		{
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			Statement st=conexion.createStatement();
			st.executeUpdate(ingresarTrabaja+"("+idOfic+","
					+idEmple+")");
			conexion.commit();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.out.println(ingresarTrabaja);
			conexion.rollback();
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}
	}

	public ArrayList<Integer> darIdEmpleadosOficina(int idOficina) throws Exception
	{
		PreparedStatement prepStmt = null;

		ArrayList<Integer> empleados = new ArrayList<Integer>();
		int empleado;
		Connection conexion=null;
		try 
		{
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaTrabajaDefault);

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				empleado = rs.getInt(idEmpleado);
				empleados.add(empleado);
			}
			conexion.commit();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaTrabajaDefault);
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
		return empleados;
	}
}
