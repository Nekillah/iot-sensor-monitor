package com.demo.sensor_api;

public class SensorData {
    private String id;
    private String tipo;
    private double valor;

    public SensorData() {}

    public SensorData(String id, String tipo, double valor) {
        this.id = id;
        this.tipo = tipo;
        this.valor = valor;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    
    @Override
    public String toString() {
        return "Sensor{id='" + id + "', tipo='" + tipo + "', valor=" + valor + "}";
    }
}