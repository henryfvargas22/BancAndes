package vos;

import java.util.Date;

public class Operacion 
{
	private Date fecha;
	private long idPrestamo;
	private int idCliente;
	private long idCuenta;
	private String tipo;
	private double monto;
	
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public long getIdPrestamo() {
		return idPrestamo;
	}
	public void setIdPrestamo(long idPrestamo) {
		this.idPrestamo = idPrestamo;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	public long getIdCuenta() {
		return idCuenta;
	}
	public void setIdCuenta(long idCuenta) {
		this.idCuenta = idCuenta;
	}
	public int getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
}
