package domain;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class ReCatpchaResponse {

	@Getter
	@Setter
	private boolean success;

	@Getter
	@Setter
	private String hostname;

	@Getter
	@Setter
	private BigDecimal score;

	@Getter
	@Setter
	private String action;

}
