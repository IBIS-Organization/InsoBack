package com.hoteleria.hoteleria.Controller;


import com.hoteleria.hoteleria.DTO.PurchaseDTO;
import com.hoteleria.hoteleria.DTO.ReservaDTO;
import com.hoteleria.hoteleria.Entity.Habitacion;
import com.hoteleria.hoteleria.Entity.Hotel;
import com.hoteleria.hoteleria.Entity.Reserva;
import com.hoteleria.hoteleria.Mapper.ReservaMapper;
import com.hoteleria.hoteleria.Repository.HotelRepository;
import com.hoteleria.hoteleria.Repository.ReservaRepository;
import com.hoteleria.hoteleria.Service.impl.HabitacionService;
import com.hoteleria.hoteleria.Service.impl.ReservaService;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Element;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.text.AttributedCharacterIterator;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/hoteles")
public class HotelController {
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private HabitacionService habitacionService;
@Autowired
private ReservaMapper reservaMapper;
    @Autowired
    private ReservaRepository reservaRepository;


    @PostMapping("/{reservaId}/check-in")
    public ResponseEntity<ReservaDTO> realizarCheckIn(@PathVariable Integer reservaId) {
        ReservaDTO reservaDTO = reservaService.realizarCheckIn(reservaId);
        return ResponseEntity.ok(reservaDTO);
    }

    @PostMapping("/{reservaId}/check-out")
    public ResponseEntity<ReservaDTO> realizarCheckOut(@PathVariable Integer reservaId) {
        ReservaDTO reservaDTO = reservaService.realizarCheckOut(reservaId);
        return ResponseEntity.ok(reservaDTO);
    }

    @GetMapping("/reservas/{estado}")
    public ResponseEntity<List<ReservaDTO>> listarReservasPorEstado(@PathVariable String estado) {
        List<ReservaDTO> reservas = reservaService.listarReservasPorEstado(estado);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/obtenerHoteles")
    public List<Hotel> obtenerHoteles() {
        return hotelRepository.findAll();
    }

    @GetMapping("/obtenerhabitacion/{id}")
    public ResponseEntity<Habitacion>obtenerHabitacion(@PathVariable Long id) {
        Optional<Habitacion> habitacion = habitacionService.getHabitacionById(id);
        return habitacion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/filtrarhabitaciones")
    public ResponseEntity<List<Habitacion>> filtrarhabitaciones(@RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
    @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
    @RequestParam("capacidad") Integer capacidad,
    @RequestParam("categoria") String categoria
    ) {
        List<Habitacion> habitaciones = habitacionService.getHabitacionesDisponibles(fechaInicio, fechaFin, capacidad, categoria);
        return ResponseEntity.ok(habitaciones);
    }

    @GetMapping("/obtenerhabitaciones")
    public ResponseEntity<List<Habitacion>> obtenerHabitaciones() {
        List<Habitacion> habitaciones = habitacionService.getHabitaciones();
        return ResponseEntity.ok(habitaciones);
    }

    @PostMapping
    public ResponseEntity<ReservaDTO> crearReserva(@RequestBody ReservaDTO reservaDTO) {
        ReservaDTO nuevaReserva = reservaService.crearReserva(reservaDTO);
        return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
    }

    @GetMapping("/usuario/{clienteId}")
    public ResponseEntity<List<ReservaDTO>> obtenerReservasPorClienteId(@PathVariable Integer clienteId) {
        List<ReservaDTO> reservas = reservaService.obtenerReservasPorClienteId(clienteId);  // Esto ya devuelve ReservaDTO
        return ResponseEntity.ok(reservas);
    }



    @PostMapping("/habitacion/{reservaId}")
    public byte[] generatePaymentHistoryPdf(@PathVariable Integer reservaId) {
        Optional<Reserva> optionalReserva = reservaRepository.findById(reservaId);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);


        if (!optionalReserva.isPresent()) {
            return baos.toByteArray();
        }
        Reserva reserva = optionalReserva.get();

        // Extraer datos dinámicos


        // Encabezado
        Table headerTable = new Table(2);
        headerTable.setWidth(UnitValue.createPercentValue(100));
        headerTable.addCell(new Cell().add(new Paragraph("Documento Interno"))
                .setFontSize(12)
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(Border.NO_BORDER));
        document.add(headerTable);

        // Título principal
        Paragraph titulo = new Paragraph( "Detalle de Reserva" )
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(titulo);

        // Información general del cliente
        document.add(new Paragraph("Información del Cliente")
                .setUnderline()
                .setFontSize(12)
                .setMarginBottom(10));
        document.add(new Paragraph("Nombre: " + reserva.getCliente().getFirstName() +" "+ reserva.getCliente().getLastName()));
        document.add(new Paragraph("Nro de Documento: " + reserva.getDniCliente()));
        document.add(new Paragraph("Fecha Inicio:" + reserva.getFechaInicio()));
        document.add(new Paragraph("Fecha Fin: " + reserva.getFechaFin()));





        // Espaciado antes de la tabla
        document.add(new Paragraph("\n"));

        // Crear la tabla de cronograma de pagos
        Table table = new Table(new float[]{1, 3, 3, 2, 3, 3, 3});
        table.setWidth(UnitValue.createPercentValue(100));
        table.addHeaderCell(new Cell().add(new Paragraph("N°")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Categoria")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Capacidad")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Precio")).setBackgroundColor(ColorConstants.LIGHT_GRAY));


        int index = 1;

            table.addCell(new Cell().add(new Paragraph(String.valueOf(index++))));
            table.addCell(new Cell().add(new Paragraph(reserva.getHabitacion().getCategoria())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(reserva.getHabitacion().getCapacidad()))));
            table.addCell(new Cell().add(new Paragraph(String.format(String.valueOf(reserva.getHabitacion().getPrecio())))));

        document.add(table);

        // Espaciado antes de la firma
        document.add(new Paragraph("\n"));

        // Campo de firma


        // Pie de página
        Paragraph footer = new Paragraph("Documento generado automáticamente por el sistema.")
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20);
        document.add(footer);

        // Cerrar el documento
        document.close();

        // Retornar como arreglo de bytes
        return baos.toByteArray();
        }
    }

