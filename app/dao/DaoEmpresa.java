package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

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

	private static final String tipo="tipo";

	private static final String monto="monto";

	//----------------------------------------------------
	//Consultas
	//----------------------------------------------------

	/**
	 * Consulta que devuelve isan, titulo, y año de los videos en orden alfabetico
	 */
	private static final String consultaEmpresaDefault="SELECT * FROM "+tablaEmpresa+ " ORDER BY "+idEmpleador;

	private static final String ingresarEmpresa="INSERT INTO "+tablaEmpresa+" VALUES ";

	private static final String consultaEmpresaCuenta="SELECT * FROM "+tablaEmpresa+" WHERE "+idEmpleador+"=";

	private static final String eliminarEmpleadosCuenta="DELETE FROM "+tablaEmpresa+" WHERE "+idCuentaOrigen+"=";
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
				double montoPag = rs.getDouble(monto);
				String tipoPag = rs.getString(tipo);

				EmpresaValue.setId_Empleado(idEmplead);
				EmpresaValue.setId_Empleador(idEmpleadr);
				EmpresaValue.setId_Origen(idCuentaOr);
				EmpresaValue.setId_Destino(idCuentaDes);
				EmpresaValue.setMonto(montoPag);
				EmpresaValue.setTipo(tipoPag);
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

	public void asociarCuentaEmpleado(int idEmpleador, int idEmpleado, long idCuentaOrigen, long idCuentaDestino, String tipo, double monto) throws Exception
	{
		Connection conexion=null;
		try
		{
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			conexion.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			Statement st=conexion.createStatement();
			if(!existeCuentaAsociada(idEmpleador, idCuentaOrigen))
			{
				st.executeUpdate(ingresarEmpresa+"("+idEmpleador+","
						+idEmpleado+","
						+idCuentaOrigen+","
						+idCuentaDestino+","
						+monto+","
						+"'"+tipo+"')");

				conexion.commit();
			}
			else
			{
				throw new Exception("Ya tiene una cuenta asociada.");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.out.println(ingresarEmpresa);
			conexion.rollback();
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}
	}

	private boolean existeCuentaAsociada(int idEmpleador, long pCuentaOrigen) throws Exception
	{
		PreparedStatement prepStmt = null;
		Connection conexion=null;
		boolean existe=false;
		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaEmpresaCuenta+idEmpleador);

			ResultSet rs = prepStmt.executeQuery();
			while(rs.next())
			{
				long cuentaOrigen=rs.getLong(idCuentaOrigen);
				existe=existe&&(cuentaOrigen==pCuentaOrigen);
			}
			conexion.commit();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.out.println(consultaEmpresaCuenta);
			conexion.rollback();
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}	
		return existe;
	}

	public HashMap<Long,Double> cuentasAPagarNomina(int idEmpleador) throws Exception
	{
		PreparedStatement prepStmt = null;

		HashMap<Long,Double> cuentas = new HashMap<Long,Double>();
		Connection conexion=null;

		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaEmpresaCuenta+idEmpleador);

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				long idCuentaDes = rs.getLong(idCuentaDestino);
				double montoPag = rs.getDouble(monto);
				cuentas.put(idCuentaDes, montoPag);
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
		return cuentas;
	}

	public void desasociarEmpleadosCuenta(long idOrigen) throws Exception
	{
		Connection conexion=null;
		try
		{
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			conexion.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			Statement st=conexion.createStatement();
			st.executeUpdate(eliminarEmpleadosCuenta+idOrigen);
			conexion.commit();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.out.println(eliminarEmpleadosCuenta);
			conexion.rollback();
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}
	}

	public long darCuentaNomina(int idEmpleador) throws Exception 
	{
		PreparedStatement prepStmt = null;
		Connection conexion=null;
		long resp = 0;
		try 
		{
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaEmpresaCuenta+idEmpleador);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next())
			{
				resp=rs.getLong(idCuentaOrigen);
			}
			conexion.commit();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.out.println(consultaEmpresaCuenta);
			conexion.rollback();
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}
		return resp;
	}
}
