package pt.teixeiram2.UrlShortner.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pt.teixeiram2.UrlShortner.model.UrlMap;
import pt.teixeiram2.proto.ShortnerMessageOuterClass;

@Service
public class ShortnerMessagePublisher {

	private final KafkaTemplate<Long, byte[]> kafkaTemplate;

	public ShortnerMessagePublisher(KafkaTemplate<Long, byte[]> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void publishMessage(UrlMap urlMap) {
		ShortnerMessageOuterClass.ShortnerMessage message =
				ShortnerMessageOuterClass.ShortnerMessage.newBuilder()
						.setFullUrl(urlMap.getUrl())
						.setShortUrl(urlMap.getShortUrl())
						.build();

		kafkaTemplate.send("urlShortenMessageStream", urlMap.getChecksum(), message.toByteArray());
	}

}
