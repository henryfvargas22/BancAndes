package dao;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConsultaDAO 
{
		//----------------------------------------------------
		//Constantes
		//----------------------------------------------------
		/**
		 * ruta donde se encuentra el archivo de conexión.
		 */
		private static final String ARCHIVO_CONEXION = "conf/conexion.properties";
		
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
		
		private static ConsultaDAO instancia;
		
		/**
		 * constructor de la clase. No inicializa ningun atributo.
		 */
		public static ConsultaDAO darInstancia() 
		{		
			if( instancia == null )
	        {
	            instancia = new ConsultaDAO( );
	            instancia.inicializar();
	        }
	        return instancia;
		}
		
		// -------------------------------------------------
	    // Métodos
	    // -------------------------------------------------

		/**
		 * obtiene ls datos necesarios para establecer una conexion
		 * Los datos se obtienen a partir de un archivo properties.
		 * @param path ruta donde se encuentra el archivo properties.
		 */
		public void inicializar()
		{
			try
			{
				File arch= new File(ARCHIVO_CONEXION);
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
	    public Connection establecerConexion() throws SQLException
	    {
	    	try
	        {
				conexion = DriverManager.getConnection(cadenaConexion,usuario,clave);
				return conexion;
	        }
	        catch( SQLException exception )
	        {
	            throw new SQLException( "ERROR: ConsultaDAO obteniendo una conexin."+cadenaConexion );
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
}
