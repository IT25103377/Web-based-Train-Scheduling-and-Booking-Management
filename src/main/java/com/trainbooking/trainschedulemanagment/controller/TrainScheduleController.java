package com.trainbooking.trainschedulemanagment.controller;

import com.trainbooking.trainschedulemanagment.model.TrainSchedule;
import com.trainbooking.trainschedulemanagment.repository.StationRepository;
import com.trainbooking.trainschedulemanagment.repository.TrainRepository;
import com.trainbooking.trainschedulemanagment.repository.TrainScheduleRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/schedules")
public class TrainScheduleController {

    private final TrainScheduleRepository scheduleRepository;
    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;

    public TrainScheduleController(
            TrainScheduleRepository scheduleRepository,
            TrainRepository trainRepository,
            StationRepository stationRepository) {

        this.scheduleRepository = scheduleRepository;
        this.trainRepository = trainRepository;
        this.stationRepository = stationRepository;
    }

    // VIEW ALL SCHEDULES
    @GetMapping
    public String viewSchedules(Model model) {

        model.addAttribute(
                "schedules",
                scheduleRepository.findAll()
        );

        return "schedules/schedule-list";
    }
    // SEARCH SCHEDULES BY TRAIN NUMBER
    @GetMapping("/search")
    public String searchSchedules(
            @RequestParam("trainNumber") String trainNumber,
            Model model) {

        if (trainNumber == null || trainNumber.trim().isEmpty()) {
            model.addAttribute("schedules", scheduleRepository.findAll());
        } else {
            model.addAttribute(
                    "schedules",
                    scheduleRepository
                            .findByTrain_TrainNumberContainingIgnoreCase(
                                    trainNumber.trim()
                            )
            );
        }

        model.addAttribute("searchValue", trainNumber);

        return "schedules/schedule-list";
    }
    // FILTER SCHEDULES BY OPERATING DATE
    @GetMapping("/filter-date")
    public String filterByDate(
            @RequestParam("operatingDate") String operatingDate,
            Model model) {

        if (operatingDate == null || operatingDate.trim().isEmpty()) {

            model.addAttribute(
                    "schedules",
                    scheduleRepository.findAll()
            );

        } else {

            java.time.LocalDate date =
                    java.time.LocalDate.parse(operatingDate);

            model.addAttribute(
                    "schedules",
                    scheduleRepository.findByOperatingDate(date)
            );
        }

        model.addAttribute("selectedDate", operatingDate);

        return "schedules/schedule-list";
    }
    // OPEN ADD SCHEDULE FORM
    @GetMapping("/add")
    public String showAddForm(Model model) {

        TrainSchedule schedule = new TrainSchedule();
        schedule.setStatus("Scheduled");

        model.addAttribute("schedule", schedule);
        model.addAttribute("trains", trainRepository.findAll());
        model.addAttribute("stations", stationRepository.findAll());

        return "schedules/schedule-form";
    }

    // SAVE NEW / UPDATED SCHEDULE
    @PostMapping("/save")
    public String saveSchedule(
            @ModelAttribute("schedule") TrainSchedule schedule,
            Model model) {

        // Validation 1:
        // Departure and Arrival stations cannot be the same
        if (schedule.getDepartureStation() != null &&
                schedule.getArrivalStation() != null &&
                schedule.getDepartureStation().getStationId()
                        .equals(schedule.getArrivalStation().getStationId())) {

            model.addAttribute(
                    "errorMessage",
                    "Departure Station and Arrival Station cannot be the same."
            );

            model.addAttribute("trains", trainRepository.findAll());
            model.addAttribute("stations", stationRepository.findAll());

            return "schedules/schedule-form";
        }

        // Validation 2:
        // Arrival time must be after departure time
        if (schedule.getDepartureTime() != null &&
                schedule.getArrivalTime() != null &&
                !schedule.getArrivalTime().isAfter(schedule.getDepartureTime())) {

            model.addAttribute(
                    "errorMessage",
                    "Arrival Time must be after Departure Time."
            );

            model.addAttribute("trains", trainRepository.findAll());
            model.addAttribute("stations", stationRepository.findAll());

            return "schedules/schedule-form";
        }

        // Save only if validations are successful
        scheduleRepository.save(schedule);

        return "redirect:/schedules";
    }
    // OPEN EDIT FORM
    @GetMapping("/edit/{id}")
    public String editSchedule(
            @PathVariable Integer id,
            Model model) {

        TrainSchedule schedule =
                scheduleRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid schedule ID: " + id
                                )
                        );

        model.addAttribute("schedule", schedule);
        model.addAttribute("trains", trainRepository.findAll());
        model.addAttribute("stations", stationRepository.findAll());

        return "schedules/schedule-form";
    }

    // DELETE SCHEDULE
    @GetMapping("/delete/{id}")
    public String deleteSchedule(@PathVariable Integer id) {

        scheduleRepository.deleteById(id);

        return "redirect:/schedules";
    }
}