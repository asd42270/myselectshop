package com.sparta.myselectshop.domain.naver.service;

import com.sparta.myselectshop.domain.naver.dto.ItemDto;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j(topic = "NAVER API 호출")
public class NaverApiService {

    private final RestTemplate restTemplate;

    public NaverApiService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public List<ItemDto> searchItems(String query) throws JSONException {

        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/shop.json")
                .queryParam("display", 15)
                .queryParam("query", query)
                .encode()
                .build()
                .toUri();
        log.info("uri = " + uri);

        RequestEntity<Void> request = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", "2EicTBuR_92N1ieqbAaY")
                .header("X-Naver-Client-Secret", "qrQYwYYxS1")
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        return fromJsonToItems(response.getBody());
    }

    private List<ItemDto> fromJsonToItems(String body) throws JSONException {
        JSONObject jsonObject = new JSONObject(body);

        JSONArray jsonArray = jsonObject.getJSONArray("items");
        List<ItemDto> items = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject itemJson = jsonArray.getJSONObject(i);
            ItemDto itemDto = new ItemDto(itemJson);
            items.add(itemDto);
        }

        return items;
    }
}
