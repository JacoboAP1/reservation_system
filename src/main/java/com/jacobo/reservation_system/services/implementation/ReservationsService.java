package com.jacobo.reservation_system.services.implementation;

import com.jacobo.reservation_system.exceptions.AuthExceptions.UserDeactivationException;
import com.jacobo.reservation_system.exceptions.AuthExceptions.UserNotFoundException;
import com.jacobo.reservation_system.exceptions.MissFillingFieldsException;
import com.jacobo.reservation_system.exceptions.ReservationsExceptions.ReservationAlreadyCanceled;
import com.jacobo.reservation_system.exceptions.ReservationsExceptions.ReservationDeniedException;
import com.jacobo.reservation_system.exceptions.ReservationsExceptions.ReservationDuplicatedException;
import com.jacobo.reservation_system.exceptions.ReservationsExceptions.ReservationNotFoundException;
import com.jacobo.reservation_system.exceptions.ResourcesExceptions.ResourceDeactivationException;
import com.jacobo.reservation_system.exceptions.ResourcesExceptions.ResourceNotFoundException;
import com.jacobo.reservation_system.models.dtos.ReservationsDtos.CreateReservationsInDTO;
import com.jacobo.reservation_system.models.dtos.ReservationsDtos.CreateReservationsOutDTO;
import com.jacobo.reservation_system.models.dtos.ReservationsDtos.DeactivateReservationsOutDTO;
import com.jacobo.reservation_system.models.dtos.ReservationsDtos.GetAllReservationsOutDTO;
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
import java.util.ArrayList;
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
                () -> new UserNotFoundException("Not user authenticated")
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
        // Looks for overlapped reservations
        List<Reservations> reservations = resRepo.findOverlappingReservations(
                resource.getId(), new_start_date, new_end_date
        );

        if (!reservations.isEmpty()) {
            throw new ReservationDuplicatedException("These dates overlap with an existing one");
        }

        // Looks for number of active reservations from a user
        long activeReservations = resRepo.countByUserIdAndStatus(user.getId(), "active");

        // If a user has more than one active reservation throws an exception
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

    /**
     * Method that lists all reservations depending on the role
     * @return outDto list
     */
    @Override
    public List<GetAllReservationsOutDTO> listReservations() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Users user = userRepo.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("Not user authenticated"));

        // Returns true if it finds any role with ADMIN's name
        // .anyMatch() return the boolean value if it's found
        boolean isAdmin = user.getRole().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));

        List<Reservations> reservations;

        if (isAdmin) {
            // ADMIN is able to see all reservations (active and inactive)
            reservations = resRepo.findAll();
        } else {
            // USER is able to see only his own reservations (active and inactive)
            reservations = resRepo.findByUserId(user.getId());
        }

        List<GetAllReservationsOutDTO> outDto = new ArrayList<>();

        for (Reservations reservation : reservations) {
            GetAllReservationsOutDTO dto = new GetAllReservationsOutDTO();

            if (isAdmin) {
                // Only ADMIN can see the associated user
                dto.setUser(reservation.getUser().getId());
            }
            dto.setResource(reservation.getResource().getId());
            dto.setStart_date(reservation.getStart_date());
            dto.setEnd_date(reservation.getEnd_date());
            dto.setStatus(reservation.getStatus());
            outDto.add(dto);
        }

        return outDto;
    }

    /**
     * Method that deactivates a reservation
     * Firstly searches for the ID
     * @param id
     * @return outDto
     */
    @Override
    public DeactivateReservationsOutDTO deactivateReservation(Long id) {
        Reservations reservation = resRepo.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Enter an existing ID"));

        if (!reservation.getStatus().equals("active")) {
            throw new ReservationAlreadyCanceled("Cannot re-deactivate a reservation");
        }

        reservation.setStatus("inactive");
        resRepo.save(reservation);

        DeactivateReservationsOutDTO outDto = new DeactivateReservationsOutDTO();
        outDto.setSuccess(true);
        outDto.setMessage("Reservation canceled successfully");

        return outDto;
    }
}
