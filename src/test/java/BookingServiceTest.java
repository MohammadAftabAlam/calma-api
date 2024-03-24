import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.calmaapp.service.BookingService;

public class BookingServiceTest {

    private BookingService bookingService;

    @BeforeEach
    public void setUp() {
        bookingService = new BookingService();
    }

    // @Test
    // public void testExtractUserIdFromToken_ValidToken() {
    //     // Set up mock authentication token
    //     UserDetails userDetails = new User("1", "password", true, true, true, true, List.of());
    //     UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null);
    //     SecurityContextHolder.getContext().setAuthentication(authToken);

    //     // Call the method being tested
    //     Long userId = bookingService.extractUserIdFromToken();

    //     // Assert the result
    //     assertEquals(1L, userId);
    // }

    // @Test
    // public void testExtractUserIdFromToken_InvalidToken() {
    //     // Set up mock authentication token with invalid principal
    //     UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken("invalidPrincipal", null);
    //     SecurityContextHolder.getContext().setAuthentication(authToken);

    //     // Call the method being tested
    //     Long userId = bookingService.extractUserIdFromToken();

    //     // Assert the result
    //     assertNull(userId);
    // }
    @Test
    public void testExtractUserIdFromToken_GivenToken() {
        // Set up mock authentication token with the provided token
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJwaG9uZU51bWJlciI6Ikt6a3hPREF3TURBd01EQXdNQT09Iiwic3ViIjoiS3preE9EQXdNREF3TURBd01BPT0iLCJpYXQiOjE3MDkyMjU4MDAsImV4cCI6MTc3MjI5NzgwMH0.giVaWmaBZrop9-_Acx1D0PTTOaEmbcxdFQWyqvm_RPJPKlgsM1XlGHmtDTtDpfAXAAryfPh5VBiTMmSNRPg28w";
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(token, null);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    
        // Call the method being tested
        Long userId = bookingService.extractUserIdFromToken();
    
        // Print the user ID on the console
        System.out.println("User ID: " + userId);
    }
}
