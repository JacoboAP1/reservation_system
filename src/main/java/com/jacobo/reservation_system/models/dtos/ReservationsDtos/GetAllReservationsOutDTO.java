package com.jacobo.reservation_system.models.dtos.ReservationsDtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GetAllReservationsOutDTO {
    private Long user;
    private Long resource;
    private LocalDate start_date;
    private LocalDate end_date;
    private String status;
}
