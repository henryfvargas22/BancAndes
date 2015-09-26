import dao.DaoClientes;
import dao.DaoCuentas;
import dao.DaoEmpleados;
import dao.DaoOficinas;
import dao.DaoOperaciones;
import dao.DaoPrestamos;
import dao.DaoPuntosDeAtencion;
import dao.DaoTrabaja;
import dao.DaoUsuarios;
import fachada.BancAndes;
import junit.framework.TestCase;

import static org.junit.Assert.*;

import org.junit.*;

public class PersistanceTest
{
	private DaoTrabaja daoTrabaja;
	private DaoOperaciones daoOperaciones;
	private DaoPuntosDeAtencion daoPuntosDeAtencion;
	private DaoClientes daoClientes;
	private DaoCuentas daoCuentas;
	private DaoEmpleados daoEmpleados;
	private DaoOficinas daoOficinas;
	private DaoUsuarios daoUsuarios;
	private DaoPrestamos daoPrestamos;
	
	private BancAndes fachada;
	
	@Test
	public void unicidadTuplas()
	{
		try
		{
			fachada.darInstancia().insertarUsuario("nombre", 9000, "usuario", "contrasenia", 30, "male", "ciudad", "direccion", "natural", null, false, 0);
			fachada.darInstancia().insertarUsuario("nombre", 9000, "usuario", "contrasenia", 30, "male", "ciudad", "direccion", "natural", null, false, 0);
			fail("Tupla duplicada");
		}
		catch(Exception e)
		{
			try 
			{
				fachada.darInstancia().eliminarUsuario(true, 9000);
			} 
			catch (Exception e1) 
			{
				fail("paila");
			}
		}
	}
}
