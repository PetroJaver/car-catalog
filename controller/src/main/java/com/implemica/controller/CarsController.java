package com.implemica.controller;

import com.implemica.model.dto.CarDTO;
import com.implemica.model.entity.Car;
import com.implemica.model.service.CarServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/")
@Api(tags = "Car", description = "Operations with car", protocols = "http")
@RestController()
public class CarsController {
    @Autowired
    CarServiceImpl carService;

    @Operation(summary = "Create a Car in database.", description = "Use this api endpoint to add a car to the database. " +
            "For a successful operation, you need a jwt token, which must be passed in the header using the key \"Authorization\".", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "The operation is successful. The car has been added to the database."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Operation failed. Invalid data sent."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "You did not pass the token in the header, or the token has expired, or the token is not valid."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Car with the same fields as 'brand', 'model', 'bodyType', 'year', 'transmissionType', 'engineSize' already exist.")
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "cars", produces = "application/json")
    @PreAuthorize("hasAuthority('cars:create')")
    public ResponseEntity<Car> create(@Parameter(name = "body", description = "To successfully receive a response from the api," +
            " you should send the body according to the example, follow the CarDTO validation.") @RequestBody @Valid CarDTO carDTO) {
        try {
            Car car = carService.saveCar(carDTO);

            return new ResponseEntity(car, HttpStatus.CREATED);
        }catch (DataIntegrityViolationException e){
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "Get list of Cars in the database.", description = "Use this api endpoint to get the list of cars in the database, " +
            "you don't need to send any headers and you don't need to authenticate the request.", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation. API returns a list of cars from the database.", content = @Content(mediaType = "application/json",
                    array = @ArraySchema(arraySchema = @Schema(implementation = Car.class)))),
            @ApiResponse(responseCode = "204", description = "The operation is successful. But the list of cars in the database is empty.", content = @Content)
    })
    @GetMapping(value = "cars", produces = "application/json")
    public ResponseEntity<List<Car>> getAll() {
        final List<Car> cars = carService.findAll();
        return cars != null && !cars.isEmpty() ? new ResponseEntity<>(cars, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get Car by identifier in the database.", description = "Use this api endpoint to get the car by identifier in the database, " +
            "you don't need to send any headers and you don't need to authenticate the request.Also send the model following validation in json format.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successful operation. " +
                            "API return car by identifier from the database.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Failed operation. Car by identifier not found.")
            })
    @GetMapping(value = "cars/{id}", produces = "application/json")
    public ResponseEntity<Car> get(
            @ApiParam(
                    name = "id",
                    value = "The unique identifier of the car by which the car will be returned.",
                    required = true,
                    example = "1")
            @PathVariable(name = "id") long id) {
        final Car car = carService.findCarById(id);
        return car != null ? new ResponseEntity<>(car, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Update Car by identifier in the database.", description = "Use this api endpoint to update a car by identifier in the database. " +
            "For a successful operation, you need a jwt token, which must be passed in the header using the key \"Authorization\". " +
            "Also send the model following validation in json format.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successful operation. Car by identifier updated."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Operation failed. Invalid data sent."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "You did not pass the token in the header, or the token has expired, or the token is not valid."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Failed operation. Car by identifier not found."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Car with the same fields as 'brand', 'model', 'bodyType', 'year', 'transmissionType', 'engineSize' already exist.")
    })
    @PutMapping(value = "cars/{id}", produces = "application/json")
    @PreAuthorize("hasAuthority('cars:update')")
    public ResponseEntity<Car> update(
            @ApiParam(
                    name = "id",
                    value = "The unique identifier of the car by which the car will be updated.",
                    required = true,
                    example = "1")
            @PathVariable Long id, @Parameter(name = "body", description = "To successfully receive a response from the api," +
            " you should send the body according to the example, follow the CarDTO validation.") @Valid @RequestBody CarDTO carDto) {
        try {
            final Car car = carService.updateCarById(id, carDto);

            return car != null ? new ResponseEntity<>(car, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (DataIntegrityViolationException e){
            return new ResponseEntity(HttpStatus.CONFLICT);
        }




        //return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @Operation(summary = "Delete Car by identifier in the database.", description = "Use this api endpoint to delete a car by identifier from the database. " + "For a successful operation, you need a jwt token, which must be passed in the header using the key \"Authorization\".",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successful operation. Car by identifier has been removed from the database."),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "You did not pass the token in the header," +
                            " or the token has expired, or the token is not valid."),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Failed operation. Car by identifier not found.")
            })
    @DeleteMapping(value = "cars/{id}", produces = "application/json")
    @PreAuthorize("hasAuthority('cars:delete')")
    public ResponseEntity<Void> delete(
            @ApiParam(
                    name = "id",
                    value = "The unique identifier of the car by which the car will be delete.",
                    required = true,
                    example = "1")
            @PathVariable(name = "id") long id) {
        final boolean deleted = carService.deleteCarById(id);

        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Upload car image by car identifier in database.", description = "Use this API endpoint to upload the car image by identifier." +
            "For a successful operation, a jwt token is required, which must be passed in the header using the \"Authorization\" key.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successful operation. The image of the car by identifier has been changed."),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "You did not pass the token in the header," +
                            " or the token has expired, or the token is not valid."),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Failed operation. Car by identifier not found.")
            })
    @PreAuthorize("hasAnyAuthority('cars:create','cars:update')")
    @PostMapping(value = "cars/{id}/uploadImage", consumes = "multipart/form-data")
    public ResponseEntity<Void> uploadImage(@PathVariable @ApiParam(
            name = "id",
            value = "The unique identifier of the car, by which the image will be uploaded.",
            required = true,
            example = "1") Long id, @Parameter(name = "image", description = "The new image of the car that will" +
            " be changed for the car whose identifier was specified.") @RequestPart() MultipartFile image) {
        boolean imageUploaded = carService.uploadImageCarById(id, image);
        return imageUploaded ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
