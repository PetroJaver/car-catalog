package com.implemica.controller;

import com.implemica.model.dto.CarDTO;
import com.implemica.model.entity.Car;
import com.implemica.model.exceptions.CarNotFoundException;
import com.implemica.model.exceptions.DeleteFileException;
import com.implemica.model.exceptions.StorageServiceException;
import com.implemica.model.service.CarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the controller for handling REST API requests related to cars.
 */
@RequestMapping(value = "/cars", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Car", description = "Operations with car")
@RestController
public class CarsController {
    /**
     * Service layer for handling car operations.
     */
    @Autowired
    private CarService carService;

    /**
     * Create a car in the database.
     * For a successful operation, you need a jwt token, which must be passed in the header using the key "Authorization".
     *
     * @param carDTO the car data to create the car.
     * @return A {@link ResponseEntity} containing the created car and {@code HttpStatus.OK} if successful car created.
     * @throws IllegalArgumentException        if the car data is invalid.
     * @throws DataIntegrityViolationException car with the same fields as 'brand', 'model', 'bodyType', 'year',
     *                                         'transmissionType', 'engineSize' already exist.
     * @throws AuthenticationException         if the request lacks a valid JWT token.
     * @see Car
     * @see CarDTO
     * @see HttpStatus
     */
    @PostMapping
    @PreAuthorize("hasAuthority('create')")
    @Operation(summary = "Create a Car in database.",
            description = "Use this api endpoint to add a car to the database. For a successful operation, you need a jwt token, which must be passed in the header using the key \"Authorization\".",
            responses = {
                    @ApiResponse(responseCode = "201", description = "The operation is successful. The car has been added to the database."),
                    @ApiResponse(responseCode = "400", description = "Operation failed. Invalid data sent."),
                    @ApiResponse(responseCode = "401", description = "You did not pass the token in the header, or the token has expired, or the token is not valid."),
                    @ApiResponse(responseCode = "409", description = "Car with the same fields as 'brand', 'model', 'bodyType', 'year', 'transmissionType', 'engineSize' already exist.")
            })
    public ResponseEntity<Car> create(
            @Parameter(name = "body", description = "To successfully receive a response from the api, you should send the body according to the example, follow the CarDTO validation.")
            @RequestBody @Valid CarDTO carDTO) {
        Car car = carService.saveCar(carDTO);

        return new ResponseEntity<>(car, HttpStatus.CREATED);
    }

    /**
     * Get a list of all cars in the database.
     * You don't need to send any headers, and you don't need to authorize the request.
     *
     * @return A {@link ResponseEntity} containing a list of {@link Car} objects and {@code HttpStatus.OK} if successful list of cars found,
     * or {@code HttpStatus.NO_CONTENT} if there are no cars in the database.
     * @see Car
     * @see CarDTO
     * @see HttpStatus
     */
    @GetMapping
    @Operation(summary = "Get list of Cars in the database.",
            description = "Use this api endpoint to get the list of cars in the database, you don't need to send any headers, and you don't need to authorize the request.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation. API returns a list of cars from the database."),
                    @ApiResponse(responseCode = "204", description = "The operation is successful. But the list of cars in the database is empty.")
            })
    public ResponseEntity<List<Car>> getAll() {
        List<Car> cars = carService.findAll();

        return cars.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(cars, HttpStatus.OK);
    }

    /**
     * Get Car by identifier in the database.
     * You don't need to send any headers, and you don't need to authorize the request.
     *
     * @param id the unique identifier of the car.
     * @return A {@link ResponseEntity} containing the retrieved {@link Car} object and
     * {@code HttpStatus.OK} if the car successful found in the database,
     * or {@code HttpStatus.NOT_FOUND} if the car is not found in the database.
     * @throws CarNotFoundException if the car in the database is not found.
     * @see Car
     * @see CarDTO
     * @see HttpStatus
     */

    @GetMapping(value = "{id}")
    @Operation(summary = "Get Car by identifier in the database.",
            description = "Use this api endpoint to get the car by identifier in the database, you don't need to send any headers and you don't need to authorize the request.Also send the model following validation in json format.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation. API return car by identifier from the database."),
                    @ApiResponse(responseCode = "404", description = "Failed operation. Car by identifier not found.")
            })
    public ResponseEntity<Car> get(
            @ApiParam(name = "id", value = "The unique identifier of the car by which the car will be returned.", required = true, example = "1")
            @PathVariable Long id) throws CarNotFoundException {
        Car car = carService.findCarById(id);

        return car != null ? new ResponseEntity<>(car, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Update a car in the database by identifier.
     * For a successful operation, you need a jwt token, which must be passed in the header using the key "Authorization".
     *
     * @param id     the unique identifier of the car to be updated.
     * @param carDTO the car data to update the car.
     * @return A {@link ResponseEntity} containing the updated car and {@code HttpStatus.OK} if successful car updated,
     * or {@code HttpStatus.NOT_FOUND} if the car in the database is not found.
     * @throws IllegalArgumentException        if the car data is invalid.
     * @throws DataIntegrityViolationException car with the same fields as 'brand', 'model', 'bodyType', 'year', 'transmissionType', 'engineSize' already exist.
     * @throws AuthenticationException         if the request lacks a valid JWT token.
     * @throws CarNotFoundException            if the car in the database is not found.
     * @see Car
     * @see CarDTO
     * @see HttpStatus
     */

    @PutMapping(value = "{id}")
    @PreAuthorize("hasAuthority('update')")
    @Operation(summary = "Update Car by identifier in the database.",
            description = "Use this api endpoint to update a car by identifier in the database. For a successful operation, you need a jwt token, which must be passed in the header using the key \"Authorization\". Also send the model following validation in json format.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation. Car by identifier updated."),
                    @ApiResponse(responseCode = "400", description = "Operation failed. Invalid data sent."),
                    @ApiResponse(responseCode = "401", description = "You did not pass the token in the header, or the token has expired, or the token is not valid."),
                    @ApiResponse(responseCode = "404", description = "Failed operation. Car by identifier not found."),
                    @ApiResponse(responseCode = "409", description = "Car with the same fields as 'brand', 'model', 'bodyType', 'year', 'transmissionType', 'engineSize' already exist.")
            })
    public ResponseEntity<Car> update(
            @ApiParam(name = "id", value = "The unique identifier of the car by which the car will be updated.", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(name = "body", description = "To successfully receive a response from the api, you should send the body according to the example, follow the CarDTO validation.")
            @Valid @RequestBody CarDTO carDTO) throws CarNotFoundException {
        Car car = carService.updateCarById(id, carDTO);

        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    /**
     * Delete a car by identifier from the database.
     * For a successful operation, you need a JWT token, which must be passed in the header using the key "Authorization".
     *
     * @param id the unique identifier of the car to be deleted.
     * @return A {@link ResponseEntity} with {@code HttpStatus.OK} if successful car deleted,
     * or {@code HttpStatus.NOT_FOUND} if the car in the database is not found.
     * @throws AuthenticationException if the request lacks a valid JWT token.
     * @throws CarNotFoundException    if the car in the database is not found.
     * @throws DeleteFileException     if delete image from storage fail.
     */

    @DeleteMapping(value = "{id}")
    @PreAuthorize("hasAuthority('delete')")
    @Operation(summary = "Delete Car by identifier in the database.",
            description = "Use this api endpoint to delete a car by identifier from the database. For a successful operation, you need a jwt token, which must be passed in the header using the key \"Authorization\".",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation. Car by identifier has been removed from the database."),
                    @ApiResponse(responseCode = "401", description = "You did not pass the token in the header, or the token has expired, or the token is not valid."),
                    @ApiResponse(responseCode = "404", description = "Failed operation. Car by identifier not found.")
            })
    public ResponseEntity<Void> delete(
            @ApiParam(name = "id", value = "The unique identifier of the car by which the car will be delete.", required = true, example = "1")
            @PathVariable(name = "id") Long id) throws CarNotFoundException, DeleteFileException {
        carService.deleteCarById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Upload car image by car identifier in database.
     * For a successful operation, you need a jwt token, which must be passed in the header using the key "Authorization".
     *
     * @param id    the unique identifier of the car, by which the image will be uploaded.
     * @param image the new image of the car that will be changed for the car whose identifier was specified. Must not be null.
     * @return A {@link ResponseEntity} {@code HttpStatus.OK} if successful car image updated,
     * or {@code HttpStatus.NOT_FOUND} if the car in the database is not found.
     * @throws AuthenticationException if the request lacks a valid JWT token.
     * @throws CarNotFoundException    if the car in the database is not found.
     * @throws StorageServiceException if upload or delete image to storage fail.
     * @see Car
     * @see CarDTO
     * @see HttpStatus
     */

    @PreAuthorize("hasAuthority('update')")
    @PostMapping(value = "{id}/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.ALL_VALUE)
    @Operation(summary = "Upload car image by car identifier in database.",
            description = "Use this API endpoint to upload the car image by identifier. For a successful operation, a jwt token is required, which must be passed in the header using the \"Authorization\" key.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation. The image of the car by identifier has been changed."),
                    @ApiResponse(responseCode = "401", description = "You did not pass the token in the header, or the token has expired, or the token is not valid."),
                    @ApiResponse(responseCode = "404", description = "Failed operation. Car by identifier not found.")
            })
    public ResponseEntity<Void> uploadImage(
            @ApiParam(name = "id", value = "The unique identifier of the car, by which the image will be uploaded.", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(name = "image", description = "The new image of the car that will be changed for the car whose identifier was specified.")
            @RequestPart MultipartFile image) throws CarNotFoundException, StorageServiceException {
        carService.uploadImageCarById(id, image);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Handles {@link DataIntegrityViolationException} thrown in {@link CarsController},
     * and returns the client error code 409 "message" - "Car already exists!"
     *
     * @return body of the response.
     * @see DataIntegrityViolationException
     * @see CarsController
     * @see HttpStatus
     */
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public Map<String, String> handleDataIntegrityViolationException() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Car already exist!");

        return response;
    }

    /**
     * Handles exceptions to type {@link CarNotFoundException}, sets HTTP response status to 404 NOT FOUND,
     * and returns a Map containing a message field that describes the exception.
     *
     * @param ex {@link CarNotFoundException} object that needs to be handled.
     * @return Map containing a message field that describes the exception.
     */
    @ExceptionHandler(value = CarNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Map<String, String> handleCarNotFoundException(CarNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());

        return response;
    }

    /**
     * Handles exceptions to type {@link StorageServiceException}, sets HTTP response status to 400 BAD REQUEST,
     * and returns a Map containing a message field that describes the exception.
     *
     * @param ex {@link CarNotFoundException} object that needs to be handled.
     * @return Map containing a message field that describes the exception.
     */
    @ExceptionHandler(value = StorageServiceException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleCarNotFoundException(StorageServiceException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());

        return response;
    }
}

