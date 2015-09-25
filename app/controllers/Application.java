package controllers;

import java.sql.SQLException;
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
	private Usuario usuarioActual;
	public Result index() 
	{
		if(mensaje==null)
		{
			return ok(index.render("Bienvenido"));			
		}
		else
		{
			String msg=mensaje;
			mensaje=null;
			usuarioActual=null;
			return ok(index.render(msg));
		}
	}

	public Result gerente()
	{
		redirect("/gerente");
		String msg=mensaje;
		mensaje=null;
		return ok(gerente_de_oficina_bancandes.render(msg));
	}
	
	public Result cajero()
	{
		redirect("/cajero");
		String msg=mensaje;
		mensaje=null;
		return ok(cajero.render(msg,null,null,null));
	}

	public Result admin()
	{
		redirect("/admin");
		String msg=mensaje;
		mensaje=null;
		return ok(administrador_bancandes.render(msg));
	}

	public Result formCerrarCuenta()
	{
		return ok(cerrar_cuenta_form.render(new ArrayList<Cuenta>()));
	}

	public Result formCerrarPrestamo()
	{
		return ok(cerrar_prestamo_form.render(null));
	}

	public Result obtenerPrestamos()
	{
		DynamicForm dynamicForm = Form.form().bindFromRequest();
		Logger.info("Username is: " + dynamicForm.get("cliente"));
		String user=dynamicForm.get("cliente");
		ArrayList<Prestamo> prestamos=BancAndes.darInstancia().darPrestamosCliente(Integer.parseInt(user));
		return ok(cerrar_prestamo_form.render(prestamos));
	}

	public Result formCrearCliente()
	{
		return ok(registro_usuarios_form.render(true,null));
	}

	public Result createCliente()
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
			BancAndes.darInstancia().insertarUsuario(nombre, ced, usuario, clave, ed, genero, ciudad, direccion, tipo,null,false,-1);
			mensaje="Se agregó correctamente el usuario: "+nombre;
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
        
        
        public Result formCrearPrestamo()
        {
            ArrayList<Cliente> clientes;
            try{
                clientes=BancAndes.darInstancia().darClientesDefault();
            }
            catch(Exception e){
               clientes=new ArrayList<Cliente>();
            }
            return ok(registro_prestamo_form.render(clientes));
        }
        public Result createPrestamo(){
		try{
			DynamicForm dynamicForm=Form.form().bindFromRequest();
			Logger.info("id "+dynamicForm.get("id"));
			Logger.info("monto "+dynamicForm.get("monto"));
                        Logger.info("interes "+dynamicForm.get("interes"));
			Logger.info("cuotas "+dynamicForm.get("cuotas"));
                        Logger.info("diaPago "+dynamicForm.get("diaPago"));
                        Logger.info("cuotaMensual "+dynamicForm.get("cuotaMensual"));
                        Logger.info("cliente "+dynamicForm.get("cliente"));
			String id=dynamicForm.get("id");
			long monto=Long.parseLong(dynamicForm.get("monto"));
                        double interes=Double.parseDouble(dynamicForm.get("interes"));
			int cuotas=Integer.parseInt(dynamicForm.get("cuotas"));
                        int diaPago=Integer.parseInt(dynamicForm.get("diaPago"));
                        int cuotaMensual=Integer.parseInt(dynamicForm.get("cuotaMensual"));
                        int cliente=Integer.parseInt(dynamicForm.get("cliente"));
			BancAndes.darInstancia().agregarPrestamo(monto, interes, cuotas, diaPago, cuotaMensual, cliente);
			mensaje="Se agregó correctamente la el prestamo: "+id;
			return redirect("/gerente");
		}
		catch(Exception e){
			mensaje="No se pudo agregar el prestamo";
			e.printStackTrace();
			return redirect("/gerente");
		}
	}
        
        
	public Result formCrearCuenta()
	{
		ArrayList<Cliente> clientes;
		try
		{
			clientes=BancAndes.darInstancia().darClientesDefault(); 
		}
		catch(Exception e)
		{
			clientes=new ArrayList<Cliente>();
		}
		return ok(registro_cuenta_form.render(clientes));
	}

	public Result createCuenta()
	{
		try
		{
			DynamicForm dynamicForm=Form.form().bindFromRequest();
			Logger.info("tipoCuenta "+dynamicForm.get("tipoCuenta"));
			Logger.info("cliente "+dynamicForm.get("cliente"));
			Logger.info("idCuenta "+dynamicForm.get("idCuenta"));
			String tipo=dynamicForm.get("tipoCuenta");
			String cliente=dynamicForm.get("cliente");
			String idCuenta=dynamicForm.get("idCuenta");
			BancAndes.darInstancia().insertarCuenta(Integer.parseInt(idCuenta), tipo, Integer.parseInt(cliente));
			mensaje="Se agregó correctamente la cuenta: "+idCuenta;
			return redirect("/gerente");
		}
		catch(Exception e)
		{
			mensaje="No se pudo agregar la cuenta";
			e.printStackTrace();
			return redirect("/gerente");
		}
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
			usuarioActual=usuario;
			if(BancAndes.darInstancia().esAdmin(user,pass))
			{
				return admin();
			}
			else if(BancAndes.darInstancia().esGerente(user, pass))
			{
				return gerente();
			}
			else if(BancAndes.darInstancia().esCajero(user, pass))
			{
				return cajero();
			}
			else if(BancAndes.darInstancia().esCliente(user, pass))
			{
				return cliente();
			}
			else
			{
				return ok(index.render("Ingreso no permitido"));
			}
		} 
		catch (Exception e)
		{
			mensaje="Contraseña incorrecta";
			usuarioActual=null;
			return index();
		}
	}

	public Result cliente() 
	{
		List<Cuenta> cuentas;
		List<Prestamo> prestamos;
		try
		{
			cuentas=BancAndes.darInstancia().darCuentasCliente(usuarioActual.getCedula());
			prestamos=BancAndes.darInstancia().darPrestamosCliente(usuarioActual.getCedula());
		}
		catch(Exception e)
		{
			cuentas=new ArrayList<Cuenta>();
			prestamos=new ArrayList<Prestamo>();
		}
		redirect("/cliente");
		String msg=mensaje;
		mensaje=null;
		return(ok(cliente.render(msg, cuentas, prestamos)));
	}

	public Result formCrearEmpleado()
	{
		List<Oficina> oficinas;
		try 
		{
			oficinas=BancAndes.darInstancia().darOficinasDefault();
		} catch (Exception e) 
		{
			oficinas=new ArrayList<Oficina>();
		}
		return ok(registro_usuarios_form.render(false,oficinas));
	}

	public Result formCrearOficina()
	{
		List<Empleado> empleados;
		try
		{
			empleados=BancAndes.darInstancia().darGerentes();
		}
		catch(Exception e)
		{
			empleados=new ArrayList<Empleado>();
		}
		return ok(registro_oficina_form.render(empleados));
	}

	public Result createOficina()
	{
		try
		{
			DynamicForm dynamicForm=Form.form().bindFromRequest();
			Logger.info("name "+dynamicForm.get("nombre"));
			Logger.info("direccion "+dynamicForm.get("direccion"));
			Logger.info("telefono "+dynamicForm.get("telefono"));
			Logger.info("gerente "+dynamicForm.get("gerente"));
			String nombre=dynamicForm.get("nombre");
			String direccion=dynamicForm.get("direccion");
			String telefono=dynamicForm.get("telefono");
			int idGerente=Integer.parseInt(dynamicForm.get("gerente"));
			BancAndes.darInstancia().insertarOficina(nombre, direccion, telefono, idGerente);
			mensaje="Se agregó correctamente la oficina: "+nombre;
			return redirect("/admin");
		}
		catch(Exception e)
		{
			mensaje="No se pudo agregar la oficina";
			e.printStackTrace();
			return redirect("/admin");
		}
	}
	
	public Result createPuntoFisico()
	{
		try
		{
			DynamicForm dynamicForm = Form.form().bindFromRequest();
			Logger.info("oficina is: " + dynamicForm.get("oficina"));
			Oficina actual=BancAndes.darInstancia().darOficinaPorId(Integer.parseInt(dynamicForm.get("oficina")));
			Logger.info("tipo is: " + dynamicForm.get("tipo"));
			String tipo=dynamicForm.get("tipo");
			BancAndes.darInstancia().insertarPunto(tipo, actual.getDireccion(), actual.getId());
			mensaje="Se creó el punto de atención";
			return redirect("/admin");
		}
		catch(SQLException a)
		{
			mensaje="Ya existe el punto que desea añadir";
			return redirect("/admin");
		}
		catch(Exception e)
		{
			mensaje="No se pudo crear el punto, revise los datos";
			return redirect("/admin");
		}
	}

	public Result createEmpleado()
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
			Logger.info("rol "+dynamicForm.get("tipoEmpleado"));
			Logger.info("oficina "+dynamicForm.get("idOficina"));

			String tipo=dynamicForm.get("tipoCliente");
			String nombre=dynamicForm.get("nombre");
			String clave=dynamicForm.get("clave");
			String cedula=dynamicForm.get("numeroDocumento");
			String ciudad=dynamicForm.get("ciudad");
			String direccion=dynamicForm.get("direccion");
			String edad=dynamicForm.get("edad");
			String genero=dynamicForm.get("generoCliente");
			String usuario=dynamicForm.get("correo");
			String rol=dynamicForm.get("tipoEmpleado");
			String idOficina=dynamicForm.get("idOficina");
			int ced=Integer.parseInt(cedula);
			int ed=Integer.parseInt(edad);
			int idOfi=Integer.parseInt(idOficina);
			BancAndes.darInstancia().insertarUsuario(nombre, ced, usuario, clave, ed, genero, ciudad, direccion, tipo,rol,true,idOfi);
			mensaje="Se agregó correctamente el usuario: "+nombre;
			return redirect("/admin");
		}
		catch(Exception e)
		{
			mensaje="No se pudo agregar el usuario";
			e.printStackTrace();
			return redirect("/admin");
		}
	}
	
	public Result formCrearPuntoFisico()
	{
		List<Oficina> oficinas;
		try
		{
			oficinas=BancAndes.darInstancia().darOficinasDefault();
		}
		catch(Exception e)
		{
			oficinas=new ArrayList<Oficina>();
		}
		return ok(registro_punto_fisico.render(oficinas));
	}
	
	public Result formCrearOperacion()
	{
		DynamicForm dynamicForm=Form.form().bindFromRequest();
		Logger.info("idCliente "+dynamicForm.get("cliente"));
		Logger.info("cuenta "+dynamicForm.field("cuenta").value());
		Logger.info("prestamo "+dynamicForm.field("prestamo").value());
		String cliente=dynamicForm.get("cliente");
		String cuenta=dynamicForm.field("cuenta").value();
		String prestamo=dynamicForm.field("prestamo").value();
		String tipo=(cuenta==null?prestamo:cuenta);
		List<Cuenta> cuentas=new ArrayList<Cuenta>();
		List<Prestamo> prestamos=new ArrayList<Prestamo>();
		try
		{
			int idCliente=Integer.parseInt(cliente);
			if(tipo.equals("cuenta"))
			{
				cuentas=BancAndes.darInstancia().darCuentasCliente(idCliente);
			}
			else
			{
				prestamos=BancAndes.darInstancia().darPrestamosCliente(idCliente);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ok(cajero.render(mensaje, tipo,cuentas,prestamos));
	}
	
	public Result createOperacion()
	{
		DynamicForm dynamicForm=Form.form().bindFromRequest();
		Logger.info("cuenta/prestamo "+dynamicForm.get("select"));
		Logger.info("operacion "+dynamicForm.get("operacion"));
		Logger.info("valor "+dynamicForm.get("valor"));
		return ok("recibi");
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
			mensaje="No se pudo cerrar la cuenta";
			e.printStackTrace();
			return redirect("/gerente");
		}
	}

	public Result closePrestamo()
	{
		try
		{
			DynamicForm dynamicForm = Form.form().bindFromRequest();
			Logger.info("Prestamos is: " + dynamicForm.get("prestamo"));
			String prestamo=dynamicForm.get("prestamo");
			BancAndes.darInstancia().cerrarPrestamo(Integer.parseInt(prestamo));
			mensaje="Se cerró el prestamo con id: "+prestamo;
			return redirect("/gerente");
		}
		catch(Exception e)
		{
			mensaje="No se pudo cerrar el prestamo";
			e.printStackTrace();
			return redirect("/gerente");
		}
	}
}
