package controllers;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
			mensaje="No se pudo agregar el usuario, revise los datos";
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

			Logger.info("monto "+dynamicForm.get("monto"));
			Logger.info("interes "+dynamicForm.get("interes"));
			Logger.info("cuotas "+dynamicForm.get("cuotas"));
			Logger.info("diaPago "+dynamicForm.get("diaPago"));
			Logger.info("cuotaMensual "+dynamicForm.get("cuotaMensual"));
			Logger.info("cliente "+dynamicForm.get("cliente"));

			long monto=Long.parseLong(dynamicForm.get("monto"));
			double interes=Double.parseDouble(dynamicForm.get("interes"));
			int cuotas=Integer.parseInt(dynamicForm.get("cuotas"));
			int diaPago=Integer.parseInt(dynamicForm.get("diaPago"));
			int cuotaMensual=Integer.parseInt(dynamicForm.get("cuotaMensual"));
			int cliente=Integer.parseInt(dynamicForm.get("cliente"));

			BancAndes.darInstancia().agregarPrestamo(monto, interes, cuotas, diaPago, cuotaMensual, cliente);
			mensaje="Se agregó correctamente la el prestamo";
			return redirect("/gerente");
		}
		catch(Exception e){
			mensaje="No se pudo agregar el prestamo, revise los datos";
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
			mensaje="No se pudo agregar la cuenta, revise los datos";
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
		List<Operacion> operaciones;
		try
		{
			cuentas=BancAndes.darInstancia().darCuentasCliente(usuarioActual.getCedula());
			prestamos=BancAndes.darInstancia().darPrestamosCliente(usuarioActual.getCedula());
			operaciones=BancAndes.darInstancia().darOperacionesCliente(usuarioActual.getCedula());
		}
		catch(Exception e)
		{
			cuentas=new ArrayList<Cuenta>();
			prestamos=new ArrayList<Prestamo>();
			operaciones=new ArrayList<Operacion>();
		}
		redirect("/cliente");
		String msg=mensaje;
		mensaje=null;
		return(ok(cliente.render(msg, cuentas, prestamos, operaciones)));
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
			mensaje="No se pudo agregar la oficina, revise los datos";
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
			mensaje="No se pudo agregar el usuario, revise los datos";
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
		Logger.info("id "+dynamicForm.field("actual").value());
		String idCuentaOp=dynamicForm.get("select");
		String operacion=dynamicForm.get("operacion");
		String monto=dynamicForm.get("valor");
		try
		{
			long id=Long.parseLong(idCuentaOp);
			if(operacion.equals("Consignar")|operacion.equals("Retirar"))
			{
				double mont=Double.parseDouble(monto);
				BancAndes.darInstancia().insertarOperacionCuenta(mont, operacion, id);
			}
			else
			{
				int idi=Integer.parseInt(idCuentaOp);
				if(operacion.equals("PagarCuotaExtraordinaria"))
				{
					double montol=Long.parseLong(monto);
					BancAndes.darInstancia().insertarOperacionPrestamo(montol, operacion, idi);
				}
				else
				{
					BancAndes.darInstancia().insertarOperacionPrestamo(0, operacion, idi);
				}
			}
			mensaje="Se insertó la operación correctamente";
			return redirect("/cajero");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			mensaje="No se pudo completar la operación, revise los datos";
			return redirect("/cajero");
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
			mensaje="No se pudo cerrar la cuenta, revise los datos";
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
			mensaje="No se pudo cerrar el prestamo, revise los datos";
			e.printStackTrace();
			return redirect("/gerente");
		}
	}

	public Result formBusquedaClientes()
	{
		if(usuarioActual!=null)
		{
			List<Cliente> clientes;
			try 
			{
				clientes=BancAndes.darInstancia().darClientesDefault();
			} 
			catch (Exception e) 
			{
				clientes=new ArrayList<Cliente>();
			}
			boolean esGerente=BancAndes.darInstancia().esGerente(usuarioActual.getUsuario(), usuarioActual.getContrasenia());
			return ok(busqueda_clientes.render(esGerente,clientes));
		}
		return internalServerError();
	}

	public Result mostrarCliente(int idCliente)
	{
		if(usuarioActual!=null)
		{
			List<Operacion> operaciones;
			List<Cuenta> cuentas;
			List<Prestamo> prestamos;
			try
			{
				operaciones=BancAndes.darInstancia().darOperacionesCliente(idCliente);
				cuentas=BancAndes.darInstancia().darCuentasCliente(idCliente);
				prestamos=BancAndes.darInstancia().darPrestamosCliente(idCliente);
			}
			catch(Exception e)
			{
				operaciones=new ArrayList<Operacion>();
				cuentas=new ArrayList<Cuenta>();
				prestamos=new ArrayList<Prestamo>();
			}
			boolean esGerente=BancAndes.darInstancia().esGerente(usuarioActual.getUsuario(), usuarioActual.getContrasenia());
			return ok(resultado_cliente.render(esGerente, cuentas, prestamos, operaciones));
		}
		else
		{
			return internalServerError();
		}
	}

	public Result filtrarClientes() throws Exception
	{
		DynamicForm dynamicForm=Form.form().bindFromRequest();
		Logger.info("natural "+dynamicForm.field("natural").value());
		Logger.info("juridica "+dynamicForm.field("juridica").value());
		Logger.info("cuenta "+dynamicForm.get("cuenta"));
		Logger.info("saldo "+dynamicForm.get("saldo"));
		Logger.info("fechaInicio "+dynamicForm.get("fechaInicio"));
		Logger.info("fecha fin "+dynamicForm.get("fechaFinal"));
		Logger.info("valor "+dynamicForm.get("valor"));

		String natural=dynamicForm.field("natural").value();
		String juridica=dynamicForm.field("juridica").value();
		String cuenta=dynamicForm.get("cuenta");
		String saldo=dynamicForm.get("saldo");
		String fechaI=dynamicForm.get("fechaInicio");
		String fechaF=dynamicForm.get("fechaFinal");
		String valor=dynamicForm.get("valor");

		List<Cliente> clientes=BancAndes.darInstancia().darClientesDefault();
		if(natural!=null && juridica==null)
		{			
			clientes=clientes.stream().filter(c ->c.getTipo().equals("natural")).collect(Collectors.toList());
		}
		else if(juridica!=null && natural==null)
		{
			clientes=clientes.stream().filter(c ->c.getTipo().equals("legal")).collect(Collectors.toList());
		}
		if(!cuenta.equals(""))
		{
			long cuent=Long.parseLong(cuenta);
			Cuenta cuentTemp=BancAndes.darInstancia().darCuentaPorId(cuent);
			int ced=cuentTemp.getId_Cliente();
			clientes=clientes.stream().filter(c ->(c.getCedula()==ced)).collect(Collectors.toList());
		}
		if(!saldo.equals(""))
		{
			double sald=Double.parseDouble(saldo);
			List<Cuenta> cuentas=BancAndes.darInstancia().darCuentasDefault();
			List<Integer> ids=new ArrayList<Integer>();
			for(int i=0;i<cuentas.size();i++)
			{
				Cuenta temp=cuentas.get(i);
				if(temp.getMonto()==sald)
				{
					ids.add(temp.getId_Cliente());
				}
			}
			List<Cliente> resultados=new ArrayList<Cliente>();
			for(int j=0;j<ids.size();j++)
			{
				int ced=ids.get(j);
				for(int i=0;i<clientes.size();i++)
				{
					if(clientes.get(i).getCedula()==ced)
					{
						resultados.add(clientes.get(i));
					}
				}
			}
			clientes=resultados;
		}
		if(!fechaI.equals(""))
		{
			List<Operacion> ops=BancAndes.darInstancia().darOperacionesDefault();
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			Date fechaIni=format.parse(fechaI);
			ops=ops.stream().filter(o ->(fechaIni.compareTo(o.getFecha())<0)).collect(Collectors.toList());
			if(ops.size()>0)
			{
				List<Cliente> resultados=new ArrayList<Cliente>();
				for(int j=0;j<ops.size();j++)
				{
					System.out.println("ps");
					int ced=ops.get(j).getIdCliente();
					for(int i=0;i<clientes.size();i++)
					{
						if(clientes.get(i).getCedula()==ced)
						{
							resultados.add(clientes.get(i));
						}
					}
				}
				clientes=resultados;
			}
		}
		if(!fechaF.equals(""))
		{
			List<Operacion> ops=BancAndes.darInstancia().darOperacionesDefault();
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			Date fechafin=format.parse(fechaF);
			System.out.println(fechafin);
			System.out.println(ops.get(0).getFecha());
			ops=ops.stream().filter(o ->(fechafin.compareTo(o.getFecha())>0)).collect(Collectors.toList());
			if(ops.size()>0)
			{
				List<Cliente> resultados=new ArrayList<Cliente>();
				for(int i=0;i<ops.size();i++)
				{
					int ced=ops.get(i).getIdCliente();
					for(int j=0;j<clientes.size();j++)
					{
						if(clientes.get(j).getCedula()==ced)
						{
							resultados.add(clientes.get(j));
						}
					}				
				}
				clientes=resultados;
			}
		}
		if(!valor.equals(""))
		{
			double val=Double.parseDouble(valor);
			List<Operacion> ops=BancAndes.darInstancia().darOperacionesDefault();
			ops=ops.stream().filter(o ->o.getMonto()==val).collect(Collectors.toList());
			if(ops.size()>0)
			{
				List<Cliente> resultados=new ArrayList<Cliente>();
				for(int i=0;i<ops.size();i++)
				{
					int ced=ops.get(i).getIdCliente();
					for(int j=0;j<clientes.size();j++)
					{
						if(clientes.get(j).getCedula()==ced)
						{
							resultados.add(clientes.get(j));
						}
					}				
				}
				clientes=resultados;
			}
		}
		if(usuarioActual!=null)
		{
			boolean esGerente=BancAndes.darInstancia().esGerente(usuarioActual.getUsuario(), usuarioActual.getContrasenia());
			System.out.println("wee");
			return ok(busqueda_clientes.render(esGerente,clientes));
		}
		return unauthorized("Error 401 - No autorizado");
	}
	
	public Result formBusquedaCuentas()
	{
		if(usuarioActual!=null)
		{
			List<Cuenta> cuentas;
			try 
			{
				cuentas=BancAndes.darInstancia().darCuentasDefault();
			} 
			catch (Exception e) 
			{
				cuentas=new ArrayList<Cuenta>();
			}
			boolean esAdmin=BancAndes.darInstancia().esAdmin(usuarioActual.getUsuario(), usuarioActual.getContrasenia());
			if(esAdmin)
			return ok(busqueda_cuentas.render(cuentas));
		}
		return internalServerError();
	}
	
	public Result filtrarCuentas() throws Exception
	{
		DynamicForm dynamicForm=Form.form().bindFromRequest();
		Logger.info("ahorros "+dynamicForm.field("ahorros").value());
		Logger.info("corriente "+dynamicForm.field("corriente").value());
		Logger.info("cdt "+dynamicForm.field("afc").value());
		Logger.info("afc "+dynamicForm.field("cdt").value());
		Logger.info("valorInicial "+dynamicForm.get("valorInicial"));
		Logger.info("valorFinal "+dynamicForm.get("valorFinal"));
		Logger.info("fechaUltimoMov "+dynamicForm.get("fechaUltimoMovimiento"));

		String ahorros=dynamicForm.field("ahorros").value();
		String corriente=dynamicForm.field("corriente").value();
		String cdt=dynamicForm.field("cdt").value();
		String afc=dynamicForm.field("afc").value();
		String valorInicial=dynamicForm.get("valorInicial");
		String valorFinal=dynamicForm.get("valorFinal");
		String fechaU=dynamicForm.get("fechaUltimoMovimiento");

		List<Cuenta> cuentas=BancAndes.darInstancia().darCuentasDefault();
		if(ahorros!=null && corriente==null && cdt==null && afc==null)
		{			
			cuentas=cuentas.stream().filter(c ->c.getTipo().equals("savings")).collect(Collectors.toList());
		}
		else if(ahorros==null && corriente!=null && cdt==null && afc==null)
		{
			cuentas=cuentas.stream().filter(c ->c.getTipo().equals("current")).collect(Collectors.toList());
		}
		else if(ahorros==null && corriente==null && cdt!=null && afc==null)
		{
			cuentas=cuentas.stream().filter(c ->c.getTipo().equals("cdt")).collect(Collectors.toList());
		}
		else if(ahorros==null && corriente==null && cdt==null && afc!=null)
		{
			cuentas=cuentas.stream().filter(c ->c.getTipo().equals("afc")).collect(Collectors.toList());
		}
		if(!valorInicial.equals(""))
		{
			double inicial=Double.parseDouble(valorInicial);
			cuentas=cuentas.stream().filter(c ->c.getMonto()>inicial).collect(Collectors.toList());
		}
		if(!valorFinal.equals(""))
		{
			double fina=Double.parseDouble(valorFinal);
			cuentas=cuentas.stream().filter(c ->c.getMonto()>fina).collect(Collectors.toList());
		}
		if(!fechaU.equals(""))
		{
			List<Operacion> ops=BancAndes.darInstancia().darOperacionesDefault();
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			Date fechaIni=format.parse(fechaU);
			ops=ops.stream().filter(o ->(fechaIni.compareTo(o.getFecha())==0)).collect(Collectors.toList());
			if(ops.size()>0)
			{
				List<Cuenta> resultados=new ArrayList<Cuenta>();
				for(int j=0;j<ops.size();j++)
				{
					System.out.println("ps");
					long ced=ops.get(j).getIdCuenta();
					for(int i=0;i<cuentas.size();i++)
					{
						if(ced>-1)
						if(cuentas.get(i).getId()==ced)
						{
							resultados.add(cuentas.get(i));
						}
					}
				}
				cuentas=resultados;
			}
		}
		if(usuarioActual!=null)
		{
			boolean esAdmin=BancAndes.darInstancia().esAdmin(usuarioActual.getUsuario(), usuarioActual.getContrasenia());
			System.out.println("wee");
			if(esAdmin)
			return ok(busqueda_cuentas.render(cuentas));
		}
		return unauthorized("Error 401 - No autorizado");
	}
}
