package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Empleado;

public class DaoEmpleados 
{
	/**
	 * nombre de la tabla Empleados
	 */
	private static final String tablaEmpleado = "empleado";
	
	/**
	 * nombre de la columna titulo_original en la tabla videos.
	 */
	private static final String idEmpleado = "id";
	
	/**
	 * nombre de la columna anyo en la tabla videos.
	 */
	private static final String idEmpleado_Usuario = "id_usuario";
	
	

	//----------------------------------------------------
	//Consultas
	//----------------------------------------------------
	
	/**
	 * Consulta que devuelve isan, titulo, y año de los videos en orden alfabetico
	 */
	private static final String consultaEmpleadosDefault="SELECT * FROM "+tablaEmpleado;
	
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
    public ArrayList<Empleado> darEmpleadosDefault() throws Exception
    {
    	PreparedStatement prepStmt = null;
    	
    	ArrayList<Empleado> Empleados = new ArrayList<Empleado>();
		Empleado EmpleadoValue = new Empleado();
    	Connection conexion=null;
		
		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaEmpleadosDefault);
			
			ResultSet rs = prepStmt.executeQuery();
			
			while(rs.next())
			{
				int idCli = rs.getInt(idEmpleado);
				int cedCli = rs.getInt(idEmpleado_Usuario);
				
				EmpleadoValue.setIdEmpleado(idCli);
				EmpleadoValue.setIdUsuario(cedCli);
				Empleados.add(EmpleadoValue);
				EmpleadoValue = new Empleado();
							
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaEmpleadosDefault);
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
		return Empleados;
    }
}
