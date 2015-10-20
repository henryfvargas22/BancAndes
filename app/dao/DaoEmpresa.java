package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Empresa;

public class DaoEmpresa 
{
	/**
	 * nombre de la tabla clientes
	 */
	private static final String tablaEmpresa = "empresa";

	/**
	 * nombre de la columna titulo_original en la tabla videos.
	 */
	private static final String idEmpleador = "id_empleador";

	/**
	 * nombre de la columna anyo en la tabla videos.
	 */
	private static final String idEmpleado = "id_empleado";
	
	/**
	 * nombre de la columna titulo_original en la tabla videos.
	 */
	private static final String idCuentaOrigen = "id_cuentaorigen";

	/**
	 * nombre de la columna anyo en la tabla videos.
	 */
	private static final String idCuentaDestino = "id_cuentadestino";

	//----------------------------------------------------
	//Consultas
	//----------------------------------------------------

	/**
	 * Consulta que devuelve isan, titulo, y año de los videos en orden alfabetico
	 */
	private static final String consultaEmpresaDefault="SELECT * FROM "+tablaEmpresa+ " ORDER BY "+idEmpleador;

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
	public ArrayList<Empresa> darEmpresasDefault() throws Exception
	{
		PreparedStatement prepStmt = null;

		ArrayList<Empresa> Empresas = new ArrayList<Empresa>();
		Empresa EmpresaValue = new Empresa();
		Connection conexion=null;

		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaEmpresaDefault);

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				int idEmpleadr = rs.getInt(idEmpleador);
				int idEmplead = rs.getInt(idEmpleado);
				long idCuentaOr = rs.getLong(idCuentaOrigen);
				long idCuentaDes = rs.getLong(idCuentaDestino);

				EmpresaValue.setId_Empleado(idEmplead);
				EmpresaValue.setId_Empleador(idEmpleadr);
				EmpresaValue.setId_Origen(idCuentaOr);
				EmpresaValue.setId_Destino(idCuentaDes);
				Empresas.add(EmpresaValue);
				EmpresaValue = new Empresa();

			}
			conexion.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaEmpresaDefault);
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
		return Empresas;
	}
}
