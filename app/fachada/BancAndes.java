package fachada;

import java.sql.SQLException;
import java.util.ArrayList;
import dao.DaoClientes;
import dao.DaoCuentas;
import dao.DaoEmpleados;
import dao.DaoOficinas;
import dao.DaoOperaciones;
import dao.DaoPrestamos;
import dao.DaoPuntosDeAtencion;
import dao.DaoTrabaja;
import dao.DaoUsuarios;
import vos.Cliente;
import vos.Cuenta;
import vos.Empleado;
import vos.Oficina;
import vos.Operacion;
import vos.Prestamo;
import vos.PuntoDeAtencion;
import vos.Usuario;


public class BancAndes 
{
	/**
	 * Conexión con la clase que maneja la base de datos
	 */
	private DaoUsuarios daoUsuarios;

	private DaoClientes daoClientes;

	private DaoEmpleados daoEmpleados;

	private DaoCuentas daoCuentas;

	private DaoOficinas daoOficinas;

	private DaoPrestamos daoPrestamos;

	private DaoPuntosDeAtencion	daoPuntosDeAtencion;

	private DaoOperaciones daoOperaciones;
	
	private DaoTrabaja daoTrabaja;

	// -----------------------------------------------------------------
	// Singleton
	// -----------------------------------------------------------------


	/**
	 * Instancia única de la clase
	 */
	private static BancAndes instancia;

	/**
	 * Devuelve la instancia única de la clase
	 * @return Instancia única de la clase
	 */
	public static BancAndes darInstancia( )
	{
		if( instancia == null )
		{
			instancia = new BancAndes( );
		}
		return instancia;
	}

	/**
	 * contructor de la clase. Inicializa el atributo dao.
	 */
	private BancAndes()
	{
		daoUsuarios = new DaoUsuarios();
		daoClientes= new DaoClientes();
		daoEmpleados= new DaoEmpleados();
		daoCuentas= new DaoCuentas();
		daoOficinas= new DaoOficinas();
		daoPrestamos= new DaoPrestamos();
		daoPuntosDeAtencion= new DaoPuntosDeAtencion();
		daoOperaciones= new DaoOperaciones();
		daoTrabaja=new DaoTrabaja();
	}


	// ---------------------------------------------------
	// Métodos asociados a los casos de uso: Consulta
	// ---------------------------------------------------

	/**
	 * método que retorna los videos en orden alfabético.
	 * invoca al DAO para obtener los resultados.
	 * @return ArrayList lista con los videos ordenados alfabeticamente.
	 * @throws Exception pasa la excepción generada por el DAO
	 */
	public ArrayList<Usuario> darUsuariosDefault() throws Exception
	{
		return daoUsuarios.darUsuariosDefault();
	}

	public ArrayList<Cliente> darClientesDefault() throws Exception
	{
		return daoClientes.darClientesDefault();
	}

	public ArrayList<Empleado> darEmpleadosDefault() throws Exception
	{
		return daoEmpleados.darEmpleadosDefault();
	}

	public ArrayList<Cuenta> darCuentasDefault() throws Exception
	{
		return daoCuentas.darCuentasDefault();
	}

	public ArrayList<Oficina> darOficinasDefault() throws Exception
	{
		return daoOficinas.darOficinasDefault();
	}

	public ArrayList<Prestamo> darPrestamosDefault() throws Exception
	{
		return daoPrestamos.darPrestamosDefault();
	}

	public ArrayList<PuntoDeAtencion> darPuntosDeAtencionDefault() throws Exception
	{
		return daoPuntosDeAtencion.darPunto_De_AtencionsDefault();
	}

	public void insertarUsuario(String nombre,int cedula, String usuario, String contrasenia, int edad, String genero, String ciudad, String direccion, String tipo,String rol, boolean esEmpleado, int idOficina) throws Exception
	{
		try
		{
			daoUsuarios.registrarUsuario(nombre, cedula, usuario, contrasenia, edad, genero, ciudad, direccion, tipo);
		}
		catch(SQLException e)
		{
			//Ya estaba agregado
		}
		catch(Exception a)
		{
			throw a;
		}
		if(esEmpleado)
		{
			try
			{
				daoEmpleados.registrarEmpleado(cedula,rol,idOficina);
				Empleado nuevo=daoEmpleados.darEmpleadoId(cedula);
				int idEmple=nuevo.getIdEmpleado();
				if(!rol.equals("admin"))
				{
					daoTrabaja.registrarTrabaja(idEmple, idOficina);
				}
			}
			catch(Exception e)
			{
				throw new Exception("No se pudo agregar el empleado, ya es usuario");
			}
		}
		else
		{
			try
			{
				daoClientes.registrarCliente(cedula);
			}
			catch(Exception e)
			{
				throw new Exception("No se pudo agregar el cliente, ya es usuario");
			}
		}
	}

	public void insertarOficina(String nombre, String direccion, String telefono, int idGerente) throws Exception
	{
		daoOficinas.registrarOficina(nombre, direccion, telefono, idGerente);
	}

	public void insertarPunto(String tipo, String localizacion, int idOficina) throws Exception
	{
		daoPuntosDeAtencion.registrarPunto(tipo, localizacion, idOficina);
	}

	public void insertarCuenta(int id, String tipo, int idCliente) throws Exception
	{
		daoCuentas.registrarCuenta(id, tipo, idCliente);
	}

	public void cerrarCuenta(long id) throws Exception
	{
		daoCuentas.cerrarCuenta(id);
	}

	public void agregarPrestamo(long monto,double interes,int cuotas,int diaPago,int cuotaMensual, int idCliente) throws Exception
	{
		daoPrestamos.registrarPrestamo(monto, interes, cuotas, diaPago, cuotaMensual, idCliente);
	}

	public void cerrarPrestamo(int id) throws Exception
	{
		daoPrestamos.cerrarPrestamo(id);
	}

	public ArrayList<Operacion> darOperacionesDefault() throws Exception
	{
		return daoOperaciones.darOperacionesDefault();
	}

	public void insertarOperacionCuenta(double monto,String tipo, long idCuent) throws Exception
	{
		Cuenta actual=daoCuentas.darCuentaId(idCuent);
		if(!actual.isEstaCerrada())
		{
			int idClient=actual.getId_Cliente();
			daoOperaciones.registrarOperacionCuenta(idClient, monto, tipo, idCuent);
			if(tipo.equals("Consignar"))
			{
				daoCuentas.actualizarMonto(idCuent, monto);
			}
			else
			{
				double monto2=monto*-1;
				daoCuentas.actualizarMonto(idCuent, monto2);
			}
		}
		else
		{
			throw new Exception("Esta cerrada la cuenta");
		}
	}

	public void insertarOperacionPrestamo(long monto,String tipo, int idPrestam) throws Exception
	{
		Prestamo actual=daoPrestamos.darPrestamoId(idPrestam);
		if(!actual.isCerrado())
		{
			int idClient=actual.getIdCliente();
			if(tipo.equals("PagarCuota"))
			{
				long monto2=actual.getMonto();
				daoOperaciones.registrarOperacionPrestamo(idClient, monto2, tipo, idPrestam);
				daoPrestamos.actualizarMonto(idPrestam, monto2*-1);
			}
			else
			{
				daoOperaciones.registrarOperacionPrestamo(idClient, monto, tipo, idPrestam);
				daoPrestamos.actualizarMonto(idPrestam, monto*-1);
			}
		}
	}

	public boolean esAdmin(String usuario,String contrasenia)
	{
		try
		{
			Empleado es=daoEmpleados.iniciarSesion(usuario, contrasenia);
			if(es!=null)
			{
				return (es.getRol().equals("admin")?true:false);
			}
		}
		catch(Exception e)
		{
			return false;
		}
		return false;
	}

	public boolean esGerente(String usuario, String contrasenia)
	{
		try
		{
			Empleado es=daoEmpleados.iniciarSesion(usuario, contrasenia);
			if(es!=null)
			{
				return (es.getRol().equals("gerente")?true:false);
			}
		}
		catch(Exception e)
		{
			return false;
		}
		return false;
	}
	
	public boolean esCliente(String usuario, String contrasenia)
	{
		try
		{
			Cliente es=daoClientes.iniciarSesion(usuario, contrasenia);
			if(es!=null)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public boolean esCajero(String usuario, String contrasenia)
	{
		try
		{
			Empleado es=daoEmpleados.iniciarSesion(usuario, contrasenia);
			if(es!=null)
			{
				return (es.getRol().equals("cajero")?true:false);
			}
		}
		catch(Exception e)
		{
			return false;
		}
		return false;
	}

	public Usuario iniciarSesion(String usuario, String contrasenia) throws Exception
	{
		return daoUsuarios.iniciarSesion(usuario, contrasenia);
	}

	public ArrayList<Cuenta> darCuentasCliente(int idCliente)
	{
		try
		{
			return daoCuentas.darCuentasCliente(idCliente);
		}
		catch(Exception e)
		{
			return new ArrayList<Cuenta>();
		}
	}

	public ArrayList<Prestamo> darPrestamosCliente(int idCliente)
	{
		try
		{
			return daoPrestamos.darPrestamosCliente(idCliente);
		}
		catch(Exception e)
		{
			return new ArrayList<Prestamo>();
		}
	}

	public ArrayList<Empleado> darGerentes() throws Exception
	{
		return daoEmpleados.darGerentes();
	}
	
	public Oficina darOficinaPorId(int idOficina)
	{
		try
		{
			return daoOficinas.darOficinaPorId(idOficina);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public ArrayList<Empleado> darEmpleadosPorOficina(int idOficina)
	{
		ArrayList<Empleado> resp=new ArrayList<Empleado>();
		try
		{
			ArrayList<Integer> empleados=daoTrabaja.darIdEmpleadosOficina(idOficina);
			ArrayList<Empleado> empleadosDefault=darEmpleadosDefault();
			for(int i=0;i<empleadosDefault.size();i++)
			{
				for(int j=0;j<empleados.size();j++)
				{
					Empleado temp=empleadosDefault.get(i);
					int idTemp=empleados.get(j);
					if(temp.getIdEmpleado()==idTemp)
					{
						resp.add(temp);
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return resp;
	}
}
