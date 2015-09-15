package dao;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import vos.Usuario;

public class ConsultaDAO 
{
	//----------------------------------------------------
		//Constantes
		//----------------------------------------------------
		/**
		 * ruta donde se encuentra el archivo de conexión.
		 */
		private static final String ARCHIVO_CONEXION = "conf/conexion.properties";
		
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
		

		//----------------------------------------------------
		//Atributos
		//----------------------------------------------------
		/**
		 * conexion con la base de datos
		 */
		public Connection conexion;
		
		/**
		 * nombre del usuario para conectarse a la base de datos.
		 */
		private String usuario;
		
		/**
		 * clave de conexión a la base de datos.
		 */
		private String clave;
		
		/**
		 * URL al cual se debe conectar para acceder a la base de datos.
		 */
		private String cadenaConexion;
		
		/**
		 * constructor de la clase. No inicializa ningun atributo.
		 */
		public ConsultaDAO() 
		{		
			
		}
		
		// -------------------------------------------------
	    // Métodos
	    // -------------------------------------------------

		/**
		 * obtiene ls datos necesarios para establecer una conexion
		 * Los datos se obtienen a partir de un archivo properties.
		 * @param path ruta donde se encuentra el archivo properties.
		 */
		public void inicializar(String path)
		{
			try
			{
				File arch= new File(path+ARCHIVO_CONEXION);
				Properties prop = new Properties();
				FileInputStream in = new FileInputStream( arch );

		        prop.load( in );
		        in.close( );

				cadenaConexion = prop.getProperty("url");	// El url, el usuario y passwd deben estar en un archivo de propiedades.
													// url: "jdbc:oracle:thin:@chie.uniandes.edu.co:1521:chie10";
				usuario = prop.getProperty("usuario");	// "s2501aXX";
				clave = prop.getProperty("clave");	// "c2501XX";
				final String driver = prop.getProperty("driver");
				Class.forName(driver);
			
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println(ARCHIVO_CONEXION);
			}	
		}

		/**
		 * Método que se encarga de crear la conexión con el Driver Manager
		 * a partir de los parametros recibidos.
		 * @param url direccion url de la base de datos a la cual se desea conectar
		 * @param usuario nombre del usuario que se va a conectar a la base de datos
		 * @param clave clave de acceso a la base de datos
		 * @throws SQLException si ocurre un error generando la conexión con la base de datos.
		 */
	    private void establecerConexion(String url, String usuario, String clave) throws SQLException
	    {
	    	try
	        {
				conexion = DriverManager.getConnection(url,usuario,clave);
	        }
	        catch( SQLException exception )
	        {
	            throw new SQLException( "ERROR: ConsultaDAO obteniendo una conexin."+url );
	        }
	    }
	    
	    /**
	 	 *Cierra la conexión activa a la base de datos. Además, con=null.
	     * @param con objeto de conexión a la base de datos
	     * @throws SistemaCinesException Si se presentan errores de conexión
	     */
	    public void closeConnection(Connection connection) throws Exception {        
			try {
				connection.close();
				connection = null;
			} catch (SQLException exception) {
				throw new Exception("ERROR: ConsultaDAO: closeConnection() = cerrando una conexión.");
			}
	    } 
	    
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
	    	
			try {
				establecerConexion(cadenaConexion, usuario, clave);
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
				closeConnection(conexion);
			}		
			return usuarios;
	    }
	    
}
