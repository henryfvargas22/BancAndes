package vos;

public class Empleado extends Usuario
{
	private int idEmpleado;
	private String rol;
	private int idOficina;
	
	public Empleado()
	{
		super();
	}
	
	public int getIdUsuario()
	{
		return cedula;
	}
	
	public void setIdUsuario(int cedula)
	{
		this.cedula=cedula;
	}

	public int getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(int idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public int getIdOficina() {
		return idOficina;
	}

	public void setIdOficina(int idOficina) {
		this.idOficina = idOficina;
	}
	
}
