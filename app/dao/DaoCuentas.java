package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import vos.Cuenta;

public class DaoCuentas 
{
	/**
	 * nombre de la tabla Cuentas
	 */
	private static final String tablaCuenta = "cuenta";

	/**
	 * nombre de la columna titulo_original en la tabla videos.
	 */
	private static final String idCuenta = "id";

	/**
	 * nombre de la columna anyo en la tabla videos.
	 */
	private static final String idCuenta_Cliente = "id_Cliente";

	private static final String tipoCuenta= "tipo";

	private static final String estaCerrada="cerrada";

	private static final String montoCuenta="monto";

	//----------------------------------------------------
	//Consultas
	//----------------------------------------------------

	/**
	 * Consulta que devuelve isan, titulo, y año de los videos en orden alfabetico
	 */
	private static final String consultaCuentasDefault="SELECT * FROM "+tablaCuenta;

	private static final String insertarCuenta="INSERT INTO "+tablaCuenta+" VALUES";

	private static final String cerrarCuenta="UPDATE "+tablaCuenta+" SET cerrada=1 WHERE id=";

	private static final String consultaCuentasCliente="SELECT * FROM "+tablaCuenta+" WHERE id_Cliente=";

	private static final String consultaCuentasId="SELECT * FROM "+tablaCuenta+" WHERE id=";

	private static final String actualizarMontoCuenta="UPDATE "+tablaCuenta+" SET monto=";
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
	public ArrayList<Cuenta> darCuentasDefault() throws Exception
	{
		PreparedStatement prepStmt = null;

		ArrayList<Cuenta> Cuentas = new ArrayList<Cuenta>();
		Cuenta CuentaValue = new Cuenta();
		Connection conexion=null;

		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaCuentasDefault);

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				long id = rs.getLong(idCuenta);
				int id_Cliente = rs.getInt(idCuenta_Cliente);
				String tipo = rs.getString(tipoCuenta);
				double monto=rs.getDouble(montoCuenta);
				boolean cerrada= rs.getBoolean(estaCerrada);

				CuentaValue.setId(id);
				CuentaValue.setId_Cliente(id_Cliente);
				CuentaValue.setTipo(tipo);
				CuentaValue.setEstaCerrada(cerrada);
				CuentaValue.setMonto(monto);
				Cuentas.add(CuentaValue);
				CuentaValue = new Cuenta();

			}
			conexion.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaCuentasDefault);
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
		return Cuentas;
	}

	public void registrarCuenta(int id, String tipo, int idCliente) throws Exception
	{
		Connection conexion=null;
		try
		{
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			Statement st=conexion.createStatement();
			st.executeUpdate(insertarCuenta+"("+id+","
					+"'"+tipo+"',"+
					idCliente+","+
					0+","+
					0+")");
			conexion.commit();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.out.println(insertarCuenta);
			conexion.rollback();
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}	
	}

	public boolean cerrarCuenta(long id) throws Exception
	{
		Cuenta actual=darCuentaId(id);
		if(actual.getMonto()==0)
		{
			Connection conexion=null;
			try
			{
				conexion=ConsultaDAO.darInstancia().establecerConexion();
				Statement st=conexion.createStatement();
				st.executeUpdate(cerrarCuenta+id);
				conexion.commit();
				return true;
			}
			catch(SQLException e)
			{
				e.printStackTrace();
				System.out.println(cerrarCuenta);
				conexion.rollback();
				throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
			}
			finally 
			{
				ConsultaDAO.darInstancia().closeConnection(conexion);
			}
		}
		else
		{
			return false;
		}
	}

	public Cuenta darCuentaId(long idCuenta) throws Exception
	{
		PreparedStatement prepStmt = null;
		Cuenta CuentaValue = new Cuenta();
		Connection conexion=null;
		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaCuentasId+idCuenta+" ORDER BY id");

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				int id = rs.getInt(idCuenta_Cliente);
				String tipo = rs.getString(tipoCuenta);
				double monto=rs.getDouble(montoCuenta);
				boolean cerrada= rs.getBoolean(estaCerrada);

				CuentaValue.setId(idCuenta);
				CuentaValue.setId_Cliente(id);
				CuentaValue.setTipo(tipo);
				CuentaValue.setMonto(monto);
				CuentaValue.setEstaCerrada(cerrada);
				conexion.commit();
				return CuentaValue;
			}
			conexion.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaCuentasId+idCuenta);
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
		return null;
	}

	public ArrayList<Cuenta> darCuentasCliente(int idCliente) throws Exception
	{
		PreparedStatement prepStmt = null;

		ArrayList<Cuenta> Cuentas = new ArrayList<Cuenta>();
		Cuenta CuentaValue = new Cuenta();
		Connection conexion=null;
		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaCuentasCliente+idCliente);

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				long id = rs.getLong(idCuenta);
				String tipo = rs.getString(tipoCuenta);
				double monto=rs.getDouble(montoCuenta);

				CuentaValue.setId(id);
				CuentaValue.setId_Cliente(idCliente);
				CuentaValue.setTipo(tipo);
				CuentaValue.setMonto(monto);
				//CuentaValue.setEstaCerrada(cerrada);
				Cuentas.add(CuentaValue);
				CuentaValue = new Cuenta();

			}
			conexion.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaCuentasDefault+idCliente);
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
		return Cuentas;
	}

	public void actualizarMonto(long idCuenta,double montoNuevo) throws Exception
	{
		Cuenta actual=darCuentaId(idCuenta);
		double monto=actual.getMonto()+montoNuevo;
		Connection conexion=null;
		try
		{
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			Statement st=conexion.createStatement();
			st.executeUpdate(actualizarMontoCuenta+monto+" WHERE id="+idCuenta);
			conexion.commit();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.out.println(actualizarMontoCuenta);
			conexion.rollback();
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}	
	}
}
