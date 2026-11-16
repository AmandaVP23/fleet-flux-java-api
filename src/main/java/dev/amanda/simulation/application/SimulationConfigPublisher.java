package dev.amanda.simulation.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.amanda.infrastructure.messaging.MqttPublisher;
import dev.amanda.simulation.domain.SimulationConfigChanged;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
@ApplicationScoped
@RequiredArgsConstructor
public class SimulationConfigPublisher {
    private final MqttPublisher mqtt;
    private final ObjectMapper objectMapper;

    void onChange(@Observes(during = TransactionPhase.AFTER_SUCCESS) SimulationConfigChanged event) {
        String topic = switch (event.type()) {
            case UPDATE_DEVICES -> "fleet/config/devices";
            case ROUTE_CREATED -> "fleet/config/routes";
        };
        try {
            mqtt.publish(topic, objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            log.log(Level.SEVERE, "Failed to publish simulation config event " + event, e);
        }
    }
}
