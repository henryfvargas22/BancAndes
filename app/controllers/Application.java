package controllers;

import java.util.ArrayList;
import java.util.List;

import fachada.BancAndes;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import views.html.administrador_bancandes;
import vos.Cliente;
import vos.Cuenta;
import vos.Empleado;
import vos.Oficina;
import vos.Operacion;
import vos.Prestamo;
import vos.PuntoDeAtencion;
import vos.Usuario;
import views.html.*;

import static play.libs.Json.toJson;;

public class Application extends Controller {

	private String mensaje;
	public Result index() 
	{
		return ok(index.render("Bienvenido"));
	}

	public Result admin(Usuario us)
	{
		return ok(administrador_bancandes.render(us));
	}

	public Result gerente()
	{
		redirect("/gerente");
		String msg=mensaje;
		mensaje=null;
		return ok(gerente_de_oficina_bancandes.render(msg));
	}

	public Result formCerrarCuenta()
	{
		return ok(cerrar_cuenta_form.render(new ArrayList<Cuenta>()));
	}

	public Result formCrearUsuario()
	{
		return ok(registro_usuarios_form.render());
	}

	public Result createUsuario()
	{
		try
		{
			DynamicForm dynamicForm=Form.form().bindFromRequest();
			Logger.info("tipo "+dynamicForm.get("tipoCliente"));
			Logger.info("name "+dynamicForm.get("nombre"));
			Logger.info("clave "+dynamicForm.get("clave"));
			Logger.info("cedula "+dynamicForm.get("numeroDocumento"));
			Logger.info("ciudad "+dynamicForm.get("ciudad"));
			Logger.info("direccion "+dynamicForm.get("direccion"));
			Logger.info("edad "+dynamicForm.get("edad"));
			Logger.info("genero "+dynamicForm.get("generoCliente"));
			Logger.info("usuario "+dynamicForm.get("correo"));

			String tipo=dynamicForm.get("tipoCliente");
			String nombre=dynamicForm.get("nombre");
			String clave=dynamicForm.get("clave");
			String cedula=dynamicForm.get("numeroDocumento");
			String ciudad=dynamicForm.get("ciudad");
			String direccion=dynamicForm.get("direccion");
			String edad=dynamicForm.get("edad");
			String genero=dynamicForm.get("generoCliente");
			String usuario=dynamicForm.get("correo");
			int ced=Integer.parseInt(cedula);
			int ed=Integer.parseInt(edad);
			BancAndes.darInstancia().insertarUsuario(nombre, ced, usuario, clave, ed, genero, ciudad, direccion, tipo);
			mensaje="Se agregó correctamente el usuario";
			return redirect("/gerente");
		}
		catch(Exception e)
		{
			mensaje="No se pudo agregar el usuario";
			e.printStackTrace();
			return redirect("/gerente");
		}
	}
	public Result obtenerCuentas()
	{
		DynamicForm dynamicForm = Form.form().bindFromRequest();
		Logger.info("Username is: " + dynamicForm.get("cliente"));
		String user=dynamicForm.get("cliente");
		ArrayList<Cuenta> cuentas=BancAndes.darInstancia().darCuentasCliente(Integer.parseInt(user));
		return ok(cerrar_cuenta_form.render(cuentas));
	}

	public Result login() 
	{
		DynamicForm dynamicForm = Form.form().bindFromRequest();
		Logger.info("Username is: " + dynamicForm.get("username"));
		Logger.info("Password is: " + dynamicForm.get("password"));
		String user=dynamicForm.get("username");
		String pass=dynamicForm.get("password");
		try
		{
			Usuario usuario=BancAndes.darInstancia().iniciarSesion(dynamicForm.get("username"), dynamicForm.get("password"));
			mensaje="Bienvenido(a) "+usuario.getNombre();
			if(BancAndes.darInstancia().esAdmin(user,pass))
			{
				return admin(usuario);
			}
			else if(BancAndes.darInstancia().esGerente(user, pass))
			{
				return gerente();
			}
			else
			{
				return ok(index.render("Ingreso no permitido"));
			}
		} 
		catch (Exception e)
		{
			return internalServerError("Ups: "+"Contraseña incorrecta");
		}
	}

	public Result getUsuarios()
	{
		try 
		{
			List<Usuario> usuarios=BancAndes.darInstancia().darUsuariosDefault();
			return ok(toJson(usuarios));
		} 
		catch (Exception e) 
		{
			return internalServerError("Ups: "+e.getMessage());
		}
	}

	public Result getClientes()
	{
		try 
		{
			List<Cliente> clientes=BancAndes.darInstancia().darClientesDefault();
			return ok(toJson(clientes));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return internalServerError("Ups: "+e.getMessage());
		}
	}

	public Result getEmpleados()
	{
		try 
		{
			List<Empleado> empleados=BancAndes.darInstancia().darEmpleadosDefault();
			return ok(toJson(empleados));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return internalServerError("Ups: "+e.getMessage());
		}
	}

	public Result getCuentas()
	{
		try 
		{
			List<Cuenta> cuentas=BancAndes.darInstancia().darCuentasDefault();
			return ok(toJson(cuentas));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return internalServerError("Ups: "+e.getMessage());
		}
	}

	public Result getOficinas()
	{
		try 
		{
			List<Oficina> oficinas=BancAndes.darInstancia().darOficinasDefault();
			return ok(toJson(oficinas));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return internalServerError("Ups: "+e.getMessage());
		}
	}

	public Result getPrestamos()
	{
		try 
		{
			List<Prestamo> prestamos=BancAndes.darInstancia().darPrestamosDefault();
			return ok(toJson(prestamos));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return internalServerError("Ups: "+e.getMessage());
		}
	}

	public Result getPuntos()
	{
		try 
		{
			List<PuntoDeAtencion> puntos=BancAndes.darInstancia().darPuntosDeAtencionDefault();
			return ok(toJson(puntos));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return internalServerError("Ups: "+e.getMessage());
		}
	}

	public Result getOperaciones()
	{
		try 
		{
			List<Operacion> operaciones=BancAndes.darInstancia().darOperacionesDefault();
			return ok(toJson(operaciones));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return internalServerError("Ups: "+e.getMessage());
		}
	}

	public Result closeCuenta()
	{
		try
		{
			DynamicForm dynamicForm = Form.form().bindFromRequest();
			Logger.info("Cuenta is: " + dynamicForm.get("cuenta"));
			String cuenta=dynamicForm.get("cuenta");
			BancAndes.darInstancia().cerrarCuenta(Long.parseLong(cuenta));
			mensaje="Se cerró la cuenta con id: "+cuenta;
			return redirect("/gerente");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return internalServerError("Ups: "+e.getMessage());
		}
	}

	public Result closePrestamo()
	{
		try
		{
			DynamicForm dynamicForm = Form.form().bindFromRequest();
			Logger.info("Username is: " + dynamicForm.get("username"));
			Logger.info("Password is: " + dynamicForm.get("password"));
			String usuario=dynamicForm.get("username");
			return ok("ok, I recived POST data. That's all...");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return internalServerError("Ups: "+e.getMessage());
		}
	}
}
