// package com.calmaapp.Controller;

// import java.time.LocalDateTime;
// import java.util.NoSuchElementException;
// import java.util.Optional;

// import org.apache.http.HttpStatus;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.calmaapp.entity.Booking;
// import com.calmaapp.entity.BookingStatus;
// import com.calmaapp.entity.Salon;
// import com.calmaapp.entity.ServicesProvided;
// import com.calmaapp.entity.User;
// import com.calmaapp.repository.BookingRepository;
// import com.calmaapp.service.BookingService;

// @RestController
// @RequestMapping("/api/bookings")
// public class BookingController {
//     @Autowired
//     private BookingService bookingService;

//     @Autowired
//      private BookingRepository bookingRepository;

//     @PostMapping("/book")
// public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
//     // Extract the values of the properties from the Booking object
//     Long id = booking.getId();
//     LocalDateTime bookingDateTime = booking.getBookingDateTime();
//     Salon salon = booking.getSalon();
//     User user = booking.getUser();
//     ServicesProvided service = booking.getService();
//     BookingStatus status = booking.getStatus();
//     LocalDateTime startTime = booking.getStartTime();
//     LocalDateTime endTime = booking.getEndTime();
//     boolean userNotified = booking.isUserNotified();
//     boolean salonNotified = booking.isSalonNotified();
//     String confirmationCode = booking.getConfirmationCode();

//     // Create a new Booking object and set the values of the properties
//     Booking newBooking = new Booking();
//     newBooking.setBookingDateTime(bookingDateTime);
//     newBooking.setSalon(salon);
//     newBooking.setUser(user);
//     newBooking.setService(service);
//     newBooking.setStatus(status);
//     newBooking.setStartTime(startTime);
//     newBooking.setEndTime(endTime);
//     newBooking.setUserNotified(userNotified);
//     newBooking.setSalonNotified(salonNotified);
//     newBooking.setConfirmationCode(confirmationCode);

//     // Save the new Booking object to the database
//     bookingRepository.save(newBooking);

//     // Return the new Booking object as the response
//     return ResponseEntity.ok(newBooking);
// }

//    @PostMapping("/{bookingId}/confirm")
//     public ResponseEntity<?> confirmBooking(@PathVariable Long bookingId) {
//         try {
//             bookingService.updateBookingStatus(bookingId, BookingStatus.CONFIRMED);
//             return ResponseEntity.ok().build();
//         } catch (NoSuchElementException e) {
//             return ResponseEntity.notFound().build();
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
//         }
//     }

//     @PostMapping("/{bookingId}/cancel")
//     public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId) {
//         try {
//             bookingService.updateBookingStatus(bookingId, BookingStatus.CANCELLED);
//             return ResponseEntity.ok().build();
//         } catch (NoSuchElementException e) {
//             return ResponseEntity.notFound().build();
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
//         }
 
 
//     }

//     @GetMapping("/{id}")
// public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
//     // Retrieve the Booking object from the database
//     Optional<Booking> bookingOptional = bookingRepository.findById(id);

//     // Check if the Booking object is present
//     if (bookingOptional.isPresent()) {
//         // The Booking object is present, so return it as the response
//         Booking booking = bookingOptional.get();
//         return ResponseEntity.ok(booking);
//     } else {
//         // The Booking object is not present, so return a 404 Not Found response
//         return ResponseEntity.notFound().build();
//     }
// }
// }
