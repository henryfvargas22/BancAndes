package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import vos.Prestamo;


public class DaoPrestamos 
{
	/**
	 * nombre de la tabla Empleados
	 */
	private static final String tablaPrestamo = "prestamo";

	/**
	 * nombre de la columna titulo_original en la tabla videos.
	 */
	private static final String idPrestamo = "id";

	/**
	 * nombre de la columna anyo en la tabla videos.
	 */
	private static final String montoPrestamo = "monto";

	private static final String interesPrestamo="interes";

	private static final String cuotasPrestamo="cuotas";

	private static final String diaPagoPrestamo="dia_de_pago";

	private static final String cuotaMensualPrestamo="cuota_mensual";

	private static final String idPrestamo_Cliente="id_cliente";

	private static final String estaCerrado="cerrado";

	//----------------------------------------------------
	//Consultas
	//----------------------------------------------------

	/**
	 * Consulta que devuelve isan, titulo, y año de los videos en orden alfabetico
	 */
	private static final String consultaPrestamosDefault="SELECT * FROM "+tablaPrestamo;

	private static final String maxIdPrestamo="SELECT MAX(id) AS maximo FROM "+tablaPrestamo;

	private static final String insertarPrestamo="INSERT INTO "+tablaPrestamo+" VALUES";

	private static final String cerrarPrestamo="UPDATE "+tablaPrestamo+" SET cerrado=1 WHERE id=";

	private static final String consultaPrestamosCliente="SELECT * FROM "+tablaPrestamo+" WHERE id_Cliente=";

	private static final String consultaPrestamoId="SELECT * FROM "+tablaPrestamo+" WHERE id=";

	private static final String actualizarMontoPrestamo="UPDATE "+tablaPrestamo+" SET monto=";

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
	public ArrayList<Prestamo> darPrestamosDefault() throws Exception
	{
		PreparedStatement prepStmt = null;

		ArrayList<Prestamo> Prestamos = new ArrayList<Prestamo>();
		Prestamo PrestamoValue = new Prestamo();
		Connection conexion=null;

		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaPrestamosDefault);

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				int id = rs.getInt(idPrestamo);
				int idCliente = rs.getInt(idPrestamo_Cliente);
				long monto=rs.getLong(montoPrestamo);
				double interes=rs.getDouble(interesPrestamo);
				int cuotas = rs.getInt(cuotasPrestamo);
				int diaPago=rs.getInt(diaPagoPrestamo);
				double cuotaMensual=rs.getDouble(cuotaMensualPrestamo);
				boolean estaCerra=rs.getBoolean(estaCerrado);

				PrestamoValue.setId(id);
				PrestamoValue.setIdCliente(idCliente);
				PrestamoValue.setCuotaMensual(cuotaMensual);
				PrestamoValue.setCuotas(cuotas);
				PrestamoValue.setDiaPago(diaPago);
				PrestamoValue.setMonto(monto);
				PrestamoValue.setInteres(interes);
				PrestamoValue.setCerrado(estaCerra);
				Prestamos.add(PrestamoValue);
				PrestamoValue = new Prestamo();

			}
			conexion.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaPrestamosDefault);
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
		return Prestamos;
	}

	private int mayorId() throws Exception
	{
		PreparedStatement prepStmt = null;
		Connection conexion=null;
		int valor=0;
		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(maxIdPrestamo);

			ResultSet rs = prepStmt.executeQuery();
			valor=rs.getInt("maximo");
			conexion.commit();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.out.println(maxIdPrestamo);
			conexion.rollback();
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}	
		return valor+1;
	}

	public void registrarPrestamo(long monto,double interes,int cuotas,int diaPago,int cuotaMensual, int idCliente) throws Exception
	{
		Connection conexion=null;
		try
		{
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			Statement st=conexion.createStatement();
			st.executeUpdate(insertarPrestamo+"("+mayorId()+","
					+monto+","+
					interes+","+
					cuotas+","+
					diaPago+","+
					cuotaMensual+","+
					idCliente+","+
					0+")");
			conexion.commit();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.out.println(insertarPrestamo);
			conexion.rollback();
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}	
	}

	public boolean cerrarPrestamo(int id) throws Exception
	{
		Prestamo actual=darPrestamoId(id);
		if(actual.getMonto()==0)
		{
			Connection conexion=null;
			try
			{
				conexion=ConsultaDAO.darInstancia().establecerConexion();
				Statement st=conexion.createStatement();
				st.executeUpdate(cerrarPrestamo+id);
				conexion.commit();
				return true;
			}
			catch(SQLException e)
			{
				e.printStackTrace();
				System.out.println(cerrarPrestamo);
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

	public ArrayList<Prestamo> darPrestamosCliente(int idCliente) throws Exception
	{
		PreparedStatement prepStmt = null;

		ArrayList<Prestamo> Cuentas = new ArrayList<Prestamo>();
		Prestamo CuentaValue = new Prestamo();
		Connection conexion=null;
		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaPrestamosCliente+idCliente+" ORDER BY ID");

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				int id = rs.getInt(idPrestamo);
				long monto=rs.getLong(montoPrestamo);
				double tasa=rs.getDouble(interesPrestamo);
				int cuotas=rs.getInt(cuotasPrestamo);
				int diaPago=rs.getInt(diaPagoPrestamo);
				double cuota=rs.getDouble(cuotaMensualPrestamo);

				CuentaValue.setId(id);
				CuentaValue.setIdCliente(idCliente);
				CuentaValue.setMonto(monto);
				CuentaValue.setInteres(tasa);
				CuentaValue.setCuotas(cuotas);
				CuentaValue.setDiaPago(diaPago);
				CuentaValue.setCuotaMensual(cuota);
				Cuentas.add(CuentaValue);
				CuentaValue = new Prestamo();
			}
			conexion.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaPrestamosCliente+idCliente);
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

	public Prestamo darPrestamoId(int idPrestamo) throws Exception
	{
		PreparedStatement prepStmt = null;
		Prestamo PrestamoValue = new Prestamo();
		Connection conexion=null;
		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaPrestamoId+idPrestamo+" ORDER BY id");

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				int idCliente = rs.getInt(idPrestamo_Cliente);
				long monto=rs.getLong(montoPrestamo);
				double cuotaMensual=rs.getDouble(cuotaMensualPrestamo);
				boolean estaCerra=rs.getBoolean(estaCerrado);

				PrestamoValue.setId(idPrestamo);
				PrestamoValue.setIdCliente(idCliente);
				PrestamoValue.setCuotaMensual(cuotaMensual);
				PrestamoValue.setMonto(monto);
				PrestamoValue.setCerrado(estaCerra);

				return PrestamoValue;
			}
			conexion.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaPrestamoId+idPrestamo);
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

	public void actualizarMonto(int idPrestamo,double montoNuevo) throws Exception
	{
		Prestamo actual=darPrestamoId(idPrestamo);
		double monto=actual.getMonto()+montoNuevo;
		Connection conexion=null;
		try
		{
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			Statement st=conexion.createStatement();
			st.executeUpdate(actualizarMontoPrestamo+monto+" WHERE id="+idPrestamo);
			conexion.commit();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.out.println(actualizarMontoPrestamo);
			conexion.rollback();
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}	
	}
}
