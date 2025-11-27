package com.demo.sensor_api;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class CamelRouter extends RouteBuilder {
    
    public static final List<SensorData> DB = new ArrayList<>();

    @Override
    public void configure() throws Exception {
        
        restConfiguration()
            .component("servlet")
            .bindingMode(RestBindingMode.json);

        // --- RUTA 1: ALTA (RabbitMQ) ---
        from("spring-rabbitmq:exchange-sensores?queues=cola-sensores&routingKey=sensor.dato")
            .log("RECIBIDO DE RABBITMQ: ${body}")
            .unmarshal().json(SensorData.class)
            .process(exchange -> {
                SensorData data = exchange.getIn().getBody(SensorData.class);
                DB.add(data);
            })
            .log("GUARDADO: Total registros en memoria: " + DB.size());

        // --- RUTA 2: CONSULTA (GET) ---
        // Solución: Usamos .to() para mandar a una ruta directa interna
        rest("/api")
            .get("/sensores")
            .to("direct:listar");

        from("direct:listar")
            .setBody(constant(DB));

        // --- RUTA 3: BAJA (DELETE) ---
        rest("/api")
            .delete("/sensores/{id}")
            .to("direct:borrar");
            
        from("direct:borrar")
            .process(exchange -> {
                String id = exchange.getIn().getHeader("id", String.class);
                boolean borrado = DB.removeIf(s -> s.getId().equals(id));
                exchange.getIn().setBody(borrado ? "Sensor " + id + " eliminado" : "No encontrado");
            });
    }
}// Ruta validada para la revision final
