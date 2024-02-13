package telran.forumservice.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PeriodDto {

	LocalDate dateFrom;
	LocalDate dateTo;
	
}
