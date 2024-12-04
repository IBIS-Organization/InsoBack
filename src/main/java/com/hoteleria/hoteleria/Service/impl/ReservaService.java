package com.hoteleria.hoteleria.Service.impl;

import com.hoteleria.hoteleria.DTO.ReniecResponseDTO;
import com.hoteleria.hoteleria.DTO.ReservaDTO;
import com.hoteleria.hoteleria.Entity.Cliente;
import com.hoteleria.hoteleria.Entity.Habitacion;
import com.hoteleria.hoteleria.Entity.Reserva;
import com.hoteleria.hoteleria.Mapper.ReservaMapper;
import com.hoteleria.hoteleria.Repository.ClienteRepository;
import com.hoteleria.hoteleria.Repository.HabitacionRepository;
import com.hoteleria.hoteleria.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {
    private final String apiUrl = "https://api.apis.net.pe/v2/reniec/dni?numero={dni}";
    private final String token = "apis-token-11351.k26lXiE0YoMNVWJvtKDt4ydfVyqMz72V";
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private HabitacionRepository habitacionRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ReservaMapper reservaMapper;


    public List<ReservaDTO> listarReservasPorEstado(String estado) {
        List<Reserva> reservas = reservaRepository.findByEstado(estado);
        return reservas.stream()
                .map(reservaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservaDTO realizarCheckIn(Integer reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!"Pendiente".equalsIgnoreCase(reserva.getEstado())) {
            throw new RuntimeException("La reserva no está en estado 'pendiente'.");
        }

        reserva.setEstado("Check-in");
        reserva.setFechaCheckIn(LocalDateTime.now());
        reservaRepository.save(reserva);

        return reservaMapper.toDTO(reserva);
    }


    @Transactional
    public ReservaDTO realizarCheckOut(Integer reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!"Check-in".equalsIgnoreCase(reserva.getEstado())) {
            throw new RuntimeException("La reserva no está en estado 'check-in'.");
        }

        reserva.setEstado("Check-out");
        reserva.setFechaCheckOut(LocalDateTime.now());
        reservaRepository.save(reserva);

        // Opcional: Liberar habitación
        Habitacion habitacion = reserva.getHabitacion();
        habitacion.setCantidadReservada(habitacion.getCantidadReservada() - 1);
        if (habitacion.getCantidadReservada() < habitacion.getCantidadTotal()) {
            habitacion.setDisponible(true);
        }
        habitacionRepository.save(habitacion);

        return reservaMapper.toDTO(reserva);
    }


    private ReniecResponseDTO validarDNI(String dni) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ReniecResponseDTO> response = restTemplate.exchange(
                    apiUrl, HttpMethod.GET, entity, ReniecResponseDTO.class, dni
            );

            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public List<ReservaDTO> obtenerReservasPorClienteId(Integer clienteId) {
        List<Reserva> reservas = reservaRepository.findByCliente_Id(clienteId);
        return reservas.stream()
                .map(reserva -> {
                    ReservaDTO dto = reservaMapper.toDTO(reserva);  // Mapeamos a DTO
                    dto.setHabitacionId(reserva.getHabitacion().getId());  // Solo agregar el ID de la habitación
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservaDTO crearReserva(ReservaDTO reservaDTO) {
        ReniecResponseDTO reniecResponse = validarDNI(reservaDTO.getDniCliente());
        if (reniecResponse == null) {
            throw new RuntimeException("DNI no válido o no encontrado en la API de RENIEC");
        }

        reservaDTO.setDniCliente(reniecResponse.getNumeroDocumento());
        reservaDTO.setNombreCliente(reniecResponse.getNombres() + " "
                + reniecResponse.getApellidoPaterno() + " " + reniecResponse.getApellidoMaterno());

        Reserva reserva = reservaMapper.toEntity(reservaDTO);

        Habitacion habitacion = habitacionRepository.findById(reservaDTO.getHabitacionId())
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

        Cliente cliente = clienteRepository.findById(reservaDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        reserva.setEstado("Pendiente");
        reserva.setHabitacion(habitacion);
        reserva.setCliente(cliente);


        if (habitacion.getCantidadReservada() >= habitacion.getCantidadTotal()) {
            throw new RuntimeException("No hay habitaciones disponibles de este tipo.");
        }


        habitacion.setCantidadReservada(habitacion.getCantidadReservada() + 1);


        if (habitacion.getCantidadReservada().equals(habitacion.getCantidadTotal())) {
            habitacion.setDisponible(false);
        }

        habitacionRepository.save(habitacion);
        reserva = reservaRepository.save(reserva);

        return reservaMapper.toDTO(reserva);
    }


}
