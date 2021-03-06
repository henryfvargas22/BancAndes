package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import vos.PuntoDeAtencion;

public class DaoPuntosDeAtencion 
{
	/**
	 * nombre de la tabla Empleados
	 */
	private static final String tablaPuntoDeAtencion = "Punto_De_Atencion";
	
	/**
	 * nombre de la columna titulo_original en la tabla videos.
	 */
	private static final String idPunto_De_Atencion = "id";
	
	/**
	 * nombre de la columna anyo en la tabla videos.
	 */
	private static final String tipoPunto_De_Atencion = "tipo";
	
	private static final String localizacionPunto_De_Atencion="localizacion";
	
	private static final String idPunto_De_Atencion_Oficina="id_oficina";

	//----------------------------------------------------
	//Consultas
	//----------------------------------------------------
	
	/**
	 * Consulta que devuelve isan, titulo, y año de los videos en orden alfabetico
	 */
	private static final String consultaPunto_De_AtencionsDefault="SELECT * FROM "+tablaPuntoDeAtencion;
	
	private static final String maxIdPuntos="SELECT MAX(id) AS maximo FROM "+tablaPuntoDeAtencion;

	private static final String insertarPunto="INSERT INTO "+tablaPuntoDeAtencion+" VALUES";
	
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
    public ArrayList<PuntoDeAtencion> darPunto_De_AtencionsDefault() throws Exception
    {
    	PreparedStatement prepStmt = null;
    	
    	ArrayList<PuntoDeAtencion> Punto_De_Atencions = new ArrayList<PuntoDeAtencion>();
		PuntoDeAtencion PuntoDeAtencionValue = new PuntoDeAtencion();
    	Connection conexion=null;
		
		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaPunto_De_AtencionsDefault);
			
			ResultSet rs = prepStmt.executeQuery();
			
			while(rs.next())
			{
				int id = rs.getInt(idPunto_De_Atencion);
				String tipo = rs.getString(tipoPunto_De_Atencion);
				String localizacion=rs.getString(localizacionPunto_De_Atencion);
				int idOficina=rs.getInt(idPunto_De_Atencion_Oficina);
				
				PuntoDeAtencionValue.setId(id);
				PuntoDeAtencionValue.setIdOficina(idOficina);
				PuntoDeAtencionValue.setLocalizacion(localizacion);
				PuntoDeAtencionValue.setTipo(tipo);
				Punto_De_Atencions.add(PuntoDeAtencionValue);
				PuntoDeAtencionValue = new PuntoDeAtencion();		
			}
			conexion.commit();
		
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaPunto_De_AtencionsDefault);
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
		return Punto_De_Atencions;
    }
    
    private int mayorId() throws Exception
	{
		PreparedStatement prepStmt = null;
		Connection conexion=null;
		int valor=0;
		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(maxIdPuntos);

			ResultSet rs = prepStmt.executeQuery();
			while(rs.next())
			valor=rs.getInt("maximo");
			conexion.commit();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.out.println(maxIdPuntos);
			conexion.rollback();
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}	
		return valor+1;
	}
    
    public void registrarPunto(String tipo, String localizacion, int idOficina) throws Exception
	{
		Connection conexion=null;
		try
		{
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			Statement st=conexion.createStatement();
			st.executeUpdate(insertarPunto+"("+mayorId()+","
					+"'"+tipo+"',"+
					"'"+localizacion+"',"+
					idOficina+")");
			conexion.commit();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.out.println(insertarPunto);
			conexion.rollback();
			throw new Exception("ERROR = ConsultaDAO: loadRowsBy(..) Agregando parametros y executando el statement!!!");
		}
		finally 
		{
			ConsultaDAO.darInstancia().closeConnection(conexion);
		}	
	}
}
