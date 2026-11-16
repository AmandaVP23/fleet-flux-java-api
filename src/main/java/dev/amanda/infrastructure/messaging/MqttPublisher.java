package dev.amanda.infrastructure.messaging;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.smallrye.reactive.messaging.MutinyEmitter;
import io.smallrye.reactive.messaging.mqtt.MqttMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;

import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class MqttPublisher {

    @Inject
    @Channel("fleet-config")
    MutinyEmitter<byte[]> emitter;

    public void publish (String topic, String json) {
        emitter.sendMessageAndForget(
                MqttMessage.of(topic, json.getBytes(StandardCharsets.UTF_8), MqttQoS.AT_LEAST_ONCE));
    }
}
