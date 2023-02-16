package com.implemica.controller;

import com.implemica.model.dto.CarDTO;
import com.implemica.model.entity.Car;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/cars", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Car", description = "Operations with car")
@RestController
public class CarsController {
    @Autowired
    private CarService carService;

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

    @GetMapping
    @Operation(summary = "Get list of Cars in the database.",
            description = "Use this api endpoint to get the list of cars in the database, you don't need to send any headers and you don't need to authenticate the request.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation. API returns a list of cars from the database."),
                    @ApiResponse(responseCode = "204", description = "The operation is successful. But the list of cars in the database is empty.")
            })
    public ResponseEntity<List<Car>> getAll() {
        List<Car> cars = carService.findAll();

        return cars.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    @Operation(summary = "Get Car by identifier in the database.",
            description = "Use this api endpoint to get the car by identifier in the database, you don't need to send any headers and you don't need to authenticate the request.Also send the model following validation in json format.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation. API return car by identifier from the database."),
                    @ApiResponse(responseCode = "404", description = "Failed operation. Car by identifier not found.")
            })
    public ResponseEntity<Car> get(
            @ApiParam(name = "id", value = "The unique identifier of the car by which the car will be returned.", required = true, example = "1")
            @PathVariable Long id) {
        Car car = carService.findCarById(id);

        return car != null ? new ResponseEntity<>(car, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

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
            @Valid @RequestBody CarDTO carDto) {
        Car car = carService.updateCarById(id, carDto);

        return car != null ? new ResponseEntity<>(car, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

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
            @PathVariable(name = "id") Long id) {
        boolean deleted = carService.deleteCarById(id);

        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

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
            @RequestPart MultipartFile image) {
        boolean imageUploaded = carService.uploadImageCarById(id, image);

        return imageUploaded ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
}

