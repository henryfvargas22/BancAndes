package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Cliente;


public class DaoClientes 
{
	/**
	 * nombre de la tabla clientes
	 */
	private static final String tablaCliente = "cliente";
	
	/**
	 * nombre de la columna titulo_original en la tabla videos.
	 */
	private static final String idCliente = "id";
	
	/**
	 * nombre de la columna anyo en la tabla videos.
	 */
	private static final String idCliente_Usuario = "id_usuario";
	
	

	//----------------------------------------------------
	//Consultas
	//----------------------------------------------------
	
	/**
	 * Consulta que devuelve isan, titulo, y año de los videos en orden alfabetico
	 */
	private static final String consultaClientesDefault="SELECT * FROM "+tablaCliente;
	
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
    public ArrayList<Cliente> darClientesDefault() throws Exception
    {
    	PreparedStatement prepStmt = null;
    	
    	ArrayList<Cliente> clientes = new ArrayList<Cliente>();
		Cliente clienteValue = new Cliente();
    	Connection conexion=null;
		
		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaClientesDefault);
			
			ResultSet rs = prepStmt.executeQuery();
			
			while(rs.next())
			{
				int idCli = rs.getInt(idCliente);
				int cedCli = rs.getInt(idCliente_Usuario);
				
				clienteValue.setIdCliente(idCli);
				clienteValue.setIdUsuario(cedCli);
				clientes.add(clienteValue);
				clienteValue = new Cliente();
							
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaClientesDefault);
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
		return clientes;
    }
}
