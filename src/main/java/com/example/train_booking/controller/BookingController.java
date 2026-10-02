package com.example.train_booking.controller;

import com.example.train_booking.entity.Booking;
import com.example.train_booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public String listBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "bookings";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Booking booking = new Booking();
        booking.setBookingDate(LocalDate.now().plusDays(1)); 
        booking.setBookingNumber("BKG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        model.addAttribute("booking", booking);
        return "create_booking";
    }

    @PostMapping
    public String saveBooking(@ModelAttribute("booking") Booking booking, BindingResult result) {

        if (booking.getId() == null) {
            booking.setBookingNumber("BKG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        if (booking.getBookingStatus() == null || booking.getBookingStatus().isEmpty()) {
            booking.setBookingStatus("CONFIRMED");
        }
        if (booking.getBookingDate() == null) {
            booking.setBookingDate(LocalDate.now().plusDays(1));
        }
        if (booking.getBookingAmount() == null) {
            booking.setBookingAmount(1200.00); // Safe default
        }
        if (booking.getPassengerName() == null || booking.getPassengerName().isEmpty()) {
            booking.setPassengerName("Passenger");
        }
        if (booking.getTrainSchedule() == null || booking.getTrainSchedule().isEmpty()) {
            booking.setTrainSchedule("Colombo Fort to Kandy (08:00 AM)");
        }
        
        bookingService.saveBooking(booking);
        return "redirect:/bookings";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Booking booking = bookingService.getBookingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking Id:" + id));
        model.addAttribute("booking", booking);
        return "edit_booking";
    }

    @PostMapping("/{id}")
    public String updateBooking(@PathVariable("id") Long id, @ModelAttribute("booking") Booking booking, BindingResult result) {
        booking.setId(id);
        if (booking.getBookingStatus() == null || booking.getBookingStatus().isEmpty()) {
            booking.setBookingStatus("CONFIRMED");
        }
        
        Booking existing = bookingService.getBookingById(id).orElse(null);
        if (existing != null && (booking.getBookingNumber() == null || booking.getBookingNumber().isEmpty())) {
            booking.setBookingNumber(existing.getBookingNumber());
        }
        
        if (booking.getBookingDate() == null) booking.setBookingDate(LocalDate.now().plusDays(1));
        if (booking.getBookingAmount() == null) booking.setBookingAmount(1200.00);
        
        bookingService.saveBooking(booking);
        return "redirect:/bookings";
    }

    @GetMapping("/delete/{id}")
    public String deleteBooking(@PathVariable("id") Long id) {
        bookingService.deleteBooking(id);
        return "redirect:/bookings";
    }
}
