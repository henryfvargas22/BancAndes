package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import vos.Operacion;

public class DaoOperaciones 
{
	/**
	 * nombre de la tabla Empleados
	 */
	private static final String tablaOperacion = "operacion";

	/**
	 * nombre de la columna titulo_original en la tabla videos.
	 */
	private static final String fechaOperacion = "fecha";

	/**
	 * nombre de la columna anyo en la tabla videos.
	 */
	private static final String idCuenta = "id_cuenta";

	private static final String tipoOperacion="tipo";

	private static final String montoOperacion="monto";

	private static final String idPrestamo="id_prestamo";

	private static final String idCliente="id_cliente";

	//----------------------------------------------------
	//Consultas
	//----------------------------------------------------

	/**
	 * Consulta que devuelve isan, titulo, y año de los videos en orden alfabetico
	 */
	private static final String consultaOperacionesDefault="SELECT * FROM "+tablaOperacion;

	private static final String ingresarOperacion="INSERT INTO "+tablaOperacion+" VALUES";

	private static final String consultaOperacionesCliente="SELECT * FROM "+tablaOperacion+" WHERE id_cliente=";

	private static final String filtroOperaciones="SELECT * FROM "+tablaOperacion+" WHERE ";

	private static final String consultaConsignaciones="SELECT * FROM (operacion left join cliente on id_cliente=id_usuario) left join prestamo on prestamo.id_cliente=id_usuario where operacion.tipo='Consignar' and operacion.monto>=";
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
	public ArrayList<Operacion> darOperacionesDefault() throws Exception
	{
		PreparedStatement prepStmt = null;

		ArrayList<Operacion> Operacions = new ArrayList<Operacion>();
		Operacion OperacionValue = new Operacion();
		Connection conexion=null;

		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaOperacionesDefault);

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				long idPrest = rs.getLong(idPrestamo);
				int idClien = rs.getInt(idCliente);
				long monto=rs.getLong(montoOperacion);
				long idCuent=rs.getLong(idCuenta);
				String tipo = rs.getString(tipoOperacion);
				String fecha=rs.getString(fechaOperacion);
				//System.out.println(fecha);
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date fech=format.parse(fecha);
				//				System.out.println(fech);

				OperacionValue.setFecha(fech);
				OperacionValue.setIdCliente(idClien);
				OperacionValue.setMonto(monto);
				OperacionValue.setTipo(tipo);
				if(idPrest>0)
				{
					OperacionValue.setIdPrestamo(idPrest);
				}
				else
				{
					OperacionValue.setIdCuenta(idCuent);
				}
				Operacions.add(OperacionValue);
				OperacionValue = new Operacion();

			}
			conexion.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaOperacionesDefault);
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
		return Operacions;
	}

	public void registrarOperacionPrestamo(int idClient,double monto,String tipo, int idPrestam) throws Exception
	{
		Connection conexion=null;
		try
		{
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			conexion.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			Statement st=conexion.createStatement();
			st.executeUpdate(ingresarOperacion+"(CURRENT_TIMESTAMP,"
					+-1+","+
					"'"+tipo+"',"+
					monto+","+
					idPrestam+","+
					idClient+")");
			conexion.commit();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.out.println(ingresarOperacion);
			conexion.rollback();
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}
	}

	public void registrarOperacionCuenta(int idClient,double monto,String tipo, long idCuent) throws Exception
	{
		Connection conexion=null;
		try
		{
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			conexion.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			Statement st=conexion.createStatement();
			st.executeUpdate(ingresarOperacion+"(CURRENT_TIMESTAMP,"
					+idCuent+","+
					"'"+tipo+"',"+
					monto+","+
					-1+","+
					idClient+")");
			conexion.commit();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.out.println(ingresarOperacion);
			conexion.rollback();
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}
	}

	public ArrayList<Operacion> darOperacionesCliente(int idCliente) throws Exception
	{
		PreparedStatement prepStmt = null;

		ArrayList<Operacion> Operacions = new ArrayList<Operacion>();
		Operacion OperacionValue = new Operacion();
		Connection conexion=null;

		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaOperacionesCliente+idCliente+" ORDER BY "+fechaOperacion+" DESC");

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				long idPrest = rs.getLong(idPrestamo);
				//int idClien = rs.getInt(idCliente);
				long monto=rs.getLong(montoOperacion);
				long idCuent=rs.getLong(idCuenta);
				String tipo = rs.getString(tipoOperacion);
				Date fecha=rs.getDate(fechaOperacion);

				OperacionValue.setFecha(fecha);
				OperacionValue.setIdCliente(idCliente);
				OperacionValue.setMonto(monto);
				OperacionValue.setTipo(tipo);
				if(idPrest>0)
				{
					OperacionValue.setIdPrestamo(idPrest);
					OperacionValue.setIdCuenta(-1);
				}
				else
				{
					OperacionValue.setIdCuenta(idCuent);
					OperacionValue.setIdPrestamo(-1);
				}
				Operacions.add(OperacionValue);
				OperacionValue = new Operacion();

			}
			conexion.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaOperacionesDefault+idCliente);
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
		return Operacions;
	}

	public ArrayList<Operacion> darConsignaciones(double montoMin, boolean abierto) throws Exception
	{
		PreparedStatement prepStmt = null;

		ArrayList<Operacion> Operacions = new ArrayList<Operacion>();
		Operacion OperacionValue = new Operacion();
		Connection conexion=null;

		try
		{
			int resp=(abierto?0:1);
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaConsignaciones+montoMin+" and cerrado="+resp);

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				long idPrest = rs.getLong(idPrestamo);
				int idClien = rs.getInt(idCliente);
				long monto=rs.getLong(montoOperacion);
				long idCuent=rs.getLong(idCuenta);
				String tipo = rs.getString(tipoOperacion);
				Date fecha=rs.getDate(fechaOperacion);

				OperacionValue.setFecha(fecha);
				OperacionValue.setIdCliente(idClien);
				OperacionValue.setMonto(monto);
				OperacionValue.setTipo(tipo);
				if(idPrest>0)
				{
					OperacionValue.setIdPrestamo(idPrest);
					OperacionValue.setIdCuenta(-1);
				}
				else
				{
					OperacionValue.setIdCuenta(idCuent);
					OperacionValue.setIdPrestamo(-1);
				}
				Operacions.add(OperacionValue);
				OperacionValue = new Operacion();

			}
			conexion.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaOperacionesDefault+idCliente);
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
		return Operacions;
	}

	public ArrayList<Operacion> filtrarOperaciones(Date fechaMenor,Date fechaMayor, double monto,String tipo, boolean inversa) throws Exception
	{
		PreparedStatement prepStmt = null;

		ArrayList<Operacion> Operacions = new ArrayList<Operacion>();
		Operacion OperacionValue = new Operacion();
		Connection conexion=null;

		try
		{
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			String state=filtroOperaciones;
			SimpleDateFormat format=new SimpleDateFormat("DD/MM/YYYY");
			if(fechaMenor!=null)
			{
				String formateada=format.format(fechaMenor);
				state+="fecha>to_date('"+formateada+"','DD/MM/YYYY') ";
			}
			if(fechaMayor!=null)
			{
				String formateada=format.format(fechaMayor);
				if(!state.contains("fecha"))
				{
					state+="fecha<=to_date('"+formateada+"','DD/MM/YYYY') ";
				}
				else
				{
					state+="and fecha<=to_date('"+formateada+"','DD/MM/YYYY') ";
				}
			}
			if(monto!=-1)
			{
				if(!state.contains("fecha"))
				{
					if(!inversa)
					{
						state+="monto="+monto;
					}
					else
					{
						state+="monto!="+monto;
					}
				}
				else
				{
					if(!inversa)
					{
						state+="and monto="+monto;
					}
					else
					{
						state+="and monto!="+monto;
					}
				}
			}
			if(tipo!=null)
			{
				if(!state.contains("fecha") && !state.contains("monto"))
				{
					if(!inversa)
					{
						state+="tipo='"+tipo+"' ";
					}
					else
					{
						state+="tipo!='"+tipo+"' ";
					}
				}
				else
				{
					if(!inversa)
					{
						state+="and tipo='"+tipo+"' ";
					}
					else
					{
						state+="and tipo!='"+tipo+"' ";
					}
				}
			}
			prepStmt = conexion.prepareStatement(state);

			ResultSet rs = prepStmt.executeQuery();

			while(rs.next())
			{
				long idPrest = rs.getLong(idPrestamo);
				int idClien = rs.getInt(idCliente);
				long montol=rs.getLong(montoOperacion);
				long idCuent=rs.getLong(idCuenta);
				String tipol = rs.getString(tipoOperacion);
				Date fecha=rs.getDate(fechaOperacion);

				OperacionValue.setFecha(fecha);
				OperacionValue.setIdCliente(idClien);
				OperacionValue.setMonto(montol);
				OperacionValue.setTipo(tipol);
				if(idPrest>0)
				{
					OperacionValue.setIdPrestamo(idPrest);
					OperacionValue.setIdCuenta(-1);
				}
				else
				{
					OperacionValue.setIdCuenta(idCuent);
					OperacionValue.setIdPrestamo(-1);
				}
				Operacions.add(OperacionValue);
				OperacionValue = new Operacion();

			}
			conexion.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaOperacionesDefault+idCliente);
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
		return Operacions;
	}
}
