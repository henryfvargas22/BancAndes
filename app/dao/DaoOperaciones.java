package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
				int idPrest = rs.getInt(idPrestamo);
				int idClien = rs.getInt(idCliente);
				long monto=rs.getLong(montoOperacion);
				int idCuent=rs.getInt(idCuenta);
				String tipo = rs.getString(tipoOperacion);
				Date fecha=rs.getDate(fechaOperacion);
				
				OperacionValue.setFecha(fecha);
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
		
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaOperacionesDefault);
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