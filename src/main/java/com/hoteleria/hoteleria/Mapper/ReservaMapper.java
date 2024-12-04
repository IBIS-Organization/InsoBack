package com.hoteleria.hoteleria.Mapper;
import com.hoteleria.hoteleria.DTO.ReservaDTO;
import com.hoteleria.hoteleria.Entity.Reserva;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper {

    private final ModelMapper modelMapper;

    public ReservaMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ReservaDTO toDTO(Reserva reserva) {
        ReservaDTO reservaDTO = modelMapper.map(reserva, ReservaDTO.class);
        if (reserva.getHabitacion() != null) {
            reservaDTO.setHabitacionId(reserva.getHabitacion().getId());
        }
        if (reserva.getCliente() != null) {
            reservaDTO.setClienteId(Long.valueOf(reserva.getCliente().getId()));
        }
if(reserva.getHabitacion()!=null){
    reservaDTO.setHabitacionId(reserva.getHabitacion().getId());
}
        reservaDTO.setDniCliente(reserva.getDniCliente());
        return reservaDTO;
    }

    public Reserva toEntity(ReservaDTO reservaDTO) {
        Reserva reserva = modelMapper.map(reservaDTO, Reserva.class);

        reserva.setDniCliente(reservaDTO.getDniCliente());
        reserva.setNombreCliente(reservaDTO.getNombreCliente());
        return reserva;
    }
}