package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

	//----------------------------------------------------
	//Consultas
	//----------------------------------------------------
	
	/**
	 * Consulta que devuelve isan, titulo, y año de los videos en orden alfabetico
	 */
	private static final String consultaPrestamosDefault="SELECT * FROM "+tablaPrestamo;
	
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
				
				PrestamoValue.setId(id);
				PrestamoValue.setIdCliente(idCliente);
				PrestamoValue.setCuotaMensual(cuotaMensual);
				PrestamoValue.setCuotas(cuotas);
				PrestamoValue.setDiaPago(diaPago);
				PrestamoValue.setMonto(monto);
				PrestamoValue.setInteres(interes);
				Prestamos.add(PrestamoValue);
				PrestamoValue = new Prestamo();
							
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaPrestamosDefault);
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
}
