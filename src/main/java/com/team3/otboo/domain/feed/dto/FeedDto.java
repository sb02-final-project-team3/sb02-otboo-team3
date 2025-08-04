package com.team3.otboo.domain.feed.dto;


import com.team3.otboo.domain.weather.dto.WeatherDto;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record FeedDto(
	UUID id,
	Instant createdAt,
	Instant updatedAt,
	AuthorDto authorDto,
	WeatherDto weather,
	List<OotdDto> ootds, // 왜 테이블 이름을 ootd 라고 했을까 .?
	String content,
	Long likeCount,
	Integer CommentCount,
	Boolean likedByMe
) {

}
