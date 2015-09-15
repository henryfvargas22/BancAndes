package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Usuario;

public class DaoUsuarios 
{
	/**
	 * nombre de la tabla clientes
	 */
	private static final String tablaUsuario = "usuario";
	
	/**
	 * nombre de la columna titulo_original en la tabla videos.
	 */
	private static final String nombreUsuario = "nombre";
	
	/**
	 * nombre de la columna anyo en la tabla videos.
	 */
	private static final String cedulaUsuario = "cedula";
	
	

	//----------------------------------------------------
	//Consultas
	//----------------------------------------------------
	
	/**
	 * Consulta que devuelve isan, titulo, y año de los videos en orden alfabetico
	 */
	private static final String consultaUsuariosDefault="SELECT * FROM "+tablaUsuario;
	
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
    public ArrayList<Usuario> darUsuariosDefault() throws Exception
    {
    	PreparedStatement prepStmt = null;
    	
    	ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		Usuario usuarioValue = new Usuario();
    	Connection conexion=null;
		
		try {
			conexion=ConsultaDAO.darInstancia().establecerConexion();
			prepStmt = conexion.prepareStatement(consultaUsuariosDefault);
			
			ResultSet rs = prepStmt.executeQuery();
			
			while(rs.next()){
				String nomUsu = rs.getString(nombreUsuario);
				int cedUsu = rs.getInt(cedulaUsuario);
				
				usuarioValue.setNombre(nomUsu);
				usuarioValue.setCedula(cedUsu);	
				//System.out.println(nomUsu+cedUsu);
				usuarios.add(usuarioValue);
				usuarioValue = new Usuario();
							
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(consultaUsuariosDefault);
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
		return usuarios;
    }
    
}
