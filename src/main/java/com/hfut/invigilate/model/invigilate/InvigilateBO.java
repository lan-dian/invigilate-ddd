package com.hfut.invigilate.model.invigilate;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class InvigilateBO {

    private String name;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String address;

    private Integer studentNum;

    private String examCode;

    private Integer exchangeNum;

    private String invigilateCode;

}