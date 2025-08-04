package com.team3.otboo.domain.feed.entity;

import com.team3.otboo.domain.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "ootds")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ootd extends BaseEntity {

	private UUID feedId;

	private UUID clothesId; // Ootd -> feed 랑 clothes 랑 연결하는 중간 테이블로 쓰임.. 그럼 이름을 왜 ootd 로 했지 ootd 랑 상관 없는데

	public static Ootd create(UUID feedId, UUID clothesId) {
		Ootd ootd = new Ootd();
		ootd.feedId = feedId;
		ootd.clothesId = clothesId;

		return ootd;
	}
}
