package com.example.train_scheduling_and_booking_system.service;

import com.example.train_scheduling_and_booking_system.entity.*;
import com.example.train_scheduling_and_booking_system.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CompanionRepository companionRepository;
    private final PortalContentRepository portalContentRepository;
    private final TrainScheduleRepository trainScheduleRepository;
    private final BookingRepository bookingRepository;
    private final BookingPassengerRepository bookingPassengerRepository;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final SupportTicketRepository supportTicketRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           CompanionRepository companionRepository,
                           PortalContentRepository portalContentRepository,
                           TrainScheduleRepository trainScheduleRepository,
                           BookingRepository bookingRepository,
                           BookingPassengerRepository bookingPassengerRepository,
                           FinancialTransactionRepository financialTransactionRepository,
                           SupportTicketRepository supportTicketRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.companionRepository = companionRepository;
        this.portalContentRepository = portalContentRepository;
        this.trainScheduleRepository = trainScheduleRepository;
        this.bookingRepository = bookingRepository;
        this.bookingPassengerRepository = bookingPassengerRepository;
        this.financialTransactionRepository = financialTransactionRepository;
        this.supportTicketRepository = supportTicketRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        log.info("Initializing system roles, accounts, timetables, and demo data...");

        // 1. Seed Roles
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
        Role passengerRole = createRoleIfNotFound("ROLE_PASSENGER");
        Role opsRole = createRoleIfNotFound("ROLE_OPERATIONS_MANAGER");
        Role supervisorRole = createRoleIfNotFound("ROLE_CUSTOMER_SERVICE_SUPERVISOR");
        Role coordinatorRole = createRoleIfNotFound("ROLE_SCHEDULE_COORDINATOR");
        Role financeRole = createRoleIfNotFound("ROLE_FINANCE_OFFICER");
        Role stationMasterRole = createRoleIfNotFound("ROLE_STATION_MASTER");
        Role staffRole = createRoleIfNotFound("ROLE_STATION_STAFF");

        // 2. Seed Default Staff & Passenger Accounts
        User adminUser = createUserIfNotFound("admin", "Admin@123", "System Administrator",
                "+94770000000", "admin@trainbooking.com", adminRole);

        createUserIfNotFound("coordinator", "Coord@123", "Timetable Coordinator",
                "+94770000001", "coordinator@trainbooking.com", coordinatorRole);

        createUserIfNotFound("stationstaff", "Staff@123", "Colombo Fort Station Officer",
                "+94770000002", "station@trainbooking.com", staffRole);

        createUserIfNotFound("opsmanager", "Ops@123", "Fleet Operations Manager",
                "+94770000003", "ops@trainbooking.com", opsRole);

        User supervisorUser = createUserIfNotFound("supervisor", "Super@123", "Support & Refund Supervisor",
                "+94770000004", "supervisor@trainbooking.com", supervisorRole);

        createUserIfNotFound("finance", "Finance@123", "Chief Finance Officer",
                "+94770000005", "finance@trainbooking.com", financeRole);

        User stationMasterUser = createUserIfNotFound("stationmaster", "Station@123", "Station Master",
                "+94770000006", "stationmaster@trainbooking.com", stationMasterRole);
        if (stationMasterUser.getFullName() != null && stationMasterUser.getFullName().contains("Colombo Fort")) {
            stationMasterUser.setFullName("Station Master");
            userRepository.save(stationMasterUser);
        }

        User passengerUser = createUserIfNotFound("passenger_demo", "Pass@123", "Saman Perera",
                "+94770000007", "saman@gmail.com", passengerRole);

        // 3. Seed Companions for Passenger
        if (companionRepository.findAllByUserUserId(passengerUser.getUserId()).isEmpty()) {
            Companion c1 = new Companion();
            c1.setUser(passengerUser);
            c1.setFullName("Amara Perera");
            c1.setNicOrPassport("195823401234");
            c1.setConcessionType(ConcessionType.SENIOR);
            c1.setConcessionRef("NIC-SENIOR-1958");
            companionRepository.save(c1);

            Companion c2 = new Companion();
            c2.setUser(passengerUser);
            c2.setFullName("Kavindu Perera");
            c2.setNicOrPassport("200384501234");
            c2.setConcessionType(ConcessionType.STUDENT);
            c2.setConcessionRef("SLIIT-STU-8841");
            companionRepository.save(c2);
        }

        // 4. Seed Train Schedules
        String today = LocalDate.now().toString();
        TrainSchedule s1 = createScheduleIfNotFound("1005", "Podi Menike", "Colombo Fort", "Badulla", "05:55", "15:30", today, 120, 118, BigDecimal.valueOf(1200.00), "3", "ON_TIME", 0);
        TrainSchedule s2 = createScheduleIfNotFound("1015", "Senkadagala Menike", "Colombo Fort", "Kandy", "07:00", "09:35", today, 150, 150, BigDecimal.valueOf(600.00), "1", "ON_TIME", 0);
        TrainSchedule s3 = createScheduleIfNotFound("8056", "Ruhunu Kumari", "Colombo Fort", "Galle", "06:50", "08:50", today, 180, 180, BigDecimal.valueOf(500.00), "4", "DELAYED", 15);
        TrainSchedule s4 = createScheduleIfNotFound("4077", "Yal Devi", "Colombo Fort", "Jaffna", "05:45", "12:45", today, 200, 200, BigDecimal.valueOf(1500.00), "2", "ON_TIME", 0);
        TrainSchedule s5 = createScheduleIfNotFound("1029", "Udarata Menike", "Colombo Fort", "Kandy", "15:35", "18:15", today, 140, 140, BigDecimal.valueOf(600.00), "1", "ON_TIME", 0);
        TrainSchedule s6 = createScheduleIfNotFound("8057", "Galle Commuter", "Galle", "Colombo Fort", "14:15", "16:20", today, 160, 160, BigDecimal.valueOf(500.00), "2", "ON_TIME", 0);
        TrainSchedule s7 = createScheduleIfNotFound("1006", "Badulla Express", "Badulla", "Colombo Fort", "08:30", "18:10", today, 120, 120, BigDecimal.valueOf(1200.00), "1", "ON_TIME", 0);

        // 5. Seed a Sample Booking for Demonstration
        if (bookingRepository.findAllByUserUserIdOrderByCreatedAtDesc(passengerUser.getUserId()).isEmpty()) {
            Booking booking = new Booking();
            booking.setBookingReference("BK-" + System.currentTimeMillis());
            booking.setUser(passengerUser);
            booking.setSchedule(s1);
            booking.setTravelDate(today);
            booking.setPassengerCount(2);
            booking.setTotalAmount(BigDecimal.valueOf(2400.00));
            // 1 Senior (15% off = 1020) + 1 Student (20% off = 960) = 1980, discount = 420
            booking.setDiscountAmount(BigDecimal.valueOf(420.00));
            booking.setFinalAmount(BigDecimal.valueOf(1980.00));
            booking.setStatus("CONFIRMED");

            Booking savedBooking = bookingRepository.save(booking);

            BookingPassenger bp1 = new BookingPassenger();
            bp1.setBooking(savedBooking);
            bp1.setFullName("Amara Perera");
            bp1.setNicOrPassport("195823401234");
            bp1.setConcessionType("SENIOR");
            bp1.setFareApplied(BigDecimal.valueOf(1020.00));

            BookingPassenger bp2 = new BookingPassenger();
            bp2.setBooking(savedBooking);
            bp2.setFullName("Kavindu Perera");
            bp2.setNicOrPassport("200384501234");
            bp2.setConcessionType("STUDENT");
            bp2.setFareApplied(BigDecimal.valueOf(960.00));

            bookingPassengerRepository.save(bp1);
            bookingPassengerRepository.save(bp2);

            // Financial transaction
            FinancialTransaction txn = new FinancialTransaction();
            txn.setTransactionRef("TXN-" + System.currentTimeMillis());
            txn.setBooking(savedBooking);
            txn.setAmount(BigDecimal.valueOf(1980.00));
            txn.setTransactionType("PAYMENT");
            txn.setPaymentMethod("CREDIT_CARD");
            txn.setStatus("SUCCESS");
            financialTransactionRepository.save(txn);

            // Seed sample Support Ticket
            SupportTicket ticket = new SupportTicket();
            ticket.setTicketNumber("TKT-" + (System.currentTimeMillis() - 10000));
            ticket.setUser(passengerUser);
            ticket.setBooking(savedBooking);
            ticket.setCategory("QUERY");
            ticket.setSubject("Inquiry on bicycle carriage on Podi Menike");
            ticket.setDescription("Can I carry a foldable bicycle in the luggage compartment on train 1005?");
            ticket.setStatus("RESOLVED");
            ticket.setResolutionNotes("Foldable bicycles are permitted in the brake van subject to a 200 LKR parcel surcharge.");
            ticket.setResolvedBy(supervisorUser);
            supportTicketRepository.save(ticket);
        }

        // 6. Seed Starter Landing Page CMS Content
        createPortalContentIfNotFound(
                "ALERT_BANNER",
                "Service Alert",
                "Platform upgrades in progress at Colombo Fort. Please confirm platform numbers on the live display board before boarding.",
                "LANDING_PAGE",
                adminUser
        );

        createPortalContentIfNotFound(
                "HERO_TITLE",
                "Hero Headline",
                "Modern Rail Scheduling & Instant Ticket Booking",
                "LANDING_PAGE",
                adminUser
        );

        createPortalContentIfNotFound(
                "HERO_SUBTITLE",
                "Hero Subtitle",
                "Real-time train tracking, smart companion profiles with student & senior discounts, and role-based staff operations.",
                "LANDING_PAGE",
                adminUser
        );

        createPortalContentIfNotFound(
                "ABOUT_PORTAL",
                "About Our Rail System",
                "The National Train Scheduling and Booking Portal provides enterprise-grade rail management, live station manifests, timetable adjustments, and transparent ticketing.",
                "LANDING_PAGE",
                adminUser
        );

        log.info("System data initialization complete with all 7 RBAC roles and timetable feeds.");
    }

    private Role createRoleIfNotFound(String roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseGet(() -> roleRepository.save(Role.builder().roleName(roleName).build()));
    }

    private User createUserIfNotFound(String username, String rawPassword, String fullName,
                                      String phone, String email, Role role) {
        return userRepository.findByUsername(username).orElseGet(() -> {
            Set<Role> roles = new HashSet<>();
            roles.add(role);

            User user = User.builder()
                    .username(username)
                    .passwordHash(passwordEncoder.encode(rawPassword))
                    .fullName(fullName)
                    .phoneNumber(phone)
                    .email(email)
                    .isActive(true)
                    .roles(roles)
                    .build();

            User saved = userRepository.save(user);
            log.info("Created user: {} / {}", username, rawPassword);
            return saved;
        });
    }

    private TrainSchedule createScheduleIfNotFound(String trainNumber, String trainName, String origin,
                                                   String dest, String dep, String arr, String date,
                                                   int totalSeats, int availSeats, BigDecimal baseFare,
                                                   String platform, String status, int delay) {
        return trainScheduleRepository.findByTrainNumber(trainNumber).orElseGet(() -> {
            TrainSchedule s = new TrainSchedule();
            s.setTrainNumber(trainNumber);
            s.setTrainName(trainName);
            s.setOriginStation(origin);
            s.setDestinationStation(dest);
            s.setDepartureTime(dep);
            s.setArrivalTime(arr);
            s.setTravelDate(date);
            s.setTotalSeats(totalSeats);
            s.setAvailableSeats(availSeats);
            s.setBaseFare(baseFare);
            s.setPlatformNumber(platform);
            s.setStatus(status);
            s.setDelayMinutes(delay);
            return trainScheduleRepository.save(s);
        });
    }

    private void createPortalContentIfNotFound(String key, String title, String value, String category, User adminUser) {
        if (portalContentRepository.findByContentKey(key).isEmpty()) {
            PortalContent content = PortalContent.builder()
                    .contentKey(key)
                    .title(title)
                    .contentValue(value)
                    .category(category)
                    .updatedBy(adminUser)
                    .build();
            portalContentRepository.save(content);
        }
    }
}
