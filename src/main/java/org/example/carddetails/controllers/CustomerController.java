package org.example.carddetails.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.carddetails.dto.*;
import org.example.carddetails.models.Customer;
import org.example.carddetails.models.Hotel;
import org.example.carddetails.services.AuthService;
import org.example.carddetails.services.CustomerService;
import org.example.carddetails.services.JwtService;
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

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @QueryMapping("users")
    public List<Customer> getAllCustomers_GraphQL() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers(){
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
    public ResponseEntity<?> registerCustomer(@RequestBody RegisterRequest request) {
        try{
            AuthResponse response = authService.registerCustomer(request);
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
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateCustomerDTO updateCustomerDTO, HttpServletRequest request) {
        try{
            String userEmail = extractUserEmailFromRequest(request);
            if(customerService.updateCustomer(userEmail, updateCustomerDTO)){
                return ResponseEntity.ok().body("Customer successfully updated");
            }
            return ResponseEntity.badRequest().body("Customer not found");
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }
    //specifically for navbar/ to check weather the user is logged in
    @GetMapping("/navbar")
    public ResponseEntity<?> getNavbarDetails(HttpServletRequest request) {
        try {
            String userEmail = extractUserEmailFromRequest(request);
            if (userEmail == null || userEmail.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
            }
            CustomerProfile customerProfile = customerService.getProfileInfo(userEmail, "navbar");
            if (customerProfile == null) {
                return ResponseEntity.badRequest().body("Customer not found");
            }
            return ResponseEntity.ok().body(new NavbarResponse(true, customerProfile));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }
    //get profile details for profile page
    @GetMapping("/profile-details")
    public ResponseEntity<?> getProfileDetails(HttpServletRequest request) {
        try{
            String userEmail = extractUserEmailFromRequest(request);
            CustomerProfile customerProfile = customerService.getProfileInfo(userEmail, "profile");
            if (customerProfile == null) {
                return ResponseEntity.badRequest().body("Customer not found");
            }
            return ResponseEntity.ok().body(customerProfile);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //get profile picture
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

    //TODO fetch user details by ID along with decrypted card details


    //utility function to get JWT token form the request
    private String extractUserEmailFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Unauthorized");
        }
        final String jwtToken = authHeader.substring(7);
        return jwtService.extractUsername(jwtToken);
    }
}
