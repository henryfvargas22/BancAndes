package vos;

public class Empresa 
{
	private int id_Empleador;
	
	private int id_Empleado;
	
	private long id_Origen;
	
	private long id_Destino;
	
	private double monto;
	
	private String tipo;
	
	public int getId_Empleador() {
		return id_Empleador;
	}
	public void setId_Empleador(int id_Empleador) {
		this.id_Empleador = id_Empleador;
	}
	public int getId_Empleado() {
		return id_Empleado;
	}
	public void setId_Empleado(int id_Empleado) {
		this.id_Empleado = id_Empleado;
	}
	public long getId_Origen() {
		return id_Origen;
	}
	public void setId_Origen(long id_Origen) {
		this.id_Origen = id_Origen;
	}
	public long getId_Destino() {
		return id_Destino;
	}
	public void setId_Destino(long id_Destino) {
		this.id_Destino = id_Destino;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
