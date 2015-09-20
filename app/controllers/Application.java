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

    public Result index() 
    {
        return ok(index.render("Bienvenido"));
    }
    
    public Result admin(Usuario us)
    {
    	return ok(administrador_bancandes.render(us));
    }
    
    public Result gerente(Usuario us)
    {
    	return ok(gerente_de_oficina_bancandes.render());
    }
    
    public Result formCerrarCuenta()
    {
    	return ok(cerrar_cuenta_form.render(new ArrayList<Cuenta>()));
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
			if(BancAndes.darInstancia().esAdmin(user,pass))
			{
				return admin(usuario);
			}
			else if(BancAndes.darInstancia().esGerente(user, pass))
			{
				return gerente(usuario);
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
    
    public Result addUsuario()
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
    
    public Result addOficina()
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
    
    public Result addPunto()
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
    
    public Result addCuenta()
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
    
    public Result closeCuenta()
    {
    	try
    	{
    		DynamicForm dynamicForm = Form.form().bindFromRequest();
    	    Logger.info("Cuenta is: " + dynamicForm.get("cuenta"));
    	    String cuenta=dynamicForm.get("cuenta");
    		BancAndes.darInstancia().cerrarCuenta(Integer.parseInt(cuenta));
    		return gerente(null);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return internalServerError("Ups: "+e.getMessage());
    	}
    }
    
    public Result addPrestamo()
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
    
    public Result addOperacionCuenta()
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
    
    public Result addOperacionPrestamo()
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
