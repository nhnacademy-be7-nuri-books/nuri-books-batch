package com.nhnacademy.nuribooksbatch.common.sender;

import java.nio.charset.StandardCharsets;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DoorayWebHookSender implements MessageSender {

	String hookUrl = "https://hook.dooray.com/services/3204376758577275363/3942311840670200442/ry-IgeXAQl6P3G1oPfSrVg";

	@Override
	public void sendMessage(MessageRequest request) {

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(hookUrl);
			httpPost.addHeader("Content-Type", "application/json; charset=UTF-8");

			HookBody hookBody = new HookBody(request.subject(), request.message());
			Gson gson = new Gson();
			StringEntity stringEntity = new StringEntity(gson.toJson(hookBody), StandardCharsets.UTF_8);
			httpPost.setEntity(stringEntity);
			CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPost);

			if (closeableHttpResponse.getStatusLine().getStatusCode() != 200) {
				log.error("두레이 메신저에서 요청이 처리되지 못했습니다.");
			}

		} catch (Exception e) {
			log.error("두레이 메신저로의 메시지 전송에 문제가 발생하였습니다. : {}", e.getMessage());
		}
	}

	public static class HookBody {
		String botName;
		String botIconImage;
		String text;

		public HookBody(String botName, String text) {
			this.botName = botName;
			this.botIconImage = "https://static.dooray.com/static_images/dooray-bot.png";
			this.text = text;
		}
	}
}
