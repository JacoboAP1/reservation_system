package com.jacobo.reservation_system.services.implementation;

import com.jacobo.reservation_system.exceptions.AuthExceptions.UserDeactivationException;
import com.jacobo.reservation_system.exceptions.AuthExceptions.UserNotFoundException;
import com.jacobo.reservation_system.exceptions.MissFillingFieldsException;
import com.jacobo.reservation_system.exceptions.ReservationsExceptions.ReservationDeniedException;
import com.jacobo.reservation_system.exceptions.ReservationsExceptions.ReservationDuplicatedException;
import com.jacobo.reservation_system.exceptions.ResourcesExceptions.ResourceDeactivationException;
import com.jacobo.reservation_system.exceptions.ResourcesExceptions.ResourceNotFoundException;
import com.jacobo.reservation_system.models.dtos.ReservationsDtos.CreateReservationsInDTO;
import com.jacobo.reservation_system.models.dtos.ReservationsDtos.CreateReservationsOutDTO;
import com.jacobo.reservation_system.models.entities.Reservations;
import com.jacobo.reservation_system.models.entities.Resources;
import com.jacobo.reservation_system.models.entities.Users;
import com.jacobo.reservation_system.repositories.ReservationsRepository;
import com.jacobo.reservation_system.repositories.ResourcesRepository;
import com.jacobo.reservation_system.repositories.UsersRepository;
import com.jacobo.reservation_system.services.IReservationsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service where is all business logic
 * Reservations management
 * Implements his interface
 */
@Service
public class ReservationsService implements IReservationsService {
    private final ReservationsRepository resRepo;
    private final UsersRepository userRepo;
    private final ResourcesRepository resourceRepo;

    public ReservationsService(ReservationsRepository resRepo, UsersRepository userRepo,
        ResourcesRepository resourceRepo) {

        this.resRepo = resRepo;
        this.userRepo = userRepo;
        this.resourceRepo = resourceRepo;
    }

    /**
     * Method that creates a reservation
     * Check if user and resource associated exist
     * We ask to Spring Security for the authenticated user
     * @param inDto
     * @return outDto
     */
    @Override
    public CreateReservationsOutDTO createReservations(CreateReservationsInDTO inDto) {
        // Retrieves the current authentication object from Spring Security context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Gets the username of the currently authenticated user
        String username = auth.getName();

        Users user = userRepo.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("Not username found in the data base")
        );

        Resources resource = resourceRepo.findById(inDto.getResource_id()).
                orElseThrow(() -> new ResourceNotFoundException("Enter an existing resource ID"));

        LocalDate new_start_date = inDto.getStart_date();
        LocalDate new_end_date = inDto.getEnd_date();

        if (user.getId() == null || resource.getId() == null ||
                new_start_date == null || new_end_date == null) {

            throw new MissFillingFieldsException("You must complete all the fields before " +
                    "creating");
        }

        if (!resource.isActive()) {
            throw new ResourceDeactivationException("Cannot associate a deactivated resource");
        }

        if (!user.getActive()) {
            throw new UserDeactivationException("Cannot associate a deactivated user");
        }

        // If there's a reservation with the same resource as the JSON body
        // Checks that the present dates don't overlap with the existing ones
        List<Reservations> reservations = resRepo.findOverlappingReservations(
                resource.getId(), new_start_date, new_end_date
        );

        if (!reservations.isEmpty()) {
            throw new ReservationDuplicatedException("These dates overlap with an existing one");
        }

        // If a user has more than one active reservation throws an exception
        long activeReservations = resRepo.countByUserIdAndStatus(user.getId(), "active");

        if (activeReservations > 0) {
            throw new ReservationDeniedException("You cannot have more than 1 active reservation");
        }

        Reservations reservation = new Reservations();
        reservation.setUser(user);
        reservation.setResource(resource);
        reservation.setStart_date(new_start_date);
        reservation.setEnd_date(new_end_date);
        reservation.setStatus("active");

        resRepo.save(reservation);

        CreateReservationsOutDTO outDto = new CreateReservationsOutDTO();
        outDto.setMessage("Reservation created successfully. For more info, please check " +
                "GETTERS endpoints");

        outDto.setStatus("active");

        return outDto;
    }
}
