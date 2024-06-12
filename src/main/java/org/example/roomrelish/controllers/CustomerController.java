package org.example.roomrelish.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.roomrelish.dto.*;
import org.example.roomrelish.models.Customer;
import org.example.roomrelish.models.Hotel;
import org.example.roomrelish.services.AuthService;
import org.example.roomrelish.services.CustomerService;
import org.example.roomrelish.services.JwtService;
import org.jetbrains.annotations.TestOnly;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final AuthService authService;
    private final JwtService jwtService;
    String errorMessageCustomer = "Customer not found";

    //demo end point to test the authentication.
    @GetMapping("/hello")
    @TestOnly
    public String hello() {
        return "Hello World";
    }

    @QueryMapping("users")
    @TestOnly
    public List<Customer> getAllCustomersGraphQL() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/customers")
    public ResponseEntity<?> getAllCustomers(){
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @Operation(
            description = "User Registration",
            summary = "Registers a new Customer",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User Registration Successful"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request. The request body is invalid or missing required fields"
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody RegisterUserDTO request) {
        try{
            RegisterRequest registerRequest = authService.getRegisterRequest(request);
            AuthResponse response = authService.registerCustomer(registerRequest);
            return ResponseEntity.ok(response);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Operation(
            description = "User Authentication",
            summary = "Authenticates a Customer",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User Authentication Successful"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "User not found or incorrect credentials."
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        try{
            AuthResponse response = authService.authenticate(loginRequest);
            return ResponseEntity.ok(response);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @TestOnly
    // add new card details to the added cards field
    @PostMapping("/addCard")
    public ResponseEntity<?> addCardToUser(@RequestBody CardDTO cardDTO, HttpServletRequest request) {
        try {
            String userEmail = extractUserEmailFromRequest(request);
            customerService.addCardToUser(cardDTO, userEmail);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    //update user
    @Operation(
            description = "Update Customer Details",
            summary = "Updates Customer E-Mail, phone number and password",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer details updated successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Customer is not found"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error"
                    )
            }
    )
    @TestOnly
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateCustomerDTO updateCustomerDTO, HttpServletRequest request) {
        try{
            String userEmail = extractUserEmailFromRequest(request);
            if(customerService.updateCustomer(userEmail, updateCustomerDTO)){
                return ResponseEntity.ok().body("Customer successfully updated");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessageCustomer);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }
    //specifically for navbar/ to check weather the user is logged in
    @TestOnly
    @GetMapping("/navbar")
    public ResponseEntity<?> getNavbarDetails(HttpServletRequest request) {
        try {
            String userEmail = extractUserEmailFromRequest(request);
            if (userEmail == null || userEmail.isEmpty()) {
                return ResponseEntity.ok().body(new NavbarResponse(false, null));
            }
            CustomerProfile customerProfile = customerService.getProfileInfo(userEmail, "navbar");
            if (customerProfile == null) {
                return ResponseEntity.badRequest().body(errorMessageCustomer);
            }
            return ResponseEntity.ok().body(new NavbarResponse(true, customerProfile));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }
    //get profile details for profile page
    @TestOnly
    @GetMapping("/profile-details")
    public ResponseEntity<?> getProfileDetails(HttpServletRequest request) {
        try{
            String userEmail = extractUserEmailFromRequest(request);
            CustomerProfile customerProfile = customerService.getProfileInfo(userEmail, "profile");
            if (customerProfile == null) {
                return ResponseEntity.badRequest().body(errorMessageCustomer);
            }
            return ResponseEntity.ok().body(customerProfile);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //get profile picture
    @TestOnly
    @GetMapping("/profilePicture")
    public ResponseEntity<?> getProfilePicture(HttpServletRequest request) {
        try{
            String userEmail = extractUserEmailFromRequest(request);
            String profilePicture = customerService.getProfilePicture(userEmail);
            return ResponseEntity.ok().body(profilePicture);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    // add or update profile or cover picture
    @TestOnly
    @PostMapping("/addProfilePicture")
    public ResponseEntity<?> addOrUpdateProfilePhoto(@RequestParam("file") MultipartFile file, @RequestParam String type, HttpServletRequest request) {
        try{
            String userEmail = extractUserEmailFromRequest(request);
            String fileName = file.getOriginalFilename();
            if(fileName == null || fileName.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid image format");
            }
            boolean success = customerService.uploadImage(userEmail, type, fileName);
            if(success){
                return ResponseEntity.ok("Image uploaded successfully");
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    // to view list of favourite hotels
    @TestOnly
    @GetMapping("/favouriteHotels")
    public ResponseEntity<?> getFavouriteHotels(HttpServletRequest request) {
        try{
            String userEmail = extractUserEmailFromRequest(request);
            List<Hotel> favouriteHotels = customerService.getFavouriteHotels(userEmail);
            if(favouriteHotels.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hotels not found");
            else return ResponseEntity.ok(favouriteHotels);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    // to add a hotel to favourite hotels if it is present in hotel collection
    @TestOnly
    @PostMapping("/favouriteHotels/add")
    public ResponseEntity<?> addFavouriteHotel(@RequestParam String hotelId,HttpServletRequest request) {
        try{
            String userEmail = extractUserEmailFromRequest(request);
            customerService.addFavouriteHotel(userEmail, hotelId);
            return ResponseEntity.ok().body("Favourite Hotel added successfully");
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }
    // to remove a hotel from favourite hotels
    @TestOnly
    @DeleteMapping("/favouriteHotels/delete")
    public ResponseEntity<?> deleteFavouriteHotel(@RequestParam String hotelId,HttpServletRequest request) {
        try{
            String userEmail = extractUserEmailFromRequest(request);
            customerService.deleteFavouriteHotel(userEmail, hotelId);
            return ResponseEntity.ok().body("Favourite Hotel deleted successfully");
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }
    // recent searches
    @GetMapping("/recent")
    public ResponseEntity<?> getRecentHotels(HttpServletRequest request) {
        try{
            String userEmail = extractUserEmailFromRequest(request);
            List<Hotel> recentHotels = customerService.findRecentHotels(userEmail);
            return ResponseEntity.ok().body(recentHotels);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @TestOnly
    //utility function to get JWT token form the request
    public String extractUserEmailFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Unauthorized");
        }
        final String jwtToken = authHeader.substring(7);
        return jwtService.extractUsername(jwtToken);
    }

}
